package br.com.fiap.refugio.bo;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.fiap.refugio.dao.CidadaoDao;
import br.com.fiap.refugio.exception.BusinessException;
import br.com.fiap.refugio.exception.ResourceNotFoundException;
import br.com.fiap.refugio.model.Cidadao;

/**
 * Business Object do Cidadao. Valida regras basicas e delega a persistencia
 * ao {@link CidadaoDao}.
 */
@Service
public class CidadaoBO {

    private final CidadaoDao dao = new CidadaoDao();

    public List<Cidadao> listar() {
        return dao.listarTodos();
    }

    public Cidadao buscar(Long id) {
        Cidadao c = dao.buscarPorId(id);
        if (c == null) {
            throw new ResourceNotFoundException("Cidadao", id);
        }
        return c;
    }

    public Cidadao criar(Cidadao c) {
        validar(c);
        return dao.inserir(c);
    }

    public Cidadao atualizar(Long id, Cidadao c) {
        buscar(id); // garante 404 se nao existir
        validar(c);
        c.setIdCidadao(id);
        return dao.atualizar(c);
    }

    public void remover(Long id) {
        buscar(id);
        dao.remover(id);
    }

    private void validar(Cidadao c) {
        if (c.getNome() == null || c.getNome().isBlank()) {
            throw new BusinessException("Nome do cidadao e obrigatorio.");
        }
        if (c.getTipo() == null) {
            throw new BusinessException("Tipo do cidadao e obrigatorio (IDOSO ou TRABALHADOR_RUA).");
        }
        if (c.getIdBairro() == null) {
            throw new BusinessException("id_bairro e obrigatorio para o cidadao.");
        }
    }
}
