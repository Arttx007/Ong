const itensCarrinho = document.getElementById('itens-carrinho');
const totalCarrinho = document.getElementById('total');
const btnFinalizar = document.getElementById('finalizar');

const modalPix = document.getElementById('modalPix');
const fecharModal = document.getElementById('fecharModal');
const valorPedido = document.getElementById('valor-pedido');
const btnConfirmar = document.getElementById('confirmar-pagamento');
const mensagemConfirmacao = document.getElementById('mensagem-confirmacao');

let carrinhoProdutos = [];

// Adiciona produtos ao carrinho ao clicar em "Comprar"
document.querySelectorAll('.btn-primary').forEach(botao => {
  botao.addEventListener('click', e => {
    e.preventDefault();
    const card = botao.closest('.card');
    const nome = card.querySelector('h3').textContent;
    const preco = parseFloat(card.getAttribute('data-preco') || 10); // Valor padrão 10 se não definido

    carrinhoProdutos.push({ nome, preco });
    atualizarCarrinho();
  });
});

function atualizarCarrinho() {
  itensCarrinho.innerHTML = '';
  let total = 0;
  carrinhoProdutos.forEach(p => {
    const li = document.createElement('li');
    li.textContent = `${p.nome} - R$ ${p.preco},00`;
    itensCarrinho.appendChild(li);
    total += p.preco;
  });
  totalCarrinho.textContent = `Total: R$ ${total},00`;
}

// Ao clicar em finalizar compra
btnFinalizar.addEventListener('click', () => {
  if(carrinhoProdutos.length === 0) return;

  let total = carrinhoProdutos.reduce((acc, p) => acc + p.preco, 0);
  valorPedido.textContent = `Valor Total: R$ ${total},00`;
  mensagemConfirmacao.style.display = 'none';
  modalPix.style.display = 'block';
});

// Confirmar pagamento
btnConfirmar.addEventListener('click', () => {
  mensagemConfirmacao.style.display = 'block';
  carrinhoProdutos = [];
  atualizarCarrinho();
});

// Fechar modal
fecharModal.addEventListener('click', () => modalPix.style.display = 'none');
window.addEventListener('click', e => { 
  if(e.target === modalPix) modalPix.style.display = 'none'; 
});