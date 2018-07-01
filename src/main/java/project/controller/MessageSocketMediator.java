package project.controller;

import project.model.Message;
import project.utils.ObjectMapperFactory;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/cinemaSocket/{client-id}")
public class MessageSocketMediator {
    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());

    @OnMessage
    public String onMessage(String message, Session session, @PathParam("client-id") String clientId) {
        try {
            Message convertedMessage = ObjectMapperFactory.OBJECT_MAPPER_WITH_TIMESTAMPS.readValue(message, Message.class);
            System.out.println("received message from client " + clientId);
            for (Session s : peers) {
                try {
                    s.getBasicRemote().sendText(message);
                    System.out.println("send message to peer ");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "message was received by socket mediator and processed: " + message;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("client-id") String clientId) {
        System.out.println("mediator: opened websocket channel for client " + clientId);
        peers.add(session);

        try {
            session.getBasicRemote().sendText("good to be in touch");
        } catch (IOException e) {
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("client-id") String clientId) {
        System.out.println("mediator: closed websocket channel for client " + clientId);
        peers.remove(session);
    }
}
