package br.com.fiap.refugio.model;

import java.time.LocalDateTime;

import br.com.fiap.refugio.model.enums.StatusCheckin;

/**
 * Entidade CHECKIN. Ciclo de check-in "To bem" e rastreamento da escalada
 * (coracao da regra de negocio do Refugio).
 */
public class Checkin {

    private Long idCheckin;
    private LocalDateTime dataHoraSolicitacao;
    private LocalDateTime dataHoraResposta;
    private StatusCheckin status;
    private LocalDateTime dataHoraContatoCuidador;
    private LocalDateTime dataHoraContatoAgente;
    private Long idCidadao;
    private Long idAlerta;

    public Checkin() {
    }

    public Checkin(Long idCheckin, LocalDateTime dataHoraSolicitacao, LocalDateTime dataHoraResposta,
                   StatusCheckin status, LocalDateTime dataHoraContatoCuidador, LocalDateTime dataHoraContatoAgente,
                   Long idCidadao, Long idAlerta) {
        this.idCheckin = idCheckin;
        this.dataHoraSolicitacao = dataHoraSolicitacao;
        this.dataHoraResposta = dataHoraResposta;
        this.status = status;
        this.dataHoraContatoCuidador = dataHoraContatoCuidador;
        this.dataHoraContatoAgente = dataHoraContatoAgente;
        this.idCidadao = idCidadao;
        this.idAlerta = idAlerta;
    }

    public Long getIdCheckin() {
        return idCheckin;
    }

    public void setIdCheckin(Long idCheckin) {
        this.idCheckin = idCheckin;
    }

    public LocalDateTime getDataHoraSolicitacao() {
        return dataHoraSolicitacao;
    }

    public void setDataHoraSolicitacao(LocalDateTime dataHoraSolicitacao) {
        this.dataHoraSolicitacao = dataHoraSolicitacao;
    }

    public LocalDateTime getDataHoraResposta() {
        return dataHoraResposta;
    }

    public void setDataHoraResposta(LocalDateTime dataHoraResposta) {
        this.dataHoraResposta = dataHoraResposta;
    }

    public StatusCheckin getStatus() {
        return status;
    }

    public void setStatus(StatusCheckin status) {
        this.status = status;
    }

    public LocalDateTime getDataHoraContatoCuidador() {
        return dataHoraContatoCuidador;
    }

    public void setDataHoraContatoCuidador(LocalDateTime dataHoraContatoCuidador) {
        this.dataHoraContatoCuidador = dataHoraContatoCuidador;
    }

    public LocalDateTime getDataHoraContatoAgente() {
        return dataHoraContatoAgente;
    }

    public void setDataHoraContatoAgente(LocalDateTime dataHoraContatoAgente) {
        this.dataHoraContatoAgente = dataHoraContatoAgente;
    }

    public Long getIdCidadao() {
        return idCidadao;
    }

    public void setIdCidadao(Long idCidadao) {
        this.idCidadao = idCidadao;
    }

    public Long getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(Long idAlerta) {
        this.idAlerta = idAlerta;
    }
}
