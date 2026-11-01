package com.example.skadi_beckend.util;

import io.github.cdimascio.dotenv.Dotenv;

/*
 * Lê configurações do sistema na mesma ordem usada pelo EmailUtil e pelo ConnectionFactory:
 * 1. VM Options (-DCHAVE=valor)
 * 2. Variáveis de ambiente do sistema (Render / Docker)
 * 3. Arquivo .env (desenvolvimento local)
 *
 * O valor é lido de novo a cada chamada, então se o .env mudar não é preciso reiniciar o servidor.
 */
public class Configuracao {

    private Configuracao() {
    }

    // Devolve o valor da chave ou null se não estiver configurada
    public static String obter(String chave) {
        String valor = System.getProperty(chave);

        if (valor == null || valor.isBlank()) {
            valor = System.getenv(chave);
        }

        if (valor == null || valor.isBlank()) {
            try {
                valor = Dotenv.configure().ignoreIfMissing().load().get(chave);
            } catch (Exception e) {
                // O arquivo .env é opcional
            }
        }

        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}
