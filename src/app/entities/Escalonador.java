package app.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public abstract class Escalonador {
    protected static List<Processo> copiarLista(List<Processo> original) {
        List<Processo> copia = new ArrayList<>();
        for (Processo processo : original) {
            copia.add(new Processo(processo));
        }
        return copia;
    }

    public static void fcfs(List<Processo> processos) {
        List<Processo> listaCopia = copiarLista(processos);
        int tempoRetornoTotal = 0;
        int tempoRespostaTotal = 0;
        int tempoEsperaTotal = 0;

        int tempoAtual = 0;

        for (Processo processo : listaCopia) {
            int tempoResposta = tempoAtual - processo.gettEntrada();
            tempoRespostaTotal += tempoResposta;

            int tempoEspera = tempoResposta;
            tempoEsperaTotal += tempoEspera;

            // tempo de retorno = tempo de término - tempo de chegada
            int tempoRetorno = tempoAtual + processo.getDuracao() - processo.gettEntrada();
            tempoRetornoTotal += tempoRetorno;

            tempoAtual += processo.getDuracao();
        }

        int nProcessos = processos.size();

        float tempoRetornoMedio = (float) tempoRetornoTotal / nProcessos;
        float tempoRespostaMedio = (float) tempoRespostaTotal / nProcessos;
        float tempoEsperaMedio = (float) tempoEsperaTotal / nProcessos;

        System.out.printf("FCFS: %.1f %.1f %.1f\n", tempoRetornoMedio, tempoRespostaMedio, tempoEsperaMedio);
    }

    public static void sjf(List<Processo> processos) {
        int tempoRetornoTotal = 0;
        int tempoRespostaTotal = 0;
        int tempoEsperaTotal = 0;
        int nProcessos = processos.size();

        List<Processo> listaExecucao = new ArrayList<>();
        List<Processo> bkp = copiarLista(processos);
        int tempoAtual = 0;

        // Ordena a lista de processos pelo tempo de entrada e pela duração
        Collections.sort(bkp, Comparator.comparingInt(Processo::gettEntrada)
                .thenComparingInt(Processo::getDuracao));

        while (!bkp.isEmpty() || !listaExecucao.isEmpty()) {
            /*
             * Adiciona à lista de execução todos os processos
             * que chegaram até o momento atual
             */
            while (!bkp.isEmpty() && bkp.get(0).gettEntrada() <= tempoAtual) {
                listaExecucao.add(bkp.remove(0));
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

                // atualização das métricas
                // tempo de retorno = tempo de término - tempo de chegada
                int tempoRetorno = tempoAtual - p.gettEntrada();
                tempoRetornoTotal += tempoRetorno;

                int tempoResposta = tempoRetorno - p.getDuracao();
                tempoRespostaTotal += tempoResposta;

                int tempoEspera = tempoResposta;
                tempoEsperaTotal += tempoEspera;

                // Remove da lista de execução os processos respondidos
                while (!listaExecucao.isEmpty() && listaExecucao.get(0).isRespondido()) {
                    listaExecucao.remove(0);
                }
            } else {
                // Não há processos na lista de execução, então avança o tempo para o próximo
                // processo
                tempoAtual = bkp.get(0).gettEntrada();
            }
        }

        float tempoRetornoMedio = (float) tempoRetornoTotal / nProcessos;
        float tempoRespostaMedio = (float) tempoRespostaTotal / nProcessos;
        float tempoEsperaMedio = (float) tempoEsperaTotal / nProcessos;

        System.out.printf("SJF: tretorno: %.1f tresposta: %.1f tEspera: %.1f\n", tempoRetornoMedio, tempoRespostaMedio,
                tempoEsperaMedio);
    }

    public static void rr(List<Processo> processos) {
        List<Processo> listaCopia = copiarLista(processos);
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

        listaCopia.forEach(processo -> {
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
