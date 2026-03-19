package net.PORC.journalApp.service;


import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {
    private final Map<String, List<Long>> userRequests = new ConcurrentHashMap<>();

    private static final int MAX_REQUESTS = 15; // more relaxed
    private static final long TIME_WINDOW = 60 * 1000; // 1 min

    public boolean isAllowed(String key) {
        long now = System.currentTimeMillis();

        userRequests.putIfAbsent(key, new ArrayList<>());
        List<Long> requests = userRequests.get(key);

        requests.removeIf(time -> now - time > TIME_WINDOW);

        if (requests.size() >= MAX_REQUESTS) {
            return false;
        }

        requests.add(now);
        return true;
    }
}
