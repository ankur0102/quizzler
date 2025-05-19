package org.project.springbootstarter.service;

public interface RedisService {
    void save(String key, Long value);
    Long get(String key);
}
