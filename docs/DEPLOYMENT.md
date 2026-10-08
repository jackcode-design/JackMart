# JackMart deployment (Render + Docker + Tomcat 9)

> No public JackMart deployment exists yet. Do not claim a live URL, uptime, or
> deployment success until the steps below are completed and the health endpoint
> on the real URL returns HTTP 200 with `status = UP` and `db = UP`.

## What gets deployed
- `target/jackmart.war`, built from source by `Dockerfile` (Maven + Temurin 17,
  then Tomcat 9 + JDK 17). Nothing deploys automatically; you trigger it below.
- Context path is `/jackmart` (it comes from the WAR filename `jackmart.war`).
- Database is the existing H2 architecture — no database replacement.
  Default JDBC URL is the embedded file `jdbc:h2:./data/jackmart`, resolved under
  the container working directory (`/usr/local/tomcat`).

## H2 persistence (read before choosing a plan)
- Render free-tier containers use an ephemeral filesystem: a restart wipes
  `./data/jackmart` and the app reseeds demo data on next boot. Fine for a
  review demo, not durable storage.
- For data that survives restarts, attach a Render persistent Disk mounted at
  `/usr/local/tomcat/data` and set
  `DB_URL=jdbc:h2:/usr/local/tomcat/data/jackmart;DB_CLOSE_DELAY=-1`
  (absolute path inside the container). Disk availability and pricing are Render
  paid features — verify current terms in the dashboard before relying on them.
- Never commit real passwords, API keys, or secrets. All production secrets come
  from Render environment variables (table below). `.env`, `config.properties`,
  `data/` and `*.db` are git-ignored; `.env.example` documents every variable.

## Render environment variables
Set these under Service → Environment (names must match exactly; the app reads
each `lower.case.key` from its `UPPER_SNAKE` environment override):

| Variable | Required | Purpose / value |
|---|---|---|
| `PORT` | Set by Render | Container port; the Dockerfile remaps Tomcat 8080 → `$PORT` at startup |
| `DB_URL` | No | JDBC URL. Leave unset for the embedded file, or set the disk-backed absolute URL above |
| `DB_USER` | No | DB user (default `sa`) |
| `DB_PASSWORD` | No | DB password (default empty; set a real one with a disk-backed DB) |
| `AI_CHATBOT_PROVIDER` | No | `mock` (default, works offline) or `gemini` |
| `GEMINI_API_KEY` | Only with `gemini` | Server-side only; never put it in JSP/JS |
| `GEMINI_API_TIMEOUT_MS` | No | Outbound AI timeout in ms (default `8000`) |
| `SESSION_TIMEOUT_MINUTES` | No | Login session timeout (default `30`) |

## Render dashboard steps (do these yourself)
1. Commit and push this repo to GitHub (`mvn -B clean verify` green first).
2. Render → New → Web Service → select the JackMart repo.
3. Runtime: **Docker**. Dockerfile Path: `./Dockerfile`. Docker Build Context: repository root.
4. Health Check Path: `/jackmart/api/v1/health`.
5. Add the environment variables from the table above (at minimum, none are
   mandatory for a first boot; add `GEMINI_API_KEY` only if you use `gemini`).
6. Optional: add a Disk for H2 persistence (see above) and set the matching `DB_URL`.
7. Create Web Service and wait for the build + deploy to finish.
8. Open the service URL and run the verification below before announcing it.

## Verify after deploy (do not skip or fabricate)
- `GET <your-service-url>/jackmart/api/v1/health` → HTTP 200 with
  `{"status":"UP","db":"UP"}` (the servlet runs a real `SELECT 1`).
- End to end: register → login → browse/search → cart → mock checkout → order
  history → seller listing + incoming order → admin view → delivered review.
- Role checks: buyer gets 403 on `/seller/*` and `/admin/*`.
- Chatbot widget answers domain FAQs (mock works with no key).

## Local Docker check (requires Docker installed)
```bash
docker build -t jackmart .
docker run --rm -p 8080:8080 jackmart
# then open http://localhost:8080/jackmart/ and
# http://localhost:8080/jackmart/api/v1/health
```

## Backup
- Embedded file: copy the H2 files, or from an H2 shell run
  `SCRIPT TO 'backup.sql'` and restore with `RUNSCRIPT FROM 'backup.sql'`.
- Re-run the demo video checklist below after every redeploy.

## Demo video checklist (record before review)
1. Register buyer → login → browse/search/filter → product detail
2. Add/update/remove cart → checkout mock payment → order success
3. Buyer history → seller login → create/edit listing → incoming order → advance status
4. Delivered → buyer review → appears on product
5. Admin login → users/orders → remove listing; show 403 for buyer on admin URL
6. Chatbot widget Q&A; health endpoint; `mvn -B clean verify` green
