package br.com.fiap.refugio.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import br.com.fiap.refugio.config.ConnectionFactory;
import br.com.fiap.refugio.model.Checkin;
import br.com.fiap.refugio.model.CidadaoEmRisco;
import br.com.fiap.refugio.model.enums.StatusCheckin;

/**
 * DAO JDBC puro para a tabela CHECKIN. Alem do CRUD, expoe a consulta de valor
 * "cidadaos em risco" (idosos sem resposta em bairro com alerta vermelho).
 */
public class CheckinDao {

    private static final String COLUNAS =
            "data_hora_solicitacao, data_hora_resposta, status, data_hora_contato_cuidador, "
            + "data_hora_contato_agente, id_cidadao, id_alerta";

    public Checkin inserir(Checkin c) {
        if (c.getIdCheckin() == null) {
            String sql = "INSERT INTO CHECKIN (" + COLUNAS + ") VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id_checkin"})) {
                preencher(ps, c, 1);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        c.setIdCheckin(rs.getLong(1));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao inserir Checkin: " + e.getMessage(), e);
            }
        } else {
            String sql = "INSERT INTO CHECKIN (id_checkin, " + COLUNAS + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, c.getIdCheckin());
                preencher(ps, c, 2);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao inserir Checkin: " + e.getMessage(), e);
            }
        }
        return c;
    }

    private void preencher(PreparedStatement ps, Checkin c, int offset) throws SQLException {
        ps.setTimestamp(offset, c.getDataHoraSolicitacao() == null
                ? null : Timestamp.valueOf(c.getDataHoraSolicitacao()));
        ps.setTimestamp(offset + 1, c.getDataHoraResposta() == null
                ? null : Timestamp.valueOf(c.getDataHoraResposta()));
        ps.setString(offset + 2, c.getStatus() == null ? StatusCheckin.PENDENTE.name() : c.getStatus().name());
        ps.setTimestamp(offset + 3, c.getDataHoraContatoCuidador() == null
                ? null : Timestamp.valueOf(c.getDataHoraContatoCuidador()));
        ps.setTimestamp(offset + 4, c.getDataHoraContatoAgente() == null
                ? null : Timestamp.valueOf(c.getDataHoraContatoAgente()));
        if (c.getIdCidadao() == null) {
            ps.setNull(offset + 5, java.sql.Types.NUMERIC);
        } else {
            ps.setLong(offset + 5, c.getIdCidadao());
        }
        if (c.getIdAlerta() == null) {
            ps.setNull(offset + 6, java.sql.Types.NUMERIC);
        } else {
            ps.setLong(offset + 6, c.getIdAlerta());
        }
    }

    public Checkin atualizar(Checkin c) {
        String sql = "UPDATE CHECKIN SET data_hora_solicitacao = ?, data_hora_resposta = ?, status = ?, "
                + "data_hora_contato_cuidador = ?, data_hora_contato_agente = ?, id_cidadao = ?, "
                + "id_alerta = ? WHERE id_checkin = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencher(ps, c, 1);
            ps.setLong(8, c.getIdCheckin());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar Checkin: " + e.getMessage(), e);
        }
        return c;
    }

    public boolean remover(Long id) {
        String sql = "DELETE FROM CHECKIN WHERE id_checkin = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover Checkin: " + e.getMessage(), e);
        }
    }

    public List<Checkin> listarTodos() {
        String sql = "SELECT * FROM CHECKIN ORDER BY id_checkin";
        List<Checkin> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar Checkins: " + e.getMessage(), e);
        }
        return lista;
    }

    public Checkin buscarPorId(Long id) {
        String sql = "SELECT * FROM CHECKIN WHERE id_checkin = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar Checkin: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Consulta de valor do Refugio (reaproveita a "consulta bonus" do DQL):
     * idosos SEM resposta de check-in ha mais de 2h, em bairro com alerta
     * VERMELHO ativo. 4 tabelas (CHECKIN, CIDADAO, BAIRRO, ALERTA).
     */
    public List<CidadaoEmRisco> listarCidadaosEmRisco() {
        String sql = "SELECT b.nome AS bairro, c.nome AS idoso, c.telefone, a.nivel AS nivel_alerta, "
                + "ck.data_hora_solicitacao, ck.status "
                + "FROM CHECKIN ck "
                + "INNER JOIN CIDADAO c ON c.id_cidadao = ck.id_cidadao "
                + "INNER JOIN BAIRRO  b ON b.id_bairro  = c.id_bairro "
                + "INNER JOIN ALERTA  a ON a.id_alerta  = ck.id_alerta "
                + "WHERE c.tipo = 'IDOSO' "
                + "  AND a.nivel = 'VERMELHO' "
                + "  AND a.ativo = 'S' "
                + "  AND ck.data_hora_resposta IS NULL "
                + "  AND ck.data_hora_solicitacao + INTERVAL '2' HOUR < SYSTIMESTAMP "
                + "ORDER BY ck.data_hora_solicitacao";
        List<CidadaoEmRisco> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Timestamp sol = rs.getTimestamp("data_hora_solicitacao");
                lista.add(new CidadaoEmRisco(
                        rs.getString("bairro"),
                        rs.getString("idoso"),
                        rs.getString("telefone"),
                        rs.getString("nivel_alerta"),
                        sol == null ? null : sol.toLocalDateTime(),
                        rs.getString("status")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar cidadaos em risco: " + e.getMessage(), e);
        }
        return lista;
    }

    private Checkin mapear(ResultSet rs) throws SQLException {
        Checkin c = new Checkin();
        c.setIdCheckin(rs.getLong("id_checkin"));
        Timestamp sol = rs.getTimestamp("data_hora_solicitacao");
        c.setDataHoraSolicitacao(sol == null ? null : sol.toLocalDateTime());
        Timestamp resp = rs.getTimestamp("data_hora_resposta");
        c.setDataHoraResposta(resp == null ? null : resp.toLocalDateTime());
        String status = rs.getString("status");
        c.setStatus(status == null ? null : StatusCheckin.valueOf(status));
        Timestamp cuid = rs.getTimestamp("data_hora_contato_cuidador");
        c.setDataHoraContatoCuidador(cuid == null ? null : cuid.toLocalDateTime());
        Timestamp ag = rs.getTimestamp("data_hora_contato_agente");
        c.setDataHoraContatoAgente(ag == null ? null : ag.toLocalDateTime());
        BigDecimal idCidadao = rs.getBigDecimal("id_cidadao");
        c.setIdCidadao(idCidadao == null ? null : idCidadao.longValue());
        BigDecimal idAlerta = rs.getBigDecimal("id_alerta");
        c.setIdAlerta(idAlerta == null ? null : idAlerta.longValue());
        return c;
    }
}
