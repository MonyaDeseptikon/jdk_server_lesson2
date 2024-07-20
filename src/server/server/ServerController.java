package server.server;

import server.client.ClientController;
import server.server.gui.ServerView;
import server.server.storage.Repository;

import java.util.ArrayList;
import java.util.List;


public class ServerController {
    private boolean isWorking;
    private ServerView serverView;
    private List<ClientController> clientList;
    private Repository repository;

    public ServerController(ServerView serverView, Repository repository) {
        this.serverView = serverView;
        this.repository = repository;
        clientList = new ArrayList<>();
        serverView.setServerController(this);
    }

    public void start() {
        if (isWorking) {
            showOnWindow("Сервер уже был запущен");
        } else {
            isWorking = true;
            showOnWindow("Сервер запущен!");
        }
    }

    public void stop() {
        if (!isWorking) {
            showOnWindow("Сервер уже был остановлен");
        } else {
            isWorking = false;
            while (!clientList.isEmpty()) {
                disconnectUser(clientList.get(clientList.size() - 1));
            }
            showOnWindow("Сервер остановлен!");
        }
    }

    public void disconnectUser(ClientController clientController) {
        clientList.remove(clientController);
        if (clientController != null) {
            clientController.disconnectedFromServer();
            showOnWindow(clientController.getName() + " отключился от беседы");
        }
    }

    public boolean connectUser(ClientController clientController) {
        if (!isWorking) {
            return false;
        }
        clientList.add(clientController);
        showOnWindow(clientController.getName() + " подключился к беседе");
        return true;
    }

    public void message(String text) {
        if (!isWorking) {
            return;
        }
        showOnWindow(text);
        answerAll(text);
        saveInHistory(text);
    }

    private void answerAll(String text) {
        for (ClientController clientController : clientList) {
            clientController.answerFromServer(text);
        }
    }

    private void showOnWindow(String text) {
        serverView.showMessage(text + "\n");
    }

    private void saveInHistory(String text) {
        repository.save(text);
    }

    public String getHistory() {
        return repository.read();
    }

}
