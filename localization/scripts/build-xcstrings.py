import yaml
import json
import os
from glob import glob


SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
INPUT_DIR = os.path.join(SCRIPT_DIR, "..")
OUTPUT_FILE = os.path.join(SCRIPT_DIR, "..", "ios", "Localizable.xcstrings")

def load_all_languages(input_dir):
    strings = {}
    languages = []

    for filepath in glob(os.path.join(input_dir, "*.yaml")):
        lang_code = os.path.splitext(os.path.basename(filepath))[0]
        languages.append(lang_code)

        with open(filepath, "r", encoding="utf-8") as f:
            lang_data = yaml.safe_load(f)

        for key, value in lang_data.items():
            if key not in strings:
                strings[key] = {}
            strings[key][lang_code] = value

    return strings, languages

def build_xcstrings(strings, source_language):
    return {
        "version": "1.0",
        "sourceLanguage": source_language,
        "strings": {
            key: {
                "localizations": {
                    lang: { "stringUnit": { "value": text, "state": "translated" } }
                    for lang, text in translations.items()
                }
            }
            for key, translations in strings.items()
        }
    }

def main():
    strings, languages = load_all_languages(INPUT_DIR)
    source_lang = "en" if "en" in languages else languages[0]

    xcstrings = build_xcstrings(strings, source_lang)

    with open(OUTPUT_FILE, "w", encoding="utf-8") as f:
        json.dump(xcstrings, f, ensure_ascii=False, indent=2)

    print(f"✅ Wrote {OUTPUT_FILE} with {len(strings)} keys for languages: {', '.join(languages)}")

if __name__ == "__main__":
    main()
