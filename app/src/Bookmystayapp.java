import java.util.*;

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

class RoomInventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("Standard", 5);
        rooms.put("Deluxe", 3);
        rooms.put("Suite", 2);
    }

    public void validateRoomType(String roomType) throws InvalidBookingException {
        if (!rooms.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
    }

    public void validateAvailability(String roomType, int requested) throws InvalidBookingException {
        int available = rooms.get(roomType);
        if (requested <= 0) {
            throw new InvalidBookingException("Invalid number of rooms requested");
        }
        if (available < requested) {
            throw new InvalidBookingException("Not enough rooms available for " + roomType);
        }
    }

    public void bookRoom(String roomType, int count) throws InvalidBookingException {
        validateRoomType(roomType);
        validateAvailability(roomType, count);
        rooms.put(roomType, rooms.get(roomType) - count);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : rooms.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

class BookingService {
    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processBooking(String customerName, String roomType, int count) {
        try {
            inventory.bookRoom(roomType, count);
            System.out.println("Booking successful for " + customerName + " (" + roomType + ", " + count + ")");
        } catch (InvalidBookingException e) {
            System.out.println("Booking failed for " + customerName + ": " + e.getMessage());
        }
    }
}

class ErrorHandlingValidation {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);

        bookingService.processBooking("Arun", "Deluxe", 2);
        bookingService.processBooking("Priya", "Suite", 3);
        bookingService.processBooking("Karthik", "Premium", 1);
        bookingService.processBooking("Divya", "Standard", -1);

        inventory.displayInventory();
    }
}