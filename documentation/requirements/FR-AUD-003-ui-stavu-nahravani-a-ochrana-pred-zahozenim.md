# FR-AUD-003 — UI stavu nahrávání a ochrana před zahozením

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — FR-AUD-003
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — rozšíření o Pauza/Pokračovat; technický způsob ukládání je implementační detail, ne požadavek.
**Priorita:** MUST / MVP

## Popis

[POTVRZENO] UI jednoznačně ukazuje stav nahrávání a jeho trvání a umožňuje ovládání **Start / Pauza / Pokračovat (append) / Stop**, nikoliv jen Start/Stop. Jedna nahrávka tak může vzniknout postupně z více částí (uživatel nahrávání pozastaví a později ve stejné nahrávce pokračuje), a nedovolí náhodné zahození rozpracované nahrávky bez potvrzení.

Technický způsob uložení částí (append do jednoho souboru za běhu, nebo dílčí segmenty/soubory slučované až při Stop) je záměrně ponechán na implementaci — požadavek se týká výhradně uživatelské funkce (Start/Pauza/Pokračovat/Stop nad jednou nahrávkou), ne konkrétního úložného mechanismu.

Pozn.: nejde o totéž co [FR-AUD-009](FR-AUD-009-spojovani-epizod-do-jednoho-zaznamu.md) — tam jde o dodatečné spojení více už dokončených, samostatných nahrávek/epizod při zachování původních souborů; zde jde o pauzu/pokračování v rámci jedné stále rozpracované nahrávky.

## Akceptační kritéria

- [ ] Během nahrávání je viditelný indikátor běhu a uběhlý čas.
- [ ] Z běžícího nahrávání lze přejít do Pauzy a z Pauzy zpět do nahrávání (append) v rámci téže nahrávky.
- [ ] Pozastavená nahrávka je zachována a lze v ní pokračovat i po jejím dočasném přerušení (viz [FR-AUD-004](FR-AUD-004-lokalni-ulozeni-audia-pred-odeslanim-do-stt.md)).
- [ ] Pokus o opuštění/zrušení rozpracovaného nahrávání (včetně pozastaveného) vyžaduje explicitní potvrzení.

## Související

- Use-case: [UC-001](../use-cases/UC-001-poridit-a-ulozit-hlasovy-zaznam.md)
- ADR: (žádný)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
