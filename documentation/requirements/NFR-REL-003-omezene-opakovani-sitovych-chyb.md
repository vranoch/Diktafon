# NFR-REL-003 — Omezené opakování síťových chyb

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — NFR-REL-003
**Priorita:** SHOULD

## Popis

[NAVRŽENO] Opakování síťových chyb s omezeným backoffem; ochrana před nechtěným vícenásobným placeným voláním.

## Akceptační kritéria

- [ ] Před opakovaným placeným voláním po obnově je uživatel upozorněn na možnost dvojího účtování (viz [UC-008](../use-cases/UC-008-obnovit-prerusene-zpracovani.md)).

## Související

- Use-case: [UC-008](../use-cases/UC-008-obnovit-prerusene-zpracovani.md)
- ADR: (žádný)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
