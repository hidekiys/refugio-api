package br.com.fiap.refugio.bo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.fiap.refugio.dao.CheckinDao;
import br.com.fiap.refugio.exception.BusinessException;
import br.com.fiap.refugio.exception.ResourceNotFoundException;
import br.com.fiap.refugio.model.Checkin;
import br.com.fiap.refugio.model.CidadaoEmRisco;
import br.com.fiap.refugio.model.enums.StatusCheckin;

/**
 * Business Object do Checkin - onde vive a REGRA CENTRAL do Refugio: a
 * escalada do check-in "To bem".
 *
 * <ul>
 *   <li>Sem resposta em mais de 2h  -&gt; ESCALADO_CUIDADOR (notifica o cuidador);</li>
 *   <li>Sem resposta em mais de 4h  -&gt; ESCALADO_AGENTE   (notifica o agente de saude).</li>
 * </ul>
 */
@Service
public class CheckinBO {

    /** Limite para acionar o cuidador (1o nivel). */
    private static final long HORAS_CUIDADOR = 2;
    /** Limite para acionar o agente de saude (2o nivel). */
    private static final long HORAS_AGENTE = 4;

    private final CheckinDao dao = new CheckinDao();

    // ---------------------------------------------------------------- CRUD

    public List<Checkin> listar() {
        return dao.listarTodos();
    }

    public Checkin buscar(Long id) {
        Checkin c = dao.buscarPorId(id);
        if (c == null) {
            throw new ResourceNotFoundException("Checkin", id);
        }
        return c;
    }

    public Checkin criar(Checkin c) {
        if (c.getDataHoraSolicitacao() == null) {
            c.setDataHoraSolicitacao(LocalDateTime.now());
        }
        if (c.getStatus() == null) {
            c.setStatus(StatusCheckin.PENDENTE);
        }
        if (c.getIdCidadao() == null) {
            throw new BusinessException("id_cidadao e obrigatorio para o check-in.");
        }
        if (c.getIdAlerta() == null) {
            throw new BusinessException("id_alerta e obrigatorio para o check-in.");
        }
        return dao.inserir(c);
    }

    public Checkin atualizar(Long id, Checkin c) {
        buscar(id);
        c.setIdCheckin(id);
        return dao.atualizar(c);
    }

    public void remover(Long id) {
        buscar(id);
        dao.remover(id);
    }

    // ------------------------------------------------- Regra de negocio

    /**
     * Registra a resposta "To bem" do cidadao: marca RESPONDIDO_OK e grava o
     * momento da resposta.
     */
    public Checkin registrarResposta(Long id) {
        Checkin c = buscar(id);
        if (c.getDataHoraResposta() != null) {
            return c; // ja respondido
        }
        c.setDataHoraResposta(LocalDateTime.now());
        c.setStatus(StatusCheckin.RESPONDIDO_OK);
        return dao.atualizar(c);
    }

    /**
     * Aplica a regra de escalada a UM check-in e persiste se houve mudanca.
     *
     * @param id id do check-in
     * @return o check-in com o status/contatos atualizados
     */
    public Checkin processarEscalada(Long id) {
        Checkin c = buscar(id);
        if (aplicarEscalada(c, LocalDateTime.now())) {
            dao.atualizar(c);
        }
        return c;
    }

    /**
     * Varre TODOS os check-ins e aplica a escalada aos que estao sem resposta.
     *
     * @return apenas os check-ins que mudaram de status/contato nesta execucao
     */
    public List<Checkin> executarEscaladaGlobal() {
        LocalDateTime agora = LocalDateTime.now();
        List<Checkin> escalados = new ArrayList<>();
        for (Checkin c : dao.listarTodos()) {
            if (aplicarEscalada(c, agora)) {
                dao.atualizar(c);
                escalados.add(c);
            }
        }
        return escalados;
    }

    /**
     * Nucleo da regra. Decide, a partir do tempo decorrido desde a solicitacao,
     * se o check-in deve ser escalado para cuidador (&gt;2h) ou agente (&gt;4h).
     *
     * @return true se o objeto foi alterado (precisa persistir)
     */
    private boolean aplicarEscalada(Checkin c, LocalDateTime agora) {
        // Ja respondido ou sem data de solicitacao -> nada a escalar.
        if (c.getDataHoraResposta() != null || c.getDataHoraSolicitacao() == null) {
            return false;
        }
        long horas = Duration.between(c.getDataHoraSolicitacao(), agora).toHours();
        boolean alterado = false;

        if (horas > HORAS_AGENTE) {
            // 2o nivel: agente de saude. O 1o nivel (cuidador) ja deveria ter
            // acontecido, entao garantimos ambos os contatos preenchidos.
            if (c.getDataHoraContatoCuidador() == null) {
                c.setDataHoraContatoCuidador(agora);
                alterado = true;
            }
            if (c.getDataHoraContatoAgente() == null) {
                c.setDataHoraContatoAgente(agora);
                alterado = true;
            }
            if (c.getStatus() != StatusCheckin.ESCALADO_AGENTE) {
                c.setStatus(StatusCheckin.ESCALADO_AGENTE);
                alterado = true;
            }
        } else if (horas > HORAS_CUIDADOR) {
            // 1o nivel: cuidador.
            if (c.getDataHoraContatoCuidador() == null) {
                c.setDataHoraContatoCuidador(agora);
                alterado = true;
            }
            if (c.getStatus() != StatusCheckin.ESCALADO_CUIDADOR
                    && c.getStatus() != StatusCheckin.ESCALADO_AGENTE) {
                c.setStatus(StatusCheckin.ESCALADO_CUIDADOR);
                alterado = true;
            }
        }
        return alterado;
    }

    /**
     * Consulta de valor: idosos sem resposta de check-in ha mais de 2h em
     * bairro com alerta VERMELHO ativo. Reaproveita a logica da consulta bonus
     * do DQL (resolvida no {@link CheckinDao}).
     */
    public List<CidadaoEmRisco> cidadaosEmRisco() {
        return dao.listarCidadaosEmRisco();
    }
}
