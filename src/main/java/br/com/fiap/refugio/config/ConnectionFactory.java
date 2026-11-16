package br.com.fiap.refugio.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fabrica de conexoes JDBC com o Oracle.
 *
 * <p>Le as credenciais de variaveis de ambiente (para o render.com injetar em
 * producao) com defaults apontando para o Oracle 21c XE local:</p>
 * <ul>
 *   <li>DB_URL      (default jdbc:oracle:thin:@localhost:1521/XEPDB1)</li>
 *   <li>DB_USERNAME (default iatotvs)</li>
 *   <li>DB_PASSWORD (default iatotvs123)</li>
 * </ul>
 *
 * <p>Sem pool de conexoes (aceitavel para o escopo da GS). Cada chamada a
 * {@link #getConnection()} abre uma conexao nova via DriverManager; o chamador
 * (DAO) e responsavel por fechar.</p>
 */
public final class ConnectionFactory {

    private static final String DEFAULT_URL = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
    private static final String DEFAULT_USER = "iatotvs";
    private static final String DEFAULT_PASSWORD = "iatotvs123";

    private ConnectionFactory() {
        // utilitario - nao instanciar
    }

    private static String env(String chave, String padrao) {
        String valor = System.getenv(chave);
        return (valor == null || valor.isBlank()) ? padrao : valor;
    }

    /**
     * Abre uma nova conexao JDBC com o Oracle.
     *
     * @return conexao aberta (o chamador deve fecha-la)
     * @throws SQLException se nao conseguir conectar
     */
    public static Connection getConnection() throws SQLException {
        String url = env("DB_URL", DEFAULT_URL);
        String user = env("DB_USERNAME", DEFAULT_USER);
        String password = env("DB_PASSWORD", DEFAULT_PASSWORD);
        return DriverManager.getConnection(url, user, password);
    }
}
