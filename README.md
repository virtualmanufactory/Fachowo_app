# Fachowo

Porównywarka lokalnych usług dla małych firm. Wizytówka po NIP, tabela porównawcza (kategoria → województwo → miasto).

## Wymagania

- Java 17+
- Node 20+
- Podman 5+ (maszyna WSL: `podman machine start`)
- Maven 3.8+

## Uruchomienie

```bash
podman machine start
podman compose up -d
```

PostgreSQL w Podmanie nasłuchuje na porcie **5433** (żeby nie kolidować z lokalnym Postgresem na 5432).

Backend:

```bash
cd backend
mvn spring-boot:run
```

Frontend:

```bash
cd frontend
npm install
npm run dev
```

- Aplikacja: http://localhost:5173
- API: http://localhost:8080/api
- Swagger: http://localhost:8080/swagger-ui.html
- MinIO console: http://localhost:9001 (`fachowo` / `fachowo123`)

## Demo

Porównywarka hydraulików:

http://localhost:5173/kategoria/hydraulika/wielkopolskie/poznan

Konta demo (hasło `password`):

- `kowalski@demo.fachowo.pl`
- `hydro@demo.fachowo.pl`
- `instal@demo.fachowo.pl`

Weryfikacja NIP w trybie deweloperskim używa stuba (checksum NIP + dane przykładowe). API MF White List i CEIDG można włączyć w `application.yml`.

Testy backendu (`mvn test` w `backend/`): jednostkowe (NIP, JWT, auth, katalog, storage) oraz integracyjne API na Postgresie i Redisie w Testcontainers.
