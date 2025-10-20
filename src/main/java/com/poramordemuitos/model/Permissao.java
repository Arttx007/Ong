package com.poramordemuitos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "permissoes")
public class Permissao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean podeAdicionar;
    private boolean podeEditar;
    private boolean podeExcluir;
    private boolean podeGerenciarUsuarios;

    public Permissao() {}

    public Permissao(boolean podeAdicionar, boolean podeEditar, boolean podeExcluir, boolean podeGerenciarUsuarios) {
        this.podeAdicionar = podeAdicionar;
        this.podeEditar = podeEditar;
        this.podeExcluir = podeExcluir;
        this.podeGerenciarUsuarios = podeGerenciarUsuarios;
    }

    public Long getId() { return id; }

    public boolean isPodeAdicionar() { return podeAdicionar; }
    public void setPodeAdicionar(boolean podeAdicionar) { this.podeAdicionar = podeAdicionar; }

    public boolean isPodeEditar() { return podeEditar; }
    public void setPodeEditar(boolean podeEditar) { this.podeEditar = podeEditar; }

    public boolean isPodeExcluir() { return podeExcluir; }
    public void setPodeExcluir(boolean podeExcluir) { this.podeExcluir = podeExcluir; }

    public boolean isPodeGerenciarUsuarios() { return podeGerenciarUsuarios; }
    public void setPodeGerenciarUsuarios(boolean podeGerenciarUsuarios) { this.podeGerenciarUsuarios = podeGerenciarUsuarios; }

    public String getNome() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNome'");
    }
}