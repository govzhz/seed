package com.base.seed.facade.api;

import com.base.seed.facade.support.RpcResult;

/**
 * @author zz 2019/12/13
 */
public interface AliveFacade {

    RpcResult<Boolean> isAlive();
}
