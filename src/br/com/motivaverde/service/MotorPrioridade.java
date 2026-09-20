package br.com.motivaverde.service;

import br.com.motivaverde.interfaces.MonitoravelViaIoT;
import br.com.motivaverde.intervencao.IntervencaoOperacional;
import br.com.motivaverde.intervencao.Pulverizacao;
import br.com.motivaverde.intervencao.RocadaMecanizada;
import br.com.motivaverde.model.PrioridadeIntervencao;
import br.com.motivaverde.model.TrechoRodovia;

public class MotorPrioridade {

    private static final double LIMITE_PRIORIDADE_BAIXA = 15.0;
    private static final double LIMITE_PRIORIDADE_MODERADA = 20.0;
    private static final double LIMITE_PRIORIDADE_ALTA = 29.0;
    private static final double LIMITE_PRIORIDADE_URGENTE = 30.0;

    public ResultadoPrioridades gerarRelatorioPrioridade(TrechoRodovia[] trechos) {

        System.out.println(
                "===== RELATÓRIO DE PRIORIDADE - MOTIVA VERDE ====="
        );

        int qtBaixa = 0;
        int qtModerada = 0;
        int qtAlta = 0;
        int qtUrgente = 0;

        for (TrechoRodovia trecho : trechos) {

            atualizarDadosSeForMonitoravel(trecho);

            PrioridadeIntervencao prioridade =
                    analisarTrecho(trecho);

            switch (prioridade) {

                case BAIXA:
                    qtBaixa++;
                    break;

                case MODERADA:
                    qtModerada++;
                    break;

                case ALTA:
                    qtAlta++;
                    break;

                case URGENTE:
                    qtUrgente++;
                    break;
            }
        }

        return new ResultadoPrioridades(
                qtBaixa,
                qtModerada,
                qtAlta,
                qtUrgente
        );
    }


    private void atualizarDadosSeForMonitoravel(
            TrechoRodovia trecho
    ) {

        if (trecho instanceof MonitoravelViaIoT) {

            MonitoravelViaIoT trechoMonitorado =
                    (MonitoravelViaIoT) trecho;

            double dadoSensor =
                    trechoMonitorado.transmitirDadosSensor();

            System.out.println(
                    "\nDados recebidos via IoT no KM "
                            + trecho.getQuilometro()
                            + ": "
                            + dadoSensor
                            + " cm"
            );
        }
    }


    private PrioridadeIntervencao analisarTrecho(
            TrechoRodovia trecho
    ) {

        System.out.println(
                "\n" + trecho.exibirResumo()
        );

        PrioridadeIntervencao prioridade =
                definirPrioridade(trecho);

        IntervencaoOperacional intervencao =
                definirIntervencao(trecho);

        System.out.println(
                "Prioridade: " + prioridade
        );

        if (intervencao != null) {

            System.out.println(
                    "Intervenção indicada: "
                            + intervencao.getNomeIntervencao()
            );

            intervencao.executarServico(trecho);

        } else {

            System.out.println(
                    "Intervenção indicada: nenhuma intervenção imediata."
            );
        }

        System.out.println(
                "Altura após análise/intervenção: "
                        + trecho.getAlturaVegetacao()
                        + " cm"
        );

        System.out.println(
                "---------------------------------------------"
        );

        return prioridade;
    }


    private PrioridadeIntervencao definirPrioridade(
            TrechoRodovia trecho
    ) {

        double altura =
                trecho.getAlturaVegetacao();

        if (altura >= LIMITE_PRIORIDADE_URGENTE) {
            return PrioridadeIntervencao.URGENTE;
        }

        if (altura >= 21.0
                && altura <= LIMITE_PRIORIDADE_ALTA) {

            return PrioridadeIntervencao.ALTA;
        }

        if (altura > LIMITE_PRIORIDADE_BAIXA
                && altura <= LIMITE_PRIORIDADE_MODERADA) {

            return PrioridadeIntervencao.MODERADA;
        }

        return PrioridadeIntervencao.BAIXA;
    }


    private IntervencaoOperacional definirIntervencao(
            TrechoRodovia trecho
    ) {

        double altura =
                trecho.getAlturaVegetacao();

        if (altura >= LIMITE_PRIORIDADE_URGENTE) {
            return new RocadaMecanizada();
        }

        if (altura >= 21.0
                && altura <= LIMITE_PRIORIDADE_ALTA) {

            return new Pulverizacao();
        }

        return null;
    }


    public record ResultadoPrioridades(
            int qtBaixa,
            int qtModerada,
            int qtAlta,
            int qtUrgente
    ) {
    }
}