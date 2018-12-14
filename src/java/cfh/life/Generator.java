package cfh.life;

import java.util.Arrays;
import java.util.function.Function;

public abstract class Generator implements Function<boolean[][], boolean[][]> {

    protected enum Action {
        STAY, LIVE, DIE, INVERT;
    }

    String name() {
        return getClass().getSimpleName();
    }
    
    @Override
    public abstract boolean[][] apply(boolean[][] data);
    
    protected boolean[][] reduce(boolean[][] data) {
        var top = false;
        for (var i = 0; i < data[0].length; i++) {
            if (data[0][i]) {
                top = true;
                break;
            }
        }
        if (!top) {
            data = Arrays.copyOfRange(data, 1, data.length);
        }
        
        var bottom = false;
        for (var i = 0; i < data[0].length; i++) {
            if (data[data.length-1][i]) {
                bottom = true;
                break;
            }
        }
        if (!bottom) {
            data = Arrays.copyOf(data, data.length-1);
        }
        
        var left = false;
        for (var j = 0; j < data.length; j++) {
            if (data[j][0]) {
                left = true;
                break;
            }
        }
        if (!left) {
            for (var j = 0; j < data.length; j++) {
                data[j] = Arrays.copyOfRange(data[j], 1, data[j].length);
            }
        }
        
        var right = false;
        for (var j = 0; j < data.length; j++) {
            if (data[j][data[j].length-1]) {
                right = true;
                break;
            }
        }
        if (!right) {
            for (var j = 0; j < data.length; j++) {
                data[j] = Arrays.copyOf(data[j], data[j].length-1);
            }
        }

        return data;
    }
}
