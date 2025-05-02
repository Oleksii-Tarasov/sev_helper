package ua.gov.court.supreme.sevhelper.servlet;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/websocket/dbupdate")
public class SevUsersUpdWebSocket {
    public static final Set<Session> sessions = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    public static void notifyClients() {
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText("reload");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
