package com.atlantis.zeus.base.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * redisson 工具类
 *
 * @author likang02@corp.netease.com
 * @date 7/10/22 5:37 PM
 */
@Slf4j
@Component
public class RedissonUtil {

    @Resource(name = "redissonClient")
    private RedissonClient redissonClient;

    /**
     * 加锁
     */
    public RLock lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    /**
     * 带超时的锁
     *
     * @param timeout 超时时间 单位：秒
     */
    public RLock lock(String lockKey, int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, TimeUnit.SECONDS);
        return lock;
    }

    /**
     * 带超时的锁
     *
     * @param unit    时间单位
     * @param timeout 超时时间
     */
    public RLock lock(String lockKey, TimeUnit unit, int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
        return lock;
    }


    /**
     * 尝试获取锁
     *
     * @param waitTime  最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     */
    public boolean tryLock(String lockKey, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 尝试获取锁
     *
     * @param unit      时间单位
     * @param waitTime  最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     */
    public boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 释放锁
     */
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }


    /**
     * 释放锁
     */
    public void unlock(RLock lock) {
        lock.unlock();
    }

    /**
     * 向延迟队列添加
     *
     * @param value     延迟消息
     * @param delay     延迟时间
     * @param timeUnit  时间单位
     * @param queueName 队列名
     * @param <T>
     */
    public <T> boolean addDelayQueueMsg(T value, long delay, TimeUnit timeUnit, String queueName) {
        if (StringUtils.isBlank(queueName) || Objects.isNull(value)) {
            return false;
        }

        try {
            RBlockingDeque<T> blockingDeque = redissonClient.getBlockingDeque(queueName);
            RDelayedQueue<T> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
            delayedQueue.offer(value, delay, timeUnit);
            log.info("RedissonService_addDelayQueue: add delay msg successful, queueName：{}, value：{}", queueName, value);
        } catch (Exception e) {
            log.error("RedissonService_addDelayQueue: add delay msg fail, queueName：{}, value：{}", queueName, value, e);
            return false;
        }
        return true;
    }

    /**
     * 获取延迟队列消息
     *
     * @param queueName
     * @param <T>
     * @return
     * @throws InterruptedException 等待是抛出中断异常
     */
    public <T> T getDelayQueueMsg(String queueName) throws InterruptedException {
        if (StringUtils.isBlank(queueName)) {
            return null;
        }

        RBlockingDeque<T> blockingDeque = redissonClient.getBlockingDeque(queueName);
        RDelayedQueue<T> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
        // 获取队列头结点，阻塞队列，直到右有数据才返回
        return  blockingDeque.take();
    }

    /**
     * 删除指定队列中的消息
     *
     * @param o         指定删除的消息对象队列值(同队列需保证唯一性)
     * @param queueName 指定队列键
     */
    public <T> boolean removeDelayedQueueMsg(T o, String queueName) {
        if (StringUtils.isBlank(queueName) || Objects.isNull(o)) {
            return false;
        }

        RBlockingDeque<T> blockingDeque = redissonClient.getBlockingDeque(queueName);
        RDelayedQueue<T> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
        return delayedQueue.remove(o);
    }


}
