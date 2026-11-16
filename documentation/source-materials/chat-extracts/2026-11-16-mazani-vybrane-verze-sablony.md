# Chat-extract — Mazání vybrané verze šablony, 3 možnosti při ukládání

**Datum:** 2026-11-16

## Kontext diskuze

Navazuje na [chat-extract — Ukládání/mazání šablon a formátování vzorového textu](2026-11-16-sablony-ukladani-mazani-format.md)
a [FR-TPL-006](../../requirements/FR-TPL-006-smazani-sablony.md), který zatím řešil jen smazání
celé šablony (všech verzí najednou). Uživatel upřesnil, že primárně potřebuje smazat vybranou
jednotlivou verzi šablony, ne nutně všechny najednou (mazání všech verzí zůstává jako další
možnost).

Zároveň upřesnil dialog při ukládání změn existující šablony: místo dvou možností (přepsat →
nová verze / nová šablona) má nabízet tři jasně odlišené možnosti.

## Rozhodnutí uživatele

1. V seznamu šablon lze zobrazit všechny verze jedné šablony a smazat jen vybranou verzi
   (ostatní verze zůstanou zachované); smazání všech verzí najednou zůstává k dispozici.
2. Dialog při ukládání změn existující šablony nabízí tři možnosti:
   - **Přepsat stávající verzi** — obsah aktuální verze se změní na místě, číslo verze se
     nemění.
   - **Uložit jako novou verzi** — vznikne nová verze téže šablony (dosavadní chování).
   - **Uložit jako novou šablonu** — vznikne zcela samostatná šablona (dosavadní chování).

## Navazující artefakty

- [FR-TPL-006 (aktualizace)](../../requirements/FR-TPL-006-smazani-sablony.md)
