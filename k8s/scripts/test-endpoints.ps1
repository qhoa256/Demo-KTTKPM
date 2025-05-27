# PowerShell script for testing Costume Rental System endpoints on Windows
# Author: Augment Agent
# Description: Tests all frontend and API endpoints

param(
    [string]$BaseUrl = "http://costume-rental.local"
)

# Color functions for better output
function Write-ColorOutput($ForegroundColor) {
    $fc = $host.UI.RawUI.ForegroundColor
    $host.UI.RawUI.ForegroundColor = $ForegroundColor
    if ($args) {
        Write-Output $args
    } else {
        $input | Write-Output
    }
    $host.UI.RawUI.ForegroundColor = $fc
}

function Write-Success { Write-ColorOutput Green $args }
function Write-Error { Write-ColorOutput Red $args }
function Write-Warning { Write-ColorOutput Yellow $args }
function Write-Info { Write-ColorOutput Cyan $args }

$BASE_URL = $BaseUrl

Write-Info "🧪 Testing Costume Rental System Endpoints"
Write-Info "Base URL: $BASE_URL"
Write-Info ""

# Function to test endpoint
function Test-Endpoint {
    param(
        [string]$Name,
        [string]$Url,
        [string]$ExpectedContent = $null
    )
    
    Write-Info "Testing $Name..."
    Write-Info "URL: $Url"
    
    try {
        $response = Invoke-WebRequest -Uri $Url -UseBasicParsing -TimeoutSec 10
        
        if ($response.StatusCode -eq 200) {
            Write-Success "✅ $Name - Status: $($response.StatusCode)"
            
            if ($ExpectedContent) {
                if ($response.Content -like "*$ExpectedContent*") {
                    Write-Success "   Content check passed"
                } else {
                    Write-Warning "   Content check failed - expected: $ExpectedContent"
                }
            }
            
            # Show first 100 characters of response
            $preview = $response.Content.Substring(0, [Math]::Min(100, $response.Content.Length))
            Write-Info "   Preview: $($preview)..."
        } else {
            Write-Warning "⚠️ $Name - Status: $($response.StatusCode)"
        }
    }
    catch {
        Write-Error "❌ $Name - Error: $($_.Exception.Message)"
    }
    
    Write-Info ""
}

# Function to test JSON API endpoint
function Test-JsonEndpoint {
    param(
        [string]$Name,
        [string]$Url
    )
    
    Write-Info "Testing $Name..."
    Write-Info "URL: $Url"
    
    try {
        $response = Invoke-RestMethod -Uri $Url -Method Get -TimeoutSec 10
        
        Write-Success "✅ $Name - JSON Response received"
        
        # Display JSON response
        if ($response) {
            $jsonOutput = $response | ConvertTo-Json -Depth 3
            Write-Info "   Response: $jsonOutput"
        }
    }
    catch {
        Write-Error "❌ $Name - Error: $($_.Exception.Message)"
    }
    
    Write-Info ""
}

Write-Info "🌐 Testing Frontend Endpoints:"
Write-Info "=" * 50

# Test frontend endpoints
Test-Endpoint -Name "Frontend Root" -Url "$BASE_URL/" -ExpectedContent "html"
Test-Endpoint -Name "Login Page" -Url "$BASE_URL/login" -ExpectedContent "login"

Write-Info ""
Write-Info "🔌 Testing API Endpoints:"
Write-Info "=" * 50

# Test API endpoints
Test-JsonEndpoint -Name "User Service API" -Url "$BASE_URL/api/users/"
Test-JsonEndpoint -Name "Costume Service API" -Url "$BASE_URL/api/costumes/"
Test-JsonEndpoint -Name "Bill Service API" -Url "$BASE_URL/api/bills/"
Test-JsonEndpoint -Name "Supplier Service API" -Url "$BASE_URL/api/suppliers/"
Test-JsonEndpoint -Name "Import Bill Service API" -Url "$BASE_URL/api/import-bills/"

Write-Info ""
Write-Info "🔍 Testing Health Endpoints:"
Write-Info "=" * 50

# Test health endpoints (direct service access via port-forward if needed)
$healthEndpoints = @(
    @{ name = "User Service Health"; url = "$BASE_URL/api/users/" },
    @{ name = "Costume Service Health"; url = "$BASE_URL/api/costumes/" }
)

foreach ($endpoint in $healthEndpoints) {
    Test-JsonEndpoint -Name $endpoint.name -Url $endpoint.url
}

Write-Info ""
Write-Success "🎉 Endpoint testing completed!"
Write-Info ""

Write-Warning "📝 Manual Testing Suggestions:"
Write-Info "1. Open browser and visit: $BASE_URL"
Write-Info "2. Test login functionality"
Write-Info "3. Navigate through different pages"
Write-Info "4. Test API endpoints with Postman or similar tools"
Write-Info ""

Write-Info "🔧 Troubleshooting Commands:"
Write-Info "- Check pods: kubectl get pods -n costume-rental"
Write-Info "- Check services: kubectl get svc -n costume-rental"
Write-Info "- Check ingress: kubectl get ingress -n costume-rental"
Write-Info "- Port forward: kubectl port-forward svc/client-costume-rental 8080:8080 -n costume-rental"
