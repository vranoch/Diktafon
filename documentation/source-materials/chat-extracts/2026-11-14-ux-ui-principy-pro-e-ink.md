# Chat-extract — UX/UI principy pro E Ink a proces návrhu obrazovek

**Datum:** 2026-11-14

## Kontext diskuze

Po několika kolech iterativních úprav grafického mockupu (samostatný canvas mimo
implementaci, viz navrhované obrazovky v Design artefaktu) uživatel upozornil, že:

- Řada požadavků, které zadával po částech, už je napsaná v existujících formálních
  artefaktech (zejména [NFR-UX-001](../../requirements/NFR-UX-001-jednoduche-ux-pro-netechnickeho-uzivatele.md),
  [NFR-UX-002](../../requirements/NFR-UX-002-e-ink-optimalizace-ui.md),
  [ADR-010](../../decisions/ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md)) a mělo
  se z nich vycházet důsledněji místo dohadování po jednotlivých obrazovkách.
- Dosavadní návrhy obrazovek dostatečně nerespektovaly osvědčené UX postupy a platformní
  specifika cílového zařízení (BOOX E Ink tableta).
- Je potřeba promyslet, jak má vypadat postup zadávání/návrhu UX/UI pro další funkce a jak
  výsledek zaznamenat do specifikace, aby to implementace musela respektovat.

## Zjištění z existujících podkladů

Ze zdrojového dokumentu ([20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md](source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md))
a z NFR-UX-001/002:

- Cílové zařízení (ASM-003): BOOX Go 10.3 Gen II Lumi — 10,3" **šedá (grayscale) E Ink**
  tableta, ovládaná perem i prstem, jediný uživatel.
- NFR-UX-002 je MUST pro "běžné použití" (hlavní cesta dle ADR-010): vysoký kontrast, velké a
  jasné ovládací prvky, minimum animací a rychlých překreslení, zvětšitelný text. U
  "rozšířeného použití" může být kompaktnější, u administrace to není závazné.
- Dosavadní mockup to porušoval: barevné pastelové odznaky nesoucí význam jen odstínem,
  pulzující/živé animované prvky (nahrávací tečka, waveform), malé kontextové ikony
  (32×32 px) a téměř stejná hustota napříč všemi úrovněmi použití.

## Rozhodnutí uživatele

1. **Cílové zařízení pro návrh:** potvrzeno BOOX Go 10.3 Gen II Lumi (šedá E Ink) — návrh má
   počítat s téměř monochromatickou, vysoce kontrastní paletou, barva jen jako doplněk.
2. **Konkrétní čísla v NFR-UX-002:** doplnit měřitelná akceptační kritéria (min. velikost
   dotykové plochy, základní velikost textu) místo jen kvalitativního "velké a jasné".
3. **Záznam UX/UI návrhu do specifikace:** každý use-case dostane krátkou sekci
   "Obrazovky a tok" s odkazem na schválený screen-flow mockup; principy samotné se drží v
   jedné ADR (nezakládá se nová, pátá kategorie formálních artefaktů vedle
   requirements/decisions/use-cases/modules).

## Navazující artefakty

- [ADR-015 — Principy UI/UX pro E Ink a proces návrhu obrazovek](../../decisions/ADR-015-principy-ui-ux-pro-e-ink-a-proces-navrhu-obrazovek.md)
- Aktualizace [NFR-UX-002](../../requirements/NFR-UX-002-e-ink-optimalizace-ui.md) s konkrétními
  akceptačními kritérii.
