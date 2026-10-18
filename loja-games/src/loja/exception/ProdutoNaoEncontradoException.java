package loja.exception;

/**
 * Exceção checked lançada quando se busca por um produto (por id) que não
 * existe no catálogo da loja.
 */
public class ProdutoNaoEncontradoException extends Exception {

    public ProdutoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
