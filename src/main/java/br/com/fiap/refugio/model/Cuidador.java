package br.com.fiap.refugio.model;

/**
 * Entidade CUIDADOR. Contato de confianca do cidadao - 1o nivel da escalada.
 */
public class Cuidador {

    private Long idCuidador;
    private String nome;
    private String telefone;
    private String parentesco;
    private Integer prioridade;
    private Long idCidadao;

    public Cuidador() {
    }

    public Cuidador(Long idCuidador, String nome, String telefone, String parentesco,
                    Integer prioridade, Long idCidadao) {
        this.idCuidador = idCuidador;
        this.nome = nome;
        this.telefone = telefone;
        this.parentesco = parentesco;
        this.prioridade = prioridade;
        this.idCidadao = idCidadao;
    }

    public Long getIdCuidador() {
        return idCuidador;
    }

    public void setIdCuidador(Long idCuidador) {
        this.idCuidador = idCuidador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public Integer getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Integer prioridade) {
        this.prioridade = prioridade;
    }

    public Long getIdCidadao() {
        return idCidadao;
    }

    public void setIdCidadao(Long idCidadao) {
        this.idCidadao = idCidadao;
    }
}
