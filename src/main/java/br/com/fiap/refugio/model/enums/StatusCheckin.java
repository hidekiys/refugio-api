package br.com.fiap.refugio.model.enums;

/**
 * Status do ciclo de check-in "To bem" e da escalada.
 * Espelha o CHECK da coluna CHECKIN.status no banco.
 */
public enum StatusCheckin {
    PENDENTE,
    RESPONDIDO_OK,
    ESCALADO_CUIDADOR,
    ESCALADO_AGENTE
}
