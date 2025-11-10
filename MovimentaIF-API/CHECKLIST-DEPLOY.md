# ✅ Checklist - Deploy Heroku

Use este checklist para garantir que tudo está pronto para o deploy.

## 📋 Pré-Deploy

- [ ] Heroku CLI instalado (`heroku --version`)
- [ ] Git instalado (`git --version`)
- [ ] Java 17 instalado (`java --version`)
- [ ] Maven instalado (`mvn --version`)
- [ ] Login no Heroku realizado (`heroku login`)

## 🔧 Arquivos Necessários

- [x] `Procfile` criado
- [x] `system.properties` criado
- [x] `application.properties` configurado com variáveis de ambiente
- [x] `.gitignore` atualizado
- [x] `FirebaseConfig.java` configurado para ler variáveis de ambiente
- [x] `SecurityConfig.java` com CORS configurado
- [x] `HealthController.java` para health check

## 🔐 Variáveis de Ambiente

As seguintes variáveis devem ser configuradas no Heroku:

- [ ] `FIREBASE_CREDENTIALS` - JSON completo das credenciais Firebase
- [ ] `FIREBASE_DATABASE_URL` - URL do Firestore
- [ ] `JWT_ISSUER_URI` - URI do emissor JWT
- [ ] `PORT` - ⚠️ NÃO CONFIGURAR (Heroku configura automaticamente)

## 🚀 Deploy

- [ ] Código commitado no Git
- [ ] Remote do Heroku adicionado (`heroku git:remote -a movimentaif-api`)
- [ ] Push para Heroku realizado (`git push heroku main`)
- [ ] Build bem-sucedido (sem erros)
- [ ] Aplicação iniciada (dyno web rodando)

## 🧪 Testes Pós-Deploy

- [ ] Health check respondendo: `https://movimentaif-api.herokuapp.com/api/health`
- [ ] Root respondendo: `https://movimentaif-api.herokuapp.com/api/`
- [ ] Logs sem erros críticos (`heroku logs --tail`)
- [ ] Endpoints protegidos requerem autenticação
- [ ] CORS funcionando (testar do frontend)

## 📊 Monitoramento

- [ ] Dashboard do Heroku acessível
- [ ] Métricas de uso visíveis
- [ ] Logs configurados
- [ ] Alertas configurados (opcional)

## 🌐 Integração Frontend

Atualize o frontend para usar a URL de produção:

### MovimentaIF-Admin (Web)
Arquivo: `assets/js/firebaseConfig.js` ou similar

```javascript
const API_BASE_URL = 'https://movimentaif-api.herokuapp.com/api';
```

### MovimentaIFApp (Android)
Arquivo: `app/src/main/java/.../network/ApiService.kt` ou `RetrofitClient.kt`

```kotlin
const val BASE_URL = "https://movimentaif-api.herokuapp.com/"
```

## 🔄 Atualizações Futuras

Sempre que atualizar o código:

1. [ ] Fazer alterações no código
2. [ ] Testar localmente
3. [ ] Commit das mudanças
4. [ ] Push para Heroku
5. [ ] Verificar logs
6. [ ] Testar endpoints

## 📝 Comandos Úteis de Verificação

```powershell
# Verificar status
heroku ps --app movimentaif-api

# Verificar variáveis
heroku config --app movimentaif-api

# Ver logs
heroku logs --tail --app movimentaif-api

# Testar health
Invoke-WebRequest -Uri "https://movimentaif-api.herokuapp.com/api/health"
```

## ⚠️ Troubleshooting

Se algo der errado:

1. [ ] Verificar logs: `heroku logs --tail`
2. [ ] Verificar variáveis de ambiente: `heroku config`
3. [ ] Verificar build: Logs durante o push
4. [ ] Reiniciar: `heroku restart`
5. [ ] Verificar Procfile está correto
6. [ ] Verificar system.properties tem Java 17

## 🎯 Objetivos Alcançados

- [x] API hospedada no Heroku
- [x] Variáveis de ambiente configuradas e seguras
- [x] Credenciais Firebase não expostas no código
- [x] CORS configurado para permitir acesso externo
- [x] Health check endpoint disponível
- [x] Logs monitoráveis
- [x] Deploy automatizado com script PowerShell

## 📞 Suporte

Se precisar de ajuda:

- Documentação Heroku: https://devcenter.heroku.com
- Logs: `heroku logs --tail`
- Status Heroku: https://status.heroku.com
- Community: https://help.heroku.com

---

**Data do Deploy:** _________________  
**URL da API:** https://movimentaif-api.herokuapp.com  
**Responsável:** _________________
