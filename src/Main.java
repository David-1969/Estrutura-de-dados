import java.util.Random;
import java.util.Scanner;

public class Main {

    private static final String RESET = "\u001B[0m";
    private static final String AMARELO = "\u001B[43m";
    private static final String VERMELHO = "\u001B[41m";
    private static final String AZUL = "\u001B[44m";
    private static final String PRETO = "\u001B[40m";
    private static final String BRANCO = "\u001B[47m";

    private static String[] cores;
    private static String[] tabuleiro;
    private static boolean[] revelado;
    private static String participante1, participante2;
    private static int[] pontuacoes = {0, 0};
    private static int tamanhoTabuleiro;
    private static String[][] historicoPontuacao = new String[10][2];
    private static String corParticipante1, corParticipante2;

    private static void embaralharCores() {
        Random random = new Random();
        for (int i = cores.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            String temp = cores[i];
            cores[i] = cores[j];
            cores[j] = temp;
        }
    }


    private static void exibirTabuleiro(String[] tabuleiro, boolean[] revelado) {
        int size = (int) Math.sqrt(tamanhoTabuleiro);
        System.out.println("TABULEIRO:");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int pos = i * size + j;
                if (revelado[pos]) {
                    String cor = tabuleiro[pos];
                    switch (cor) {
                        case "Amarelo":
                            System.out.print(AMARELO + " ■ " + RESET);
                            break;
                        case "Vermelho":
                            System.out.print(VERMELHO + " ■ " + RESET);
                            break;
                        case "Azul":
                            System.out.print(AZUL + " ■ " + RESET);
                            break;
                        case "Preto":
                            System.out.print(PRETO + " ■ " + RESET);
                            break;
                        default:
                            System.out.print(BRANCO + " ■ " + RESET);
                            break;
                    }
                } else {
                    System.out.print(BRANCO + " ■ " + RESET);
                }
            }
            System.out.println();
        }
    }

    private static void jogar(String[] tabuleiro, boolean[] revelado, int jogador) {
        Scanner scanner = new Scanner(System.in);
        int pos1, pos2;

        System.out.println("\nJogador " + (jogador == 1 ? participante1 : participante2) + " (Sua vez - Pontuação: " + pontuacoes[jogador - 1] + " pontos):");
        exibirTabuleiro(tabuleiro, revelado);
        System.out.println("Escolha a primeira posição (1-" + tamanhoTabuleiro + "): ");
        pos1 = scanner.nextInt() - 1;
        while (pos1 < 0 || pos1 >= tamanhoTabuleiro || revelado[pos1]) {
            System.out.println("Posição inválida ou já revelada! Tente novamente.");
            pos1 = scanner.nextInt() - 1;
        }
        revelado[pos1] = true;
        exibirTabuleiro(tabuleiro, revelado);
        System.out.println("Escolha a segunda posição (1-" + tamanhoTabuleiro + "): ");
        pos2 = scanner.nextInt() - 1;
        while (pos2 < 0 || pos2 >= tamanhoTabuleiro || pos1 == pos2 || revelado[pos2]) {
            System.out.println("Posição inválida ou já revelada! Tente novamente.");
            pos2 = scanner.nextInt() - 1;
        }
        revelado[pos2] = true;
        exibirTabuleiro(tabuleiro, revelado);
            if (tabuleiro[pos1].equals(tabuleiro[pos2])) {
            System.out.println("Parabéns! Jogue novamente.");
            if (tabuleiro[pos1].equals("Preto")) {
                pontuacoes[jogador - 1] += 50;
            } else if (tabuleiro[pos1].equals("Azul") || tabuleiro[pos1].equals("Vermelho")) {
                pontuacoes[jogador - 1] += 10;
            } else if (tabuleiro[pos1].equals("Amarelo")) {
                pontuacoes[jogador - 1] += 5;
            }
        } else {
            if (tabuleiro[pos1].equals("Preto") || tabuleiro[pos2].equals("Preto")) {
                pontuacoes[jogador - 1] -= 50;
                if (pontuacoes[jogador - 1] < 0) {
                    pontuacoes[jogador - 1] = 0;
                }
                System.out.println("Você perdeu 50 pontos por errar um par de cor preta!");
            } else if ((tabuleiro[pos1].equals(corParticipante1) && tabuleiro[pos2].equals(corParticipante2)) ||
                    (tabuleiro[pos2].equals(corParticipante1) && tabuleiro[pos1].equals(corParticipante2))) {
                pontuacoes[jogador - 1] -= 5;
                if (pontuacoes[jogador - 1] < 0) {
                    pontuacoes[jogador - 1] = 0;
                }
                System.out.println("Você perdeu 5 pontos por pegar um quadrado da cor do rival!");
            }
            System.out.println("Par não encontrado!");
            revelado[pos1] = false;
            revelado[pos2] = false;
        }
        mostrarPontuacao(jogador);
    }
    private static void mostrarPontuacao(int jogador) {
        System.out.println("Pontuação Atual de " + (jogador == 1 ? participante1 : participante2) + ": " + pontuacoes[jogador - 1] + " pontos");
    }
    private static void configurarTabuleiro() {
        int numAmarelo = (int) (tamanhoTabuleiro * 0.40);
        int numAzul = (int) (tamanhoTabuleiro * 0.25);
        int numVermelho = (int) (tamanhoTabuleiro * 0.25);
        int numPreto = (int) (tamanhoTabuleiro * 0.10);
        cores = new String[tamanhoTabuleiro];

        for (int i = 0; i < numAmarelo; i++) {
            cores[i] = "Amarelo";
        }
        for (int i = numAmarelo; i < numAmarelo + numAzul; i++) {
            cores[i] = "Azul";
        }
        for (int i = numAmarelo + numAzul; i < numAmarelo + numAzul + numVermelho; i++) {
            cores[i] = "Vermelho";
        }
        for (int i = numAmarelo + numAzul + numVermelho; i < tamanhoTabuleiro; i++) {
            cores[i] = "Preto";
        }
        embaralharCores();
    }

    private static void exibirRegras() {
        System.out.println("\nREGRAS DO JOGO:");
        System.out.println("1. O objetivo do jogo é encontrar pares de quadradinhos da mesma cor.");
        System.out.println("2. O tabuleiro é composto por quadradinhos de várias cores.");
        System.out.println("3. O jogador escolhe dois quadradinhos e, se forem iguais, o jogador pontua.");
        System.out.println("4. Se o jogador escolher um par da sua cor, ele pontua mais.");
        System.out.println("5. Se os quadradinhos forem da cor preta, o jogador perde 50 pontos.");
        System.out.println("6. A pontuação é atualizada ao final de cada rodada.");
        System.out.println("7. Quando um jogador encontra um par, ele pode jogar novamente.");
        System.out.println("8. O jogo termina quando todos os pares forem encontrados.");
        System.out.println("9. Se o jogador pegar um quadrado da cor do rival e errar o par, ele perde 5 pontos.");
        System.out.println("10. O jogador com a maior pontuação ao final do jogo vence.\n");
    }
    private static void exibirHistorico() {
        System.out.println("\nHISTÓRICO DE PONTUAÇÃO:");
        for (int i = 0; i < historicoPontuacao.length; i++) {
            if (historicoPontuacao[i][0] != null) {
                System.out.println(historicoPontuacao[i][0] + ": " + historicoPontuacao[i][1] + " pontos");
            }
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMENU:");
            System.out.println("1. INICIAR");
            System.out.println("2. PONTUAÇÃO PARTICIPANTES");
            System.out.println("3. REGRAS DO JOGO");
            System.out.println("4. SAIR");
            System.out.print("Escolha uma opção (1-4): ");
            int opcao = scanner.nextInt();

            if (opcao == 1) {
                System.out.println("\nCONFIGURAÇÕES DO JOGO");
                System.out.println("QUAL O TAMANHO DE TABULEIRO DESEJA JOGAR?");
                System.out.println("a. 4 x 4");
                System.out.println("b. 6 x 6");
                System.out.println("c. 8 x 8");
                System.out.println("d. 10 x 10");
                System.out.print("DIGITE A OPÇÃO: ");
                String opcaoTabuleiro = scanner.next();
                switch (opcaoTabuleiro) {
                    case "a":
                        tamanhoTabuleiro = 16; // 6x6
                        break;
                    case "b":
                        tamanhoTabuleiro = 36; // 8x8
                        break;
                    case "c":
                        tamanhoTabuleiro = 64; // 10x10
                        break;
                    case "d":
                        tamanhoTabuleiro = 100; // 10x10
                        break;
                    default:
                        System.out.println("Opção inválida! Usando tamanho 6x6 por padrão.");
                        tamanhoTabuleiro = 36;
                }
                configurarTabuleiro();
                tabuleiro = new String[tamanhoTabuleiro];
                revelado = new boolean[tamanhoTabuleiro];
                System.arraycopy(cores, 0, tabuleiro, 0, cores.length);

                scanner.nextLine();
                System.out.print("QUAL O APELIDO DA(O) PARTICIPANTE 1? DIGITE O APELIDO: ");
                participante1 = scanner.nextLine();
                System.out.print("QUAL O APELIDO DA(O) PARTICIPANTE 2? DIGITE O APELIDO: ");
                participante2 = scanner.nextLine();
                System.out.println(participante1 + ", escolha sua cor (Azul ou Vermelho): ");
                corParticipante1 = scanner.nextLine();
                while (!(corParticipante1.equals("Azul") || corParticipante1.equals("Vermelho"))) {
                    System.out.println("Cor inválida! Escolha entre Azul ou Vermelho.");
                    corParticipante1 = scanner.nextLine();
                }
                corParticipante2 = corParticipante1.equals("Azul") ? "Vermelho" : "Azul";
                while (true) {
                    jogar(tabuleiro, revelado, 1);
                    jogar(tabuleiro, revelado, 2);
                }
            } else if (opcao == 2) {
                exibirHistorico();
            } else if (opcao == 3) {
                exibirRegras();
            } else if (opcao == 4) {
                System.out.println("\nObrigado por jogar! Até logo!");
                break;
            } else {
                System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }
}

