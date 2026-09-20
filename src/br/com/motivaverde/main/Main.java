package br.com.motivaverde.main;

import br.com.motivaverde.dao.EquipeManutencaoDAO;
import br.com.motivaverde.dao.EquipeManutencaoDAO.EquipeRegistro;
import br.com.motivaverde.dao.IntervencaoOperacionalDAO;
import br.com.motivaverde.dao.IntervencaoOperacionalDAO.IntervencaoRegistro;
import br.com.motivaverde.dao.RelatorioPrioridadeDAO;
import br.com.motivaverde.dao.RelatorioPrioridadeDAO.RelatorioRegistro;
import br.com.motivaverde.dao.TrechoRodoviaDAO;
import br.com.motivaverde.dao.TrechoRodoviaDAO.TrechoRegistro;
import br.com.motivaverde.db.ConexaoBD;
import br.com.motivaverde.model.CondicaoCrescimento;
import br.com.motivaverde.model.EquipeManutencao;
import br.com.motivaverde.model.TrechoMonitorado;
import br.com.motivaverde.model.TrechoRodovia;
import br.com.motivaverde.service.GeradorRelatorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        ConexaoBD conexao = ConexaoBD.getInstancia();

        try {

            System.out.println(
                    "============================================="
            );
            System.out.println(
                    "       MOTIVA VERDE - SPRINT 03"
            );
            System.out.println(
                    "============================================="
            );

            // 1. Testar conexão
            conexao.conectar();


            // 2. Instanciar DAOs
            EquipeManutencaoDAO daoEquipe =
                    new EquipeManutencaoDAO();

            TrechoRodoviaDAO daoTrecho =
                    new TrechoRodoviaDAO();

            IntervencaoOperacionalDAO daoIntervencao =
                    new IntervencaoOperacionalDAO();

            RelatorioPrioridadeDAO daoRelatorio =
                    new RelatorioPrioridadeDAO();


            // 3. Demonstrar CRUD
            demonstrarCrud(
                    daoEquipe,
                    daoTrecho,
                    daoIntervencao,
                    daoRelatorio
            );


            // 4. Carregar trechos reais do Oracle
            System.out.println(
                    "\n============================================="
            );
            System.out.println(
                    "CARREGANDO TRECHOS DO BANCO"
            );
            System.out.println(
                    "============================================="
            );

            TrechoRodovia[] trechos =
                    carregarTrechosDoBanco(
                            daoEquipe,
                            daoTrecho
                    );

            System.out.println(
                    "Trechos carregados: "
                            + trechos.length
            );


            // 5. Gerar relatório e salvar no Oracle
            System.out.println(
                    "\n============================================="
            );
            System.out.println(
                    "GERANDO RELATÓRIO"
            );
            System.out.println(
                    "============================================="
            );

            GeradorRelatorio gerador =
                    new GeradorRelatorio();

            gerador.gerarRelatorio(trechos);


            // 6. Consultar histórico
            System.out.println(
                    "\n============================================="
            );
            System.out.println(
                    "HISTÓRICO DE RELATÓRIOS"
            );
            System.out.println(
                    "============================================="
            );

            daoRelatorio.listarTodas()
                    .forEach(System.out::println);


        } catch (Exception e) {

            System.err.println(
                    "\nErro durante a execução do sistema:"
            );

            System.err.println(
                    e.getMessage()
            );

            e.printStackTrace();

        } finally {

            // 7. Encerrar conexão
            conexao.desconectar();
        }
    }


    private static void demonstrarCrud(
            EquipeManutencaoDAO daoEquipe,
            TrechoRodoviaDAO daoTrecho,
            IntervencaoOperacionalDAO daoIntervencao,
            RelatorioPrioridadeDAO daoRelatorio
    ) {

        System.out.println(
                "\n============================================="
        );
        System.out.println(
                "DEMONSTRAÇÃO DOS CRUDs"
        );
        System.out.println(
                "============================================="
        );


        // =====================================================
        // CRUD - EQUIPE
        // =====================================================

        System.out.println(
                "\n===== CRUD EQUIPE ====="
        );

        EquipeRegistro equipeTeste =
                daoEquipe.inserir(
                        "Equipe CRUD",
                        "Manutenção de teste"
                );

        System.out.println(
                "INSERT: " + equipeTeste
        );

        System.out.println(
                "BUSCAR: "
                        + daoEquipe.buscarPorId(
                                equipeTeste.id()
                        )
        );

        System.out.println(
                "LISTAR:"
        );

        daoEquipe.listarTodas()
                .forEach(System.out::println);

        boolean equipeAtualizada =
                daoEquipe.atualizar(
                        equipeTeste.id(),
                        "Equipe CRUD Atualizada",
                        "Manutenção preventiva"
                );

        System.out.println(
                "UPDATE: " + equipeAtualizada
        );


        // =====================================================
        // CRUD - TRECHO
        // =====================================================

        System.out.println(
                "\n===== CRUD TRECHO ====="
        );

        TrechoRegistro trechoTeste =
                daoTrecho.inserir(
                        99,
                        22.0,
                        CondicaoCrescimento.CRESCIMENTO_MODERADO,
                        equipeTeste.id(),
                        "MONITORADO",
                        "SENSOR-CRUD-099"
                );

        System.out.println(
                "INSERT: " + trechoTeste
        );

        System.out.println(
                "BUSCAR: "
                        + daoTrecho.buscarPorId(
                                trechoTeste.id()
                        )
        );

        System.out.println(
                "LISTAR:"
        );

        daoTrecho.listarTodas()
                .forEach(System.out::println);

        boolean trechoAtualizado =
                daoTrecho.atualizar(
                        trechoTeste.id(),
                        99,
                        26.0,
                        CondicaoCrescimento.ALTO_CRESCIMENTO,
                        equipeTeste.id(),
                        "MONITORADO",
                        "SENSOR-CRUD-099"
                );

        System.out.println(
                "UPDATE: " + trechoAtualizado
        );


        // =====================================================
        // CRUD - INTERVENÇÃO
        // =====================================================

        System.out.println(
                "\n===== CRUD INTERVENÇÃO ====="
        );

        IntervencaoRegistro intervencaoTeste =
                daoIntervencao.inserir(
                        trechoTeste.id(),
                        "PULVERIZACAO",
                        26.0,
                        21.0
                );

        System.out.println(
                "INSERT: " + intervencaoTeste
        );

        System.out.println(
                "BUSCAR: "
                        + daoIntervencao.buscarPorId(
                                intervencaoTeste.id()
                        )
        );

        System.out.println(
                "LISTAR:"
        );

        daoIntervencao.listarTodas()
                .forEach(System.out::println);

        boolean intervencaoAtualizada =
                daoIntervencao.atualizar(
                        intervencaoTeste.id(),
                        trechoTeste.id(),
                        "ROCADA_MECANIZADA",
                        26.0,
                        8.0
                );

        System.out.println(
                "UPDATE: "
                        + intervencaoAtualizada
        );


        // =====================================================
        // CRUD - RELATÓRIO
        // =====================================================

        System.out.println(
                "\n===== CRUD RELATÓRIO ====="
        );

        RelatorioRegistro relatorioTeste =
                daoRelatorio.inserir(
                        1,
                        1,
                        1,
                        1,
                        "Relatório temporário para demonstração do CRUD."
                );

        System.out.println(
                "INSERT: " + relatorioTeste
        );

        System.out.println(
                "BUSCAR: "
                        + daoRelatorio.buscarPorId(
                                relatorioTeste.id()
                        )
        );

        System.out.println(
                "LISTAR:"
        );

        daoRelatorio.listarTodas()
                .forEach(System.out::println);

        boolean relatorioAtualizado =
                daoRelatorio.atualizar(
                        relatorioTeste.id(),
                        2,
                        1,
                        1,
                        0,
                        "Relatório temporário atualizado."
                );

        System.out.println(
                "UPDATE: "
                        + relatorioAtualizado
        );


        // =====================================================
        // DELETE
        // =====================================================
        // A ordem é importante por causa das FKs.
        // Primeiro removemos os registros dependentes.
        // =====================================================

        System.out.println(
                "\n===== DELETE DOS DADOS TEMPORÁRIOS ====="
        );

        boolean relatorioDeletado =
                daoRelatorio.deletar(
                        relatorioTeste.id()
                );

        System.out.println(
                "Relatório deletado: "
                        + relatorioDeletado
        );


        boolean intervencaoDeletada =
                daoIntervencao.deletar(
                        intervencaoTeste.id()
                );

        System.out.println(
                "Intervenção deletada: "
                        + intervencaoDeletada
        );


        boolean trechoDeletado =
                daoTrecho.deletar(
                        trechoTeste.id()
                );

        System.out.println(
                "Trecho deletado: "
                        + trechoDeletado
        );


        boolean equipeDeletada =
                daoEquipe.deletar(
                        equipeTeste.id()
                );

        System.out.println(
                "Equipe deletada: "
                        + equipeDeletada
        );

        System.out.println(
                "\nCRUDs demonstrados com sucesso."
        );
    }


    private static TrechoRodovia[] carregarTrechosDoBanco(
            EquipeManutencaoDAO daoEquipe,
            TrechoRodoviaDAO daoTrecho
    ) {

        Map<Long, EquipeManutencao> equipes =
                new HashMap<>();


        // Carregar equipes
        for (EquipeRegistro registro :
                daoEquipe.listarTodas()) {

            EquipeManutencao equipe =
                    new EquipeManutencao(
                            registro.nome(),
                            registro.especialidade()
                    );

            equipes.put(
                    registro.id(),
                    equipe
            );
        }


        // Carregar trechos
        List<TrechoRodovia> trechos =
                new ArrayList<>();

        for (TrechoRegistro registro :
                daoTrecho.listarTodas()) {

            EquipeManutencao equipe = null;

            if (registro.idEquipe() != null) {

                equipe =
                        equipes.get(
                                registro.idEquipe()
                        );
            }


            TrechoRodovia trecho;


            if ("MONITORADO".equals(
                    registro.tipoTrecho()
            )) {

                trecho =
                        new TrechoMonitorado(
                                registro.quilometro(),
                                registro.alturaVegetacao(),
                                registro.condicaoCrescimento(),
                                equipe,
                                registro.codigoSensor()
                        );

            } else {

                trecho =
                        new TrechoRodovia(
                                registro.quilometro(),
                                registro.alturaVegetacao(),
                                registro.condicaoCrescimento(),
                                equipe
                        );
            }


            trechos.add(trecho);
        }


        return trechos.toArray(
                new TrechoRodovia[0]
        );
    }
}