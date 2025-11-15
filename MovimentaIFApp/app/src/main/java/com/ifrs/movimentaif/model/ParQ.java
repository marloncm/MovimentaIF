package com.ifrs.movimentaif.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ParQ {
    private String parqId;
    private String userId;
    private Map<String, Boolean> respostas;
    private String observacoes;
    private Date dataCriacao;
    private Date dataAtualizacao;

    public ParQ() {
        this.respostas = new HashMap<>();
        this.dataCriacao = new Date();
        this.dataAtualizacao = new Date();
        inicializarPerguntas();
    }

    public ParQ(String parqId, String userId) {
        this.parqId = parqId;
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
            "Algum médico já disse que você possui algum problema cardíaco e que só deveria realizar atividade física supervisionado por profissionais de saúde?",
            "Você sente dores no peito quando pratica atividade física?",
            "No último mês, você sentiu dores no peito quando praticou atividade física?",
            "Você apresenta desequilíbrio devido à tontura e/ou perda de consciência?",
            "Você possui algum problema ósseo ou articular que poderia ser piorado pela atividade física?",
            "Você toma atualmente algum medicamento para pressão arterial e/ou problema cardíaco?",
            "Sabe de alguma outra razão pela qual você não deve praticar atividade física?"
        };
    }

    // Getters e Setters
    public String getParqId() {
        return parqId;
    }

    public void setParqId(String parqId) {
        this.parqId = parqId;
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
