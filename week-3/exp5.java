public class exp5 {

    String empName;
    double salary;

    static String companyName = "Bright Horizon Technologies";
    static int employeeCount = 0;

    public exp5(String empName, double salary) {
        this.empName = empName;
        this.salary = salary;
        employeeCount++;
    }

    static void printCompanyInfo() {
        System.out.println(companyName);
        System.out.println("Employees on record: " + employeeCount);
    }

    public static void main(String[] args) {

        exp5 emp1 = new exp5("Ravi", 40000);
        exp5 emp2 = new exp5("Anita", 45000);
        exp5 emp3 = new exp5("Kiran", 42000);

        exp5.printCompanyInfo();
    }
}