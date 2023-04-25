package com.app;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.junit.Test;

import com.app.assets.escalonadores.Fcfs;
import com.app.assets.escalonadores.Rr;
import com.app.assets.escalonadores.Sjf;
import com.app.assets.processos.Processo;

public class TesteEscalonadores {
    public List<Processo> popular(String localArquivo) {
        List<Processo> saida = new ArrayList<>();
        int id = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(localArquivo))) {
            String linha = br.readLine();
            // Ler cada linha do arquivo
            while (linha != null) {
                // Separar os valores da linha em campos
                StringTokenizer tokenizer = new StringTokenizer(linha, " ");

                // Ler os valores dos campos
                int entrada = Integer.parseInt(tokenizer.nextToken());
                int duracao = Integer.parseInt(tokenizer.nextToken());

                // Criar um novo objeto Processo e adicioná-lo ao ArrayList
                Processo processo = new Processo(entrada, duracao, id);
                saida.add(processo);
                linha = br.readLine();

                id++;
            }
        } catch (IOException e) {
            System.out.println("Arquivo não encontrado!");
        }

        return saida;
    }

    @Test
    public void teste1() {
        List<Processo> processos = popular("./src/test/inputs/in1.csv");

        String saidaTeste = new Fcfs(processos).getMetricas() + "\n" + new Sjf(processos).getMetricas() + "\n"
                + new Rr(processos, 2).getMetricas();

        String correto = "FCFS 93,7 77,8 77,8\nSJF 46,9 31,0 31,0\nRR 60,4 8,6 44,5";

        assertEquals(correto, saidaTeste);
    }

    @Test
    public void teste2() {
        List<Processo> processos = popular("./src/test/inputs/in2.csv");

        String saidaTeste = new Fcfs(processos).getMetricas() + "\n" + new Sjf(processos).getMetricas() + "\n"
                + new Rr(processos, 2).getMetricas();

        String correto = "FCFS 30,5 19,5 19,5\nSJF 21,5 10,5 10,5\nRR 31,5 2,0 20,5";

        assertEquals(correto, saidaTeste);
    }

    @Test
    public void teste3() {
        List<Processo> processos = popular("./src/test/inputs/in3.csv");

        String saidaTeste = new Fcfs(processos).getMetricas() + "\n" + new Sjf(processos).getMetricas() + "\n"
                + new Rr(processos, 2).getMetricas();

        String correto = "FCFS 31,5 20,5 20,5\nSJF 22,5 11,5 11,5\nRR 31,5 2,0 20,5";

        assertEquals(correto, saidaTeste);
    }

    @Test
    public void teste4() {
        List<Processo> processos = popular("./src/test/inputs/in4.csv");

        String saidaTeste = new Fcfs(processos).getMetricas() + "\n" + new Sjf(processos).getMetricas() + "\n"
                + new Rr(processos, 2).getMetricas();

        String correto = "FCFS 12,5 6,0 6,0\nSJF 12,5 6,0 6,0\nRR 15,5 1,0 9,0";

        assertEquals(correto, saidaTeste);
    }
}
