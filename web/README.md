# **Veiledning for KOSTRA-kontrollprogrammet - web-basert**

![img.png](img.png)
kostra-kontrollprogram-web er en web-app som kjøres lokalt og benytter kostra-kontrollprogram internt for kontroll av
datafiler.

## For sluttbrukere

Åpne et kommandolinjevindu og start webapplikasjonen med:
```bash
java -jar <navn på JAR-fil>
```

Åpne en nettleser med adressen som vises i kommandolinjevinduet, f.eks. http://localhost:8080/

## Open API dokumentasjon

API-dokumentasjon finnes på http://localhost:8080/swagger/kostra-kontrollprogram-api-1.0.yml

# For utviklere

## Komme i gang med utvikling backend 

Bygge app fra rot
```bash
./mvnw clean -pl web -am install
```

Starte app fra rot
```bash
./mvnw -pl web exec:exec
```

## Komme i gang med utvikling frontend

IntelliJ har som standard plugins for Typescript-utvikling.

- Node.js 18 eller nyere er påkrevet
- React Developer Tools plugin i Chrome anbefales
- Vitest Runner kan være en nyttig IntelliJ-plugin

Starte React/Vite utviklingsmiljø
```bash
cd ./web-frontend
npm install
npm run dev
```
Backend må være tilgjengelig på port 8080. http://localhost/8081 med hot reload benyttes for frontend.

Kjøre frontend-tester (også en del av `mvn verify`)
```bash
npx vitest run
```

