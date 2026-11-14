# NFR-UX-002 — E Ink optimalizace UI

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-14
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — NFR-UX-002
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — zvětšování textu a dostatečně velké/jasné ovládací prvky jsou závazné pro standardní použití, u administrace nejsou nutné.
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — zpřesněno na tři úrovně použití dle [ADR-010](../decisions/ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md).
- [Chat-extract 2026-11-14](../source-materials/chat-extracts/2026-11-14-ux-ui-principy-pro-e-ink.md) a [ADR-015](../decisions/ADR-015-principy-ui-ux-pro-e-ink-a-proces-navrhu-obrazovek.md) — doplnění konkrétních měřitelných čísel (dotyková plocha, velikost textu) místo jen kvalitativního popisu.
**Priorita:** MUST

## Popis

[POTVRZENO] E Ink optimalizace: vysoký kontrast, velké a jasné ovládací prvky, minimum animací a rychlých překreslení. Text musí jít kvůli lepší čitelnosti zvětšit. Tyto požadavky (zvětšitelný text, dostatečně velké a jasné ovládací prvky) platí závazně pro **běžné použití** aplikace (nahrávání, přehled nahrávek, revize přepisu/dokumentu, export — viz [ADR-010](../decisions/ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md)). U **rozšířeného použití** (organizace do složek, mazání) mohou být o něco kompaktnější. Pro **administraci** (nastavení, správa API klíčů/providerů, technická správa šablon) nejsou závazné a mohou mít kompaktnější UI.

Konkrétní měřitelné hodnoty (viz [ADR-015](../decisions/ADR-015-principy-ui-ux-pro-e-ink-a-proces-navrhu-obrazovek.md)):

- Minimální dotyková plocha ovládacích prvků: **48×48 dp** pro běžné a rozšířené použití, **40×40 dp** přípustné v administraci.
- Základní velikost textu: **min. 16 sp** pro běžné použití, **min. 14 sp** pro sekundární texty, rozšířené a administrační obrazovky; vše ve škálovatelných jednotkách (`sp`), ne pevných `px`/`dp`.
- Žádná kontinuální animace ani automatické překreslení rychlejší než **1× za sekundu** na obrazovkách běžného použití (žádný živý waveform, žádná pulzující indikace nahrávání — stav se ukazuje staticky).
- Barva není jediný nosič významu — stav/upozornění vždy nese i ikona a text.

## Akceptační kritéria

- [ ] Čitelnost a ovladatelnost (prstem i perem) je ověřena zkouškou na cílovém zařízení (AT-013).
- [ ] V obrazovkách běžného použití lze velikost zobrazovaného textu zvětšit (respektuje systémové škálování písma).
- [ ] Ovládací prvky v obrazovkách běžného a rozšířeného použití mají dotykovou plochu min. 48×48 dp; v administraci min. 40×40 dp.
- [ ] Základní text v běžném použití má min. 16 sp, sekundární/rozšířené/administrační texty min. 14 sp.
- [ ] Žádná obrazovka běžného použití neobsahuje kontinuální animaci ani automatické překreslení rychlejší než 1× za sekundu.
- [ ] Stav nebo upozornění nikde nespoléhá jen na barvu — vždy je doplněn ikonou a textem.
- [ ] Riziko R-009 — příliš malé/pomalé E Ink UI; mitigace: prototyp na cílovém BOOX, omezení animací, úprava interakcí.

## Související

- Use-case: (žádný)
- ADR: [ADR-010](../decisions/ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md), [ADR-015](../decisions/ADR-015-principy-ui-ux-pro-e-ink-a-proces-navrhu-obrazovek.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
