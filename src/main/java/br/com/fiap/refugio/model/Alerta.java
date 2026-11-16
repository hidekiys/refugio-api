package br.com.fiap.refugio.model;

import java.time.LocalDateTime;

import br.com.fiap.refugio.model.vo.IndiceDeCalor;

/**
 * Entidade ALERTA. Alerta de calor emitido para um bairro. Agregado que
 * carrega o VO {@link IndiceDeCalor} (valor do indice + nivel).
 */
public class Alerta {

    private Long idAlerta;
    private IndiceDeCalor indiceCalor;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
    private Boolean ativo;
    private Long idBairro;
    private Long idLeitura;

    public Alerta() {
    }

    public Alerta(Long idAlerta, IndiceDeCalor indiceCalor, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim,
                  Boolean ativo, Long idBairro, Long idLeitura) {
        this.idAlerta = idAlerta;
        this.indiceCalor = indiceCalor;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.ativo = ativo;
        this.idBairro = idBairro;
        this.idLeitura = idLeitura;
    }

    public Long getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(Long idAlerta) {
        this.idAlerta = idAlerta;
    }

    public IndiceDeCalor getIndiceCalor() {
        return indiceCalor;
    }

    public void setIndiceCalor(IndiceDeCalor indiceCalor) {
        this.indiceCalor = indiceCalor;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Long getIdBairro() {
        return idBairro;
    }

    public void setIdBairro(Long idBairro) {
        this.idBairro = idBairro;
    }

    public Long getIdLeitura() {
        return idLeitura;
    }

    public void setIdLeitura(Long idLeitura) {
        this.idLeitura = idLeitura;
    }
}
