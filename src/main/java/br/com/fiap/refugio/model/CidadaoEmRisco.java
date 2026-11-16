package br.com.fiap.refugio.model;

import java.time.LocalDateTime;

/**
 * Read-model (projecao) da "consulta de valor" do Refugio: idoso sem resposta
 * de check-in em bairro com alerta VERMELHO ativo. Nao e uma tabela - resulta
 * do JOIN entre CHECKIN, CIDADAO, BAIRRO e ALERTA.
 */
public class CidadaoEmRisco {

    private String bairro;
    private String idoso;
    private String telefone;
    private String nivelAlerta;
    private LocalDateTime dataHoraSolicitacao;
    private String status;

    public CidadaoEmRisco() {
    }

    public CidadaoEmRisco(String bairro, String idoso, String telefone, String nivelAlerta,
                          LocalDateTime dataHoraSolicitacao, String status) {
        this.bairro = bairro;
        this.idoso = idoso;
        this.telefone = telefone;
        this.nivelAlerta = nivelAlerta;
        this.dataHoraSolicitacao = dataHoraSolicitacao;
        this.status = status;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getIdoso() {
        return idoso;
    }

    public void setIdoso(String idoso) {
        this.idoso = idoso;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getNivelAlerta() {
        return nivelAlerta;
    }

    public void setNivelAlerta(String nivelAlerta) {
        this.nivelAlerta = nivelAlerta;
    }

    public LocalDateTime getDataHoraSolicitacao() {
        return dataHoraSolicitacao;
    }

    public void setDataHoraSolicitacao(LocalDateTime dataHoraSolicitacao) {
        this.dataHoraSolicitacao = dataHoraSolicitacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
