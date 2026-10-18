package loja.model;

/**
 * Acessório de games (controle, headset, cadeira gamer, etc.).
 * Produtos com garantia estendida recebem um acréscimo no preço final.
 */
public class Acessorio extends Produto {

    private final String tipo; // Controle, Headset, Cadeira, etc.
    private final int mesesGarantia;

    public Acessorio(String id, String nome, double preco, int quantidadeEstoque,
                      String tipo, int mesesGarantia) {
        super(id, nome, preco, quantidadeEstoque);
        this.tipo = tipo;
        this.mesesGarantia = mesesGarantia;
    }

    @Override
    public double calcularPrecoFinal() {
        // Garantia estendida (acima de 12 meses) soma 5% ao preço.
        return mesesGarantia > 12 ? getPreco() * 1.05 : getPreco();
    }

    @Override
    public String getCategoria() {
        return "Acessório";
    }

    public String getTipo() {
        return tipo;
    }

    public int getMesesGarantia() {
        return mesesGarantia;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Tipo: %s | Garantia: %d meses", tipo, mesesGarantia);
    }
}
