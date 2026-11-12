# ai/skills/

Krátké, nástroj-agnostické návody typu "jak na to". Jsou psané jako prostý Markdown, aby je
mohl použít jakýkoli AI nástroj (Claude Code, Codex, ...) i člověk.

## Konvence pro skill

- Rozsah: 1–2 strany, jedno téma na jeden soubor.
- Struktura: **Proč** (WHY) → **Co** (WHAT) → **Jak** (HOW) → krátký ukázkový příklad.
- Soubor pojmenovaný `kebab-case.md`, podle potřeby ve vlastní podsložce podle kategorie
  (např. `ai/skills/testing/...`, `ai/skills/development/...`).
- Žádná provozní znalost navíc mimo tento soubor — pokud se skill zmiňuje v `CLAUDE.md` nebo
  jiném nástroj-specifickém souboru, tam smí být jen odkaz, ne duplicitní obsah.

Viz [`_template.md`](_template.md) pro šablonu nového skillu.
