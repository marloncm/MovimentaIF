# 📱 Como Instalar o MovimentaIF no Celular

## 🎯 Guia Completo para Instalação

Este guia vai te ensinar a instalar o aplicativo MovimentaIF no seu celular Android de forma simples e rápida.

---

## 📋 Requisitos

- **Android 7.0 ou superior** (API 24+)
- **Conexão com a internet** (para autenticação e uso do app)
- **Cerca de 50 MB de espaço livre**

---

## 🚀 Método 1: Instalar APK Pronto (RECOMENDADO)

### Passo 1: Habilitar "Fontes Desconhecidas"

1. Abra as **Configurações** do seu celular
2. Procure por **"Segurança"** ou **"Privacidade"**
3. Ative a opção **"Instalar apps de fontes desconhecidas"** ou **"Permitir desta fonte"**
   - No Android 8+ você pode permitir apenas para o app que vai instalar (Chrome, WhatsApp, etc.)

### Passo 2: Baixar o APK

**Opção A: Por Link Direto**
1. O desenvolvedor vai te enviar o arquivo APK por:
   - WhatsApp
   - Google Drive
   - Email
   - Link de download

**Opção B: Por Transferência Direta**
1. Conecte o celular no computador via USB
2. Copie o arquivo `app-debug.apk` ou `app-release.apk` para a pasta **Downloads** do celular

### Passo 3: Instalar o APK

1. Abra o **Gerenciador de Arquivos** do seu celular
2. Vá até a pasta **Downloads**
3. Toque no arquivo APK baixado (nome: `MovimentaIF.apk` ou similar)
4. Toque em **"Instalar"**
5. Aguarde a instalação (5-10 segundos)
6. Toque em **"Abrir"**

### Passo 4: Pronto! 🎉

O app já está instalado e pronto para usar!

---

## 🔧 Método 2: Compilar do Código-Fonte

### Requisitos
- Android Studio instalado
- JDK 11 ou superior
- Git

### Passos para o Desenvolvedor

1. **Clonar o repositório:**
```bash
git clone https://github.com/marloncm/MovimentaIF.git
cd MovimentaIF/MovimentaIFApp
```

2. **Abrir no Android Studio:**
   - File → Open
   - Selecionar a pasta `MovimentaIFApp`
   - Aguardar o Gradle sincronizar

3. **Gerar APK Debug (para testes):**
```bash
# Via terminal
./gradlew assembleDebug

# Ou no Android Studio:
# Build → Build Bundle(s) / APK(s) → Build APK(s)
```

4. **Localizar o APK:**
   - Caminho: `app/build/outputs/apk/debug/app-debug.apk`

5. **Transferir para o celular:**
   - Via USB, WhatsApp, Drive, etc.

---

## 🔐 Método 3: Gerar APK Release Assinado (Para Produção)

### Para o Desenvolvedor

1. **Gerar Keystore (primeira vez):**
```bash
keytool -genkey -v -keystore movimentaif.keystore -alias movimentaif -keyalg RSA -keysize 2048 -validity 10000
```

2. **Configurar assinatura no `build.gradle.kts`:**
```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("../movimentaif.keystore")
            storePassword = "SUA_SENHA"
            keyAlias = "movimentaif"
            keyPassword = "SUA_SENHA"
        }
    }
    
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            // ...
        }
    }
}
```

3. **Compilar versão release:**
```bash
./gradlew assembleRelease
```

4. **APK estará em:**
   - `app/build/outputs/apk/release/app-release.apk`

---

## 📲 Testando no Celular via Android Studio

### Pré-requisitos
- Celular com **Modo Desenvolvedor** ativado
- **Depuração USB** habilitada

### Como Habilitar Modo Desenvolvedor

1. **Configurações** → **Sobre o telefone**
2. Toque **7 vezes** em **"Número da versão"** ou **"Versão do Android"**
3. Volte para **Configurações**
4. Procure **"Opções do desenvolvedor"** ou **"Developer options"**
5. Ative **"Depuração USB"**

### Executar via USB

1. Conecte o celular no computador via USB
2. No celular, aceite a permissão de depuração USB
3. No Android Studio, clique em **"Run"** (▶️)
4. Selecione seu dispositivo na lista
5. O app será instalado e aberto automaticamente

---

## ❓ Problemas Comuns e Soluções

### ⚠️ "App não instalado" ou "Erro ao analisar o pacote"

