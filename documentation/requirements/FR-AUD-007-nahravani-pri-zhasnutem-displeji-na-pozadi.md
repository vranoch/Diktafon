# FR-AUD-007 — Nahrávání při zhasnutém displeji / na pozadí

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — FR-AUD-007
**Priorita:** SHOULD / MVP

## Popis

[OVĚŘIT] Záznam může pokračovat při zhasnutém displeji a po přechodu aplikace na pozadí. Schopnost se nesmí přislíbit bez testu na konkrétním BOOX firmwaru.

TODO: ověřit na cílovém zařízení (ASM-003, ASM-008) — konkrétní model BOOX, verze firmware, chování úsporných režimů a foreground service (AT-010). Riziko R-004 — BOOX uspí nahrávání či síťový požadavek; mitigace: pilotní test na HW, foreground service, persistované workflow.

## Akceptační kritéria

- [ ] Dlouhé nahrávání se zhasnutým displejem je změřeno na cílovém BOOX zařízení (AT-010); pokud není podporováno, je to v UI explicitně označeno jako nepodporované.

## Související

- Use-case: [UC-001](../use-cases/UC-001-poridit-a-ulozit-hlasovy-zaznam.md)
- ADR: (žádný)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
