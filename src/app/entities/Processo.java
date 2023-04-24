package app.entities;

public class Processo {
    private int tEntrada;
    private int duracao;
    private int duracaoRestante;
    private int id;
    private boolean respondido;
    private int tResposta;

    public Processo(int entrada, int duracao, int id) {
        this.tEntrada = entrada;
        this.duracao = duracao;
        this.duracaoRestante = duracao;
        this.id = id;
        this.respondido = false;
        this.tResposta = 0;
    }

    public int gettEntrada() {
        return this.tEntrada;
    }

    public int getDuracao() {
        return this.duracao;
    }

    public int getDuracaoRestante() {
        return this.duracaoRestante;
    }

    public void setDuracaoRestante(int duracaoRestante) {
        this.duracaoRestante = duracaoRestante;
    }

    public int getId() {
        return this.id;
    }

    public boolean isRespondido() {
        return this.respondido;
    }

    public void setRespondido(boolean respondido) {
        this.respondido = respondido;
    }

    public int gettResposta() {
        return this.tResposta;
    }

    public void settResposta(int tResposta) {
        this.tResposta = tResposta;
    }

    @Override
    public String toString() {
        return new String(
                "| ID: " + this.id + " (" + this.tEntrada + " " + this.duracao + ")| \n STATUS: " + isRespondido());
    }

    public Processo(Processo processo) {
        this.tEntrada = processo.tEntrada;
        this.duracao = processo.duracao;
        this.duracaoRestante = processo.duracaoRestante;
        this.id = processo.id;
        this.respondido = processo.respondido;
        this.tResposta = processo.tResposta;
    }
}