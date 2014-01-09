package org.icechamps.server;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.io.Tcp;
import akka.io.TcpMessage;

import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * Listens for incoming connections and hands off fully connected connections to other actors
 *
 * @author Robert.Diaz
 * @since 1.0, 01/06/2014
 */
public class TcpServerActor extends UntypedActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private final ActorRef manager;
    private final int port;

    public TcpServerActor(ActorRef manager, int port) {
        this.manager = manager;
        this.port = port;
    }

    public static Props make(ActorRef manager, int port) {
        return Props.create(TcpServerActor.class, manager, port);
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        log.info("Starting server");

        final ActorRef tcp = Tcp.get(getContext().system()).manager();
        tcp.tell(TcpMessage.bind(getSelf(), new InetSocketAddress(port), 100), getSelf());
        log.info("Server started on port " + port);
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
        log.info("Unbinding server");

        final ActorRef tcp = Tcp.get(getContext().system()).manager();
        tcp.tell(TcpMessage.unbind(), getSelf());
        log.info("Server unbound");
    }

    @Override
    public void onReceive(Object message) {
        log.info(message.toString());
        if (message instanceof Tcp.Bound) {
            manager.tell(message, getSelf());
        } else if (message instanceof Tcp.CommandFailed) {
            getContext().stop(getSelf());
        } else if (message instanceof Tcp.Connected) {
            final Tcp.Connected conn = (Tcp.Connected) message;
            manager.tell(conn, getSelf());

            final ActorRef handler = getContext().actorOf(Props.create(JsonHandler.class, getSender()), "JsonHandler::" + UUID.randomUUID().toString());
            getSender().tell(TcpMessage.register(handler), getSelf());
        }
    }
}
