package com.arthur.sportsclubscoringsystem.controller;

import com.arthur.sportsclubscoringsystem.dto.CampeonatoRequestDTO;
import com.arthur.sportsclubscoringsystem.dto.CampeonatoResponseDTO;
import com.arthur.sportsclubscoringsystem.service.CampeonatoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campeonatos")
public class CampeonatoController {

    private final CampeonatoService campeonatoService;

    public CampeonatoController(CampeonatoService campeonatoService) {
        this.campeonatoService = campeonatoService;
    }

    @PostMapping
    public ResponseEntity<CampeonatoResponseDTO> criarCampeonato(@RequestBody CampeonatoRequestDTO dto) {
        CampeonatoResponseDTO novoCampeonato = campeonatoService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCampeonato);
    }

    @GetMapping
    public ResponseEntity<List<CampeonatoResponseDTO>> listarCampeonatos() {
        List<CampeonatoResponseDTO> campeonatos = campeonatoService.listarTodos();
        return ResponseEntity.ok(campeonatos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampeonatoResponseDTO> buscarCampeonatoPorId(@PathVariable Long id) {
        CampeonatoResponseDTO campeonato = campeonatoService.buscarPorId(id);
        return ResponseEntity.ok(campeonato);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CampeonatoResponseDTO> atualizarCampeonato(@PathVariable Long id, @RequestBody CampeonatoRequestDTO dto) {
        CampeonatoResponseDTO campeonatoAtualizado = campeonatoService.atualizar(id, dto);
        return ResponseEntity.ok(campeonatoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCampeonato(@PathVariable Long id) {
        campeonatoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}