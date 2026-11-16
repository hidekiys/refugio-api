package br.com.fiap.refugio.model;

/**
 * Entidade HORARIO_FUNC. Janela de funcionamento de um ponto de refresco
 * (uma linha por dia da semana; 0=domingo ... 6=sabado).
 */
public class HorarioFuncionamento {

    private Long idHorario;
    private Integer diaSemana;
    private String horaAbertura;
    private String horaFechamento;
    private Long idPonto;

    public HorarioFuncionamento() {
    }

    public HorarioFuncionamento(Long idHorario, Integer diaSemana, String horaAbertura,
                                String horaFechamento, Long idPonto) {
        this.idHorario = idHorario;
        this.diaSemana = diaSemana;
        this.horaAbertura = horaAbertura;
        this.horaFechamento = horaFechamento;
        this.idPonto = idPonto;
    }

    public Long getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Long idHorario) {
        this.idHorario = idHorario;
    }

    public Integer getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(Integer diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHoraAbertura() {
        return horaAbertura;
    }

    public void setHoraAbertura(String horaAbertura) {
        this.horaAbertura = horaAbertura;
    }

    public String getHoraFechamento() {
        return horaFechamento;
    }

    public void setHoraFechamento(String horaFechamento) {
        this.horaFechamento = horaFechamento;
    }

    public Long getIdPonto() {
        return idPonto;
    }

    public void setIdPonto(Long idPonto) {
        this.idPonto = idPonto;
    }
}
