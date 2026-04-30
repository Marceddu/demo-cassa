# Deploy su Railway

## Variabili ambiente Railway
Imposta queste variabili nel servizio app:
- `SPRING_PROFILES_ACTIVE=prod`
- `PORT` (Railway la imposta automaticamente)
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `DB_POOL_MAX_SIZE` (opzionale, default 20)
- `PRINT_ENABLED=false` (consigliato su Railway)

## PostgreSQL
1. Aggiungi plugin PostgreSQL su Railway.
2. Copia host/porta/db/user/password nelle variabili `DB_*`.
3. Avvia deploy: Flyway è attivo e JPA è `ddl-auto=validate`.

## Avvio locale/prod
- Locale: `mvn spring-boot:run` (profilo default `local`).
- Prod-like locale: `SPRING_PROFILES_ACTIVE=prod DB_URL=... DB_USERNAME=... DB_PASSWORD=... mvn spring-boot:run`.

## Nota stampa termica
La stampa TCP usa IP `192.168.1.10:9100` (rete locale).
Su Railway non è raggiungibile, quindi lascia `PRINT_ENABLED=false`.
In locale puoi mettere `PRINT_ENABLED=true`.
