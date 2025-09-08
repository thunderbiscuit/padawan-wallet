#!/usr/bin/env python3
"""
This script validates that all localization YAML files have the same keys as en.yaml:
1. All keys from en.yaml must exist in other files
2. No extra keys should exist in other files that don't exist in en.yaml
3. Throws errors if mismatches are found instead of auto-fixing them
"""

import os
import re
import sys
from pathlib import Path
from typing import Dict, List, Tuple, Optional


class LocalizationValidator:
    def __init__(self, base_dir: str = "../"):
        self.base_dir = Path(base_dir)
        self.reference_file = self.base_dir / "en.yaml"
        
    def parse_yaml_structure(self, file_path: Path) -> Tuple[List[Tuple[str, str, str]], Dict[str, str]]:
        """
        Parse a YAML file and return:
        1. List of (line_type, key, content) tuples preserving order and structure
        2. Dictionary of key-value pairs for quick lookups
        
        line_type can be: 'comment', 'empty', 'key_value'
        """
        if not file_path.exists():
            return [], {}
            
        structure = []
        key_values = {}
        
        with open(file_path, 'r', encoding='utf-8') as f:
            for line in f:
                original_line = line.rstrip('\n\r')
                stripped = line.strip()
                
                if not stripped:
                    # Empty line
                    structure.append(('empty', '', original_line))
                elif stripped.startswith('#'):
                    # Comment line
                    structure.append(('comment', '', original_line))
                elif ':' in stripped and not stripped.startswith('#'):
                    # Key-value pair
                    # Handle the specific format: key: "value" or key: value
                    match = re.match(r'^(\s*)([^:]+):\s*(.*)$', original_line)
                    if match:
                        indent, key, value = match.groups()
                        key = key.strip()
                        structure.append(('key_value', key, original_line))
                        key_values[key] = value.strip()
                else:
                    # Treat as comment or preserve as-is
                    structure.append(('other', '', original_line))
                    
        return structure, key_values
    
    def get_yaml_files(self) -> List[Path]:
        """Get all YAML files in the directory except the reference file."""
        yaml_files = []
        for file_path in self.base_dir.glob("*.yaml"):
            if file_path.name != "en.yaml":
                yaml_files.append(file_path)
        return sorted(yaml_files)
    
    def validate_file(self, target_file: Path, reference_structure: List[Tuple[str, str, str]], 
                      reference_keys: Dict[str, str]) -> None:
        """
        Validate a target file against the reference structure.
        Raises an exception if keys are missing or extra.
        """
        target_structure, target_keys = self.parse_yaml_structure(target_file)
        
        # Check for missing and extra keys
        missing_keys = set(reference_keys.keys()) - set(target_keys.keys())
        extra_keys = set(target_keys.keys()) - set(reference_keys.keys())
        
        errors = []
        
        if missing_keys:
            errors.append(f"Missing keys: {', '.join(sorted(missing_keys))}")
        
        if extra_keys:
            errors.append(f"Extra keys: {', '.join(sorted(extra_keys))}")
        
        if errors:
            error_message = f"{target_file.name} has localization key mismatches:\n"
            for error in errors:
                error_message += f"  - {error}\n"
            raise ValueError(error_message.rstrip())
        
        print(f"✅ {target_file.name}: Keys are in sync")
    
    def validate_reference_file(self) -> bool:
        """Validate that the reference file exists and is readable."""
        if not self.reference_file.exists():
            print(f"❌ Reference file {self.reference_file} not found!")
            return False
            
        try:
            structure, keys = self.parse_yaml_structure(self.reference_file)
            if not keys:
                print(f"\n❌ Reference file {self.reference_file} contains no key-value pairs!")
                return False
            print(f"Reference file {self.reference_file} loaded with {len(keys)} keys")
            return True
        except Exception as e:
            print(f"❌ Error reading reference file: {e}")
            return False
    
    def run(self) -> bool:
        """Run the validation process."""
        print("\nPadawan Wallet Localization Validator")
        print("=" * 90)
        
        # Validate reference file
        if not self.validate_reference_file():
            return False
            
        # Load reference structure
        reference_structure, reference_keys = self.parse_yaml_structure(self.reference_file)
        
        # Get target files
        target_files = self.get_yaml_files()
        if not target_files:
            print("No localization files found to validate")
            return True
            
        print(f"Found {len(target_files)} localization files to validate")
        print()
        
        # Validate each file
        validation_errors = []
        for target_file in target_files:
            try:
                self.validate_file(target_file, reference_structure, reference_keys)
            except ValueError as e:
                validation_errors.append(str(e))
                
        print()
        if validation_errors:
            print("❌ Validation failed with the following errors:")
            print()
            for error in validation_errors:
                print(error)
                print()
            print("Please manually add/remove the missing/extra keys and run the validator again.")
            return False
        else:
            print("✅ All localization files are valid")
            
        return True


def main():
    """Main entry point."""
    script_dir = Path(__file__).parent
    validator = LocalizationValidator(script_dir.parent)
    
    try:
        success = validator.run()
        sys.exit(0 if success else 1)
    except KeyboardInterrupt:
        print("\nInterrupted by user")
        sys.exit(1)
    except Exception as e:
        print(f"\n❌ Unexpected error: {e}")
        sys.exit(1)


if __name__ == "__main__":
    main()
