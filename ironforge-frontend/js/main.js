// ===== NAVBAR SCROLL =====
const navbar = document.getElementById('navbar');
const hamburger = document.getElementById('hamburger');

window.addEventListener('scroll', () => {
    if (window.scrollY > 50) {
        navbar.classList.add('scrolled');
    } else {
        navbar.classList.remove('scrolled');
    }
});

// ===== HAMBURGER MENU =====
hamburger.addEventListener('click', () => {
    const navLinks = document.querySelector('.nav-links');
    const navButtons = document.querySelector('.nav-buttons');
    navLinks.style.display =
        navLinks.style.display === 'flex' ? 'none' : 'flex';
    navButtons.style.display =
        navButtons.style.display === 'flex' ? 'none' : 'flex';
});

// ===== COMPTEURS ANIMÉS =====
const animateCounters = () => {
    const counters = document.querySelectorAll('.stat-number');

    counters.forEach(counter => {
        const target = parseInt(counter.getAttribute('data-target'));
        const duration = 2000;
        const step = target / (duration / 16);
        let current = 0;

        const updateCounter = () => {
            current += step;
            if (current < target) {
                counter.textContent = Math.floor(current);
                requestAnimationFrame(updateCounter);
            } else {
                counter.textContent = target;
            }
        };
        updateCounter();
    });
};

// ===== INTERSECTION OBSERVER =====
const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            entry.target.classList.add('visible');

            if (entry.target.classList.contains('hero-stats')) {
                animateCounters();
            }
        }
    });
}, { threshold: 0.1 });

// Observer les éléments
document.querySelectorAll('.service-card, .programme-card, .coach-card, .produit-card, .plan-card, .hero-stats').forEach(el => {
    observer.observe(el);
});

// ===== SMOOTH SCROLL =====
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', (e) => {
        e.preventDefault();
        const target = document.querySelector(anchor.getAttribute('href'));
        if (target) {
            target.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }
    });
});

// ===== BOUTON PANIER =====
document.querySelectorAll('.btn-panier').forEach(btn => {
    btn.addEventListener('click', () => {
        const token = localStorage.getItem('token');
        if (!token) {
            window.location.href = 'login.html';
            return;
        }
        btn.innerHTML = '<i class="fas fa-check"></i> Ajouté !';
        btn.style.background = '#00C853';
        btn.style.borderColor = '#00C853';
        btn.style.color = 'white';

        setTimeout(() => {
            btn.innerHTML = '<i class="fas fa-shopping-cart"></i> Ajouter';
            btn.style.background = '';
            btn.style.borderColor = '';
            btn.style.color = '';
        }, 2000);
    });
});

// ===== FADE IN ANIMATION =====
const style = document.createElement('style');
style.textContent = `
    .service-card, .programme-card,
    .coach-card, .produit-card, .plan-card {
        opacity: 0;
        transform: translateY(30px);
        transition: opacity 0.6s ease, transform 0.6s ease;
    }
    .service-card.visible, .programme-card.visible,
    .coach-card.visible, .produit-card.visible,
    .plan-card.visible {
        opacity: 1;
        transform: translateY(0);
    }
`;
document.head.appendChild(style);

// ===== ACTIVE NAV LINK =====
window.addEventListener('scroll', () => {
    const sections = document.querySelectorAll('section[id]');
    const navLinks = document.querySelectorAll('.nav-links a');

    sections.forEach(section => {
        const sectionTop = section.offsetTop - 100;
        const sectionHeight = section.clientHeight;

        if (window.scrollY >= sectionTop &&
            window.scrollY < sectionTop + sectionHeight) {
            navLinks.forEach(link => {
                link.classList.remove('active');
                if (link.getAttribute('href') === `#${section.id}`) {
                    link.classList.add('active');
                }
            });
        }
    });
});

console.log('🔥 IRONFORGE Frontend chargé !');