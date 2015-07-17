/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.dart;

import java.io.IOException;

/**
 *
 * @author Rice Pavel
 */
public class Main {
  
  public static void main(String[] args) throws IOException {
    if (args.length == 2) {
      String portStr = args[0];
      String countThreadsStr = args[1];
      Integer port = getInt(portStr);
      Integer count = getInt(countThreadsStr);
      if (port != null && count != null) {
        System.out.println("запускаем сервер...");
        Server server = new Server(port, count);
        Thread thread = new Thread(server);
        thread.run();
        
      } else {
        String error = "";
        if (port == null) {
          error += "порт передан в неправильном формате";
        }
        if (count == null) {
          error += "количество потоков передано в неправильном формате";
        }
        System.err.println(error);
      }
    } else {
      System.err.println("количество аргументов должно быть 2");
    }
  }
  
  private static Integer getInt(String str) {
    try {
      return Integer.valueOf(str);
    } catch (Exception e) {
      return null;
    }
  }
  
}
