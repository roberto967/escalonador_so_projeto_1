package com.app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.app.assets.escalonadores.Fcfs;
import com.app.assets.escalonadores.Rr;
import com.app.assets.escalonadores.Sjf;
import com.app.assets.exceptions.DuracaoExeption;
import com.app.assets.processos.Processo;

/*
 * 
 */

public abstract class App {
    public static void main(String[] args) {
        String localArquivo = "./in.csv";

        List<Processo> processos = new ArrayList<>();
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

                if (duracao == 0) {
                    throw new DuracaoExeption("Processo com duracao 0");
                }

                // Criar um novo objeto Processo e adicioná-lo ao ArrayList
                Processo processo = new Processo(entrada, duracao, id);
                processos.add(processo);
                linha = br.readLine();

                id++;
            }

            System.out.println(new Fcfs(processos).getMetricas());
            System.out.println(new Sjf(processos).getMetricas());
            System.out.println(new Rr(processos, 2).getMetricas());
        } catch (IOException e) {
            System.out.println("Arquivo nao encontrado!");
        } catch (DuracaoExeption e) {
            e.printStackTrace();
        }
    }
}
