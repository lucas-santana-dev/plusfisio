param(
    [string]$Owner = "lucas-santana-dev",
    [string]$Repo = "lucas-santana-dev/plusfisio",
    [string]$ProjectTitle = "PlusFisio MVP",
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

$projectList = Invoke-Gh project list --owner $Owner --format json | ConvertFrom-Json
$existingProject = $projectList.projects | Where-Object { $_.title -eq $ProjectTitle } | Select-Object -First 1

if ($existingProject) {
    $projectNumber = [string]$existingProject.number
    Write-Host "Project already exists: $ProjectTitle (#$projectNumber)"
}
else {
    Write-Host "Creating project: $ProjectTitle"
    $createdProject = Invoke-Gh project create --owner $Owner --title $ProjectTitle --format json | ConvertFrom-Json
    $projectNumber = [string]$createdProject.number
}

Write-Host "Linking project to repository: $Repo"
Invoke-Gh project link $projectNumber --owner $Owner --repo $Repo | Out-Null

Write-Host ""
Write-Host "Project ready: $ProjectTitle (#$projectNumber)"
Write-Host "Next manual steps in GitHub UI:"
Write-Host "1. Create Status options: Backlog, Ready, In Progress, Blocked, In Review, Done"
Write-Host "2. Create fields: Type, Priority, Area, Sprint, Target Release"
Write-Host "3. Set Sprint as a weekly iteration field"
Write-Host "4. Create saved views: Backlog, Current Sprint, In Progress, Blocked, Release Focus, Done This Sprint"
