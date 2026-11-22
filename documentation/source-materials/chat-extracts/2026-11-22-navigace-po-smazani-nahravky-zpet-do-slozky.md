# Chat-extract — Navigace po smazání nahrávky zpět do složky, ověření procházení stromu složek

**Datum:** 2026-11-22

## Kontext

Uživatel nahlásil, že po smazání nahrávky (z obrazovky Detail nahrávky, dialog "Smazat celý
záznam") aplikace skočí na domovskou obrazovku místo zpět do složky, odkud byla nahrávka
otevřena — a formuloval obecný princip: dokud uživatel pracuje s nahrávkami uvnitř nějaké
složky, všechny operace nad nahrávkou (ne jen mazání) by se po dokončení měly vracet do té samé
složky, protože je pravděpodobné, že bude pokračovat další prací tamtéž; pokud ne, má k dispozici
tlačítko domů nebo může navigovat stromem složek sám. Součástí požadavku bylo i ověřit, zda
procházení stromu složek (pohyb mezi podsložkami, návrat výš) skutečně funguje.

## Zjištění

- `RecordingDetailScreen` (jediný vstupní bod je z `FolderBrowserScreen` přes
  `onOpenRecording`) měl `onDeleted` zapojené napřímo na `goHome` (skok na Home bez ohledu na
  původní složku) — jediné takové chybné zapojení v celém navigačním grafu; ostatní operace nad
  nahrávkou (přepis, strukturování, import, nové nahrávání) se už správně vrací buď prostým
  "pop" zpět, nebo (u uložení nové nahrávky, kde se cílová složka dá během nahrávání změnit)
  explicitní navigací do cílové složky s `popUpTo("home")`.
- Procházení stromu složek funguje: klepnutí na podsložku v `FolderBrowserScreen` do ní
  naviguje (`onOpenFolder` → `navigate("folders/$id")`). Návrat výš zajišťuje obecné tlačítko
  zpět v hlavičce obrazovky (`popBackStack()`) — to v běžném prohledávání (postupné prokliky
  dolů) odpovídá návratu o úroveň výš, ale je to založené na historii navigace, ne na skutečné
  hierarchii složek. Pokud nějaká akce historii zkolabuje pomocí `popUpTo` (typicky "uložení
  nové nahrávky" nebo dřív i chybné mazání), ztrácí se tím i historie nadřazených složek nad
  cílovou složkou.

## Rozhodnutí

Protože `recordingDetail/{recordingId}` má jediný vstupní bod (vždy ze složky, ve které nahrávka
je), stačí po smazání použít prostý `navController.popBackStack()` místo `goHome` nebo
navigace s `popUpTo` — vrátí uživatele přesně do složky, odkud přišel, a navíc (na rozdíl od
`popUpTo`) zachová i historii nad ní, takže zpětná navigace výš stromem dál funguje. Stejný
princip (po dokončení operace nad nahrávkou skončit ve složce, ne na Home) už platil pro
ostatní operace — opraveno bylo jen mazání.

## Zdroje

- Tato konverzace, 2026-11-22.
