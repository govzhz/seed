package com.base.seed.service.redis;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.DefaultScriptExecutor;
import org.springframework.data.redis.core.script.ScriptExecutor;
import org.springframework.stereotype.Component;

@Component
public class RedisComponent {

  private static final String PREFIX_LOCK = "lock:";

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  /**
   * 将字符串值 value 关联到 key
   *
   * @param ttl 过期时间（秒）
   */
  public void set(String key, String value, long ttl) {
    redisTemplate.boundValueOps(key).set(value, ttl, TimeUnit.SECONDS);
  }

  /**
   * 返回 key 所关联的字符串值
   *
   * @return key所关联的字符串值，若不存在则返回null
   */
  public String get(String key) {
    return redisTemplate.boundValueOps(key).get();
  }

  /**
   * 为给定 key 设置生存时间
   */
  public void expire(String key, long ttl) {
    redisTemplate.boundValueOps(key).expire(ttl, TimeUnit.SECONDS);
  }

  /**
   * 删除给定的 key
   */
  public void del(String key) {
    redisTemplate.delete(key);
  }

  /**
   * 获得分布式锁 1. 校验锁是否已经被抢占，若未被抢占则竞争锁 2. 若锁已被抢占或竞争锁失败则睡眠5毫秒继续竞争直到超时 3. 若竞争锁成功则设置锁最大生存时间和当前线程唯一标识
   * <p>
   * 使用 String lock = acquireLock(key, acquireTimeout, lockTimeout); if(lock != null){ try { do something; } finally {
   * releaseLock(key, lock) } }
   *
   * @param lockName:       锁名
   * @param acquireTimeout: 获取锁超时时间（s）
   * @param lockTimeout:    锁最大生存时间（s）
   * @return 当前线程锁唯一标识（若未获得锁则返回空）
   */
  public String acquireLock(String lockName, Long acquireTimeout, Long lockTimeout) {
    String identifier = UUID.randomUUID().toString();
    String lockKey = PREFIX_LOCK + lockName;
    Long end = System.currentTimeMillis() + acquireTimeout * 1000L + 1;

    while (System.currentTimeMillis() < end) {
      DefaultRedisScript<String> script = new DefaultRedisScript<>();
      script.setScriptText("if redis.call('EXISTS', KEYS[1]) == 0 then " +
          "return redis.call('SETEX', KEYS[1], unpack(ARGV)) end");
      script.setResultType(String.class);

      ScriptExecutor<String> scriptExecutor = new DefaultScriptExecutor<>(redisTemplate);
      String res =
          scriptExecutor.execute(script, Collections.singletonList(lockKey), String.valueOf(lockTimeout), identifier);

      if ("OK".equals(res)) {
        return identifier;
      }

      try {
        Thread.sleep(5L);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
    return null;
  }

  /**
   * 释放分布式锁 校验当前线程是否持有锁，若持有则删除该键
   *
   * @param lockName:   锁名
   * @param identifier: 当前线程锁唯一标识
   */
  public void releaseLock(String lockName, String identifier) {
    String lockKey = PREFIX_LOCK + lockName;

    DefaultRedisScript<Boolean> script = new DefaultRedisScript<>();
    script.setScriptText("if redis.call('GET', KEYS[1]) == ARGV[1] then " +
        "return redis.call('DEL', KEYS[1]) or true end");
    script.setResultType(Boolean.class);

    ScriptExecutor<String> scriptExecutor = new DefaultScriptExecutor<>(redisTemplate);
    scriptExecutor.execute(script, Collections.singletonList(lockKey), identifier);
  }

  /**
   * 自增
   *
   * @param key: key
   * @return 自增后的值（若key不存在则返回1）
   */
  private Long incr(String key, Long ttl) {
    DefaultRedisScript<Long> script = new DefaultRedisScript<>();
    script.setScriptText("local current = redis.call('INCR', KEYS[1]) if tonumber(current) == 1 " +
        "then redis.call('EXPIRE', KEYS[1], ARGV[1]) end return current");
    script.setResultType(Long.class);

    ScriptExecutor<String> scriptExecutor = new DefaultScriptExecutor<>(redisTemplate);
    return scriptExecutor.execute(script, Collections.singletonList(key), String.valueOf(ttl));
  }
}
