package com.ifrs.movimentaif.movimentaifapi.model;

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
    }

    public ParQ(String parqId, String userId) {
        this.parqId = parqId;
        this.userId = userId;
        this.respostas = new HashMap<>();
        this.dataCriacao = new Date();
        this.dataAtualizacao = new Date();
    }

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
