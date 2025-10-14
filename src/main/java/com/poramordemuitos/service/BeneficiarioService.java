package com.poramordemuitos.service;

import com.poramordemuitos.model.Beneficiario;
import com.poramordemuitos.repository.BeneficiarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BeneficiarioService {
    private final BeneficiarioRepository repo;
    public BeneficiarioService(BeneficiarioRepository repo) { this.repo = repo; }

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
}