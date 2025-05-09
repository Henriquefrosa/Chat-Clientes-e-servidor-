# Chat Cliente-Servidor com Salas e Envio de Arquivos

Projeto desenvolvido para a disciplina de **Programação em Dispositivos Móveis**, com o objetivo de implementar um sistema de chat baseado na arquitetura cliente-servidor, com funcionalidades como troca de mensagens privadas, salas de bate-papo e envio de arquivos entre usuários.

## Integrantes do Grupo

- Roger de Azevedo  
- [Adicione aqui os demais nomes dos integrantes]

## Descrição Geral

Este sistema de chat foi implementado em Java utilizando **Sockets TCP** para comunicação entre cliente e servidor. O sistema permite que múltiplos usuários se conectem simultaneamente ao servidor, entrem em salas, enviem mensagens públicas e privadas, além de compartilhar arquivos entre si.

O projeto é composto por duas aplicações:
- **Servidor**: responsável por gerenciar conexões, salas, mensagens e arquivos.
- **Cliente**: interface em console que permite interação com o sistema.

## Funcionalidades Implementadas

- ✅ Conexão de múltiplos clientes via socket TCP.
- ✅ Registro automático de usuários.
- ✅ Criação e entrada em salas de bate-papo.
- ✅ Envio de mensagens para a sala atual.
- ✅ Envio de mensagens privadas.
- ✅ Envio de arquivos entre usuários.
- ✅ Comando para listar usuários conectados.
- ✅ Comando `/help` com lista de comandos disponíveis.
- ✅ Log de conexões com IP, usuário e horário.

## Comandos Disponíveis

| Comando | Descrição |
|--------|------------|
| `/join <sala>` | Entra ou cria uma nova sala. |
| `/send room <mensagem>` | Envia uma mensagem para todos os usuários da sala atual. |
| `/send message <destinatário> <mensagem>` | Envia uma mensagem privada. |
| `/send file <destinatário> <caminho_arquivo>` | Envia um arquivo para outro usuário. |
| `/users` | Lista todos os usuários conectados. |
| `/help` | Exibe a lista de comandos disponíveis. |
| `/sair` | Encerra a conexão com o servidor. |

## Requisitos

- Java 8 ou superior
- Sistema operacional compatível com execução de Java
- IDE  IntelliJ IDEA

## Como Executar

# 1. Abre o diretorio na IDE

# 2. Rode as classes java com o metodo main implementado, no caso desse projeto as classes Cliente e Servidor.

## OBS: Rode primeiro a classe servidor





