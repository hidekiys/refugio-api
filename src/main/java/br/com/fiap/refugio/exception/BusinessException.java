package br.com.fiap.refugio.exception;

/**
 * Lancada quando uma regra de negocio e violada (dado invalido, estado
 * inconsistente). Mapeada para HTTP 400 pelo {@link GlobalExceptionHandler}.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
