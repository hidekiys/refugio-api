package br.com.fiap.refugio.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.fiap.refugio.config.ConnectionFactory;
import br.com.fiap.refugio.model.PontoDeRefresco;
import br.com.fiap.refugio.model.enums.TipoPonto;
import br.com.fiap.refugio.model.vo.Coordenada;

/**
 * DAO JDBC puro para a tabela PONTO_REFRESCO.
 */
public class PontoRefrescoDao {

    private static final String COLUNAS =
            "nome, tipo, latitude, longitude, endereco, climatizado, tem_bebedouro, capacidade, id_bairro";

    public PontoDeRefresco inserir(PontoDeRefresco p) {
        if (p.getIdPonto() == null) {
            String sql = "INSERT INTO PONTO_REFRESCO (" + COLUNAS + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id_ponto"})) {
                preencher(ps, p, 1);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        p.setIdPonto(rs.getLong(1));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao inserir PontoDeRefresco: " + e.getMessage(), e);
            }
        } else {
            String sql = "INSERT INTO PONTO_REFRESCO (id_ponto, " + COLUNAS
                    + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, p.getIdPonto());
                preencher(ps, p, 2);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao inserir PontoDeRefresco: " + e.getMessage(), e);
            }
        }
        return p;
    }

    private void preencher(PreparedStatement ps, PontoDeRefresco p, int offset) throws SQLException {
        Coordenada coord = p.getCoordenada();
        ps.setString(offset, p.getNome());
        ps.setString(offset + 1, p.getTipo() == null ? null : p.getTipo().name());
        if (coord == null || coord.getLatitude() == null) {
            ps.setNull(offset + 2, java.sql.Types.NUMERIC);
        } else {
            ps.setBigDecimal(offset + 2, BigDecimal.valueOf(coord.getLatitude()));
        }
        if (coord == null || coord.getLongitude() == null) {
            ps.setNull(offset + 3, java.sql.Types.NUMERIC);
        } else {
            ps.setBigDecimal(offset + 3, BigDecimal.valueOf(coord.getLongitude()));
        }
        ps.setString(offset + 4, p.getEndereco());
        ps.setString(offset + 5, Boolean.TRUE.equals(p.getClimatizado()) ? "S" : "N");
        ps.setString(offset + 6, Boolean.TRUE.equals(p.getTemBebedouro()) ? "S" : "N");
        if (p.getCapacidade() == null) {
            ps.setNull(offset + 7, java.sql.Types.NUMERIC);
        } else {
            ps.setInt(offset + 7, p.getCapacidade());
        }
        if (p.getIdBairro() == null) {
            ps.setNull(offset + 8, java.sql.Types.NUMERIC);
        } else {
            ps.setLong(offset + 8, p.getIdBairro());
        }
    }

    public PontoDeRefresco atualizar(PontoDeRefresco p) {
        String sql = "UPDATE PONTO_REFRESCO SET nome = ?, tipo = ?, latitude = ?, longitude = ?, endereco = ?, "
                + "climatizado = ?, tem_bebedouro = ?, capacidade = ?, id_bairro = ? WHERE id_ponto = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencher(ps, p, 1);
            ps.setLong(10, p.getIdPonto());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar PontoDeRefresco: " + e.getMessage(), e);
        }
        return p;
    }

    public boolean remover(Long id) {
        String sql = "DELETE FROM PONTO_REFRESCO WHERE id_ponto = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover PontoDeRefresco: " + e.getMessage(), e);
        }
    }

    public List<PontoDeRefresco> listarTodos() {
        String sql = "SELECT * FROM PONTO_REFRESCO ORDER BY id_ponto";
        List<PontoDeRefresco> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar PontosDeRefresco: " + e.getMessage(), e);
        }
        return lista;
    }

    public PontoDeRefresco buscarPorId(Long id) {
        String sql = "SELECT * FROM PONTO_REFRESCO WHERE id_ponto = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar PontoDeRefresco: " + e.getMessage(), e);
        }
        return null;
    }

    private PontoDeRefresco mapear(ResultSet rs) throws SQLException {
        PontoDeRefresco p = new PontoDeRefresco();
        p.setIdPonto(rs.getLong("id_ponto"));
        p.setNome(rs.getString("nome"));
        String tipo = rs.getString("tipo");
        p.setTipo(tipo == null ? null : TipoPonto.valueOf(tipo));
        BigDecimal lat = rs.getBigDecimal("latitude");
        BigDecimal lon = rs.getBigDecimal("longitude");
        if (lat != null || lon != null) {
            p.setCoordenada(new Coordenada(
                    lat == null ? null : lat.doubleValue(),
                    lon == null ? null : lon.doubleValue()));
        }
        p.setEndereco(rs.getString("endereco"));
        p.setClimatizado("S".equals(rs.getString("climatizado")));
        p.setTemBebedouro("S".equals(rs.getString("tem_bebedouro")));
        BigDecimal cap = rs.getBigDecimal("capacidade");
        p.setCapacidade(cap == null ? null : cap.intValue());
        BigDecimal idBairro = rs.getBigDecimal("id_bairro");
        p.setIdBairro(idBairro == null ? null : idBairro.longValue());
        return p;
    }
}
