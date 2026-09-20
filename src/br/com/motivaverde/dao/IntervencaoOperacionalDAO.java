package br.com.motivaverde.dao;

import br.com.motivaverde.db.ConexaoBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class IntervencaoOperacionalDAO {

    private static final String SQL_INSERIR =
            "INSERT INTO TB_INTERVENCAO_OPERACIONAL " +
            "(ID_TRECHO, TIPO_INTERVENCAO, ALTURA_ANTES, ALTURA_DEPOIS) " +
            "VALUES (?, ?, ?, ?)";

    private static final String SQL_BUSCAR_POR_ID =
            "SELECT ID_INTERVENCAO, ID_TRECHO, TIPO_INTERVENCAO, " +
            "DATA_EXECUCAO, ALTURA_ANTES, ALTURA_DEPOIS " +
            "FROM TB_INTERVENCAO_OPERACIONAL " +
            "WHERE ID_INTERVENCAO = ?";

    private static final String SQL_LISTAR_TODAS =
            "SELECT ID_INTERVENCAO, ID_TRECHO, TIPO_INTERVENCAO, " +
            "DATA_EXECUCAO, ALTURA_ANTES, ALTURA_DEPOIS " +
            "FROM TB_INTERVENCAO_OPERACIONAL " +
            "ORDER BY ID_INTERVENCAO";

    private static final String SQL_ATUALIZAR =
            "UPDATE TB_INTERVENCAO_OPERACIONAL " +
            "SET ID_TRECHO = ?, TIPO_INTERVENCAO = ?, " +
            "ALTURA_ANTES = ?, ALTURA_DEPOIS = ? " +
            "WHERE ID_INTERVENCAO = ?";

    private static final String SQL_DELETAR =
            "DELETE FROM TB_INTERVENCAO_OPERACIONAL " +
            "WHERE ID_INTERVENCAO = ?";


    public IntervencaoOperacionalDAO() {
    }


    public IntervencaoRegistro inserir(
            long idTrecho,
            String tipoIntervencao,
            double alturaAntes,
            double alturaDepois
    ) {

        Connection conn = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt = conn.prepareStatement(
                SQL_INSERIR,
                new String[]{"ID_INTERVENCAO"}
        )) {

            stmt.setLong(1, idTrecho);
            stmt.setString(2, tipoIntervencao);
            stmt.setDouble(3, alturaAntes);
            stmt.setDouble(4, alturaDepois);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new RuntimeException(
                        "Nenhuma intervenção foi inserida."
                );
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {

                if (rs.next()) {
                    long id = rs.getLong(1);

                    return buscarPorId(id);
                }
            }

            throw new RuntimeException(
                    "Intervenção inserida, mas não foi possível obter o ID gerado."
            );

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao inserir intervenção: " + e.getMessage(),
                    e
            );
        }
    }


    public IntervencaoRegistro buscarPorId(long id) {

        Connection conn = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt =
                     conn.prepareStatement(SQL_BUSCAR_POR_ID)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return extrairIntervencao(rs);
                }

                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao buscar intervenção: " + e.getMessage(),
                    e
            );
        }
    }


    public List<IntervencaoRegistro> listarTodas() {

        List<IntervencaoRegistro> intervencoes =
                new ArrayList<>();

        Connection conn = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt =
                     conn.prepareStatement(SQL_LISTAR_TODAS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                intervencoes.add(
                        extrairIntervencao(rs)
                );
            }

            return intervencoes;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao listar intervenções: "
                            + e.getMessage(),
                    e
            );
        }
    }


    public boolean atualizar(
            long id,
            long idTrecho,
            String tipoIntervencao,
            double alturaAntes,
            double alturaDepois
    ) {

        Connection conn =
                ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt =
                     conn.prepareStatement(SQL_ATUALIZAR)) {

            stmt.setLong(1, idTrecho);
            stmt.setString(2, tipoIntervencao);
            stmt.setDouble(3, alturaAntes);
            stmt.setDouble(4, alturaDepois);
            stmt.setLong(5, id);

            int linhasAfetadas =
                    stmt.executeUpdate();

            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao atualizar intervenção: "
                            + e.getMessage(),
                    e
            );
        }
    }


    public boolean deletar(long id) {

        Connection conn =
                ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt =
                     conn.prepareStatement(SQL_DELETAR)) {

            stmt.setLong(1, id);

            int linhasAfetadas =
                    stmt.executeUpdate();

            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao deletar intervenção: "
                            + e.getMessage(),
                    e
            );
        }
    }


    private IntervencaoRegistro extrairIntervencao(
            ResultSet rs
    ) throws SQLException {

        LocalDateTime dataExecucao =
                rs.getTimestamp("DATA_EXECUCAO")
                        .toLocalDateTime();

        return new IntervencaoRegistro(
                rs.getLong("ID_INTERVENCAO"),
                rs.getLong("ID_TRECHO"),
                rs.getString("TIPO_INTERVENCAO"),
                dataExecucao,
                rs.getDouble("ALTURA_ANTES"),
                rs.getDouble("ALTURA_DEPOIS")
        );
    }


    public record IntervencaoRegistro(
            long id,
            long idTrecho,
            String tipoIntervencao,
            LocalDateTime dataExecucao,
            double alturaAntes,
            double alturaDepois
    ) {
    }
}