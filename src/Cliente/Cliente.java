package Cliente;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    private static final String IP_SERVIDOR = "127.0.0.1"; // Use IP externo se necessário
    private static final int PORTA = 12345;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(IP_SERVIDOR, PORTA);
            System.out.println("Conectado ao servidor em " + IP_SERVIDOR + ":" + PORTA);

            PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner teclado = new Scanner(System.in);
            InputStream in = socket.getInputStream(); // PEGAR UMA VEZ SÓ

            // Envia nome do usuário
            System.out.print("Digite seu nome de usuário: ");
            String nome = teclado.nextLine();
            saida.println(nome);

            // Exibe comandos disponíveis
            exibirAjuda();

            // Thread para escutar mensagens do servidor
            new Thread(() -> receberMensagens(entrada, in)).start();

            // Loop principal de envio de comandos
            while (true) {
                System.out.print(">> ");
                String comando = teclado.nextLine();
                saida.println(comando);

                if (comando.equals("/sair")) break;
                if (comando.equalsIgnoreCase("/help")) exibirAjuda();
            }

            socket.close();
        } catch (IOException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
        }
    }

    private static void receberMensagens(BufferedReader entrada, InputStream in) {
        try {
            while (true) {
                String linha = entrada.readLine();
                if (linha == null) break;

                if (linha.startsWith("/file")) {
                    // Exemplo: /file user1 "arquivo.txt" 1024
                    int primeiraAspa = linha.indexOf('"');
                    int segundaAspa = linha.indexOf('"', primeiraAspa + 1);

                    String remetente = linha.split(" ")[1];
                    String nomeArquivo = linha.substring(primeiraAspa + 1, segundaAspa);
                    String resto = linha.substring(segundaAspa + 1).trim();
                    long tamanho = Long.parseLong(resto);

                    File dir = new File("downloads");
                    if (!dir.exists()) dir.mkdir();

                    FileOutputStream fos = new FileOutputStream("downloads/" + nomeArquivo);
                    byte[] buffer = new byte[4096];
                    long totalLido = 0;

                    while (totalLido < tamanho) {
                        int count = in.read(buffer, 0, (int)Math.min(buffer.length, tamanho - totalLido));
                        if (count == -1) break;
                        fos.write(buffer, 0, count);
                        totalLido += count;
                    }

                    fos.close();
                    System.out.println("\n Arquivo recebido de " + remetente + ": " + nomeArquivo + " (" + tamanho + " bytes)");
                    System.out.print(">> ");
                } else {
                    System.out.println("\n" + linha);
                    System.out.print(">> ");
                }
            }
        } catch (IOException e) {
            System.out.println(" Conexão encerrada.");
        }
    }

    private static void exibirAjuda() {
        System.out.println("\n Comandos disponíveis:");
        System.out.println("  /join <sala>                 → Entrar ou criar uma sala de chat");
        System.out.println("  /send room <mensagem>        → Enviar mensagem para a sala atual");
        System.out.println("  /send message <dest> <msg>   → Enviar mensagem privada");
        System.out.println("  /send file <dest> <caminho>  → Enviar arquivo para outro usuário");
        System.out.println("  /users                       → Listar usuários conectados");
        System.out.println("  /help                        → Exibir esta lista de comandos");
        System.out.println("  /sair                        → Sair do chat");
        System.out.println();
    }
}
