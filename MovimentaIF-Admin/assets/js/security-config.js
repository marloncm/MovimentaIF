/**
 * 🔐 Configuração de Segurança - MovimentaIF Admin
 * 
 * Este arquivo centraliza configurações de segurança e ambiente
 * para o painel administrativo web.
 */

// Detecta se está em produção (GitHub Pages, Netlify, Vercel, etc.)
const isProduction = () => {
    return window.location.hostname !== 'localhost'
        && window.location.hostname !== '127.0.0.1'
        && !window.location.hostname.includes('192.168')
        && !window.location.hostname.includes('10.0.2.2');
};

// Logger seguro - remove logs em produção
export const logger = {
    log: (...args) => {
        if (!isProduction()) {
            console.log(...args);
        }
    },
    error: (...args) => {
        if (!isProduction()) {
            console.error(...args);
        } else {
            // Em produção, apenas registra erros críticos
            console.error('Erro detectado. Consulte o administrador.');
        }
    },
    warn: (...args) => {
        if (!isProduction()) {
            console.warn(...args);
        }
    },
    info: (...args) => {
        if (!isProduction()) {
            console.info(...args);
        }
    }
};

// Configuração de ambiente
export const config = {
    isProduction: isProduction(),

    // Headers de segurança recomendados
    securityHeaders: {
        'X-Content-Type-Options': 'nosniff',
        'X-Frame-Options': 'DENY',
        'X-XSS-Protection': '1; mode=block',
        'Referrer-Policy': 'strict-origin-when-cross-origin'
    },

    // Timeout para requisições (30 segundos)
    requestTimeout: 30000,

    // Validação de dados
    validation: {
        maxNameLength: 100,
        maxDescriptionLength: 500,
        maxEmailLength: 255
    }
};

// Função helper para fazer fetch com timeout
export async function fetchWithTimeout(url, options = {}, timeout = config.requestTimeout) {
    const controller = new AbortController();
    const id = setTimeout(() => controller.abort(), timeout);

    try {
        const response = await fetch(url, {
            ...options,
            signal: controller.signal
        });
        clearTimeout(id);
        return response;
    } catch (error) {
        clearTimeout(id);
        if (error.name === 'AbortError') {
            throw new Error('Requisição excedeu o tempo limite');
        }
        throw error;
    }
}

// Sanitização básica de inputs (previne XSS)
export function sanitizeInput(input) {
    if (typeof input !== 'string') return input;

    const div = document.createElement('div');
    div.textContent = input;
    return div.innerHTML;
}

// Validação de email
export function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

// Validação de URL (para links de vídeo)
export function isValidURL(url) {
    try {
        new URL(url);
        return true;
    } catch {
        return false;
    }
}

// Proteção contra navegação não autenticada
export function requireAuth(auth) {
    if (!auth.currentUser) {
        logger.warn('Usuário não autenticado. Redirecionando...');
        window.location.replace('index.html');
        return false;
    }
    return true;
}

// Desabilita console.log em produção globalmente
if (isProduction()) {
    console.log = () => { };
    console.info = () => { };
    console.warn = () => { };
    // Mantém console.error mas sanitiza a mensagem
    const originalError = console.error;
    console.error = (...args) => {
        originalError('Erro detectado. Consulte o administrador do sistema.');
    };
}

logger.info('🔐 Modo:', isProduction() ? 'PRODUÇÃO' : 'DESENVOLVIMENTO');
