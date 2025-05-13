package ua.gov.court.supreme.sevhelper.servlet;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
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
        Set<Session> invalidSessions = new HashSet<>();

        for (Session session : sessions) {
            try {
                if (session.isOpen()) {
                    session.getBasicRemote().sendText("reload");
                } else {
                    // The session is closed but remains on the list
                    invalidSessions.add(session);
                }
            } catch (IOException e) {
                // Error while sending, most likely invalid session
                System.err.println("Error sending message to client: " + e.getMessage());
                invalidSessions.add(session);
            }
        }

        // Removing invalid sessions
        if (!invalidSessions.isEmpty()) {
            sessions.removeAll(invalidSessions);
            System.out.println("Deleted " + invalidSessions.size() + " invalid WebSocket sessions");
        }
    }
}
