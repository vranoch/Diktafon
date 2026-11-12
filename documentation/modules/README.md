# documentation/modules/

Specifikace jednotlivých modulů/komponent systému. Jeden modul = jedna podsložka
`documentation/modules/<nazev-modulu>/module-spec.md`. Struktura kódu a struktura
dokumentace modulů by měly zůstat 1:1 — podsložka zde odpovídá modulu/komponentě v kódu.

## Konvence

- `documentation/modules/<nazev-modulu>/module-spec.md`, metadata + lifecycle podle
  [AGENTS.md §5](../../AGENTS.md).
- Jádro specifikace modulu je **coverage tabulka** — mapuje požadavky, use-cases a ADR
  relevantní pro modul na stav jejich implementace v něm:

  | Odkaz (FR/NFR/UC/ADR) | Stav implementace | Poznámka |
  |---|---|---|
  | FR-001 | FINAL | |
  | UC-003 | PROVISIONAL | čeká se na rozhodnutí o ... |

  Stav implementace: `PROVISIONAL` (implementováno předběžně, může se změnit),
  `FINAL` (hotovo a stabilní), `UNRESOLVED` (požadavek se modulu týká, ale zatím neřešeno).
- Modul-spec se aktualizuje spolu se změnou modulu, ne až dodatečně.

Viz [`_template/module-spec.md`](_template/module-spec.md) pro šablonu nového modulu.

## Index modulů

| Modul | Změněno | Shrnutí |
|---|---|---|
| — | — | zatím žádný modul |
