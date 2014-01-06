import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
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
    private BufferedReader in = null;

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
            in = new BufferedReader(new InputStreamReader(theSocket.getInputStream()));

            run();
        }
        catch (Exception e) {
            String msg = "Problems encountered while creating connection: ";
            if (hostName == null)
                msg += "No host name was provided!";
            else
                msg += "host: " + hostName + " @ port: " + port;
            logger.error(msg);
        }
    }

    @Override
    public void run() {

        for (int i = 0; i < iterations; i++) {
            try {
                Thread.sleep(getInterval());
            }
            catch (Exception e) {
                Thread.currentThread().interrupt();
            }


        }

    }

    public Object send() {

    }

    public void die() {

        if (Math.random() < 5.0) {
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

}
