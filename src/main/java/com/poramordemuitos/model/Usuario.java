package com.poramordemuitos.model;

import jakarta.persistence.*;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    private String senha;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "permissao_id", referencedColumnName = "id")
    private Permissao permissao;

    public Usuario() {}

    public Usuario(String nome, String email, String senha, Permissao permissao) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.permissao = permissao;
    }

    public Long getId() { return id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public Permissao getPermissao() { return permissao; }
    public void setPermissao(Permissao permissao) { this.permissao = permissao; }

    public boolean podeAdicionar() { return permissao != null && permissao.isPodeAdicionar(); }
    public boolean podeEditar() { return permissao != null && permissao.isPodeEditar(); }
    public boolean podeExcluir() { return permissao != null && permissao.isPodeExcluir(); }
    public boolean podeGerenciarUsuarios() { return permissao != null && permissao.isPodeGerenciarUsuarios(); }
}