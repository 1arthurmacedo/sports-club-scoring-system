package com.arthur.sportsclubscoringsystem.controller;

import com.arthur.sportsclubscoringsystem.dto.ClubeRequestDTO;
import com.arthur.sportsclubscoringsystem.dto.ClubeResponseDTO;
import com.arthur.sportsclubscoringsystem.service.ClubeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clubes")
public class ClubeController {

    private final ClubeService clubeService;

    public ClubeController(ClubeService clubeService) {
        this.clubeService = clubeService;
    }

    @PostMapping
    public ResponseEntity<ClubeResponseDTO> criarClube(@RequestBody ClubeRequestDTO dto) {
        ClubeResponseDTO novoClube = clubeService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoClube);
    }

    @GetMapping
    public ResponseEntity<List<ClubeResponseDTO>> listarClubes() {
        List<ClubeResponseDTO> clubes = clubeService.buscarTodos(); // Corrigido para buscarTodos()
        return ResponseEntity.ok(clubes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubeResponseDTO> buscarClubePorId(@PathVariable Long id) {
        ClubeResponseDTO clube = clubeService.buscarPorId(id);
        return ResponseEntity.ok(clube);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClubeResponseDTO> atualizarClube(@PathVariable Long id, @RequestBody ClubeRequestDTO dto) {
        ClubeResponseDTO clubeAtualizado = clubeService.atualizar(id, dto);
        return ResponseEntity.ok(clubeAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarClube(@PathVariable Long id) {
        clubeService.excluir(id); // Corrigido para excluir(id)
        return ResponseEntity.noContent().build();
    }
}