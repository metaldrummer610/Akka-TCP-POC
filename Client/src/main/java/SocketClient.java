import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.icechamps.common.Person;
import org.icechamps.common.Pet;
import org.icechamps.common.protocol.RequestPacket;
import org.icechamps.common.protocol.ResponsePacket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: greg.frady
 * Date: 1/6/14
 * Time: 11:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class SocketClient extends Thread {

    private Logger logger = Logger.getLogger(SocketClient.class);

    private Socket theSocket = null;
    private BufferedOutputStream out = null;
    private BufferedInputStream in = null;

    private int iterations = 500;
    private int maxIntervalMillis = 100;

    public SocketClient(String hostName, int port, int iterations, int maxIntervalMillis) {
        this(hostName, port);
        this.iterations = iterations;
        this.maxIntervalMillis = maxIntervalMillis;
    }

    public SocketClient(String hostName, int port) {
        try {
            theSocket =  new Socket(hostName, port);
            out = new BufferedOutputStream(theSocket.getOutputStream());
            in = new BufferedInputStream(theSocket.getInputStream());
        }
        catch (Exception e) {
            String msg = "Problems encountered while creating connection: ";
            if (hostName == null)
                msg += "No host name was provided!";
            else
                msg += "host: " + hostName + " @ port: " + port;
            logger.error(msg);
        }
        logger.info("Connection created.");
    }

    @Override
    public void run() {

        for (int i = 0; i < iterations; i++) {
            try {
                int interval = getInterval();
                logger.info("Sleep interval: " + interval);
                Thread.sleep(interval);
            }
            catch (Exception e) {
                Thread.currentThread().interrupt();
            }

            exchange();

        }

        die();

    }

    public void die() {

        if (getRandomBoolean()) {
            // die gracefully
            try {
                in.close();
                theSocket.close();
            }
            catch (Exception e) {
                logger.error("Problems encountered while closing connection.");
            }
        }
        else {
            // kill the connection
            in = null;
            theSocket = null;
        }

    }

    private int getInterval() {
        Random random = new Random();
        return random.nextInt(maxIntervalMillis + 1);
    }

    private Boolean getRandomBoolean() {
        if (Math.random() < 5.0)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }


    private void exchange() {
        RequestPacket pkt = new RequestPacket();
        pkt.setId((new Random()).nextInt());

        logger.info("Packet id: " + pkt.getId());

        Gson gson = new Gson();
        if (getRandomBoolean()) {
            pkt.setType(1);
            pkt.setPayload(gson.toJson(getPerson()));
        }
        else {
            pkt.setType(2);
            pkt.setPayload(gson.toJson(getPet()));
        }

        pkt.setPayload(gson.toString());

        try {
            out.write(pkt.toBuffer().array());
            out.flush();
            logger.info("Packet written to the outbound buffer.");
        }
        catch (Exception e) {
            logger.error("Problems encountered while sending data packet: ", e);
        }

        try {
            ResponsePacket response = readFromBuffer();

            logger.info("MESSAGE RECIEVED: ");

            String errors = response.getFailureReason();
            int id = response.getId();
            int type = response.getType();
            Object clazz = response.getClass();

            logger.info("errors: " + errors + ", id: " + id + ", type: " + type + ", class: " + clazz);

         }
        catch (Exception e) {
            logger.error("Problems encountered while receiving data packet: ", e);
        }
    }

    private ResponsePacket readFromBuffer() {
        byte[] buffer = new byte[1024];

        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
        try {
            while(in.read(buffer) != -1) {
                byteBuffer.put(buffer);
            }
        }
        catch (Exception e) {
            logger.error(e);
        }

        return new ResponsePacket(byteBuffer);
    }

    private Person getPerson() {
        Person prsn = new Person();
        prsn.setAge(27);
        prsn.setFirstName("Greg");
        prsn.setLastName("Frady");

        return prsn;
    }

    private Pet getPet() {
        Pet pet = new Pet();
        if (getRandomBoolean()) {
            pet.setAge(5);
            pet.setName("Pepper");
        }
        else {
            pet.setAge(8);
            pet.setName("Korky");
        }
        pet.setOwner(getPerson());

        return pet;
    }

}
