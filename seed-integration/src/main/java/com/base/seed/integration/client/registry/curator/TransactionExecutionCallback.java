package com.base.seed.integration.client.registry.curator;

import java.util.List;
import org.apache.curator.framework.api.transaction.CuratorOp;
import org.apache.curator.framework.api.transaction.TransactionOp;

public interface TransactionExecutionCallback {

  List<CuratorOp> execute(TransactionOp transactionOp) throws Exception;
}
