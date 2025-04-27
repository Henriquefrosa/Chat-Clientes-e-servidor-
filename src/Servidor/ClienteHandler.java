package Servidor;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class ClienteHandler extends Thread {
    private Socket socket;
    private String nome;
    private BufferedReader entrada;
    private PrintWriter saida;

    public ClienteHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            saida = new PrintWriter(socket.getOutputStream(), true);

            // Solicita nome de usuário
            saida.println("Digite seu nome de usuário:");
            nome = entrada.readLine();
            Servidor.registrarCliente(nome, this);

            logConexao();

            System.out.println(nome + " conectou.");

            String linha;
            while ((linha = entrada.readLine()) != null) {
                if (linha.startsWith("/send message")) {
                    tratarMensagemTexto(linha);
                } else if (linha.startsWith("/send file")) {
                    tratarArquivo(linha);
                } else if (linha.equals("/users")) {
                    saida.println("Usuários conectados: " + Servidor.listarClientes());
                } else if (linha.equals("/sair")) {
                    break;
                } else {
                    saida.println("Comando inválido.");
                }
            }

        } catch (IOException e) {
            System.out.println("Erro com cliente " + nome);
        } finally {
            try {
                Servidor.removerCliente(nome);
                socket.close();
                System.out.println(nome + " desconectado.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void tratarMensagemTexto(String linha) {
        String[] partes = linha.split(" ", 4);
        if (partes.length < 4) {
            saida.println("Formato: /send message <destinatario> <mensagem>");
            return;
        }
        String destino = partes[2];
        String mensagem = partes[3];
        ClienteHandler handler = Servidor.getCliente(destino);
        if (handler != null) {
            handler.saida.println(nome + ": " + mensagem);
        } else {
            saida.println("Usuário " + destino + " não encontrado.");
        }
    }

    private void tratarArquivo(String linha) throws IOException {
        String[] partes = linha.split(" ", 4); // <<<< 4 aqui
        if (partes.length < 4) {
            saida.println("Formato correto: /send file <destinatario> <caminho_arquivo>");
            return;
        }

        String destino = partes[2]; // <<<< 2
        String caminho = partes[3]; // <<<< 3

        File file = new File(caminho);
        if (!file.exists()) {
            saida.println("Arquivo não encontrado: " + caminho);
            return;
        }

        ClienteHandler handler = Servidor.getCliente(destino);
        if (handler != null) {
            // Envia metadados
            handler.saida.println("/file " + nome + " \"" + file.getName() + "\" " + file.length());


            // Envia arquivo
            byte[] buffer = new byte[4096];
            OutputStream out = handler.socket.getOutputStream();
            InputStream in = new FileInputStream(file);
            int count;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            out.flush();
            in.close();
        } else {
            saida.println("Usuário " + destino + " não encontrado.");
        }
    }

    private void logConexao() {
        String ip = socket.getInetAddress().getHostAddress();
        String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        try (BufferedWriter log = new BufferedWriter(new FileWriter("log_conexoes.txt", true))) {
            log.write("Usuário: " + nome + " | IP: " + ip + " | Data: " + data + "\n");
        } catch (IOException e) {
            System.out.println("Erro ao gravar log.");
        }
    }
}
