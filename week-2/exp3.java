public class exp3 {

    static void parseInventoryRecord(String csvLine) {
        String[] fields = csvLine.split(",");

        if (fields.length != 3) {
            System.out.println("Invalid Record");
            return;
        }

        String product = fields[0];
        String sku = fields[1];
        String qty = fields[2];

        System.out.println("Product: " + product + " | SKU: " + sku + " | Qty: " + qty);
    }

    public static void main(String[] args) {
        System.out.println("Test 1:");
        parseInventoryRecord("Wireless Mouse,WM-2201,150");

        System.out.println("\nTest 2:");
        parseInventoryRecord("Wireless Mouse,150");
    }
}