package com.poramordemuitos.service;

import com.poramordemuitos.model.Beneficiario;
import com.poramordemuitos.repository.BeneficiarioRepository;
import com.poramordemuitos.repository.FotoRepository; // importe seu repositório de fotos
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BeneficiarioService {
    private final BeneficiarioRepository repo;
    private final FotoRepository fotoRepo;

    public BeneficiarioService(BeneficiarioRepository repo, FotoRepository fotoRepo) { 
        this.repo = repo; 
        this.fotoRepo = fotoRepo;
    }

    public List<Beneficiario> listar() { return repo.findAll(); }

    public Beneficiario salvar(Beneficiario b) {
        b.setDataAtualizacao(LocalDate.now());
        return repo.save(b);
    }

    public Beneficiario buscar(Long id) { return repo.findById(id).orElseThrow(); }

    public void deletar(Long id) { repo.deleteById(id); }

    public Beneficiario atualizar(Long id, Beneficiario dados) {
        Beneficiario b = repo.findById(id).orElseThrow();
        b.setNome(dados.getNome());
        b.setIdade(dados.getIdade());
        b.setDescricao(dados.getDescricao());
        b.setSituacaoAtual(dados.getSituacaoAtual());
        b.setNecessidadeAtendida(dados.getNecessidadeAtendida());
        b.setDataAtualizacao(LocalDate.now());
        return repo.save(b);
    }

    @Transactional
    public void apagarFotosDoBeneficiario(Long beneficiarioId) {
        Beneficiario b = repo.findById(beneficiarioId).orElseThrow();

        // Apaga as fotos do banco
        fotoRepo.deleteAll(b.getFotos());

        // Limpa a lista no objeto
        b.getFotos().clear();

        // Salva alterações
        repo.save(b);
    }
}
