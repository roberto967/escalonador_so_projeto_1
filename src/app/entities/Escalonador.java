package app.entities;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public abstract class Escalonador {
    public static void fcfs(List<Processo> processos) {
        float tempoRetornoTotal = 0;
        float tempoRespostaTotal = 0;
        float tempoEsperaTotal = 0;

        float tempoAtual = 0;

        for (Processo processo : processos) {
            float tempoResposta = tempoAtual - processo.gettEntrada();
            tempoRespostaTotal += tempoResposta;

            float tempoEspera = tempoResposta;
            tempoEsperaTotal += tempoEspera;

            // tempo de retorno = tempo de término - tempo de chegada
            float tempoRetorno = tempoAtual + processo.getDuracao() - processo.gettEntrada();
            tempoRetornoTotal += tempoRetorno;

            tempoAtual += processo.getDuracao();
        }

        int tam = processos.size();

        float tempoRetornoMedio = tempoRetornoTotal / tam;
        float tempoRespostaMedio = tempoRespostaTotal / tam;
        float tempoEsperaMedio = tempoEsperaTotal / tam;

        System.out.printf("FCFS: %.1f %.1f %.1f\n", tempoRetornoMedio, tempoRespostaMedio, tempoEsperaMedio);
    }

    public static void sjf(List<Processo> processos) {

        // System.out.printf("sjf: %.2f %.2f %.2f\n", tempoRetornoMedio,
        // tempoRespostaMedio, tempoEsperaMedio);
    }

    public static void rr(List<Processo> processos) {
        int quantum = 2;

        int tRetornoTotal = 0;
        int tRespostaTotal = 0;
        int tEsperaTotal = 0;
        int retorno = 0;
        int nProcessos = processos.size();

        // Lista de processos que estão em execução no momento
        List<Processo> listaExecucao = new LinkedList<>();
        // Lista de processos que ainda não chegaram, ordenados pelo tempo de chegada
        PriorityQueue<Processo> listaChegada = new PriorityQueue<>(Comparator.comparingInt(Processo::gettEntrada));

        processos.forEach(processo -> {
            if (processo.gettEntrada() == 0) {
                listaExecucao.add(processo);
            } else {
                listaChegada.add(processo);
            }
        });

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
                retorno = Math.max(retorno, p.gettEntrada());
                listaExecucao.add(p);
            }

            Processo p = listaExecucao.remove(0);

            // Guarda o tempo que o processo foi atendido pela primeira vez
            if (!p.isRespondido()) {
                p.setRespondido(true);
                p.settResposta(retorno - p.gettEntrada());
            }

            /*
             * verifica se o processo ainda tem duração restante maior que o quantum,
             * se sim, significa que ele nao vai ser finalizado então ele atualiza a lista
             * com os processos que chegaram durante a execução do atual e coloca o atual no
             * fim da fila
             */
            if (p.getDuracaoRestante() > quantum) {
                p.setDuracaoRestante(p.getDuracaoRestante() - quantum);
                retorno += quantum;

                /*
                 * verifica se ainda tem processo pra chegar e se tiver ele coloca na fila os
                 * processos cujo o tempo de chegada é menor ou igual ao tempo atual de retorno
                 */
                while (!listaChegada.isEmpty() && listaChegada.peek().gettEntrada() <= retorno) {
                    listaExecucao.add(listaChegada.poll());
                }

                listaExecucao.add(p);
            } else {
                /*
                 * se entrou aqui significa que o processo foi finalizado, então ele atualiza o
                 * tempo de retorno atual com o tempo de duração restante do processo e atualiza
                 * os totais.
                 */
                retorno += p.getDuracaoRestante();

                tRetornoTotal += retorno - p.gettEntrada();
                tRespostaTotal += p.gettResposta();
                tEsperaTotal += retorno - p.getDuracao() - p.gettEntrada();

                while (!listaChegada.isEmpty() && listaChegada.peek().gettEntrada() <= retorno) {
                    listaExecucao.add(listaChegada.poll());
                }
            }
        }

        float tempoRetornoMedio = (float) tRetornoTotal / (float) nProcessos;
        float tempoRespostaMedio = (float) tRespostaTotal / (float) nProcessos;
        float tempoEsperaMedio = (float) tEsperaTotal / (float) nProcessos;

        System.out.printf("RR: %.1f %.1f %.1f\n", tempoRetornoMedio, tempoRespostaMedio, tempoEsperaMedio);
    }

}
