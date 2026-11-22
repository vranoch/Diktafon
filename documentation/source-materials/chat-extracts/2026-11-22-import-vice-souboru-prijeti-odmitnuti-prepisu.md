# Chat-extract — Import více souborů, přijetí/odmítnutí přepisu, přejmenování pole šablony

**Datum:** 2026-11-22

## Kontext

Uživatel po vyzkoušení aplikace nahlásil tři problémy ze skutečného používání:

1. Import externí nahrávky: po klepnutí na soubor v systémovém dialogu "se na první pohled nic
   nestane", takže uživatel klepal opakovaně — na pozadí se ale import vždy provedl, takže
   opakované klepání založilo několik nahrávek vedle sebe.
2. Obrazovka Přepis: po dokončení přepisu se text rovnou uložil bez možnosti ho
   přijmout/odmítnout a obrazovka neměla žádné tlačítko posouvající proces dál — jediná cesta
   pryč byla navigační šipka zpět/domů.
3. V editaci šablony je pole pro zadání uživatelského nadpisu pole nazváno "Název pole", což je
   matoucí vedle technického "name"/"key" rozlišení zavedeného v ADR-006.

## Rozhodnutí

1. **Import:** systémový file picker přepnut z `ActivityResultContracts.OpenDocument()` (výběr
   jednoho souboru, bez explicitního potvrzení) na `OpenMultipleDocuments()` — systémový dialog
   tak umožňuje zaškrtnout jeden nebo více souborů a výběr potvrdit tlačítkem v dialogu (nebo
   zrušit), čímž mizí nejednoznačnost z bodu 1. Tím se zároveň řeší dříve otevřená otázka v
   [UC-002](../../use-cases/UC-002-importovat-externi-nahravku.md) (`TODO`, zda lze vybrat více
   souborů najednou) — ANO, lze; každý vybraný soubor se importuje jako samostatná nahrávka
   (název = zadaný název + pořadové číslo, pokud je souborů víc). Navigace po dokončení (zpět do
   složky, ve které byl import spuštěn) už fungovala správně a nebylo nutné ji měnit.
2. **Přepis:** čerstvý výsledek STT se už neukládá automaticky — zůstává jako nepotvrzený návrh,
   dokud uživatel nezvolí "Přijmout přepis" (uloží se jako nová revize) nebo "Odmítnout" (text se
   vrátí na předchozí uloženou hodnotu, nebo zůstane prázdný, pokud žádná předchozí revize
   neexistovala). Obě akce vrátí uživatele na předchozí obrazovku (Detail nahrávky). Ruční editace
   už dříve uloženého přepisu (tlačítka "Uložit úpravy"/"Zrušit úpravy") zůstává beze změny coby
   samostatný, navazující krok.
3. **Šablona:** popisek pole v editoru přejmenován z "Název pole" na "Nadpis pole".

## Zdroje

- Tato konverzace, 2026-11-22.
