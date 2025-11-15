# Changelog - MovimentaIF App

## [1.0.0] - 2025-11-15

### 🔒 Segurança
- Migração completa de HTTP para HTTPS
- URL de produção atualizada: `https://movimentaif-api-7895a5f0638f.herokuapp.com/`
- Implementado Network Security Config bloqueando tráfego cleartext (HTTP)
- Adicionado interceptor JWT automático com Firebase Authentication
- Logs sensíveis removidos em builds de produção
- ProGuard/R8 habilitado para ofuscação de código
- `allowBackup` desabilitado para prevenir vazamento de dados

### ⚙️ Configurações
- Timeouts de rede configurados (30s connect/read/write)
- OkHttp logging apenas em modo DEBUG
- Minificação de código habilitada (release builds)
- Shrink resources habilitado (redução de APK)

### 📦 Dependências Adicionadas
- `okhttp:4.12.0` - Cliente HTTP seguro
- `logging-interceptor:4.12.0` - Logging HTTP controlado

### 📝 Arquivos Modificados
- `RetrofitInstance.kt` - URL de produção + interceptor JWT + OkHttp client
- `AndroidManifest.xml` - Segurança hardening (allowBackup, usesCleartextTraffic)
- `build.gradle.kts` - Minificação habilitada + novas dependências
- `proguard-rules.pro` - Regras para remover logs e ofuscar código
- `libs.versions.toml` - Versões do OkHttp

### ➕ Arquivos Criados
- `network_security_config.xml` - Configuração de segurança de rede
- `SECURITY.md` - Documentação completa de segurança

### 🐛 Correções
- Removido IP localhost inseguro (10.0.2.2:8080)
- Tokens JWT agora são adicionados automaticamente nas requisições
- Certificados SSL validados corretamente

### 📊 Melhorias de Performance
- Redução de tamanho do APK com R8
- Timeouts otimizados para melhor UX
- Interceptor de autenticação eficiente

---

## Notas de Migração

### Para Desenvolvedores
1. Fazer clean build: `./gradlew clean`
2. Sincronizar Gradle
3. Build de debug: `./gradlew assembleDebug`
4. Build de release: `./gradlew assembleRelease`

### Para Testes
- **Debug:** Logs habilitados, código não ofuscado
- **Release:** Sem logs, código ofuscado, APK otimizado

### Compatibilidade
- Min SDK: 24 (Android 7.0)
- Target SDK: 36 (Android 14)
- Java: 11

---

**Desenvolvedor:** Copilot AI  
**Data:** 15/11/2025  
**API Backend:** MovimentaIF-API v1.0.0
