# Chat-extract — Správa seznamu povolených jazyků přepisu

**Datum:** 2026-11-14

## Kontext diskuze

Při ručním testování sestaveného debug APK (implementace ADR-015/mockupu, viz commit
`9981606`) uživatel v Administraci narazil na to, že chybí správa známých/povolených jazyků
přepisu — existuje jen jedno volně psané textové pole s kódem jazyka
(`app/src/main/java/app/diktafon/settings/ui/ApiConfigScreen.kt`), označené v kódu jako
`TODO (ASSUMPTION)`, protože dřívější mockup počítal se spravovaným seznamem jazyků, ale tato
funkčnost nebyla podložená žádným existujícím FR ani schématem databáze.

## Rozhodnutí uživatele

Uživatel požádal o založení formálního požadavku na tuto funkčnost (místo rovnou implementace
bez podložení) a následně požadavek schválil k realizaci.

## Navazující artefakty

- [FR-CFG-005 — Správa seznamu povolených jazyků přepisu](../../requirements/FR-CFG-005-sprava-seznamu-povolenych-jazyku-prepisu.md)
