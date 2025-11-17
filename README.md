# MovimentaIF - Sistema de Gestão da Academia

<div align="center">

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-brightgreen.svg)
![Java](https://img.shields.io/badge/Java-17-orange.svg)
![Firebase](https://img.shields.io/badge/Firebase-Firestore-yellow.svg)
![Heroku](https://img.shields.io/badge/Heroku-Deployed-purple.svg)
![License](https://img.shields.io/badge/License-MIT-blue.svg)

**Sistema completo de gestão digital para a academia do IFRS Campus Porto Alegre**

[Sobre](#sobre) • [Tecnologias](#tecnologias) • [Arquitetura](#arquitetura) • [Instalação](#instalação) • [API](#api) • [Deploy](#deploy) • [Contribuir](#contribuir)

</div>

---

## 📋 Sobre o Projeto

O **MovimentaIF** é um sistema desenvolvido como Trabalho de Conclusão de Curso (TCC) para modernizar e otimizar a gestão da academia do IFRS Campus Porto Alegre. O sistema oferece uma solução completa e integrada composta por três módulos principais:

- **🌐 MovimentaIF-Admin** - Painel web administrativo para professores e gestores
- **🚀 MovimentaIF-API** - API REST para integração entre os sistemas
- **📱 MovimentaIFApp** - Aplicativo móvel Android para os alunos

### 🎯 Objetivos

- Digitalizar o gerenciamento de alunos e treinos
- Facilitar o acompanhamento da evolução dos alunos
- Permitir acesso móvel às informações de treino
- Otimizar o agendamento de horários na academia
- Centralizar dados em plataforma cloud segura

---

## 🛠️ Tecnologias

### Backend (MovimentaIF-API)

- **Framework**: Spring Boot 3.5.4
- **Linguagem**: Java 17
- **Banco de Dados**: Firebase Firestore (NoSQL)
- **Autenticação**: Firebase Authentication + JWT
- **Segurança**: Spring Security + OAuth2 Resource Server
- **Build**: Maven
- **Deploy**: Heroku

### Frontend Web (MovimentaIF-Admin)

- **Interface**: HTML5, CSS3, JavaScript (ES6+)
- **Framework CSS**: Bootstrap 5
- **Autenticação**: Firebase SDK
- **Hospedagem**: Cloudflare Pages / GitHub Pages

### Mobile (MovimentaIFApp)

- **Linguagem**: Kotlin
- **SDK Mínimo**: Android 7.0 (API 24)
- **SDK Alvo**: Android 14 (API 36)
- **Arquitetura**: MVVM + Navigation Components
- **Rede**: Retrofit + Coroutines
- **Autenticação**: Firebase Auth + Google Sign-In

---

## 🏗️ Arquitetura do Sistema

```mermaid
graph TB
    A[MovimentaIF Admin Web] -->|HTTP/REST| B[MovimentaIF-API]
    C[MovimentaIF App Android] -->|HTTP/REST| B
    B -->|Firebase SDK| D[Firebase Firestore]
    B -->|JWT Validation| E[Firebase Auth]
    A -->|Authentication| E
    C -->|Authentication| E
    
    style A fill:#e3f2fd
    style C fill:#fff3e0
    style B fill:#c8e6c9
    style D fill:#ffccbc
    style E fill:#f8bbd0
```

### Fluxo de Dados

1. **Autenticação**: Usuários fazem login via Firebase Authentication (email/senha ou Google)
2. **Token JWT**: Firebase gera token JWT após autenticação bem-sucedida
3. **Requisições**: Clientes enviam token no header `Authorization: Bearer <token>`
4. **Validação**: API valida token com Firebase e autoriza acesso
5. **Dados**: API realiza operações no Firestore e retorna resposta

---

## 📦 Instalação e Configuração

### Pré-requisitos

- Java 17 ou superior
- Maven 3.9+
- Conta Firebase (Firestore + Authentication)
- Heroku CLI (para deploy)
- Git

### 1. Clone o Repositório

```bash
git clone https://github.com/seu-usuario/MovimentaIF.git
cd MovimentaIF/MovimentaIF-API
```

### 2. Configure o Firebase

1. Crie um projeto no [Firebase Console](https://console.firebase.google.com)
2. Ative o Firestore Database
3. Ative o Firebase Authentication (Email/Password e Google)
4. Gere uma chave privada de conta de serviço:
   - Configurações do Projeto → Contas de Serviço
   - Gerar nova chave privada (JSON)

### 3. Configure as Variáveis de Ambiente

Crie um arquivo `.env` ou configure as variáveis:

```properties
FIREBASE_CREDENTIALS={"type":"service_account",...}  # JSON completo da chave privada
FIREBASE_DATABASE_URL=https://seu-projeto.firebaseio.com
JWT_ISSUER_URI=https://securetoken.google.com/seu-projeto-id
PORT=8080
```

### 4. Compile e Execute

```bash
# Compilar
mvn clean install

# Executar localmente
mvn spring-boot:run

# Ou execute o JAR
java -jar target/MovimentaIF-API-0.0.1-SNAPSHOT.jar
```

A API estará disponível em `http://localhost:8080`

---

## 🌐 API Endpoints

### Autenticação

Todos os endpoints (exceto criação de usuário) requerem autenticação via JWT token.

**Header obrigatório:**
```http
Authorization: Bearer <seu-token-firebase-jwt>
```

### 📍 Recursos Disponíveis

#### 👤 Usuários (`/api/users`)

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| `POST` | `/api/users` | Criar novo usuário | ❌ Público |
| `GET` | `/api/users/{userId}` | Buscar usuário por ID | ✅ Requerida |
| `GET` | `/api/users` | Listar todos usuários | ✅ Requerida |
| `GET` | `/api/users/app-users` | Listar usuários do app | ✅ Requerida |

#### 💪 Exercícios (`/api/workouts`)

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| `POST` | `/api/workouts` | Criar exercício | ✅ Requerida |
| `GET` | `/api/workouts/{workoutId}` | Buscar exercício | ✅ Requerida |
| `GET` | `/api/workouts` | Listar exercícios | ✅ Requerida |

#### 📋 Fichas de Treino (`/api/workout-charts`)

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| `POST` | `/api/workout-charts` | Criar ficha | ✅ Requerida |
| `GET` | `/api/workout-charts/{chartId}` | Buscar ficha | ✅ Requerida |
| `GET` | `/api/workout-charts/user/{userId}` | Fichas do usuário | ✅ Requerida |
| `PUT` | `/api/workout-charts/{chartId}` | Atualizar ficha | ✅ Requerida |

#### 📊 Histórico (`/api/workout-history`)

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| `POST` | `/api/workout-history` | Criar histórico | ✅ Requerida |
| `GET` | `/api/workout-history/user/{userId}` | Histórico do usuário | ✅ Requerida |

#### 🔗 Exercícios do Usuário (`/api/user-workouts`)

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| `POST` | `/api/user-workouts` | Criar configuração | ✅ Requerida |
| `GET` | `/api/user-workouts/{userWorkoutId}` | Buscar configuração | ✅ Requerida |
| `GET` | `/api/user-workouts/user/{userId}` | Configurações do usuário | ✅ Requerida |

### 📝 Exemplo de Requisição

**Criar Usuário (Público)**

```bash
curl -X POST https://movimentaif-api-7895a5f0638f.herokuapp.com/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "userName": "João Silva",
    "email": "joao@example.com",
    "role": "USER"
  }'
```

**Buscar Usuário (Autenticado)**

```bash
curl -X GET https://movimentaif-api-7895a5f0638f.herokuapp.com/api/users/123 \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6..."
```

---

## 🚀 Deploy no Heroku

### Método Automático (Recomendado)

```powershell
# 1. Navegue até a pasta da API
cd MovimentaIF-API

# 2. Execute o script de deploy
.\deploy-heroku.ps1 -AppName "movimentaif-api" -FirstDeploy -SetVars
```

### Método Manual

```bash
# 1. Login no Heroku
heroku login

# 2. Criar aplicação
heroku create movimentaif-api

# 3. Configurar variáveis de ambiente
heroku config:set FIREBASE_CREDENTIALS='{"type":"service_account",...}'
heroku config:set FIREBASE_DATABASE_URL='https://...'
heroku config:set JWT_ISSUER_URI='https://securetoken.google.com/...'

# 4. Deploy (da raiz do projeto)
cd ..
git subtree push --prefix MovimentaIF-API heroku main
```

📖 **Documentação completa**: [README-DEPLOY.md](README-DEPLOY.md)

---

## 📊 Modelo de Dados

### Entidades Principais

```mermaid
erDiagram
    USER ||--o{ USER_WORKOUT : possui
    USER ||--o| WORKOUT_CHART : "tem atual"
    WORKOUT ||--o{ USER_WORKOUT : "referenciado por"
    WORKOUT_CHART ||--o{ WORKOUT : contém
    
    USER {
        string userId PK
        string userName
        string email
        string role
        boolean isActive
        string affiliationType
        string workoutChartId FK
    }
    
    WORKOUT {
        string workoutId PK
        string workoutName
        string workoutDescription
        string workoutVideoLink
    }
    
    WORKOUT_CHART {
        string chartId PK
        string userId FK
        list mondayWorkouts
        list tuesdayWorkouts
        date startDate
        date endDate
    }
```

📖 **Modelo completo**: [MODELO-ER.md](../MODELO-ER.md)

---

## 🔐 Segurança

### Autenticação e Autorização

- ✅ Autenticação via Firebase JWT
- ✅ Tokens validados em cada requisição
- ✅ CORS configurado para domínios específicos
- ✅ Credenciais armazenadas como variáveis de ambiente
- ✅ HTTPS obrigatório em produção

### Boas Práticas

- Chaves privadas **nunca** commitadas no Git
- Tokens com validade limitada
- Refresh tokens implementados no cliente
- Rate limiting (via Heroku)

---

## 📱 Integração com Clientes

### Web Admin

Configure a URL base da API:

```javascript
// firebaseConfig.js
const API_BASE_URL = 'https://movimentaif-api-7895a5f0638f.herokuapp.com/api';
```

### App Android

Configure o Retrofit:

```kotlin
// RetrofitClient.kt
const val BASE_URL = "https://movimentaif-api-7895a5f0638f.herokuapp.com/"

object RetrofitClient {
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val token = FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.result?.token
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(request)
        }
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
```

---

## 🧪 Testes

```bash
# Executar testes unitários
mvn test

# Executar com cobertura
mvn test jacoco:report

# Testar endpoint localmente
curl http://localhost:8080/api/users
```

---

## 📈 Monitoramento

### Logs do Heroku

```bash
# Ver logs em tempo real
heroku logs --tail --app movimentaif-api

# Ver últimas 100 linhas
heroku logs -n 100 --app movimentaif-api
```

### Métricas

Acesse o dashboard do Heroku para monitorar:
- Uso de CPU e memória
- Tempo de resposta
- Taxa de erro
- Número de requisições

🔗 [Dashboard Heroku](https://dashboard.heroku.com/apps/movimentaif-api)

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Por favor, siga os passos:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/NovaFuncionalidade`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/NovaFuncionalidade`)
5. Abra um Pull Request

### Padrões de Código

- Use Java Code Conventions
- Documente métodos públicos com Javadoc
- Escreva testes para novas funcionalidades
- Mantenha a cobertura de testes acima de 80%

---

## 📄 Licença

Este projeto foi desenvolvido como Trabalho de Conclusão de Curso do IFRS Campus Porto Alegre.

---

## 👥 Autores

**Instituição**: IFRS - Campus Porto Alegre  
**Tipo**: Trabalho de Conclusão de Curso (TCC)  
**Ano**: 2024/2025

---

## 📞 Suporte

- **Issues**: [GitHub Issues](https://github.com/seu-usuario/MovimentaIF/issues)
- **Documentação**: [Wiki do Projeto](https://github.com/seu-usuario/MovimentaIF/wiki)

---

## 🔗 Links Úteis

- [Firebase Console](https://console.firebase.google.com)
- [Heroku Dashboard](https://dashboard.heroku.com)
- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [Firebase Admin SDK](https://firebase.google.com/docs/admin/setup)

---

<div align="center">

**Desenvolvido com ❤️ no IFRS Campus Porto Alegre**

[![IFRS](https://img.shields.io/badge/IFRS-Porto%20Alegre-green)](https://ifrs.edu.br/portoalegre/)

</div>
