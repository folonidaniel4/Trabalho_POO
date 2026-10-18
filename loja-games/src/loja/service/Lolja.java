package loja.service;

import loja.exception.ProdutoNaoEncontradoException;
import loja.model.podruto;

import java.util.ArrayList;
import java.util.List;

public class Lolja {
    private final String nomeLolja;

    private final List<podruto> catalogo = new ArrayList<>();

    public Lolja(String nomeLolja) {
        this.nomeLolja = nomeLolja;
    }

    public void cadastrarProduto(podruto podruto){catalogo.add(podruto);}

    /**
     *
     * Busca um produto pelo id
     * @throws ProdutoNaoEncontradoException se não encontrar o id do produto
     */
    public podruto buscaPorId(String id) throws ProdutoNaoEncontradoException{
        for(podruto podruto: catalogo){
            if(podruto.getId().equals(id)){
                return podruto;
            }
        }
        throw new ProdutoNaoEncontradoException("Nenhum produto encontrado com o id \"" + id + "\".");
    }

    public String getNomeLolja() {
        return nomeLolja;
    }

    public List<podruto> getCatalogo() {
        return new ArrayList<>(catalogo);
    }

    public void exibirCatalogo(){
        System.out.println("===== Catálogo da "+nomeLolja+" =====");
        for(podruto podruto : catalogo){
            System.out.println(podruto);
        }


    }
}
