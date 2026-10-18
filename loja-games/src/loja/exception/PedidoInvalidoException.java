package loja.exception;

/**
 * Exceção unchecked (RuntimeException) lançada quando o carrinho de compras
 * está em um estado inválido para finalizar o pedido (ex: carrinho vazio).
 * É "unchecked" porque representa um erro de uso da API que idealmente é
 * evitado com validação prévia, não algo que o chamador precise
 * obrigatoriamente capturar.
 */
public class PedidoInvalidoException extends RuntimeException {

    public PedidoInvalidoException(String mensagem) {
        super(mensagem);
    }
}
