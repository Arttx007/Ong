const acoesPrivadas = document.getElementById('acoes-privadas');

function atualizarVisibilidadeLogin() {
  const logado = !!localStorage.getItem('usuario_logado');
  acoesPrivadas.style.display = logado ? 'block' : 'none';
}

// Logout
function logout() {
  localStorage.removeItem('usuario_logado');
  atualizarVisibilidadeLogin();
}

atualizarVisibilidadeLogin();