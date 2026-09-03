public class exp1 {

    String title;
    String author;
    int copiesAvailable;

    public exp1(String title, String author, int copiesAvailable) {
        this.title = title;
        this.author = author;
        this.copiesAvailable = copiesAvailable;
    }

    void printEntry() {
        System.out.println(title + " by " + author + " - " + copiesAvailable + " copies available");
    }

    public static void main(String[] args) {

        exp1[] books = new exp1[4];

        books[0] = new exp1("Clean Code", "Robert C. Martin", 3);
        books[1] = new exp1("Effective Java", "Joshua Bloch", 5);
        books[2] = new exp1("Refactoring", "Martin Fowler", 0);
        books[3] = new exp1("Design Patterns", "GoF", 2);

        for (exp1 book : books) {
            book.printEntry();
        }
    }
}