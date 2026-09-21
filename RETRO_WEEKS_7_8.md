# JackMart — Weeks 7–8 Retro

## What worked
- Core F1–F8 implementation from Weeks 1–6 provided the base for hardening and deployment.
- Security checks and automated regression tests were added before the full-build review.
- A repeatable WAR deployment and smoke-test path was documented.

## What did not work
- Live deployment details depend on the hosting environment and cannot be verified from source code alone.
- The actual public URL, TLS certificate and uptime must be configured on the selected server.

## One change for the next sprint
- Keep a short live smoke-test record after every deployment and keep a backup demo video before each review.
