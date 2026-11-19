package loja;

import loja.exception.EstoqueInsuficienteException;
import loja.exception.PedidoInvalidoException;
import loja.exception.ProdutoNaoEncontradoException;
import loja.model.Acessorio;
import loja.model.JogoDigital;
import loja.model.JogoFisico;
import loja.model.Produto;
import loja.persistencia.AutoSave;
import loja.persistencia.GerenciadorDados;
import loja.service.Carrinho;
import loja.service.Loja;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static final long INTERVALO_AUTO_SAVE_MS = 30_000; // 30 segundos

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        // Singleton: sempre a mesma instância do gerenciador de dados
        GerenciadorDados gerenciador = GerenciadorDados.getInstance();

        // ----- Persistência: carregar na volta do sistema -----
        Loja loja = carregarOuCriarLoja(gerenciador);

        // ----- Thread de auto-save rodando em paralelo ao menu -----
        AutoSave autoSave = new AutoSave(loja, INTERVALO_AUTO_SAVE_MS);
        Thread threadAutoSave = new Thread(autoSave);
        threadAutoSave.start();

        Carrinho carrinho = new Carrinho();
        int opcao;
        do {
            exibirMenu(loja);
            opcao = lerInteiro("Escolha uma opção: ");
            switch (opcao) {
                case 1:
                    loja.exibirCatalogo();
                    break;
                case 2:
                    cadastrarProduto(loja);
                    break;
                case 3:
                    adicionarAoCarrinho(loja, carrinho);
                    break;
                case 4:
                    carrinho.exibirResumo();
                    break;
                case 5:
                    carrinho = finalizarCompra(loja, carrinho);
                    break;
                case 6:
                    reporEstoque(loja);
                    break;
                case 0:
                    System.out.println("Encerrando...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);

        // ----- Encerra a thread de auto-save -----
        // solicita à thread que pare. Se ela estiver em sleep(), uma exceção é lançada!
        threadAutoSave.interrupt();
        try {
            // a Main Thread aguarda a thread de auto-save terminar
            threadAutoSave.join();
        } catch (InterruptedException e) {
            System.out.println("Interrompido ao aguardar o auto-save: " + e.getMessage());
        }

        // ----- Salva na saída do sistema -----
        try {
            gerenciador.salvarLoja(loja);
            System.out.println("Dados salvos em \"" + gerenciador.getArquivoDados() + "\". Até logo!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar os dados na saída: " + e.getMessage());
        }
    }

    // ===================== Persistência =====================

    private static Loja carregarOuCriarLoja(GerenciadorDados gerenciador) {
        if (gerenciador.existemDadosSalvos()) {
            try {
                Loja loja = gerenciador.carregarLoja();
                System.out.println("Dados carregados de \"" + gerenciador.getArquivoDados() + "\".");
                return loja;
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Não foi possível carregar os dados salvos (" + e.getMessage()
                        + "). Iniciando com o catálogo padrão.");
            }
        } else {
            System.out.println("Nenhum dado salvo encontrado. Iniciando com o catálogo padrão.");
        }
        return criarLojaPadrao();
    }

    private static Loja criarLojaPadrao() {
        Loja loja = new Loja("GameZone Store");
        loja.cadastrarProduto(new JogoDigital("D001", "Hollow Knight", 39.90, 100, "Steam", 9.5));
        loja.cadastrarProduto(new JogoFisico("F001", "God of War Ragnarök", 249.90, 5, "PS5", false));
        loja.cadastrarProduto(new JogoFisico("F002", "Zelda: Tears of the Kingdom - Edição Colecionador", 349.90, 2, "Switch", true));
        loja.cadastrarProduto(new Acessorio("A001", "Headset HyperX Cloud II", 349.00, 10, "Headset", 24));
        return loja;
    }

    // ===================== Menu =====================

    private static void exibirMenu(Loja loja) {
        System.out.println();
        System.out.println("===== " + loja.getNomeLoja() + " =====");
        System.out.println("1 - Listar catálogo");
        System.out.println("2 - Cadastrar produto");
        System.out.println("3 - Adicionar produto ao carrinho");
        System.out.println("4 - Ver carrinho");
        System.out.println("5 - Finalizar compra");
        System.out.println("6 - Repor estoque");
        System.out.println("0 - Sair (salvar e encerrar)");
    }

    private static void cadastrarProduto(Loja loja) {
        System.out.println("Tipo: 1 - Jogo Digital | 2 - Jogo Físico | 3 - Acessório");
        int tipo = lerInteiro("Tipo: ");
        if (tipo < 1 || tipo > 3) {
            System.out.println("Tipo inválido.");
            return;
        }

        String id = lerTexto("Id: ");
        try {
            loja.buscarPorId(id);
            System.out.println("Já existe um produto com o id \"" + id + "\".");
            return;
        } catch (ProdutoNaoEncontradoException e) {
            // id livre: pode cadastrar
        }

        String nome = lerTexto("Nome: ");
        double preco = lerDecimal("Preço: ");
        int estoque = lerInteiro("Quantidade em estoque: ");

        try {
            Produto produto;
            switch (tipo) {
                case 1:
                    produto = new JogoDigital(id, nome, preco, estoque,
                            lerTexto("Plataforma (Steam, PSN...): "),
                            lerDecimal("Tamanho do download (GB): "));
                    break;
                case 2:
                    produto = new JogoFisico(id, nome, preco, estoque,
                            lerTexto("Plataforma (PS5, Switch...): "),
                            lerTexto("Edição de colecionador? (s/n): ").equalsIgnoreCase("s"));
                    break;
                default:
                    produto = new Acessorio(id, nome, preco, estoque,
                            lerTexto("Tipo (Controle, Headset...): "),
                            lerInteiro("Meses de garantia: "));
            }
            loja.cadastrarProduto(produto);
            System.out.println("Produto cadastrado: " + produto);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    private static void adicionarAoCarrinho(Loja loja, Carrinho carrinho) {
        try {
            Produto produto = loja.buscarPorId(lerTexto("Id do produto: "));
            int quantidade = lerInteiro("Quantidade: ");
            carrinho.adicionarItem(produto, quantidade);
            System.out.println("Adicionado ao carrinho: " + quantidade + "x " + produto.getNome());
        } catch (ProdutoNaoEncontradoException | IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    /** Finaliza o pedido e devolve um carrinho novo (vazio) se der certo. */
    private static Carrinho finalizarCompra(Loja loja, Carrinho carrinho) {
        try {
            carrinho.exibirResumo();
            // synchronized(loja): o auto-save não grava a loja no meio da baixa de estoque
            synchronized (loja) {
                carrinho.finalizarPedido();
            }
            System.out.println("Pedido finalizado com sucesso!");
            return new Carrinho();
        } catch (PedidoInvalidoException | EstoqueInsuficienteException e) {
            System.out.println("Erro ao finalizar pedido: " + e.getMessage());
            return carrinho;
        }
    }

    private static void reporEstoque(Loja loja) {
        try {
            Produto produto = loja.buscarPorId(lerTexto("Id do produto: "));
            int quantidade = lerInteiro("Quantidade a adicionar: ");
            synchronized (loja) {
                produto.adicionarEstoque(quantidade);
            }
            System.out.println("Estoque atualizado: " + produto);
        } catch (ProdutoNaoEncontradoException | IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    // ===================== Leitura de dados =====================

    private static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            try {
                return Integer.parseInt(lerTexto(mensagem));
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }

    private static double lerDecimal(String mensagem) {
        while (true) {
            try {
                return Double.parseDouble(lerTexto(mensagem).replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido (ex: 59.90).");
            }
        }
    }
}
