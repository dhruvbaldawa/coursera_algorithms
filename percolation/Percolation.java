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
        return (i-1)*SIZE + (j-1)*SIZE;
    }

    private boolean connectNeighbor(int index, int i, int j) {
        try {
            if(isOpen(i, j)) {
                percolation.union(index, indexOf(i, j));
            }
        }
        catch (IndexOutOfBoundsException e) {
            return false;
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

        // open the virtual top and virtual bottom tops
        site[virtualTop] = true;
        site[virtualBottom] = true;
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
        connectNeighbor(i+1, j);
        connectNeighbor(i-1, j);
        connectNeighbor(i, j+1);
        connectNeighbor(i, j-1);
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
}
