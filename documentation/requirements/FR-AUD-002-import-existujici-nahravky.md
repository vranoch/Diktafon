# FR-AUD-002 — Import existující nahrávky

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-22
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — FR-AUD-002
- [Chat-extract — Import více souborů, přijetí/odmítnutí přepisu, přejmenování pole šablony, 2026-11-22](../source-materials/chat-extracts/2026-11-22-import-vice-souboru-prijeti-odmitnuti-prepisu.md)
**Priorita:** MUST / MVP

## Popis

[POTVRZENO] Uživatel může importovat existující nahrávku z úložiště nebo externího diktafonu prostřednictvím systémového výběru souboru (Storage Access Framework). Výběr v systémovém dialogu umožňuje zvolit i více souborů najednou a vyžaduje explicitní potvrzení (nebo zrušení) tlačítkem v dialogu — každý vybraný soubor se importuje jako samostatná nahrávka.

## Akceptační kritéria

- [ ] Import přes systémový file picker je dostupný z hlavní obrazovky (AT-002).
- [ ] V systémovém dialogu lze vybrat jeden nebo více souborů a výběr explicitně potvrdit nebo zrušit — nejde jen o klepnutí na soubor bez jasné zpětné vazby.
- [ ] Po odebrání externího média (USB/karta, ze které byl soubor importován) lze import stále zpracovat.

## Související

- Use-case: [UC-002](../use-cases/UC-002-importovat-externi-nahravku.md)
- ADR: (žádný)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
