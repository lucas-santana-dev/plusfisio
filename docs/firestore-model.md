# PlusFisio - Firestore Model

## Objective

Define the initial Firestore model for the MVP with focus on:

- multi-tenant isolation by `studioId`
- low read/write cost on the Spark plan
- simple but defensible security rules
- operational queries for schedule, clients, packages, and payments

## Canonical structure

### Global profile

- `users/{uid}`

Minimum fields:

- `userId`
- `email`
- `displayName`
- `studioId`
- `role`
- `createdAtEpochMillis`
- `updatedAtEpochMillis`

### Tenant root

- `studios/{studioId}`

Minimum fields:

- `studioId`
- `name`
- `businessType`
- `ownerUserId`
- `timezone`
- `phone`
- `whatsappPhone`
- `createdAtEpochMillis`
- `updatedAtEpochMillis`
- `isActive`

### Membership

- `studios/{studioId}/members/{uid}`

Minimum fields:

- `userId`
- `studioId`
- `role`
- `displayName`
- `email`
- `isActive`
- `createdAtEpochMillis`
- `updatedAtEpochMillis`

### Clients

- `studios/{studioId}/clients/{clientId}`

Minimum fields:

- `clientId`
- `studioId`
- `fullName`
- `phone`
- `whatsappPhone`
- `notes`
- `status`
- `searchName`
- `createdAtEpochMillis`
- `updatedAtEpochMillis`

### Appointments

- `studios/{studioId}/appointments/{appointmentId}`

Minimum fields:

- `appointmentId`
- `studioId`
- `clientId`
- `clientNameSnapshot`
- `professionalUserId`
- `professionalNameSnapshot`
- `startsAtEpochMillis`
- `endsAtEpochMillis`
- `dayKey`
- `status`
- `confirmationStatus`
- `packageId`
- `notes`
- `createdAtEpochMillis`
- `updatedAtEpochMillis`

### Packages

- `studios/{studioId}/packages/{packageId}`

Minimum fields:

- `packageId`
- `studioId`
- `clientId`
- `clientNameSnapshot`
- `title`
- `sessionCountTotal`
- `sessionCountRemaining`
- `status`
- `startDateEpochMillis`
- `expiresAtEpochMillis`
- `createdAtEpochMillis`
- `updatedAtEpochMillis`

### Package ledger

- `studios/{studioId}/packageLedger/{entryId}`

Minimum fields:

- `entryId`
- `studioId`
- `packageId`
- `clientId`
- `appointmentId`
- `type`
- `delta`
- `reason`
- `createdAtEpochMillis`
- `createdByUserId`

### Payments

- `studios/{studioId}/payments/{paymentId}`

Minimum fields:

- `paymentId`
- `studioId`
- `clientId`
- `clientNameSnapshot`
- `packageId`
- `amountCents`
- `currency`
- `dueDateEpochMillis`
- `status`
- `description`
- `paidAtEpochMillis`
- `createdAtEpochMillis`
- `updatedAtEpochMillis`

## Spark strategy

- keep operational queries inside each studio subtree
- avoid dashboard aggregations for now
- avoid speculative indexes
- use name snapshots in operational documents to reduce extra reads
- keep payments and appointments in one collection per studio

## Tenant bootstrap

The onboarding flow is the step that materializes the backend:

1. create `studios/{studioId}`
2. create `studios/{studioId}/members/{uid}`
3. update `users/{uid}` with `studioId` and `role`

The remaining collections are created naturally as the first operational records are written.
