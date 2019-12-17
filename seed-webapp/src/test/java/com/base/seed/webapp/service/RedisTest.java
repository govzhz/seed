package com.base.seed.webapp.service;

import com.base.seed.webapp.BaseTest;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis操作
 *
 * @author zz 2019-03-25
 */
public class RedisTest extends BaseTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisTemplate<String, User> userRedisTemplate;

    @Test
    public void baseTest(){

        final String key = "myTestKey";

        final String value = "myTestValue";

        redisTemplate.execute((RedisCallback<Void>) redisConnection -> {
            redisConnection.set(key.getBytes(), value.getBytes());
            return null;
        });

        String res = redisTemplate.execute(
                (RedisCallback<String>) redisConnection -> new String(redisConnection.get(key.getBytes())));

        Assert.assertEquals(res, value);
    }

    @Test
    public void userTest(){

        String key = "myUserKey";

        // 对象必须要有默认构造函数
        User user = new User();
        user.setAge(1);
        user.setName("user");

        userRedisTemplate.opsForValue().set(key, user);

        User newUser = userRedisTemplate.opsForValue().get(key);

        Assert.assertEquals(user, newUser);
    }

}


@Data
class User {

    private String name;

    private Integer age;
}
