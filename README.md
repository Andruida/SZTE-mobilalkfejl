# Nézzük!

Avagy az SZTE Mobilalkalmazás fejlesztés 2023/24/2 félévének beadandója, jelige: "Online Színházjegy vásárlás".

## Javítási segédlet

| Feladat                                                                                                                                     | Fájl(ok)                                                                                                                                                                                                                                                                     | Megjegyzés                                                                                 |
|---------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------|
| Firebase autentikáció meg van valósítva                                                                                                     | [LoginActivity.java:106](app/src/main/java/hu/andruida/nezzuk/authentication/LoginActivity.java#L106)                                                                                                                                                                        | Az összes Activity megnézi, hogy a felhasználó be van-e jelentkezve.                       |
| Adatmodell definiálása (class vagy interfész formájában)                                                                                    | [TicketListing](app/src/main/java/hu/andruida/nezzuk/model/TicketListing.java)                                                                                                                                                                                               |                                                                                            |
| Legalább 3 különböző activity vagy fragmens használata                                                                                      | [LoginActivity.java](app/src/main/java/hu/andruida/nezzuk/authentication/LoginActivity.java), [MainActivity.java](app/src/main/java/hu/andruida/nezzuk/activities/MainActivity.java), [CartActivity.java](app/src/main/java/hu/andruida/nezzuk/activities/CartActivity.java) |                                                                                            |
| Beviteli mezők beviteli típusa megfelelő (jelszó kicsillagozva, email-nél megfelelő billentyűzet jelenik meg stb.)                          | [activity_login.xml](app/src/main/res/layout/activity_login.xml), [activity_register.xml](app/src/main/res/layout/activity_register.xml)                                                                                                                                     |                                                                                            |
| ConstraintLayout és még egy másik layout típus használata                                                                                   | LinearLayout: [activity_login.xml](app/src/main/res/layout/activity_login.xml), ConstraintLayout: [activity_cart.xml](app/src/main/res/layout/activity_cart.xml)                                                                                                             |                                                                                            |
| Reszponzív                                                                                                                                  |                                                                                                                                                                                                                                                                              |                                                                                            |
| Legalább 2 különböző animáció használata                                                                                                    | [CartAdapter.java:97](app/src/main/java/hu/andruida/nezzuk/model/adapters/CartAdapter.java#L97), [CartActivity.java:92](app/src/main/java/hu/andruida/nezzuk/activities/CartActivity.java#L92)                                                                               | Mindkettő a kosár oldalon.                                                                 |
| Intentek használata: navigáció meg van valósítva az activityk/fragmensek között                                                             |                                                                                                                                                                                                                                                                              | Minden Activity elérhető és az adott Activity-k ellenőrzik, hogy jogosult-e a felhasználó. |
| Legalább egy Lifecycle Hook használata a teljes projektben:                                                                                 | [LoginActivity.java:92](app/src/main/java/hu/andruida/nezzuk/authentication/LoginActivity.java#L92)                                                                                                                                                                          | `onPause` esetén menti a beírt e-mail címet.                                               |
| Legalább egy olyan androidos erőforrás használata, amihez kell android permission                                                           | [CartActivity.java:108](app/src/main/java/hu/andruida/nezzuk/activities/CartActivity.java#L108)                                                                                                                                                                              | Értesítések                                                                                |
| Legalább egy notification vagy alam manager vagy job scheduler használata                                                                   | [CartActivity.java:128](app/src/main/java/hu/andruida/nezzuk/activities/CartActivity.java#L128)                                                                                                                                                                              |                                                                                            |
| CRUD műveletek mindegyike megvalósult és az adatbázis műveletek a konvenciónak megfelelően külön szálon történnek                           | [CartRepository.java](app/src/main/java/hu/andruida/nezzuk/repository/CartRepository.java)                                                                                                                                                                                   |                                                                                            |
| Legalább 2 komplex Firestore lekérdezés megvalósítása, amely indexet igényel (ide tartoznak: where feltétel, rendezés, léptetés, limitálás) | [LoginActivity.java:75](app/src/main/java/hu/andruida/nezzuk/authentication/LoginActivity.java#L75), [MainActivity.java:90](app/src/main/java/hu/andruida/nezzuk/activities/MainActivity.java#L90)                                                                           |                                                                                            |

## Google credentials

A GitHub repoban nem teszem közzé a `google-credentials.json` fájlt. 
Enélkül elképzelhető, hogy a projekt nem fog lefordulni. 

Az alkalmazás egy adatokkal előre feltöltött Firestore adatbázist vár. Az adatbázisban a 
következő collection van:

```
ticket_listings {
    amountLeft: number
    date: string
    description: string
    imageUrl: string
    location: string
    price: number
    time: string
    title: string 
}
```

Hitelesítési módszerek közül az e-mail+jelszó és az anonim bejelentkeztetést használja.

## Értesítések

Az értesítéshez engedélykérő ablak csak újabb Android verziókon működik, ha neked nem jelenik meg, 
akkor engedélyezd kézzel az alkalmazásbeállításokban.