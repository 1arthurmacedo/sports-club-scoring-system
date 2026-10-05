package com.arthur.sportsclubscoringsystem;

import com.arthur.sportsclubscoringsystem.dto.*;
import com.arthur.sportsclubscoringsystem.enums.Nivel;
import com.arthur.sportsclubscoringsystem.enums.Posicao;
import com.arthur.sportsclubscoringsystem.service.CampeonatoService;
import com.arthur.sportsclubscoringsystem.service.ClubeService;
import com.arthur.sportsclubscoringsystem.service.ParticipacaoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@Component
public class ConsoleMenu implements CommandLineRunner {

    private final ClubeService clubeService;
    private final CampeonatoService campeonatoService;
    private final ParticipacaoService participacaoService;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ConsoleMenu(ClubeService clubeService,
                       CampeonatoService campeonatoService,
                       ParticipacaoService participacaoService) {
        this.clubeService = clubeService;
        this.campeonatoService = campeonatoService;
        this.participacaoService = participacaoService;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        boolean rodando = true;

        while (rodando) {
            System.out.println("\n=========================================");
            System.out.println("      SISTEMA DE PONTUAÇÃO ESPORTIVA     ");
            System.out.println("=========================================");
            System.out.println("1. Cadastrar Clube");
            System.out.println("2. Listar Clubes");
            System.out.println("3. Cadastrar Campeonato");
            System.out.println("4. Listar Campeonatos");
            System.out.println("5. Registrar Participação / Pontuação");
            System.out.println("6. Listar Participações");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> cadastrarClube(scanner);
                case "2" -> listarClubes();
                case "3" -> cadastrarCampeonato(scanner);
                case "4" -> listarCampeonatos();
                case "5" -> registrarParticipacao(scanner);
                case "6" -> listarParticipacoes();
                case "0" -> {
                    rodando = false;
                    System.out.println("\nSistema encerrado!");
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void cadastrarClube(Scanner scanner) {
        try {
            System.out.print("\nNome do Clube: ");
            String nome = scanner.nextLine();

            System.out.print("Nome do Dono: ");
            String dono = scanner.nextLine();

            System.out.print("Data de Fundação (dd/MM/yyyy): ");
            String dataStr = scanner.nextLine();
            LocalDate dataFundacao = LocalDate.parse(dataStr, dateFormatter);

            ClubeRequestDTO dto = new ClubeRequestDTO();
            dto.setNome(nome);
            dto.setDono(dono);
            dto.setDataFundacao(dataFundacao);

            ClubeResponseDTO salvo = clubeService.salvar(dto);
            System.out.println("-> Clube cadastrado com sucesso! ID: " + salvo.getId());

        } catch (Exception e) {
            System.out.println("Erro ao cadastrar clube: " + e.getMessage());
        }
    }

    private void listarClubes() {
        System.out.println("\n--- LISTA DE CLUBES ---");
        var lista = clubeService.buscarTodos();
        if (lista.isEmpty()) {
            System.out.println("Nenhum clube cadastrado.");
        } else {
            lista.forEach(c -> System.out.printf("ID: %d | Nome: %s | Dono: %s | Pontos: %.1f%n",
                    c.getId(), c.getNome(), c.getDono(), c.getPontuacaoTotal()));
        }
    }

    private void cadastrarCampeonato(Scanner scanner) {
        try {
            System.out.print("\nNome do Campeonato: ");
            String nome = scanner.nextLine();

            System.out.println("Opções de Nível: ESTADUAL, NACIONAL, INTERNACIONAL");
            System.out.print("Digite o Nível: ");
            String nivelStr = scanner.nextLine().toUpperCase();
            Nivel nivel = Nivel.valueOf(nivelStr);

            System.out.print("Data de Início (dd/MM/yyyy): ");
            LocalDate dataInicio = LocalDate.parse(scanner.nextLine(), dateFormatter);

            System.out.print("Data de Fim (dd/MM/yyyy): ");
            LocalDate dataFim = LocalDate.parse(scanner.nextLine(), dateFormatter);

            CampeonatoRequestDTO dto = new CampeonatoRequestDTO();
            dto.setNome(nome);
            dto.setNivel(nivel);
            dto.setDataInicio(dataInicio);
            dto.setDataFim(dataFim);

            CampeonatoResponseDTO salvo = campeonatoService.salvar(dto);
            System.out.println("-> Campeonato cadastrado com sucesso! ID: " + salvo.getId());

        } catch (Exception e) {
            System.out.println("Erro ao cadastrar campeonato: " + e.getMessage());
        }
    }

    private void listarCampeonatos() {
        System.out.println("\n--- LISTA DE CAMPEONATOS ---");
        var lista = campeonatoService.buscarTodos(); // <--- Alterado para buscarTodos()
        if (lista.isEmpty()) {
            System.out.println("Nenhum campeonato cadastrado.");
        } else {
            lista.forEach(c -> System.out.printf("ID: %d | Nome: %s | Nível: %s%n",
                    c.getId(), c.getNome(), c.getNivel()));
        }
    }

    private void registrarParticipacao(Scanner scanner) {
        try {
            System.out.print("\nID do Clube: ");
            Long clubeId = Long.parseLong(scanner.nextLine());

            System.out.print("ID do Campeonato: ");
            Long campeonatoId = Long.parseLong(scanner.nextLine());

            System.out.println("Opções de Posição: PRIMEIRO, SEGUNDO, TERCEIRO, OUTRO");
            System.out.print("Digite a Posição: ");
            String posicaoStr = scanner.nextLine().toUpperCase();
            Posicao posicao = Posicao.valueOf(posicaoStr);

            ParticipacaoRequestDTO dto = new ParticipacaoRequestDTO();
            dto.setIdClube(clubeId);
            dto.setIdCampeonato(campeonatoId);
            dto.setPosicao(posicao);

            ParticipacaoResponseDTO salvo = participacaoService.salvar(dto);

            System.out.println("-> Participação registrada com sucesso!");
            System.out.printf("   [ID: %d] Clube: %s | Campeonato: %s | Posição: %s%n",
                    salvo.getId(), salvo.getNomeClube(), salvo.getNomeCampeonato(), salvo.getPosicao());

        } catch (Exception e) {
            System.out.println("Erro ao registrar participação: " + e.getMessage());
        }
    }

    private void listarParticipacoes() {
        System.out.println("\n--- PARTICIPAÇÕES E RESULTADOS ---");
        var lista = participacaoService.buscarTodos();
        if (lista.isEmpty()) {
            System.out.println("Nenhuma participação registrada.");
        } else {
            lista.forEach(p -> System.out.printf("ID: %d | Clube: %s | Campeonato: %s | Posição: %s%n",
                    p.getId(), p.getNomeClube(), p.getNomeCampeonato(), p.getPosicao()));
        }
    }
}