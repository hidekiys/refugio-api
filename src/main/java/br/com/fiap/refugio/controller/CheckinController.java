package br.com.fiap.refugio.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.refugio.bo.CheckinBO;
import br.com.fiap.refugio.model.Checkin;
import br.com.fiap.refugio.model.CidadaoEmRisco;

/**
 * Endpoints REST de Checkin, incluindo os endpoints da regra de negocio
 * central (escalada) e a consulta de cidadaos em risco.
 */
@RestController
@RequestMapping("/api/checkins")
public class CheckinController {

    private final CheckinBO bo;

    public CheckinController(CheckinBO bo) {
        this.bo = bo;
    }

    // ------------------------------------------------------------ CRUD

    @GetMapping
    public List<Checkin> listar() {
        return bo.listar();
    }

    @GetMapping("/{id}")
    public Checkin buscar(@PathVariable Long id) {
        return bo.buscar(id);
    }

    @PostMapping
    public ResponseEntity<Checkin> criar(@RequestBody Checkin checkin) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bo.criar(checkin));
    }

    @PutMapping("/{id}")
    public Checkin atualizar(@PathVariable Long id, @RequestBody Checkin checkin) {
        return bo.atualizar(id, checkin);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        bo.remover(id);
        return ResponseEntity.noContent().build();
    }

    // -------------------------------------------- Regra de negocio

    /**
     * Registra a resposta "To bem" do cidadao para um check-in.
     */
    @PostMapping("/{id}/responder")
    public Checkin responder(@PathVariable Long id) {
        return bo.registrarResposta(id);
    }

    /**
     * Dispara a escalada para UM check-in (avalia 2h/4h e atualiza status).
     */
    @PostMapping("/{id}/escalar")
    public Checkin escalar(@PathVariable Long id) {
        return bo.processarEscalada(id);
    }

    /**
     * Dispara a escalada para TODOS os check-ins pendentes e devolve os que
     * mudaram nesta execucao.
     */
    @PostMapping("/escalar")
    public List<Checkin> escalarTodos() {
        return bo.executarEscaladaGlobal();
    }

    /**
     * Consulta de valor: idosos sem resposta em bairro com alerta VERMELHO ativo.
     */
    @GetMapping("/cidadaos-em-risco")
    public List<CidadaoEmRisco> cidadaosEmRisco() {
        return bo.cidadaosEmRisco();
    }
}