**Causa:** APK corrompido ou incompatível

**Solução:**
1. Baixe o APK novamente
2. Verifique se seu Android é 7.0 ou superior
3. Limpe o cache: Configurações → Apps → Instalar apps → Limpar cache

### ⚠️ "Fontes desconhecidas bloqueadas"

**Solução:**
1. Vá em Configurações → Segurança
2. Ative "Fontes desconhecidas" ou "Permitir instalação de apps desconhecidos"

### ⚠️ "O app continua fechando"

**Solução:**
1. Verifique sua conexão com a internet
2. Limpe o cache do app: Configurações → Apps → MovimentaIF → Limpar cache
3. Desinstale e reinstale o app

### ⚠️ "Não consigo fazer login"

**Solução:**
1. Verifique sua conexão com a internet
2. Certifique-se de estar usando email e senha corretos
3. Se for o primeiro uso, registre-se primeiro
4. Verifique se a API está online: https://movimentaif-api-7895a5f0638f.herokuapp.com/api/health

---

## 📧 Cadastro e Login

### Primeira Vez

1. Abra o app
2. Clique em **"Registrar"**
3. Preencha:
   - Nome completo
   - Email válido
   - Senha (mínimo 6 caracteres)
4. Clique em **"Confirmar Registro"**
5. Aguarde confirmação
6. Faça login com email e senha

### Login com Google

1. Clique no botão **"Entrar com Google"**
2. Selecione sua conta Google
3. Permita o acesso
4. Pronto!

---

## 🔄 Atualizações

### Como atualizar o app

1. Baixe a nova versão do APK
2. Instale por cima da versão antiga
3. Seus dados serão preservados

**Observação:** Não desinstale antes de atualizar para não perder dados locais!

---

## 🗑️ Desinstalação

1. Configurações → Apps
2. Procure **"MovimentaIF"**
3. Toque em **"Desinstalar"**
4. Confirme

---

## 📊 Informações Técnicas

| Item | Valor |
|------|-------|
| **Nome do App** | MovimentaIF |
| **Tamanho** | ~10-15 MB (debug) / ~6-8 MB (release) |
| **Min SDK** | 24 (Android 7.0) |
| **Target SDK** | 36 (Android 14) |
| **Permissões** | Internet |
| **Backend** | https://movimentaif-api-7895a5f0638f.herokuapp.com |
| **Autenticação** | Firebase Auth |

---

## 🆘 Suporte

### Precisa de Ajuda?

**Desenvolvedor:** Marlon C. Mariano  
**Email:** marlon.cmariano93@gmail.com  
**GitHub:** https://github.com/marloncm/MovimentaIF  

### Reportar Problemas

Se encontrar algum erro:
1. Anote o que aconteceu
2. Tire um print da tela
3. Envie para o desenvolvedor com detalhes

---

## 🎓 Tutorial em Vídeo

### Para criar um tutorial:

1. Grave a tela do celular mostrando:
   - Download do APK
   - Instalação
   - Primeiro uso
   - Login
   - Funcionalidades principais

2. Upload no YouTube como "não listado"

3. Compartilhe o link com os usuários

---

## ✅ Checklist de Instalação

Marque conforme for fazendo:

- [ ] Verificar versão do Android (7.0+)
- [ ] Habilitar fontes desconhecidas
- [ ] Baixar o APK
- [ ] Instalar o APK
- [ ] Abrir o app
- [ ] Fazer cadastro/login
- [ ] Testar funcionalidades
- [ ] Tudo funcionando!

---

## 🚀 Pronto para Usar!

Depois de instalado, você poderá:

✅ Fazer login com email/senha ou Google  
✅ Ver seus treinos  
✅ Acompanhar seu progresso  
✅ Agendar horários  
✅ Visualizar histórico  

**Aproveite o MovimentaIF! 💪**

---

## 📝 Notas Importantes

⚠️ **Segurança:**
- O app só funciona com HTTPS
- Seus dados são protegidos por Firebase
- Tokens de autenticação são renovados automaticamente

⚠️ **Conectividade:**
- É necessário internet para usar o app
- Dados são sincronizados com a nuvem

⚠️ **Atualizações:**
- Fique atento a novas versões
- Atualizações trazem melhorias e correções

---

**Desenvolvido com ❤️ para o IFRS Campus Porto Alegre**  
**Versão:** 1.0.0  
**Data:** Novembro 2025
