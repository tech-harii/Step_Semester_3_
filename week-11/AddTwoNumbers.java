class ListNode {
    int val;
    ListNode next;

    ListNode(int val) {
        this.val = val;
    }
}

public class AddTwoNumbers {

    public static ListNode addNumbers(ListNode a, ListNode b) {
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;

        int carry = 0;

        while (a != null || b != null || carry != 0) {
            int sum = carry;

            if (a != null) {
                sum += a.val;
                a = a.next;
            }

            if (b != null) {
                sum += b.val;
                b = b.next;
            }

            carry = sum / 10;

            current.next = new ListNode(sum % 10);
            current = current.next;
        }

        return dummy.next;
    }

    // Helper method to print a linked list
    public static void printList(ListNode head) {
        while (head != null) {
            System.out.print(head.val);

            if (head.next != null) {
                System.out.print(" -> ");
            }

            head = head.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {

        // Example 1: 342 + 465 = 807
        ListNode a1 = new ListNode(2);
        a1.next = new ListNode(4);
        a1.next.next = new ListNode(3);

        ListNode b1 = new ListNode(5);
        b1.next = new ListNode(6);
        b1.next.next = new ListNode(4);

        ListNode result1 = addNumbers(a1, b1);
        printList(result1);

        // Example 2: 99 + 1 = 100
        ListNode a2 = new ListNode(9);
        a2.next = new ListNode(9);

        ListNode b2 = new ListNode(1);

        ListNode result2 = addNumbers(a2, b2);
        printList(result2);
    }
}