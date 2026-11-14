# FR-CFG-005 — Správa seznamu povolených jazyků přepisu

**Stav:** NAVRŽENO
**Vytvořeno:** 2026-11-14
**Změněno:** 2026-11-14
**Zdroje:**
- Zpětná vazba uživatele z ručního testu sestaveného APK, 2026-11-14 (tato konverzace) — v
  Administraci chybí správa známých/povolených jazyků přepisu; aktuální implementace má jen
  jedno volně psané textové pole s kódem jazyka (viz `TODO (ASSUMPTION)` v
  `app/src/main/java/app/diktafon/settings/ui/ApiConfigScreen.kt`).
**Priorita:** SHOULD / NEXT

## Popis

[NAVRŽENO] Administrace umožní spravovat seznam povolených/známých jazyků přepisu (kód +
zobrazovaný název), ze kterého se jazyk vybírá — na obrazovce Přepis (viz
[FR-STT-001](FR-STT-001-odeslani-audia-do-stt-a-ulozeni-prepisu.md)) i jako výchozí hodnota v
Administraci (viz [FR-CFG-002](FR-CFG-002-nastaveni-providera-modelu-a-vychozi-sablony.md)) —
místo dnešního jediného volně psaného textového pole s kódem jazyka.

## Akceptační kritéria

- [ ] Administrace zobrazuje seznam povolených jazyků (kód + zobrazovaný název, např. `cs` →
      „Čeština“) a umožňuje do něj jazyk přidat a ze seznamu jazyk odebrat.
- [ ] Jazyk pro přepis (na obrazovce Přepis i jako výchozí hodnota v Administraci) se vybírá
      z tohoto seznamu, ne volným textem.
- [ ] Výchozí sada po instalaci obsahuje alespoň češtinu a angličtinu (odpovídá dnešnímu
      pevně danému seznamu v kódu) a čeština zůstává výchozí volbou.
- [ ] Odebrání jazyka ze seznamu nesmí porušit už existující přepisy pořízené v tomto jazyce —
      u hotového přepisu jazyk zůstává zobrazený i poté, co byl ze seznamu povolených odebrán.

## Související

- Use-case: [UC-007](../use-cases/UC-007-nastavit-externi-api.md)
- FR: [FR-STT-001](FR-STT-001-odeslani-audia-do-stt-a-ulozeni-prepisu.md), [FR-CFG-002](FR-CFG-002-nastaveni-providera-modelu-a-vychozi-sablony.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v [documentation/modules/README.md](../modules/README.md))
