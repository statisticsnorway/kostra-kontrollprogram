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
./gradlew -p web clean build
```

Starte app fra rot
```bash
./gradlew -p web run
```

Bygge kjørbar JAR-fil
```bash
./gradlew -p web shadowJar
```

Starte app fra JAR-fil
```bash
java -jar ./web/build/libs/kostra-kontrollprogram-web-LOCAL-SNAPSHOT-all.jar
```

## Komme i gang med utvikling frontend

IntelliJ har som standard plugins for Typescript-utvikling.

- Node.js 18 eller nyere er påkrevet
- Chrome React Developer Tools plugin anbefales
- Vitest Runner kan være en nyttig IntelliJ-plugin

Starte React/Vite utviklingsmiljø
```bash
cd ./web-frontend
npm install
npm run dev
```
Backend må være tilgjengelig på port 8080. 
Frontend vil være tilgjengelig på http://localhost/8081.

Kjøre frontend-tester (også en del av `./gradlew check`)
```bash
npx vitest run
```

Bygge Docker-image, kostra-kontrollprogram-web
```bash
./gradlew clean -p web jibDockerBuild --image=kostra-kontrollprogram
```

Liste alle Docker-images
```bash
docker images
```
kostra-kontrollprogram:latest vil vises i listen.

Kjør Docker
```bash
docker run --name kostra-kontrollprogram -p 8080:8080 kostra-kontrollprogram:latest
```
Applikasjonen vil være tilgjengelig på http://localhost:8080/

Rydde opp
```bash
docker rm kostra-kontrollprogram # fjerne stanset container
docker rmi kostra-kontrollprogram # fjerne Docker-image
```
