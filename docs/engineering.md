# PlusFisio - Engineering Workflow

## Objective

This document defines the Git workflow for PlusFisio so the team can work on the MVP with clear integration, release, and production branches.

The process is intentionally lightweight. It favors MVP speed and release clarity over a heavy GitFlow setup.

## Branches

- `main`: production branch. It represents what is approved for release and should stay stable.
- `develop`: integration branch. All regular product work lands here before a release.
- `feature/<scope>`: new feature branch created from `develop`.
- `fix/<scope>`: non-urgent correction branch created from `develop`.
- `release/vX.Y.0`: temporary stabilization branch created from `develop` when preparing a release.
- `hotfix/vX.Y.Z-<scope>`: urgent production fix branch created from `main`.

## Branch Flow

### Regular feature work

1. Create a branch from `develop` using `feature/<scope>` or `fix/<scope>`.
2. Open a PR into `develop`.
3. Merge only after review and required validation.

Example branch names:

- `feature/auth-login`
- `feature/schedule-day-view`
- `fix/empty-home-state`

### Release preparation

1. Create `release/vX.Y.0` from `develop`.
2. Allow only stabilization work in this branch:
   - bug fixes
   - copy adjustments
   - final validation
3. Open a PR from `release/vX.Y.0` into `main`.
4. After merging into `main`, create the Git tag for the version.
5. Merge `main` back into `develop` to keep both branches aligned.

### Hotfixes

1. Create `hotfix/vX.Y.Z-<scope>` from `main`.
2. Open a PR into `main`.
3. Tag the new patch release after merge.
4. Merge `main` back into `develop`.

## Versioning

PlusFisio uses Semantic Versioning with Git tags.

- Pre-1.0 releases use `0.MINOR.PATCH`
- Stable product starts at `1.0.0`
- Tags use the format `vX.Y.Z`

### Rules

- Increment `MINOR` for new MVP capabilities or grouped product evolution.
- Increment `PATCH` for corrections without meaningful scope expansion.
- Increment `MAJOR` only for a stable release or breaking product changes after `1.0.0`.

Examples:

- `v0.1.0`: first internal integrated milestone
- `v0.2.0`: new MVP workflow added
- `v0.2.1`: release correction
- `v1.0.0`: first production-ready commercial release

## Pull Requests

### Target branches

- `feature/*` -> `develop`
- `fix/*` -> `develop`
- `release/*` -> `main`
- `hotfix/*` -> `main`

### PR title format

Use:

`<type>: <short summary>`

Examples:

- `feat: add login validation`
- `fix: handle empty schedule state`
- `docs: define git workflow`

Recommended types:

- `feat`
- `fix`
- `refactor`
- `docs`
- `test`
- `chore`

### Minimum merge criteria

- PR follows the template
- branch target is correct
- no direct push to `main` or `develop`
- relevant build/checks were executed
- affected tests were executed when they exist
- at least one review was completed
- known risks are documented in the PR

## Branch Protection

Configure these rules in the Git hosting provider for both `main` and `develop`:

- block direct pushes
- require pull requests before merge
- require at least one approval
- require branches to be up to date before merge when supported

For `main`, additionally prefer:

- squash or merge only through reviewed PRs
- tags created only from merged release or hotfix work

## Recommended Commands

Create and switch to `develop` locally:

```bash
git checkout main
git pull origin main
git checkout -b develop
git push -u origin develop
```

Create a feature branch:

```bash
git checkout develop
git pull origin develop
git checkout -b feature/auth-login
```

Create a release branch:

```bash
git checkout develop
git pull origin develop
git checkout -b release/v0.1.0
```

Tag a release:

```bash
git checkout main
git pull origin main
git tag -a v0.1.0 -m "Release v0.1.0"
git push origin v0.1.0
```
