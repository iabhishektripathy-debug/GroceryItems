package com.redisjava;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @RequestMapping(value = "/health", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }
}
