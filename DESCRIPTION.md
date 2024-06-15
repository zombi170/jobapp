A cél egy álláskereső alkalmazás létrehozása.

A feladatot Java 11 nyelven, Spring framework segítségével valósítsa meg.

Az adatbázishoz célszerű inmemory adatbázist használni vagy file alapú megoldást, melynek tartalmaznia kell már meglévő adatokat is! 
Inmemory database esetén kezdeti adatfeltöltést is tartalmazzon a megoldás.

Követelmények:
1. Az alkalmazás biztosítson lehetőséget kliensalkalmazások regisztrációjára (POST /client). A kliens átadja a nevét (validáció: max 100 karakter), e-mail címét (validáció: érvényes email cím formátum, bármilyen regexp használatával, valamint egyediség ellenőrzése). A responseban a szerver egy api kulcsot ad vissza UUID formátumban.
2. Az alkalmazás biztosítson lehetőséget állások létrehozására (POST /position). A kliens átadja az állás megnevezését (validáció: max 50 karakter), a munkavégzés földrajzi helyét (validáció: max 50 karakter). A szerver első lépésben ellenőrzi az api kulcs érvényességét. Nem érvényes api kulcs esetén hibaüzenettel tér vissza. A szerver mentse el az állást, majd térjen vissza egy URL-lel a responseban, hogy milyen oldalon érhető el a pozició.
3. Az alkalmazás biztosítson lehetőséget állások keresésére (GET /position/search). A kliens átadja a keresett keywordöt (pl.: "finance", validáció: max 50 karakter) valamint a lokációt (pl.: "london", validáció: max 50 karakter). A szerver első lépésben ellenőrzi az api kulcs érvényességét. Nem érvényes api kulcs esetén hibaüzenettel tér vissza.
   Érvényes api kulcs esetén az átadott adatokkal bekérdez az adatbázisban tárolt állások.
   Találatnak számít a részleges egyezés is pl: ha az állás megnevezése: “Java backend developer” és a keresésben a feltétel annyi, hogy “developer” akkor az találat. Ha a keresés sikerrel járt a kliens számára egy URL listával kell visszatérni a hírdetésekhez tartozó URL-el.
4.  A keresési eredmények megnyitásához pedig szükséges implementálni egy GET (/position/id) kérést.
    
A szerver validációs hibák esetén egységes hibatípussal térjen vissza, részletezve, hogy milyen mezőkkel milyen validációs hiba történt.

Az API réteget válassza el az adatelérési rétegtől.

Az alkalmazáshoz swagger-ui használata nem kötelező, de ajánlott.

A fejlesztés után soroljon fel továbbfejlesztési lehetőségeket ezzel a projekttel kapcsolatban, hogy teljes mértékben production ready alkalmazás legyen és az üzemeltetés is elfogadja tőlünk ezt az alkalmazást.
    Készítsen rövid leírást az alkalmazásról, hogyan és milyen eszközökkel lehet bekonfigurálni és futtatni.