package com.redisjava.grocery;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.grocery.load-on-startup", havingValue = "true", matchIfMissing = true)
public class GroceryStartupRunner implements ApplicationRunner {

    private final GroceryItemsRedisLoader groceryItemsRedisLoader;

    public GroceryStartupRunner(GroceryItemsRedisLoader groceryItemsRedisLoader) {
        this.groceryItemsRedisLoader = groceryItemsRedisLoader;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        groceryItemsRedisLoader.loadItemsToRedis();
    }
}
