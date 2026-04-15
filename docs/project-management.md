# PlusFisio - Project Management

## Objective

This document defines how PlusFisio work is organized in GitHub for a solo development workflow.

The goal is to keep planning visible and disciplined without adding unnecessary overhead to the MVP.

## Source of Truth

Use GitHub as the primary operational workspace:

- GitHub Issues for work items
- GitHub Projects for board and sprint visualization
- GitHub Milestones for MVP progress tracking
- Pull Requests linked back to issues

The repository documentation defines the process. The GitHub Project is the day-to-day operational view.

## Work Item Model

PlusFisio uses a light hierarchy:

- `Epic`: a major MVP capability or milestone-sized block
- `Story`: a user-facing deliverable with acceptance criteria
- `Task`: technical implementation work that supports a story
- `Bug`: a defect in current behavior
- `Chore`: maintenance, setup, or non-product technical work

### Rules

- Every board item should be a GitHub issue.
- `Epic` is used for grouping and progress visibility, not as the main daily execution item.
- `Story` is the preferred unit for planning sprint outcomes.
- `Task` should stay small enough to complete without dragging through multiple sprints.
- `Bug` can be created directly and prioritized based on impact.

## GitHub Project Setup

Create one project called `PlusFisio MVP`.

### Status column

Use these statuses:

- `Backlog`
- `Ready`
- `In Progress`
- `Blocked`
- `In Review`
- `Done`

### Custom fields

Create these fields in the project:

- `Type`: `Epic`, `Story`, `Task`, `Bug`, `Chore`
- `Priority`: `P0`, `P1`, `P2`
- `Area`: `Auth`, `Onboarding`, `Home`, `Agenda`, `Clientes`, `Pacotes`, `Financeiro`, `Core`, `Infra`
- `Sprint`: weekly iteration field
- `Target Release`: version target such as `v0.1.0` or `v0.2.0`

### Recommended views

- `Backlog`: all items not in the active sprint
- `Current Sprint`: items in the current weekly iteration
- `In Progress`: active work only
- `Blocked`: issues that need resolution
- `Release Focus`: filtered by target release
- `Done This Sprint`: completed items in the current iteration

## Sprint Model

Use a weekly sprint cadence.

### Weekly rhythm

Monday:

- review backlog
- pick stories and tasks for the week
- assign sprint and priority

During the week:

- keep only one main story in progress
- move blocked work explicitly to `Blocked`
- create follow-up tasks instead of overloading the current issue

Friday:

- close done work
- review unfinished items
- move leftovers back to `Backlog` or next sprint intentionally

### Solo WIP limits

- maximum `1` main story in `In Progress`
- maximum `2` active tasks at the same time
- avoid starting a new story before the current one is in review or done

## Milestones and MVP Mapping

Create milestones aligned to the MVP roadmap:

- `M1 - Fundação técnica`
- `M2 - Operação básica`
- `M3 - Core de valor`
- `M4 - Fechamento do MVP`

Suggested mapping:

- `M1`: auth, tenant context, onboarding
- `M2`: clientes, agenda, remarcação
- `M3`: confirmação, pacotes, presença, falta
- `M4`: cobrança simples, lembretes, home operacional

## Labels

Create these labels in GitHub.

### Type

- `type: epic`
- `type: story`
- `type: task`
- `type: bug`
- `type: chore`

### Priority

- `priority: p0`
- `priority: p1`
- `priority: p2`

### Area

- `area: auth`
- `area: onboarding`
- `area: home`
- `area: agenda`
- `area: clientes`
- `area: pacotes`
- `area: financeiro`
- `area: core`
- `area: infra`

### Workflow helpers

- `blocked`
- `needs-refinement`
- `release-critical`

## Working Conventions

- Issue titles should be short and outcome-oriented.
- Stories should include acceptance criteria.
- Tasks should reference a parent story or epic when relevant.
- Pull requests should close issues with references such as `Closes #12`.
- Branches should reflect the issue scope and follow the Git workflow defined in `docs/engineering.md`.
- A story should only be closed when the behavior is implemented and minimally validated.

## Suggested Initial Epics

Seed the board with these epics:

- `M1 - Fundação técnica`
- `M2 - Operação básica`
- `M3 - Core de valor`
- `M4 - Fechamento do MVP`

Under those epics, create the first stories from `docs/mvp.md`:

- login e sessão
- onboarding do negócio
- cadastro de clientes
- agenda diária e semanal
- criação e remarcação de agendamento
- confirmação manual e via WhatsApp
- pacotes e saldo de sessões
- presença e falta
- cobrança simples
- lembretes de vencimento
- home operacional

## GitHub Configuration Checklist

Once GitHub access is authenticated, configure:

- project `PlusFisio MVP`
- project statuses and custom fields
- weekly iterations
- milestones `M1` to `M4`
- labels listed in this document
- issue auto-add rules to the project when supported

This setup should remain intentionally small. If the board starts demanding too much maintenance, simplify before adding more process.

## Automation

The repository includes helper scripts for the GitHub setup:

- `scripts/github/setup-labels.ps1`
- `scripts/github/setup-milestones.ps1`
- `scripts/github/create-project.ps1`

Recommended order:

```powershell
gh auth login -h github.com
gh auth refresh -h github.com -s project

powershell -ExecutionPolicy Bypass -File .\scripts\github\setup-labels.ps1
powershell -ExecutionPolicy Bypass -File .\scripts\github\setup-milestones.ps1
powershell -ExecutionPolicy Bypass -File .\scripts\github\create-project.ps1
```

If the local `gh` session is authenticated in your terminal but the script still cannot read the keyring token, run the scripts by explicitly exporting a token in the same shell:

```powershell
$env:GH_TOKEN = "YOUR_TOKEN_HERE"

powershell -ExecutionPolicy Bypass -File .\scripts\github\setup-labels.ps1 -GhToken $env:GH_TOKEN
powershell -ExecutionPolicy Bypass -File .\scripts\github\setup-milestones.ps1 -GhToken $env:GH_TOKEN
powershell -ExecutionPolicy Bypass -File .\scripts\github\create-project.ps1 -GhToken $env:GH_TOKEN
```

After the scripts run, complete the remaining project field setup in the GitHub UI:

1. Open the `PlusFisio MVP` project.
2. Add status options: `Backlog`, `Ready`, `In Progress`, `Blocked`, `In Review`, `Done`.
3. Add custom fields:
   - `Type`: `Epic`, `Story`, `Task`, `Bug`, `Chore`
   - `Priority`: `P0`, `P1`, `P2`
   - `Area`: `Auth`, `Onboarding`, `Home`, `Agenda`, `Clientes`, `Pacotes`, `Financeiro`, `Core`, `Infra`
   - `Sprint`: weekly iteration
   - `Target Release`: text or single select
4. Create the saved views defined earlier in this document.
