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
            System.out.println("\n===== SISTEMA DE PONTUAÇÃO ESPORTIVA =====");
            System.out.println("1. Menu de Clubes");
            System.out.println("2. Menu de Campeonatos");
            System.out.println("3. Menu de Participações");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> menuClubes(scanner);
                case "2" -> menuCampeonatos(scanner);
                case "3" -> menuParticipacoes(scanner);
                case "0" -> {
                    rodando = false;
                    System.out.println("\nSistema encerrado!");
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    // --- SUBMENUS ---
    private void menuClubes(Scanner scanner) {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n--- GERENCIAR CLUBES ---");
            System.out.println("1. Cadastrar Clube");
            System.out.println("2. Listar Clubes");
            System.out.println("3. Editar Clube");
            System.out.println("4. Excluir Clube");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> cadastrarClube(scanner);
                case "2" -> listarClubes();
                case "3" -> editarClube(scanner);
                case "4" -> excluirClube(scanner);
                case "0" -> voltar = true;
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void menuCampeonatos(Scanner scanner) {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n--- GERENCIAR CAMPEONATOS ---");
            System.out.println("1. Cadastrar Campeonato");
            System.out.println("2. Listar Campeonatos");
            System.out.println("3. Editar Campeonato");
            System.out.println("4. Excluir Campeonato");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> cadastrarCampeonato(scanner);
                case "2" -> listarCampeonatos();
                case "3" -> editarCampeonato(scanner);
                case "4" -> excluirCampeonato(scanner);
                case "0" -> voltar = true;
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void menuParticipacoes(Scanner scanner) {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n--- GERENCIAR PARTICIPAÇÕES ---");
            System.out.println("1. Registrar Participação / Pontuação");
            System.out.println("2. Listar Participações");
            System.out.println("3. Editar Participação");
            System.out.println("4. Excluir Participação");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> registrarParticipacao(scanner);
                case "2" -> listarParticipacoes();
                case "3" -> editarParticipacao(scanner);
                case "4" -> excluirParticipacao(scanner);
                case "0" -> voltar = true;
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    // --- OPERAÇÕES DE CLUBE ---
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

    private void editarClube(Scanner scanner) {
        try {
            System.out.print("\nID do Clube a editar: ");
            Long id = Long.parseLong(scanner.nextLine());

            System.out.print("Novo Nome do Clube: ");
            String nome = scanner.nextLine();

            System.out.print("Novo Nome do Dono: ");
            String dono = scanner.nextLine();

            System.out.print("Nova Data de Fundação (dd/MM/yyyy): ");
            String dataStr = scanner.nextLine();
            LocalDate dataFundacao = LocalDate.parse(dataStr, dateFormatter);

            ClubeRequestDTO dto = new ClubeRequestDTO();
            dto.setNome(nome);
            dto.setDono(dono);
            dto.setDataFundacao(dataFundacao);

            ClubeResponseDTO atualizado = clubeService.atualizar(id, dto);
            System.out.println("-> Clube atualizado com sucesso! ID: " + atualizado.getId());

        } catch (Exception e) {
            System.out.println("Erro ao editar clube: " + e.getMessage());
        }
    }

    private void excluirClube(Scanner scanner) {
        try {
            System.out.print("\nID do Clube a ser excluído: ");
            Long id = Long.parseLong(scanner.nextLine());

            clubeService.excluir(id);
            System.out.println("-> Clube excluído com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao excluir clube: " + e.getMessage());
        }
    }

    // --- OPERAÇÕES DE CAMPEONATO ---
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
        var lista = campeonatoService.buscarTodos();
        if (lista.isEmpty()) {
            System.out.println("Nenhum campeonato cadastrado.");
        } else {
            lista.forEach(c -> System.out.printf("ID: %d | Nome: %s | Nível: %s%n",
                    c.getId(), c.getNome(), c.getNivel()));
        }
    }

    private void editarCampeonato(Scanner scanner) {
        try {
            System.out.print("\nID do Campeonato a editar: ");
            Long id = Long.parseLong(scanner.nextLine());

            System.out.print("Novo Nome do Campeonato: ");
            String nome = scanner.nextLine();

            System.out.println("Opções de Nível: ESTADUAL, NACIONAL, INTERNACIONAL");
            System.out.print("Digite o Novo Nível: ");
            String nivelStr = scanner.nextLine().toUpperCase();
            Nivel nivel = Nivel.valueOf(nivelStr);

            System.out.print("Nova Data de Início (dd/MM/yyyy): ");
            LocalDate dataInicio = LocalDate.parse(scanner.nextLine(), dateFormatter);

            System.out.print("Nova Data de Fim (dd/MM/yyyy): ");
            LocalDate dataFim = LocalDate.parse(scanner.nextLine(), dateFormatter);

            CampeonatoRequestDTO dto = new CampeonatoRequestDTO();
            dto.setNome(nome);
            dto.setNivel(nivel);
            dto.setDataInicio(dataInicio);
            dto.setDataFim(dataFim);

            CampeonatoResponseDTO atualizado = campeonatoService.atualizar(id, dto);
            System.out.println("-> Campeonato atualizado com sucesso! ID: " + atualizado.getId());

        } catch (Exception e) {
            System.out.println("Erro ao editar campeonato: " + e.getMessage());
        }
    }

    private void excluirCampeonato(Scanner scanner) {
        try {
            System.out.print("\nID do Campeonato a ser excluído: ");
            Long id = Long.parseLong(scanner.nextLine());

            campeonatoService.excluir(id);
            System.out.println("-> Campeonato excluído com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao excluir campeonato: " + e.getMessage());
        }
    }

    // --- OPERAÇÕES DE PARTICIPAÇÃO ---
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

    private void editarParticipacao(Scanner scanner) {
        try {
            System.out.print("\nID da Participação a editar: ");
            Long id = Long.parseLong(scanner.nextLine());

            System.out.print("Novo ID do Clube: ");
            Long clubeId = Long.parseLong(scanner.nextLine());

            System.out.print("Novo ID do Campeonato: ");
            Long campeonatoId = Long.parseLong(scanner.nextLine());

            System.out.println("Opções de Posição: PRIMEIRO, SEGUNDO, TERCEIRO, OUTRO");
            System.out.print("Digite a Nova Posição: ");
            String posicaoStr = scanner.nextLine().toUpperCase();
            Posicao posicao = Posicao.valueOf(posicaoStr);

            ParticipacaoRequestDTO dto = new ParticipacaoRequestDTO();
            dto.setIdClube(clubeId);
            dto.setIdCampeonato(campeonatoId);
            dto.setPosicao(posicao);

            ParticipacaoResponseDTO atualizada = participacaoService.atualizar(id, dto);

            System.out.println("-> Participação atualizada com sucesso!");
            System.out.printf("   [ID: %d] Clube: %s | Campeonato: %s | Posição: %s%n",
                    atualizada.getId(), atualizada.getNomeClube(), atualizada.getNomeCampeonato(), atualizada.getPosicao());

        } catch (Exception e) {
            System.out.println("Erro ao editar participação: " + e.getMessage());
        }
    }

    private void excluirParticipacao(Scanner scanner) {
        try {
            System.out.print("\nID da Participação a ser excluída: ");
            Long id = Long.parseLong(scanner.nextLine());

            participacaoService.excluir(id);
            System.out.println("-> Participação excluída com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao excluir participação: " + e.getMessage());
        }
    }
}