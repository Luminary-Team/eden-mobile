package com.eden.model;

public class Produto {
    int imagem;
    String nome, descricao, tipoEntrega;
    double valor;
    boolean adcCarrinho;

    public Produto() {
    }

    public Produto(int imagem, String nome, String descricao, double valor, boolean adcCarrinho) {
        this.imagem = imagem;
        this.nome = nome;
        this.descricao = descricao;
        this.valor = valor;
        this.adcCarrinho = adcCarrinho;
    }

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public String getTipoEntrega() {
        return tipoEntrega;
    }

    public void setTipoEntrega(String tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
    public boolean isAdcCarrinho() {
        return adcCarrinho;
    }

    public void setAdcCarrinho(boolean adcCarrinho) {
        this.adcCarrinho = adcCarrinho;
    }
}
