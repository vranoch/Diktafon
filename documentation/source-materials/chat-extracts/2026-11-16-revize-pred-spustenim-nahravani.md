# Chat-extract — Revize názvu/poznámky před spuštěním nahrávání

**Datum:** 2026-11-16

## Kontext diskuze

Při ručním testování aplikace uživatel zjistil, že po klepnutí na "Nová nahrávka" se mikrofon
spustí okamžitě — název a poznámku šlo upravit až za běhu nahrávání. Uživatel chtěl nejdřív
revidovat předgenerovaný název a volitelně doplnit poznámku, a teprve pak explicitně zahájit
samotný záznam.

Dosavadní [FR-AUD-005](../../requirements/FR-AUD-005-seznam-nahravek-s-metadaty.md) popisovala
jen to, že je název "ihned editovatelný" po zahájení nahrávání — nerozlišovala, zda editace
probíhá už za běhu mikrofonu, nebo v samostatném kroku před ním.

## Rozhodnutí uživatele

Zavést mezikrok: klepnutí na "Nová nahrávka" otevře obrazovku s předgenerovaným názvem a
poznámkou k revizi (mikrofon ještě neběží), teprve tlačítko "Spustit nahrávání" zahájí reálný
záznam.

## Navazující artefakty

- [FR-AUD-005 (aktualizace)](../../requirements/FR-AUD-005-seznam-nahravek-s-metadaty.md)
