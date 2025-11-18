import { auth, API_BASE_URL, getAuthTokenAndFetch, onAuthStateChanged, signOut } from "./firebaseConfig.js";

const USERS_API_URL = `${API_BASE_URL}/users`;
const loadingSpinner = document.getElementById('loading-spinner');
const userDetailsContainer = document.getElementById('user-details-container');
const contentView = document.getElementById('content-view');
const toggleEditBtn = document.getElementById('toggle-edit-btn');
const cancelEditBtn = document.getElementById('cancel-edit-btn');
const statusMessageEl = document.getElementById('status-message');

let currentUserData = null;
let currentUserId = null;
let currentTab = 'info';
let isEditing = false;

// --- Funções Auxiliares ---
function showMessage(message, isError = true) {
    statusMessageEl.textContent = message;
    statusMessageEl.classList.remove('d-none', 'alert-success', 'alert-danger', 'alert-warning');
    statusMessageEl.classList.add(isError ? 'alert-danger' : 'alert-success');
    setTimeout(() => statusMessageEl.classList.add('d-none'), 3000);
}

/**
 * Converte o valor de data (timestamp ou Date string) em formato local de data e hora.
 */
function formatDateTime(dateValue) {
    if (!dateValue) return 'N/A';

    let date;

    if (typeof dateValue === 'number' || (typeof dateValue === 'string' && /^\d+$/.test(dateValue))) {
        date = new Date(Number(dateValue));
    } else if (typeof dateValue === 'string') {
        date = new Date(dateValue);
    }

    if (!date || isNaN(date.getTime())) return 'N/A';

    const dateOptions = { day: '2-digit', month: '2-digit', year: 'numeric' };
    const timeOptions = { hour: '2-digit', minute: '2-digit', hour12: false };

    const formattedDate = date.toLocaleDateString('pt-BR', dateOptions);
    const formattedTime = date.toLocaleTimeString('pt-BR', timeOptions);

    return `${formattedDate} às ${formattedTime}`;
}

// --- Lógica de Autenticação e Carregamento Inicial ---
onAuthStateChanged(auth, user => {
    if (user) {
        document.getElementById('user-email').textContent = user.email;
        fetchUserDetails();
    } else {
        window.location.replace('index.html');
    }
});

document.getElementById('logout-btn').addEventListener('click', () => {
    signOut(auth).then(() => {
        window.location.replace('index.html');
    });
});

// --- Funções de Edição de Status ---

function toggleEditMode() {
    if (currentTab !== 'info') {
        showMessage("A edição de status só pode ser feita na aba 'Informações Gerais'.", true);
        return;
    }

    isEditing = !isEditing;

    if (isEditing) {
        toggleEditBtn.innerHTML = '<i class="fa-solid fa-save me-1"></i> Salvar Alterações';
        toggleEditBtn.classList.remove('btn-warning');
        toggleEditBtn.classList.add('btn-success');
        // Exibe o botão Cancelar e desabilita o Ficha de Treinos
        cancelEditBtn.classList.remove('d-none');
        document.getElementById('view-workout-btn').disabled = true;
    } else {
        toggleEditBtn.innerHTML = '<i class="fa-solid fa-edit me-1"></i> Editar';
        toggleEditBtn.classList.remove('btn-success');
        toggleEditBtn.classList.add('btn-warning');
        // Esconde o botão Cancelar e reabilita o Ficha de Treinos
        cancelEditBtn.classList.add('d-none');
        document.getElementById('view-workout-btn').disabled = false;
    }

    renderUserTabs(currentTab); // Re-renderiza a aba Info no novo modo
}

async function saveUserStatus() {
    if (!currentUserData || !currentUserId) return;

    // 1. Coleta os novos valores dos switches
    const updatedData = {
        // Preservar todos os campos existentes para o PUT parcial funcionar
        ...currentUserData,

        // Campos booleanos de edição
        interviewed: document.getElementById('switch-interviewed')?.checked,
        didFirstWorkout: document.getElementById('switch-did-first-workout')?.checked,
        active: document.getElementById('switch-is-active')?.checked,
        signedTermOfCommitment: document.getElementById('switch-signed-commitment')?.checked
    };

    delete updatedData.toJSON;

    try {
        // Requisição PUT para o endpoint de atualização
        const response = await getAuthTokenAndFetch(`${USERS_API_URL}/${currentUserId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updatedData)
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Falha ao salvar status. ${errorText}`);
        }

        // O backend deve retornar o objeto atualizado no PUT
        const updatedUser = await response.json();
        currentUserData = updatedUser;

        showMessage("Status do usuário salvo com sucesso!", false);

        // CRÍTICO: Força a saída do modo de edição em caso de sucesso
        isEditing = false;

        // Atualiza a UI para modo de visualização
        toggleEditBtn.innerHTML = '<i class="fa-solid fa-edit me-1"></i> Editar';
        toggleEditBtn.classList.remove('btn-success');
        toggleEditBtn.classList.add('btn-warning');
        cancelEditBtn.classList.add('d-none');
        document.getElementById('view-workout-btn').disabled = false;

        // Re-renderiza sem entrar no loop
        renderUserTabs(currentTab);

    } catch (error) {
        showMessage(`Erro ao salvar: ${error.message}`, true);
        // Mantém no modo de edição para o usuário corrigir
    }
}

