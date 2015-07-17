/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.dart.pool;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 *
 * @author Rice Pavel
 */
public class Pool {

  private final PoolWorker[] workers;
  private final ConcurrentLinkedDeque<Runnable> queue = new ConcurrentLinkedDeque();

  ConcurrentLinkedDeque<Runnable> getQueue() {
    return queue;
  }

  public Pool(int countThreads) {
    workers = new PoolWorker[countThreads];
    for (int i = 0; i < countThreads; i++) {
      workers[i] = new PoolWorker(queue);
      workers[i].start();
    }
  }

  public void add(Runnable runnable) {
    queue.addLast(runnable);
    synchronized (queue) {
      queue.notify();
    }
  }

}
