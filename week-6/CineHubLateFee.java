
import java.util.Arrays;

class EventTicket {
    private final String attendeeId;
    private final double basePrice;
    private double balanceDue;

    private static final int MAX_LATE_FEES = 10;
    private final double[] lateFeeHistory = new double[MAX_LATE_FEES];
    private int lateFeeCount = 0;

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

    protected void applyLateFee(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Late fee must be positive");
        }
        if (lateFeeCount >= MAX_LATE_FEES) {
            throw new IllegalStateException("Late-fee history full (max " + MAX_LATE_FEES + ")");
        }
        // Apply to balance and record in the audit trail.
        balanceDue += amount;
        lateFeeHistory[lateFeeCount++] = amount;
    }

    // Defensive copy — caller can never touch the real internal array.
    public double[] getLateFeeHistory() {
        return Arrays.copyOf(lateFeeHistory, lateFeeCount);
    }

    public String printTicket() {
        return "Standard Event Ticket | Balance Due: " + balanceDue;
    }

    public static String classifyGeneration(EventTicket ticket) {
        if (ticket == null) return "Unknown ticket";
        if (ticket instanceof PremiumWorkshopTicket) return "Multilevel descendant (3 generations deep)";
        if (ticket instanceof WorkshopTicket)         return "Workshop descendant (2 generations deep)";
        if (ticket instanceof HackathonTicket)        return "Hierarchical sibling (independent branch)";
        return "Base generation (root)";
    }

    public static double getTotalBalanceDue(EventTicket[] tickets) {
        double total = 0;
        if (tickets == null) return total;
        for (EventTicket t : tickets) if (t != null) total += t.getBalanceDue();
        return total;
    }

    public static String registerBatch(String[] attendeeIds, double basePrice) {
        int registered = 0, rejected = 0;
        if (attendeeIds == null) return "Registered: 0 | Rejected: 0";
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

class WorkshopTicket extends EventTicket {
    private final String track;

    public WorkshopTicket(String attendeeId, double basePrice, String track) {
        super(attendeeId, basePrice);
        this.track = track;
    }

    public String getTrack() {
        return track;
    }

    // Reuse parent logic — do NOT reimplement recording/deduction.
    @Override
    protected void applyLateFee(double amount) {
        super.applyLateFee(amount * 2);
    }

    @Override
    public String printTicket() {
        return "Workshop Ticket | Track: " + track + " | Balance Due: " + getBalanceDue();
    }
}

class PremiumWorkshopTicket extends WorkshopTicket {
    private final double kitFee;

    public PremiumWorkshopTicket(String attendeeId, double basePrice, String track, double kitFee) {
        super(attendeeId, basePrice, track);
        this.kitFee = kitFee;
    }

    public double getKitFee() {
        return kitFee;
    }

    @Override
    public String printTicket() {
        return "Premium Workshop Ticket | Track: " + getTrack()
                + " | Kit Fee: " + kitFee
                + " | Balance Due: " + getBalanceDue();
    }
}

class HackathonTicket extends EventTicket {
    private final String teamName;

    public HackathonTicket(String attendeeId, double basePrice, String teamName) {
        super(attendeeId, basePrice);
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }

    @Override
    public String printTicket() {
        return "Hackathon Ticket | Team: " + teamName
                + " | Balance Due: " + getBalanceDue();
    }
}

public class CineHubLateFee {
    public static void main(String[] args) {


        WorkshopTicket w = new WorkshopTicket("STU2", 1200, "AI/ML");
        w.pay(1200);
        w.applyLateFee(100);   // recorded as 200 in history
        System.out.println("Balance due: " + w.getBalanceDue()); // 200.0

        double[] history = w.getLateFeeHistory();
        System.out.println("History before tampering: " + Arrays.toString(history)); // [200.0]

        history[0] = 999; // tamper with the returned copy only
        System.out.println("History after tampering:  " + Arrays.toString(w.getLateFeeHistory())); // [200.0]

        EventTicket e = new EventTicket("STU1", 500);
        e.pay(500);
        e.applyLateFee(50);    // recorded as 50
        System.out.println("Base balance due: " + e.getBalanceDue());           // 50.0
        System.out.println("Base history:     " + Arrays.toString(e.getLateFeeHistory())); // [50.0]


        EventTicket[] all = {
            new EventTicket("STU1", 500),
            new WorkshopTicket("STU2", 1200, "AI/ML"),
            new PremiumWorkshopTicket("STU3", 2000, "Cloud Native", 300),
            new HackathonTicket("STU4", 800, "Byte Force")
        };
        System.out.println("Total balance due: " + EventTicket.getTotalBalanceDue(all)); // 4500.0
    }
}