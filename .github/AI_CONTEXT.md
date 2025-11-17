# Contexto da IA - MovimentaIF

Este arquivo contém informações essenciais sobre o projeto MovimentaIF para assistir IAs em futuras interações.

## 📋 Visão Geral do Projeto

**Nome**: MovimentaIF  
**Tipo**: Sistema de Gestão para Academia  
**Contexto**: Trabalho de Conclusão de Curso (TCC) - IFRS Campus Porto Alegre  
**Período**: 2024/2025  

### Componentes do Sistema

1. **MovimentaIF-API** - Backend Spring Boot (Java 17)
2. **MovimentaIF-Admin** - Frontend Web (HTML/CSS/JS)
3. **MovimentaIFApp** - Aplicativo Android (Kotlin)

## 🏗️ Arquitetura Técnica

### Stack Tecnológico

**Backend (API)**
- Framework: Spring Boot 3.5.4
- Linguagem: Java 17
- Build: Maven
- Database: Firebase Firestore (NoSQL)
- Auth: Firebase Authentication + JWT
- Deploy: Heroku
- URL Produção: `https://movimentaif-api-7895a5f0638f.herokuapp.com`

**Frontend Web (Admin)**
- HTML5, CSS3, JavaScript ES6+
- Firebase SDK para autenticação
- Bootstrap 5 para UI

**Mobile (App)**
- Linguagem: Kotlin
- Min SDK: 24 (Android 7.0)
- Target SDK: 36 (Android 14)
- Build: Gradle 8.13
- Arquitetura: MVVM + Navigation Components
- HTTP: Retrofit 2.x + Coroutines
- Auth: Firebase Auth + Google Sign-In
- UI: Material Design 3

## 📊 Estrutura de Dados

### Coleções Firebase Firestore

1. **users** - Dados dos usuários (alunos, professores, admin)
2. **workouts** - Catálogo de exercícios
3. **workoutCharts** - Fichas de treino semanais
4. **userWorkouts** - Configurações personalizadas de exercícios (séries, repetições, peso)
5. **workoutHistory** - Histórico de fichas antigas
6. **parq** - Questionário PAR-Q (pré-atividade física)
7. **anamnese** - Questionário de anamnese de saúde
8. **dailyWorkoutCompletions** - Registro de conclusão diária de treinos

### Principais Entidades

**User**
```java
- userId: String (UID do Firebase)
- userName: String
- email: String
- phoneNumber: String
- role: String (USER, INSTRUCTOR, ADMIN)
- affiliationType: String (estudante, servidor, comunidade)
- isActive: Boolean
- workoutChartId: String (referência para ficha atual)
- parqId: String
- anamneseId: String
```

**Workout**
```java
- workoutId: String
- workoutName: String
- workoutDescription: String
- workoutVideoLink: String (URL do YouTube)
```

**WorkoutChart**
```java
- chartId: String
- userId: String
- mondayWorkouts: List<String> (IDs de UserWorkout)
- tuesdayWorkouts: List<String>
- wednesdayWorkouts: List<String>
- thursdayWorkouts: List<String>
- fridayWorkouts: List<String>
- startDate: Date
- endDate: Date
```

**UserWorkout**
```java
- userWorkoutId: String
- userId: String
- workoutId: String (referência para Workout)
- series: Integer
- repetitions: Integer
- weight: Double
- obs: String
```

**DailyWorkoutCompletion**
```java
- completionId: String
- userId: String
- dayOfWeek: String (monday, tuesday, wednesday, thursday, friday)
- completedDate: Date
- workoutChartId: String
```

**ParQ / Anamnese**
```java
- parqId/anamneseId: String
- userId: String
- respostas: Map<String, Boolean> (7 perguntas)
- observacoes: String
```

## 🔌 Endpoints da API

### Base URL
- **Produção**: `https://movimentaif-api-7895a5f0638f.herokuapp.com/api`
- **Local**: `http://localhost:8080/api`

### Autenticação
- Todos os endpoints (exceto POST /users) requerem JWT token
- Header: `Authorization: Bearer <firebase-jwt-token>`

### Principais Rotas

