package com.ifrs.movimentaif.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Anamnese {
    private String anamneseId;
    private String userId;
    private Map<String, Boolean> respostas;
    private String observacoes;
    private Date dataCriacao;
    private Date dataAtualizacao;

    public Anamnese() {
        this.respostas = new HashMap<>();
        this.dataCriacao = new Date();
        this.dataAtualizacao = new Date();
        inicializarPerguntas();
    }

    public Anamnese(String anamneseId, String userId) {
        this.anamneseId = anamneseId;
        this.userId = userId;
        this.respostas = new HashMap<>();
        this.dataCriacao = new Date();
        this.dataAtualizacao = new Date();
        inicializarPerguntas();
    }

    private void inicializarPerguntas() {
        respostas.put("pergunta1", false);
        respostas.put("pergunta2", false);
        respostas.put("pergunta3", false);
        respostas.put("pergunta4", false);
        respostas.put("pergunta5", false);
        respostas.put("pergunta6", false);
        respostas.put("pergunta7", false);
    }

    public static String[] getPerguntas() {
        return new String[]{
            "Você já praticou atividade física regular anteriormente?",
            "Você possui ou teve alguma lesão muscular ou articular?",
            "Você fuma ou já fumou?",
            "Você consome bebidas alcoólicas regularmente?",
            "Você possui histórico familiar de doenças cardiovasculares?",
            "Você está grávida ou com suspeita de gravidez?",
            "Você possui alguma restrição alimentar ou alergia relevante para atividade física?"
        };
    }

    // Getters e Setters
    public String getAnamneseId() {
        return anamneseId;
    }

    public void setAnamneseId(String anamneseId) {
        this.anamneseId = anamneseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, Boolean> getRespostas() {
        return respostas;
    }

    public void setRespostas(Map<String, Boolean> respostas) {
        this.respostas = respostas;
        this.dataAtualizacao = new Date();
    }

    public Boolean getResposta(String pergunta) {
        return respostas.getOrDefault(pergunta, false);
    }

    public void setResposta(String pergunta, Boolean resposta) {
        this.respostas.put(pergunta, resposta);
        this.dataAtualizacao = new Date();
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
        this.dataAtualizacao = new Date();
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Date dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}
