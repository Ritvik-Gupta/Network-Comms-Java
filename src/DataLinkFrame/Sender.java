package src.DataLinkFrame;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Sender {
   public static void main(String[] args) {
      try {
         ServerSocket ss = new ServerSocket(6666);
         Socket s = ss.accept();

         Frame frame = new Frame("01111110", "0111111110101000");

         DataOutputStream dout = new DataOutputStream(s.getOutputStream());
         dout.writeUTF(frame.toString());
         dout.flush();
         dout.close();

         ss.close();
      } catch (Exception e) {
         System.out.println(e);
      }
   }
}
