

  // ===== Scroll suave do menu =====
  const navLinks = document.querySelectorAll('nav ul li a');
  navLinks.forEach(link => {
    link.addEventListener('click', function(event) {
      event.preventDefault();
      const targetId = link.getAttribute('href').substring(1);
      const targetSection = document.getElementById(targetId);
      window.scrollTo({
        top: targetSection.offsetTop - 20,
        behavior: 'smooth'
      });
    });
  });
  document.addEventListener('DOMContentLoaded', function() {
    // 1. Seleciona o botão de toggle e a barra de navegação completa
    const navToggle = document.querySelector('.nav-toggle');
    const heroNav = document.querySelector('.hero-nav');

    // 2. Adiciona o listener de evento para o clique
    if (navToggle && heroNav) {
        navToggle.addEventListener('click', function() {
            // Alterna a classe 'open' na barra de navegação. 
            // O CSS usa esta classe para mostrar o menu.
            heroNav.classList.toggle('open');
            
            // Acessibilidade: Alterna o atributo aria-expanded
            const isExpanded = navToggle.getAttribute('aria-expanded') === 'true' || false;
            navToggle.setAttribute('aria-expanded', !isExpanded);
        });
    }
});


