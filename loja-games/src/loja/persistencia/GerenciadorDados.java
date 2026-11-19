package loja.persistencia;

import loja.service.Loja;

import java.io.IOException;

/**
 * Padrão de projeto SINGLETON: garante que exista uma única instância
 * responsável por salvar/carregar os dados da loja. Assim, o Main e a
 * thread de auto-save usam exatamente o mesmo objeto (e o mesmo arquivo).
 *
 * Estrutura do Singleton (conforme slides):
 *   1. Construtor privado
 *   2. Variável estática do tipo da própria classe
 *   3. Método de acesso estático getInstance()
 */
public class GerenciadorDados {

    private static final String ARQUIVO_DADOS = "loja.dat";

    // 2. Variável estática que guarda a única instância (criada uma única vez)
    private static GerenciadorDados instancia = new GerenciadorDados();

    // Uso da classe genérica, especializada para Loja
    private final Persistencia<Loja> persistencia;

    // 1. Construtor privado: ninguém de fora consegue dar "new GerenciadorDados()"
    private GerenciadorDados() {
        this.persistencia = new Persistencia<>(ARQUIVO_DADOS);
    }

    // 3. Método de acesso estático
    public static GerenciadorDados getInstance() {
        return instancia;
    }

    public boolean existemDadosSalvos() {
        return persistencia.existeArquivo();
    }

    /**
     * Salva a loja no arquivo. O bloco synchronized(loja) impede que a
     * loja seja alterada (cadastro/compra) no meio da gravação.
     */
    public void salvarLoja(Loja loja) throws IOException {
        synchronized (loja) {
            persistencia.salvar(loja);
        }
    }

    public Loja carregarLoja() throws IOException, ClassNotFoundException {
        Object obj = persistencia.carregar();

        // Só tem sentido se obj for do tipo Loja !
        if (!(obj instanceof Loja)) {
            throw new IOException("O arquivo \"" + getArquivoDados() + "\" não contém uma Loja.");
        }

        // Faz o casting
        return (Loja) obj;
    }

    public String getArquivoDados() {
        return persistencia.getCaminhoArquivo();
    }
}
