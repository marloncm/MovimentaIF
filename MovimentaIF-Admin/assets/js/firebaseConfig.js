import { initializeApp } from "https://www.gstatic.com/firebasejs/11.6.1/firebase-app.js";
import { getAuth, onAuthStateChanged, signOut } from "https://www.gstatic.com/firebasejs/11.6.1/firebase-auth.js";
import { fetchWithTimeout, logger } from "./security-config.js";

const firebaseConfig = {
    apiKey: "AIzaSyAnET6gJ175qHFbHcKm40tynj7s9x4sXqU",
    authDomain: "movimentaif.firebaseapp.com",
    projectId: "movimentaif",
    storageBucket: "movimentaif.firebasestorage.app",
    messagingSenderId: "705983497984",
    appId: "1:705983497984:web:f16672db437ce21aa2d5e5",
    measurementId: "G-5K2CYJ742W"
};
const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);

// ✅ URL de Produção com HTTPS
export const API_BASE_URL = 'https://movimentaif-api-7895a5f0638f.herokuapp.com/api';


export async function getAuthTokenAndFetch(url, options = {}) {
    const user = auth.currentUser;
    if (!user) {
        logger.warn('Usuário não autenticado. Redirecionando...');
        window.location.replace('index.html');
        return Promise.reject(new Error("No user authenticated."));
    }
    const token = await user.getIdToken();
    const headers = { ...options.headers, 'Authorization': `Bearer ${token}` };
    return fetchWithTimeout(url, { ...options, headers });
}




