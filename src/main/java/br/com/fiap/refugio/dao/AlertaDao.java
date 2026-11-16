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
import br.com.fiap.refugio.model.Alerta;
import br.com.fiap.refugio.model.enums.NivelAlerta;
import br.com.fiap.refugio.model.vo.IndiceDeCalor;

/**
 * DAO JDBC puro para a tabela ALERTA. O VO IndiceDeCalor e desmembrado nas
 * colunas nivel + indice_calor.
 */
public class AlertaDao {

    private static final String COLUNAS =
            "nivel, indice_calor, data_hora_inicio, data_hora_fim, ativo, id_bairro, id_leitura";

    public Alerta inserir(Alerta a) {
        if (a.getIdAlerta() == null) {
            String sql = "INSERT INTO ALERTA (" + COLUNAS + ") VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id_alerta"})) {
                preencher(ps, a, 1);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        a.setIdAlerta(rs.getLong(1));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao inserir Alerta: " + e.getMessage(), e);
            }
        } else {
            String sql = "INSERT INTO ALERTA (id_alerta, " + COLUNAS + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, a.getIdAlerta());
                preencher(ps, a, 2);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao inserir Alerta: " + e.getMessage(), e);
            }
        }
        return a;
    }

    private void preencher(PreparedStatement ps, Alerta a, int offset) throws SQLException {
        IndiceDeCalor ic = a.getIndiceCalor();
        NivelAlerta nivel = ic == null ? null : ic.getNivel();
        Double valor = ic == null ? null : ic.getValor();
        ps.setString(offset, nivel == null ? null : nivel.name());
        if (valor == null) {
            ps.setNull(offset + 1, java.sql.Types.NUMERIC);
        } else {
            ps.setBigDecimal(offset + 1, BigDecimal.valueOf(valor));
        }
        ps.setTimestamp(offset + 2, a.getDataHoraInicio() == null ? null : Timestamp.valueOf(a.getDataHoraInicio()));
        ps.setTimestamp(offset + 3, a.getDataHoraFim() == null ? null : Timestamp.valueOf(a.getDataHoraFim()));
        ps.setString(offset + 4, Boolean.FALSE.equals(a.getAtivo()) ? "N" : "S");
        if (a.getIdBairro() == null) {
            ps.setNull(offset + 5, java.sql.Types.NUMERIC);
        } else {
            ps.setLong(offset + 5, a.getIdBairro());
        }
        if (a.getIdLeitura() == null) {
            ps.setNull(offset + 6, java.sql.Types.NUMERIC);
        } else {
            ps.setLong(offset + 6, a.getIdLeitura());
        }
    }

    public Alerta atualizar(Alerta a) {
        String sql = "UPDATE ALERTA SET nivel = ?, indice_calor = ?, data_hora_inicio = ?, data_hora_fim = ?, "
                + "ativo = ?, id_bairro = ?, id_leitura = ? WHERE id_alerta = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencher(ps, a, 1);
            ps.setLong(8, a.getIdAlerta());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar Alerta: " + e.getMessage(), e);
        }
        return a;
    }

    public boolean remover(Long id) {
        String sql = "DELETE FROM ALERTA WHERE id_alerta = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover Alerta: " + e.getMessage(), e);
        }
    }

    public List<Alerta> listarTodos() {
        String sql = "SELECT * FROM ALERTA ORDER BY id_alerta";
        List<Alerta> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar Alertas: " + e.getMessage(), e);
        }
        return lista;
    }

    public Alerta buscarPorId(Long id) {
        String sql = "SELECT * FROM ALERTA WHERE id_alerta = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar Alerta: " + e.getMessage(), e);
        }
        return null;
    }

    private Alerta mapear(ResultSet rs) throws SQLException {
        Alerta a = new Alerta();
        a.setIdAlerta(rs.getLong("id_alerta"));
        String nivel = rs.getString("nivel");
        BigDecimal valor = rs.getBigDecimal("indice_calor");
        a.setIndiceCalor(new IndiceDeCalor(
                valor == null ? null : valor.doubleValue(),
                nivel == null ? null : NivelAlerta.valueOf(nivel)));
        Timestamp ini = rs.getTimestamp("data_hora_inicio");
        a.setDataHoraInicio(ini == null ? null : ini.toLocalDateTime());
        Timestamp fim = rs.getTimestamp("data_hora_fim");
        a.setDataHoraFim(fim == null ? null : fim.toLocalDateTime());
        a.setAtivo("S".equals(rs.getString("ativo")));
        BigDecimal idBairro = rs.getBigDecimal("id_bairro");
        a.setIdBairro(idBairro == null ? null : idBairro.longValue());
        BigDecimal idLeitura = rs.getBigDecimal("id_leitura");
        a.setIdLeitura(idLeitura == null ? null : idLeitura.longValue());
        return a;
    }
}
