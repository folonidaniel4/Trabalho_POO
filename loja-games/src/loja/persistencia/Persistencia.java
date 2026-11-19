package loja.persistencia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Classe GENÉRICA responsável por salvar e carregar qualquer objeto
 * serializável em um arquivo (serialização Java).
 *
 * O parâmetro de tipo T tem um limite superior (upper bound):
 * "T extends Serializable" -> só aceita tipos que podem ser serializados.
 * Assim o compilador impede, por exemplo, Persistencia<Thread>.
 */
public class Persistencia<T extends Serializable> {

    private final String caminhoArquivo;

    public Persistencia(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    /** Grava o objeto no arquivo (sobrescreve o conteúdo anterior). */
    public synchronized void salvar(T objeto) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(caminhoArquivo))) {
            out.writeObject(objeto); // grava o objeto serializando !!!
        }
    }

    /** Lê o objeto gravado no arquivo. */
    public synchronized T carregar() throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(caminhoArquivo))) {
            Object obj = in.readObject();
            // casting para o tipo genérico T (como o (T[]) da classe Stack genérica)
            return (T) obj;
        }
    }

    public boolean existeArquivo() {
        return new File(caminhoArquivo).exists();
    }

    public String getCaminhoArquivo() {
        return caminhoArquivo;
    }
}