**Users**
- `POST /users` - Criar usuário (público)
- `GET /users/{uid}` - Buscar usuário por ID
- `PUT /users/{uid}` - Atualizar usuário
- `GET /users` - Listar todos
- `GET /users/app-users` - Listar apenas usuários do app

**Workouts**
- `POST /workouts` - Criar exercício
- `GET /workouts/{workoutId}` - Buscar exercício
- `GET /workouts` - Listar todos

**Workout Charts**
- `POST /charts` - Criar ficha
- `GET /charts/{chartId}` - Buscar ficha
- `GET /charts/user/{userId}` - Ficha do usuário
- `PUT /charts/{chartId}` - Atualizar ficha

**User Workouts**
- `POST /user-workouts` - Criar configuração
- `GET /user-workouts/{userWorkoutId}` - Buscar configuração
- `GET /user-workouts/user/{userId}` - Configurações do usuário

**PAR-Q / Anamnese**
- `POST /parq` - Criar PAR-Q
- `GET /parq/user/{userId}` - Buscar PAR-Q do usuário
- `PUT /parq/{parqId}` - Atualizar PAR-Q
- `POST /anamnese` - Criar Anamnese
- `GET /anamnese/user/{userId}` - Buscar Anamnese do usuário
- `PUT /anamnese/{anamneseId}` - Atualizar Anamnese

**Workout Completions**
- `POST /workout-completions` - Registrar conclusão de treino
- `GET /workout-completions/user/{userId}` - Histórico de conclusões
- `GET /workout-completions/user/{userId}/day/{dayOfWeek}/today` - Verificar se completou hoje
- `GET /workout-completions/user/{userId}/total` - Total de treinos completados
- `GET /workout-completions/user/{userId}/active-days` - Dias ativos (únicos)

## 🎨 Funcionalidades Implementadas

### App Mobile (Android)

1. **Autenticação**
   - Login com email/senha
   - Login com Google
   - Biometria (fingerprint/face) - apenas se credenciais salvas
   - Logout e recuperação de senha

2. **Perfil do Usuário**
   - Visualizar dados pessoais
   - Editar perfil (nome, telefone, tipo de vínculo)
   - Status de PAR-Q e Anamnese

3. **Questionários de Saúde**
   - PAR-Q (7 perguntas sobre aptidão física)
   - Anamnese (7 perguntas sobre histórico de saúde)
   - Carregamento automático de dados existentes
   - Criação/edição via API

4. **Ficha de Treino**
   - Visualização organizada por dia da semana (Seg-Sex)
   - Cards com exercícios, séries, repetições, peso e observações
   - Botões para assistir vídeos de demonstração
   - Compartilhamento de exercícios via redes sociais
   - **Botão "Finalizar Treino do Dia"** em cada card de dia
   - Verificação automática de treinos já concluídos no dia

5. **Lista de Exercícios**
   - Catálogo completo de exercícios disponíveis
   - Descrições e links para vídeos

6. **Estatísticas**
   - Total de treinos completados
   - Dias ativos (dias únicos de treino)
   - Exibido na tela Home

7. **Som de Interface**
   - SoundManager com arquivo `som.mp3`
   - Volume 1.0f, OnLoadCompleteListener para eliminar delay
   - Som em todos os botões do app

8. **Sobre**
   - Informações sobre o app
   - Créditos de desenvolvimento
   - Link para Instagram @maismovimento_ifrs

### Painel Admin Web

1. **Dashboard**
   - Visão geral de alunos ativos
   - Estatísticas gerais

2. **Gerenciamento de Usuários**
   - Lista de alunos
   - Detalhes do usuário
   - Edição de informações
   - Histórico de fichas

3. **Gerenciamento de Treinos**
   - Criar/editar exercícios
   - Criar/editar fichas de treino
   - Associar exercícios aos dias da semana

4. **Agenda**
   - Visualização de horários
   - Agendamento de treinos

## 🔧 Configurações Importantes

### Firebase Configuration

**Arquivo**: `google-services.json` (Android) / `firebaseConfig.js` (Web)

Configurações necessárias:
- Project ID
- API Key
- Auth Domain
- Storage Bucket
- Database URL

### Variáveis de Ambiente (Heroku)

