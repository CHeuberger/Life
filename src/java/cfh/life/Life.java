package cfh.life;

import java.util.ArrayList;
import java.util.List;

public class Life {

    int x;
    int y;
    int w;
    int h;
    
    boolean[][] data;
    
    private final List<Listener> listeners = new ArrayList<>();
    
    
    Life(int width, int height) {
        x = 0;
        y = 0;
        w = width;
        h = height;
        data = new boolean[h][w];
    }
    
    void data(boolean[][] d) {
        data = d;
        h = d.length;
        w = d[0].length;
        listeners.forEach(Listener::changed);
    }
    
    public void addListener(Listener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }
    
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    public static interface Listener {
        void changed();
        void resized();
    }
}