toggleEditBtn.addEventListener('click', () => {
    // Se estiver editando E o botão for o de salvar
    if (isEditing && toggleEditBtn.classList.contains('btn-success')) {
        saveUserStatus();
    } else {
        toggleEditMode();
    }
});

// NOVO LISTENER: Cancelar Edição (desfaz as alterações visuais sem recarregar)
cancelEditBtn.addEventListener('click', () => {
    if (isEditing) {
        isEditing = false;

        // Atualiza a UI para modo de visualização
        toggleEditBtn.innerHTML = '<i class="fa-solid fa-edit me-1"></i> Editar';
        toggleEditBtn.classList.remove('btn-success');
        toggleEditBtn.classList.add('btn-warning');
        cancelEditBtn.classList.add('d-none');
        document.getElementById('view-workout-btn').disabled = false;

        // Re-renderiza com os dados originais
        renderUserTabs(currentTab);
        showMessage("Edição cancelada.", false);
    }
});

// --- Funções de Renderização das Abas ---

function renderGeneralInfoContent(user, isEditingMode) {
    const checkIcon = '<i class="fa-solid fa-check-circle me-2 text-success"></i>';
    const crossIcon = '<i class="fa-solid fa-times-circle me-2 text-danger"></i>';

    // Funções de Status para o modo de visualização (Read-Only)
    const getStatusDisplay = (value, label) => `
                <li>
                    ${value ? checkIcon : crossIcon}
                    <span class="text-muted">${label}</span>
                </li>
            `;

    // Funções de Status para o modo de Edição (Switches)
    const getStatusSwitch = (id, label, description, checked) => `
                <div class="col-md-6">
                    <div class="switch-container">
                        <div class="form-check form-switch">
                            <input class="form-check-input" type="checkbox" role="switch" id="${id}" ${checked ? 'checked' : ''}>
                            <label class="form-check-label" for="${id}">${label}</label>
                            <p class="text-muted small mt-1 mb-0">${description}</p>
                        </div>
                    </div>
                </div>
            `;

    let statusBlockHTML = '';

    if (isEditingMode) {
        // Conteúdo no MODO EDIÇÃO (Switches)
        statusBlockHTML = `
                    <h5 class="fw-bold text-custom-slate-800 mb-3">Editar Status de Progresso</h5>
                    <div class="row">
                        ${getStatusSwitch('switch-interviewed', 'Realizou Entrevista', 'PAR-Q e Anamnese preenchidos.', user.interviewed || false)}
                        ${getStatusSwitch('switch-did-first-workout', 'Concluiu Primeiro Treino', 'Marco inicial do acompanhamento.', user.didFirstWorkout || false)}
                        ${getStatusSwitch('switch-is-active', 'Usuário Ativo', 'Pode acessar a academia e agendar treinos.', user.active || false)}
                        ${getStatusSwitch('switch-signed-commitment', 'Assinou Termo de Comp.', 'Requisito legal e de segurança.', user.signedTermOfCommitment || false)} 
                    </div>
                `;
    } else {
        // Conteúdo no MODO VISUALIZAÇÃO (Read-Only)
        statusBlockHTML = `
                    <h5 class="fw-bold text-custom-slate-800">Status de Progresso</h5>
                    <ul class="checkmark-list mt-3">
                        ${getStatusDisplay(user.interviewed, 'Realizou Entrevista')}
                        ${getStatusDisplay(user.didFirstWorkout, 'Concluiu Primeiro Treino')}
                        ${getStatusDisplay(user.scheduledFirstWorkout, 'Agendou Primeiro Treino')}
                        ${getStatusDisplay(user.active, 'Usuário Ativo')}
                        ${getStatusDisplay(user.signedTermOfCommitment, 'Termo de Compromisso Assinado')}
                    </ul>
                `;
    }

    // Formatação da data de nascimento (Age) e a nova formatação de data/hora
    const ageDate = user.age ? new Date(user.age).toLocaleDateString('pt-BR') : 'N/A';
    const firstWorkoutDate = formatDateTime(user.firstWorkoutDate);
    const interviewDateFormatted = formatDateTime(user.interviewDate);

    return `
                <div class="row">
                    <div class="col-md-5">
                        <div class="card shadow-sm p-4 rounded-3 bg-white mb-4">
                            ${statusBlockHTML}
                            
                            ${!isEditingMode ? `
                            <hr class="my-4">
                            <div>
                                <h5 class="fw-bold text-custom-slate-800">Datas Importantes</h5>
                                <p class="text-muted mt-2">
                                    <i class="fa-solid fa-calendar-check me-2"></i>
                                    Entrevista agendada: <span class="fw-semibold">${interviewDateFormatted}</span>
                                </p>
                                <p class="text-muted mt-2">
                                    <i class="fa-solid fa-calendar-alt me-2"></i>
                                    Primeiro treino: <span class="fw-semibold">${firstWorkoutDate}</span>
                                </p>
                            </div>
                            ` : ''}
                        </div>
                    </div>
                    <div class="col-md-7">
                        <div class="card shadow-sm p-4 rounded-3 bg-white mb-4">
                            <h5 class="fw-bold text-custom-slate-800">Informações Pessoais</h5>
                            <div class="mt-3">
                                <p class="mb-2"><i class="fa-solid fa-user me-2"></i> <strong>Nome:</strong> <span>${user.userName || 'N/A'}</span></p>
                                <p class="mb-2"><i class="fa-solid fa-envelope me-2"></i> <strong>E-mail:</strong> <span>${user.email || 'N/A'}</span></p>
                                <p class="mb-2"><i class="fa-solid fa-phone me-2"></i> <strong>Telefone:</strong> <span>${user.phoneNumber || 'N/A'}</span></p>
                                <p class="mb-2"><i class="fa-solid fa-calendar-day me-2"></i> <strong>Data de Nasc:</strong> <span>${ageDate}</span></p>
                                <p class="mb-2"><i class="fa-solid fa-id-card me-2"></i> <strong>Afiliação:</strong> <span>${user.affiliationType || 'N/A'}</span></p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="card shadow-sm mt-4 p-4 rounded-3 bg-white">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h5 class="fw-bold text-custom-slate-800 mb-0">Observações do Professor</h5>
                        <button type="button" class="btn btn-sm btn-outline-primary" id="edit-obs-btn">
                            <i class="fa-solid fa-edit me-1"></i>Editar
                        </button>
                    </div>
                    <div id="obs-view-mode">
                        <p class="text-muted" id="obs-display">${user.userObs || 'Nenhuma observação registrada.'}</p>
                    </div>
                    <div id="obs-edit-mode" class="d-none">
                        <div class="form-floating mb-3">
                            <textarea class="form-control" placeholder="Deixe um comentário aqui" id="user-obs-textarea" style="height: 100px">${user.userObs || ''}</textarea>
                            <label for="user-obs-textarea" class="text-muted">Adicione suas observações...</label>
                        </div>
                        <div class="d-flex gap-2">
                            <button type="button" class="btn btn-sm btn-secondary" id="cancel-obs-btn">Cancelar</button>
                            <button type="button" class="btn btn-sm btn-primary" id="save-obs-btn">
                                <i class="fa-solid fa-save me-1"></i>Salvar
                            </button>
                        </div>
                    </div>
                </div>
            `;
}