```
FIREBASE_CREDENTIALS=<JSON da service account>
FIREBASE_DATABASE_URL=https://seu-projeto.firebaseio.com
JWT_ISSUER_URI=https://securetoken.google.com/seu-projeto-id
PORT=8080
```

### Android Build Configuration

- **Gradle Version**: 8.13
- **Kotlin Version**: Definido em `libs.versions.toml`
- **Compile SDK**: 36
- **Min SDK**: 24
- **Target SDK**: 36

**Dependências principais**:
- Firebase BOM
- Retrofit 2.x
- Navigation Component
- Material Design 3
- Coroutines
- Lifecycle Components

### Arquivos de Som

- **Localização**: `app/src/main/res/raw/som.mp3`
- **Uso**: SoundManager para feedback de cliques

## 🚀 Deploy e Build

### Deploy API (Heroku)

```bash
# Da raiz do projeto
git subtree push --prefix MovimentaIF-API heroku main

# Ou force push se necessário
git push heroku `git subtree split --prefix MovimentaIF-API main`:main --force
```

### Build Android

```bash
cd MovimentaIFApp
.\gradlew assembleDebug --no-daemon
```

**Output**: `app/build/outputs/apk/debug/app-debug.apk`

### Deploy Web Admin

- Hospedagem: GitHub Pages ou Cloudflare Pages
- Build: Arquivos estáticos (HTML/CSS/JS)

## 🐛 Problemas Conhecidos e Soluções

### 1. Erro "Carregando dados" em PAR-Q/Anamnese vazio
**Causa**: API retorna 404 quando não há dados  
**Solução**: Implementado catch silencioso que não exibe erro para novos questionários

### 2. Menu hamburguer não funcionando
**Causa**: Conflito com navegação padrão do Android  
**Solução**: Custom NavigationItemSelectedListener em HomeActivity

### 3. Delay no som de clique
**Causa**: Som não carregado antes do play  
**Solução**: OnLoadCompleteListener + flag isLoaded no SoundManager

### 4. Biometria aparecendo sem credenciais
**Causa**: Validação insuficiente  
**Solução**: Verificar getUserEmail() e getUserId() antes de exibir botão

### 5. Botão "Inicio" não funcionando no drawer
**Causa**: Override incorreto do onOptionsItemSelected  
**Solução**: Navegação explícita para R.id.nav_home no listener customizado

## 📁 Estrutura de Pastas

```
MovimentaIF/
├── MovimentaIF-API/
│   ├── src/main/java/com/ifrs/movimentaif/movimentaifapi/
│   │   ├── controller/     # REST Controllers
│   │   ├── service/        # Business Logic
│   │   ├── model/          # Data Models
│   │   ├── config/         # Security & Firebase Config
│   │   └── MovimentaIfApiApplication.java
│   ├── pom.xml
│   ├── Procfile           # Heroku
│   └── system.properties  # Java version
│
├── MovimentaIF-Admin/
│   ├── assets/
│   │   ├── css/
│   │   ├── js/
│   │   └── images/
│   ├── index.html
│   ├── users.html
│   ├── workouts.html
│   └── dashboard.html
│
└── MovimentaIFApp/
    ├── app/src/main/
    │   ├── java/com/ifrs/movimentaif/
    │   │   ├── ui/              # Activities & Fragments
    │   │   ├── model/           # Data Classes
    │   │   ├── api/             # Retrofit & Services
    │   │   ├── utils/           # Utilities (SoundManager, etc)
    │   │   ├── HomeActivity.kt
    │   │   └── LoginActivity.kt
    │   ├── res/
    │   │   ├── layout/          # XML Layouts
    │   │   ├── raw/             # som.mp3
    │   │   └── values/          # Strings, Colors, Themes
    │   └── AndroidManifest.xml
    ├── build.gradle.kts
    └── google-services.json
```

## 🔄 Fluxo de Trabalho Típico

### Adicionar Nova Funcionalidade

1. **Backend (API)**
   - Criar model em `model/`
   - Criar service em `service/`
   - Criar controller em `controller/`
   - Testar endpoints localmente
   - Deploy no Heroku

