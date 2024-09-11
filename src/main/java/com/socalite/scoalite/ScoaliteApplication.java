package  com.socalite.scoalite;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableSwagger2
@SpringBootApplication
public class ScoaliteApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScoaliteApplication.class, args);
    }

}

/*
public class ScoaliteApplication {

    static class Lock {
        private volatile int number;
        private volatile int limit;

        public Lock(int number, int limit) {
            this.number = number;
            this.limit = limit;
        }

        public synchronized void printOdd() {
            while(number <= limit) {
                if (number % 2 != 0) {
                    System.out.println(Thread.currentThread().getName() + " " + number);
                    number++;
                    notify();
                } else {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        public synchronized void printEven() {
            while (number <= limit) {
                if (number % 2 == 0) {
                    System.out.println(Thread.currentThread().getName() + " " + number);
                    number++;
                    notify();
                } else {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

//    private static final Lock lock = new Lock();

    public static void main(String[] args) throws InterruptedException {
        var lock = new Lock(1, 20);

        Thread evenThread = new Thread(lock::printEven);
        Thread oddThread = new Thread(lock::printOdd);

        oddThread.start();
        evenThread.start();

        oddThread.join();
        evenThread.join();
    }
}*/
