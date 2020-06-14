package com.base.seed.service.impl;

import com.base.seed.common.constants.GlobalRedisKeys;
import com.base.seed.common.enums.RetryAttemptType;
import com.base.seed.service.RetryAttemptService;
import javax.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RetryAttemptServiceImpl implements RetryAttemptService {

  @Autowired
  private RedissonClient redissonClient;
  private final static String PREFIX_RETRY_ATTEMPT = GlobalRedisKeys.PROJECT_PREFIX + "retryAttempt:";

  @Override
  public boolean isLocked(@Nonnull String unionKey, @Nonnull RetryAttemptType retryAttemptType) {
    long retryAttempts = getRAtomicLong(retryAttemptType.getCode(), unionKey).get();
    boolean isLocked = retryAttempts >= retryAttemptType.getMaxOperationCnt();
    log.info("UnionKey[{}], Operator[{}] retryAttempts={}, locked state: {}",
        unionKey, retryAttemptType.getCode(), retryAttempts, isLocked);
    return isLocked;
  }

  @Override
  public void retry(@Nonnull String unionKey, @Nonnull RetryAttemptType retryAttemptType) {
    trySetExpire(unionKey, retryAttemptType,
        getRAtomicLong(retryAttemptType.getCode(), unionKey).incrementAndGet());
  }

  @Override
  public boolean clear(@Nonnull String unionKey, @Nonnull RetryAttemptType retryAttemptType) {
    log.info("UnionKey[{}], Operator[{}] reset retries.", unionKey, retryAttemptType.getCode());
    getRAtomicLong(retryAttemptType.getCode(), unionKey).delete();
    return true;
  }

  /**
   * 尝试设置过期时间
   * <p>
   * 需要注意设置 Key 和 Expire 非原子操作，因此每次 retry 时都需要检查 Key 是否已添加 Expire
   */
  private void trySetExpire(String unionKey, RetryAttemptType retryAttemptType, long retryAttempts) {
    long remainTimeToLive = getRAtomicLong(retryAttemptType.getCode(), unionKey).remainTimeToLive();
    if (retryAttempts == 1 || remainTimeToLive == -1) {
      log.info("UnionKey[{}], Operator[{}] set expire[{}-{}], retryAttempts[{}]", unionKey,
          retryAttemptType.getCode(), retryAttemptType.getDuration(), retryAttemptType.getTimeUnit(), retryAttempts);
      getRAtomicLong(retryAttemptType.getCode(), unionKey).expire(retryAttemptType.getDuration(),
          retryAttemptType.getTimeUnit());
    }
  }

  private RAtomicLong getRAtomicLong(String operatorCode, String unionKey) {
    String key = buildRetryAttemptKey(operatorCode, unionKey);
    return redissonClient.getAtomicLong(key);
  }

  private String buildRetryAttemptKey(String operatorCode, String unionKey) {
    return PREFIX_RETRY_ATTEMPT + operatorCode + ":" + unionKey;
  }
}
