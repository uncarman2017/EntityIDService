package com.fadada.econtracthr.entityidservice.host.util;

import java.util.concurrent.TimeUnit;

import org.redisson.api.*;

public class RedissLockUtil {

    private static RedissonClient redissonClient;

    public void setRedissonClient(RedissonClient redissonClient){
        this.redissonClient = redissonClient;
    }

    /***
     * @Author libo
     * @Description //加锁
     * @return org.redisson.api.RLock
     */
    public static RLock getLock(String lockKey){
        RLock lock = redissonClient.getLock(lockKey);
        return  lock;
    }

    /***
     * @Author libo
     * @Description //获取公平锁
     * @return org.redisson.api.RLock
     */
    public static RLock getFairLock(String lockKey){
        RLock fairLock = redissonClient.getFairLock(lockKey);
        return  fairLock;
    }
    /***
     * @Author libo
     * @Description //获取读写锁
     * @return org.redisson.api.RReadWriteLock
     */
    public static RReadWriteLock getReadWriteLock(String lockKey){
        RReadWriteLock rwlock = redissonClient.getReadWriteLock(lockKey);
        return rwlock;
    }
    /***
     * @Author libo
     * @Description //获取信号量
     * @return
     */
    public static RSemaphore getSemaphore(String lockKey){
        RSemaphore semaphore = redissonClient.getSemaphore(lockKey);
        return semaphore;
    }

    /***
     * @Author libo
     * @Description //获取countDownLatch
     * @return
     */
    public static RCountDownLatch  getCountDownLatch(String lockKey){
        RCountDownLatch  rCountDownLatch  = redissonClient.getCountDownLatch(lockKey);
        return rCountDownLatch;
    }

    /***
     * @Author libo
     * @Description //异步加锁
     * @Date 15:03 2018/12/18
     * @return org.redisson.api.RFuture<java.lang.Boolean>
     */
    public static RFuture<Boolean> tryLockAsync(String lockKey,int waitTime,int leaseTime,TimeUnit timeUnit) throws InterruptedException {
        RLock lock = redissonClient.getLock(lockKey);
        Thread.sleep(1000);
        return lock.tryLockAsync(waitTime,leaseTime,timeUnit);
    }

    /***
     * @Author libo
     * @Description //释放锁
     * @Date 17:12 2018/11/16
     * @return void
     */
    public static void unLock(String lockKey){
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }

    /***
     * @Author libo
     * @Description //释放锁
     * @Date 17:12 2018/11/16
     * @return void
     */
    public static void unLock(RLock lock){
        lock.unlock();
    }

    /***
     * @Author libo
     * @Description //带超时的锁   单位 秒
     * @Date 17:17 2018/11/16
     * @return org.redisson.api.RLock
     */
    public static RLock lock(String lockKey,int timeout){
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, TimeUnit.SECONDS);
        return lock;
    }

    public static RLock lock(String lockKey,TimeUnit timeUnit,int timeout){
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, timeUnit);
        return lock;
    }

    /***
     * @Author libo
     * @param lockKey
     * @param waitTime 最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @Date 17:20 2018/11/16
     * @return boolean
     */
    public static boolean tryLock(String lockKey,int waitTime, int leaseTime){
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return  lock.tryLock(waitTime,leaseTime,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return  false;
        }
    }

    /***
     * @Author libo
     * @param lockKey
     * @param waitTime 最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @Date 17:20 2018/11/16
     * @return boolean
     */
    public static boolean tryLock(String lockKey,TimeUnit timeUnit, int waitTime, int leaseTime){
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return  lock.tryLock(waitTime,leaseTime,timeUnit);
        } catch (InterruptedException e) {
            return  false;
        }
    }
}