// Função principal de renderização das abas (sem alteração)
function renderUserTabs(tab) {
    currentTab = tab;
    const tabs = {
        'info': document.getElementById('tab-info'),
        'parq': document.getElementById('tab-parq'),
        'anamnese': document.getElementById('tab-anamnese')
    };

    for (const key in tabs) {
        if (tabs[key]) tabs[key].classList.remove('active');
    }

    if (tabs[tab]) tabs[tab].classList.add('active');

    // CRÍTICO: Desabilita o botão de editar se não for a aba de Informações Gerais
    toggleEditBtn.disabled = (tab !== 'info');

    let contentHTML = '';

    if (tab === 'info') {
        contentHTML = renderGeneralInfoContent(currentUserData, isEditing);
    } else if (tab === 'parq') {
        contentHTML = `
                    <div class="alert alert-info text-center mt-4">
                        O conteúdo do Questionário PAR-Q será implementado aqui.
                    </div>
                `;
    } else if (tab === 'anamnese') {
        contentHTML = `
                    <div class="alert alert-info text-center mt-4">
                        O conteúdo da Ficha de Anamnese será implementado aqui.
                    </div>
                `;
    }

    contentView.innerHTML = contentHTML;
}

// --- Lógica de Carregamento de Dados ---

async function fetchUserDetails() {
    loadingSpinner.classList.remove('d-none');
    userDetailsContainer.classList.add('d-none');
    statusMessageEl.classList.add('d-none');

    const urlParams = new URLSearchParams(window.location.search);
    const userId = urlParams.get('uid');
    currentUserId = userId;

    if (!userId) {
        showMessage("ID do usuário não encontrado na URL.", true);
        loadingSpinner.classList.add('d-none');
        return;
    }

    try {
        const response = await getAuthTokenAndFetch(`${USERS_API_URL}/${userId}`);

        if (response.status === 404) {
            throw new Error('Usuário não encontrado.');
        }
        if (!response.ok) {
            throw new Error(`Erro ao buscar os dados do usuário. Status: ${response.status}`);
        }

        currentUserData = await response.json();

        // Redefine a URL de retorno para a tela de detalhes com o UID
        document.getElementById('back-to-users-btn').href = `users.html`;

        renderUserTabs(currentTab); // Carrega a aba ativa
        userDetailsContainer.classList.remove('d-none');

    } catch (error) {
        showMessage(`Erro ao carregar: ${error.message}`, true);
    } finally {
        loadingSpinner.classList.add('d-none');
    }
}

