document.addEventListener('DOMContentLoaded', async () => {
  const container = document.querySelector('#beneficiarios .imagens-lado-lado');
  if (!container) return;

  try {
    const response = await fetch('http://localhost:8080/beneficiarios');
    if (!response.ok) throw new Error('Erro ao buscar beneficiários');

    const beneficiarios = await response.json();
    container.innerHTML = '';

    if (beneficiarios.length === 0) {
      container.innerHTML = '<p style="text-align:center;">Nenhum beneficiário cadastrado ainda.</p>';
      return;
    }

    beneficiarios.forEach(b => {
      const item = document.createElement('div');
      item.className = 'item reveal';

      const img = document.createElement('img');
      img.src = (b.fotos && b.fotos.length > 0 && b.fotos[0].url)
        ? b.fotos[0].url
        : '/src/Logo.png';
      img.alt = b.nome || 'Beneficiário';
      img.loading = 'lazy';

      const nome = document.createElement('p');
      nome.textContent = b.nome || 'Sem nome';
      nome.style.textAlign = 'center';
      nome.style.fontWeight = 'bold';
      nome.style.marginTop = '8px';

      item.appendChild(img);
      item.appendChild(nome);
      container.appendChild(item);
    });

  } catch (erro) {
    console.error('Erro ao carregar beneficiários:', erro);
    container.innerHTML = '<p style="color:red; text-align:center;">Erro ao carregar beneficiários.</p>';
  }
});