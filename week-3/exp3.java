public class exp3 {

    String empId;
    String empName;
    double salary;
    boolean isIntern;

    public exp3(String empId, String empName, double salary) {
        this.empId = empId;
        this.empName = empName;
        this.salary = salary;
        this.isIntern = false;
    }

    public exp3(String empId, String empName) {
        this(empId, empName, 0);
        this.isIntern = true;
    }

    void printProfile() {
        System.out.println(empId + " | " + empName + " | Rs " + salary + " | Intern: " + isIntern);
    }

    public static void main(String[] args) {

        exp3 permanent = new exp3("E-101", "Divya", 65000);
        exp3 intern = new exp3("E-102", "Arjun");

        permanent.printProfile();
        intern.printProfile();
    }
}