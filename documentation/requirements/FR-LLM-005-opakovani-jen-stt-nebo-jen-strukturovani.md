# FR-LLM-005 — Opakování jen STT nebo jen strukturování

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — FR-LLM-005
**Priorita:** SHOULD / MVP

## Popis

[NAVRŽENO] Uživatel může opakovat samostatně jen krok STT nebo jen krok strukturování a přitom použít dříve uložený přepis, aniž by musel nahrávku zpracovávat od začátku.

## Akceptační kritéria

- [ ] Opakování strukturování z existujícího přepisu nevyžaduje nové volání STT (AT-007).

## Související

- Use-case: [UC-004](../use-cases/UC-004-strukturovat-text-podle-sablony.md), [UC-008](../use-cases/UC-008-obnovit-prerusene-zpracovani.md)
- ADR: [ADR-003](../decisions/ADR-003-oddelene-kroky-stt-a-llm.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
