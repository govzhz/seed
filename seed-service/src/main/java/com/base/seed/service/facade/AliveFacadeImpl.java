package com.base.seed.service.facade;

import com.base.seed.facade.api.AliveFacade;
import com.base.seed.facade.support.RpcResult;
import org.apache.dubbo.config.annotation.Service;

@Service
public class AliveFacadeImpl implements AliveFacade {

  @Override
  public RpcResult<Boolean> isAlive() {
    return RpcResult.success(true);
  }
}
