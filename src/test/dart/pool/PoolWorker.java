/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.dart.pool;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 *
 * @author Rice Pavel
 */
public class PoolWorker extends Thread {

  private final ConcurrentLinkedDeque<Runnable> queue;

  public PoolWorker(ConcurrentLinkedDeque queue) {
    this.queue = queue;
  }

  @Override
  public void run() {
    try {
      while (true) {
        Runnable runnable;
        while (queue.isEmpty()) {
          synchronized (queue) {
            queue.wait();
          }
        }
        runnable = queue.removeFirst();
        runnable.run();
      }
    } catch (Exception e) {

    }
  }

}
