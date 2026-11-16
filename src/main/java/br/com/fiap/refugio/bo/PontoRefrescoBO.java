package br.com.fiap.refugio.bo;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.fiap.refugio.dao.PontoRefrescoDao;
import br.com.fiap.refugio.exception.BusinessException;
import br.com.fiap.refugio.exception.ResourceNotFoundException;
import br.com.fiap.refugio.model.PontoDeRefresco;

/**
 * Business Object do Ponto de Refresco.
 */
@Service
public class PontoRefrescoBO {

    private final PontoRefrescoDao dao = new PontoRefrescoDao();

    public List<PontoDeRefresco> listar() {
        return dao.listarTodos();
    }

    public PontoDeRefresco buscar(Long id) {
        PontoDeRefresco p = dao.buscarPorId(id);
        if (p == null) {
            throw new ResourceNotFoundException("PontoDeRefresco", id);
        }
        return p;
    }

    public PontoDeRefresco criar(PontoDeRefresco p) {
        validar(p);
        return dao.inserir(p);
    }

    public PontoDeRefresco atualizar(Long id, PontoDeRefresco p) {
        buscar(id);
        validar(p);
        p.setIdPonto(id);
        return dao.atualizar(p);
    }

    public void remover(Long id) {
        buscar(id);
        dao.remover(id);
    }

    private void validar(PontoDeRefresco p) {
        if (p.getNome() == null || p.getNome().isBlank()) {
            throw new BusinessException("Nome do ponto de refresco e obrigatorio.");
        }
        if (p.getTipo() == null) {
            throw new BusinessException("Tipo do ponto e obrigatorio (UBS, CEU ou COMERCIO_PARCEIRO).");
        }
        if (p.getIdBairro() == null) {
            throw new BusinessException("id_bairro e obrigatorio para o ponto de refresco.");
        }
    }
}
