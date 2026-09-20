package br.com.motivaverde.dao;

import br.com.motivaverde.db.ConexaoBD;
import br.com.motivaverde.model.CondicaoCrescimento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrechoRodoviaDAO {

    private static final String SQL_INSERIR =
            "INSERT INTO TB_TRECHO_RODOVIA " +
            "(QUILOMETRO, ALTURA_VEGETACAO, CONDICAO_CRESCIMENTO, " +
            "ID_EQUIPE, TIPO_TRECHO, CODIGO_SENSOR) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_BUSCAR_POR_ID =
            "SELECT ID_TRECHO, QUILOMETRO, ALTURA_VEGETACAO, " +
            "CONDICAO_CRESCIMENTO, ID_EQUIPE, TIPO_TRECHO, CODIGO_SENSOR " +
            "FROM TB_TRECHO_RODOVIA " +
            "WHERE ID_TRECHO = ?";

    private static final String SQL_LISTAR_TODAS =
            "SELECT ID_TRECHO, QUILOMETRO, ALTURA_VEGETACAO, " +
            "CONDICAO_CRESCIMENTO, ID_EQUIPE, TIPO_TRECHO, CODIGO_SENSOR " +
            "FROM TB_TRECHO_RODOVIA " +
            "ORDER BY QUILOMETRO";

    private static final String SQL_ATUALIZAR =
            "UPDATE TB_TRECHO_RODOVIA " +
            "SET QUILOMETRO = ?, ALTURA_VEGETACAO = ?, " +
            "CONDICAO_CRESCIMENTO = ?, ID_EQUIPE = ?, " +
            "TIPO_TRECHO = ?, CODIGO_SENSOR = ? " +
            "WHERE ID_TRECHO = ?";

    private static final String SQL_DELETAR =
            "DELETE FROM TB_TRECHO_RODOVIA " +
            "WHERE ID_TRECHO = ?";


    public TrechoRodoviaDAO() {
    }


    public TrechoRegistro inserir(
            int quilometro,
            double alturaVegetacao,
            CondicaoCrescimento condicaoCrescimento,
            Long idEquipe,
            String tipoTrecho,
            String codigoSensor
    ) {

        Connection conn = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt = conn.prepareStatement(
                SQL_INSERIR,
                new String[]{"ID_TRECHO"}
        )) {

            stmt.setInt(1, quilometro);
            stmt.setDouble(2, alturaVegetacao);
            stmt.setString(3, condicaoCrescimento.name());

            if (idEquipe != null) {
                stmt.setLong(4, idEquipe);
            } else {
                stmt.setNull(4, java.sql.Types.NUMERIC);
            }

            stmt.setString(5, tipoTrecho);

            if (codigoSensor != null) {
                stmt.setString(6, codigoSensor);
            } else {
                stmt.setNull(6, java.sql.Types.VARCHAR);
            }

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new RuntimeException(
                        "Nenhum trecho foi inserido."
                );
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {

                if (rs.next()) {

                    long id = rs.getLong(1);

                    return new TrechoRegistro(
                            id,
                            quilometro,
                            alturaVegetacao,
                            condicaoCrescimento,
                            idEquipe,
                            tipoTrecho,
                            codigoSensor
                    );
                }
            }

            throw new RuntimeException(
                    "Trecho inserido, mas não foi possível obter o ID gerado."
            );

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao inserir trecho: " + e.getMessage(),
                    e
            );
        }
    }


    public TrechoRegistro buscarPorId(long id) {

        Connection conn = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt =
                     conn.prepareStatement(SQL_BUSCAR_POR_ID)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return extrairTrecho(rs);
                }

                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao buscar trecho: " + e.getMessage(),
                    e
            );
        }
    }


    public List<TrechoRegistro> listarTodas() {

        List<TrechoRegistro> trechos = new ArrayList<>();

        Connection conn = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt =
                     conn.prepareStatement(SQL_LISTAR_TODAS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                trechos.add(extrairTrecho(rs));
            }

            return trechos;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao listar trechos: " + e.getMessage(),
                    e
            );
        }
    }


    public boolean atualizar(
            long id,
            int quilometro,
            double alturaVegetacao,
            CondicaoCrescimento condicaoCrescimento,
            Long idEquipe,
            String tipoTrecho,
            String codigoSensor
    ) {

        Connection conn = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt =
                     conn.prepareStatement(SQL_ATUALIZAR)) {

            stmt.setInt(1, quilometro);
            stmt.setDouble(2, alturaVegetacao);
            stmt.setString(3, condicaoCrescimento.name());

            if (idEquipe != null) {
                stmt.setLong(4, idEquipe);
            } else {
                stmt.setNull(4, java.sql.Types.NUMERIC);
            }

            stmt.setString(5, tipoTrecho);

            if (codigoSensor != null) {
                stmt.setString(6, codigoSensor);
            } else {
                stmt.setNull(6, java.sql.Types.VARCHAR);
            }

            stmt.setLong(7, id);

            int linhasAfetadas = stmt.executeUpdate();

            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao atualizar trecho: " + e.getMessage(),
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
                    "Erro ao deletar trecho: " + e.getMessage(),
                    e
            );
        }
    }


    private TrechoRegistro extrairTrecho(ResultSet rs)
            throws SQLException {

        long idEquipeBanco = rs.getLong("ID_EQUIPE");

        Long idEquipe = rs.wasNull()
                ? null
                : idEquipeBanco;

        return new TrechoRegistro(
                rs.getLong("ID_TRECHO"),
                rs.getInt("QUILOMETRO"),
                rs.getDouble("ALTURA_VEGETACAO"),
                CondicaoCrescimento.valueOf(
                        rs.getString("CONDICAO_CRESCIMENTO")
                ),
                idEquipe,
                rs.getString("TIPO_TRECHO"),
                rs.getString("CODIGO_SENSOR")
        );
    }


    public record TrechoRegistro(
            long id,
            int quilometro,
            double alturaVegetacao,
            CondicaoCrescimento condicaoCrescimento,
            Long idEquipe,
            String tipoTrecho,
            String codigoSensor
    ) {
    }
}