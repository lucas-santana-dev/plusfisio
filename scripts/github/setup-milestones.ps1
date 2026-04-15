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

$milestones = @(
    @{
        Title = "M1 - Fundação técnica"
        Description = "Auth, tenant context, onboarding and technical foundation."
    }
    @{
        Title = "M2 - Operação básica"
        Description = "Clientes, agenda e remarcação para operação básica do negócio."
    }
    @{
        Title = "M3 - Core de valor"
        Description = "Confirmação, pacotes, presença e falta."
    }
    @{
        Title = "M4 - Fechamento do MVP"
        Description = "Cobrança simples, lembretes e home operacional."
    }
)

$existingMilestones = Invoke-Gh api "repos/$Repo/milestones?state=all&per_page=100" | ConvertFrom-Json

foreach ($milestone in $milestones) {
    $match = $existingMilestones | Where-Object { $_.title -eq $milestone.Title }
    if ($match) {
        Write-Host "Milestone already exists: $($milestone.Title)"
        continue
    }

    Write-Host "Creating milestone: $($milestone.Title)"
    Invoke-Gh api "repos/$Repo/milestones" `
        --method POST `
        --raw-field title="$($milestone.Title)" `
        --raw-field description="$($milestone.Description)" | Out-Null
}

Write-Host "Milestones are configured for $Repo"
