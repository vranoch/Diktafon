# FR-TPL-003 — Zobrazení aktivní šablony a záznam její verze

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — FR-TPL-003
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — zobrazení obsahu šablony před zpracováním je potvrzeně volitelné, ne povinný krok.
**Priorita:** MUST / MVP

## Popis

[POTVRZENO] Před zpracováním lze zobrazit aktivní šablonu, ale není to povinný krok workflow — hlavní kontrolní bod je až výsledný dokument (viz [UC-004](../use-cases/UC-004-strukturovat-text-podle-sablony.md)). Výsledný strukturovaný dokument vždy zaznamená ID a verzi šablony, podle které vznikl.

## Akceptační kritéria

- [ ] Detail výsledného dokumentu obsahuje ID a verzi použité šablony (neměnné i při pozdější aktualizaci šablony).
- [ ] Zpracování lze spustit (potvrdit) bez nutnosti si předtím zobrazit obsah šablony.

## Související

- Use-case: [UC-004](../use-cases/UC-004-strukturovat-text-podle-sablony.md)
- ADR: [ADR-006](../decisions/ADR-006-schema-sablony-je-kontrakt.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
