package com.redisjava.grocery;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import java.util.Map;

@RestController
@RequestMapping("/api/groceries")
public class GroceryLoadController {

    private final GroceryItemsRedisLoader groceryItemsRedisLoader;

    public GroceryLoadController(GroceryItemsRedisLoader groceryItemsRedisLoader) {
        this.groceryItemsRedisLoader = groceryItemsRedisLoader;
    }

    @GetMapping
    public List<GroceryItem> list() throws Exception {
        return groceryItemsRedisLoader.readAllFromRedis();
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<GroceryItem> getById(@PathVariable int id) throws Exception {
        return groceryItemsRedisLoader.readFromRedis(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @RequestMapping(value = "/load", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Map<String, Object>> load() throws Exception {
        int count = groceryItemsRedisLoader.loadItemsToRedis();
        return ResponseEntity.ok(Map.of("loaded", count));
    }
}
