
class EventTicket {
    
    private static int counter = 0;

    private final String ticketId;

    private final double basePrice;
    private double balanceDue;

    public EventTicket(double basePrice) {
        if (basePrice <= 0) {
            throw new IllegalArgumentException("basePrice must be positive");
        }
        counter++;
        this.ticketId = "TCK-" + (1000 + counter);
        this.basePrice = basePrice;
        this.balanceDue = basePrice;
    }

    // ---- Getters ----
    public String getTicketId()   { return ticketId; }
    public double getBasePrice()  { return basePrice; }
    public double getBalanceDue() { return balanceDue; }

    public static int getTicketsIssued() { return counter; }

    public void pay(double amount) {
        if (amount <= 0 || amount > balanceDue) {
            throw new IllegalArgumentException("Invalid payment amount");
        }
        balanceDue -= amount;
    }

    public void pay(double amount, String mode) {
        System.out.println("Payment mode: " + mode);
        pay(amount);
    }
    public static boolean isValidPromoCode(String code) {
        if (code == null || code.length() != 5) return false;

        if (code.charAt(0) != 'F')                 return false;
        if (!Character.isDigit(code.charAt(1)))    return false;
        if (!Character.isDigit(code.charAt(2)))    return false;
        if (!Character.isDigit(code.charAt(3)))    return false;
        if (!Character.isUpperCase(code.charAt(4))) return false;

        return true;
    }
    public String printTicket() {
        return "Standard | ID: " + ticketId + " | Balance: " + balanceDue;
    }

    public static String processNightlySettlement(EventTicket[] tickets) {
        int processed  = 0;
        int nullSkipped = 0;
        int groupCount  = 0;
        int individual  = 0;

        if (tickets == null) {
            return "0 processed | 0 null skipped | 0 group | 0 individual";
        }

        for (EventTicket t : tickets) {
            if (t == null) {
                nullSkipped++;
                continue;
            }
            processed++;
            if (t instanceof GroupTicket) {
                groupCount++;
            } else {
                individual++;
            }
        }

        return processed + " processed | "
                + nullSkipped + " null skipped | "
                + groupCount + " group | "
                + individual + " individual";
    }
}

class WorkshopTicket extends EventTicket {
    private final String track;

    public WorkshopTicket(double basePrice, String track) {
        super(basePrice);
        this.track = track;
    }

    public String getTrack() { return track; }

    @Override
    public String printTicket() {
        return "Workshop | ID: " + getTicketId()
                + " | Track: " + track
                + " | Balance: " + getBalanceDue();
    }
}

class PremiumWorkshopTicket extends WorkshopTicket {
    private final double kitFee;

    public PremiumWorkshopTicket(double basePrice, String track, double kitFee) {
        super(basePrice, track);
        this.kitFee = kitFee;
    }

    public double getKitFee() { return kitFee; }

    @Override
    public String printTicket() {
        return "Premium Workshop | ID: " + getTicketId()
                + " | Track: " + getTrack()
                + " | Kit Fee: " + kitFee
                + " | Balance: " + getBalanceDue();
    }
}

class HackathonTicket extends EventTicket {
    private final String teamName;

    public HackathonTicket(double basePrice, String teamName) {
        super(basePrice);
        this.teamName = teamName;
    }

    public String getTeamName() { return teamName; }

    @Override
    public String printTicket() {
        return "Hackathon | ID: " + getTicketId()
                + " | Team: " + teamName
                + " | Balance: " + getBalanceDue();
    }
}

class GroupTicket extends EventTicket {
    private final int groupSize;

    public GroupTicket(double basePrice, int groupSize) {
        super(basePrice);
        if (groupSize <= 0) {
            throw new IllegalArgumentException("groupSize must be positive");
        }
        this.groupSize = groupSize;
    }

    public int getGroupSize() { return groupSize; }

    @Override
    public String printTicket() {
        return "Group | ID: " + getTicketId()
                + " | Group Size: " + groupSize
                + " | Balance: " + getBalanceDue();
    }
}

public class FestTicketSystem {
    public static void main(String[] args) {

        // ---- Demo 1: auto-incrementing ticketId + issued counter ----
        EventTicket t1 = new EventTicket(500);
        System.out.println(t1.getTicketId());                 // TCK-1001
        System.out.println(EventTicket.getTicketsIssued());   // 1

        // ---- Demo 2: promo code validator ----
        System.out.println(EventTicket.isValidPromoCode("F123A")); // true
        System.out.println(EventTicket.isValidPromoCode("F12A"));  // false (too short)
        System.out.println(EventTicket.isValidPromoCode("X123A")); // false (wrong prefix)

        // ---- Demo 3: overloaded pay(...) ----
        t1.pay(200);
        t1.pay(200, "UPI");
        System.out.println(t1.getBalanceDue()); // 100.0

        EventTicket[] batch = {
            new GroupTicket(2000, 5),
            null,
            new EventTicket(500)
        };
        System.out.println(EventTicket.processNightlySettlement(batch));

        System.out.println("Tickets issued so far: " + EventTicket.getTicketsIssued());
    }
}