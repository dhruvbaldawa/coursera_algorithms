public class Percolation {
    private boolean[] site;
    private int SIZE;
    private WeightedQuickUnionUF percolation;
    private int virtualTop;
    private int virtualBottom;

    public Percolation(int N) {
        SIZE = N;
        percolation = new WeightedQuickUnionUF(SIZE*SIZE + 2);
        site = new boolean[SIZE*SIZE + 2];
        virtualTop = SIZE*SIZE;
        virtualBottom = SIZE*SIZE + 1;

        for (int i = 0; i < SIZE * SIZE; i++) {
            site[i] = false;
        }

        // open the virtual top
        site[virtualTop] = true;
        // close the virtual bottom
        site[virtualBottom] = false;
    }

    private int getIndex(int x, int y) {
        // By convention, x, y are integers from 1..N
        int i = x - 1;
        int j = y - 1;
        if (i < 0 || i >= SIZE || j < 0 || j >= SIZE) {
            throw new IndexOutOfBoundsException();
        }
        return i*SIZE + j;
    }

    private boolean isValidNeighbor(int i, int j) {
        try {
            if (isOpen(i, j)) {
                return true;
            }
        }
        catch (IndexOutOfBoundsException e) {
            return false;
        }
        return false;
    }
    private void connectNeighbor(int index, int i, int j) {
        if (isValidNeighbor(i, j)) {
            percolation.union(index, getIndex(i, j));
        }
    }

    public void open(int i, int j) {
        if (isOpen(i, j)) {
            return;
        }
        site[getIndex(i, j)] = true;
        int index = getIndex(i, j);

        // If opening in the top row, connect to the virtual top
        if (i == 1) {
            percolation.union(virtualTop, index);
        }
        // If opening in the bottom row, connect to the virtual bottom
        if (i == SIZE) {
            percolation.union(virtualBottom, index);
        }
        // Connecting to all the valid 4-neighbors.
        connectNeighbor(index, i+1, j);
        connectNeighbor(index, i-1, j);
        connectNeighbor(index, i, j+1);
        connectNeighbor(index, i, j-1);
    }

    public boolean isOpen(int i, int j) {
        return site[getIndex(i, j)];
    }

    public boolean isFull(int i, int j) {
        return percolation.connected(virtualTop, getIndex(i, j)) && (
            (isValidNeighbor(i+1, j) && site[getIndex(i+1, j)]) ||
            (isValidNeighbor(i-1, j) && site[getIndex(i-1, j)]) ||
            (isValidNeighbor(i, j+1) && site[getIndex(i, j+1)]) ||
            (isValidNeighbor(i, j-1) && site[getIndex(i, j-1)])
        );
    }

    public boolean percolates() {
        return percolation.connected(virtualTop, virtualBottom);
    }

    private void printStatus() {
        /**
        * Prints the status of the percolation board.
        * "#" if full, "." otherwise
        * "@" if open, "." otherwise
        * status of each cell is of the order <full><open>
        **/
        for (int i = 1; i <= SIZE; i++) {
            for (int j = 1; j <= SIZE; j++) {
                if (isFull(i, j)) {
                    System.out.print("#");
                }
                else {
                    System.out.print(".");
                }

                if (isOpen(i, j)) {
                    System.out.print("@");
                }
                else {
                    System.out.print(".");
                }
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    private static void testBasicCase() {
        /** Test Case **/
        System.out.println("\nTest basic working.");
        Percolation p = new Percolation(5);
        // Check opening
        p.open(1, 1);
        assert p.isOpen(1, 1);
        // Check connection with virtualTop
        assert p.isFull(1, 1);
        // Open diagonally opposite site, should not be full
        p.open(2, 2);
        assert !p.isFull(2, 2);
        // Open few more sites, and check percolation
        p.open(2, 1);
        assert p.isFull(2, 1);  // Vertical percolation
        assert p.isFull(2, 2);  // Horizontal percolation
        assert !p.percolates();  // system should not percolate
        // Quickly take the system to percolation state
        p.open(3, 2);
        p.open(4, 2);
        p.open(5, 2);
        assert p.percolates();  // system should have percolated now
        p.printStatus();
    }

    private static void testBackwash() {
        /** Test Case **/
        System.out.println("\nTest backwash.");
        Percolation p = new Percolation(5);
        p.open(1, 1);
        p.open(2, 1);
        p.open(3, 1);
        p.open(4, 1);
        p.open(5, 1);
        p.open(5, 5);

        assert p.isFull(5, 1);  // 5, 1 should be full because of percolation
        assert !p.isFull(5, 5);  // 5, 5 should not be full (else its backwash)
        p.printStatus();
    }

    public static void main(String[] args) {
        testBasicCase();
        testBackwash();
    }
}
