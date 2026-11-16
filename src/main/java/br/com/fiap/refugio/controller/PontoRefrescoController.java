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

import br.com.fiap.refugio.bo.PontoRefrescoBO;
import br.com.fiap.refugio.model.PontoDeRefresco;

/**
 * Endpoints REST de Ponto de Refresco.
 */
@RestController
@RequestMapping("/api/pontos-refresco")
public class PontoRefrescoController {

    private final PontoRefrescoBO bo;

    public PontoRefrescoController(PontoRefrescoBO bo) {
        this.bo = bo;
    }

    @GetMapping
    public List<PontoDeRefresco> listar() {
        return bo.listar();
    }

    @GetMapping("/{id}")
    public PontoDeRefresco buscar(@PathVariable Long id) {
        return bo.buscar(id);
    }

    @PostMapping
    public ResponseEntity<PontoDeRefresco> criar(@RequestBody PontoDeRefresco ponto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bo.criar(ponto));
    }

    @PutMapping("/{id}")
    public PontoDeRefresco atualizar(@PathVariable Long id, @RequestBody PontoDeRefresco ponto) {
        return bo.atualizar(id, ponto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        bo.remover(id);
        return ResponseEntity.noContent().build();
    }
}
