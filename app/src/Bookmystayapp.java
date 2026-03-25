import java.util.*;

class Reservation {
    private static int idCounter = 1;

    private int reservationId;
    private String customerName;
    private String roomType;
    private int nights;
    private double pricePerNight;

    public Reservation(String customerName, String roomType, int nights, double pricePerNight) {
        this.reservationId = idCounter++;
        this.customerName = customerName;
        this.roomType = roomType;
        this.nights = nights;
        this.pricePerNight = pricePerNight;
    }

    public int getReservationId() {
        return reservationId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    public double getTotalAmount() {
        return nights * pricePerNight;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Customer: " + customerName +
                ", Room Type: " + roomType +
                ", Nights: " + nights +
                ", Total: ₹" + getTotalAmount();
    }
}

class BookingHistory {
    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations);
    }
}

class BookingReportService {

    public void printAllBookings(List<Reservation> reservations) {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    public void generateSummary(List<Reservation> reservations) {
        int totalBookings = reservations.size();
        double totalRevenue = 0;

        for (Reservation r : reservations) {
            totalRevenue += r.getTotalAmount();
        }

        System.out.println("\n--- Booking Summary Report ---");
        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Revenue: ₹" + totalRevenue);
    }
}

 class BookingHistoryReport {

    public static void main(String[] args) {

        BookingHistory bookingHistory = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        Reservation r1 = new Reservation("Arun", "Deluxe", 2, 2500);
        Reservation r2 = new Reservation("Priya", "Suite", 3, 4000);
        Reservation r3 = new Reservation("Karthik", "Standard", 1, 1500);

        bookingHistory.addReservation(r1);
        bookingHistory.addReservation(r2);
        bookingHistory.addReservation(r3);

        List<Reservation> storedReservations = bookingHistory.getAllReservations();

        reportService.printAllBookings(storedReservations);
        reportService.generateSummary(storedReservations);
    }
}