class ListNode {
    int val;
    ListNode next;

    ListNode(int val) {
        this.val = val;
    }
}

public class IntersectionLinkedList {

    public static ListNode findIntersection(ListNode a, ListNode b) {
        if (a == null || b == null) {
            return null;
        }

        ListNode p1 = a;
        ListNode p2 = b;

        while (p1 != p2) {
            p1 = (p1 == null) ? b : p1.next;
            p2 = (p2 == null) ? a : p2.next;
        }

        return p1; // Intersection node or null
    }

    public static void main(String[] args) {

        // Shared part: 8 -> 4 -> 5
        ListNode common = new ListNode(8);
        common.next = new ListNode(4);
        common.next.next = new ListNode(5);

        // List A: 4 -> 1 -> 8 -> 4 -> 5
        ListNode a = new ListNode(4);
        a.next = new ListNode(1);
        a.next.next = common;

        // List B: 5 -> 6 -> 1 -> 8 -> 4 -> 5
        ListNode b = new ListNode(5);
        b.next = new ListNode(6);
        b.next.next = new ListNode(1);
        b.next.next.next = common;

        ListNode result = findIntersection(a, b);

        if (result != null) {
            System.out.println("Intersection at node: " + result.val);
        } else {
            System.out.println("No intersection");
        }
    }
}
