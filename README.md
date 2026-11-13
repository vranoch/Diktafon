# Diktafon

TBD: stručný popis projektu, až bude zvolený účel a tech stack.

## Pro vývojáře (lidi i AI)

- Pravidla a konvence práce v repozitáři: [AGENTS.md](AGENTS.md)
- Claude Code specifika: [CLAUDE.md](CLAUDE.md)
- Návody "jak na to": [ai/skills/](ai/skills/)
- Opakované postupy: [ai/workflows/](ai/workflows/)
- Architektonická rozhodnutí: [documentation/decisions/](documentation/decisions/)

## Quickstart

Vývoj probíhá výhradně v kontejneru (viz [ADR-011](documentation/decisions/ADR-011-vyvojove-prostredi-v-kontejnerech-a-testovaci-strategie.md)) — žádné lokální Android/Kotlin SDK není potřeba.

```bash
docker build -t diktafon-dev -f .devcontainer/Dockerfile .devcontainer
docker run --rm -v "$(pwd)":/workspace diktafon-dev sh -c "cd /workspace && ./gradlew test"
```

Technologický stack: Kotlin, Jetpack Compose, Room, Hilt, Retrofit (viz [ADR-012](documentation/decisions/ADR-012-technologicky-stack-compose-room-hilt-retrofit.md)).
