class SharedResource {
    private int data;
    private boolean bChanged = false; 

    public synchronized int get() {
        while (!bChanged) {
            try {
                wait(); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Consumer interrupted");
            }
        }

        System.out.println("Consumer: Received [" + data + "]");
        
        bChanged = false;
        notify(); 
        return data;
    }

    public synchronized void set(int value) {
        while (bChanged) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Producer interrupted");
            }
        }

        this.data = value;
        this.bChanged = true; 
        System.out.println("Producer: Sent [" + value + "]");

        notify(); 
    }
}

public class ThreadCoordination {
    public static void main(String[] args) {
        SharedResource resource = new SharedResource();

        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                resource.set(i);
                try {
                    Thread.sleep(200); 
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "ProducerThread");

        Thread consumer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                resource.get();
            }
        }, "ConsumerThread");

        producer.start();
        consumer.start();
    }
}