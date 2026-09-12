
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

    public double getBalanceDue() { return balanceDue; }
    public String getAttendeeId() { return attendeeId; }
    public double getBasePrice()  { return basePrice; }

    protected void applyLateFee(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Late fee must be positive");
        if (lateFeeCount >= MAX_LATE_FEES) throw new IllegalStateException("Late-fee history full");
        balanceDue += amount;
        lateFeeHistory[lateFeeCount++] = amount;
    }

    public double[] getLateFeeHistory() {
        return Arrays.copyOf(lateFeeHistory, lateFeeCount);
    }

    public String printTicket() {
        return "Standard | Balance: " + balanceDue;
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
            try { new EventTicket(id, basePrice); registered++; }
            catch (IllegalArgumentException e) { rejected++; }
        }
        return "Registered: " + registered + " | Rejected: " + rejected;
    }

    // ---- Nightly Announcer ----
    public static String batchPrint(EventTicket[] tickets) {
        StringBuilder sb = new StringBuilder();
        if (tickets == null) return "";

        for (EventTicket t : tickets) {
            // Polymorphic call — no instanceof chain deciding what to print.
            sb.append(t.printTicket());

            // Guarded downcast: check FIRST, cast only inside the true branch.
            if (t instanceof WorkshopTicket) {
                WorkshopTicket wt = (WorkshopTicket) t;
                sb.append(" [Track via downcast: ").append(wt.getTrack()).append("]");
            }

            sb.append(" | ");
        }
        return sb.toString();
    }
}

class WorkshopTicket extends EventTicket {
    private final String track;

    public WorkshopTicket(String attendeeId, double basePrice, String track) {
        super(attendeeId, basePrice);
        this.track = track;
    }

    public String getTrack() { return track; }

    @Override
    protected void applyLateFee(double amount) {
        super.applyLateFee(amount * 2);
    }

    @Override
    public String printTicket() {
        return "Workshop | Track: " + track + " | Balance: " + getBalanceDue();
    }
}

class PremiumWorkshopTicket extends WorkshopTicket {
    private final double kitFee;

    public PremiumWorkshopTicket(String attendeeId, double basePrice, String track, double kitFee) {
        super(attendeeId, basePrice, track);
        this.kitFee = kitFee;
    }

    public double getKitFee() { return kitFee; }

    @Override
    public String printTicket() {
        return "Premium Workshop | Track: " + getTrack()
                + " | Kit Fee: " + kitFee
                + " | Balance: " + getBalanceDue();
    }
}

class HackathonTicket extends EventTicket {
    private final String teamName;

    public HackathonTicket(String attendeeId, double basePrice, String teamName) {
        super(attendeeId, basePrice);
        this.teamName = teamName;
    }

    public String getTeamName() { return teamName; }

    @Override
    public String printTicket() {
        return "Hackathon | Team: " + teamName + " | Balance: " + getBalanceDue();
    }
}

public class CineHubAnnouncer {
    public static void main(String[] args) {

        // --- Demo 1: basic batchPrint ---
        EventTicket[] batch = {
            new EventTicket("STU1", 500),
            new WorkshopTicket("STU2", 1200, "AI/ML")
        };
        System.out.println(EventTicket.batchPrint(batch));

        // --- Demo 2: mixed family, one pass ---
        EventTicket[] mixed = {
            new EventTicket("STU1", 500),
            new WorkshopTicket("STU2", 1200, "AI/ML"),
            new PremiumWorkshopTicket("STU3", 2000, "Cloud Native", 300),
            new HackathonTicket("STU4", 800, "Byte Force")
        };
        System.out.println(EventTicket.batchPrint(mixed));

        // --- Demo 3: avoid ClassCastException by checking first ---
        EventTicket plain = new EventTicket("STU5", 500);
        if (plain instanceof WorkshopTicket) {
            WorkshopTicket wt = (WorkshopTicket) plain; // safe downcast
            System.out.println(wt.getTrack());
        } else {
            System.out.println("Not a WorkshopTicket — downcast skipped safely.");
        }

    
    }
}