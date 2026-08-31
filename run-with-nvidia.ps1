$ErrorActionPreference = 'Stop'

if ([string]::IsNullOrWhiteSpace($env:NVIDIA_API_KEY)) {
    $secureKey = Read-Host 'Paste your NVIDIA API key (it will not be saved)' -AsSecureString
    $bstr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($secureKey)
    try {
        $env:NVIDIA_API_KEY = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($bstr)
    }
    finally {
        [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($bstr)
    }
}

$workspaceRoot = Split-Path -Parent $PSScriptRoot
$localMaven = Join-Path $workspaceRoot '.tools\apache-maven-3.9.9\bin\mvn.cmd'

if (Test-Path $localMaven) {
    & $localMaven spring-boot:run
}
elseif (Get-Command mvn -ErrorAction SilentlyContinue) {
    mvn spring-boot:run
}
else {
    throw 'Maven was not found. Install Maven or restore .tools\\apache-maven-3.9.9.'
}
