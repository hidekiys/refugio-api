package br.com.fiap.refugio.model;

import java.time.LocalDate;

import br.com.fiap.refugio.model.enums.TipoCidadao;
import br.com.fiap.refugio.model.vo.Coordenada;

/**
 * Entidade CIDADAO. Pessoa monitorada (idoso que mora sozinho / trabalhador
 * de rua). Agregado que carrega o VO {@link Coordenada}.
 */
public class Cidadao {

    private Long idCidadao;
    private String nome;
    private LocalDate dataNascimento;
    private String telefone;
    private TipoCidadao tipo;
    private Coordenada coordenada;
    private String endereco;
    private Boolean ativo;
    private Long idBairro;

    public Cidadao() {
    }

    public Cidadao(Long idCidadao, String nome, LocalDate dataNascimento, String telefone, TipoCidadao tipo,
                   Coordenada coordenada, String endereco, Boolean ativo, Long idBairro) {
        this.idCidadao = idCidadao;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.tipo = tipo;
        this.coordenada = coordenada;
        this.endereco = endereco;
        this.ativo = ativo;
        this.idBairro = idBairro;
    }

    public Long getIdCidadao() {
        return idCidadao;
    }

    public void setIdCidadao(Long idCidadao) {
        this.idCidadao = idCidadao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public TipoCidadao getTipo() {
        return tipo;
    }

    public void setTipo(TipoCidadao tipo) {
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
}
