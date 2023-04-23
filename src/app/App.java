package app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import app.entities.Escalonador;
import app.entities.Processo;

public abstract class App {
    public static void main(String[] args) {
        String arquivo = "./in.csv";

        List<Processo> processos = new ArrayList<>();
        int id = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
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
                processos.add(processo);
                linha = br.readLine();

                id++;
            }
        } catch (IOException e) {
            System.out.println("Arquivo não encontrado!");
        }

        // System.out.println(new String(processos.toString()));
        //Escalonador.fcfs(processos);
        Escalonador.sjf(processos);
        //Escalonador.rr(processos);
    }
}