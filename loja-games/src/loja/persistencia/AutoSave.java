package loja.persistencia;

import loja.service.Loja;

import java.io.IOException;

/**
 * THREAD de auto-save: a cada intervalo de tempo salva os dados da loja
 * em disco, rodando em paralelo com o menu (Main Thread).
 *
 * Implementa Runnable (como o exemplo "Operario" dos slides de Threads):
 * o objeto define o comportamento e é passado para um Thread que o executa.
 */
public class AutoSave implements Runnable {

    private final Loja loja;
    private final long intervaloMs;

    public AutoSave(Loja loja, long intervaloMs) {
        this.loja = loja;
        this.intervaloMs = intervaloMs;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(intervaloMs);
            } catch (InterruptedException e) {
                // a Main Thread chamou interrupt() ao encerrar o sistema:
                // como a thread estava em sleep(), a exceção é lançada e o run() termina.
                return;
            }
            try {
                GerenciadorDados.getInstance().salvarLoja(loja);
            } catch (IOException e) {
                System.out.println("\n[auto-save] Erro ao salvar: " + e.getMessage());
            }
        }
    }
}
