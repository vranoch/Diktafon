# ADR-014 — Navigace: strom složek jako hlavní cesta, kontextové akce na nahrávce

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-13
**Změněno:** 2026-11-22
**Zdroje:**
- Zpětná vazba uživatele po prvním vyzkoušení aplikace, 2026-11-13 (tato konverzace) — plochý seznam tlačítek na hlavní obrazovce nerozlišoval běžné/rozšířené/administrační použití a pád při "Nová nahrávka" (chybějící runtime oprávnění k mikrofonu).
- Upřesnění uživatele v rámci revize, 2026-11-13 (tato konverzace) — popis navigace jako souborového prohlížeče s kontextovými operacemi na "koncové" nahrávce.
- [Chat-extract — Navigace po smazání nahrávky zpět do složky, ověření procházení stromu složek, 2026-11-22](../source-materials/chat-extracts/2026-11-22-navigace-po-smazani-nahravky-zpet-do-slozky.md)

## Kontext

První implementace hlavní obrazovky ukázala všechny funkce (nová nahrávka, import, složky, nastavení, nová šablona, obnova) jako rovnocenná tlačítka v jedné ploché nabídce. To porušovalo [ADR-010](ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md) (tři úrovně použití musí být rozlišitelné) i [NFR-UX-001](../requirements/NFR-UX-001-jednoduche-ux-pro-netechnickeho-uzivatele.md). Zároveň chyběla samostatná obrazovka pro správu šablon a e-mailových příjemců a jednotlivé funkce (přehrát, přepsat, konvertovat, odeslat, smazat) neměly žádné přirozené místo, odkud by se k nim přistupovalo k dané nahrávce.

## Rozhodnutí

Navigace je organizována podle [ADR-010](ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md) takto:

1. **Hlavní obrazovka** — jen zkratka "Nová nahrávka" (do naposledy použité složky, viz [ADR-013](ADR-013-hierarchicka-struktura-slozek.md)), "Importovat", ikona pro procházení složek a ikona ozubeného kola pro administraci. Žádné další tlačítko.
2. **Procházení složek** (běžné a rozšířené použití dohromady, rozlišené akcemi) — rekurzivní obrazovka nad stromem z [ADR-013](ADR-013-hierarchicka-struktura-slozek.md): zobrazí podsložky a nahrávky aktuální složky; nabízí vytvořit/přejmenovat/přesunout/smazat složku a založit novou nahrávku/import přímo do procházené složky.
3. **Detail nahrávky je "koncový" uzel stromu** — teprve zde se zobrazují kontextové operace nad konkrétní nahrávkou (přehrát, přepsat nebo upravit existující přepis, konvertovat podle šablony nebo upravit existující výsledek, odeslat přepis/výsledek, smazat — ať celou nahrávku nebo jen dílčí artefakt). Žádná z těchto funkcí není dostupná z plochého globálního menu.
4. **Administrace** (gear ikona, znatelně oddělená) — připojení (API klíče, provider/model, výchozí jazyk), správa šablon (katalog s možností úpravy existující verze), e-mailoví příjemci.

Zároveň oprava: start nahrávání vyžádá runtime oprávnění `RECORD_AUDIO`, pokud ještě není uděleno, a selhání startu nahrávání (oprávnění, úložiště) se zachytává a zobrazuje jako chyba místo pádu aplikace.

**Aktualizace 2026-11-22:** doplněn explicitní princip — dokončení libovolné operace nad
nahrávkou (smazání, import, uložení nové nahrávky, ...) vede zpátky do složky, odkud byla
nahrávka/operace otevřena, nikdy automaticky na Home. Oprava se týkala mazání celé nahrávky
(`onDeleted` bylo chybně napevno zapojené na skok na Home); ostatní operace se už vracely
správně. Protože Detail nahrávky má jediný vstupní bod (ze složky), stačí k návratu prostý
`popBackStack()` — na rozdíl od navigace s `popUpTo` tím zůstává zachovaná i historie nad
danou složkou, takže uživatel může zpětným tlačítkem pokračovat dál nahoru stromem složek.

Zároveň doplněn breadcrumb (cesta od kořene po aktuální složku) v záhlaví obrazovky Procházení
složek — jednotlivé úrovně kromě té aktuální jsou klikací a skáčou přímo na danou úroveň (přes
`navigate(...) { popUpTo("home") }`, stejný vzor jako jinde v navigačním grafu), takže návrat o
víc úrovní najednou nezávisí na tom, jak přesně (jakou cestou zpětné navigace) uživatel do
aktuální složky došel — řeší to i zmíněné omezení obyčejného tlačítka zpět, které je založené na
historii navigace, ne na skutečné hierarchii složek.

## Důsledky

- Několik obrazovek z první iterace zaniklo nebo bylo nahrazeno (plochý seznam nahrávek, samostatná jednoúrovňová správa složek) ve prospěch jedné rekurzivní obrazovky procházení.
- Přibyly obrazovky, které předtím chyběly, přestože požadavky už existovaly: katalog šablon ([FR-TPL-002](../requirements/FR-TPL-002-struktura-sablony-id-verze-schema-instrukce.md)/[FR-TPL-004](../requirements/FR-TPL-004-vytvoreni-a-uprava-sablony-z-prikladu-s-testovanim.md)) a e-mailoví příjemci ([FR-CFG-004](../requirements/FR-CFG-004-sprava-seznamu-emailovych-adres.md)).
- Přehrávání nahrávky ([FR-AUD-006](../requirements/FR-AUD-006-prehrani-nahravky-pred-odeslanim.md)) dostalo svou první implementaci (dříve chybělo) právě jako součást detailu nahrávky.
- Runtime oprávnění k mikrofonu (dříve jen zmíněné jako alternativní tok A1 v [UC-001](../use-cases/UC-001-poridit-a-ulozit-hlasovy-zaznam.md), nikdy neimplementované) je nyní vyžádáno a jeho zamítnutí se řeší jako chybový stav, ne pád aplikace.

## Související

- Požadavek: [FR-AUD-006](../requirements/FR-AUD-006-prehrani-nahravky-pred-odeslanim.md), [FR-AUD-011](../requirements/FR-AUD-011-organizace-nahravek-do-slozek.md), [FR-TPL-004](../requirements/FR-TPL-004-vytvoreni-a-uprava-sablony-z-prikladu-s-testovanim.md), [FR-CFG-004](../requirements/FR-CFG-004-sprava-seznamu-emailovych-adres.md)
- Use-case: [UC-001](../use-cases/UC-001-poridit-a-ulozit-hlasovy-zaznam.md), [UC-011](../use-cases/UC-011-organizovat-nahravky-do-slozek.md)
- ADR: [ADR-010](ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md), [ADR-013](ADR-013-hierarchicka-struktura-slozek.md)
