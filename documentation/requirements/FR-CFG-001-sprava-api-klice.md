# FR-CFG-001 — Správa API klíče

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — FR-CFG-001
**Priorita:** MUST / MVP

## Popis

[NAVRŽENO] Uživatel může zadat, ověřit, změnit a smazat API klíč, aniž by byl klíč kdykoliv zobrazen v logu.

## Akceptační kritéria

- [ ] Zadání/změna/smazání API klíče je dostupné z nastavení.
- [ ] Ověření/odebrání klíče nezanechá hodnotu klíče v běžných logách (AT-012).

## Související

- Use-case: [UC-003](../use-cases/UC-003-prevest-nahravku-na-cesky-text.md), [UC-007](../use-cases/UC-007-nastavit-externi-api.md)
- ADR: [ADR-002](../decisions/ADR-002-bez-vlastni-serverove-casti-v-mvp.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
