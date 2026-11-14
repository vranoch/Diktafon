# ADR-015 — Principy UI/UX pro E Ink a proces návrhu obrazovek

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-14
**Změněno:** 2026-11-14 (doplněny odkazy z use-case souborů po schválení přepracovaného mockupu)
**Zdroje:**
- [Chat-extract 2026-11-14](../source-materials/chat-extracts/2026-11-14-ux-ui-principy-pro-e-ink.md) — zpětná vazba uživatele, že dosavadní mockup nerespektoval dost důsledně NFR-UX-001/002 a ADR-010, a rozhodnutí o cílovém zařízení, konkrétních číslech a způsobu záznamu UX návrhu.

## Kontext

Několik kol grafického mockupu (mimo implementaci, viz [ai/skills](../../ai/skills/) a Design
artefakt) ukázalo, že iterativní ladění jednotlivých obrazovek jen podle průběžné zpětné vazby
vede k nekonzistentnímu výsledku, který navíc nerespektuje už existující požadavky:

- [NFR-UX-001](../requirements/NFR-UX-001-jednoduche-ux-pro-netechnickeho-uzivatele.md) a
  [ADR-010](ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md) žádají, aby se tři
  úrovně použití (běžné/rozšířené/administrační) lišily nejen umístěním v navigaci, ale i
  hustotou a nápadností UI.
- [NFR-UX-002](../requirements/NFR-UX-002-e-ink-optimalizace-ui.md) je pro běžné použití MUST:
  vysoký kontrast, velké a jasné ovládací prvky, minimum animací a rychlých překreslení,
  zvětšitelný text — ale bez konkrétních čísel to nejde ověřit ani vynutit.
- Cílové zařízení (ASM-003 ve zdrojovém dokumentu) je BOOX Go 10.3 Gen II Lumi — 10,3" **šedá
  (grayscale) E Ink** tableta ovládaná perem i prstem, jeden uživatel. Barva na takovém displeji
  nenese spolehlivě význam, kontinuální animace a časté překreslení jsou na E Ink nápadné a
  rušivé (riziko R-009).

Řešilo se, zda tato pravidla:
(a) nechat jen kvalitativní a ověřit až testem na reálném zařízení (AT-013), nebo
(b) doplnit konkrétní testovatelná čísla do NFR-UX-002 už teď.

Uživatel zvolil (b). Řešilo se také, kam zaznamenávat schválený screen-flow návrh ke
konkrétnímu use-case, aby ho implementace musela respektovat: zvažovaly se (a) krátká sekce
odkazující na mockup přímo v každém UC + principy v jedné ADR (toto rozhodnutí), nebo
(b) nová, pátá kategorie formálních artefaktů vedle requirements/decisions/use-cases/modules.
Varianta (b) je větší architektonická změna struktury repozitáře a nebyla zvolena.

## Rozhodnutí

### 1. Konkrétní pravidla UI/UX (závazná pro běžné a rozšířené použití dle ADR-010)

- **Barva nikdy není jediný nosič významu.** Stav/upozornění se vždy nese i ikonou a textem;
  barva je jen doplněk. Platí i na obrazovkách administrace kvůli konzistenci, i když tam není
  MUST.
- **Paleta vychází z vysokého kontrastu černá/bílá/šedá.** Nejvýše jeden tmavý akcentní odstín
  pro primární akci a jeden odstín pro destruktivní/varovnou akci; žádné světlé pastelové
  plochy jako jediný nosič stavu.
- **Minimální dotyková plocha 48×48 dp** pro ovládací prvky na obrazovkách běžného a
  rozšířeného použití (prst i pero musí trefit spolehlivě); administrace smí jít až na 40×40 dp.
- **Základní velikost textu min. 16 sp** na obrazovkách běžného použití (popisky, hlavní
  obsah), min. 14 sp přípustné pro vedlejší/sekundární texty a pro rozšířené/administrační
  obrazovky. Všechny textové styly používají škálovatelné jednotky (`sp`, ne pevné `px`/`dp`),
  aby fungovalo zvětšení systémového písma.
- **Žádná kontinuální animace ani automatické překreslení rychlejší než 1× za sekundu** na
  obrazovkách běžného použití. Změny stavu jsou diskrétní (ikona/text se změní najednou, bez
  přechodu). Zejména: žádný živý waveform, žádná pulzující/blikající indikace nahrávání —
  stav "nahrává se" se ukazuje staticky (tučný text/ikona), časovač smí aktualizovat text
  maximálně 1× za sekundu.
