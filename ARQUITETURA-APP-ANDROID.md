# 📱 Arquitetura do Aplicativo Android MovimentaIF

## 📋 Índice
1. [Visão Geral](#visão-geral)
2. [Estrutura de Pacotes](#estrutura-de-pacotes)
3. [Dependências e Bibliotecas](#dependências-e-bibliotecas)
4. [AndroidManifest.xml](#androidmanifestxml)
5. [Comunicação com API](#comunicação-com-api)
6. [Fluxo de Autenticação](#fluxo-de-autenticação)
7. [Arquitetura MVVM](#arquitetura-mvvm)
8. [Segurança](#segurança)

---

## 🎯 Visão Geral

O **MovimentaIF** é um aplicativo Android nativo desenvolvido em **Kotlin** que utiliza:
- **Firebase Authentication** para gerenciamento de usuários
- **Retrofit** para comunicação com API REST
- **MVVM (Model-View-ViewModel)** como padrão arquitetural
- **ViewBinding** para manipulação de views
- **Coroutines** para operações assíncronas
- **EncryptedSharedPreferences** para armazenamento seguro
- **Biometric API** para autenticação biométrica

---

## 📦 Estrutura de Pacotes

```
com.ifrs.movimentaif/
├── api/                           # Camada de Comunicação com API
│   ├── ApiService.kt             # Interface Retrofit com todos os endpoints
│   └── RetrofitInstance.kt       # Configuração singleton do Retrofit
│
├── model/                         # Modelos de Dados (DTOs)
│   ├── User.java                 # Modelo de usuário
│   ├── Workout.kt                # Modelo de exercício
│   ├── WorkoutChart.kt           # Modelo de ficha de treino
│   ├── UserWorkout.kt            # Exercício atribuído ao usuário
│   ├── ParQ.java                 # Questionário PAR-Q
│   ├── Anamnese.java             # Anamnese médica
│   ├── DailyWorkoutCompletion.kt # Registro de treino completo
│   ├── ExerciseCompletion.kt     # Registro de exercício completo
│   └── AcademyInfo.kt            # Informações da academia
│
├── ui/                            # Camada de Interface (Activities e Fragments)
│   ├── home/                     # Tela principal (Dashboard)
│   │   ├── HomeFragment.kt
│   │   └── HomeViewModel.kt
│   ├── profile/                  # Perfil do usuário
│   │   ├── ProfileFragment.kt
│   │   └── ProfileEditActivity.kt
│   ├── workoutlist/              # Lista de exercícios
│   │   ├── WorkoutListFragment.kt
│   │   └── WorkoutAdapter.kt
│   ├── userworkouts/             # Treinos do usuário
│   │   └── UserWorkoutsFragment.kt
│   ├── parq/                     # Questionário PAR-Q
│   │   └── ParQActivity.kt
│   ├── anamnese/                 # Anamnese médica
│   │   └── AnamneseActivity.kt
│   ├── gallery/                  # Galeria (educativo)
│   │   ├── GalleryFragment.kt
│   │   └── GalleryViewModel.kt
│   ├── about/                    # Sobre o app
│   │   └── AboutFragment.kt
│   └── logout/                   # Logout
│       └── LogoutFragment.kt
│
├── services/                      # Serviços em Background
│   └── MyFirebaseMessagingService.kt  # Push notifications
│
├── utils/                         # Utilitários e Helpers
│   ├── SecurePreferences.kt      # SharedPreferences criptografadas
│   ├── BiometricManager.kt       # Gerenciador de biometria
│   ├── SoundManager.kt           # Gerenciador de sons
│   └── ViewExtensions.kt         # Extensões para Views
│
├── SplashActivity.kt              # Tela de splash (inicial)
├── LoginActivity.kt               # Tela de login
├── RegisterActivity.kt            # Tela de cadastro
├── HomeActivity.kt                # Activity principal com Navigation Drawer
└── MainActivity.kt                # (Legacy - não utilizada)
```

---

## 📚 Dependências e Bibliotecas

### **1. Core Android**
```kotlin
androidx.core:core-ktx:1.17.0               // Extensões Kotlin para Android
androidx.appcompat:appcompat:1.7.1          // Compatibilidade com versões antigas
androidx.constraintlayout:constraintlayout  // Layouts responsivos
```

**Função**: Bibliotecas essenciais do Android que fornecem APIs modernas e compatibilidade retroativa.

---

### **2. Material Design**
```kotlin
com.google.android.material:material:1.13.0
```

**Função**: Componentes visuais seguindo as diretrizes do Material Design 3 (cards, buttons, navigation drawer, etc.).

---

### **3. Firebase**
```kotlin
firebase-bom:34.5.0                         // Bill of Materials (gerencia versões)
firebase-auth:24.0.1                        // Autenticação
firebase-messaging:23.4.0                   // Push Notifications
```

**Função**:
- **Firebase Authentication**: Gerencia login/cadastro com email/senha e Google Sign-In
- **Firebase Cloud Messaging (FCM)**: Envio de notificações push para usuários
- **Firebase BOM**: Garante que todas as bibliotecas Firebase sejam compatíveis entre si

---

### **4. Retrofit (Comunicação HTTP)**
```kotlin
retrofit:3.0.0                              // Cliente HTTP
converter-gson:3.0.0                        // Conversor JSON ↔ Kotlin
okhttp:4.12.0                               // Cliente HTTP base
okhttp-logging-interceptor:4.12.0           // Log de requisições
```

**Função**:
- **Retrofit**: Framework para fazer requisições HTTP de forma declarativa
- **Gson**: Converte JSON da API em objetos Kotlin/Java automaticamente
- **OkHttp**: Cliente HTTP robusto com suporte a interceptors
- **Logging Interceptor**: Registra todas as requisições/respostas (apenas em debug)

---

### **5. Lifecycle e ViewModel**
```kotlin
lifecycle-livedata-ktx:2.9.4                // Dados observáveis
lifecycle-viewmodel-ktx:2.9.4               // ViewModels
lifecycle-runtime-ktx:2.9.4                 // Coroutines lifecycle-aware
```

**Função**: Implementa o padrão MVVM com dados observáveis e gerenciamento de estado que sobrevive a mudanças de configuração.

---

### **6. Navigation Component**
```kotlin
navigation-fragment-ktx:2.9.5               // Navegação entre fragments
navigation-ui-ktx:2.9.5                     // UI components de navegação
```

**Função**: Gerencia navegação entre telas (fragments) de forma declarativa com suporte a deep links e transições animadas.

---

### **7. Google Sign-In**
```kotlin
play-services-auth:21.4.0                   // Google Sign-In
credentials:1.5.0                           // Credentials API
credentials-play-services-auth:1.5.0        // Integração com Play Services
googleid:1.1.1                              // Google ID
```

**Função**: Permite login com conta Google usando a API moderna de credenciais do Android.

---

### **8. Segurança**
```kotlin
androidx.biometric:biometric:1.2.0-alpha05          // Autenticação biométrica
androidx.security:security-crypto:1.1.0-alpha06     // Criptografia de dados
```

**Função**:
- **Biometric**: Autenticação por impressão digital/reconhecimento facial
- **Security Crypto**: Criptografa SharedPreferences usando AES-256

---

### **9. Testes**
```kotlin
junit:4.13.2                                // Testes unitários
androidx.junit:1.3.0                        // Testes Android
androidx.espresso-core:3.7.0               // Testes de UI
```

---

## 🔧 AndroidManifest.xml

### **Permissões Necessárias**

```xml
<uses-permission android:name="android.permission.INTERNET" />
```
- **Motivo**: Comunicação com API REST externa (Heroku)
- **Tipo**: Normal (não requer consentimento explícito do usuário)

```xml
<uses-permission android:name="android.permission.USE_BIOMETRIC" />
<uses-permission android:name="android.permission.USE_FINGERPRINT" />
```
- **Motivo**: Autenticação por impressão digital/reconhecimento facial
- **Tipo**: Normal (API moderna usa BiometricPrompt)

```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```
- **Motivo**: Enviar notificações push (Android 13+)
- **Tipo**: Dangerous (requer consentimento do usuário em runtime)

---

### **Configurações de Segurança**

```xml
<application
    android:allowBackup="false"                         <!-- Desativa backup automático -->
    android:networkSecurityConfig="@xml/network_security_config"
    android:usesCleartextTraffic="false">              <!-- Bloqueia HTTP não criptografado -->
```

**Arquivo `network_security_config.xml`**:
```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <base-config cleartextTrafficPermitted="false">
        <trust-anchors>
            <certificates src="system" />
        </trust-anchors>
    </base-config>
</network-security-config>
```
- **Função**: Força uso de HTTPS em todas as requisições
- **Segurança**: Impede ataques Man-in-the-Middle

---

### **Activities Declaradas**

#### **1. SplashActivity** (Tela Inicial)
```xml
<activity android:name=".SplashActivity" android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```
- **Função**: Primeira tela ao abrir o app
- **Fluxo**: Verifica se usuário está logado → redireciona para `HomeActivity` ou `LoginActivity`

#### **2. LoginActivity**
- **Função**: Tela de login com email/senha ou Google Sign-In
- **Suporte a Biometria**: Se habilitada, usa impressão digital

#### **3. RegisterActivity**
- **Função**: Cadastro de novo usuário
- **Validações**: CPF, email, senha forte

#### **4. HomeActivity** (Principal)
- **Função**: Navigation Drawer com acesso a todas as funcionalidades
- **Fragments**: Home, Perfil, Treinos, Exercícios, Sobre, Logout

#### **5. ProfileEditActivity**
- **Função**: Edição de dados do perfil
- **parentActivityName**: Define botão "voltar" na ActionBar

#### **6. ParQActivity**
- **Função**: Questionário PAR-Q (Physical Activity Readiness Questionnaire)
- **Validação**: 7 perguntas obrigatórias

#### **7. AnamneseActivity**
- **Função**: Anamnese médica detalhada
- **Campos**: Histórico de saúde, medicamentos, lesões, etc.

---

### **Services**

#### **MyFirebaseMessagingService**
```xml
<service
    android:name=".services.MyFirebaseMessagingService"
    android:exported="false">
    <intent-filter>
        <action android:name="com.google.firebase.MESSAGING_EVENT" />
    </intent-filter>
</service>
```
- **Função**: Recebe notificações push do Firebase Cloud Messaging
- **Uso**: Avisos sobre novos treinos, lembretes de exercícios, etc.

---

## 🌐 Comunicação com API

### **1. RetrofitInstance.kt** (Singleton)

```kotlin
object RetrofitInstance {
    private const val BASE_URL = "https://movimentaif-api-7895a5f0638f.herokuapp.com/"
    
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }
}
```

**Componentes**:

#### **A. Gson com Configuração de Datas**
```kotlin
private val gson = GsonBuilder()
    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    .setLenient()
    .create()
```
- **Função**: Converte datas ISO-8601 entre JSON e objetos Kotlin

#### **B. Auth Interceptor (JWT Automático)**
```kotlin
private val authInterceptor = Interceptor { chain ->
    val currentUser = FirebaseAuth.getInstance().currentUser
    val token = currentUser?.getIdToken(false)?.result?.token
    
    val request = chain.request().newBuilder()
        .addHeader("Authorization", "Bearer $token")
        .build()
    
    chain.proceed(request)
}
```
- **Função**: Adiciona token JWT do Firebase em **todas** as requisições
- **Segurança**: API valida o token antes de processar a requisição

#### **C. Logging Interceptor (Debug)**
```kotlin
private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor.Level.BODY  // Log completo em debug
    } else {
        HttpLoggingInterceptor.Level.NONE  // Sem logs em produção
    }
}
```

#### **D. OkHttpClient com Timeouts**
```kotlin
private val httpClient = OkHttpClient.Builder()
    .addInterceptor(authInterceptor)
    .addInterceptor(loggingInterceptor)
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()
```

---

### **2. ApiService.kt** (Interface Retrofit)

Define todos os endpoints da API REST:

#### **Usuários**
```kotlin
@POST("api/users")
suspend fun registerUser(@Body user: User): Response<User>

@GET("api/users/{uid}")
suspend fun getUserById(@Path("uid") uid: String): Response<User>

@PUT("api/users/{uid}")
suspend fun updateUser(@Path("uid") uid: String, @Body user: User): Response<User>
```

#### **Exercícios e Treinos**
```kotlin
@GET("api/workouts")
suspend fun getAllWorkouts(): Response<List<Workout>>

@GET("api/charts/user/{userId}")
suspend fun getWorkoutChartByUserId(@Path("userId") userId: String): Response<WorkoutChart>

@GET("api/user-workouts/user/{userId}")
suspend fun getUserWorkoutsByUserId(@Path("userId") userId: String): Response<List<UserWorkout>>
```

#### **PAR-Q e Anamnese**
```kotlin
@POST("api/parq")
suspend fun createParQ(@Body parq: ParQ): Response<ParQ>

@GET("api/parq/user/{userId}")
suspend fun getParQByUserId(@Path("userId") userId: String): Response<ParQ>

@POST("api/anamnese")
suspend fun createAnamnese(@Body anamnese: Anamnese): Response<Anamnese>
```

#### **Registro de Completude**
```kotlin
@POST("api/workout-completions")
suspend fun createWorkoutCompletion(@Body completion: DailyWorkoutCompletion): Response<DailyWorkoutCompletion>

@GET("api/workout-completions/user/{userId}/total")
suspend fun getTotalWorkoutsCompleted(@Path("userId") userId: String): Response<Int>

@POST("api/exercise-completions")
suspend fun createExerciseCompletion(@Body completion: ExerciseCompletion): Response<ExerciseCompletion>
```

#### **Informações da Academia**
```kotlin
@GET("api/academy-info")
suspend fun getAcademyInfo(): Response<AcademyInfo>
```

---

### **3. Como Fazer Requisições**

#### **Exemplo: Buscar dados do usuário**
```kotlin
// Em ViewModel ou Repository
viewModelScope.launch {
    try {
        val response = RetrofitInstance.api.getUserById(userId)
        if (response.isSuccessful) {
            val user = response.body()
            // Atualizar LiveData/StateFlow
        } else {
            // Tratar erro HTTP (400, 404, 500, etc.)
        }
    } catch (e: Exception) {
        // Tratar erro de rede/timeout
    }
}
```

#### **Fluxo de Requisição**:
1. **App** chama `RetrofitInstance.api.getUserById(uid)`
2. **AuthInterceptor** adiciona `Authorization: Bearer <token>`
3. **Retrofit** converte para HTTP GET
4. **OkHttp** envia para `https://movimentaif-api.herokuapp.com/api/users/{uid}`
5. **API Java/Spring Boot** valida token JWT
6. **Firestore** retorna dados do usuário
7. **Gson** converte JSON → objeto `User`
8. **App** recebe resposta e atualiza UI

---

## 🔐 Fluxo de Autenticação

### **1. Cadastro (RegisterActivity)**
```
Usuário preenche formulário
    ↓
Validações locais (CPF, email, senha forte)
    ↓
Firebase Authentication cria conta
    ↓
Gera UID único
    ↓
Envia dados para API REST (POST /api/users)
    ↓
API salva no Firestore
    ↓
Redireciona para HomeActivity
```

### **2. Login com Email/Senha (LoginActivity)**
```
Usuário digita email e senha
    ↓
Firebase Authentication valida credenciais
    ↓
Se válido: gera JWT token
    ↓
Busca dados do usuário na API (GET /api/users/{uid})
    ↓
Salva userId em SecurePreferences
    ↓
Redireciona para HomeActivity
```

### **3. Login com Google (One Tap Sign-In)**
```
Usuário clica em "Entrar com Google"
    ↓
Abre seletor de contas do Google
    ↓
Firebase autentica com ID Token do Google
    ↓
Verifica se usuário já existe na API
    ↓
Se não existe: cria novo registro
    ↓
Redireciona para HomeActivity
```

### **4. Login com Biometria**
```
App verifica SecurePreferences.isBiometricEnabled()
    ↓
Se habilitado: exibe BiometricPrompt
    ↓
Sistema Android valida impressão digital
    ↓
Se válido: recupera email/userId das preferências
    ↓
Firebase faz login silencioso (currentUser já existe)
    ↓
Redireciona para HomeActivity
```

---

## 🏗️ Arquitetura MVVM

### **Camadas**

```
┌─────────────────────────────────────┐
│   VIEW (Activity/Fragment)          │  ← UI + ViewBinding
│   - Observa LiveData/StateFlow      │
│   - Exibe dados ao usuário          │
└──────────────┬──────────────────────┘
               │
               ↓
┌─────────────────────────────────────┐
│   VIEWMODEL                         │  ← Lógica de apresentação
│   - Gerencia estado da UI           │
│   - Expõe LiveData/StateFlow        │
│   - Chama Repository                │
└──────────────┬──────────────────────┘
               │
               ↓
┌─────────────────────────────────────┐
│   REPOSITORY (Opcional)             │  ← Abstração de dados
│   - Decide fonte de dados           │
│   - Cache/Offline-first             │
└──────────────┬──────────────────────┘
               │
               ↓
┌─────────────────────────────────────┐
│   API (Retrofit)                    │  ← Comunicação externa
│   - Requisições HTTP                │
│   - Conversão JSON ↔ Objetos        │
└─────────────────────────────────────┘
```

### **Exemplo: HomeFragment + HomeViewModel**

#### **HomeViewModel.kt**
```kotlin
class HomeViewModel : ViewModel() {
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName
    
    fun loadUserData(userId: String) {
        viewModelScope.launch {
            val response = RetrofitInstance.api.getUserById(userId)
            if (response.isSuccessful) {
                _userName.value = response.body()?.name
            }
        }
    }
}
```

#### **HomeFragment.kt**
```kotlin
class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        
        // Observa mudanças nos dados
        viewModel.userName.observe(viewLifecycleOwner) { name ->
            binding.textUserName.text = name
        }
        
        // Carrega dados
        viewModel.loadUserData(userId)
    }
}
```

---

## 🔒 Segurança

### **1. EncryptedSharedPreferences**
```kotlin
class SecurePreferences(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        PrefKeyEncryptionScheme.AES256_SIV,
        PrefValueEncryptionScheme.AES256_GCM
    )
}
```
- **Criptografia**: AES-256-GCM (padrão militar)
- **Armazena**: Email, userId, flag de biometria habilitada
- **Proteção**: Mesmo com root no device, dados são ilegíveis

### **2. BiometricPrompt**
```kotlin
class BiometricManager(private val activity: FragmentActivity) {
    fun authenticate(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticação Biométrica")
            .setNegativeButtonText("Usar senha")
            .build()
        
        biometricPrompt.authenticate(promptInfo)
    }
}
```
- **Suporte**: Impressão digital, reconhecimento facial
- **Fallback**: Senha do dispositivo se biometria falhar

### **3. Network Security**
- ✅ HTTPS obrigatório (TLS 1.2+)
- ✅ Certificate Pinning (configurável)
- ✅ Sem cleartext traffic (HTTP bloqueado)

### **4. Autenticação JWT**
```
Cliente                 Firebase              API REST
  |                        |                     |
  |------ Login ---------->|                     |
  |<----- JWT Token --------|                     |
  |                                              |
  |-------- API Request (Header: Bearer Token) ->|
  |                                        Valida Token
  |                                        Consulta Firestore
  |<----- Resposta JSON -------------------------|
```

### **5. ProGuard (Obfuscação)**
```gradle
buildTypes {
    release {
        isMinifyEnabled = true
        isShrinkResources = true
        proguardFiles("proguard-rules.pro")
    }
}
```
- **Função**: Ofusca código Java/Kotlin no APK de produção
- **Proteção**: Dificulta engenharia reversa

---

## 📊 Fluxo de Dados Completo

### **Exemplo: Marcar Exercício como Completo**

```
1. USUÁRIO clica em "Marcar como Concluído" no Fragment
       ↓
2. FRAGMENT chama ViewModel.markExerciseComplete(exerciseId)
       ↓
3. VIEWMODEL cria objeto ExerciseCompletion
       ↓
4. VIEWMODEL chama RetrofitInstance.api.createExerciseCompletion(completion)
       ↓
5. RETROFIT/OKHTTP adiciona Authorization header (JWT)
       ↓
6. RETROFIT envia POST https://.../api/exercise-completions
       ↓
7. API JAVA/SPRING BOOT valida JWT
       ↓
8. API salva no FIRESTORE collection "exercise_completions"
       ↓
9. API retorna Response<ExerciseCompletion>
       ↓
10. GSON converte JSON → objeto Kotlin
       ↓
11. VIEWMODEL atualiza LiveData
       ↓
12. FRAGMENT observa mudança e exibe Toast "Exercício concluído!"
```

---

## 🎯 Principais Componentes e Suas Funções

| Componente | Função | Comunicação |
|------------|--------|-------------|
| **SplashActivity** | Tela inicial, verifica autenticação | → LoginActivity ou HomeActivity |
| **LoginActivity** | Autenticação (email, Google, biometria) | → Firebase Auth → API → HomeActivity |
| **RegisterActivity** | Cadastro de novo usuário | → Firebase Auth → API → HomeActivity |
| **HomeActivity** | Navigation Drawer, container de fragments | ↔ Fragments via Navigation Component |
| **HomeFragment** | Dashboard com resumo de treinos | → API (getUserWorkouts, getAcademyInfo) |
| **WorkoutListFragment** | Lista de exercícios disponíveis | → API (getAllWorkouts) |
| **UserWorkoutsFragment** | Treinos atribuídos ao usuário | → API (getUserWorkoutsByUserId) |
| **ProfileFragment** | Exibe dados do perfil | → API (getUserById) |
| **ProfileEditActivity** | Edição de perfil | → API (updateUser) |
| **ParQActivity** | Questionário PAR-Q | → API (createParQ, updateParQ) |
| **AnamneseActivity** | Anamnese médica | → API (createAnamnese, updateAnamnese) |
| **RetrofitInstance** | Singleton do Retrofit com interceptors | → API REST (Heroku) |
| **ApiService** | Interface com endpoints da API | Usado por todos os ViewModels |
| **SecurePreferences** | Armazenamento criptografado | Usado por LoginActivity e BiometricManager |
| **BiometricManager** | Autenticação biométrica | Usado por LoginActivity |
| **MyFirebaseMessagingService** | Notificações push | ← Firebase Cloud Messaging |

---

## 🔄 Comunicação Entre Componentes

### **Navigation Component**
```xml
<!-- nav_graph.xml -->
<navigation>
    <fragment id="@+id/nav_home" name="HomeFragment" />
    <fragment id="@+id/nav_profile" name="ProfileFragment" />
    <fragment id="@+id/nav_workouts" name="UserWorkoutsFragment" />
    
    <action id="@+id/action_home_to_profile" 
            destination="@+id/nav_profile" />
</navigation>
```

**Em HomeActivity**:
```kotlin
val navController = findNavController(R.id.nav_host_fragment)
NavigationUI.setupWithNavController(binding.navView, navController)
```

### **ViewBinding**
```kotlin
// Fragment
private var _binding: FragmentHomeBinding? = null
private val binding get() = _binding!!

override fun onCreateView(inflater: LayoutInflater, ...): View {
    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    return binding.root
}

override fun onDestroyView() {
    super.onDestroyView()
    _binding = null  // Evita memory leak
}
```

---

## 🚀 Resumo da Stack Tecnológica

| Categoria | Tecnologia | Versão |
|-----------|------------|--------|
| **Linguagem** | Kotlin | 2.0.21 |
| **SDK Mínimo** | Android 7.0 (API 24) | - |
| **SDK Alvo** | Android 14 (API 36) | - |
| **Autenticação** | Firebase Auth | 24.0.1 |
| **HTTP Client** | Retrofit + OkHttp | 3.0.0 / 4.12.0 |
| **JSON Parser** | Gson | 3.0.0 |
| **Async** | Kotlin Coroutines | - |
| **UI** | Material Design 3 | 1.13.0 |
| **Arquitetura** | MVVM + LiveData | - |
| **Navegação** | Navigation Component | 2.9.5 |
| **Segurança** | EncryptedSharedPreferences | 1.1.0-alpha06 |
| **Biometria** | AndroidX Biometric | 1.2.0-alpha05 |
| **Notificações** | Firebase Messaging | 23.4.0 |

---

## 📖 Referências

- [Documentação oficial Android](https://developer.android.com/)
- [Firebase Android Setup](https://firebase.google.com/docs/android/setup)
- [Retrofit Documentation](https://square.github.io/retrofit/)
- [Material Design 3](https://m3.material.io/)
- [Kotlin Coroutines Guide](https://kotlinlang.org/docs/coroutines-guide.html)

---

**Última atualização**: 21 de novembro de 2025  
**Versão do App**: 1.0  
**Autor**: Equipe MovimentaIF
