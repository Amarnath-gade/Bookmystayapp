import java.util.*;

class Reservation {
    private static int counter = 1;
    private int id;
    private String customerName;
    private String roomType;

    public Reservation(String customerName, String roomType) {
        this.id = counter++;
        this.customerName = customerName;
        this.roomType = roomType;
    }

    public int getId() {
        return id;
    }

    public String getRoomType() {
        return roomType;
    }

    public String toString() {
        return "Reservation ID: " + id + ", Customer: " + customerName + ", Room: " + roomType;
    }
}

class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Standard", 5);
        inventory.put("Deluxe", 3);
        inventory.put("Suite", 2);
    }

    public boolean isAvailable(String roomType) {
        return inventory.containsKey(roomType) && inventory.get(roomType) > 0;
    }

    public void allocate(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void release(String roomType) {
        inventory.put(roomType, inventory.get(roomType) + 1);
    }

    public void display() {
        System.out.println("\nInventory Status:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

class BookingService {
    private Map<Integer, Reservation> bookings = new HashMap<>();
    private Stack<Integer> rollbackStack = new Stack<>();
    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void book(String customerName, String roomType) {
        if (!inventory.isAvailable(roomType)) {
            System.out.println("Booking failed: No rooms available for " + roomType);
            return;
        }

        inventory.allocate(roomType);
        Reservation reservation = new Reservation(customerName, roomType);
        bookings.put(reservation.getId(), reservation);
        rollbackStack.push(reservation.getId());

        System.out.println("Booked successfully: " + reservation);
    }

    public void cancel(int reservationId) {
        if (!bookings.containsKey(reservationId)) {
            System.out.println("Cancellation failed: Invalid reservation ID " + reservationId);
            return;
        }

        Reservation reservation = bookings.remove(reservationId);
        inventory.release(reservation.getRoomType());

        if (!rollbackStack.isEmpty() && rollbackStack.peek() == reservationId) {
            rollbackStack.pop();
        }

        System.out.println("Cancelled successfully: Reservation ID " + reservationId);
    }

    public void displayBookings() {
        System.out.println("\nActive Bookings:");
        for (Reservation r : bookings.values()) {
            System.out.println(r);
        }
    }
}

class BookingCancellation {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService(inventory);

        service.book("Arun", "Deluxe");
        service.book("Priya", "Suite");
        service.book("Karthik", "Standard");

        service.displayBookings();
        inventory.display();

        service.cancel(2);
        service.cancel(5);

        service.displayBookings();
        inventory.display();
    }
}