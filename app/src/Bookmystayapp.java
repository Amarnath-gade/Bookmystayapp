import java.io.*;
import java.util.*;

class Booking implements Serializable {
    int guestId;
    int roomsBooked;

    public Booking(int guestId, int roomsBooked) {
        this.guestId = guestId;
        this.roomsBooked = roomsBooked;
    }

    public String toString() {
        return "Guest " + guestId + " booked " + roomsBooked + " rooms";
    }
}

class HotelState implements Serializable {
    int availableRooms;
    List<Booking> bookings;

    public HotelState(int availableRooms, List<Booking> bookings) {
        this.availableRooms = availableRooms;
        this.bookings = bookings;
    }
}

class PersistenceService {
    private static final String FILE_NAME = "hotel_state.ser";

    public static void saveState(HotelState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(state);
            System.out.println("State saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving state.");
        }
    }

    public static HotelState loadState() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            System.out.println("State loaded successfully.");
            return (HotelState) ois.readObject();
        } catch (Exception e) {
            System.out.println("No valid saved state found. Starting fresh.");
            return new HotelState(10, new ArrayList<>());
        }
    }
}

public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        HotelState state = PersistenceService.loadState();

        System.out.println("Available Rooms: " + state.availableRooms);

        if (state.bookings.isEmpty()) {
            state.bookings.add(new Booking(1, 2));
            state.availableRooms -= 2;

            state.bookings.add(new Booking(2, 3));
            state.availableRooms -= 3;

            System.out.println("New bookings added.");
        }

        System.out.println("\nCurrent Bookings:");
        for (Booking b : state.bookings) {
            System.out.println(b);
        }

        System.out.println("\nRemaining Rooms: " + state.availableRooms);

        PersistenceService.saveState(state);
    }
}