# NFR-PERF-001 — Limity délky/velikosti nahrávky

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — NFR-PERF-001
**Priorita:** MUST

## Popis

[OVĚŘIT] Podporované maximální trvání/velikost nahrávky odpovídá limitům vybraného STT API a paměti zařízení. Konkrétní limit je nutné stanovit testem (Q-003).

TODO: ověřit konkrétní limit podle zvoleného STT API a reálných nahrávek. Riziko R-006 — velký audio soubor přesáhne API limit; mitigace: předběžná validace, vhodný kodek, případné segmentování v NEXT.

## Akceptační kritéria

- [ ] Nahrávka přesahující stanovený limit je před odesláním do STT rozpoznána a uživateli srozumitelně oznámena.

## Související

- Use-case: [UC-002](../use-cases/UC-002-importovat-externi-nahravku.md)
- ADR: (žádný)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
