param(
    [string]$Repo = "lucas-santana-dev/plusfisio",
    [string]$GhToken = ""
)

$ErrorActionPreference = "Stop"

if ($GhToken) {
    $env:GH_TOKEN = $GhToken
}

function Invoke-Gh {
    param(
        [Parameter(ValueFromRemainingArguments = $true)]
        [string[]]$Args
    )

    $output = & gh @Args
    if ($LASTEXITCODE -ne 0) {
        throw "gh command failed: gh $($Args -join ' ')"
    }

    return $output
}

$labels = @(
    @{ Name = "type: epic"; Color = "5319E7"; Description = "Major MVP capability or milestone-sized block" }
    @{ Name = "type: story"; Color = "1D76DB"; Description = "User-facing deliverable with acceptance criteria" }
    @{ Name = "type: task"; Color = "0E8A16"; Description = "Technical work item that supports a story" }
    @{ Name = "type: bug"; Color = "D73A4A"; Description = "Defect in current behavior" }
    @{ Name = "type: chore"; Color = "6F42C1"; Description = "Maintenance or non-product technical work" }
    @{ Name = "priority: p0"; Color = "B60205"; Description = "Critical priority" }
    @{ Name = "priority: p1"; Color = "D93F0B"; Description = "High priority" }
    @{ Name = "priority: p2"; Color = "FBCA04"; Description = "Normal priority" }
    @{ Name = "area: auth"; Color = "0052CC"; Description = "Authentication and session flows" }
    @{ Name = "area: onboarding"; Color = "006B75"; Description = "Business onboarding flows" }
    @{ Name = "area: home"; Color = "0E8A16"; Description = "Home and daily hub flows" }
    @{ Name = "area: agenda"; Color = "5319E7"; Description = "Scheduling and appointment flows" }
    @{ Name = "area: clientes"; Color = "1D76DB"; Description = "Client and patient management" }
    @{ Name = "area: pacotes"; Color = "C2E0C6"; Description = "Packages and remaining sessions" }
    @{ Name = "area: financeiro"; Color = "BFDADC"; Description = "Charges, payments, and reminders" }
    @{ Name = "area: core"; Color = "5319E7"; Description = "Shared core and architecture" }
    @{ Name = "area: infra"; Color = "D4C5F9"; Description = "Tooling, CI, and repository setup" }
    @{ Name = "blocked"; Color = "000000"; Description = "Work blocked and needing intervention" }
    @{ Name = "needs-refinement"; Color = "F9D0C4"; Description = "Needs clarification before execution" }
    @{ Name = "release-critical"; Color = "B60205"; Description = "Must be resolved for the target release" }
)

$existingLabelsJson = Invoke-Gh label list --repo $Repo --limit 200 --json name
$existingLabelNames = @()
if ($existingLabelsJson) {
    $existingLabelNames = ($existingLabelsJson | ConvertFrom-Json).name
}

foreach ($label in $labels) {
    if ($existingLabelNames -contains $label.Name) {
        Write-Host "Updating label: $($label.Name)"
        Invoke-Gh label edit $label.Name --repo $Repo --color $label.Color --description $label.Description | Out-Null
    }
    else {
        Write-Host "Creating label: $($label.Name)"
        Invoke-Gh label create $label.Name --repo $Repo --color $label.Color --description $label.Description | Out-Null
    }
}

Write-Host "Labels are configured for $Repo"
