# NFR-REL-001 — Zachování dat při selhání AI volání

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — NFR-REL-001
**Priorita:** MUST

## Popis

[NAVRŽENO] Odeslání, timeout ani selhání AI volání (STT/LLM) nesmí smazat originální audio ani potvrzené uživatelské editace.

## Akceptační kritéria

- [ ] Selhání/timeout AI volání (AT-004) nevede ke ztrátě audia ani dříve potvrzených editací.

## Související

- Use-case: [UC-001](../use-cases/UC-001-poridit-a-ulozit-hlasovy-zaznam.md), [UC-008](../use-cases/UC-008-obnovit-prerusene-zpracovani.md)
- ADR: [ADR-005](../decisions/ADR-005-lokalni-perzistence-vsech-mezivysledku.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
