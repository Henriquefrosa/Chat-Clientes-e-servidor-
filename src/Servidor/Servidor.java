package Servidor;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Servidor {
    private static Map<String, ClienteHandler> clientes = new HashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        System.out.println("Servidor.Servidor rodando na porta 12345...");

        while (true) {
            Socket socket = serverSocket.accept();
            new ClienteHandler(socket).start();
        }
    }

    public static synchronized void registrarCliente(String nome, ClienteHandler handler) {
        clientes.put(nome, handler);
    }

    public static synchronized void removerCliente(String nome) {
        clientes.remove(nome);
    }

    public static synchronized Set<String> listarClientes() {
        return clientes.keySet();
    }

    public static synchronized ClienteHandler getCliente(String nome) {
        return clientes.get(nome);
    }
}

