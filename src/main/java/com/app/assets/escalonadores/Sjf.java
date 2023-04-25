package com.app.assets.escalonadores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.app.assets.processos.Processo;

public class Sjf extends Escalonador {
    private List<Processo> totalProcesos;

    public Sjf(List<Processo> processos) {
        this.totalProcesos = super.copiarLista(processos);

        Collections.sort(this.totalProcesos, Comparator.comparingInt(Processo::gettEntrada)
                .thenComparingInt(Processo::getDuracao));
    }

    protected void escalonar() {
        int tempoRetornoTotal = 0;
        int tempoRespostaTotal = 0;
        int tempoEsperaTotal = 0;
        int tempoAtual = 0;
        int nProcessos = totalProcesos.size();

        List<Processo> listaExecucao = new ArrayList<>();

        while (!totalProcesos.isEmpty() || !listaExecucao.isEmpty()) {
            /*
             * remove da lista de processos a serem executados e
             * adiciona à lista de execução todos os processos
             * que chegaram até o momento atual, que no caso é 0
             */
            while (!totalProcesos.isEmpty() && totalProcesos.get(0).gettEntrada() <= tempoAtual) {
                listaExecucao.add(totalProcesos.remove(0));
            }

            // Ordena a lista de execução pela duração dos processos
            Collections.sort(listaExecucao, Comparator.comparingInt(Processo::getDuracao));

            // Verifica se há processos na lista de execução
            if (!listaExecucao.isEmpty()) {
                // Executa o processo com a menor duração
                Processo p = listaExecucao.get(0);
                tempoAtual += p.getDuracao();

                // Marca o processo como respondido e registra o tempo de resposta
                p.setRespondido(true);
                p.settResposta(tempoAtual - p.gettEntrada() - p.getDuracao());
                p.setDuracaoRestante(0);

                // atualização das métricas
                int tempoRetorno = tempoAtual - p.gettEntrada();
                tempoRetornoTotal += tempoRetorno;

                int tempoResposta = tempoRetorno - p.getDuracao();
                tempoRespostaTotal += tempoResposta;

                // tempo de espera = tempo de resposta pois nao há entropia
                int tempoEspera = tempoResposta;
                tempoEsperaTotal += tempoEspera;

                // Remove da lista de execução os processos respondidos
                while (!listaExecucao.isEmpty() && listaExecucao.get(0).isRespondido()) {
                    listaExecucao.remove(0);
                }
            } else {
                // Não há processos na lista de execução, então avança o tempo para o próximo
                // processo
                tempoAtual = totalProcesos.get(0).gettEntrada();
            }
        }

        setRetornoMedio((float) tempoRetornoTotal / (float) nProcessos);
        setRespostaMedio((float) tempoRespostaTotal / (float) nProcessos);
        setEsperaMedio((float) tempoEsperaTotal / (float) nProcessos);
    }

    public String getMetricas() {
        this.escalonar();
        return super.getMetricas("SJF");
    }
}
