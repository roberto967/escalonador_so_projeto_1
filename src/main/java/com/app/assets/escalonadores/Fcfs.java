package com.app.assets.escalonadores;

import java.util.List;

import com.app.assets.processos.Processo;

public class Fcfs extends Escalonador {
    private List<Processo> totalProcessos;

    public Fcfs(List<Processo> processos) {
        this.totalProcessos = super.copiarLista(processos);
    }

    protected void escalonar() {
        int tempoRetornoTotal = 0;
        int tempoRespostaTotal = 0;
        int tempoEsperaTotal = 0;
        int nProcessos = totalProcessos.size();
        System.out.println(nProcessos);

        int tempoAtual = 0;

        // verifica se o tempo atual é menor que o tempo de entrada do primeiro processo
        // se sim avança o tempo
        if (totalProcessos.get(0).gettEntrada() > tempoAtual) {
            tempoAtual = totalProcessos.get(0).gettEntrada();
        }

        for (Processo p : totalProcessos) {
            tempoAtual += p.getDuracao();

            // atualização das métricas
            int tempoRetorno = tempoAtual - p.gettEntrada();
            tempoRetornoTotal += tempoRetorno;

            /*
             * diferença entre o tempo que começou a ser executado e o tempo que chegou ao
             * sistema, como o tempoAtual já avançou para o fim do processo é necessário
             * subtraí-lo no calculo
             */
            int tempoResposta = tempoAtual - p.gettEntrada() - p.getDuracao();
            tempoRespostaTotal += tempoResposta;

            // tempo de espera = tempo de resposta pois nao há entropia
            int tempoEspera = tempoResposta;
            tempoEsperaTotal += tempoEspera;
        }

        float tempoRetornoMedio = (float) tempoRetornoTotal / nProcessos;
        float tempoRespostaMedio = (float) tempoRespostaTotal / nProcessos;
        float tempoEsperaMedio = (float) tempoEsperaTotal / nProcessos;

        super.setRetornoMedio(tempoRetornoMedio);
        super.setRespostaMedio(tempoRespostaMedio);
        super.setEsperaMedio(tempoEsperaMedio);
    }

    public String getMetricas() {
        this.escalonar();
        return super.getMetricas("FCFS");
    }
}
