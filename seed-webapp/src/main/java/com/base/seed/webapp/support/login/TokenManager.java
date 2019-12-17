package com.base.seed.webapp.support.login;

import com.base.seed.common.constant.RedisConstants;
import com.base.seed.service.redis.RedisComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author zz 2018/6/13.
 */
@Component
public class TokenManager {

    @Autowired
    private RedisComponent redisComponent;

    /**
     * 根据token获取uid
     *      如果token不存在则返回null
     */
    public String getByToken(String token){
        String uid = redisComponent.get(token);

        // 如果成功获取到uid，则延长token过期时间
        if (uid != null) {
            redisComponent.expire(token, RedisConstants.TOKEN_EXPIRE);
        }
        return uid;
    }

    /**
     * 设置token
     */
    public String setToken(String uid){
        String token = UUID.randomUUID().toString().replace("-","")
                    + System.currentTimeMillis();
        redisComponent.set(token, uid, RedisConstants.TOKEN_EXPIRE);
        return token;
    }

    /**
     * 删除token
     */
    public void removeToken(String token){
        redisComponent.del(token);
    }
}
