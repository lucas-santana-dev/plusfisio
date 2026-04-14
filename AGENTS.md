# AGENTS.md

## Project
PlusFisio is a mobile-first SaaS for small Pilates studios and small physiotherapy clinics.
The initial product targets businesses with 1 to 3 professionals and operations heavily centered on phone and WhatsApp.

## Product direction
- Prioritize a realistic, sellable MVP.
- Optimize for operational simplicity over feature breadth.
- Focus the MVP on:
    - client/patient registration
    - daily/weekly schedule
    - appointment creation and rescheduling
    - attendance / no-show / reschedule tracking
    - session packages
    - remaining sessions tracking
    - simple payment tracking
    - payment due reminders
    - WhatsApp-driven confirmation flows
    - professional home dashboard
- Do not expand scope unnecessarily.
- The home screen is a prioritization hub, not a full admin dashboard.
- Favor UX that is comfortable on small phone screens.

## Platforms
- Primary platforms: Android and iOS.
- Stack: Kotlin Multiplatform + Compose Multiplatform.
- Web admin is not part of the initial MVP.
- Desktop is not a priority.
- Design every decision so future web expansion remains possible, but do not build for web now unless explicitly asked.

## Backend and external services
- Backend is Firebase.
- Prefer Firebase Authentication, Cloud Firestore, Cloud Functions, Firebase Cloud Messaging, Crashlytics, and Analytics when appropriate.
- Do not default to Ktor or Room as the project backend/data stack.
- Data modeling must consider multi-tenant behavior using `studioId`.
- Prefer simple and explicit Firebase integrations over overengineered abstractions.

## Architecture
- Use feature-layered modularization.
- Preferred direction:
    - `presentation -> domain <- data`
- Domain must stay framework-light.
- Presentation must not depend directly on data implementations.
- Keep code in a feature module unless it is clearly shared by more than one feature.
- Avoid cross-feature dependencies.
- Shared logic should move to `core` only when genuinely reused.

## Presentation rules
- Follow MVI-style presentation:
    - one `State` per screen
    - one `Action` sealed interface per screen
    - one `Event` sealed interface per screen
    - one `ViewModel` per screen or flow as appropriate
- UI is dumb:
    - composables render state
    - composables emit actions
    - business logic does not live in composables
- Root composables own ViewModel wiring.
- Screen composables receive only state and callbacks.
- Prefer previewable screen composables.
- Keep forms and operational flows simple and explicit.

## Compose rules
- All application state belongs in the ViewModel.
- Use Compose state only for Compose-owned concerns such as scroll/list state when required.
- Avoid unnecessary side effects.
- Prefer readable and stable composables over clever abstractions.
- Prioritize accessibility and realistic previews.
- Optimize for small-screen comfort:
    - avoid overly dense screens
    - avoid too many expanded sections at once
    - prefer progressive disclosure
    - use bottom sheets and expandable sections when appropriate

## Navigation
- Use type-safe navigation patterns.
- Keep one nav graph per feature where appropriate.
- Avoid tight coupling between features.
- Prefer IDs in routes instead of passing full objects.
- Cross-feature navigation should remain explicit and decoupled.

## Dependency injection
- Use Koin.
- Prefer Koin DSL for the MVP.
- Do not introduce Koin annotations unless explicitly requested.
- Keep DI simple and easy to debug.

## Data layer
- Distinguish clearly between:
    - domain models
    - data transfer/storage models
    - UI models
- Repositories should exist only when coordinating multiple data sources or behaviors.
- Prefer explicit mappers.
- Avoid leaking Firebase-specific models into domain and presentation layers.
- Keep Firestore collection/document structure understandable and easy to evolve.

## Error handling
- Use typed result/error handling instead of throwing expected exceptions.
- Prefer a shared `Result<T, E>` pattern.
- Map user-facing errors to UI-friendly text.
- Catch errors at the layer that owns them.

## Testing
- Unit-test ViewModels and non-trivial domain/data logic.
- Prefer fakes over mocks when practical.
- Write Compose UI tests for critical flows.
- Test behavior that is likely to change or break.
- Do not add heavy test complexity without real payoff.

## Firebase-specific guidance
- Authentication should be simple and pragmatic for the MVP.
- Firestore structure must support:
    - studios
    - professionals/users
    - clients/patients
    - appointments
    - packages
    - payments
- Use Cloud Functions only when they add clear value, such as centralized business rules, notifications, or scheduled tasks.
- Do not overuse Cloud Functions for logic that can stay simple in the app or Firestore rules.
- Security rules must be considered part of the architecture, not an afterthought.

## Product constraints
Do not add these to the MVP unless explicitly requested:
- full web admin
- desktop app
- advanced reports
- multi-unit/franchise support
- complex financial management
- full medical records/prontuário
- official WhatsApp API integration
- highly customizable permissions matrix
- heavy offline-first architecture

## Code style
- Prefer simple, direct, production-usable code.
- Avoid speculative abstractions.
- Avoid large rewrites when a focused change is enough.
- Keep naming consistent and descriptive.
- Prefer code that is easy for a Kotlin/Android developer to maintain.

## Workflow
Before implementing, first understand:
- the feature goal
- the user flow
- the affected modules
- whether the request belongs in MVP scope

When implementing:
- make the smallest coherent change
- preserve architecture boundaries
- update related files consistently
- avoid hidden side effects

When reviewing or planning:
- call out trade-offs clearly
- point out risks early
- prefer pragmatic decisions over theoretically perfect ones

## Done criteria
A task is only considered done when applicable:
- architecture boundaries are respected
- code compiles
- affected flows are covered by tests or at least reviewed for testability
- navigation and state handling are coherent
- strings and error states are handled
- changes remain aligned with the PlusFisio MVP

## Commands
When relevant, prefer checking and mentioning the commands needed to validate the work in this repo, such as:
- build
- unit tests
- lint
- platform-specific run instructions

If commands are not yet defined in the repo, do not invent fake commands. Instead, note what should be added.

## Guidance on output
When planning:
- be structured
- be pragmatic
- favor sellable MVP decisions

When coding:
- explain important trade-offs briefly
- avoid unnecessary verbosity
- do not introduce technology that conflicts with the chosen stack

## Priority reminder
For PlusFisio, always optimize first for:
1. MVP speed
2. product clarity
3. mobile usability
4. maintainable Kotlin architecture
5. future extensibility without overengineering