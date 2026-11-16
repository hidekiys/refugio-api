package br.com.fiap.refugio.model.enums;

/**
 * Nivel do alerta de calor emitido para um bairro.
 * Espelha o CHECK da coluna ALERTA.nivel no banco.
 */
public enum NivelAlerta {
    VERDE,
    AMARELO,
    LARANJA,
    VERMELHO
}
