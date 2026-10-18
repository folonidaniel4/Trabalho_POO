package loja.model;

/**
 * Jogo vendido em formato digital (download).
 * Não ocupa espaço físico em estoque real, mas ainda controlamos "licenças"
 * disponíveis como se fossem estoque. Tem um pequeno desconto fixo por não
 * ter custo de mídia física.
 */
public class JogoDigital extends Produto {

    private static final double DESCONTO_DIGITAL = 0.10; // 10% mais barato que a mídia física

    private final String plataforma; // Steam, Epic Games, PSN, etc.
    private final double tamanhoDownloadGB;

    public JogoDigital(String id, String nome, double preco, int quantidadeEstoque,
                        String plataforma, double tamanhoDownloadGB) {
        super(id, nome, preco, quantidadeEstoque);
        this.plataforma = plataforma;
        this.tamanhoDownloadGB = tamanhoDownloadGB;
    }

    @Override
    public double calcularPrecoFinal() {
        return getPreco() * (1 - DESCONTO_DIGITAL);
    }

    @Override
    public String getCategoria() {
        return "Jogo Digital";
    }

    public String getPlataforma() {
        return plataforma;
    }

    public double getTamanhoDownloadGB() {
        return tamanhoDownloadGB;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Plataforma: %s | Download: %.1f GB", plataforma, tamanhoDownloadGB);
    }
}
