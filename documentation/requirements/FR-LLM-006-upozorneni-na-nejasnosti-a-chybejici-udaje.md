# FR-LLM-006 — Upozornění na nejasnosti a chybějící údaje

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — FR-LLM-006
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — nejisté/chybějící hodnoty musí být nepřehlédnutelné, viz [FR-LLM-004](FR-LLM-004-zakaz-domysleni-faktu-v-promptu.md).
**Priorita:** MUST / MVP

## Popis

[POTVRZENO] Nejasnosti a chybějící povinné údaje se uvádějí odděleně od samotných strukturovaných dat a zároveň jsou přímo u dotčeného pole vizuálně označené, aby je uživatel při kontrole v žádném případě nepřehlédl.

## Akceptační kritéria

- [ ] Výstup strukturování zobrazuje samostatný seznam nejasností/chybějících povinných polí vedle vyplněných dat.
- [ ] Každé pole uvedené v tomto seznamu je zároveň viditelně označené přímo v místě, kde se v dokumentu nachází.

## Související

- Use-case: [UC-004](../use-cases/UC-004-strukturovat-text-podle-sablony.md)
- ADR: [ADR-006](../decisions/ADR-006-schema-sablony-je-kontrakt.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
