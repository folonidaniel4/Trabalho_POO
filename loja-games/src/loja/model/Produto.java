package loja.model;

import loja.exception.EstoqueInsuficienteException;

/**
 * Classe abstrata que representa um produto genérico da loja de games.
 * Concentra os atributos e comportamentos comuns a todos os tipos de produto,
 * deixando para as subclasses a responsabilidade de definir como o preço
 * final é calculado e qual é a categoria do produto (polimorfismo).
 */
public abstract class Produto {

    private final String id;
    private final String nome;
    private double preco;
    private int quantidadeEstoque;

    protected Produto(String id, String nome, double preco, int quantidadeEstoque) {
        if (preco < 0) {
            throw new IllegalArgumentException("O preço do produto não pode ser negativo.");
        }
        if (quantidadeEstoque < 0) {
            throw new IllegalArgumentException("A quantidade em estoque não pode ser negativa.");
        }
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    // ---- Métodos abstratos: cada subclasse implementa a própria regra ----

    /** Calcula o preço final de venda, aplicando as regras específicas do tipo de produto. */
    public abstract double calcularPrecoFinal();

    /** Retorna a categoria do produto (usado para exibição e relatórios). */
    public abstract String getCategoria();

    // ---- Comportamentos comuns ----

    public void removerEstoque(int quantidade) throws EstoqueInsuficienteException {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade a remover deve ser maior que zero.");
        }
        if (quantidade > this.quantidadeEstoque) {
            throw new EstoqueInsuficienteException(
                    String.format("Estoque insuficiente para \"%s\": disponível %d, solicitado %d.",
                            nome, quantidadeEstoque, quantidade));
        }
        this.quantidadeEstoque -= quantidade;
    }

    public void adicionarEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade a adicionar deve ser maior que zero.");
        }
        this.quantidadeEstoque += quantidade;
    }

    // ---- Getters ----

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) - Preço base: R$ %.2f | Preço final: R$ %.2f | Estoque: %d",
                id, nome, getCategoria(), preco, calcularPrecoFinal(), quantidadeEstoque);
    }
}
