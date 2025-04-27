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
                        String[] partes = linha.split(" ", 4);
                        String remetente = partes[1];
                        String nomeArquivo = partes[2];
                        long tamanho = Long.parseLong(partes[3]);

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
                    } else {
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
