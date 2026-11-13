# NFR-UX-002 — E Ink optimalizace UI

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — NFR-UX-002
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — zvětšování textu a dostatečně velké/jasné ovládací prvky jsou závazné pro standardní použití, u administrace nejsou nutné.
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — zpřesněno na tři úrovně použití dle [ADR-010](../decisions/ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md).
**Priorita:** MUST

## Popis

[POTVRZENO] E Ink optimalizace: vysoký kontrast, velké a jasné ovládací prvky, minimum animací a rychlých překreslení. Text musí jít kvůli lepší čitelnosti zvětšit. Tyto požadavky (zvětšitelný text, dostatečně velké a jasné ovládací prvky) platí závazně pro **běžné použití** aplikace (nahrávání, přehled nahrávek, revize přepisu/dokumentu, export — viz [ADR-010](../decisions/ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md)). U **rozšířeného použití** (organizace do složek, mazání) mohou být o něco kompaktnější. Pro **administraci** (nastavení, správa API klíčů/providerů, technická správa šablon) nejsou závazné a mohou mít kompaktnější UI.

## Akceptační kritéria

- [ ] Čitelnost a ovladatelnost (prstem i perem) je ověřena zkouškou na cílovém zařízení (AT-013).
- [ ] V obrazovkách běžného použití lze velikost zobrazovaného textu zvětšit.
- [ ] Ovládací prvky v obrazovkách běžného použití jsou dostatečně velké a jasné pro snadné použití.
- [ ] Riziko R-009 — příliš malé/pomalé E Ink UI; mitigace: prototyp na cílovém BOOX, omezení animací, úprava interakcí.

## Související

- Use-case: (žádný)
- ADR: [ADR-010](../decisions/ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
