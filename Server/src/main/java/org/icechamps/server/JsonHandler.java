package org.icechamps.server;

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
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {
        log.info(message.toString());
        if (message instanceof Tcp.Received) {
            final ByteString data = ((Tcp.Received) message).data();
            handle(data);
        } else if (message instanceof Tcp.ConnectionClosed) {
            log.info("Connection closed");
            getContext().stop(getSelf());
        } else {
            unhandled(message);
        }
    }

    private void handle(ByteString data) {
        log.info("Received: " + data.compact().utf8String());
        RequestPacket requestPacket = new RequestPacket(data.toByteBuffer());

        log.info("Parsed the following requestPacket: " + requestPacket.toString());

        ResponsePacket responsePacket = new ResponsePacket();
        responsePacket.setId(requestPacket.getId());
        responsePacket.setType(requestPacket.getType());
        responsePacket.setSuccess(true);

        getSender().tell(TcpMessage.write(ByteString.fromByteBuffer(responsePacket.toBuffer())), getSelf());
    }
}

