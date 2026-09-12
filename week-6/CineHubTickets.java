// CineHubTickets.java
// Compile:  javac CineHubTickets.java
// Run:      java CineHubTickets

class EventTicket {
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

    public String printTicket() {
        return "Standard Event Ticket | Balance Due: " + balanceDue;
    }

    // instanceof-only classifier, no manual "type" field.
    public static String classifyGeneration(EventTicket ticket) {
        if (ticket == null) {
            return "Unknown ticket";
        }
        if (ticket instanceof PremiumWorkshopTicket) {
            return "Multilevel descendant (3 generations deep)";
        } else if (ticket instanceof WorkshopTicket) {
            return "Workshop descendant (2 generations deep)";
        } else if (ticket instanceof HackathonTicket) {
            return "Hierarchical sibling (independent branch)";
        } else if (ticket instanceof EventTicket) {
            return "Base generation (root)";
        }
        return "Unknown ticket";
    }

    // Trusts polymorphism — no type checks inside the loop.
    public static double getTotalBalanceDue(EventTicket[] tickets) {
        double total = 0;
        if (tickets == null) return total;
        for (EventTicket t : tickets) {
            if (t != null) {
                total += t.getBalanceDue();
            }
        }
        return total;
    }

    // Batch registration validator (from earlier problem).
    public static String registerBatch(String[] attendeeIds, double basePrice) {
        int registered = 0, rejected = 0;
        if (attendeeIds == null) {
            return "Registered: 0 | Rejected: 0";
        }
        for (String id : attendeeIds) {
            try {
                new EventTicket(id, basePrice); // constructor is the only validator
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

public class CineHubTickets {
    public static void main(String[] args) {

        // --- printTicket() demo ---
        EventTicket standard  = new EventTicket("STU1", 500);
        WorkshopTicket workshop = new WorkshopTicket("STU2", 1200, "AI/ML");
        PremiumWorkshopTicket premium = new PremiumWorkshopTicket("STU3", 2000, "Cloud Native", 300);
        HackathonTicket hackathon = new HackathonTicket("STU4", 800, "Byte Force");

        System.out.println(standard.printTicket());
        System.out.println(workshop.printTicket());
        System.out.println(premium.printTicket());
        System.out.println(hackathon.printTicket());

        // --- classifyGeneration() demo ---
        System.out.println(EventTicket.classifyGeneration(premium));
        System.out.println(EventTicket.classifyGeneration(hackathon));

        // --- getTotalBalanceDue() demo ---
        EventTicket[] all = { standard, workshop, premium, hackathon };
        System.out.println(EventTicket.getTotalBalanceDue(all)); // 4500.0

        // --- WorkshopTicket pay() demo ---
        WorkshopTicket w = new WorkshopTicket("STU2", 1200, "AI/ML");
        w.pay(500);
        System.out.println(w.getBalanceDue()); // 700.0

        // --- registerBatch() demo ---
        String result = EventTicket.registerBatch(
                new String[]{"STU1", "ST1", "STU2", " ", "STU3"}, 500);
        System.out.println(result); // Registered: 3 | Rejected: 2
    }
}