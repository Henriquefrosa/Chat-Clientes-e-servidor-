package Servidor;

import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {
    private static Map<String, ClienteHandler> clientes = new HashMap<>();
    private static Map<String, Set<ClienteHandler>> salas = new HashMap<>();
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        System.out.println("Servidor rodando na porta 12345...");
        while (true) {
            Socket socket = serverSocket.accept();
            new ClienteHandler(socket).start();
        }
    }



    public static synchronized void registrarCliente(String nome, ClienteHandler handler) {
        clientes.put(nome, handler);
    }



    public static synchronized void removerCliente(String nome, ClienteHandler handler) {
        clientes.remove(nome);
        for (Set<ClienteHandler> sala : salas.values()) {
            sala.remove(handler);
        }
    }

    public static synchronized ClienteHandler getCliente(String nome) {
        return clientes.get(nome);
    }



    public static synchronized Set<String> listarClientes() {
        return clientes.keySet();
    }



    public static synchronized void entrarNaSala(String nomeSala, ClienteHandler handler) {
        salas.putIfAbsent(nomeSala, new HashSet<>());
        for (Set<ClienteHandler> sala : salas.values()) {
            sala.remove(handler); // remove de outras salas
        }
        salas.get(nomeSala).add(handler);
    }

    public static synchronized Set<ClienteHandler> getSala(String nomeSala) {
        return salas.getOrDefault(nomeSala, new HashSet<>());
    }
}