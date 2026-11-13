# ADR-007 — Serverless proxy až při změně okolností

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — kapitola 8, ADR-007

## Kontext

Volání API přímo z klienta (viz [ADR-002](ADR-002-bez-vlastni-serverove-casti-v-mvp.md)) nese riziko úniku klíče; server/proxy vrstvu ale nemá smysl budovat dříve, než pro ni vznikne reálná potřeba.

## Rozhodnutí

Pro MVP se nebuduje žádný kontejner ani VM se serverless proxy vrstvou.

## Důsledky

Trigger pro revizi: požadavek na centrální autentizaci, ochranu API klíčů nebo správu více uživatelů. Možnosti při naplnění triggeru: Azure Functions/Container Apps nebo Google Cloud Run — infrastruktura se nevolí dříve, než potřeba skutečně vznikne. Tento ADR je záměrně odložený (deferred) do doby, než nastane popsaný trigger.
