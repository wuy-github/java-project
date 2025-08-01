package com.project.futabuslines.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/redis")
@RequiredArgsConstructor
public class RedisTestController {

    private final RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/set")
    public String setValue(
            @RequestParam String key,
            @RequestParam String value
    ) {
        redisTemplate.opsForValue().set(key, value);
        return "Saved key=" + key + " with value=" + value;
    }

    @GetMapping("/get")
    public Object getValue(@RequestParam String key) {
        Object value = redisTemplate.opsForValue().get(key);
        return Objects.requireNonNullElseGet(value, () -> "No value found for key=" + key);
    }

    @DeleteMapping("/delete")
    public String deleteKey(@RequestParam String key) {
        Boolean deleted = redisTemplate.delete(key);
        return deleted != null && deleted ? "Deleted key=" + key : "Key not found";
    }
}
