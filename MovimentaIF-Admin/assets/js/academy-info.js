import { auth, API_BASE_URL, getAuthTokenAndFetch, onAuthStateChanged, signOut } from "./firebaseConfig.js";

const ACADEMY_INFO_API_URL = `${API_BASE_URL}/academy-info`;
const loadingSpinner = document.getElementById('loading-spinner');
const academyInfoContainer = document.getElementById('academy-info-container');
const statusMessageEl = document.getElementById('status-message');
const form = document.getElementById('academy-info-form');

let currentAcademyInfo = null;
let originalFormData = null;

// --- Funções Auxiliares ---
function showMessage(message, isError = true) {
    statusMessageEl.textContent = message;
    statusMessageEl.classList.remove('d-none', 'alert-success', 'alert-danger');
    statusMessageEl.classList.add(isError ? 'alert-danger' : 'alert-success');
    setTimeout(() => statusMessageEl.classList.add('d-none'), 4000);
}

function formatDateForInput(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toISOString().split('T')[0];
}

function formatTimeForInput(timeString) {
    if (!timeString) return '';
    // Se vier no formato HH:mm:ss, pega apenas HH:mm
    return timeString.substring(0, 5);
}

function formatDateForDisplay(dateString) {
    if (!dateString) return '-';
    const date = new Date(dateString);
    return date.toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit', year: 'numeric' });
}

function updatePreview() {
    const startDate = document.getElementById('start-date').value;
    const endDate = document.getElementById('end-date').value;
    const openHour = document.getElementById('open-hour').value;
    const closeHour = document.getElementById('close-hour').value;
    const additionalInfo = document.getElementById('additional-info').value;

    document.getElementById('preview-period').textContent = 
        startDate && endDate 
            ? `${formatDateForDisplay(startDate)} até ${formatDateForDisplay(endDate)}`
            : '-';

    document.getElementById('preview-hours').textContent = 
        openHour && closeHour 
            ? `${openHour} às ${closeHour}`
            : '-';

    document.getElementById('preview-info').textContent = additionalInfo || '-';
}

// --- Lógica de Autenticação ---
onAuthStateChanged(auth, user => {
    if (user) {
        document.getElementById('user-email').textContent = user.email;
        fetchAcademyInfo();
    } else {
        window.location.replace('index.html');
    }
});

document.getElementById('logout-btn').addEventListener('click', () => {
    signOut(auth).then(() => {
        window.location.replace('index.html');
    });
});

// --- Lógica de Carregamento de Dados ---
async function fetchAcademyInfo() {
    loadingSpinner.classList.remove('d-none');
    academyInfoContainer.classList.add('d-none');
    statusMessageEl.classList.add('d-none');

    try {
        const response = await getAuthTokenAndFetch(ACADEMY_INFO_API_URL);

        if (response.status === 404) {
            // Se não existir, cria um registro padrão
            showMessage('Nenhuma informação encontrada. Configure os dados abaixo.', false);
            academyInfoContainer.classList.remove('d-none');
            return;
        }

        if (!response.ok) {
            throw new Error(`Erro ao buscar informações. Status: ${response.status}`);
        }

        const text = await response.text();
        if (!text) {
            showMessage('Nenhuma informação encontrada. Configure os dados abaixo.', false);
            academyInfoContainer.classList.remove('d-none');
            return;
        }

        currentAcademyInfo = JSON.parse(text);
        populateForm(currentAcademyInfo);
        academyInfoContainer.classList.remove('d-none');

    } catch (error) {
        showMessage(`Erro ao carregar: ${error.message}`, true);
        academyInfoContainer.classList.remove('d-none');
    } finally {
        loadingSpinner.classList.add('d-none');
    }
}

function populateForm(data) {
    document.getElementById('start-date').value = formatDateForInput(data.startDate);
    document.getElementById('end-date').value = formatDateForInput(data.endDate);
    document.getElementById('open-hour').value = formatTimeForInput(data.openHour);
    document.getElementById('close-hour').value = formatTimeForInput(data.closeHour);
    document.getElementById('additional-info').value = data.additionalInfo || '';
    
    // Salva os dados originais para comparação
    originalFormData = {
        startDate: formatDateForInput(data.startDate),
        endDate: formatDateForInput(data.endDate),
        openHour: formatTimeForInput(data.openHour),
        closeHour: formatTimeForInput(data.closeHour),
        additionalInfo: data.additionalInfo || ''
    };
    
    updatePreview();
}

// --- Event Listeners ---

// Atualiza preview em tempo real
document.getElementById('start-date').addEventListener('change', updatePreview);
document.getElementById('end-date').addEventListener('change', updatePreview);
document.getElementById('open-hour').addEventListener('change', updatePreview);
document.getElementById('close-hour').addEventListener('change', updatePreview);
document.getElementById('additional-info').addEventListener('input', updatePreview);

// Botão Cancelar
document.getElementById('cancel-btn').addEventListener('click', () => {
    if (originalFormData) {
        document.getElementById('start-date').value = originalFormData.startDate;
        document.getElementById('end-date').value = originalFormData.endDate;
        document.getElementById('open-hour').value = originalFormData.openHour;
        document.getElementById('close-hour').value = originalFormData.closeHour;
        document.getElementById('additional-info').value = originalFormData.additionalInfo;
        updatePreview();
        showMessage('Alterações canceladas.', false);
    }
});

// Submit do Formulário
form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const formData = {
        startDate: document.getElementById('start-date').value,
        endDate: document.getElementById('end-date').value,
        openHour: document.getElementById('open-hour').value + ':00', // Adiciona segundos
        closeHour: document.getElementById('close-hour').value + ':00',
        additionalInfo: document.getElementById('additional-info').value
    };

    try {
        const method = currentAcademyInfo ? 'PUT' : 'POST';
        const url = currentAcademyInfo 
            ? `${ACADEMY_INFO_API_URL}/${currentAcademyInfo.id}` 
            : ACADEMY_INFO_API_URL;

        const response = await getAuthTokenAndFetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(formData)
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Falha ao salvar. ${errorText}`);
        }

        const savedData = await response.json();
        currentAcademyInfo = savedData;
        originalFormData = {
            startDate: formatDateForInput(savedData.startDate),
            endDate: formatDateForInput(savedData.endDate),
            openHour: formatTimeForInput(savedData.openHour),
            closeHour: formatTimeForInput(savedData.closeHour),
            additionalInfo: savedData.additionalInfo || ''
        };

        showMessage('Informações salvas com sucesso!', false);

    } catch (error) {
        showMessage(`Erro ao salvar: ${error.message}`, true);
    }
});
