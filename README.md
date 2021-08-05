# Indítás
Egyszerű spring boot start. Az alkalmazás H2 inMemory adatbázist használ.

# Tesztelés Postmannel
A /docs/postman mappában található az exportált postman hívások. 

Az alábbi sorrendben kell végrehajtani tesztelés céljából:
1. 01 City - Upload
    input_city.txt fájl betöltése.
2. 07 Flight - Upload
    input_flight.txt fájl betöltése.
3. 17 Dijkstra all airlines
    Útvonal keresés az összes járattal.
4. 18 Dijkstra one airline
    Útvonal keresés az adott légitársaság járataival.
5. 16 Airline - Airline flights
    Az adott légitársasághoz tartozó járatok.
6. 19 Ways between cities
    Két város közötti járatok.

A további postman hívások CRUD műveletek.
