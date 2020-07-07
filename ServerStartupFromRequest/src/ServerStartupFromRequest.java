import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.SocketAddress;

public class ServerStartupFromRequest {

    public static void main(String[] args){

        System.out.println(available(25565));
        listen(25565);

    }

    /**
     * Checks to see if a specific port is available.
     * TCP & UDP Connections
     * @param port the port to check for availability
     */
    public static boolean available(int port) {
        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }

    public static void listen(int port){
        if(available(port)){
            ServerSocket ss = null;
            try{
                ss = new ServerSocket(port);
                ss.setReuseAddress(true);
                //ss.accept();
                System.out.print("Connection Found!");
                ss.close();
                String[] command = {"cmd.exe","/C","start","C:\\Users\\kipcl\\OneDrive\\Desktop\\Minecraft\\Run_Test_Server.bat"};
                Process process = Runtime.getRuntime().exec(command);
            }catch(IOException e){
                /*Do Nothing*/
            }
        }
    }
}
