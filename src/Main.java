import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    private static final int TEXT_COUNT = 10_000;
    private static final int LENGTH_OF_TEXT = 100_000;
    private static final int QUEUE_CAPACITY = 100;
    private static BlockingQueue<String> QueueA = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
    private static BlockingQueue<String> QueueB = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
    private static BlockingQueue<String> QueueC = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

    public static void main(String[] args) {

        Thread generate = new Thread(() -> {
            for (int i = 0; i < TEXT_COUNT; i++) {
                String texts = generateText("abc", LENGTH_OF_TEXT);
                try {
                    QueueA.put(texts);
                    QueueB.put(texts);
                    QueueC.put(texts);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        generate.start();

        Thread aMax = new Thread(() -> {
            char simvol = 'a';
            int countA = findMaxCount(QueueA, simvol);
            System.out.println(" Максимальное количество повторяющихся символов " + simvol
                    + " в тексте -> " + countA + " раз ");
        });
        aMax.start();

        Thread bMax = new Thread(() -> {
            char simvol = 'b';
            int countB = findMaxCount(QueueB, simvol);
            System.out.println(" Максимальное количество повторяющихся символов " + simvol
                    + " в тексте -> " + countB + " раз ");
        });
        bMax.start();

        Thread cMax = new Thread(() -> {
            char simvol = 'c';
            int countC = findMaxCount(QueueC, simvol);
            System.out.println(" Максимальное количество повторяющихся символов " + simvol
                    + " в тексте -> " + countC + " раз ");
        });
        cMax.start();

        try {
            generate.join();
            aMax.join();
            bMax.join();
            cMax.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int findMaxCount(BlockingQueue<String> queue, char letter) {
        int count ;
        int maxCount = 0;

        try {
            for (int i = 0; i < TEXT_COUNT; i++) {
                String mas = queue.take();
                count = 0;
                for (char text : mas.toCharArray()) {
                    if (text == letter) {
                        count++;
                    }
                }
                if (count > maxCount) {
                    maxCount = count;
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return maxCount;
    }
}

