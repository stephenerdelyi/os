import java.util.Random;
import java.security.SecureRandom;

public class OS {
    static TaskQueue taskList = new TaskQueue();
    static TaskHelpers taskHelpers = new TaskHelpers();
    static RandomStringHelper stringGenerator = new RandomStringHelper();

    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            Task randomTask = taskHelpers.randomTask();
            taskList.enqueue(randomTask);
        }

        taskList.print();
    }

    public static class TaskQueue {
        static int numAllowed = 150;
        static int numCreated = 0;
        static Task[] dataArray = new Task[numAllowed];

        public static void construct() {
            //no construction
        }

        public static boolean enqueue(Task newTask) {
            if (!isFull()) {
                dataArray[numCreated] = new Task();
                dataArray[numCreated].code = newTask.code;
                dataArray[numCreated].description = newTask.description;
                dataArray[numCreated].numCycles = newTask.numCycles;
                numCreated++;
                return true;
            }
            return false;
        }

        public static Task dequeue() {

            if (!isEmpty()) {
                Task returnTask = dataArray[0];
                for (int i = 1; i < numCreated; i++) {
                    dataArray[i - 1] = dataArray[i];
                }
                dataArray[numCreated] = null;
                numCreated--;
                return returnTask;
            }
            return taskHelpers.nullTask();
        }

        public static void print() {
            for (int i = 0; i < numCreated; i++) {
                int j = i + 1;
                char code = dataArray[i].code;
                String description = dataArray[i].description;
                int numCycles = dataArray[i].numCycles;
                System.out.println("Task " + j + ": " + code + " - " + description + " - " + numCycles);
            }
        }

        private static boolean isFull() {
            if (numCreated >= numAllowed) {
                return true;
            }
            return false;
        }

        private static boolean isEmpty() {
            if (numCreated == 0) {
                return true;
            }
            return false;
        }
    }

    /////////////////////////////////////////////////////
    //                 HELPER CLASSES                  //
    /////////////////////////////////////////////////////
    public static class Task {
        char code;
        String description;
        int numCycles;
    }

    public static class TaskHelpers {
        public static Task randomTask() {
            Random random = new Random();
            int randomNum = random.nextInt(99);

            String testString = stringGenerator.generate(10, "alphanumeric");
            char testChar = stringGenerator.generate(10, "alpha").charAt(0);

            Task randomTask = new Task();
            randomTask.code = testChar;
            randomTask.description = testString;
            randomTask.numCycles = randomNum;
            return randomTask;
        }

        private static Task nullTask() {
            Task nullTask = new Task();
            return nullTask;
        }
    }

    public static class RandomStringHelper {
        private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private static final String NUMS = "0123456789";
        private static final String ALPHANUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private static final SecureRandom RANDOM = new SecureRandom();

        public static String generate(int count, String type) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < count; ++i) {
                if (type == "alpha") {
                    sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
                } else if (type == "num") {
                    sb.append(NUMS.charAt(RANDOM.nextInt(NUMS.length())));
                } else {
                    sb.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
                }
            }
            return sb.toString();
        }
    }
}
