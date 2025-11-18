import { auth, API_BASE_URL, getAuthTokenAndFetch, onAuthStateChanged, signOut } from "./firebaseConfig.js";

const USERS_API_URL = `${API_BASE_URL}/users`;
const loadingSpinner = document.getElementById('loading-spinner');
const editFormContainer = document.getElementById('edit-form-container');
const editUserStatusForm = document.getElementById('editUserStatusForm');
const statusMessageEl = document.getElementById('status-message');

let currentUserId = null;
let currentUserData = null; // Armazena todos os dados do usuário

function showMessage(message, isError = true) {
    statusMessageEl.textContent = message;
    statusMessageEl.classList.remove('d-none', 'alert-success', 'alert-danger');
    statusMessageEl.classList.add(isError ? 'alert-danger' : 'alert-success');
}

onAuthStateChanged(auth, user => {
    if (user) {
        document.getElementById('user-email').textContent = user.email;
        loadEditForm();
    } else {
        window.location.replace('index.html');
    }
});

document.getElementById('logout-btn').addEventListener('click', () => {
    signOut(auth).then(() => {
        window.location.replace('index.html');
    });
});

// Navegação de volta para user-details
document.getElementById('back-btn').addEventListener('click', (e) => {
    e.preventDefault();
    if (currentUserId) {
        window.location.href = `user-details.html?uid=${currentUserId}`;
    } else {
        window.location.href = `users.html`;
    }
});

async function loadEditForm() {
    loadingSpinner.classList.remove('d-none');
    editFormContainer.classList.add('d-none');

    const urlParams = new URLSearchParams(window.location.search);
    currentUserId = urlParams.get('uid');

    if (!currentUserId) {
        alert("Erro: ID de usuário ausente.");
        window.location.href = 'users.html';
        return;
    }

    try {
        const response = await getAuthTokenAndFetch(`${USERS_API_URL}/${currentUserId}`);
        if (!response.ok) throw new Error("Falha ao carregar dados do usuário.");

        const user = await response.json();
        currentUserData = user; // Armazena todos os dados

        document.getElementById('user-name-title').textContent = user.userName || user.email;
        document.getElementById('user-id-field').value = currentUserId;

        // Preenche os switches com os valores atuais
        document.getElementById('edit-interviewed').checked = user.interviewed || false;
        document.getElementById('edit-did-first-workout').checked = user.didFirstWorkout || false;
        document.getElementById('edit-is-active').checked = user.isActive || false;
        // Simulação: Assinado
        document.getElementById('edit-signed-commitment').checked = user.interviewed || false;

        editFormContainer.classList.remove('d-none');

    } catch (error) {
        showMessage(`Erro: ${error.message}`, true);
    } finally {
        loadingSpinner.classList.add('d-none');
    }
}

editUserStatusForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const userId = document.getElementById('user-id-field').value;
    const interviewStatus = document.getElementById('edit-interviewed').checked;
    const firstWorkoutStatus = document.getElementById('edit-did-first-workout').checked;
    const activeStatus = document.getElementById('edit-is-active').checked;

    // CRÍTICO: Envia TODOS os campos preservando os valores existentes
    const updatedData = {
        userId: currentUserData.userId,
        userName: currentUserData.userName,
        email: currentUserData.email,
        age: currentUserData.age,
        phoneNumber: currentUserData.phoneNumber,
        role: currentUserData.role,
        createdAt: currentUserData.createdAt,
        isActive: Boolean(activeStatus),
        affiliationType: currentUserData.affiliationType,
        interviewed: Boolean(interviewStatus),
        didFirstWorkout: Boolean(firstWorkoutStatus),
        scheduledFirstWorkout: Boolean(currentUserData.scheduledFirstWorkout),
        isAppUser: Boolean(currentUserData.isAppUser),
        firstWorkoutDate: currentUserData.firstWorkoutDate, // Preserva a data
        interviewDate: currentUserData.interviewDate, // Preserva a data
        signedTermOfCommitment: Boolean(currentUserData.signedTermOfCommitment),
        workoutChartId: currentUserData.workoutChartId,
        isAdmin: currentUserData.isAdmin,
        parqId: currentUserData.parqId,
        anamneseId: currentUserData.anamneseId,
        userObs: currentUserData.userObs
    };

    console.log('Enviando atualização de status:', updatedData);

    try {
        const response = await getAuthTokenAndFetch(`${USERS_API_URL}/${userId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updatedData)
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Falha ao salvar status. ${errorText}`);
        }

        showMessage("Status do usuário salvo com sucesso!", false);
        // Redireciona de volta para a tela de detalhes após um pequeno delay
        setTimeout(() => {
            window.location.href = `user-details.html?uid=${userId}`;
        }, 1000);

    } catch (error) {
        showMessage(`Erro ao salvar: ${error.message}`, true);
    }
});