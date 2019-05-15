package kalog.com.vn;

import java.util.*;
import java.util.concurrent.*;

public class BarrierInAction {
    public static void main(String[] args) {

//        Map<String, String> map = new HashMap<>();
//        map.put(null, "123");
//        map.put(null, null);
//        System.out.println(map.get(null));
        List<String> list = new ArrayList<>();
        list.add(null);
//        System.out.println(list.get(0));
        list.stream().map(s -> {System.out.println(s);return s;}
        );
        Set<String> set = new HashSet<>();
        set.add(null);
        set.stream().map(s -> {System.out.println(s);return s;}
        );
        class Friend implements Callable<String> {
            private CyclicBarrier barrier;

            public Friend(CyclicBarrier barrier) {
                this.barrier = barrier;
            }

            public String call() throws Exception {
                Random random = new Random();
                Thread.sleep(random.nextInt(20)*100 + 100);
                System.out.println("I just arrived, waiting for the other...");

//                barrier.await();
                barrier.await(5, TimeUnit.SECONDS);
                System.out.println("Let's go to the cinema!");
                return "ok";
            }
        }

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        CyclicBarrier barrier = new CyclicBarrier(4, () -> System.out.println("Barrier openning"));
        List<Future<String>> futures = new ArrayList<Future<String>>();

        try {
            for (int i = 0; i < 4; i++) {
                Friend friend = new Friend(barrier);
                futures.add(executorService.submit(friend));
            }
            futures.forEach(future -> {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println(e.getMessage());
                }
            });
        } finally {
            executorService.shutdown();
        }
    }
}
