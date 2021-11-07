
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/*

public class PercolationStats {
   public PercolationStats(int n, int trials)    // perfor m trials independent experiments on an n-by-n grid
   public double mean()                          // sample mean of percolation threshold
   public double stddev()                        // sample standard deviation of percolation threshold
   public double confidenceLo()                  // low  endpoint of 95% confidence interval
   public double confidenceHi()                  // high endpoint of 95% confidence interval

   public static void main(String[] args)        // test client (described below)
}

*/

public class PercolationStats {
    
    private final int n;
    private final int trials;
    
    private double p;
    private double s;
    private double confideceMin;
    private double confideceMax;
        
    private void checkArguments(int n, int trials) {
        if (n < 1 || trials < 1) {
            throw  new IllegalArgumentException();
        }
    }
    
    private double startPercolation() {
        Percolation percolation = new Percolation(this.n);
        int row, col;
        do {
            do {
                row = StdRandom.uniform(0, n) + 1;
                col = StdRandom.uniform(0, n) + 1;
            }
            while (percolation.isOpen(row, col));
            percolation.open(row, col);
        }
        while (!percolation.percolates());
        return percolation.numberOfOpenSites() / this.n;
    }
  
    private void startTest() {
        this.p = 0;
        this.s = 0;
        double[] thresholds = new double[this.trials];
        for (int i = 0; i < this.trials; i++) {
            thresholds[i] = this.startPercolation();
        }
        this.p = StdStats.mean(thresholds);
        this.s = StdStats.stddev(thresholds);
        this.confideceMin = this.p - (1.96 * this.s / Math.sqrt(this.trials));
        this.confideceMax = this.p + (1.96 * this.s / Math.sqrt(this.trials));
    }
    
    /*
    private void startTest() {
        this.p = 0;
        this.s = 0;
        double[] thresholds = new double[this.trials];
        for (int i = 0; i < this.trials; i++) {
            thresholds[i] = this.startPercolation();
            this.p += thresholds[i];
        }
        this.p /= this.trials;
        for (double threshold : thresholds) {
            this.s += (threshold - this.p) * (threshold - this.p);
        }
        this.s /= (this.trials - 1);
        this.s = Math.sqrt(this.s);
        this.confideceMin = this.p - (1.96 * this.s / Math.sqrt(this.trials));
        this.confideceMax = this.p + (1.96 * this.s / Math.sqrt(this.trials));
    }*/
    
    public double mean() {
        return this.p;
    }
    
    public double stddev() {
        return this.s;
    }
    
    public double confidenceLo() {
        return this.confideceMin;
    }
    
    public double confidenceHi() {
        return this.confideceMax;
    }
    
    public PercolationStats(int n, int trials) {
        this.checkArguments(n, trials);
        this.n = n;
        this.trials = trials;
        this.startTest();
    }
    
    public static void main(String[] args) {
        int N, T;
        N = StdIn.readInt();
        T = StdIn.readInt();
        PercolationStats percolationStats = new PercolationStats(N, T);
        StdOut.print(percolationStats.mean());
        StdOut.print(percolationStats.stddev());
        StdOut.print(percolationStats.confidenceLo());
        StdOut.print(percolationStats.confidenceHi());
    }
    
}
