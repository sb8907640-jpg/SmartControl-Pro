# SmartControl Pro

**Consent-Based Device Management Platform**

## ⚠️ IMPORTANT — CONSENT-BASED ONLY

This application works **ONLY** with explicit user consent.

- ✅ Every feature requires **explicit permission**
- ✅ User sees **exactly what they are allowing**
- ✅ User has **STOP button** on every screen
- ✅ User can **revoke consent anytime**
- ✅ **No hidden features** — everything is visible
- ✅ **No background spying** — all actions are logged and visible

## Features (All Consent-Based)

| Feature | Consent Required | Visible to User |
|---------|-----------------|-----------------|
| Location | ✅ Yes | ✅ Always |
| Camera | ✅ Yes | ✅ Always |
| Mic | ✅ Yes | ✅ Always |
| Screen Recording | ✅ Yes | ✅ Always |
| File Access | ✅ Yes | ✅ Always |
| Gallery Access | ✅ Yes | ✅ Always |
| SMS (view) | ✅ Yes | ✅ Always |
| Call Logs (view) | ✅ Yes | ✅ Always |
| Contacts (view) | ✅ Yes | ✅ Always |
| Clipboard | ✅ Yes | ✅ Always |

## Architecture

- **Backend:** Node.js + Express + PostgreSQL
- **Android Owner:** Kotlin + Jetpack Compose
- **Android Receiver Lite:** Kotlin + Jetpack Compose (≤8MB)
- **Web Panel:** Next.js 14 + TypeScript
- **Database:** PostgreSQL + Firestore

## Quick Start

```bash
# Backend
cd backend
npm install
cp .env.example .env
npm run migrate
npm run dev

# Android Owner
cd android-owner
./gradlew assembleDebug

# Android Receiver Lite
cd android-receiver-lite
./gradlew assembleDebug

# Web Panel
cd web-panel
npm install
npm run dev