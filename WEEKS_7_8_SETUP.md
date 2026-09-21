# JackMart — Week 8: Deployment + Smoke Testing
**Window:** Sep 14 – Sep 20, 2026

## Goal
Prepare the application for the Sep 21 full-build review with a reproducible Tomcat deployment and a documented smoke-test process.

## Work completed in this package
1. Add a Linux/Tomcat deployment script.
2. Add Tomcat environment configuration template.
3. Add Nginx reverse-proxy configuration template for HTTPS.
4. Add live health endpoint verification script.
5. Add full smoke-test checklist for buyer, seller, admin and review flows.
6. Add backup instructions for the H2 `.mv.db` file.
7. Update deployment and review documentation.

## Suggested commits
1. `feat: add reproducible tomcat deployment`
2. `test: add live health and smoke test checklist`
3. `docs: finalize deployment and review package`

## Definition of Done
- `mvn clean package` creates `target/jackmart.war`.
- WAR deploys to Tomcat 9.
- `/api/v1/health` reports application and DB status.
- Buyer, seller and admin journeys are smoke-tested.
- H2 data is backed up before the review.
- The public review URL is recorded in README.
