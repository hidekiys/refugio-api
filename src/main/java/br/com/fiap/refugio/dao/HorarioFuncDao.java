package br.com.fiap.refugio.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.fiap.refugio.config.ConnectionFactory;
import br.com.fiap.refugio.model.HorarioFuncionamento;

/**
 * DAO JDBC puro para a tabela HORARIO_FUNC.
 */
public class HorarioFuncDao {

    private static final String COLUNAS = "dia_semana, hora_abertura, hora_fechamento, id_ponto";

    public HorarioFuncionamento inserir(HorarioFuncionamento h) {
        if (h.getIdHorario() == null) {
            String sql = "INSERT INTO HORARIO_FUNC (" + COLUNAS + ") VALUES (?, ?, ?, ?)";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id_horario"})) {
                preencher(ps, h, 1);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        h.setIdHorario(rs.getLong(1));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao inserir HorarioFuncionamento: " + e.getMessage(), e);
            }
        } else {
            String sql = "INSERT INTO HORARIO_FUNC (id_horario, " + COLUNAS + ") VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, h.getIdHorario());
                preencher(ps, h, 2);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao inserir HorarioFuncionamento: " + e.getMessage(), e);
            }
        }
        return h;
    }

    private void preencher(PreparedStatement ps, HorarioFuncionamento h, int offset) throws SQLException {
        if (h.getDiaSemana() == null) {
            ps.setNull(offset, java.sql.Types.NUMERIC);
        } else {
            ps.setInt(offset, h.getDiaSemana());
        }
        ps.setString(offset + 1, h.getHoraAbertura());
        ps.setString(offset + 2, h.getHoraFechamento());
        if (h.getIdPonto() == null) {
            ps.setNull(offset + 3, java.sql.Types.NUMERIC);
        } else {
            ps.setLong(offset + 3, h.getIdPonto());
        }
    }

    public HorarioFuncionamento atualizar(HorarioFuncionamento h) {
        String sql = "UPDATE HORARIO_FUNC SET dia_semana = ?, hora_abertura = ?, hora_fechamento = ?, "
                + "id_ponto = ? WHERE id_horario = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencher(ps, h, 1);
            ps.setLong(5, h.getIdHorario());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar HorarioFuncionamento: " + e.getMessage(), e);
        }
        return h;
    }

    public boolean remover(Long id) {
        String sql = "DELETE FROM HORARIO_FUNC WHERE id_horario = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover HorarioFuncionamento: " + e.getMessage(), e);
        }
    }

    public List<HorarioFuncionamento> listarTodos() {
        String sql = "SELECT * FROM HORARIO_FUNC ORDER BY id_horario";
        List<HorarioFuncionamento> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar HorariosFuncionamento: " + e.getMessage(), e);
        }
        return lista;
    }

    public HorarioFuncionamento buscarPorId(Long id) {
        String sql = "SELECT * FROM HORARIO_FUNC WHERE id_horario = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar HorarioFuncionamento: " + e.getMessage(), e);
        }
        return null;
    }

    private HorarioFuncionamento mapear(ResultSet rs) throws SQLException {
        HorarioFuncionamento h = new HorarioFuncionamento();
        h.setIdHorario(rs.getLong("id_horario"));
        BigDecimal dia = rs.getBigDecimal("dia_semana");
        h.setDiaSemana(dia == null ? null : dia.intValue());
        h.setHoraAbertura(rs.getString("hora_abertura"));
        h.setHoraFechamento(rs.getString("hora_fechamento"));
        BigDecimal idPonto = rs.getBigDecimal("id_ponto");
        h.setIdPonto(idPonto == null ? null : idPonto.longValue());
        return h;
    }
}
