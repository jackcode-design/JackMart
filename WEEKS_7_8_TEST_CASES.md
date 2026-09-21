# JackMart — Week 8 Deployment Runbook

## Target
Tomcat 9.0.x + JDK 17 on a Linux server, with H2 data persisted on disk.

## Build
From the project root:

```bash
mvn clean package
```

Expected artifact:

```text
target/jackmart.war
```

## Environment
Set these variables on the server:

```bash
export JACKMART_DB_URL='jdbc:h2:tcp://localhost:9092/./data/jackmart'
export JACKMART_DB_USER='sa'
export JACKMART_DB_PASSWORD='CHANGE_ME'
```

Never commit real credentials.

## Deploy
The supplied script:

```bash
deploy/scripts/deploy.sh
```

expects:
- `TOMCAT_HOME` — Tomcat installation directory
- `WAR_FILE` — optional WAR path; defaults to `target/jackmart.war`
- `APP_NAME` — optional; defaults to `jackmart`

Example:

```bash
export TOMCAT_HOME=/opt/tomcat
export WAR_FILE=target/jackmart.war
deploy/scripts/deploy.sh
```

## Health verification
After Tomcat starts:

```bash
curl -fsS http://localhost:8080/jackmart/api/v1/health
```

Expected shape:

```json
{"status":"UP","db":"UP"}
```

## Reverse proxy
`deploy/nginx/jackmart.conf` is a starting template. Configure DNS and TLS according to the selected hosting provider.

## Backup
Before each review:

```bash
cp /opt/jackmart/data/jackmart.mv.db /opt/jackmart/backup/jackmart-$(date +%Y%m%d-%H%M).mv.db
```

Keep backups outside the deployed WAR directory.

## Review-day checklist
- [ ] Public URL opens.
- [ ] Health endpoint returns DB UP.
- [ ] Register/login works.
- [ ] Browse/search works.
- [ ] Cart and checkout work.
- [ ] Buyer order history works.
- [ ] Seller listing/order status works.
- [ ] Admin panel works.
- [ ] Review flow works.
- [ ] Screenshot of live URL captured.
- [ ] 2–3 minute backup demo video recorded.
