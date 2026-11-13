# FR-LLM-003 — Lokální validace výstupu LLM

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — FR-LLM-003
**Priorita:** MUST / MVP

## Popis

[NAVRŽENO] Aplikace lokálně validuje výsledek vůči schématu šablony a chyby jasně signalizuje; chybné schéma se nesmí tvářit jako úspěšně dokončené zpracování.

## Akceptační kritéria

- [ ] Výstup nevalidní vůči schématu je validátorem odmítnut a uživatel jej nedostane jako schválený dokument (AT-005).

## Související

- Use-case: [UC-004](../use-cases/UC-004-strukturovat-text-podle-sablony.md)
- ADR: [ADR-006](../decisions/ADR-006-schema-sablony-je-kontrakt.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
