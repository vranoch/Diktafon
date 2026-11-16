# Chat-extract — Ukládání/mazání šablon a formátování vzorového textu

**Datum:** 2026-11-16

## Kontext diskuze

Při ručním testování obrazovky Šablona (vytvoření/úprava přes vzorový text, viz
[FR-TPL-004](../../requirements/FR-TPL-004-vytvoreni-a-uprava-sablony-z-prikladu-s-testovanim.md))
uživatel zjistil tři problémy:

1. Žádný mechanismus pro formátování vzorového textu (tučně/kurzíva).
2. Stránka při delším textu nejde scrollovat, takže tlačítko uložení je nečitelné/na okraji
   obrazovky; opakované kliky na něj (protože nebylo jasné, co se stalo) při editaci existující
   šablony potichu zakládaly další a další verze bez varování.
3. Neexistuje způsob, jak šablonu smazat.

## Rozhodnutí uživatele

- Šablony nemusí zůstat neviditelně verzované bez varování — před uložením změn u existující
  šablony se má aplikace zeptat, zda přepsat (založit novou verzi) nebo založit úplně novou,
  samostatnou šablonu. [NFR-MAINT-002](../../requirements/NFR-MAINT-002-verzovani-sablon-a-migrace-dat.md)
  (verzování) a explicitní potvrzení z [FR-TPL-004](../../requirements/FR-TPL-004-vytvoreni-a-uprava-sablony-z-prikladu-s-testovanim.md)
  zůstávají v platnosti — šlo o chybějící implementaci již schváleného chování, ne o jeho zrušení.
- Existující šablonu (všechny její verze) musí jít smazat.
- Vzorový text potřebuje alespoň základní formátování (tučně/kurzíva) — řešeno vložením
  Markdown značek (`**`/`*`) tlačítky nad polem, beze změny výstupního formátu (Markdown už
  používá [MarkdownDocumentFormatter](../../../app/src/main/java/app/diktafon/export/data/MarkdownDocumentFormatter.kt)).

## Navazující artefakty

- [FR-TPL-006 — Smazání šablony](../../requirements/FR-TPL-006-smazani-sablony.md)
