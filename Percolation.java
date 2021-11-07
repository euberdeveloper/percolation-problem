
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/*

public class Percolation {
   public Percolation(int n)                // create n-by-n grid, with all sites blocked
   public    void open(int row, int col)    // open site (row, col) if  it is not open already
   public boolean isOpen(int row, int col)  // is site (row, col) open?
   public boolean isFull(int row, int col)  // is site (row, col) full?
   public     int numberOfOpenSites()       // number of open sites
   public boolean percolates()              // does the system percolate?

   public static void main(String[] args)   // test client (optional)

}
*/

public class Percolation {
   
    private final int[][] grid;
    private final int n;
    private int openSites;
    
    private final WeightedQuickUnionUF unionFind;
    private final WeightedQuickUnionUF unionFindWithoutBottom;
    private final int top;
    private final int bottom;
    
    private void checkArguments(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
    }
    private void checkArguments(int row, int col) {
        if (row < 1 || row > this.n) {
            throw new IllegalArgumentException();
        }
        if (col < 1 || col > this.n) {
            throw new IllegalArgumentException();
        }
    }
    
    private void union(int x, int y, WeightedQuickUnionUF unionFind) {
        if(!unionFind.connected(x, y)) {
            unionFind.union(x, y);
        }
    }
    
    private void unionTop(int row, int col) {
        if (row == 1) {
            this.union(col, this.top, this.unionFind);
            this.union(col, this.top, this.unionFindWithoutBottom);
        }
    }
    private void unionBottom(int row, int col) {
        if (row == this.n) {
            this.union(this.equivalent(row, col), this.bottom, this.unionFind);
        }
    }
    
    private int equivalent(int row, int col) {
        return ((row - 1) * n) + col;
    }
    private void unionBounds(int row, int col) {
        if (this.grid[row - 1][col] == 1) {
            this.union(this.equivalent(row, col), this.equivalent(row - 1, col), this.unionFind);
            this.union(this.equivalent(row, col), this.equivalent(row - 1, col), this.unionFindWithoutBottom);
        }
        if (this.grid[row][col + 1] == 1) {
            this.union(this.equivalent(row, col), this.equivalent(row, col + 1), this.unionFind);
            this.union(this.equivalent(row, col), this.equivalent(row, col + 1), this.unionFindWithoutBottom);
        }
        if (this.grid[row + 1][col] == 1) {
            this.union(this.equivalent(row, col), this.equivalent(row + 1, col), this.unionFind);
            this.union(this.equivalent(row, col), this.equivalent(row + 1, col), this.unionFindWithoutBottom);
        }
        if (this.grid[row][col - 1] == 1) {
            this.union(this.equivalent(row, col), this.equivalent(row, col - 1), this.unionFind);
            this.union(this.equivalent(row, col), this.equivalent(row, col - 1), this.unionFindWithoutBottom);
        }
    }
    
    public void open(int row, int col) {
        this.checkArguments(row, col);
        if (!this.isOpen(row, col)) {
            this.grid[row][col] = 1;
            this.unionTop(row, col);
            this.unionBottom(row, col);
            this.unionBounds(row, col);
            this.openSites++;
        }
    }
    
    public boolean isOpen(int row, int col) {
        this.checkArguments(row, col);
        return (this.grid[row][col] == 1);
    }
    
    public boolean isFull(int row, int col) {
        this.checkArguments(row, col);
        return this.unionFindWithoutBottom.connected(this.equivalent(row, col), this.top);
    }
    
    public int numberOfOpenSites() {
        return this.openSites;
    }
    
    public boolean percolates() {
        return this.unionFind.connected(this.top, this.bottom);
    }
    
    public Percolation(int n) {
        this.checkArguments(n);
        this.n = n;
        this.grid = new int[n + 2][n + 2];
        for (int i = 0; i < n + 2; i++) {
            for (int j = 0; j < n + 2; j++) {
                this.grid[i][j] = 0;
            }
        }
        this.unionFind = new WeightedQuickUnionUF((n * n) + 2);
        this.unionFindWithoutBottom = new WeightedQuickUnionUF((n * n) + 1);
        this.top = 0;
        this.bottom = ((n * n) + 1);
        this.openSites = 0;
    }
    
}
