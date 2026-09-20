package br.com.motivaverde.dao;

import br.com.motivaverde.db.ConexaoBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RelatorioPrioridadeDAO {

    private static final String SQL_INSERIR =
            "INSERT INTO TB_RELATORIO_PRIORIDADE " +
            "(QT_BAIXA, QT_MODERADA, QT_ALTA, QT_URGENTE, RESUMO) " +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_BUSCAR_POR_ID =
            "SELECT ID_RELATORIO, DATA_GERACAO, QT_BAIXA, " +
            "QT_MODERADA, QT_ALTA, QT_URGENTE, RESUMO " +
            "FROM TB_RELATORIO_PRIORIDADE " +
            "WHERE ID_RELATORIO = ?";

    private static final String SQL_LISTAR_TODAS =
            "SELECT ID_RELATORIO, DATA_GERACAO, QT_BAIXA, " +
            "QT_MODERADA, QT_ALTA, QT_URGENTE, RESUMO " +
            "FROM TB_RELATORIO_PRIORIDADE " +
            "ORDER BY ID_RELATORIO";

    private static final String SQL_ATUALIZAR =
            "UPDATE TB_RELATORIO_PRIORIDADE " +
            "SET QT_BAIXA = ?, QT_MODERADA = ?, QT_ALTA = ?, " +
            "QT_URGENTE = ?, RESUMO = ? " +
            "WHERE ID_RELATORIO = ?";

    private static final String SQL_DELETAR =
            "DELETE FROM TB_RELATORIO_PRIORIDADE " +
            "WHERE ID_RELATORIO = ?";


    public RelatorioPrioridadeDAO() {
    }


    public RelatorioRegistro inserir(
            int qtBaixa,
            int qtModerada,
            int qtAlta,
            int qtUrgente,
            String resumo
    ) {

        Connection conn = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt = conn.prepareStatement(
                SQL_INSERIR,
                new String[]{"ID_RELATORIO"}
        )) {

            stmt.setInt(1, qtBaixa);
            stmt.setInt(2, qtModerada);
            stmt.setInt(3, qtAlta);
            stmt.setInt(4, qtUrgente);
            stmt.setString(5, resumo);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new RuntimeException(
                        "Nenhum relatório foi inserido."
                );
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {

                if (rs.next()) {
                    long id = rs.getLong(1);

                    return buscarPorId(id);
                }
            }

            throw new RuntimeException(
                    "Relatório inserido, mas não foi possível obter o ID gerado."
            );

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao inserir relatório: " + e.getMessage(),
                    e
            );
        }
    }


    public RelatorioRegistro salvarRelatorio(
            int qtBaixa,
            int qtModerada,
            int qtAlta,
            int qtUrgente,
            String resumo
    ) {

        return inserir(
                qtBaixa,
                qtModerada,
                qtAlta,
                qtUrgente,
                resumo
        );
    }


    public RelatorioRegistro buscarPorId(long id) {

        Connection conn = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt =
                     conn.prepareStatement(SQL_BUSCAR_POR_ID)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return extrairRelatorio(rs);
                }

                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao buscar relatório: " + e.getMessage(),
                    e
            );
        }
    }


    public List<RelatorioRegistro> listarTodas() {

        List<RelatorioRegistro> relatorios =
                new ArrayList<>();

        Connection conn = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt =
                     conn.prepareStatement(SQL_LISTAR_TODAS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                relatorios.add(
                        extrairRelatorio(rs)
                );
            }

            return relatorios;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao listar relatórios: "
                            + e.getMessage(),
                    e
            );
        }
    }


    public boolean atualizar(
            long id,
            int qtBaixa,
            int qtModerada,
            int qtAlta,
            int qtUrgente,
            String resumo
    ) {

        Connection conn =
                ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt =
                     conn.prepareStatement(SQL_ATUALIZAR)) {

            stmt.setInt(1, qtBaixa);
            stmt.setInt(2, qtModerada);
            stmt.setInt(3, qtAlta);
            stmt.setInt(4, qtUrgente);
            stmt.setString(5, resumo);
            stmt.setLong(6, id);

            int linhasAfetadas =
                    stmt.executeUpdate();

            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao atualizar relatório: "
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
                    "Erro ao deletar relatório: "
                            + e.getMessage(),
                    e
            );
        }
    }


    private RelatorioRegistro extrairRelatorio(
            ResultSet rs
    ) throws SQLException {

        LocalDateTime dataGeracao =
                rs.getTimestamp("DATA_GERACAO")
                        .toLocalDateTime();

        return new RelatorioRegistro(
                rs.getLong("ID_RELATORIO"),
                dataGeracao,
                rs.getInt("QT_BAIXA"),
                rs.getInt("QT_MODERADA"),
                rs.getInt("QT_ALTA"),
                rs.getInt("QT_URGENTE"),
                rs.getString("RESUMO")
        );
    }


    public record RelatorioRegistro(
            long id,
            LocalDateTime dataGeracao,
            int qtBaixa,
            int qtModerada,
            int qtAlta,
            int qtUrgente,
            String resumo
    ) {
    }
}