// --- Event Listeners ---

document.addEventListener('click', (e) => {
    if (e.target.id === 'tab-info') renderUserTabs('info');
    if (e.target.id === 'tab-parq') renderUserTabs('parq');
    if (e.target.id === 'tab-anamnese') renderUserTabs('anamnese');
});

document.getElementById('view-workout-btn').addEventListener('click', () => {
    if (currentUserId) {
        window.location.href = `user-workout-chart.html?uid=${currentUserId}`;
    } else {
        showMessage("Erro: ID do usuário não encontrado.", true);
    }
});

// --- Lógica de Agendamento de Entrevista ---

let scheduleInterviewModal;

document.getElementById('schedule-interview-btn').addEventListener('click', () => {
    if (!scheduleInterviewModal) {
        scheduleInterviewModal = new bootstrap.Modal(document.getElementById('scheduleInterviewModal'));
    }

    // Pré-preenche com a data atual se houver
    const dateInput = document.getElementById('interview-date-input');
    if (currentUserData && currentUserData.interviewDate) {
        const date = new Date(currentUserData.interviewDate);
        dateInput.value = date.toISOString().slice(0, 16);
    } else {
        dateInput.value = '';
    }

    scheduleInterviewModal.show();
});

document.getElementById('save-interview-btn').addEventListener('click', async () => {
    const dateInput = document.getElementById('interview-date-input');
    const modalStatusMessage = document.getElementById('modal-status-message');

    if (!dateInput.value) {
        modalStatusMessage.textContent = 'Por favor, selecione uma data e hora.';
        modalStatusMessage.classList.remove('d-none', 'alert-success');
        modalStatusMessage.classList.add('alert-danger');
        return;
    }

    const interviewDate = new Date(dateInput.value);

    try {
        const updatedData = {
            ...currentUserData,
            interviewDate: interviewDate.toISOString(),
            scheduledFirstWorkout: true
        };

        delete updatedData.toJSON;

        const response = await getAuthTokenAndFetch(`${USERS_API_URL}/${currentUserId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updatedData)
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Falha ao agendar entrevista. ${errorText}`);
        }

        const updatedUser = await response.json();
        currentUserData = updatedUser;

        modalStatusMessage.textContent = 'Entrevista agendada com sucesso!';
        modalStatusMessage.classList.remove('d-none', 'alert-danger');
        modalStatusMessage.classList.add('alert-success');

        setTimeout(() => {
            scheduleInterviewModal.hide();
            modalStatusMessage.classList.add('d-none');
            renderUserTabs(currentTab);
            showMessage('Entrevista agendada com sucesso!', false);
        }, 1500);

    } catch (error) {
        modalStatusMessage.textContent = `Erro: ${error.message}`;
        modalStatusMessage.classList.remove('d-none', 'alert-success');
        modalStatusMessage.classList.add('alert-danger');
    }
});

