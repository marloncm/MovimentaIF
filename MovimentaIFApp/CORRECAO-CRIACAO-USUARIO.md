# 🐛 Correção: Usuário não sendo criado no Firestore

## Problema Identificado

Quando um usuário era criado no app Android, ele era registrado no **Firebase Authentication**, mas o documento **não era criado no Firestore** através da API.

### Causa Raiz

O fluxo estava assim:

```kotlin
// RegisterActivity.kt - ANTES (ERRADO)
val newUser = User(username, email, false)
registerUserInInternalApi(newUser)
```

```java
// User.java - Construtor problemático
public User(String name, String email, Boolean isAdmin){
    this.userId = UUID.randomUUID().toString();  // ⚠️ PROBLEMA!
    this.userName = name;
    this.email = email;
    // ...
}
```

**Problema:** O `userId` era gerado como um UUID aleatório em vez de usar o UID do Firebase Authentication.

Quando a API recebia o usuário:

```java
// UserController.java
@PostMapping
public void newUser(@RequestBody User user) throws Exception {
    User existingUser = userService.getUserById(user.getUserId());
    if (existingUser == null) {
        userService.saveUser(user);  // Salvava com ID errado!
    }
}
```

O documento era criado no Firestore com um ID diferente do Firebase Auth, causando:
- ❌ Usuário duplicado (um no Auth, outro no Firestore)
- ❌ IDs incompatíveis
- ❌ Impossibilidade de vincular dados

---

## Solução Implementada

### 1. Atualizado RegisterActivity.kt

```kotlin
// RegisterActivity.kt - DEPOIS (CORRETO)
auth.createUserWithEmailAndPassword(email, password)
    .addOnCompleteListener(this) { task ->
        if (task.isSuccessful) {
            Log.d("Firebase", "createUserWithEmail:success")

            // ✅ Pegar o UID do usuário criado no Firebase
            val firebaseUser = auth.currentUser
            val userId = firebaseUser?.uid ?: ""

            // ✅ Criar User com o UID correto do Firebase
            val newUser = User(userId, email)
            newUser.userName = username
            newUser.isAppUser = true
            newUser.isActive = true

            // Chamar API com o UID correto
            registerUserInInternalApi(newUser)
        }
    }
```

### 2. Fluxo Corrigido

```
1. App cria usuário no Firebase Auth
   ↓
2. Firebase retorna UID (ex: "cq8J2Qdw41OdlJuPMWZjZYpU66D2")
   ↓
3. App cria objeto User com esse UID
   ↓
4. App envia para API POST /api/users
   ↓
5. API salva no Firestore com o mesmo UID
   ↓
6. ✅ Usuário sincronizado: Auth UID = Firestore Document ID
```

---

## Benefícios da Correção

### ✅ Antes (Problema)
```
Firebase Auth:
├─ UID: cq8J2Qdw41OdlJuPMWZjZYpU66D2
└─ Email: teste@email.com

Firestore:
├─ Document ID: a1b2c3d4-random-uuid  ❌ DIFERENTE!
└─ email: teste@email.com
```

### ✅ Depois (Correto)
```
Firebase Auth:
├─ UID: cq8J2Qdw41OdlJuPMWZjZYpU66D2
└─ Email: teste@email.com

Firestore:
├─ Document ID: cq8J2Qdw41OdlJuPMWZjZYpU66D2  ✅ MESMO ID!
└─ email: teste@email.com
└─ userName: "João Silva"
└─ isAppUser: true
└─ isActive: true
```

---

## Como Testar

### 1. Criar Novo Usuário no App

1. Abra o app
2. Clique em "Registrar"
3. Preencha:
   - Nome: "Teste Novo"
   - Email: "teste.novo@email.com"
   - Senha: "123456"
4. Clique em "Confirmar Registro"

### 2. Verificar no Firebase Console

**Firebase Authentication:**
```
UID: abc123xyz (exemplo)
Email: teste.novo@email.com
```

**Firestore Database:**
```
Collection: users
Document ID: abc123xyz (mesmo UID!)
Fields:
  - userName: "Teste Novo"
  - email: "teste.novo@email.com"
  - isAppUser: true
  - isActive: true
  - createdAt: 2025-11-15...
```

### 3. Verificar via API

```bash
# 1. Fazer login para pegar token
curl --location 'https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=AIzaSyAnET6gJ175qHFbHcKm40tynj7s9x4sXqU' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "teste.novo@email.com",
    "password": "123456",
    "returnSecureToken": true
}'

# 2. Copiar o idToken retornado

# 3. Listar usuários
curl --location 'https://movimentaif-api-7895a5f0638f.herokuapp.com/api/users/appusers' \
--header 'Authorization: Bearer SEU_TOKEN_AQUI'

# 4. Verificar que o novo usuário aparece na lista
```

---

## Arquivos Modificados

```
✏️ MovimentaIFApp/app/src/main/java/com/ifrs/movimentaif/RegisterActivity.kt
   - Alterado para pegar UID do Firebase Auth
   - Usar construtor User(userId, email)
   - Definir userName, isAppUser, isActive após criação
```

---

## Commits

```bash
# Código commitado
git add MovimentaIFApp/app/src/main/java/com/ifrs/movimentaif/RegisterActivity.kt
git commit -m "fix: usar UID do Firebase Auth ao criar usuário no Firestore

- Corrigido RegisterActivity para pegar UID do currentUser
- Usar construtor User(userId, email) em vez de gerar UUID
- Garantir sincronização entre Firebase Auth e Firestore
- Document ID no Firestore agora é igual ao UID do Auth"
```

---

## Observações Importantes

### ⚠️ Usuários Antigos

Se você já criou usuários antes dessa correção, eles podem ter IDs diferentes:
- **Firebase Auth:** UID real
- **Firestore:** UUID aleatório

**Solução:** Deletar usuários antigos e recriar, ou fazer migração manual.

### ✅ Novos Usuários

Todos os usuários criados após essa correção terão:
- **ID único e consistente**
- **Sincronização perfeita** entre Auth e Firestore
- **Facilidade para consultas** e relacionamentos

---

## Próximos Passos

1. ✅ Testar criação de novos usuários
2. ✅ Verificar sincronização Auth ↔ Firestore
3. ⚠️ Migrar usuários antigos (se necessário)
4. ✅ Atualizar documentação
5. ✅ Gerar novo APK com correção

---

**Data da Correção:** 15/11/2025  
**Versão:** 1.0.1  
**Status:** ✅ Corrigido e testado
