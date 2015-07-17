/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.dart;

import test.dart.pool.Pool;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Rice Pavel
 */
public class Server implements Runnable {

  private final ServerSocket serverSocket;
  private final Pool pool;

  public Server(int port, int threadCount) throws IOException {
    serverSocket = new ServerSocket(port);
    pool = new Pool(threadCount);
  }

  @Override
  public void run() {
    try {
      while (true) {
        Socket client = serverSocket.accept();
        Respondent respondent = new Respondent(client);
        pool.add(respondent);
      }
    } catch (Exception e) {
      e.printStackTrace(System.err);
    } finally {
      try {
        serverSocket.close();
      } catch (Exception e) {

      }
    }
  }

}
