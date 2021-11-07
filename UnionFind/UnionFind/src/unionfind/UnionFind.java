/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unionfind;

/**
 *
 * @author eugen
 */
public class UnionFind {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int n = 100000;
        QuickUF uf = new QuickUF(n);
        System.out.println("connection");
        for(int i = 0; i < 80000; i++) {
            int x = (int) Math.floor(Math.random() * n);
            int y = (int) Math.floor(Math.random() * n);
            System.out.print(x + " " + y + "\n");
            uf.union(x, y);
        }
        System.out.println("test");
        for(int i = 0; i < 50000; i++) {
            int x = (int) Math.floor(Math.random() * n);
            int y = (int) Math.floor(Math.random() * n);
            System.out.print(x + " " + y + " " + uf.connected(x, y) + "\n");
        }
        
    }
    
}
