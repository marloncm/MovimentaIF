# 🚀 Deploy da MovimentaIF-API no Heroku

Este guia contém o passo a passo completo para fazer deploy da API no Heroku.

## 📋 Pré-requisitos

- Conta no Heroku (já possui ✅)
- Heroku CLI instalado
- Git instalado
- Java 17 e Maven configurados

## 🔧 Instalação do Heroku CLI

Se ainda não tiver instalado:

**Windows (PowerShell):**
```powershell
# Baixe o instalador
Invoke-WebRequest -Uri "https://cli-assets.heroku.com/install.sh" -OutFile "install.sh"

# Ou use o instalador direto:
# https://cli-assets.heroku.com/heroku-x64.exe
```

**Ou use Chocolatey:**
```powershell
choco install heroku-cli
```

## 📝 Passo a Passo para Deploy

### 1️⃣ Login no Heroku

```powershell
heroku login
```

### 2️⃣ Criar a Aplicação no Heroku

Entre na pasta da API e crie o app:

```powershell
cd MovimentaIF-API
heroku create movimentaif-api
```

> **Nota:** Se o nome `movimentaif-api` já estiver em uso, escolha outro nome único como `movimentaif-api-2024` ou `movimentaif-ifrs-api`.

### 3️⃣ Configurar as Variáveis de Ambiente

Configure todas as variáveis de ambiente necessárias:

```powershell
# Configurar credenciais do Firebase (substitua pelo seu JSON completo)
# Obtenha em: Firebase Console > Project Settings > Service Accounts > Generate New Private Key
heroku config:set FIREBASE_CREDENTIALS='{"type":"service_account","project_id":"SEU_PROJECT_ID",...}' --app movimentaif-api

# Configurar URL do Firebase Database
heroku config:set FIREBASE_DATABASE_URL=https://movimentaif-default-rtdb.firebaseio.com --app movimentaif-api

# Configurar JWT Issuer
heroku config:set JWT_ISSUER_URI=https://securetoken.google.com/movimentaif --app movimentaif-api
```

**⚠️ IMPORTANTE:** A variável `PORT` é configurada automaticamente pelo Heroku, não precisa definir!

### 4️⃣ Verificar as Variáveis Configuradas

```powershell
heroku config --app movimentaif-api
```

### 5️⃣ Inicializar Git (se ainda não tiver)

```powershell
git init
git add .
git commit -m "Preparar para deploy no Heroku"
```

### 6️⃣ Adicionar o Remote do Heroku

```powershell
heroku git:remote -a movimentaif-api
```

### 7️⃣ Fazer o Deploy

```powershell
git push heroku main
```

Se sua branch principal for `master` ao invés de `main`:
```powershell
git push heroku master
```

### 8️⃣ Verificar os Logs

```powershell
heroku logs --tail --app movimentaif-api
```

### 9️⃣ Abrir a Aplicação

```powershell
heroku open --app movimentaif-api
```

## 🔍 Testando a API

Após o deploy, sua API estará disponível em:
```
https://movimentaif-api.herokuapp.com
```

Teste os endpoints:
```powershell
# Testar se a API está rodando
curl https://movimentaif-api.herokuapp.com/actuator/health

# Testar endpoint de usuários (se público)
curl https://movimentaif-api.herokuapp.com/api/users
```

## 🛠️ Comandos Úteis

```powershell
# Ver aplicações
heroku apps

# Ver logs em tempo real
heroku logs --tail --app movimentaif-api

# Reiniciar a aplicação
heroku restart --app movimentaif-api

# Escalar dynos (aumentar recursos)
heroku ps:scale web=1 --app movimentaif-api

# Abrir console do Heroku
heroku run bash --app movimentaif-api

# Ver status da aplicação
heroku ps --app movimentaif-api

# Configurar buildpack (se necessário)
heroku buildpacks:set heroku/java --app movimentaif-api
```

## 🔐 Gerenciamento de Variáveis de Ambiente

### Ver todas as variáveis
```powershell
heroku config --app movimentaif-api
```

### Adicionar/Atualizar variável
```powershell
heroku config:set NOME_VARIAVEL=valor --app movimentaif-api
```

### Remover variável
```powershell
heroku config:unset NOME_VARIAVEL --app movimentaif-api
```

## 🌐 Configuração de CORS (se necessário)

Se precisar permitir requisições do frontend web/mobile, adicione configuração de CORS na sua aplicação Spring Boot.

Crie um arquivo `WebConfig.java`:

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // Em produção, especifique os domínios
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
```

## 📊 Monitoramento

Acesse o dashboard do Heroku para monitorar:
- https://dashboard.heroku.com/apps/movimentaif-api

## ⚡ Performance

Para melhorar a performance:
- Use plano pago para evitar sleep dos dynos
- Configure cache quando possível
- Otimize queries do Firestore

## 🆘 Troubleshooting

### Erro: "Application error"
```powershell
heroku logs --tail --app movimentaif-api
```

### Erro: "No web processes running"
```powershell
heroku ps:scale web=1 --app movimentaif-api
```

### Erro de memória
Aumente o heap size do Java editando o `Procfile`:
```
web: java -Xmx512m -Dserver.port=$PORT $JAVA_OPTS -jar target/MovimentaIF-API-0.0.1-SNAPSHOT.jar
```

## 🔄 Atualizações Futuras

Para atualizar a aplicação:

```powershell
# 1. Faça as alterações no código
# 2. Commit as mudanças
git add .
git commit -m "Descrição das mudanças"

# 3. Push para o Heroku
git push heroku main

# 4. Verificar deploy
heroku logs --tail --app movimentaif-api
```

## 📝 Notas Importantes

- ✅ As credenciais do Firebase estão nas variáveis de ambiente (seguras)
- ✅ A porta é configurada automaticamente pelo Heroku via `$PORT`
- ✅ O JWT Issuer está configurado para o Firebase
- ✅ O banco de dados é o Firebase Firestore (sem necessidade de adicionar DB no Heroku)
- ⚠️ O plano gratuito do Heroku coloca dynos para "dormir" após 30 minutos de inatividade
- ⚠️ O primeiro acesso após sleep pode demorar ~10-30 segundos

## 🎯 Próximos Passos

1. Configure um domínio personalizado (opcional)
2. Configure CI/CD com GitHub Actions (opcional)
3. Adicione monitoramento com New Relic ou Datadog (opcional)
4. Configure backups automáticos do Firestore

## 🔗 Links Úteis

- [Heroku Dashboard](https://dashboard.heroku.com)
- [Heroku CLI Docs](https://devcenter.heroku.com/articles/heroku-cli)
- [Spring Boot on Heroku](https://devcenter.heroku.com/articles/deploying-spring-boot-apps-to-heroku)