- **Hustota podle úrovně použití** (ADR-010): běžné použití má velkorysé odsazení a nejvýše
  jednu primární akci na obrazovku; rozšířené použití střední hustotu; administrace smí být
  kompaktní (menší vstupní pole, hustší seznamy).
- **Konzistentní hlavička** (zpět + domů) na každé obrazovce běžného a rozšířeného použití,
  zachovat i v administraci kvůli předvídatelnosti.
- **Žádná akce dostupná jen gestem** (např. swipe-to-delete) bez viditelné ikony/tlačítka —
  platí kvůli perovému i prstovému ovládání.

Tato pravidla doplňují akceptační kritéria [NFR-UX-002](../requirements/NFR-UX-002-e-ink-optimalizace-ui.md)
(viz jeho aktualizace v rámci tohoto rozhodnutí) a nijak neruší [ADR-010](ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md).

### 2. Proces návrhu obrazovek pro novou/měněnou funkci

1. Před návrhem UI se znovu přečtou příslušné FR/UC/NFR-UX a tato ADR.
2. Vytvoří se screen-flow mockup (obrazovky + navigační graf) mimo implementaci, označený
   podle úrovně použití (běžné/rozšířené/administrační) a zkontrolovaný proti pravidlům výše.
3. Mockup se ověří proti akceptačním kritériím dotčeného use-case.
4. Teprve po schválení uživatelem se mockup promítne do implementace.
5. Po schválení se do příslušného use-case doplní krátká sekce **„Obrazovky a tok“** s odkazem
   na schválený mockup (odkaz na Design artefakt nebo jeho exportovaný snímek) a jednou větou,
   které obrazovky daný use-case realizují. Use-case samotný zůstává jediným zdrojem pravdy o
   funkčním chování; sekce jen odkazuje na vizuální řešení.
6. Delší diskuze vedoucí k designovému rozhodnutí se zaznamená jako datovaný chat-extract
   (AGENTS.md §5.5) dřív, než se artefakt označí za PŘIJATO/schválený.

## Důsledky

- Současný mockup (Design artefakt) se musí projít a přepracovat podle pravidel výše — zejména
  obrazovky běžné cesty (Domů → Nahrávání/Import → Detail → Přepis → Šablona → Zpracování →
  Kontrola → Export): odstranit barevné odznaky nesoucí význam jen odstínem, zrušit
  pulzující/živé animace v nahrávání, zvětšit kontextové ikony na min. 48×48 dp, sjednotit
  hustotu podle úrovně použití.
- Každý budoucí UC, který mění UI, dostane sekci „Obrazovky a tok“ — zpětně se doplní až po
  schválení přepracovaného mockupu, aby neodkazovala na zastaralý návrh.
- Nezakládá se nová kategorie formálních artefaktů; princip odkazu z UC na mockup je odlehčená
  alternativa.

## Související

- Požadavek: [NFR-UX-001](../requirements/NFR-UX-001-jednoduche-ux-pro-netechnickeho-uzivatele.md), [NFR-UX-002](../requirements/NFR-UX-002-e-ink-optimalizace-ui.md)
- ADR: [ADR-010](ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md), [ADR-014](ADR-014-navigace-strom-slozek-kontextove-akce.md)
- Use-case: [UC-001](../use-cases/UC-001-poridit-a-ulozit-hlasovy-zaznam.md), [UC-002](../use-cases/UC-002-importovat-externi-nahravku.md), [UC-003](../use-cases/UC-003-prevest-nahravku-na-cesky-text.md), [UC-004](../use-cases/UC-004-strukturovat-text-podle-sablony.md), [UC-005](../use-cases/UC-005-opravit-a-schvalit-navrh-dokumentu.md), [UC-006](../use-cases/UC-006-exportovat-predat-dokument.md), [UC-007](../use-cases/UC-007-nastavit-externi-api.md), [UC-008](../use-cases/UC-008-obnovit-prerusene-zpracovani.md), [UC-009](../use-cases/UC-009-vymazat-vlastni-data.md), [UC-010](../use-cases/UC-010-zpracovat-audio-s-nadiktovanou-sablonou.md), [UC-011](../use-cases/UC-011-organizovat-nahravky-do-slozek.md), [UC-012](../use-cases/UC-012-vytvorit-a-otestovat-sablonu.md) — každý má sekci „Obrazovky a tok“ odkazující na schválený mockup.
