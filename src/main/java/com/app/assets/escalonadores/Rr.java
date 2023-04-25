package com.app.assets.escalonadores;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import com.app.assets.processos.Processo;

public class Rr extends Escalonador {
    // Lista de processos que estão em execução no momento
    List<Processo> listaExecucao = new LinkedList<>();
    // Lista de processos que ainda não chegaram, ordenados pelo tempo de chegada
    PriorityQueue<Processo> listaChegada = new PriorityQueue<>(Comparator.comparingInt(Processo::gettEntrada));

    private int quantum;
    private int nProcessos;

    public Rr(List<Processo> processos, int quantum) {
        List<Processo> totalProcesos = super.copiarLista(processos);

        totalProcesos.forEach(processo -> {
            if (processo.gettEntrada() == 0) {
                listaExecucao.add(processo);
            } else {
                listaChegada.add(processo);
            }
        });

        this.quantum = quantum;
        this.nProcessos = totalProcesos.size();
    }

    protected void escalonar() {
        int tempoRetornoTotal = 0;
        int tempoRespostaTotal = 0;
        int tempoEsperaTotal = 0;

        int tempoAtual = 0;

        /*
         * roda enquando ainda houverem processos executando e processos a serem
         * executados
         */
        while (!listaExecucao.isEmpty() || !listaChegada.isEmpty()) {
            /*
             * se a lista de execução tiver vazia ele retira o primeiro elemento da lista de
             * chegada
             */
            if (listaExecucao.isEmpty()) {
                Processo p = listaChegada.poll();
                tempoAtual = Math.max(tempoAtual, p.gettEntrada());
                listaExecucao.add(p);
            }

            Processo p = listaExecucao.remove(0);

            // Guarda o tempo que o processo foi atendido pela primeira vez
            if (!p.isRespondido()) {
                p.setRespondido(true);
                p.settResposta(tempoAtual - p.gettEntrada());
            }

            /*
             * verifica se o processo ainda tem duração restante maior que o quantum,
             * se sim, significa que ele nao vai ser finalizado então ele atualiza a lista
             * com os processos que chegaram durante a execução do atual e coloca o atual no
             * fim da fila
             */
            if (p.getDuracaoRestante() > quantum) {
                p.setDuracaoRestante(p.getDuracaoRestante() - quantum);
                tempoAtual += quantum;

                /*
                 * verifica se ainda tem processo pra chegar e se tiver ele coloca na fila os
                 * processos cujo o tempo de chegada é menor ou igual ao tempo atual
                 */
                while (!listaChegada.isEmpty() && listaChegada.peek().gettEntrada() <= tempoAtual) {
                    listaExecucao.add(listaChegada.poll());
                }

                listaExecucao.add(p);
            } else {
                /*
                 * se entrou aqui significa que o processo foi finalizado, então ele atualiza o
                 * tempo de retorno atual com o tempo de duração restante do processo e atualiza
                 * os totais.
                 */
                tempoAtual += p.getDuracaoRestante();
                int tempoRetorno = tempoAtual - p.gettEntrada();
                int tempoResposta = p.gettResposta();
                int tempoEspera = tempoAtual - p.getDuracao() - p.gettEntrada();

                tempoRetornoTotal += tempoRetorno;
                tempoRespostaTotal += tempoResposta;
                tempoEsperaTotal += tempoEspera;

                while (!listaChegada.isEmpty() && listaChegada.peek().gettEntrada() <= tempoAtual) {
                    listaExecucao.add(listaChegada.poll());
                }
            }
        }

        setRetornoMedio((float) tempoRetornoTotal / (float) nProcessos);
        setRespostaMedio((float) tempoRespostaTotal / (float) nProcessos);
        setEsperaMedio((float) tempoEsperaTotal / (float) nProcessos);
    }

    public String getMetricas() {
        this.escalonar();
        return super.getMetricas("RR");
    }
}
