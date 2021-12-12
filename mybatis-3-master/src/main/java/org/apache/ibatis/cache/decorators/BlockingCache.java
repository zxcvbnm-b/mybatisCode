/*
 *    Copyright 2009-2021 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.cache.decorators;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;

/**
 简单的阻塞装饰器

 EhCache的BlockingCache装饰器的简单低效版本。

 当元素在缓存中找不到时，它会对缓存键设置一个锁。

 *这样，其他线程就会一直等到这个元素被填满，而不会去访问数据库。
 *等到有人给这个key设置值，不然getObject方法会一直阻塞
 * <p>By its nature, this implementation can cause deadlock when used incorrectly.
 *
 * @author Eduardo Macarron
 *
 */
public class BlockingCache implements Cache {

  private long timeout;
  private final Cache delegate;
  private final ConcurrentHashMap<Object, CountDownLatch> locks;

  public BlockingCache(Cache delegate) {
    this.delegate = delegate;
    this.locks = new ConcurrentHashMap<>();
  }

  @Override
  public String getId() {
    return delegate.getId();
  }

  @Override
  public int getSize() {
    return delegate.getSize();
  }
  /*当指定的key的值被设置，那么在getObject获取值的线程就会被释放而得到返回值。*/
  @Override
  public void putObject(Object key, Object value) {
    try {
      delegate.putObject(key, value);
    } finally {
      releaseLock(key);
    }
  }
  /*获取不到值就阻塞，直到能获取到这个key的值为止*/
  @Override
  public Object getObject(Object key) {
    acquireLock(key);
    Object value = delegate.getObject(key);
    if (value != null) {
      releaseLock(key);
    }
    return value;
  }

  @Override
  public Object removeObject(Object key) {
    // despite of its name, this method is called only to release locks
    releaseLock(key);
    return null;
  }

  @Override
  public void clear() {
    delegate.clear();
  }
  /*尝试获取锁  分段锁？：第一次当locks中没有值的时候，返回null，直接返回表示加锁成功，当其他线程再来根据相同的key来获取数据的时候，就能获取到
  上次放入的 newLatch，然后就会被调用 await 进行阻塞，直到调用countDown是否锁：
  （
  如果这个方法被同一个线程多次调用，那么可能会出现问题吧？
  第一次加锁成功，但是第二次再调用就可能死锁了吧？）
  *
  * */
  private void acquireLock(Object key) {
    CountDownLatch newLatch = new CountDownLatch(1);
    while (true) {
   /*   而putIfAbsent在放入数据时，如果存在重复的key，那么putIfAbsent不会放入值。*/
      /*如果已经存在值，那么返回存在的值，如果不存在值，那么put一个值，并返回null，返回null，那么直接不阻塞的，*/
      CountDownLatch latch = locks.putIfAbsent(key, newLatch);
      if (latch == null) {
        break;
      }
      try {
        if (timeout > 0) {
          boolean acquired = latch.await(timeout, TimeUnit.MILLISECONDS);
          if (!acquired) {
            throw new CacheException(
                "Couldn't get a lock in " + timeout + " for the key " + key + " at the cache " + delegate.getId());
          }
        } else {
          latch.await();
        }
      } catch (InterruptedException e) {
        throw new CacheException("Got interrupted while trying to acquire lock for key " + key, e);
      }
    }
  }
/*CountDownLatch数量减不到0， 就会在await处等待*/
  private void releaseLock(Object key) {
    CountDownLatch latch = locks.remove(key);
    if (latch == null) {
      throw new IllegalStateException("Detected an attempt at releasing unacquired lock. This should never happen.");
    }
    latch.countDown();
  }

  public long getTimeout() {
    return timeout;
  }

  public void setTimeout(long timeout) {
    this.timeout = timeout;
  }
}
