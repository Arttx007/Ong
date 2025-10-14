const form = document.querySelector("#login-form");
const msg = document.querySelector("#msg");

form.addEventListener("submit", async (e) => {
  e.preventDefault();

  const email = document.querySelector("#email").value.trim();
  const senha = document.querySelector("#senha").value.trim();

  if (!email || !senha) {
    msg.textContent = "Preencha todos os campos!";
    msg.style.color = "orange";
    return;
  }

  try {
    const resp = await fetch("http://localhost:8080/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, senha })
    });

    if (!resp.ok) {
      throw new Error("Usuário ou senha inválidos!");
    }

    const data = await resp.json();
    localStorage.setItem("token", data.token);
    window.location.href = "depoimentos.html";

  } catch (error) {
    msg.textContent = error.message;
    msg.style.color = "red";
  }
});


// MODAL - ESQUECI A SENHA
const forgotBtn = document.querySelector("#forgot-btn");
const modal = document.querySelector("#forgot-modal");
const closeBtn = document.querySelector(".close-modal");
const sendBtn = document.querySelector("#send-recover");
const recoverEmail = document.querySelector("#recover-email");
const recoverMsg = document.querySelector("#recover-msg");

// Abre o modal
forgotBtn.addEventListener("click", (e) => {
  e.preventDefault();
  modal.classList.add("active");
});

// Fecha o modal
closeBtn.addEventListener("click", () => {
  modal.classList.remove("active");
  recoverMsg.textContent = "";
  recoverEmail.value = "";
});

// Envia pedido de recuperação (integração será feita pelo back-nd)
sendBtn.addEventListener("click", async () => {
  const email = recoverEmail.value.trim();

  if (!email) {
    recoverMsg.textContent = "Digite seu e-mail!";
    recoverMsg.style.color = "orange";
    return;
  }

try {
  // Temporário: apenas limpa o campo e fecha o modal
  recoverEmail.value = "";
  recoverMsg.textContent = "";
  modal.classList.remove("active");

} catch (error) {
  recoverMsg.textContent = "Erro ao enviar link!";
  recoverMsg.style.color = "red";
}
});


// TOAST
function showToast(text) {
  const container = document.getElementById("toast-container");
  const toast = document.createElement("div");
  toast.className = "toast";
  toast.textContent = text;
  container.appendChild(toast);

  setTimeout(() => {
    toast.style.animation = "fadeSlideOut 0.5s forwards";
    setTimeout(() => toast.remove(), 500);
  }, 2500);
}