package src.DataLinkFrame;

import java.io.DataInputStream;
import java.net.Socket;

public class Receiver {
   public static void main(String[] args) {
      try {
         Socket s = new Socket("localhost", 6666);
         DataInputStream dis = new DataInputStream(s.getInputStream());
         String str = dis.readUTF();
         System.out.println("message = " + str);
         s.close();
      } catch (Exception e) {
         System.out.println(e);
      }
   }
}
