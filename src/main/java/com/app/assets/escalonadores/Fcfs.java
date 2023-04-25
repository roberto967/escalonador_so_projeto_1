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

        int tempoAtual = 0;

        // verifica se o tempo atual é menor que o tempo de entrada do primeiro processo
        // se sim avança o tempo
        if (totalProcessos.get(0).gettEntrada() > tempoAtual) {
            tempoAtual = totalProcessos.get(0).gettEntrada();
        }

        for (Processo p : totalProcessos) {
            int tempoResposta = tempoAtual - p.gettEntrada();
            tempoRespostaTotal += tempoResposta;

            int tempoEspera = tempoResposta;
            tempoEsperaTotal += tempoEspera;

            // tempo de retorno = tempo de término - tempo de chegada
            int tempoRetorno = tempoAtual + p.getDuracao() - p.gettEntrada();
            tempoRetornoTotal += tempoRetorno;

            // Marca o processo como respondido e registra o tempo de resposta
            p.setRespondido(true);
            p.settResposta(tempoAtual - p.gettEntrada() - p.getDuracao());
            p.setDuracaoRestante(0);

            tempoAtual += p.getDuracao();
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
