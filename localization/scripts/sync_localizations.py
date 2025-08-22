#!/usr/bin/env python3
"""
This script ensures all localization YAML files follow the same structure as en.yaml:
1. All keys from en.yaml exist in other files
2. No extra keys exist in other files that don't exist in en.yaml
3. Keys are sorted in the exact same order as en.yaml
"""

import os
import re
import sys
from pathlib import Path
from typing import Dict, List, Tuple, Optional


class LocalizationSync:
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
    
    def sync_file(self, target_file: Path, reference_structure: List[Tuple[str, str, str]], 
                  reference_keys: Dict[str, str]) -> bool:
        """
        Sync a target file to match the reference structure.
        Returns True if changes were made.
        """
        target_structure, target_keys = self.parse_yaml_structure(target_file)
        
        # Check if sync is needed
        missing_keys = set(reference_keys.keys()) - set(target_keys.keys())
        extra_keys = set(target_keys.keys()) - set(reference_keys.keys())
        
        # Check if order matches (by comparing key sequence)
        ref_key_order = [key for line_type, key, content in reference_structure if line_type == 'key_value']
        target_key_order = [key for line_type, key, content in target_structure if line_type == 'key_value']
        
        order_matches = ref_key_order == target_key_order[:len(ref_key_order)]
        keys_match = not missing_keys and not extra_keys
        
        if keys_match and order_matches:
            print(f"✅ {target_file.name}: Already in sync")
            return False
            
        print(f"🔄 {target_file.name}: Syncing...")
        
        if missing_keys:
            print(f"  - Adding missing keys: {', '.join(sorted(missing_keys))}")
        if extra_keys:
            print(f"  - Removing extra keys: {', '.join(sorted(extra_keys))}")
        if not order_matches:
            print(f"  - Reordering keys to match reference")
            
        # Build new content following reference structure
        new_content = []
        
        # Get language comment from original file if it exists
        language_comment = None
        for line_type, key, content in target_structure:
            if line_type == 'comment' and 'Language:' in content:
                language_comment = content
                break
                
        # Process reference structure
        for line_type, key, content in reference_structure:
            if line_type == 'comment':
                if 'Language:' in content and language_comment:
                    # Use target's language comment instead of reference
                    new_content.append(language_comment)
                else:
                    new_content.append(content)
            elif line_type == 'empty':
                new_content.append('')
            elif line_type == 'key_value':
                if key in target_keys:
                    # Use existing translation
                    # Reconstruct line with same format as reference but target value
                    ref_match = re.match(r'^(\s*)([^:]+):\s*(.*)$', content)
                    if ref_match:
                        indent, _, _ = ref_match.groups()
                        # Find the original line in target to preserve its value formatting
                        target_value = target_keys[key]
                        new_line = f"{indent}{key}:{' ' * (max(1, len(content.split(':')[0]) + 1 - len(key) - len(indent)))}{target_value}"
                        new_content.append(new_line)
                    else:
                        new_content.append(content)  # Fallback
                else:
                    # Missing key - add placeholder with reference format
                    new_content.append(f"{content}  # TODO: Translate")
            else:
                new_content.append(content)
        
        # Write the synchronized file
        with open(target_file, 'w', encoding='utf-8') as f:
            for line in new_content:
                f.write(line + '\n')
                
        return True
    
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
        """Run the synchronization process."""
        print("\nPadawan Wallet Localization Sync")
        print("=" * 90)
        
        # Validate reference file
        if not self.validate_reference_file():
            return False,
            
        # Load reference structure
        reference_structure, reference_keys = self.parse_yaml_structure(self.reference_file)
        
        # Get target files
        target_files = self.get_yaml_files()
        if not target_files:
            print("No localization files found to sync")
            return True
            
        print(f"Found {len(target_files)} localization files to check")
        print()
        
        # Sync each file
        changes_made = False
        for target_file in target_files:
            if self.sync_file(target_file, reference_structure, reference_keys):
                changes_made = True
                
        print()
        if changes_made:
            print("✅ Synchronization completed with changes")
        else:
            print("✅ All files already synchronized")
            
        return True


def main():
    """Main entry point."""
    script_dir = Path(__file__).parent
    sync_tool = LocalizationSync(script_dir.parent)
    
    try:
        success = sync_tool.run()
        sys.exit(0 if success else 1)
    except KeyboardInterrupt:
        print("\nInterrupted by user")
        sys.exit(1)
    except Exception as e:
        print(f"\n❌ Unexpected error: {e}")
        sys.exit(1)


if __name__ == "__main__":
    main()
