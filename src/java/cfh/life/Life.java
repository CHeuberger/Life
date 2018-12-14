package cfh.life;

public class Life {

    int x;
    int y;
    int w;
    int h;
    
    boolean[][] data;
    
    
    Life(int width, int height) {
        x = 0;
        y = 0;
        w = width;
        h = height;
        data = new boolean[h][w];
    }
}
