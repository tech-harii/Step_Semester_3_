import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ques3 {

    public enum RoomCategory { STANDARD, DELUXE, SUITE }

    public interface PricingStrategy {
        double calculatePrice(int nights);
    }

    public static class StandardPricing implements PricingStrategy {
        @Override public double calculatePrice(int nights) { return nights * 150.00; }
    }

    public static class DeluxePricing implements PricingStrategy {
        @Override public double calculatePrice(int nights) { return nights * 200.00; }
    }

    public static class Room {
        private final String roomId;
        private final RoomCategory category;
        private final PricingStrategy pricingStrategy;

        public Room(String roomId, RoomCategory category, PricingStrategy pricingStrategy) {
            this.roomId = roomId;
            this.category = category;
            this.pricingStrategy = pricingStrategy;
        }

        public String getRoomId() { return roomId; }
        public PricingStrategy getPricingStrategy() { return pricingStrategy; }
    }

    public enum ReservationStatus { CONFIRMED, CANCELLED }

    public static class Reservation {
        private final String reservationId;
        private final Room room;
        private final String customerName;
        private final LocalDate checkIn;
        private final LocalDate checkOut;
        private final double totalPrice;
        private ReservationStatus status;

        public Reservation(String reservationId, Room room, String customerName, LocalDate checkIn, LocalDate checkOut) {
            this.reservationId = reservationId;
            this.room = room;
            this.customerName = customerName;
            this.checkIn = checkIn;
            this.checkOut = checkOut;
            int nights = (int) ChronoUnit.DAYS.between(checkIn, checkOut);
            this.totalPrice = room.getPricingStrategy().calculatePrice(nights);
            this.status = ReservationStatus.CONFIRMED;
        }

        public boolean overlapsWith(LocalDate start, LocalDate end) {
            if (this.status == ReservationStatus.CANCELLED) return false;
            return checkIn.isBefore(end) && checkOut.isAfter(start);
        }

        public void cancel(LocalDate currentDate) {
            if (currentDate.isAfter(checkIn)) {
                throw new IllegalStateException("Cannot cancel reservation after or on check-in date.");
            }
            this.status = ReservationStatus.CANCELLED;
        }

        public Room getRoom() { return room; }
        public double getTotalPrice() { return totalPrice; }
        public LocalDate getCheckIn() { return checkIn; }
        public LocalDate getCheckOut() { return checkOut; }
        public ReservationStatus getStatus() { return status; }
    }

    public static class BookingManager {
        private final List<Room> rooms = new ArrayList<>();
        private final List<Reservation> reservations = new ArrayList<>();

        public void addRoom(Room room) { rooms.add(room); }

        public Optional<Reservation> bookRoom(String roomId, String customer, LocalDate checkIn, LocalDate checkOut) {
            Room room = rooms.stream()
                    .filter(r -> r.getRoomId().equals(roomId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Room not found"));

            boolean isOverlapping = reservations.stream()
                    .filter(r -> r.getRoom().getRoomId().equals(roomId))
                    .anyMatch(r -> r.overlapsWith(checkIn, checkOut));

            if (isOverlapping) {
                return Optional.empty();
            }

            Reservation res = new Reservation(UUID.randomUUID().toString(), room, customer, checkIn, checkOut);
            reservations.add(res);
            return Optional.of(res);
        }

        public boolean cancelReservation(Reservation reservation, LocalDate currentDate) {
            try {
                reservation.cancel(currentDate);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    // --- MAIN TEST DRIVER ---
    public static void main(String[] args) {
        BookingManager manager = new BookingManager();

        Room deluxe101 = new Room("Deluxe Room 101", RoomCategory.DELUXE, new DeluxePricing());
        Room standard205 = new Room("Standard Room 205", RoomCategory.STANDARD, new StandardPricing());
        manager.addRoom(deluxe101);
        manager.addRoom(standard205);

        // 1. Deluxe Room 101 (2024-12-01 to 2024-12-05)
        Optional<Reservation> res1 = manager.bookRoom("Deluxe Room 101", "Customer A",
                LocalDate.of(2024, 12, 1), LocalDate.of(2024, 12, 5));
        res1.ifPresent(r -> System.out.printf("%s booked from %s to %s. Total price: $%.2f.%n",
                r.getRoom().getRoomId(), r.getCheckIn(), r.getCheckOut(), r.getTotalPrice()));

        // 2. Standard Room 205 (2024-12-03 to 2024-12-07)
        Optional<Reservation> res2 = manager.bookRoom("Standard Room 205", "Customer B",
                LocalDate.of(2024, 12, 3), LocalDate.of(2024, 12, 7));
        res2.ifPresent(r -> System.out.printf("%s booked from %s to %s. Total price: $%.2f.%n",
                r.getRoom().getRoomId(), r.getCheckIn(), r.getCheckOut(), r.getTotalPrice()));

        // 3. Deluxe Room 101 (Overlapping request)
        Optional<Reservation> res3 = manager.bookRoom("Deluxe Room 101", "Customer C",
                LocalDate.of(2024, 12, 3), LocalDate.of(2024, 12, 7));
        if (res3.isEmpty()) {
            System.out.println("Booking failed: Deluxe Room 101 is not available for 2024-12-03 to 2024-12-07.");
        }

        // 4. Cancel Deluxe Room 101 reservation
        if (res1.isPresent() && manager.cancelReservation(res1.get(), LocalDate.of(2024, 11, 20))) {
            System.out.println("Reservation for Deluxe Room 101 cancelled successfully.");
        }
    }
}