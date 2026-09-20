package br.com.motivaverde.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {

    private static final String HOST = "oracle.fiap.com.br";
    private static final String PORT = "1521";
    private static final String SID = "ORCL";

    private static final String USER = System.getenv("ORACLE_USER");
    private static final String PASSWORD = System.getenv("ORACLE_PASSWORD");

    private static ConexaoBD instancia;

    private Connection conexao;

    private ConexaoBD() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    "Driver Oracle não encontrado. Verifique o ojdbc17.jar.",
                    e
            );
        }
    }

    public static ConexaoBD getInstancia() {
        if (instancia == null) {
            instancia = new ConexaoBD();
        }

        return instancia;
    }

    public Connection conectar() {
        if (USER == null || USER.isBlank()
                || PASSWORD == null || PASSWORD.isBlank()) {

            throw new IllegalStateException(
                    "As variáveis ORACLE_USER e ORACLE_PASSWORD não foram configuradas."
            );
        }

        try {
            if (conexao == null || conexao.isClosed()) {

                String url = "jdbc:oracle:thin:@"
                        + HOST + ":"
                        + PORT + ":"
                        + SID;

                conexao = DriverManager.getConnection(
                        url,
                        USER,
                        PASSWORD
                );

                System.out.println("Conexão com o Oracle realizada com sucesso!");
            }

            return conexao;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao conectar ao Oracle: " + e.getMessage(),
                    e
            );
        }
    }

    public Connection getConexao() {
        return conectar();
    }

    public void desconectar() {
        if (conexao != null) {
            try {
                if (!conexao.isClosed()) {
                    conexao.close();
                    System.out.println("Conexão com o Oracle encerrada.");
                }
            } catch (SQLException e) {
                System.err.println(
                        "Erro ao fechar conexão: " + e.getMessage()
                );
            }
        }
    }
}