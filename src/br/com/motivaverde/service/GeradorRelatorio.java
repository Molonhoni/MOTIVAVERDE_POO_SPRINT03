package br.com.motivaverde.service;

import br.com.motivaverde.dao.RelatorioPrioridadeDAO;
import br.com.motivaverde.dao.RelatorioPrioridadeDAO.RelatorioRegistro;
import br.com.motivaverde.model.TrechoRodovia;
import br.com.motivaverde.service.MotorPrioridade.ResultadoPrioridades;

public class GeradorRelatorio {

    private final MotorPrioridade motorPrioridade;
    private final RelatorioPrioridadeDAO relatorioDAO;


    public GeradorRelatorio() {

        this.motorPrioridade =
                new MotorPrioridade();

        this.relatorioDAO =
                new RelatorioPrioridadeDAO();
    }


    public void gerarRelatorio(
            TrechoRodovia[] trechos
    ) {

        ResultadoPrioridades resultado =
                motorPrioridade
                        .gerarRelatorioPrioridade(trechos);


        String resumo =
                "Relatório Motiva Verde | "
                        + "Baixa: " + resultado.qtBaixa()
                        + " | Moderada: " + resultado.qtModerada()
                        + " | Alta: " + resultado.qtAlta()
                        + " | Urgente: " + resultado.qtUrgente();


        RelatorioRegistro relatorioSalvo =
                relatorioDAO.salvarRelatorio(
                        resultado.qtBaixa(),
                        resultado.qtModerada(),
                        resultado.qtAlta(),
                        resultado.qtUrgente(),
                        resumo
                );


        System.out.println(
                "\n===== PERSISTÊNCIA DO RELATÓRIO ====="
        );

        System.out.println(
                "Relatório salvo no banco com sucesso!"
        );

        System.out.println(
                "ID do relatório: "
                        + relatorioSalvo.id()
        );

        System.out.println(
                "Data de geração: "
                        + relatorioSalvo.dataGeracao()
        );
    }
}