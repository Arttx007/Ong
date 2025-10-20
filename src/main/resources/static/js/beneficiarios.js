document.addEventListener('DOMContentLoaded', () => {
  const container = document.querySelector('#beneficiarios .imagens-lado-lado');
  if (!container) return;

  // 1️⃣ Cria os botões CRUD
  const botoesContainer = document.createElement('div');
  botoesContainer.classList.add('botoes-crud');

  const btnAdicionar = document.createElement('button');
  btnAdicionar.textContent = 'Adicionar Beneficiário';
  btnAdicionar.classList.add('btn', 'btn-primary');

  const btnEditar = document.createElement('button');
  btnEditar.textContent = 'Editar Beneficiário';
  btnEditar.classList.add('btn', 'btn-outline');

  const btnExcluir = document.createElement('button');
  btnExcluir.textContent = 'Excluir Beneficiário';
  btnExcluir.classList.add('btn', 'btn-outline');

  botoesContainer.append(btnAdicionar, btnEditar, btnExcluir);

  // 2️⃣ Coloca acima das imagens
  container.parentElement.prepend(botoesContainer);
});