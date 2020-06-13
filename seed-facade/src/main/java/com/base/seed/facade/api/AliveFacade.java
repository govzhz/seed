package com.base.seed.facade.api;

import com.base.seed.facade.support.RpcResult;

public interface AliveFacade {

  RpcResult<Boolean> isAlive();
}
