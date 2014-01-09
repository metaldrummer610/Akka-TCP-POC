package org.icechamps.server;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;
import com.google.gson.Gson;
import org.icechamps.common.protocol.RequestPacket;
import org.icechamps.common.protocol.ResponsePacket;

/**
 * Handles the incoming messages from the network
 *
 * @author Robert.Diaz
 * @since 1.0, 01/06/2014
 */
public class JsonHandler extends UntypedActor {
    private final Gson gson = new Gson();
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private ActorRef connection;

    public JsonHandler(ActorRef connection) {
        log.info("Connection: {}", connection);
        this.connection = connection;

        getContext().watch(connection);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        log.debug(message.toString());
        if (message instanceof Tcp.Received) {
            final ByteString data = ((Tcp.Received) message).data();
            handle(data);
        } else if (message instanceof Tcp.ConnectionClosed) {
            log.info("Connection closed");
            getContext().stop(getSelf());
        } else {
            log.warning("The following message is unhandled: {}", message);
            unhandled(message);
        }
    }

    private void handle(ByteString data) {
        log.debug("Received: " + data.compact().utf8String());
        RequestPacket requestPacket = new RequestPacket(data.toByteBuffer());

        log.debug("Parsed the following requestPacket: " + requestPacket.toString());

        ResponsePacket responsePacket = new ResponsePacket();
        responsePacket.setId(requestPacket.getId());
        responsePacket.setType(requestPacket.getType());
        responsePacket.setSuccess(true);

        ByteString response = ByteString.fromByteBuffer(responsePacket.toBuffer());
        log.debug("Response: {}", response.compact().utf8String());

        connection.tell(TcpMessage.write(response), getSelf());
        log.debug("Wrote response");
    }
}

