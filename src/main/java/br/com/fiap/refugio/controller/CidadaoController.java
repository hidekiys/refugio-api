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

import br.com.fiap.refugio.bo.CidadaoBO;
import br.com.fiap.refugio.model.Cidadao;

/**
 * Endpoints REST de Cidadao.
 */
@RestController
@RequestMapping("/api/cidadaos")
public class CidadaoController {

    private final CidadaoBO bo;

    public CidadaoController(CidadaoBO bo) {
        this.bo = bo;
    }

    @GetMapping
    public List<Cidadao> listar() {
        return bo.listar();
    }

    @GetMapping("/{id}")
    public Cidadao buscar(@PathVariable Long id) {
        return bo.buscar(id);
    }

    @PostMapping
    public ResponseEntity<Cidadao> criar(@RequestBody Cidadao cidadao) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bo.criar(cidadao));
    }

    @PutMapping("/{id}")
    public Cidadao atualizar(@PathVariable Long id, @RequestBody Cidadao cidadao) {
        return bo.atualizar(id, cidadao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        bo.remover(id);
        return ResponseEntity.noContent().build();
    }
}
