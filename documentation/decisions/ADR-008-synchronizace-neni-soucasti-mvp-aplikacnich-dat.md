# ADR-008 — Synchronizace není součástí MVP aplikačních dat

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — kapitola 8, ADR-008

## Kontext

BOOX zařízení nabízí vlastní systémovou synchronizaci poznámek; je otázka, zda naše aplikace potřebuje vlastní cloudovou synchronizaci projektových dat mezi zařízeními.

## Rozhodnutí

Projektová data (nahrávky, přepisy, dokumenty) žijí pouze lokálně na zařízení; export/předání je explicitní akce uživatele (viz [FR-SYNC-001](../requirements/FR-SYNC-001-vlastni-cloudova-synchronizace.md)).

## Důsledky

Jednodušší bezpečnost a menší rozsah bez vlastní serverové úschovny. Synchronizace systémových poznámek BOOX s PC/mobily není automaticky řešena naším produktem a zůstává otevřenou otázkou pro NEXT (Q-015 v [zdrojovém dokumentu, kap. 12](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md)).
