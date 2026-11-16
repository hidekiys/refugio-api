package br.com.fiap.refugio.model;

/**
 * Entidade AGENTE_SAUDE. Agente de saude da UBS - 2o nivel da escalada.
 */
public class AgenteSaude {

    private Long idAgente;
    private String nome;
    private String telefone;
    private String cargo;
    private Long idBairro;
    private Long idPonto;

    public AgenteSaude() {
    }

    public AgenteSaude(Long idAgente, String nome, String telefone, String cargo,
                       Long idBairro, Long idPonto) {
        this.idAgente = idAgente;
        this.nome = nome;
        this.telefone = telefone;
        this.cargo = cargo;
        this.idBairro = idBairro;
        this.idPonto = idPonto;
    }

    public Long getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(Long idAgente) {
        this.idAgente = idAgente;
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

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Long getIdBairro() {
        return idBairro;
    }

    public void setIdBairro(Long idBairro) {
        this.idBairro = idBairro;
    }

    public Long getIdPonto() {
        return idPonto;
    }

    public void setIdPonto(Long idPonto) {
        this.idPonto = idPonto;
    }
}
