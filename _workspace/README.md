# _workspace/

Dočasný scratch prostor. Obsah podsložek `inbox/`, `processing/`, `archive/` je negitovaný
(viz `.gitignore`) — commituje se jen toto README a `rules/`.

## Použití

1. **Staging vstupů** (`inbox/`) — surový materiál (poznámky, exporty, přepisy diskuzí), který
   se má přetavit do trvalého dokumentu v `documentation/`. Podsložková struktura `inbox/`
   zrcadlí strukturu `documentation/` (kam má výsledek jít). Po zpracování se zdrojový soubor
   z `inbox/` smaže; mapování vstup → cíl viz [`rules/conversion-map.yaml`](rules/conversion-map.yaml).
2. **Ad-hoc ladění/analýza** (`processing/`) — transientní pracovní soubory bez potřeby verzovat.
3. **Referenční data pro generování** — podklady pro jednorázovou úlohu; výstup skončí v kódu
   nebo dokumentaci, zdroj se po dokončení smaže (volitelně nejdřív zazálohuje do `archive/`).

## Podsložky

- `inbox/` — vstupy čekající na zpracování (negitováno)
- `processing/` — dočasné pracovní soubory (negitováno)
- `archive/` — volitelná záloha před smazáním zdroje (negitováno)
- `rules/` — **committed**: [`conversion-map.yaml`](rules/conversion-map.yaml) (mapování
  inbox → destinace) a [`processing-notes.md`](rules/processing-notes.md) (log zpracovaného)

Nic z obsahu `inbox/`, `processing/`, `archive/` by nemělo být trvalou součástí repozitáře —
pokud ano, patří to přesunout do `documentation/` nebo kódu a odsud smazat.
