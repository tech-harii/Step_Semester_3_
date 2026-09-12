import java.util.*;
public class EventTicket {
    private final String attendeeId;
    private final double basePrice;
    private double balanceDue;

    public EventTicket(String attendeeId, double basePrice) {

        if (attendeeId == null || attendeeId.trim().isEmpty() || attendeeId.length() < 4) {
            throw new IllegalArgumentException("Invalid attendeeId");
        }
        if (basePrice <= 0) {
            throw new IllegalArgumentException("basePrice must be positive");
        }

        this.attendeeId = attendeeId;
        this.basePrice = basePrice;
        this.balanceDue = basePrice;
    }

    public void pay(double amount) {
        if (amount <= 0 || amount > balanceDue) {
            throw new IllegalArgumentException("Invalid payment amount");
        }
        balanceDue -= amount;
    }

    public double getBalanceDue() {
        return balanceDue;
    }

    public String getAttendeeId() {
        return attendeeId;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public static String registerBatch(String[] attendeeIds, double basePrice) {
        int registered = 0;
        int rejected = 0;

        if (attendeeIds == null) {
            return "Registered: 0 | Rejected: 0";
        }

        for (String id : attendeeIds) {
            try {
                new EventTicket(id, basePrice);
                registered++;
            } catch (IllegalArgumentException e) {
                rejected++;
            }
        }

        return "Registered: " + registered + " | Rejected: " + rejected;
    }
}