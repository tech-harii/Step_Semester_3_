import java.util.*;

public class ques2 {


    public static abstract class Vehicle {
        private final String licensePlate;
        private final String model;
        private boolean available = true;

        public Vehicle(String licensePlate, String model) {
            this.licensePlate = licensePlate;
            this.model = model;
        }

        public String getModel() { return model; }
        public boolean isAvailable() { return available; }
        public void setAvailable(boolean available) { this.available = available; }

        public abstract double calculateCharge(int days);
    }

    public static class StandardCar extends Vehicle {
        private static final double DAILY_RATE = 50.00;

        public StandardCar(String licensePlate, String model) {
            super(licensePlate, model);
        }

        @Override
        public double calculateCharge(int days) {
            return days * DAILY_RATE;
        }
    }

    public static class LuxuryCar extends Vehicle {
        private static final double DAILY_RATE = 100.00;

        public LuxuryCar(String licensePlate, String model) {
            super(licensePlate, model);
        }

        @Override
        public double calculateCharge(int days) {
            return days * DAILY_RATE;
        }
    }

    public static class SUV extends Vehicle {
        private static final double DAILY_RATE = 80.00;

        public SUV(String licensePlate, String model) {
            super(licensePlate, model);
        }

        @Override
        public double calculateCharge(int days) {
            return days * DAILY_RATE;
        }
    }

    // --- RENTAL DOMAIN ---
    public static class Rental {
        private final String rentalId;
        private final Vehicle vehicle;
        private final String customerName;
        private final int days;
        private final double totalCharge;
        private boolean active;

        public Rental(String rentalId, Vehicle vehicle, String customerName, int days) {
            if (!vehicle.isAvailable()) {
                throw new IllegalStateException("Vehicle " + vehicle.getModel() + " is currently not available.");
            }
            this.rentalId = rentalId;
            this.vehicle = vehicle;
            this.customerName = customerName;
            this.days = days;
            this.totalCharge = vehicle.calculateCharge(days);
            this.vehicle.setAvailable(false);
            this.active = true;
        }

        public void returnVehicle() {
            if (!active) {
                throw new IllegalStateException("Rental is already closed.");
            }
            this.vehicle.setAvailable(true);
            this.active = false;
        }

        public Vehicle getVehicle() { return vehicle; }
        public double getTotalCharge() { return totalCharge; }
        public int getDays() { return days; }
    }

    // --- RENTAL SERVICE ---
    public static class RentalService {
        private final Map<String, Vehicle> inventory = new HashMap<>();

        public void addVehicle(Vehicle v) { inventory.put(v.getModel(), v); }

        public Rental rentVehicle(String model, String customerName, int days) {
            Vehicle vehicle = inventory.get(model);
            if (vehicle == null) throw new IllegalArgumentException("Vehicle model not found.");
            return new Rental(UUID.randomUUID().toString(), vehicle, customerName, days);
        }
    }

    // --- MAIN TEST DRIVER ---
    public static void main(String[] args) {
        RentalService service = new RentalService();
        LuxuryCar luxuryCar = new LuxuryCar("LUX-001", "Luxury Car A");
        StandardCar standardCar = new StandardCar("STD-001", "Standard Car B");

        service.addVehicle(luxuryCar);
        service.addVehicle(standardCar);

        // 1. Rent Luxury Car A for 3 days
        Rental rental1 = service.rentVehicle("Luxury Car A", "Customer 1", 3);
        System.out.printf("Luxury Car A rented for %d days. Total charge: $%.2f.%n", rental1.getDays(), rental1.getTotalCharge());

        // 2. Rent Standard Car B for 5 days
        Rental rental2 = service.rentVehicle("Standard Car B", "Customer 2", 5);
        System.out.printf("Standard Car B rented for %d days. Total charge: $%.2f.%n", rental2.getDays(), rental2.getTotalCharge());

        // 3. Return Luxury Car A
        rental1.returnVehicle();
        System.out.println("Luxury Car A returned. Now available.");
    }
}