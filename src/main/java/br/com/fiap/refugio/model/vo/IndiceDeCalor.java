package br.com.fiap.refugio.model.vo;

import br.com.fiap.refugio.model.enums.NivelAlerta;

/**
 * Value Object do indice de calor (Rothfusz/NOAA) e o nivel de alerta
 * correspondente. Usado no agregado Alerta (colunas indice_calor + nivel).
 */
public class IndiceDeCalor {

    private Double valor;
    private NivelAlerta nivel;

    public IndiceDeCalor() {
    }

    public IndiceDeCalor(Double valor, NivelAlerta nivel) {
        this.valor = valor;
        this.nivel = nivel;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public NivelAlerta getNivel() {
        return nivel;
    }

    public void setNivel(NivelAlerta nivel) {
        this.nivel = nivel;
    }
}
