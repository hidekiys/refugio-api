package br.com.fiap.refugio.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.fiap.refugio.config.ConnectionFactory;
import br.com.fiap.refugio.model.Cuidador;

/**
 * DAO JDBC puro para a tabela CUIDADOR.
 */
public class CuidadorDao {

    private static final String COLUNAS = "nome, telefone, parentesco, prioridade, id_cidadao";

    public Cuidador inserir(Cuidador c) {
        if (c.getIdCuidador() == null) {
            String sql = "INSERT INTO CUIDADOR (" + COLUNAS + ") VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id_cuidador"})) {
                preencher(ps, c, 1);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        c.setIdCuidador(rs.getLong(1));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao inserir Cuidador: " + e.getMessage(), e);
            }
        } else {
            String sql = "INSERT INTO CUIDADOR (id_cuidador, " + COLUNAS + ") VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, c.getIdCuidador());
                preencher(ps, c, 2);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao inserir Cuidador: " + e.getMessage(), e);
            }
        }
        return c;
    }

    private void preencher(PreparedStatement ps, Cuidador c, int offset) throws SQLException {
        ps.setString(offset, c.getNome());
        ps.setString(offset + 1, c.getTelefone());
        ps.setString(offset + 2, c.getParentesco());
        if (c.getPrioridade() == null) {
            ps.setInt(offset + 3, 1);
        } else {
            ps.setInt(offset + 3, c.getPrioridade());
        }
        if (c.getIdCidadao() == null) {
            ps.setNull(offset + 4, java.sql.Types.NUMERIC);
        } else {
            ps.setLong(offset + 4, c.getIdCidadao());
        }
    }

    public Cuidador atualizar(Cuidador c) {
        String sql = "UPDATE CUIDADOR SET nome = ?, telefone = ?, parentesco = ?, prioridade = ?, "
                + "id_cidadao = ? WHERE id_cuidador = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencher(ps, c, 1);
            ps.setLong(6, c.getIdCuidador());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar Cuidador: " + e.getMessage(), e);
        }
        return c;
    }

    public boolean remover(Long id) {
        String sql = "DELETE FROM CUIDADOR WHERE id_cuidador = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover Cuidador: " + e.getMessage(), e);
        }
    }

    public List<Cuidador> listarTodos() {
        String sql = "SELECT * FROM CUIDADOR ORDER BY id_cuidador";
        List<Cuidador> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar Cuidadores: " + e.getMessage(), e);
        }
        return lista;
    }

    public Cuidador buscarPorId(Long id) {
        String sql = "SELECT * FROM CUIDADOR WHERE id_cuidador = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar Cuidador: " + e.getMessage(), e);
        }
        return null;
    }

    private Cuidador mapear(ResultSet rs) throws SQLException {
        Cuidador c = new Cuidador();
        c.setIdCuidador(rs.getLong("id_cuidador"));
        c.setNome(rs.getString("nome"));
        c.setTelefone(rs.getString("telefone"));
        c.setParentesco(rs.getString("parentesco"));
        BigDecimal prio = rs.getBigDecimal("prioridade");
        c.setPrioridade(prio == null ? null : prio.intValue());
        BigDecimal idCidadao = rs.getBigDecimal("id_cidadao");
        c.setIdCidadao(idCidadao == null ? null : idCidadao.longValue());
        return c;
    }
}
