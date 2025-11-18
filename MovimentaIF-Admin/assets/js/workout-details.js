import { auth, API_BASE_URL, getAuthTokenAndFetch, onAuthStateChanged, signOut } from "./firebaseConfig.js";

const workoutContent = document.getElementById('workout-content');
const WORKOUTS_API_URL = `${API_BASE_URL}/workouts`;

onAuthStateChanged(auth, user => {
    if (user) {
        document.getElementById('user-email').textContent = user.email;
        const urlParams = new URLSearchParams(window.location.search);
        const workoutId = urlParams.get('id');
        if (workoutId) {
            fetchWorkoutDetails(workoutId);
        } else {
            workoutContent.innerHTML = `<div class="alert alert-warning">Treino não encontrado.</div>`;
        }
    } else {
        window.location.replace('index.html');
    }
});

document.getElementById('logout-btn').addEventListener('click', () => {
    signOut(auth).then(() => {
        window.location.replace('index.html');
    });
});

document.getElementById('back-btn').addEventListener('click', () => {
    window.location.href = 'workouts.html';
});

async function fetchWorkoutDetails(id) {
    try {
        // O spinner é exibido por padrão no HTML.
        const response = await getAuthTokenAndFetch(`${WORKOUTS_API_URL}/${id}`);
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Erro ao buscar detalhes do treino.');
        }
        const workout = await response.json();
        renderWorkoutDetails(workout);
    } catch (error) {
        workoutContent.innerHTML = `<div class="alert alert-danger text-center mt-4" role="alert">Erro ao carregar os detalhes do treino: ${error.message}</div>`;
    }
}

function getYouTubeVideoId(url) {
    // Regex atualizado para capturar links normais, curtos (youtu.be) e Shorts
    const regex = /(?:youtube\.com\/(?:[^\/]+\/.+\/|(?:v|e(?:mbed)?)\/|.*[?&]v=)|youtu\.be\/|youtube\.com\/shorts\/)([^"&?\/\s]{11})/;
    const match = url.match(regex);
    return match ? match[1] : null;
}

function renderWorkoutDetails(workout) {
    const videoId = getYouTubeVideoId(workout.workoutVideoLink);
    const videoEmbed = videoId ? `
                <div class="ratio ratio-16x9 rounded-3 shadow mb-4 bg-dark">
                    <iframe src="https://www.youtube.com/embed/${videoId}" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
                </div>
            ` : `<div class="alert alert-warning text-center">Este treino não possui um link de vídeo válido.</div>`;

    workoutContent.innerHTML = `
                <div class="card shadow-sm p-4 mb-4 bg-custom-white">
                    <h3 class="card-title fw-bold text-custom-slate-800">${workout.workoutName}</h3>
                    <p class="card-text text-muted">${workout.workoutDescription}</p>
                </div>
                ${videoEmbed}
            `;
}