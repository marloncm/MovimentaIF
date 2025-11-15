package com.ifrs.movimentaif.movimentaifapi.model;

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
    }

    public Anamnese(String anamneseId, String userId) {
        this.anamneseId = anamneseId;
        this.userId = userId;
        this.respostas = new HashMap<>();
        this.dataCriacao = new Date();
        this.dataAtualizacao = new Date();
    }

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