// --- Lógica de Agendamento de Primeiro Treino ---

let scheduleWorkoutModal;

document.getElementById('schedule-workout-btn').addEventListener('click', () => {
    if (!scheduleWorkoutModal) {
        scheduleWorkoutModal = new bootstrap.Modal(document.getElementById('scheduleWorkoutModal'));
    }

    // Pré-preenche com a data atual se houver
    const dateInput = document.getElementById('workout-date-input');
    if (currentUserData && currentUserData.firstWorkoutDate) {
        const date = new Date(currentUserData.firstWorkoutDate);
        dateInput.value = date.toISOString().slice(0, 16);
    } else {
        dateInput.value = '';
    }

    scheduleWorkoutModal.show();
});

document.getElementById('save-workout-btn').addEventListener('click', async () => {
    const dateInput = document.getElementById('workout-date-input');
    const modalStatusMessage = document.getElementById('modal-workout-status-message');

    if (!dateInput.value) {
        modalStatusMessage.textContent = 'Por favor, selecione uma data e hora.';
        modalStatusMessage.classList.remove('d-none', 'alert-success');
        modalStatusMessage.classList.add('alert-danger');
        return;
    }

    const workoutDate = new Date(dateInput.value);
    
    try {
        const updatedData = {
            ...currentUserData,
            firstWorkoutDate: workoutDate.toISOString(),
            scheduledFirstWorkout: true
        };        delete updatedData.toJSON;

        const response = await getAuthTokenAndFetch(`${USERS_API_URL}/${currentUserId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updatedData)
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Falha ao agendar primeiro treino. ${errorText}`);
        }

        const updatedUser = await response.json();
        currentUserData = updatedUser;

        modalStatusMessage.textContent = 'Primeiro treino agendado com sucesso!';
        modalStatusMessage.classList.remove('d-none', 'alert-danger');
        modalStatusMessage.classList.add('alert-success');

        setTimeout(() => {
            scheduleWorkoutModal.hide();
            modalStatusMessage.classList.add('d-none');
            renderUserTabs(currentTab);
            showMessage('Primeiro treino agendado com sucesso!', false);
        }, 1500);

    } catch (error) {
        modalStatusMessage.textContent = `Erro: ${error.message}`;
        modalStatusMessage.classList.remove('d-none', 'alert-success');
        modalStatusMessage.classList.add('alert-danger');
    }
});

// --- Lógica de Edição de Observações ---

document.addEventListener('click', (e) => {
    // Botão Editar Observações
    if (e.target.closest('#edit-obs-btn')) {
        document.getElementById('obs-view-mode').classList.add('d-none');
        document.getElementById('obs-edit-mode').classList.remove('d-none');
        document.getElementById('edit-obs-btn').classList.add('d-none');
    }

    // Botão Cancelar Observações
    if (e.target.closest('#cancel-obs-btn')) {
        document.getElementById('obs-edit-mode').classList.add('d-none');
        document.getElementById('obs-view-mode').classList.remove('d-none');
        document.getElementById('edit-obs-btn').classList.remove('d-none');
        // Restaura o valor original
        document.getElementById('user-obs-textarea').value = currentUserData.userObs || '';
    }

    // Botão Salvar Observações
    if (e.target.closest('#save-obs-btn')) {
        saveUserObservations();
    }
});

async function saveUserObservations() {
    const textarea = document.getElementById('user-obs-textarea');
    const newObs = textarea.value.trim();

    try {
        const updatedData = {
            ...currentUserData,
            userObs: newObs
        };

        delete updatedData.toJSON;

        const response = await getAuthTokenAndFetch(`${USERS_API_URL}/${currentUserId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updatedData)
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Falha ao salvar observações. ${errorText}`);
        }

        const updatedUser = await response.json();
        currentUserData = updatedUser;

        // Atualiza a visualização
        document.getElementById('obs-display').textContent = newObs || 'Nenhuma observação registrada.';
        document.getElementById('obs-edit-mode').classList.add('d-none');
        document.getElementById('obs-view-mode').classList.remove('d-none');
        document.getElementById('edit-obs-btn').classList.remove('d-none');

        showMessage('Observações salvas com sucesso!', false);

    } catch (error) {
        showMessage(`Erro ao salvar observações: ${error.message}`, true);
    }
}
