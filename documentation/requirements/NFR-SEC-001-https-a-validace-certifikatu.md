# NFR-SEC-001 — HTTPS a validace certifikátu

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — NFR-SEC-001
**Priorita:** MUST

## Popis

[NAVRŽENO] Komunikace s externími API pouze přes HTTPS s validací certifikátu; žádné plaintext HTTP ani logování klíčů.

## Akceptační kritéria

- [ ] Veškerá komunikace s STT/LLM providery probíhá výhradně přes HTTPS s platnou validací certifikátu.

## Související

- Use-case: [UC-007](../use-cases/UC-007-nastavit-externi-api.md)
- ADR: (žádný)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
