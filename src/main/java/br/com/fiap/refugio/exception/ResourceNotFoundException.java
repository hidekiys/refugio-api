package br.com.fiap.refugio.exception;

/**
 * Lancada quando um recurso solicitado nao existe. Mapeada para HTTP 404
 * pelo {@link GlobalExceptionHandler}.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String recurso, Object id) {
        super(recurso + " nao encontrado(a) para o id " + id);
    }
}
