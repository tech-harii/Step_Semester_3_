public class exp2 {

    private double basicSalary;
    private double bonus;

    public exp2(double openingBasicSalary) {
        if (openingBasicSalary < 0) {
            System.out.println("Warning: negative opening salary given, starting at 0 instead.");
            this.basicSalary = 0;
        } else {
            this.basicSalary = openingBasicSalary;
        }

        this.bonus = 0;
    }

    public void creditBonus(double amount) {
        if (amount <= 0) {
            System.out.println("Bonus amount must be positive — rejected.");
            return;
        }

        bonus += amount;
        System.out.println("Bonus credited: Rs " + amount);
    }

    public void deductTax(double percent) {
        if (percent < 0 || percent > 100) {
            System.out.println("Tax percent must be between 0 and 100 — rejected.");
            return;
        }

        basicSalary -= basicSalary * (percent / 100);
        System.out.println("Tax deducted: " + (int) percent + "%");
    }

    public double getNetSalary() {
        return basicSalary + bonus;
    }

    public static void main(String[] args) {

        exp2 account = new exp2(50000);

        account.creditBonus(5000);
        account.deductTax(10);

        System.out.println("Net salary: Rs " + account.getNetSalary());
    }
}