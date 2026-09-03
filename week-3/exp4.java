public class exp4 {

    String studentName;
    int seatNumber;

    public exp4(String studentName, int seatNumber) {
        this.studentName = studentName;
        this.seatNumber = seatNumber;
    }

    public static void main(String[] args) {

        exp4 priya = new exp4("Priya", 0);

        exp4 copy = priya;
        copy.seatNumber = 45;

        exp4 separate = new exp4("Priya", 45);

        System.out.println("Priya's seatNumber (via first variable): " + priya.seatNumber);
        System.out.println("copy == priya: " + (copy == priya));
        System.out.println("separate == priya: " + (separate == priya));
    }
}