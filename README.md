# Álláskereső alkalmazás
Az álláskereső alkalmazás egy Spring Boot alapú Java 11 alkalmazás, amely lehetőséget biztosít kliensek regisztrálására.
Valamint a kliensek által hozott állások létrehozására és keresésére.
Az alkalmazás in-memory H2 adatbázist használ.

## Követelmények
- Java 11
- Maven

## Konfiguráció
1. Az alkalmazás forráskódjának klónozása vagy letöltése után, a `mvn clean install` 
parancsot futtava letöltődnek a szükséges függőségek.
2. Az alkalmazás futtatásához a `mvn spring-boot:run` parancsot kell futtatni.

Az alkalmazáshoz tartozik Swagger UI dokumentáció is, amely a következő URL-en érhető el: 
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

Az adatbázis a következő URL-en érhető el (Driver class: org.h2.Driver, JDBC url: jdbc:h2:mem:testdb, user: sa, pass: password):
[http://localhost:8080/h2-console](http://localhost:8080/h2-console)

## Továbbfejlesztési lehetőségek
- Felhasználói felület készítése.
- Kliensek adatainak módosítása és kliensek törlése.
- Állások módosítása és törlése.
- Állásokhoz egyéb mezők (pl. leírás, követelmények, juttatások...) hozzáadása.
- Felhasználói szerepkörök hozzáadása, jelszóval és jogosultságokkal.
- Adatbázisba lementett érzékeny adatok titkosítása.
- Skálázhatóság biztosítása megnövekedett terhelés esetén.
- Hibakezelés javítása és naplózás bevezetése.
- Rendszeres biztonsági mentések készítése az adatbázisról.
- Automata tesztek készítése
