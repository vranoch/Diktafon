# NFR-REL-002 — Perzistence stavu workflow

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — NFR-REL-002
**Priorita:** MUST

## Popis

[NAVRŽENO] Stav workflow se perzistuje; opakování po pádu zařízení vede k bezpečné obnově nebo k novému řízenému požadavku, nikoliv k nedefinovanému stavu.

## Akceptační kritéria

- [ ] Po ztrátě sítě nebo restartu aplikace během STT zůstává úloha bezpečně znovu spustitelná (AT-003).

## Související

- Use-case: [UC-003](../use-cases/UC-003-prevest-nahravku-na-cesky-text.md), [UC-008](../use-cases/UC-008-obnovit-prerusene-zpracovani.md)
- ADR: [ADR-005](../decisions/ADR-005-lokalni-perzistence-vsech-mezivysledku.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
