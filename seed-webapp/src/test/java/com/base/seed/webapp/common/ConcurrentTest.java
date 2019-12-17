package com.base.seed.webapp.common;

import com.base.seed.webapp.BaseTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zz 2019/12/10
 */
public class ConcurrentTest extends BaseTest {

    private ExecutorService executorService = new ThreadPoolExecutor(
            5,
            5,
            60,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1),
            new ThreadPoolExecutor.DiscardOldestPolicy()
    );

    @Test
    public void test() throws ExecutionException, InterruptedException {
        int threadNum = 5;

        List<CompletableFuture<String>> list = new ArrayList<>(threadNum);

        for (int i = 0; i < threadNum; i++){

            list.add(CompletableFuture.supplyAsync(() -> {

                try {
                    // do something
                } catch (Exception e) {

                }
                return null;
            }, executorService));
        }

        CompletableFuture<Map<String, Long>> mapCompletableFuture = CompletableFuture.allOf(list.toArray(new CompletableFuture[0]))
                .thenApply(v -> list.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()))
                .thenApply(tokens -> tokens.stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())));

        Assert.assertEquals(mapCompletableFuture.get().size(), 0);
    }
}
