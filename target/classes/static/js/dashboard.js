// Modal Beneficiário
const abrirModal = document.getElementById('abrirModal');
const fecharModal = document.getElementById('fecharModal');
const modal = document.getElementById('modalBeneficiario');
const form = document.getElementById('formBeneficiarioModal');
const mensagem = document.getElementById('mensagemBeneficiarioModal');

if(abrirModal && modal && form) {

    // Abrir modal
    abrirModal.addEventListener('click', () => {
        modal.classList.remove('hidden');
        modal.classList.add('flex');
    });

    // Fechar modal
    fecharModal.addEventListener('click', () => {
        modal.classList.add('hidden');
        modal.classList.remove('flex');
        form.reset();
        mensagem.innerText = '';
    });

    // Fechar clicando fora do modal
    modal.addEventListener('click', (e) => {
        if (e.target === modal) {
            modal.classList.add('hidden');
            modal.classList.remove('flex');
            form.reset();
            mensagem.innerText = '';
        }
    });

    // Enviar formulário
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = new FormData(form);

        try {
            const response = await fetch('http://localhost:8080/beneficiarios', {
                method: 'POST',
                headers: {
                    'usuarioId': '1' // substitua pelo ID do usuário logado
                },
                body: formData
            });

            if (!response.ok) {
                const text = await response.text();
                mensagem.innerText = 'Erro: ' + text;
                return;
            }

            const result = await response.json();
            mensagem.innerText = `Beneficiário "${result.nome}" adicionado com sucesso!`;
            form.reset();
        } catch (error) {
            mensagem.innerText = 'Erro ao adicionar beneficiário.';
            console.error(error);
        }
    });

}
