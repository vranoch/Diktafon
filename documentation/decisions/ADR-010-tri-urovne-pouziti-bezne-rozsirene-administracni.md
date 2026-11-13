# ADR-010 — Tři úrovně použití: běžné, rozšířené a administrační

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace).

## Kontext

Aplikace má být jednoduchá a intuitivní pro méně technického uživatele (viz [NFR-UX-001](../requirements/NFR-UX-001-jednoduche-ux-pro-netechnickeho-uzivatele.md)), zároveň ale nabízí i podrobnější správu dat (organizace do složek [FR-AUD-011](../requirements/FR-AUD-011-organizace-nahravek-do-slozek.md), mazání [FR-DATA-001](../requirements/FR-DATA-001-mazani-nahravek-prepisu-a-vysledku.md)) a technickou administraci (nastavení API [FR-CFG-001](../requirements/FR-CFG-001-sprava-api-klice.md)/[FR-CFG-002](../requirements/FR-CFG-002-nastaveni-providera-modelu-a-vychozi-sablony.md), definice a správa šablon [FR-TPL-004](../requirements/FR-TPL-004-vytvoreni-a-uprava-sablony-z-prikladu-s-testovanim.md)). Natlačení všech těchto funkcí do jednoho rovnocenného menu by snížilo jednoduchost hlavních, nejčastějších průchodů.

## Rozhodnutí

UI rozlišuje tři úrovně použití, s rozdílnou mírou jednoduchosti/dostupnosti:

1. **Běžné použití** — vytváření a práce s nahrávkami a přepisy (hlavní, nejčastější průchody: nahrát/importovat → vybrat šablonu → zpracovat → zkontrolovat → exportovat). Musí být maximálně jednoduché a intuitivní, viditelné hned při otevření aplikace.
2. **Rozšířené použití** — podrobnější práce se složkami a nahrávkami (přesouvání, mazání, organizace — viz [FR-AUD-011](../requirements/FR-AUD-011-organizace-nahravek-do-slozek.md), [FR-DATA-001](../requirements/FR-DATA-001-mazani-nahravek-prepisu-a-vysledku.md)). Méně časté úkony, mohou mít nižší dostupnost než běžné použití (např. o úroveň skryté v menu).
3. **Administrační použití** — nastavování systémových hodnot (API klíče/providery, limity), definice a správa šablon. Musí být znatelně odděleno od běžného i rozšířeného použití — nikoliv promíchané s pracovním workflow.

## Důsledky

Navigační struktura/menu aplikace musí tyto tři úrovně respektovat a administraci viditelně oddělit (např. samostatná sekce nastavení dostupná z jiného místa než běžná práce s nahrávkami). [NFR-UX-001](../requirements/NFR-UX-001-jednoduche-ux-pro-netechnickeho-uzivatele.md) a [NFR-UX-002](../requirements/NFR-UX-002-e-ink-optimalizace-ui.md) se o toto rozdělení opírají. Riziko: špatně odhadnutá hranice mezi „běžné“ a „rozšířené“ může uživatele zmást — ověřit na reálném použití.
