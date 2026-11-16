package br.com.fiap.refugio.model;

/**
 * Entidade BAIRRO. Eixo que liga clima, alertas, cidadaos e pontos de refresco.
 */
public class Bairro {

    private Long idBairro;
    private String nome;
    private String cidade;
    private String uf;
    private String codigoIbge;
    private String zona;

    public Bairro() {
    }

    public Bairro(Long idBairro, String nome, String cidade, String uf, String codigoIbge, String zona) {
        this.idBairro = idBairro;
        this.nome = nome;
        this.cidade = cidade;
        this.uf = uf;
        this.codigoIbge = codigoIbge;
        this.zona = zona;
    }

    public Long getIdBairro() {
        return idBairro;
    }

    public void setIdBairro(Long idBairro) {
        this.idBairro = idBairro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCodigoIbge() {
        return codigoIbge;
    }

    public void setCodigoIbge(String codigoIbge) {
        this.codigoIbge = codigoIbge;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }
}
