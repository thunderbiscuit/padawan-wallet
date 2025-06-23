import os
import yaml
import xml.sax.saxutils as saxutils

INPUT_DIR = ".."
OUTPUT_DIR = "../android"

os.makedirs(OUTPUT_DIR, exist_ok=True)

# Step 1: verify that all keys are the same across languages

# Store all keys by language
all_keys_by_lang = {}
all_keys = set()

# Load and collect keys
for filename in os.listdir(INPUT_DIR):
    if filename.endswith(".yaml"):
        lang_code = filename.split(".")[0]
        filepath = os.path.join(INPUT_DIR, filename)

        with open(filepath, 'r', encoding='utf-8') as f:
            strings = yaml.safe_load(f) or {}

        all_keys_by_lang[lang_code] = set(strings.keys())
        all_keys.update(strings.keys())

# Check for missing keys
has_error = False
for lang, keys in all_keys_by_lang.items():
    missing = all_keys - keys
    extra = keys - all_keys
    if missing:
        has_error = True
        print(f"❌ {lang}.yaml is missing keys: {sorted(missing)}")
    if extra:
        print(f"⚠️ {lang}.yaml has extra keys: {sorted(extra)}")

if has_error:
    print("🚫 Key mismatch found. Fix your YAML files before generating output.")
    exit(1)

# Step 2: Build the XML files from the yaml files
def escape_android_string(s):
    return saxutils.escape(s).replace("'", "\\'").replace('\n', '\\n')

def generate_strings_xml(lang_code, strings_dict):
    lines = ['<?xml version="1.0" encoding="utf-8"?>']
    lines.append('<resources>')
    for key, value in strings_dict.items():
        escaped_value = escape_android_string(value)
        lines.append(f'    <string name="{key}">{escaped_value}</string>')
    lines.append('</resources>')
    return '\n'.join(lines)

for filename in os.listdir(INPUT_DIR):
    if filename.endswith(".yaml"):
        lang_code = filename.split(".")[0]
        filepath = os.path.join(INPUT_DIR, filename)

        with open(filepath, 'r', encoding='utf-8') as f:
            strings = yaml.safe_load(f)

        android_folder = "values" if lang_code == "en" else f"values-{lang_code}"
        output_path = os.path.join(OUTPUT_DIR, android_folder)
        os.makedirs(output_path, exist_ok=True)

        xml_content = generate_strings_xml(lang_code, strings)
        with open(os.path.join(output_path, "strings.xml"), "w", encoding="utf-8") as out_file:
            out_file.write(xml_content)

        print(f"✔ Generated: {output_path}/strings.xml")
