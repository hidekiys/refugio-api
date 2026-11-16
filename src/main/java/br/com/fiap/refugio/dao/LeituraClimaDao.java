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
import br.com.fiap.refugio.model.LeituraClima;
import br.com.fiap.refugio.model.enums.FonteLeitura;

/**
 * DAO JDBC puro para a tabela LEITURA_CLIMA.
 */
public class LeituraClimaDao {

    private static final String COLUNAS = "data_hora, temperatura, umidade, indice_calor, fonte, id_bairro";

    public LeituraClima inserir(LeituraClima l) {
        if (l.getIdLeitura() == null) {
            String sql = "INSERT INTO LEITURA_CLIMA (" + COLUNAS + ") VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id_leitura"})) {
                preencher(ps, l, 1);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        l.setIdLeitura(rs.getLong(1));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao inserir LeituraClima: " + e.getMessage(), e);
            }
        } else {
            String sql = "INSERT INTO LEITURA_CLIMA (id_leitura, " + COLUNAS + ") VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, l.getIdLeitura());
                preencher(ps, l, 2);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao inserir LeituraClima: " + e.getMessage(), e);
            }
        }
        return l;
    }

    private void preencher(PreparedStatement ps, LeituraClima l, int offset) throws SQLException {
        ps.setTimestamp(offset, l.getDataHora() == null ? null : Timestamp.valueOf(l.getDataHora()));
        if (l.getTemperatura() == null) {
            ps.setNull(offset + 1, java.sql.Types.NUMERIC);
        } else {
            ps.setBigDecimal(offset + 1, BigDecimal.valueOf(l.getTemperatura()));
        }
        if (l.getUmidade() == null) {
            ps.setNull(offset + 2, java.sql.Types.NUMERIC);
        } else {
            ps.setBigDecimal(offset + 2, BigDecimal.valueOf(l.getUmidade()));
        }
        if (l.getIndiceCalor() == null) {
            ps.setNull(offset + 3, java.sql.Types.NUMERIC);
        } else {
            ps.setBigDecimal(offset + 3, BigDecimal.valueOf(l.getIndiceCalor()));
        }
        ps.setString(offset + 4, l.getFonte() == null ? null : l.getFonte().name());
        if (l.getIdBairro() == null) {
            ps.setNull(offset + 5, java.sql.Types.NUMERIC);
        } else {
            ps.setLong(offset + 5, l.getIdBairro());
        }
    }

    public LeituraClima atualizar(LeituraClima l) {
        String sql = "UPDATE LEITURA_CLIMA SET data_hora = ?, temperatura = ?, umidade = ?, indice_calor = ?, "
                + "fonte = ?, id_bairro = ? WHERE id_leitura = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencher(ps, l, 1);
            ps.setLong(7, l.getIdLeitura());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar LeituraClima: " + e.getMessage(), e);
        }
        return l;
    }

    public boolean remover(Long id) {
        String sql = "DELETE FROM LEITURA_CLIMA WHERE id_leitura = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover LeituraClima: " + e.getMessage(), e);
        }
    }

    public List<LeituraClima> listarTodos() {
        String sql = "SELECT * FROM LEITURA_CLIMA ORDER BY id_leitura";
        List<LeituraClima> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar LeiturasClima: " + e.getMessage(), e);
        }
        return lista;
    }

    public LeituraClima buscarPorId(Long id) {
        String sql = "SELECT * FROM LEITURA_CLIMA WHERE id_leitura = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar LeituraClima: " + e.getMessage(), e);
        }
        return null;
    }

    private LeituraClima mapear(ResultSet rs) throws SQLException {
        LeituraClima l = new LeituraClima();
        l.setIdLeitura(rs.getLong("id_leitura"));
        Timestamp dh = rs.getTimestamp("data_hora");
        l.setDataHora(dh == null ? null : dh.toLocalDateTime());
        BigDecimal temp = rs.getBigDecimal("temperatura");
        l.setTemperatura(temp == null ? null : temp.doubleValue());
        BigDecimal umid = rs.getBigDecimal("umidade");
        l.setUmidade(umid == null ? null : umid.doubleValue());
        BigDecimal ic = rs.getBigDecimal("indice_calor");
        l.setIndiceCalor(ic == null ? null : ic.doubleValue());
        String fonte = rs.getString("fonte");
        l.setFonte(fonte == null ? null : FonteLeitura.valueOf(fonte));
        BigDecimal idBairro = rs.getBigDecimal("id_bairro");
        l.setIdBairro(idBairro == null ? null : idBairro.longValue());
        return l;
    }
}
