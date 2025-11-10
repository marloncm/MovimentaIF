# 🚀 Guia Rápido - Deploy Heroku

## ⚡ Método Mais Rápido (Recomendado)

### 1. Instale o Heroku CLI
```powershell
choco install heroku-cli
# OU baixe: https://cli-assets.heroku.com/heroku-x64.exe
```

### 2. Navegue até a pasta da API
```powershell
cd c:\Users\Marlon\Documents\TCC2\Sistema\MovimentaIF\MovimentaIF-API
```

### 3. Execute o script automatizado
```powershell
# Primeiro deploy (cria app + configura variáveis + faz deploy)
.\deploy-heroku.ps1 -AppName "movimentaif-api" -FirstDeploy -SetVars
```

**Pronto!** 🎉 Sua API estará em: `https://movimentaif-api.herokuapp.com`

---

## 📝 Deploys Futuros (Atualizações)

Sempre que fizer alterações no código:

```powershell
cd MovimentaIF-API
.\deploy-heroku.ps1 -AppName "movimentaif-api"
```

---

## 🧪 Testar a API

Após o deploy, teste:

```powershell
# PowerShell
Invoke-WebRequest -Uri "https://movimentaif-api.herokuapp.com/api/health"

# Navegador
# Acesse: https://movimentaif-api.herokuapp.com/api/health
```

---

## 📊 Monitorar Logs

```powershell
heroku logs --tail --app movimentaif-api
```

---

## 🔧 Comandos Úteis

```powershell
# Ver apps
heroku apps

# Ver variáveis de ambiente
heroku config --app movimentaif-api

# Reiniciar
heroku restart --app movimentaif-api

# Abrir dashboard
heroku open --app movimentaif-api
```

---

## 🌐 URLs Importantes

- **API Base:** `https://movimentaif-api.herokuapp.com`
- **Health Check:** `https://movimentaif-api.herokuapp.com/api/health`
- **Dashboard Heroku:** https://dashboard.heroku.com/apps/movimentaif-api

---

## ⚠️ Problemas Comuns

### App não inicia
```powershell
heroku logs --tail --app movimentaif-api
heroku restart --app movimentaif-api
```

### Variáveis não configuradas
```powershell
.\deploy-heroku.ps1 -AppName "movimentaif-api" -SetVars
```

### Erro de porta
✅ Já configurado! O Heroku define a porta automaticamente via `$PORT`

---

## 📖 Documentação Completa

Para mais detalhes, consulte: `README-DEPLOY.md`
