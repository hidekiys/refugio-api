package br.com.fiap.refugio.bo;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.fiap.refugio.dao.AlertaDao;
import br.com.fiap.refugio.exception.BusinessException;
import br.com.fiap.refugio.exception.ResourceNotFoundException;
import br.com.fiap.refugio.model.Alerta;

/**
 * Business Object do Alerta de calor.
 */
@Service
public class AlertaBO {

    private final AlertaDao dao = new AlertaDao();

    public List<Alerta> listar() {
        return dao.listarTodos();
    }

    public Alerta buscar(Long id) {
        Alerta a = dao.buscarPorId(id);
        if (a == null) {
            throw new ResourceNotFoundException("Alerta", id);
        }
        return a;
    }

    public Alerta criar(Alerta a) {
        validar(a);
        return dao.inserir(a);
    }

    public Alerta atualizar(Long id, Alerta a) {
        buscar(id);
        validar(a);
        a.setIdAlerta(id);
        return dao.atualizar(a);
    }

    public void remover(Long id) {
        buscar(id);
        dao.remover(id);
    }

    private void validar(Alerta a) {
        if (a.getIndiceCalor() == null || a.getIndiceCalor().getNivel() == null) {
            throw new BusinessException("Nivel do alerta e obrigatorio (VERDE, AMARELO, LARANJA ou VERMELHO).");
        }
        if (a.getDataHoraInicio() == null) {
            throw new BusinessException("data_hora_inicio do alerta e obrigatoria.");
        }
        if (a.getIdBairro() == null) {
            throw new BusinessException("id_bairro e obrigatorio para o alerta.");
        }
    }
}
