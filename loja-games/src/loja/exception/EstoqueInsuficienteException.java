package loja.exception;

/**
 * Exceção checked lançada quando se tenta remover do estoque uma quantidade
 * maior do que a disponível. É "checked" porque representa uma condição de
 * negócio que quem chama o método DEVE tratar (ex: avisar o cliente, sugerir
 * quantidade menor), e não um erro de programação.
 */
public class EstoqueInsuficienteException extends Exception {

    public EstoqueInsuficienteException(String mensagem) {
        super(mensagem);
    }
}
