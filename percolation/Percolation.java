import java.lang.*;


public class Percolation {
    private boolean[] site;
    private int SIZE;
    private WeightedQuickUnionUF percolation;
    private int virtualTop;
    private int virtualBottom;

    private int getIndex(int i, int j) throws IndexOutOfBoundsException {
        if (i < 0 || i >= SIZE || j < 0 || j >= SIZE) {
            throw new IndexOutOfBoundsException();
        }
        return i*SIZE + j;
    }

    private void connectNeighbor(int index, int i, int j) {
        try {
            if(isOpen(i, j)) {
                percolation.union(index, getIndex(i, j));
            }
        }
        catch (IndexOutOfBoundsException e) {
            return;
        }
    }

    public Percolation(int N) {
        SIZE = N;
        percolation = new WeightedQuickUnionUF(SIZE*SIZE + 2);
        site = new boolean[SIZE*SIZE + 2];
        virtualTop = SIZE*SIZE;
        virtualBottom = SIZE*SIZE + 1;

        for(int i=0; i < SIZE*SIZE; i++) {
            site[i] = false;
        }

        // open the virtual top
        site[virtualTop] = true;
        // close the virtual bottom
        site[virtualBottom] = false;
    }

    public void open(int i, int j) {
        if(isOpen(i, j)) {
            return;
        }
        site[getIndex(i, j)] = true;
        int index = getIndex(i, j);

        // If opening in the top row, connect to the virtual top
        if (i == 0) {
            percolation.union(virtualTop, index);
        }
        // If opening in the bottom row, connect to the virtual bottom
        if (i == SIZE - 1) {
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
        return percolation.connected(virtualTop, getIndex(i, j));
    }

    public boolean percolates() {
        return percolation.connected(virtualTop, virtualBottom);
    }

    public void printStatus() {
        /**
        * Prints the status of the percolation board.
        * "#" if full, "." otherwise
        * "@" if open, "." otherwise
        * status of each cell is of the order <full><open>
        **/
        for(int i=0; i<SIZE; i++) {
            for(int j=0; j<SIZE; j++) {
                if(isFull(i, j)) {
                    System.out.print("#");
                }
                else {
                    System.out.print(".");
                }

                if(isOpen(i, j)) {
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

    public static void main(String args[]) {
        /** Test Case **/
        Percolation p = new Percolation(5);
        // Check opening
        p.open(0, 0);
        assert p.isOpen(0, 0);
        // Check connection with virtualTop
        assert p.isFull(0, 0);
        // Open diagonally opposite site, should not be full
        p.open(1, 1);
        assert !p.isFull(1, 1);
        // Open few more sites, and check percolation
        p.open(1, 0);
        assert p.isFull(1, 0);  // Vertical percolation
        assert p.isFull(1, 1);  // Horizontal percolation
        assert !p.percolates();  // system should not percolate
        // Quickly take the system to percolation state
        p.open(2, 1);
        p.open(3, 1);
        p.open(4, 1);
        assert p.percolates();  // system should have percolated now
        p.printStatus();
    }
}
