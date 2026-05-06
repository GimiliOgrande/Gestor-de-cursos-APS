package br.ufpb.dcx.aps.model;

public class Usuario {

    private int id;
    private String nome;
    private String email;
    private String senha;
    private CategoriaUsuario categoria;

    public Usuario(int id, String nome, String email, String senha, CategoriaUsuario categoria) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public CategoriaUsuario getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaUsuario categoria) {
        this.categoria = categoria;
    }
}
