package br.com.fiap.refugio.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.fiap.refugio.config.ConnectionFactory;
import br.com.fiap.refugio.model.Bairro;

/**
 * DAO JDBC puro para a tabela BAIRRO.
 */
public class BairroDao {

    public Bairro inserir(Bairro b) {
        if (b.getIdBairro() == null) {
            String sql = "INSERT INTO BAIRRO (nome, cidade, uf, codigo_ibge, zona) VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id_bairro"})) {
                preencher(ps, b);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        b.setIdBairro(rs.getLong(1));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao inserir Bairro: " + e.getMessage(), e);
            }
        } else {
            String sql = "INSERT INTO BAIRRO (id_bairro, nome, cidade, uf, codigo_ibge, zona) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, b.getIdBairro());
                ps.setString(2, b.getNome());
                ps.setString(3, b.getCidade());
                ps.setString(4, b.getUf());
                ps.setString(5, b.getCodigoIbge());
                ps.setString(6, b.getZona());
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao inserir Bairro: " + e.getMessage(), e);
            }
        }
        return b;
    }

    private void preencher(PreparedStatement ps, Bairro b) throws SQLException {
        ps.setString(1, b.getNome());
        ps.setString(2, b.getCidade());
        ps.setString(3, b.getUf());
        ps.setString(4, b.getCodigoIbge());
        ps.setString(5, b.getZona());
    }

    public Bairro atualizar(Bairro b) {
        String sql = "UPDATE BAIRRO SET nome = ?, cidade = ?, uf = ?, codigo_ibge = ?, zona = ? WHERE id_bairro = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencher(ps, b);
            ps.setLong(6, b.getIdBairro());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar Bairro: " + e.getMessage(), e);
        }
        return b;
    }

    public boolean remover(Long id) {
        String sql = "DELETE FROM BAIRRO WHERE id_bairro = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover Bairro: " + e.getMessage(), e);
        }
    }

    public List<Bairro> listarTodos() {
        String sql = "SELECT * FROM BAIRRO ORDER BY id_bairro";
        List<Bairro> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar Bairros: " + e.getMessage(), e);
        }
        return lista;
    }

    public Bairro buscarPorId(Long id) {
        String sql = "SELECT * FROM BAIRRO WHERE id_bairro = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar Bairro: " + e.getMessage(), e);
        }
        return null;
    }

    private Bairro mapear(ResultSet rs) throws SQLException {
        Bairro b = new Bairro();
        b.setIdBairro(rs.getLong("id_bairro"));
        b.setNome(rs.getString("nome"));
        b.setCidade(rs.getString("cidade"));
        b.setUf(rs.getString("uf"));
        b.setCodigoIbge(rs.getString("codigo_ibge"));
        b.setZona(rs.getString("zona"));
        return b;
    }
}
