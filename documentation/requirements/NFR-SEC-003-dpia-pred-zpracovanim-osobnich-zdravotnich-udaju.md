# NFR-SEC-003 — DPIA před zpracováním osobních/zdravotních údajů

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — NFR-SEC-003
**Priorita:** MUST

## Popis

[NAVRŽENO] Před zpracováním osobních/zdravotních údajů se posoudí DPIA/role stran, právní základ, smlouvy, lokace, retence a přenosy dat; samotné HTTPS nestačí.

TODO: ověřit smluvní podmínky, subprocesory, region zpracování a právní základ před použitím reálných citlivých dat (Q-010, ASM-010). Riziko R-008 — retence osobních/zdravotních dat u AI API.

## Akceptační kritéria

- [ ] Do dokončení právního/bezpečnostního posouzení se zpracovávají pouze anonymizované nebo syntetické testovací nahrávky.

## Související

- Use-case: [UC-009](../use-cases/UC-009-vymazat-vlastni-data.md)
- ADR: (žádný)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
