import org.apache.log4j.BasicConfigurator;

/**
 * Created with IntelliJ IDEA.
 * User: greg.frady
 * Date: 1/7/14
 * Time: 10:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClientManager {

    private static int clients = 500;

    public static void main(String[] args) {
        BasicConfigurator.configure();

        for (int i = 0; i < clients; i++) {
            SocketClient client = new SocketClient("localhost", 9008, 50, 300);
            client.start();
        }

    }

}
