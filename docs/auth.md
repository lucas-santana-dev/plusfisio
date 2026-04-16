# PlusFisio - Auth Foundation

## Objective

This document describes the initial authentication foundation delivered in branch `feature/auth-session-tenant-gate`.

The goal of this step was:

- replace the login placeholder behavior with a real flow contract
- add a session-based app entry gate
- prepare the app for real onboarding and a real home screen
- keep Firebase integration isolated behind a repository contract

## Implemented structure

### Packages

- `features/auth/domain`
- `features/auth/data`
- `features/auth/presentation`
- `root/presentation`

### Main contracts

- `AuthSession`
- `AuthRepository`
- `AuthError`
- `SignInUseCase`
- `ObserveAuthSessionUseCase`
- `SignOutUseCase`

### Shared result wrapper

`core/domain/Result.kt` now provides typed success and failure handling for expected errors.

## Current app flow

App startup flow:

1. Splash
2. Resolve current session
3. Route to login, onboarding placeholder, or home template

Routing rules:

- no session -> login
- session without `studioId` -> onboarding placeholder
- session with `studioId` -> home template

## Current provider

The default implementation is now `FirebaseAuthRepository`.

Current behavior:

1. authenticate with Firebase Auth using email and password
2. read `users/{uid}` from Cloud Firestore
3. create the document if it does not exist yet
4. build `AuthSession` with `email`, `displayName`, and `studioId`

If `studioId` is null, the app routes to the onboarding placeholder.

## Fallback and local development

`FakeAuthRepository` can still be useful for isolated local development, but it is no longer the default binding.

If you need to temporarily switch back:

1. change the binding in `authDataModule`
2. keep the same `AuthRepository` contract
3. leave presentation and app root untouched

Presentation should remain unchanged:

- `LoginViewModel`
- `LoginRoot`
- `AppRootViewModel`
- onboarding placeholder route
- home template route

## Out of scope for this step

- real studio registration
- real password recovery
- production logout integration
- operational home data
- full onboarding form

## Platform note

Android uses local Firebase config when `composeApp/google-services.json` exists.

iOS already has:

- fixed bundle identifier
- guarded `FirebaseApp.configure()` in `iOSApp.swift`

What still depends on Xcode:

- adding a local `GoogleService-Info.plist` to the app target
- adding the native Firebase Apple package so `FirebaseCore` is available in the project

## Test coverage

- login validation
- login success event
- login failure event
- loading state during sign-in
- route without session
- route with session and no `studioId`
- route with session and `studioId`
