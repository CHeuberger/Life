package cfh.life;

import static cfh.life.Generator.Action.*;

import java.util.List;

public class Asterisk extends Generator {
    
    //                                                  0    1    2     3     4    5    6    7    8
    private static final List<Action> actions = List.of(DIE, DIE, STAY, LIVE, DIE, DIE, DIE, DIE, DIE);

    @Override
    public boolean[][] apply(boolean[][] data) {
        boolean tmp[][] = new boolean[data.length+2][data[0].length+2];
        for (var j = -1; j < data.length+1; j++) {
            for (var i = -1; i < data[0].length+1; i++) {
                var cnt = 0;
                for (var dj = -1 ; dj <= +1 ; dj++) {
                    for (var di = -1; di <= +1; di += (dj==0 ? 2 : 1)) {
                        cnt += test(i + di, j + dj, data);
                    }
                }
                boolean value;
                switch (actions.get(cnt)) {
                    case STAY:
                        value = 0<=i && i<data[0].length && 0<=j && j<data.length && data[j][i];
                        break;
                    case LIVE:
                        value = true;
                        break;
                    case DIE:
                        value = false;
                        break;
                    case INVERT:
                        value = 0<=i && i<data[0].length && 0<=j && j<data.length && !data[j][i];
                        break;
                    default:
                        throw new IllegalArgumentException(actions.get(cnt) + " for cnt=" + cnt);
                }
                tmp[j+1][i+1] = value;
            }
        }

        return reduce(tmp);
    }

    private static int test(int i, int j, boolean[][] data) {
        if (0 <= i && i < data[0].length && 0 <= j && j < data.length && data[j][i])
            return 1;
        else
            return 0;
    }
}
