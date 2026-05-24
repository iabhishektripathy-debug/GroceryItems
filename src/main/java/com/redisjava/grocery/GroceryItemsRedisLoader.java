package com.redisjava.grocery;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class GroceryItemsRedisLoader {

    static final String ITEM_KEY_PREFIX = "grocery:item:";
    static final String ITEM_IDS_KEY = "grocery:itemIds";

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public GroceryItemsRedisLoader(StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Reads {@code grocery-items.json} from the classpath and writes each item to Redis.
     *
     * @return number of items loaded
     */
    public int loadItemsToRedis() throws Exception {
        ClassPathResource resource = new ClassPathResource("grocery-items.json");
        try (InputStream in = resource.getInputStream()) {
            List<GroceryItem> items = objectMapper.readValue(in, new TypeReference<>() {});
            stringRedisTemplate.delete(ITEM_IDS_KEY);
            for (GroceryItem item : items) {
                String key = ITEM_KEY_PREFIX + item.id();
                String json = objectMapper.writeValueAsString(item);
                stringRedisTemplate.opsForValue().set(key, json);
                stringRedisTemplate.opsForSet().add(ITEM_IDS_KEY, String.valueOf(item.id()));
            }
            return items.size();
        }
    }

    public List<GroceryItem> readAllFromRedis() throws Exception {
        Set<String> idStrings = stringRedisTemplate.opsForSet().members(ITEM_IDS_KEY);
        if (idStrings == null || idStrings.isEmpty()) {
            return List.of();
        }
        List<GroceryItem> out = new ArrayList<>();
        for (String idStr : idStrings) {
            readFromRedis(Integer.parseInt(idStr)).ifPresent(out::add);
        }
        out.sort(Comparator.comparingInt(GroceryItem::id));
        return out;
    }

    public Optional<GroceryItem> readFromRedis(int id) throws Exception {
        String json = stringRedisTemplate.opsForValue().get(ITEM_KEY_PREFIX + id);
        if (json == null || json.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(objectMapper.readValue(json, GroceryItem.class));
    }
}
