package Cliente;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 12345);

        Scanner teclado = new Scanner(System.in);
        BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);

        InputStream in = socket.getInputStream();  // Já pega o input stream uma vez.

        // Thread para receber mensagens
        new Thread(() -> {
            try {
                while (true) {
                    String linha = entrada.readLine();
                    if (linha == null) break;

                    if (linha.startsWith("/file")) {
                        int primeiraAspa = linha.indexOf('"');
                        int segundaAspa = linha.indexOf('"', primeiraAspa + 1);

                        String remetente = linha.split(" ")[1];
                        String nomeArquivo = linha.substring(primeiraAspa + 1, segundaAspa);
                        String resto = linha.substring(segundaAspa + 1).trim();
                        long tamanho = Long.parseLong(resto);

                        FileOutputStream fos = new FileOutputStream(nomeArquivo);

                        byte[] buffer = new byte[4096];
                        long total = 0;
                        while (total < tamanho) {
                            int count = in.read(buffer, 0, (int)Math.min(buffer.length, tamanho - total));
                            if (count == -1) break;
                            fos.write(buffer, 0, count);
                            total += count;
                        }
                        fos.close();
                        System.out.println("Arquivo '" + nomeArquivo + "' recebido de " + remetente);
                    }
                    else {
                        System.out.println(linha);
                    }
                }
            } catch (IOException e) {
                System.out.println("Conexão encerrada.");
            }
        }).start();

        // Envia comandos para o servidor
        while (true) {
            String comando = teclado.nextLine();
            saida.println(comando);
            if (comando.equals("/sair")) {
                break;
            }
        }

        socket.close();
    }
}
