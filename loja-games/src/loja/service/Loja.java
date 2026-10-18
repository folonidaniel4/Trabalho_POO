package loja.service;

import loja.exception.ProdutoNaoEncontradoException;
import loja.model.Produto;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa o catálogo da loja online e as operações de gerenciamento
 * de produtos (cadastro e busca).
 */
public class Loja {

    private final String nomeLoja;
    private final List<Produto> catalogo = new ArrayList<>();

    public Loja(String nomeLoja) {
        this.nomeLoja = nomeLoja;
    }

    public void cadastrarProduto(Produto produto) {
        catalogo.add(produto);
    }

    /**
     * Busca um produto pelo id.
     * @throws ProdutoNaoEncontradoException se nenhum produto com esse id existir.
     */
    public Produto buscarPorId(String id) throws ProdutoNaoEncontradoException {
        for (Produto produto : catalogo) {
            if (produto.getId().equals(id)) {
                return produto;
            }
        }
        throw new ProdutoNaoEncontradoException("Nenhum produto encontrado com o id \"" + id + "\".");
    }

    public List<Produto> getCatalogo() {
        return new ArrayList<>(catalogo);
    }

    public String getNomeLoja() {
        return nomeLoja;
    }

    /** Exibe o catálogo inteiro, demonstrando o polimorfismo de toString()/calcularPrecoFinal(). */
    public void exibirCatalogo() {
        System.out.println("===== Catálogo da " + nomeLoja + " =====");
        for (Produto produto : catalogo) {
            System.out.println(produto);
        }
    }
}
