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

> **Poznámka (2026-11-12):** Při extrakci požadavků/ADR/use-cases ze
> [zdrojového dokumentu](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md)
> (kap. 7.2 — logické komponenty: Presentation/E Ink UI, Recording/Import, Workflow
> Coordinator, Provider Adapters, Template Engine + Validator, Local Persistence,
> Security & Settings, Export Adapter) nebyly založeny moduly, protože tech stack ani
> struktura kódu ještě nejsou zvoleny a modul-spec musí být 1:1 s podsložkou v kódu
> (viz konvence výše). Moduly se založí, až vznikne odpovídající kód.
