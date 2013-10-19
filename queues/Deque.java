import java.util.Iterator;
import java.util.NoSuchElementException;


public class Deque<Item> implements Iterable<Item> {
    private int size;
    private Node<Item> head;
    private Node<Item> tail;
    private static class Node<Item> {
        private Item item;
        private Node next;
        private Node prev;

        public Node(Item i) {
            item = i;
        }
    }

    public Deque() {

    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        Node<Item> node = new Node<Item>(item);
        node.next = head;
        if (head != null) {
            head.prev = node;
        }
        head = node;

        // when there is only one element
        if (isEmpty()) {
            tail = node;
        }
        size += 1;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        Node<Item> node = new Node<Item>(item);
        if (tail != null) {
            tail.next = node;
        }
        node.prev = tail;
        tail = node;

        // when there is only one element
        if (isEmpty()) {
            head = node;
        }
        size += 1;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node<Item> node = head;
        head = head.next;
        size -= 1;
        return node.item;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node<Item> node = tail;
        tail = tail.prev;
        size -= 1;
        return node.item;
    }

    public Iterator<Item> iterator() {
        return new ListIterator<Item>(head);
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        public ListIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // Unit tests
    private static void testBasicDequeOperations() {
        Deque<String> d = new Deque<String>();
        // verify that deque is empty
        assert d.isEmpty();
        assert d.size() == 0;

        // lets add some elements
        d.addFirst("a");
        assert !d.isEmpty();
        assert d.size() == 1;
        assert d.removeLast() == "a";

        d.addLast("z");
        assert !d.isEmpty();
        assert d.size() == 1;
        assert d.removeFirst() == "z";

        assert false: "All tests pass!";
    }

    private static void testIterator() {
        Deque<String> d = new Deque<String>();
        String[] s = {"a", "b", "c", "d", "e"};

        // Setup sample data
        for (String t : s) {
            d.addLast(t);
        }

        // Verify
        int i = 0;
        for (String t : d) {
            assert t == s[i];
            i += 1;
        }
    }

    public static void main(String[] args) {
        testBasicDequeOperations();
        testIterator();
    }
}
