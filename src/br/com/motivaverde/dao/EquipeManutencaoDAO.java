package br.com.motivaverde.dao;

import br.com.motivaverde.db.ConexaoBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EquipeManutencaoDAO {

    private static final String SQL_INSERIR =
            "INSERT INTO TB_EQUIPE_MANUTENCAO (NOME, ESPECIALIDADE) VALUES (?, ?)";

    private static final String SQL_BUSCAR_POR_ID =
            "SELECT ID_EQUIPE, NOME, ESPECIALIDADE " +
            "FROM TB_EQUIPE_MANUTENCAO " +
            "WHERE ID_EQUIPE = ?";

    private static final String SQL_LISTAR_TODAS =
            "SELECT ID_EQUIPE, NOME, ESPECIALIDADE " +
            "FROM TB_EQUIPE_MANUTENCAO " +
            "ORDER BY ID_EQUIPE";

    private static final String SQL_ATUALIZAR =
            "UPDATE TB_EQUIPE_MANUTENCAO " +
            "SET NOME = ?, ESPECIALIDADE = ? " +
            "WHERE ID_EQUIPE = ?";

    private static final String SQL_DELETAR =
            "DELETE FROM TB_EQUIPE_MANUTENCAO " +
            "WHERE ID_EQUIPE = ?";


    public EquipeManutencaoDAO() {
    }


    public EquipeRegistro inserir(String nome, String especialidade) {

        Connection conn = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt = conn.prepareStatement(
                SQL_INSERIR,
                new String[]{"ID_EQUIPE"}
        )) {

            stmt.setString(1, nome);
            stmt.setString(2, especialidade);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new RuntimeException(
                        "Nenhuma equipe foi inserida."
                );
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {

                if (rs.next()) {
                    long id = rs.getLong(1);

                    return new EquipeRegistro(
                            id,
                            nome,
                            especialidade
                    );
                }
            }

            throw new RuntimeException(
                    "Equipe inserida, mas não foi possível obter o ID gerado."
            );

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao inserir equipe: " + e.getMessage(),
                    e
            );
        }
    }


    public EquipeRegistro buscarPorId(long id) {

        Connection conn = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt =
                     conn.prepareStatement(SQL_BUSCAR_POR_ID)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return extrairEquipe(rs);
                }

                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao buscar equipe: " + e.getMessage(),
                    e
            );
        }
    }


    public List<EquipeRegistro> listarTodas() {

        List<EquipeRegistro> equipes = new ArrayList<>();

        Connection conn = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt =
                     conn.prepareStatement(SQL_LISTAR_TODAS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                equipes.add(extrairEquipe(rs));
            }

            return equipes;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao listar equipes: " + e.getMessage(),
                    e
            );
        }
    }


    public boolean atualizar(
            long id,
            String nome,
            String especialidade
    ) {

        Connection conn = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt =
                     conn.prepareStatement(SQL_ATUALIZAR)) {

            stmt.setString(1, nome);
            stmt.setString(2, especialidade);
            stmt.setLong(3, id);

            int linhasAfetadas = stmt.executeUpdate();

            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao atualizar equipe: " + e.getMessage(),
                    e
            );
        }
    }


    public boolean deletar(long id) {

        Connection conn = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt =
                     conn.prepareStatement(SQL_DELETAR)) {

            stmt.setLong(1, id);

            int linhasAfetadas = stmt.executeUpdate();

            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao deletar equipe: " + e.getMessage(),
                    e
            );
        }
    }


    private EquipeRegistro extrairEquipe(ResultSet rs)
            throws SQLException {

        return new EquipeRegistro(
                rs.getLong("ID_EQUIPE"),
                rs.getString("NOME"),
                rs.getString("ESPECIALIDADE")
        );
    }


    public record EquipeRegistro(
            long id,
            String nome,
            String especialidade
    ) {
    }
}