2. **Mobile (App)**
   - Criar data class em `model/`
   - Adicionar endpoints em `ApiService.kt`
   - Criar Activity/Fragment em `ui/`
   - Criar layout XML em `res/layout/`
   - Registrar no AndroidManifest
   - Build e teste

3. **Web Admin**
   - Criar página HTML
   - Adicionar lógica JS
   - Estilizar com CSS
   - Integrar com Firebase SDK

## 💡 Convenções de Código

### Java (API)
- CamelCase para classes e métodos
- Javadoc para métodos públicos
- Services separados dos Controllers
- DTOs quando necessário

### Kotlin (App)
- camelCase para variáveis e funções
- PascalCase para classes
- Data classes para models
- Coroutines para operações assíncronas
- ViewBinding para acessar views
- Evitar !! (null assertion)

### JavaScript (Web)
- camelCase para variáveis e funções
- async/await para Firebase
- Const/Let ao invés de var
- Arrow functions quando apropriado

## 🎯 Roadmap de Funcionalidades

### Implementado ✅
- Sistema de autenticação completo
- Gerenciamento de usuários
- Catálogo de exercícios
- Fichas de treino semanais
- Questionários PAR-Q e Anamnese
- Compartilhamento de exercícios
- Sons de interface
- Conclusão diária de treinos
- Estatísticas de treinos
- Perfil editável

### Planejado 📋
- Notificações push
- Agenda de horários
- Relatórios de progresso
- Fotos de evolução
- Chat com instrutores
- Gamificação (badges, conquistas)
- Integração com wearables
- Modo offline

## 🔐 Segurança

### Implementado
- JWT Authentication via Firebase
- CORS configurado
- Credenciais em variáveis de ambiente
- HTTPS obrigatório em produção
- Validação de tokens em cada request
- Role-based access (USER, INSTRUCTOR, ADMIN)

### Boas Práticas
- Nunca commitar credenciais no Git
- Usar `.gitignore` para arquivos sensíveis
- Rotacionar tokens periodicamente
- Validar input do usuário
- Sanitizar dados antes de salvar

## 📚 Recursos de Aprendizado

### Documentação Oficial
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Firebase](https://firebase.google.com/docs)
- [Kotlin](https://kotlinlang.org/docs/home.html)
- [Android Developers](https://developer.android.com)
- [Retrofit](https://square.github.io/retrofit/)

### Específico do Projeto
- `README.md` - Visão geral e instalação
- `MODELO-ER.md` - Diagrama de entidades
- `README-DEPLOY.md` - Deploy no Heroku
- `SECURITY.md` / `SECURITY-ADMIN.md` - Guias de segurança
- `CHANGELOG.md` - Histórico de mudanças

## 🤖 Instruções para Assistentes de IA

### Ao Trabalhar Neste Projeto

1. **Sempre verificar** se a API está deployada antes de testar no app
2. **Confirmar** estrutura do Firebase antes de criar novos endpoints
3. **Manter consistência** entre models do Java e Kotlin
4. **Testar localmente** antes de fazer deploy
5. **Documentar** mudanças significativas
6. **Preservar** a segurança e não expor credenciais
7. **Usar** `--no-daemon` no Gradle para evitar processos pendurados
8. **Verificar** compilação antes de commitar código

### Comandos Úteis de Referência

```bash
# API - Build local
cd MovimentaIF-API
mvn clean install

# API - Deploy Heroku
git subtree push --prefix MovimentaIF-API heroku main

# App - Build Debug
cd MovimentaIFApp
.\gradlew assembleDebug --no-daemon

# App - Ver logs
.\gradlew --info

# Git - Status
git status
git log --oneline -10

# Heroku - Logs
heroku logs --tail --app movimentaif-api
```

### Padrões de Commit

- `feat:` Nova funcionalidade
- `fix:` Correção de bug
- `docs:` Documentação
- `style:` Formatação
- `refactor:` Refatoração de código
- `test:` Testes
- `chore:` Tarefas de manutenção

Exemplo: `feat: adicionar sistema de conclusão diária de treinos`

---

**Última Atualização**: 15/11/2025  
**Versão da API**: v28 (Heroku)  
**Versão do App**: 0.0.1-SNAPSHOT
