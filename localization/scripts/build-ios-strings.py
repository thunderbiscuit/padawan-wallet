import yaml
import os
from glob import glob

SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
INPUT_DIR = os.path.join(SCRIPT_DIR, "..")
OUTPUT_DIR = os.path.join(SCRIPT_DIR, "..", "ios")

os.makedirs(OUTPUT_DIR, exist_ok=True)

def load_all_languages(input_dir):
    strings = {}
    languages = []

    for filepath in glob(os.path.join(input_dir, "*.yaml")):
        lang_code = os.path.splitext(os.path.basename(filepath))[0]
        languages.append(lang_code)

        with open(filepath, "r", encoding="utf-8") as f:
            lang_data = yaml.safe_load(f)

        strings[lang_code] = lang_data

    return strings, languages

def escape_string(value):
    """
    - Substitui quebras de linha reais por \n
    - Escapa aspas duplas, se houver.
    """
    return value.replace("\n", "\\n").replace('"', '\\"')

def write_strings_files(strings_per_language):
    for lang_code, translations in strings_per_language.items():
        output_file = os.path.join(OUTPUT_DIR, f"{lang_code}.lproj", "Localizable.strings")
        os.makedirs(os.path.dirname(output_file), exist_ok=True)

        with open(output_file, "w", encoding="utf-16") as f:
            for key, value in translations.items():
                escaped_value = escape_string(str(value))
                f.write(f'"{key}" = "{escaped_value}";\n')

        print(f"✅ Wrote {output_file} with {len(translations)} keys.")

def main():
    strings_per_language, languages = load_all_languages(INPUT_DIR)
    write_strings_files(strings_per_language)
    print(f"✅ Generated .strings files for languages: {', '.join(languages)}")

if __name__ == "__main__":
    main()