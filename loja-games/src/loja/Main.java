package loja;

import loja.exception.EstoqueInsuficienteException;
import loja.exception.PedidoInvalidoException;
import loja.exception.ProdutoNaoEncontradoException;
import loja.model.Acessorio;
import loja.model.JogoDigital;
import loja.model.JogoFisico;
import loja.model.Produto;
import loja.service.Carrinho;
import loja.service.Loja;

public class Main {

    public static void main(String[] args) {

        Loja loja = new Loja("GameZone Store");

        // Cadastro de produtos de tipos diferentes (herança: todos são Produto)
        loja.cadastrarProduto(new JogoDigital("D001", "Hollow Knight", 39.90, 100, "Steam", 9.5));
        loja.cadastrarProduto(new JogoFisico("F001", "God of War Ragnarök", 249.90, 5, "PS5", false));
        loja.cadastrarProduto(new JogoFisico("F002", "Zelda: Tears of the Kingdom - Edição Colecionador", 349.90, 2, "Switch", true));
        loja.cadastrarProduto(new Acessorio("A001", "Headset HyperX Cloud II", 349.00, 10, "Headset", 24));

        // Polimorfismo: cada item imprime/calcula o preço de forma diferente
        loja.exibirCatalogo();
        System.out.println();
Map
        // ----- Cenário de sucesso -----
        Carrinho carrinho = new Carrinho();
        try {
            Produto p1 = loja.buscarPorId("D001");
            Produto p2 = loja.buscarPorId("F001");

            carrinho.adicionarItem(p1, 1);
            carrinho.adicionarItem(p2, 1);

            carrinho.exibirResumo();
            carrinho.finalizarPedido();
            System.out.println("Pedido finalizado com sucesso!\n");

        } catch (ProdutoNaoEncontradoException | EstoqueInsuficienteException e) {
            System.out.println("Erro ao processar pedido: " + e.getMessage());
        }

        // ----- Cenário de erro: produto inexistente -----
        try {
            loja.buscarPorId("X999");
        } catch (ProdutoNaoEncontradoException e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }

        // ----- Cenário de erro: estoque insuficiente -----
        try {
            Produto colecionador = loja.buscarPorId("F002");
            Carrinho carrinho2 = new Carrinho();
            carrinho2.adicionarItem(colecionador, 5); // só há 2 em estoque
            carrinho2.finalizarPedido();
        } catch (ProdutoNaoEncontradoException | EstoqueInsuficienteException e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }

        // ----- Cenário de erro: carrinho vazio (exceção unchecked) -----
        try {
            Carrinho carrinhoVazio = new Carrinho();
            carrinhoVazio.finalizarPedido();
        } catch (PedidoInvalidoException | EstoqueInsuficienteException e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }

        // ----- Cenário de erro: dado inválido no construtor -----
        try {
            new Acessorio("A002", "Controle inválido", -50.0, 3, "Controle", 6);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }

        System.out.println("\nEstoque final:");
        loja.exibirCatalogo();
    }
}
