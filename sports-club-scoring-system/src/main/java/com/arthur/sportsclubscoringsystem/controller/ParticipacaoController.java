package com.arthur.sportsclubscoringsystem.controller;

import com.arthur.sportsclubscoringsystem.dto.ParticipacaoRequestDTO;
import com.arthur.sportsclubscoringsystem.dto.ParticipacaoResponseDTO;
import com.arthur.sportsclubscoringsystem.service.ParticipacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/participacoes")
public class ParticipacaoController {

    private final ParticipacaoService participacaoService;

    public ParticipacaoController(ParticipacaoService participacaoService) {
        this.participacaoService = participacaoService;
    }

    @PostMapping
    public ResponseEntity<ParticipacaoResponseDTO> registrarParticipacao(@RequestBody ParticipacaoRequestDTO dto) {
        ParticipacaoResponseDTO novaParticipacao = participacaoService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaParticipacao);
    }

    @GetMapping
    public ResponseEntity<List<ParticipacaoResponseDTO>> listarParticipacoes() {
        List<ParticipacaoResponseDTO> participacoes = participacaoService.buscarTodos(); // Corrigido para buscarTodos()
        return ResponseEntity.ok(participacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParticipacaoResponseDTO> buscarParticipacaoPorId(@PathVariable Long id) {
        ParticipacaoResponseDTO participacao = participacaoService.buscarPorId(id);
        return ResponseEntity.ok(participacao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarParticipacao(@PathVariable Long id) {
        participacaoService.excluir(id); // Corrigido para excluir(id)
        return ResponseEntity.noContent().build();
    }
}