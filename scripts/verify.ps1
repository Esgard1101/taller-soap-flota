[CmdletBinding()]
param(
    [switch]$Offline
)

$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path -Parent $PSScriptRoot
$localConfig = Join-Path $projectRoot 'config/local.ps1'
if (Test-Path -LiteralPath $localConfig) {
    . $localConfig
}

if ([string]::IsNullOrWhiteSpace($env:JAVA_HOME)) {
    throw 'Configura JAVA_HOME con un JDK 21 (config/local.ps1 o variables de la terminal).'
}
$javaCommand = Join-Path $env:JAVA_HOME 'bin/java.exe'
$javacCommand = Join-Path $env:JAVA_HOME 'bin/javac.exe'
if (!(Test-Path -LiteralPath $javaCommand) -or !(Test-Path -LiteralPath $javacCommand)) {
    throw 'JAVA_HOME debe apuntar a un JDK completo con java.exe y javac.exe.'
}
$versionOutput = & $javaCommand -version 2>&1
if ($LASTEXITCODE -ne 0 -or ($versionOutput -join ' ') -notmatch 'version "21[."]') {
    throw ('Se requiere JDK 21. Version detectada: ' + ($versionOutput -join ' '))
}

if (![string]::IsNullOrWhiteSpace($env:MAVEN_HOME)) {
    $mavenCommand = Join-Path $env:MAVEN_HOME 'bin/mvn.cmd'
    if (!(Test-Path -LiteralPath $mavenCommand)) {
        throw 'MAVEN_HOME no contiene bin/mvn.cmd.'
    }
} else {
    $mavenInfo = Get-Command mvn.cmd -ErrorAction SilentlyContinue
    if (!$mavenInfo) {
        throw 'Configura MAVEN_HOME o agrega Maven al PATH.'
    }
    $mavenCommand = $mavenInfo.Source
}

$mavenArguments = @('--batch-mode', '--no-transfer-progress')
if ($Offline) {
    $mavenArguments += '--offline'
}
$mavenArguments += 'verify'

Push-Location $projectRoot
try {
    & $mavenCommand @mavenArguments
    if ($LASTEXITCODE -ne 0) {
        throw ('Maven termino con codigo ' + $LASTEXITCODE)
    }
} finally {
    Pop-Location
}

