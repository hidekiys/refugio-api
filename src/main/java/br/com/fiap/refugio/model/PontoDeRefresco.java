package br.com.fiap.refugio.model;

import br.com.fiap.refugio.model.enums.TipoPonto;
import br.com.fiap.refugio.model.vo.Coordenada;

/**
 * Entidade PONTO_REFRESCO. Local de alivio do calor (UBS, CEU, comercio
 * parceiro). Agregado que carrega o VO {@link Coordenada}.
 */
public class PontoDeRefresco {

    private Long idPonto;
    private String nome;
    private TipoPonto tipo;
    private Coordenada coordenada;
    private String endereco;
    private Boolean climatizado;
    private Boolean temBebedouro;
    private Integer capacidade;
    private Long idBairro;

    public PontoDeRefresco() {
    }

    public PontoDeRefresco(Long idPonto, String nome, TipoPonto tipo, Coordenada coordenada, String endereco,
                           Boolean climatizado, Boolean temBebedouro, Integer capacidade, Long idBairro) {
        this.idPonto = idPonto;
        this.nome = nome;
        this.tipo = tipo;
        this.coordenada = coordenada;
        this.endereco = endereco;
        this.climatizado = climatizado;
        this.temBebedouro = temBebedouro;
        this.capacidade = capacidade;
        this.idBairro = idBairro;
    }

    public Long getIdPonto() {
        return idPonto;
    }

    public void setIdPonto(Long idPonto) {
        this.idPonto = idPonto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoPonto getTipo() {
        return tipo;
    }

    public void setTipo(TipoPonto tipo) {
        this.tipo = tipo;
    }

    public Coordenada getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(Coordenada coordenada) {
        this.coordenada = coordenada;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Boolean getClimatizado() {
        return climatizado;
    }

    public void setClimatizado(Boolean climatizado) {
        this.climatizado = climatizado;
    }

    public Boolean getTemBebedouro() {
        return temBebedouro;
    }

    public void setTemBebedouro(Boolean temBebedouro) {
        this.temBebedouro = temBebedouro;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public Long getIdBairro() {
        return idBairro;
    }

    public void setIdBairro(Long idBairro) {
        this.idBairro = idBairro;
    }
}
