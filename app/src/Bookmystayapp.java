import java.util.*;
import java.util.concurrent.*;

class BookingRequest {
    int guestId;
    int roomsRequested;

    public BookingRequest(int guestId, int roomsRequested) {
        this.guestId = guestId;
        this.roomsRequested = roomsRequested;
    }
}

class HotelInventory {
    private int availableRooms;

    public HotelInventory(int rooms) {
        this.availableRooms = rooms;
    }

    public synchronized boolean allocateRoom(int guestId, int roomsRequested) {
        System.out.println("Guest " + guestId + " requesting " + roomsRequested + " rooms.");

        if (availableRooms >= roomsRequested) {
            System.out.println("Rooms available. Processing booking for Guest " + guestId);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            availableRooms -= roomsRequested;
            System.out.println("Booking SUCCESS for Guest " + guestId +
                    ". Remaining rooms: " + availableRooms);
            return true;
        } else {
            System.out.println("Booking FAILED for Guest " + guestId +
                    ". Not enough rooms.");
            return false;
        }
    }

    public int getAvailableRooms() {
        return availableRooms;
    }
}

class BookingProcessor implements Runnable {

    private BlockingQueue<BookingRequest> bookingQueue;
    private HotelInventory inventory;

    public BookingProcessor(BlockingQueue<BookingRequest> queue, HotelInventory inventory) {
        this.bookingQueue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        try {
            while (true) {
                BookingRequest request = bookingQueue.poll(2, TimeUnit.SECONDS);

                if (request == null) {
                    break;
                }

                inventory.allocateRoom(request.guestId, request.roomsRequested);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        BlockingQueue<BookingRequest> bookingQueue = new LinkedBlockingQueue<>();
        HotelInventory inventory = new HotelInventory(10);

        bookingQueue.add(new BookingRequest(1, 3));
        bookingQueue.add(new BookingRequest(2, 4));
        bookingQueue.add(new BookingRequest(3, 2));
        bookingQueue.add(new BookingRequest(4, 3));
        bookingQueue.add(new BookingRequest(5, 1));

        Thread t1 = new Thread(new BookingProcessor(bookingQueue, inventory));
        Thread t2 = new Thread(new BookingProcessor(bookingQueue, inventory));
        Thread t3 = new Thread(new BookingProcessor(bookingQueue, inventory));

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nFinal Available Rooms: " + inventory.getAvailableRooms());
        System.out.println("All bookings processed safely.");
    }
}