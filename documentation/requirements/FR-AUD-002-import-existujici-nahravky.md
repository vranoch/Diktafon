# FR-AUD-002 — Import existující nahrávky

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-22
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — FR-AUD-002
- [Chat-extract — Import více souborů, přijetí/odmítnutí přepisu, přejmenování pole šablony, 2026-11-22](../source-materials/chat-extracts/2026-11-22-import-vice-souboru-prijeti-odmitnuti-prepisu.md)
- [Chat-extract — Import: dialog nejde zavřít, přepnutí na ACTION_GET_CONTENT, filtr jako volba, 2026-11-22](../source-materials/chat-extracts/2026-11-22-import-dialog-nejde-zavrit-get-content-a-filtr.md)
**Priorita:** MUST / MVP

## Popis

[POTVRZENO] Uživatel může importovat existující nahrávku z úložiště nebo externího diktafonu prostřednictvím systémového výběru souboru (Storage Access Framework). Výběr v systémovém dialogu umožňuje zvolit i více souborů najednou a vyžaduje explicitní potvrzení (nebo zrušení) tlačítkem v dialogu — každý vybraný soubor se importuje jako samostatná nahrávka.

**Aktualizace 2026-11-22:** systémový požadavek přepnut z `ACTION_OPEN_DOCUMENT` na
`ACTION_GET_CONTENT` — ten nejdřív projde systémovým výběrem aplikace (chooser), který má vždy
tlačítko zpět/zavřít vykreslené samotným OS, na rozdíl od `ACTION_OPEN_DOCUMENT`, jenž jde rovnou
do jednoho konkrétního poskytovatele úložiště, jehož vlastní obrazovka na některých zařízeních
(pozorováno na BOOX/Onyx) může cestu zpět postrádat. Appka nemůže do cizí aplikace žádným
způsobem zasahovat ani jí vnutit vlastní UI — to je hranice platformy. Dále přidána volba v
Administraci (Připojení), zda picker filtruje jen audio soubory, nebo nabízí všechny (výchozí).

## Akceptační kritéria

- [ ] Import přes systémový file picker je dostupný z hlavní obrazovky (AT-002).
- [ ] V systémovém dialogu lze vybrat jeden nebo více souborů a výběr explicitně potvrdit nebo zrušit — nejde jen o klepnutí na soubor bez jasné zpětné vazby.
- [ ] Systémový dialog vždy nabízí cestu zpět/zrušit, i když konkrétní poskytovatel úložiště žádnou nemá (zajištěno volbou intentu, který nejdřív projde systémovým chooserem).
- [ ] V Administraci lze zvolit, zda picker nabízí jen audio soubory, nebo všechny.
- [ ] Po odebrání externího média (USB/karta, ze které byl soubor importován) lze import stále zpracovat.

## Související

- Use-case: [UC-002](../use-cases/UC-002-importovat-externi-nahravku.md)
- ADR: (žádný)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
