package br.com.fiap.refugio.model;

import java.time.LocalDateTime;

import br.com.fiap.refugio.model.enums.FonteLeitura;

/**
 * Entidade LEITURA_CLIMA. Leitura horaria de clima (INMET estacao A701 ou
 * sensor IoT). O indice de calor (Rothfusz/NOAA) e armazenado para historico.
 */
public class LeituraClima {

    private Long idLeitura;
    private LocalDateTime dataHora;
    private Double temperatura;
    private Double umidade;
    private Double indiceCalor;
    private FonteLeitura fonte;
    private Long idBairro;

    public LeituraClima() {
    }

    public LeituraClima(Long idLeitura, LocalDateTime dataHora, Double temperatura, Double umidade,
                        Double indiceCalor, FonteLeitura fonte, Long idBairro) {
        this.idLeitura = idLeitura;
        this.dataHora = dataHora;
        this.temperatura = temperatura;
        this.umidade = umidade;
        this.indiceCalor = indiceCalor;
        this.fonte = fonte;
        this.idBairro = idBairro;
    }

    public Long getIdLeitura() {
        return idLeitura;
    }

    public void setIdLeitura(Long idLeitura) {
        this.idLeitura = idLeitura;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public Double getUmidade() {
        return umidade;
    }

    public void setUmidade(Double umidade) {
        this.umidade = umidade;
    }

    public Double getIndiceCalor() {
        return indiceCalor;
    }

    public void setIndiceCalor(Double indiceCalor) {
        this.indiceCalor = indiceCalor;
    }

    public FonteLeitura getFonte() {
        return fonte;
    }

    public void setFonte(FonteLeitura fonte) {
        this.fonte = fonte;
    }

    public Long getIdBairro() {
        return idBairro;
    }

    public void setIdBairro(Long idBairro) {
        this.idBairro = idBairro;
    }
}
