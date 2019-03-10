package hello.programmer.concurrent;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MapLoop {

    public static void main(String[] args) {
        final ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>(2);
        map.put("er","sdf");
        System.out.println(map.size());
//        for (int i = 0; i < 100000; i++) {
//
//            new Thread(() -> map.put(UUID.randomUUID().toString(), "")).start();
//        }
    }
}
