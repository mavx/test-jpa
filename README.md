# test-jpa
## Expected Behaviour
Once application is started, it will insert 10k rows every 10s, and pick 5k rows to update every 5s. The printed Hibernate statistics should help observe whether statements are being batched to PostgreSQL


## Usage
Run `docker-compose.yml` to startup PostgreSQL DB before running `Application.java`
```
docker compose up -d
```
