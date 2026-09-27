import java.time.LocalDate;

public class ques4 {

    // --- EMPLOYEE EXTENSIBILITY (Strategy) ---
    public static abstract class Employee {
        private final String id;
        private final String name;

        public Employee(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getName() { return name; }
        public abstract String getEmploymentType();
    }

    public static class FullTimeEmployee extends Employee {
        public FullTimeEmployee(String id, String name) { super(id, name); }
        @Override public String getEmploymentType() { return "Full-time"; }
    }

    public static class PartTimeEmployee extends Employee {
        public PartTimeEmployee(String id, String name) { super(id, name); }
        @Override public String getEmploymentType() { return "Part-time"; }
    }

    // --- STATE PATTERN FOR LEAVE REQUEST ---
    public enum LeaveStatus { PENDING, APPROVED, REJECTED }

    public static class LeaveRequest {
        private final Employee employee;
        private final LocalDate startDate;
        private final LocalDate endDate;
        private LeaveStatus status;

        public LeaveRequest(Employee employee, LocalDate startDate, LocalDate endDate) {
            this.employee = employee;
            this.startDate = startDate;
            this.endDate = endDate;
            this.status = LeaveStatus.PENDING;
        }

        public void approve() {
            if (status != LeaveStatus.PENDING) {
                throw new IllegalStateException("Only pending requests can be approved.");
            }
            this.status = LeaveStatus.APPROVED;
        }

        public void reject() {
            if (status != LeaveStatus.PENDING) {
                throw new IllegalStateException("Only pending requests can be rejected.");
            }
            this.status = LeaveStatus.REJECTED;
        }

        public void revertToPending() {
            if (status == LeaveStatus.APPROVED || status == LeaveStatus.REJECTED) {
                throw new IllegalStateException("Cannot change status: " + status + " request cannot revert to Pending.");
            }
            this.status = LeaveStatus.PENDING;
        }

        public Employee getEmployee() { return employee; }
        public LocalDate getStartDate() { return startDate; }
        public LocalDate getEndDate() { return endDate; }
        public LeaveStatus getStatus() { return status; }
    }

    // --- MAIN TEST DRIVER ---
    public static void main(String[] args) {
        Employee john = new FullTimeEmployee("E001", "John Doe");
        Employee jane = new PartTimeEmployee("E002", "Jane Smith");

        // 1. John Doe submits request
        LeaveRequest johnRequest = new LeaveRequest(john, LocalDate.of(2024, 10, 10), LocalDate.of(2024, 10, 12));
        System.out.printf("Leave request submitted by %s for %s to %s. Status: %s.%n",
                john.getName(), johnRequest.getStartDate(), johnRequest.getEndDate(), johnRequest.getStatus());

        // 2. Manager approves John's request
        johnRequest.approve();
        System.out.printf("Leave request for %s approved. Status: %s.%n", john.getName(), johnRequest.getStatus());

        // 3. Jane Smith submits request
        LeaveRequest janeRequest = new LeaveRequest(jane, LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 5));
        System.out.printf("Leave request submitted by %s for %s to %s. Status: %s.%n",
                jane.getName(), janeRequest.getStartDate(), janeRequest.getEndDate(), janeRequest.getStatus());

        // 4. Attempt invalid state reversal
        try {
            johnRequest.revertToPending();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}