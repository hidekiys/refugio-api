package br.com.fiap.refugio.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.fiap.refugio.config.ConnectionFactory;
import br.com.fiap.refugio.model.Cidadao;
import br.com.fiap.refugio.model.enums.TipoCidadao;
import br.com.fiap.refugio.model.vo.Coordenada;

/**
 * DAO JDBC puro para a tabela CIDADAO.
 */
public class CidadaoDao {

    private static final String COLUNAS =
            "nome, data_nascimento, telefone, tipo, latitude, longitude, endereco, ativo, id_bairro";

    public Cidadao inserir(Cidadao c) {
        if (c.getIdCidadao() == null) {
            String sql = "INSERT INTO CIDADAO (" + COLUNAS + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id_cidadao"})) {
                preencher(ps, c, 1);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        c.setIdCidadao(rs.getLong(1));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao inserir Cidadao: " + e.getMessage(), e);
            }
        } else {
            String sql = "INSERT INTO CIDADAO (id_cidadao, " + COLUNAS + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, c.getIdCidadao());
                preencher(ps, c, 2);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao inserir Cidadao: " + e.getMessage(), e);
            }
        }
        return c;
    }

    private void preencher(PreparedStatement ps, Cidadao c, int offset) throws SQLException {
        Coordenada coord = c.getCoordenada();
        ps.setString(offset, c.getNome());
        ps.setDate(offset + 1, c.getDataNascimento() == null ? null : Date.valueOf(c.getDataNascimento()));
        ps.setString(offset + 2, c.getTelefone());
        ps.setString(offset + 3, c.getTipo() == null ? null : c.getTipo().name());
        if (coord == null || coord.getLatitude() == null) {
            ps.setNull(offset + 4, java.sql.Types.NUMERIC);
        } else {
            ps.setBigDecimal(offset + 4, BigDecimal.valueOf(coord.getLatitude()));
        }
        if (coord == null || coord.getLongitude() == null) {
            ps.setNull(offset + 5, java.sql.Types.NUMERIC);
        } else {
            ps.setBigDecimal(offset + 5, BigDecimal.valueOf(coord.getLongitude()));
        }
        ps.setString(offset + 6, c.getEndereco());
        ps.setString(offset + 7, Boolean.FALSE.equals(c.getAtivo()) ? "N" : "S");
        if (c.getIdBairro() == null) {
            ps.setNull(offset + 8, java.sql.Types.NUMERIC);
        } else {
            ps.setLong(offset + 8, c.getIdBairro());
        }
    }

    public Cidadao atualizar(Cidadao c) {
        String sql = "UPDATE CIDADAO SET nome = ?, data_nascimento = ?, telefone = ?, tipo = ?, "
                + "latitude = ?, longitude = ?, endereco = ?, ativo = ?, id_bairro = ? WHERE id_cidadao = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencher(ps, c, 1);
            ps.setLong(10, c.getIdCidadao());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar Cidadao: " + e.getMessage(), e);
        }
        return c;
    }

    public boolean remover(Long id) {
        String sql = "DELETE FROM CIDADAO WHERE id_cidadao = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover Cidadao: " + e.getMessage(), e);
        }
    }

    public List<Cidadao> listarTodos() {
        String sql = "SELECT * FROM CIDADAO ORDER BY id_cidadao";
        List<Cidadao> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar Cidadaos: " + e.getMessage(), e);
        }
        return lista;
    }

    public Cidadao buscarPorId(Long id) {
        String sql = "SELECT * FROM CIDADAO WHERE id_cidadao = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar Cidadao: " + e.getMessage(), e);
        }
        return null;
    }

    private Cidadao mapear(ResultSet rs) throws SQLException {
        Cidadao c = new Cidadao();
        c.setIdCidadao(rs.getLong("id_cidadao"));
        c.setNome(rs.getString("nome"));
        Date nasc = rs.getDate("data_nascimento");
        c.setDataNascimento(nasc == null ? null : nasc.toLocalDate());
        c.setTelefone(rs.getString("telefone"));
        String tipo = rs.getString("tipo");
        c.setTipo(tipo == null ? null : TipoCidadao.valueOf(tipo));
        BigDecimal lat = rs.getBigDecimal("latitude");
        BigDecimal lon = rs.getBigDecimal("longitude");
        if (lat != null || lon != null) {
            c.setCoordenada(new Coordenada(
                    lat == null ? null : lat.doubleValue(),
                    lon == null ? null : lon.doubleValue()));
        }
        c.setEndereco(rs.getString("endereco"));
        c.setAtivo("S".equals(rs.getString("ativo")));
        BigDecimal idBairro = rs.getBigDecimal("id_bairro");
        c.setIdBairro(idBairro == null ? null : idBairro.longValue());
        return c;
    }
}
