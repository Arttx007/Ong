const form = document.querySelector("#login-form");
const msg = document.querySelector("#msg");

form.addEventListener("submit", async (e) => {
 e.preventDefault();

// Ajuste para usar 'username' e 'password', que são os nomes esperados pelo Spring Security
// Lendo dos campos #email (como username) e #senha (como password)
const usernameInput = document.querySelector("#email").value.trim(); 
const passwordInput = document.querySelector("#senha").value.trim();

 if (!usernameInput || !passwordInput) {
    msg.textContent = "Preencha todos os campos!";
    msg.style.color = "orange";
    return;
  }

  // CRUCIAL: Formata os dados no padrão "application/x-www-form-urlencoded"
  // As chaves *devem* ser 'username' e 'password' para o Spring Security.
  // ... no seu login.js
const data = `username=${encodeURIComponent(usuario)}&password=${encodeURIComponent(senha)}`; 
// ...

  try {
    // Enviando para a rota padrão de login do Spring Security
    const resp = await fetch("/login", { 
      method: "POST",
      // CORREÇÃO: Mudar Content-Type para o formato de formulário
      headers: { "Content-Type": "application/x-www-form-urlencoded" }, 
      // CORREÇÃO: Enviar a string URL-encoded
      body: data 
    });

    if (resp.ok) {
        // Sucesso! Redireciona para a página principal (ou protegida)
        // A sessão de login já foi estabelecida pelo Spring.
        window.location.href = "/"; 
        // Se você estava esperando JSON com token, essa lógica deve ser revisada
        // para se adequar ao fluxo de autenticação via sessão do Spring Security.
    } else if (resp.status === 401) {
        // Falha de credenciais (401 retornado pelo failureHandler)
        throw new Error("Usuário ou senha inválidos!");
    } else {
        // Outros erros HTTP (403, 500, etc.)
        throw new Error(`Erro ao tentar login. Status: ${resp.status}`);
    }

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
