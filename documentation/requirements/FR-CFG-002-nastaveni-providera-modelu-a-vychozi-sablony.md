# FR-CFG-002 — Nastavení providera/modelu a výchozí šablony

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — FR-CFG-002
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — doplněn výchozí jazyk nahrávky/přepisu (čeština), viz [FR-STT-001](FR-STT-001-odeslani-audia-do-stt-a-ulozeni-prepisu.md).
**Priorita:** MUST / MVP

## Popis

[NAVRŽENO] Lze nastavit poskytovatele/model pro STT a LLM, zvolit výchozí šablonu a výchozí jazyk nahrávky/přepisu (výchozí hodnota čeština), který lze pro jednotlivou nahrávku přepsat.

## Akceptační kritéria

- [ ] Nastavení umožňuje zvolit providera a model pro STT i LLM odděleně a nastavit výchozí šablonu.
- [ ] Nastavení umožňuje zvolit výchozí jazyk přepisu; výchozí hodnota je čeština.

## Související

- Use-case: [UC-007](../use-cases/UC-007-nastavit-externi-api.md)
- ADR: [ADR-004](../decisions/ADR-004-vymenitelni-externi-ai-poskytovatele.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
