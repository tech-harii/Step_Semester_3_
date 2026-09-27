import java.util.*;

public class Question5_FoodOrderSystem {

    public static class FoodItem {
        private final String name;
        private final double price;

        public FoodItem(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() { return name; }
        public double getPrice() { return price; }
    }

    public static class LineItem {
        private final FoodItem item;
        private final int quantity;

        public LineItem(FoodItem item, int quantity) {
            this.item = item;
            this.quantity = quantity;
        }

        public double getSubtotal() { return item.getPrice() * quantity; }
        public FoodItem getItem() { return item; }
        public int getQuantity() { return quantity; }
    }

    // --- PAYMENT STRATEGY ---
    public interface IPaymentMethod {
        boolean processPayment(double amount);
        String getMethodName();
    }

    public static class CreditCardPayment implements IPaymentMethod {
        private final boolean simulateSuccess;

        public CreditCardPayment(boolean simulateSuccess) {
            this.simulateSuccess = simulateSuccess;
        }

        @Override public boolean processPayment(double amount) { return simulateSuccess; }
        @Override public String getMethodName() { return "Credit Card"; }
    }

    public static class DigitalWalletPayment implements IPaymentMethod {
        private final boolean simulateSuccess;

        public DigitalWalletPayment(boolean simulateSuccess) {
            this.simulateSuccess = simulateSuccess;
        }

        @Override public boolean processPayment(double amount) { return simulateSuccess; }
        @Override public String getMethodName() { return "Digital Wallet"; }
    }

    // --- OBSERVER PATTERN FOR NOTIFICATIONS ---
    public interface NotificationListener {
        void onEvent(String message);
    }

    public enum OrderStatus { CREATED, PLACED, PAID, PENDING_PAYMENT }

    public static class Order {
        private final String orderId;
        private final List<LineItem> items = new ArrayList<>();
        private OrderStatus status = OrderStatus.CREATED;
        private final List<NotificationListener> listeners = new ArrayList<>();

        public Order(String orderId) {
            this.orderId = orderId;
        }

        public void addListener(NotificationListener listener) { listeners.add(listener); }

        private void notifyListeners(String message) {
            for (NotificationListener listener : listeners) {
                listener.onEvent(message);
            }
        }

        public void addItem(FoodItem item, int quantity) {
            items.add(new LineItem(item, quantity));
        }

        public double calculateTotal() {
            return items.stream().mapToDouble(LineItem::getSubtotal).sum();
        }

        public boolean placeAndPay(IPaymentMethod paymentMethod) {
            if (items.isEmpty()) {
                System.out.println("Cannot place order: Order must contain at least one item.");
                return false;
            }

            this.status = OrderStatus.PLACED;
            System.out.println("Order placed.");

            boolean success = paymentMethod.processPayment(calculateTotal());
            if (success) {
                this.status = OrderStatus.PAID;
                System.out.println("Payment via " + paymentMethod.getMethodName() + " successful.");
                System.out.println("Order status: Paid.");
                notifyListeners("Notification: Order #" + orderId + " placed and paid.");
            } else {
                this.status = OrderStatus.PENDING_PAYMENT;
                System.out.println("Payment via " + paymentMethod.getMethodName() + " failed.");
                System.out.println("Order status: Pending Payment.");
                notifyListeners("Notification: Order #" + orderId + " placed, awaiting payment.");
            }
            return success;
        }
    }

    // --- MAIN TEST DRIVER ---
    public static void main(String[] args) {
        FoodItem pizza = new FoodItem("Pizza", 12.00);
        FoodItem soda = new FoodItem("Soda", 2.50);
        FoodItem burger = new FoodItem("Burger", 8.00);

        NotificationListener customerNotifier = message -> System.out.println(message);

        Order order1 = new Order("123");
        order1.addListener(customerNotifier);
        order1.addItem(pizza, 2);
        order1.addItem(soda, 1);
        System.out.println("Order created. Added Pizza (Qty 2), Soda (Qty 1).");

        Order emptyOrder = new Order("122");
        emptyOrder.placeAndPay(new CreditCardPayment(true));

        // 3. Place Order 1 with successful Credit Card payment
        order1.placeAndPay(new CreditCardPayment(true));

        System.out.println();

        Order order2 = new Order("124");
        order2.addListener(customerNotifier);
        order2.addItem(burger, 1);
        order2.placeAndPay(new DigitalWalletPayment(false));
    }
}