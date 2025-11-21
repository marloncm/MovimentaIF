// Mobile Menu Handler
document.addEventListener('DOMContentLoaded', function() {
    // Criar botão de menu mobile se não existir
    if (!document.querySelector('.mobile-menu-toggle')) {
        const menuBtn = document.createElement('button');
        menuBtn.className = 'mobile-menu-toggle';
        menuBtn.innerHTML = '<i class="fas fa-bars"></i>';
        menuBtn.setAttribute('aria-label', 'Abrir menu');
        document.body.appendChild(menuBtn);
    }

    // Criar overlay se não existir
    if (!document.querySelector('.sidebar-overlay')) {
        const overlay = document.createElement('div');
        overlay.className = 'sidebar-overlay';
        document.body.appendChild(overlay);
    }

    const menuToggle = document.querySelector('.mobile-menu-toggle');
    const sidebar = document.querySelector('aside');
    const overlay = document.querySelector('.sidebar-overlay');
    const mainContent = document.querySelector('.col-md-10, .main-content');

    if (!sidebar) return;

    // Adicionar classes necessárias
    sidebar.classList.add('sidebar');
    if (mainContent) mainContent.classList.add('main-content');

    // Toggle menu
    function toggleMenu() {
        sidebar.classList.toggle('show');
        overlay.classList.toggle('show');
        
        // Alterar ícone
        const icon = menuToggle.querySelector('i');
        if (sidebar.classList.contains('show')) {
            icon.className = 'fas fa-times';
            menuToggle.setAttribute('aria-label', 'Fechar menu');
            document.body.style.overflow = 'hidden';
        } else {
            icon.className = 'fas fa-bars';
            menuToggle.setAttribute('aria-label', 'Abrir menu');
            document.body.style.overflow = '';
        }
    }

    // Fechar menu
    function closeMenu() {
        sidebar.classList.remove('show');
        overlay.classList.remove('show');
        const icon = menuToggle.querySelector('i');
        icon.className = 'fas fa-bars';
        menuToggle.setAttribute('aria-label', 'Abrir menu');
        document.body.style.overflow = '';
    }

    // Event listeners
    if (menuToggle) {
        menuToggle.addEventListener('click', toggleMenu);
    }

    if (overlay) {
        overlay.addEventListener('click', closeMenu);
    }

    // Fechar menu ao clicar em um link (em mobile)
    const navLinks = sidebar.querySelectorAll('.nav-link');
    navLinks.forEach(link => {
        link.addEventListener('click', function() {
            if (window.innerWidth <= 992) {
                closeMenu();
            }
        });
    });

    // Fechar menu ao redimensionar para desktop
    window.addEventListener('resize', function() {
        if (window.innerWidth > 992) {
            closeMenu();
        }
    });

    // Prevenir scroll do body quando menu está aberto
    sidebar.addEventListener('touchmove', function(e) {
        if (sidebar.classList.contains('show')) {
            e.stopPropagation();
        }
    }, { passive: false });
});
