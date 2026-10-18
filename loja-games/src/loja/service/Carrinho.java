package loja.service;

import loja.exception.EstoqueInsuficienteException;
import loja.exception.PedidoInvalidoException;
import loja.model.Produto;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Carrinho de compras: acumula produtos e quantidades antes da finalização
 * do pedido. Usa Produto.calcularPrecoFinal() polimorficamente para somar
 * o total, sem precisar saber se é jogo digital, físico ou acessório.
 */
public class Carrinho {

    private final Map<Produto, Integer> itens = new LinkedHashMap<>();

    public void adicionarItem(Produto produto, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        itens.merge(produto, quantidade, Integer::sum);
    }

    public double calcularTotal() {
        double total = 0;
        for (Map.Entry<Produto, Integer> entry : itens.entrySet()) {
            // Chamada polimórfica: cada produto calcula seu próprio preço final.
            total += entry.getKey().calcularPrecoFinal() * entry.getValue();
        }
        return total;
    }

    /**
     * Finaliza o pedido, dando baixa no estoque de cada item.
     * @throws EstoqueInsuficienteException se algum item não tiver estoque suficiente.
     * @throws PedidoInvalidoException      se o carrinho estiver vazio.
     */
    public void finalizarPedido() throws EstoqueInsuficienteException {
        if (itens.isEmpty()) {
            throw new PedidoInvalidoException("Não é possível finalizar um pedido com o carrinho vazio.");
        }
        for (Map.Entry<Produto, Integer> entry : itens.entrySet()) {
            entry.getKey().removerEstoque(entry.getValue());
        }
    }

    public Map<Produto, Integer> getItens() {
        return new LinkedHashMap<>(itens);
    }

    public void exibirResumo() {
        System.out.println("----- Resumo do carrinho -----");
        for (Map.Entry<Produto, Integer> entry : itens.entrySet()) {
            Produto p = entry.getKey();
            int qtd = entry.getValue();
            System.out.printf("%dx %s - subtotal: R$ %.2f%n", qtd, p.getNome(), p.calcularPrecoFinal() * qtd);
        }
        System.out.printf("TOTAL: R$ %.2f%n", calcularTotal());
    }
}
