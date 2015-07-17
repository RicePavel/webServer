/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.dart.pool;

import java.util.Deque;

/**
 *
 * @author Rice Pavel
 */
public class PoolWorker extends Thread {

  private Deque<Runnable> queue;

  public PoolWorker(Deque queue) {
    this.queue = queue;
  }

  public void run() {
    try {
      while (true) {
        Runnable runnable;
        synchronized (queue) {
          while (queue.isEmpty()) {
            queue.wait();
          }
          runnable = queue.getFirst();
        }
        runnable.run();
      }
    } catch (Exception e) {

    }
  }

}
