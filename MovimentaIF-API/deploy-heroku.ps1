# Script de Deploy Automático para Heroku
# MovimentaIF-API

param(
    [string]$AppName = "movimentaif-api",
    [switch]$FirstDeploy = $false,
    [switch]$SetVars = $false
)

Write-Host "==================================" -ForegroundColor Cyan
Write-Host "  MovimentaIF - Deploy Heroku" -ForegroundColor Cyan
Write-Host "==================================" -ForegroundColor Cyan
Write-Host ""

# Verificar se está na pasta correta
if (-not (Test-Path "pom.xml")) {
    Write-Host "❌ Erro: Execute este script na pasta MovimentaIF-API" -ForegroundColor Red
    exit 1
}

# Verificar se Heroku CLI está instalado
try {
    $herokuVersion = heroku --version
    Write-Host "✅ Heroku CLI detectado: $herokuVersion" -ForegroundColor Green
}
catch {
    Write-Host "❌ Heroku CLI não está instalado!" -ForegroundColor Red
    Write-Host "Instale com: choco install heroku-cli" -ForegroundColor Yellow
    exit 1
}

# Verificar se Git está instalado
try {
    $gitVersion = git --version
    Write-Host "✅ Git detectado: $gitVersion" -ForegroundColor Green
}
catch {
    Write-Host "❌ Git não está instalado!" -ForegroundColor Red
    exit 1
}

Write-Host ""

# Primeiro Deploy - Criar app e configurar
if ($FirstDeploy) {
    Write-Host "🚀 Criando aplicação no Heroku..." -ForegroundColor Yellow
    
    try {
        heroku create $AppName
        Write-Host "✅ Aplicação '$AppName' criada com sucesso!" -ForegroundColor Green
    }
    catch {
        Write-Host "⚠️ Aplicação pode já existir. Continuando..." -ForegroundColor Yellow
    }
    
    Write-Host "🔗 Adicionando remote do Heroku..." -ForegroundColor Yellow
    heroku git:remote -a $AppName
    
    $SetVars = $true
}

# Configurar variáveis de ambiente
if ($SetVars) {
    Write-Host ""
    Write-Host "🔧 Configurando variáveis de ambiente..." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "⚠️  ATENÇÃO: Configure manualmente suas credenciais Firebase!" -ForegroundColor Red
    Write-Host "Execute: heroku config:set FIREBASE_CREDENTIALS='<seu-json-aqui>' --app $AppName" -ForegroundColor Yellow
    Write-Host "Obtenha em: Firebase Console > Project Settings > Service Accounts" -ForegroundColor Gray
    Write-Host ""
    
    # Usuário deve configurar manualmente por segurança
    # heroku config:set FIREBASE_CREDENTIALS='{"type":"service_account",...}' --app $AppName
    heroku config:set FIREBASE_DATABASE_URL=https://movimentaif-default-rtdb.firebaseio.com --app $AppName
    heroku config:set JWT_ISSUER_URI=https://securetoken.google.com/movimentaif --app $AppName
    
    Write-Host "✅ Variáveis configuradas!" -ForegroundColor Green
}

Write-Host ""
Write-Host "📦 Preparando código para deploy..." -ForegroundColor Yellow

# Verificar se há repositório Git
if (-not (Test-Path ".git")) {
    Write-Host "🔧 Inicializando repositório Git..." -ForegroundColor Yellow
    git init
    git add .
    git commit -m "Preparar para deploy no Heroku"
}
else {
    Write-Host "📝 Commitando mudanças..." -ForegroundColor Yellow
    git add .
    $commitMsg = Read-Host "Mensagem do commit (Enter para padrão)"
    if ([string]::IsNullOrWhiteSpace($commitMsg)) {
        $commitMsg = "Deploy para Heroku - $(Get-Date -Format 'yyyy-MM-dd HH:mm')"
    }
    git commit -m $commitMsg
}

Write-Host ""
Write-Host "🚀 Fazendo deploy para o Heroku..." -ForegroundColor Yellow
Write-Host ""

git push heroku main

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "⚠️ Tentando com branch master..." -ForegroundColor Yellow
    git push heroku master
}

Write-Host ""
Write-Host "==================================" -ForegroundColor Green
Write-Host "  ✅ Deploy Concluído!" -ForegroundColor Green
Write-Host "==================================" -ForegroundColor Green
Write-Host ""
Write-Host "🌐 URL da API: https://$AppName.herokuapp.com" -ForegroundColor Cyan
Write-Host ""
Write-Host "📋 Comandos úteis:" -ForegroundColor Yellow
Write-Host "  heroku logs --tail --app $AppName    # Ver logs em tempo real" -ForegroundColor Gray
Write-Host "  heroku open --app $AppName           # Abrir no navegador" -ForegroundColor Gray
Write-Host "  heroku restart --app $AppName        # Reiniciar app" -ForegroundColor Gray
Write-Host "  heroku config --app $AppName         # Ver variáveis de ambiente" -ForegroundColor Gray
Write-Host ""

# Perguntar se quer ver os logs
$verLogs = Read-Host "Deseja ver os logs agora? (s/n)"
if ($verLogs -eq "s" -or $verLogs -eq "S") {
    heroku logs --tail --app $AppName
}
