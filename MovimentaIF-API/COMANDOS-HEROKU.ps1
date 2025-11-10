# Script Rápido - Comandos Heroku
# Use este arquivo como referência rápida

# ===================================
# PRIMEIRO DEPLOY (só uma vez)
# ===================================

# 1. Login no Heroku
heroku login

# 2. Execute o script automatizado
.\deploy-heroku.ps1 -AppName "movimentaif-api" -FirstDeploy -SetVars

# ===================================
# DEPLOYS FUTUROS (atualizações)
# ===================================

# Execute apenas:
.\deploy-heroku.ps1 -AppName "movimentaif-api"

# ===================================
# COMANDOS MANUAIS (se preferir)
# ===================================

# Login
heroku login

# Criar app (primeira vez)
heroku create movimentaif-api

# Configurar variáveis de ambiente (primeira vez)
heroku config:set FIREBASE_CREDENTIALS='{"type":"service_account",...}' --app movimentaif-api
heroku config:set FIREBASE_DATABASE_URL=https://movimentaif-default-rtdb.firebaseio.com --app movimentaif-api
heroku config:set JWT_ISSUER_URI=https://securetoken.google.com/movimentaif --app movimentaif-api

# Adicionar remote
heroku git:remote -a movimentaif-api

# Deploy
git add .
git commit -m "Deploy"
git push heroku main

# Ver logs
heroku logs --tail --app movimentaif-api

# Abrir app
heroku open --app movimentaif-api

# ===================================
# GESTÃO DA APLICAÇÃO
# ===================================

# Ver todas as apps
heroku apps

# Ver config vars
heroku config --app movimentaif-api

# Reiniciar
heroku restart --app movimentaif-api

# Ver status
heroku ps --app movimentaif-api

# Escalar dynos
heroku ps:scale web=1 --app movimentaif-api

# Console remoto
heroku run bash --app movimentaif-api

# ===================================
# TROUBLESHOOTING
# ===================================

# Ver erros
heroku logs --tail --app movimentaif-api

# Reiniciar se houver problema
heroku restart --app movimentaif-api

# Verificar buildpack
heroku buildpacks --app movimentaif-api

# Definir buildpack Java
heroku buildpacks:set heroku/java --app movimentaif-api

# ===================================
# TESTAR A API
# ===================================

# Teste básico (PowerShell)
Invoke-WebRequest -Uri "https://movimentaif-api.herokuapp.com/api/users" -Method GET

# Com curl (se tiver instalado)
curl https://movimentaif-api.herokuapp.com/api/users

# ===================================
# LINKS ÚTEIS
# ===================================

# Dashboard: https://dashboard.heroku.com/apps/movimentaif-api
# Logs: https://dashboard.heroku.com/apps/movimentaif-api/logs
# Settings: https://dashboard.heroku.com/apps/movimentaif-api/settings
