package org.icechamps.server;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Supervises the tcp server
 *
 * @author Robert.Diaz
 * @since 1.0, 01/06/2014
 */
public class TcpSupervisor extends UntypedActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private final int port;

    public TcpSupervisor(int port) {
        this.port = port;
    }

    @Override
    public void preStart() throws Exception {
        getContext().actorOf(TcpServerActor.make(getSelf(), port), "TcpServer");
    }

    @Override
    public void onReceive(Object message) throws Exception {
        log.info(message.toString());
    }

    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create();

        actorSystem.actorOf(Props.create(TcpSupervisor.class, 9008), "TcpSupervisor");
    }
}
