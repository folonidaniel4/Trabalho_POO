package loja.model;

/**
 * Jogo vendido em mídia física (caixa/disco), que precisa ser enviado ao cliente.
 * Adiciona uma taxa de frete fixa ao preço final.
 */
public class JogoFisico extends Produto {

    private static final double TAXA_FRETE = 15.00;

    private final String plataforma; // PS5, Xbox Series X, Switch, etc.
    private final boolean edicaoColecionador;

    public JogoFisico(String id, String nome, double preco, int quantidadeEstoque,
                       String plataforma, boolean edicaoColecionador) {
        super(id, nome, preco, quantidadeEstoque);
        this.plataforma = plataforma;
        this.edicaoColecionador = edicaoColecionador;
    }

    @Override
    public double calcularPrecoFinal() {
        double precoBase = getPreco() + TAXA_FRETE;
        return edicaoColecionador ? precoBase * 1.25 : precoBase; // edição de colecionador: +25%
    }

    @Override
    public String getCategoria() {
        return "Jogo Físico";
    }

    public String getPlataforma() {
        return plataforma;
    }

    public boolean isEdicaoColecionador() {
        return edicaoColecionador;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Plataforma: %s | Edição de colecionador: %s",
                plataforma, edicaoColecionador ? "Sim" : "Não");
    }
}
