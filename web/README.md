# kostra-kontrollprogram-web

![img.png](img.png)
kostra-kontrollprogram-web er en web-app som kjøres lokalt og benytter kostra-kontrollprogram internt for kontroll av
datafiler.

## Komme i gang med utvikling 

Bygge app fra rot
```bash
./mvnw clean -pl web -am verify
```

Starte app fra rot
```bash
./mvnw -pl web exec:java
```

Starte React/Vite utviklingsmiljø (krever Node.js 18 eller nyere)
```bash
cd ./web/src/main/vite-project
npm install
npm run dev
```
App må kjøre på port 8080, mens http://localhost/8081 med hot reload benyttes for endringer av frontend.

Kjøre frontend-tester (kjøres som en del av `mvn verify` også)
```bash
npx vitest run
```

## Bruk (sluttbrukere)

```bash
java -jar <navn på JAR-fil>
```