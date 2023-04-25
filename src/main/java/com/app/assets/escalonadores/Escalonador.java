package com.app.assets.escalonadores;

import java.util.ArrayList;
import java.util.List;

import com.app.assets.processos.Processo;

public abstract class Escalonador {
    private float retornoMedio;
    private float respostaMedio;
    private float esperaMedio;

    protected float getRetornoMedio() {
        return retornoMedio;
    }

    protected void setRetornoMedio(float retornoMedio) {
        this.retornoMedio = retornoMedio;
    }

    protected float getRespostaMedio() {
        return respostaMedio;
    }

    protected void setRespostaMedio(float respostaMedio) {
        this.respostaMedio = respostaMedio;
    }

    protected float getEsperaMedio() {
        return esperaMedio;
    }

    protected void setEsperaMedio(float esperaMedio) {
        this.esperaMedio = esperaMedio;
    }

    protected abstract void escalonar();

    protected String getMetricas(String siglaEscalonador) {
        return String.format("%s %.1f %.1f %.1f", siglaEscalonador, getRetornoMedio(), getRespostaMedio(),
                getEsperaMedio());
    }

    protected List<Processo> copiarLista(List<Processo> original) {
        List<Processo> copia = new ArrayList<>();
        for (Processo processo : original) {
            copia.add(new Processo(processo));
        }
        return copia;
    }
}