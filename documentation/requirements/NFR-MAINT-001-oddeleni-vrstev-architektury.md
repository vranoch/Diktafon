# NFR-MAINT-001 — Oddělení vrstev architektury

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — NFR-MAINT-001
**Priorita:** MUST

## Popis

[NAVRŽENO] Oddělení UI, workflow, domény, úložiště a provider adaptérů; modely a endpointy jsou konfigurovatelné.

## Akceptační kritéria

- [ ] Provider adaptéry (STT, LLM) jsou vyměnitelné bez zásahu do UI/domény/perzistence.

## Související

- Use-case: (žádný)
- ADR: [ADR-004](../decisions/ADR-004-vymenitelni-externi-ai-poskytovatele.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
