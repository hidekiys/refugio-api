package br.com.fiap.refugio.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.fiap.refugio.config.ConnectionFactory;
import br.com.fiap.refugio.model.AgenteSaude;

/**
 * DAO JDBC puro para a tabela AGENTE_SAUDE.
 */
public class AgenteSaudeDao {

    private static final String COLUNAS = "nome, telefone, cargo, id_bairro, id_ponto";

    public AgenteSaude inserir(AgenteSaude a) {
        if (a.getIdAgente() == null) {
            String sql = "INSERT INTO AGENTE_SAUDE (" + COLUNAS + ") VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id_agente"})) {
                preencher(ps, a, 1);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        a.setIdAgente(rs.getLong(1));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao inserir AgenteSaude: " + e.getMessage(), e);
            }
        } else {
            String sql = "INSERT INTO AGENTE_SAUDE (id_agente, " + COLUNAS + ") VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, a.getIdAgente());
                preencher(ps, a, 2);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao inserir AgenteSaude: " + e.getMessage(), e);
            }
        }
        return a;
    }

    private void preencher(PreparedStatement ps, AgenteSaude a, int offset) throws SQLException {
        ps.setString(offset, a.getNome());
        ps.setString(offset + 1, a.getTelefone());
        ps.setString(offset + 2, a.getCargo());
        if (a.getIdBairro() == null) {
            ps.setNull(offset + 3, java.sql.Types.NUMERIC);
        } else {
            ps.setLong(offset + 3, a.getIdBairro());
        }
        if (a.getIdPonto() == null) {
            ps.setNull(offset + 4, java.sql.Types.NUMERIC);
        } else {
            ps.setLong(offset + 4, a.getIdPonto());
        }
    }

    public AgenteSaude atualizar(AgenteSaude a) {
        String sql = "UPDATE AGENTE_SAUDE SET nome = ?, telefone = ?, cargo = ?, id_bairro = ?, "
                + "id_ponto = ? WHERE id_agente = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencher(ps, a, 1);
            ps.setLong(6, a.getIdAgente());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar AgenteSaude: " + e.getMessage(), e);
        }
        return a;
    }

    public boolean remover(Long id) {
        String sql = "DELETE FROM AGENTE_SAUDE WHERE id_agente = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover AgenteSaude: " + e.getMessage(), e);
        }
    }

    public List<AgenteSaude> listarTodos() {
        String sql = "SELECT * FROM AGENTE_SAUDE ORDER BY id_agente";
        List<AgenteSaude> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar AgentesSaude: " + e.getMessage(), e);
        }
        return lista;
    }

    public AgenteSaude buscarPorId(Long id) {
        String sql = "SELECT * FROM AGENTE_SAUDE WHERE id_agente = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar AgenteSaude: " + e.getMessage(), e);
        }
        return null;
    }

    private AgenteSaude mapear(ResultSet rs) throws SQLException {
        AgenteSaude a = new AgenteSaude();
        a.setIdAgente(rs.getLong("id_agente"));
        a.setNome(rs.getString("nome"));
        a.setTelefone(rs.getString("telefone"));
        a.setCargo(rs.getString("cargo"));
        BigDecimal idBairro = rs.getBigDecimal("id_bairro");
        a.setIdBairro(idBairro == null ? null : idBairro.longValue());
        BigDecimal idPonto = rs.getBigDecimal("id_ponto");
        a.setIdPonto(idPonto == null ? null : idPonto.longValue());
        return a;
    }
}
