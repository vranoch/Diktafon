# ADR-001 — Nativní Android aplikace

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — kapitola 8, ADR-001

## Kontext

Je potřeba zvolit technologickou platformu pro aplikaci běžící primárně na BOOX zařízení (Android, E Ink displej), s přístupem k mikrofonu, souborům a perem ovládanému UI (ASM-002). Zvažované alternativy: web/PWA (horší řízení nahrávání a offline chování) a multiplatform framework (pro MVP představuje další vrstvu složitosti).

## Rozhodnutí

Nativní Kotlin Android APK, prioritně cílená na BOOX, bez povinné závislosti na proprietárním BOOX SDK (ASM-011, viz [NFR-PORT-001](../requirements/NFR-PORT-001-prenositelnost-na-standardni-android.md)).

## Důsledky

Je nutné otestovat Android lifecycle a chování konkrétního výrobcova firmware (ASM-003). Přenositelnost na běžný Android telefon je žádoucí vlastnost, ne garance bez dalšího testu.
