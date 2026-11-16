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

import br.com.fiap.refugio.bo.AlertaBO;
import br.com.fiap.refugio.model.Alerta;

/**
 * Endpoints REST de Alerta de calor.
 */
@RestController
@RequestMapping("/api/alertas")
public class AlertaController {

    private final AlertaBO bo;

    public AlertaController(AlertaBO bo) {
        this.bo = bo;
    }

    @GetMapping
    public List<Alerta> listar() {
        return bo.listar();
    }

    @GetMapping("/{id}")
    public Alerta buscar(@PathVariable Long id) {
        return bo.buscar(id);
    }

    @PostMapping
    public ResponseEntity<Alerta> criar(@RequestBody Alerta alerta) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bo.criar(alerta));
    }

    @PutMapping("/{id}")
    public Alerta atualizar(@PathVariable Long id, @RequestBody Alerta alerta) {
        return bo.atualizar(id, alerta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        bo.remover(id);
        return ResponseEntity.noContent().build();
    }
}
