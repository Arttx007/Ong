document.addEventListener("DOMContentLoaded", () => {
  const reveals = document.querySelectorAll(".reveal");

  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add("active");
        // Se quiser que só ative uma vez e nunca volte:
        // observer.unobserve(entry.target);
      }
    });
  }, { threshold: 0.2 }); // 20% visível na tela já ativa

  reveals.forEach(reveal => {
    observer.observe(reveal);
  });
});