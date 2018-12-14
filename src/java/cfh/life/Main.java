package cfh.life;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;


public class Main {
    
    public static void main(String[] args) {
        new Main();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    private Life life;
    private JFrame frame;
    
    private SwingWorker<?, ?> runWorker = null;
    
    private Main() {
        SwingUtilities.invokeLater(this::initDB);
    }
    
    private void initDB() {
        life = new Life(20, 20);
        
        var panel = new LifePanel(life);
        panel.setPreferredSize(new Dimension(500, 500));
        
        var step = new JButton("Step");
        step.addActionListener(this::doStep);
        
        var run = new JButton("Run");
        run.addActionListener(this::doRun);
        
        var quit = new JButton("Quit");
        quit.addActionListener(this::doQuit);
        
        var buttons = Box.createHorizontalBox();
        buttons.add(Box.createHorizontalGlue());
        buttons.add(step);
        buttons.add(Box.createHorizontalStrut(20));
        buttons.add(run);
        buttons.add(Box.createHorizontalGlue());
        buttons.add(quit);
        buttons.add(Box.createHorizontalGlue());
        
        frame = new JFrame("Life");
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttons, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private void doStep(ActionEvent ev) {
        if (runWorker != null) {
            runWorker.cancel(true);
            runWorker = null;
        } else {
            life.data(generateExpand());
        }
    }
    
    private void doRun(ActionEvent ev) {
        if (runWorker == null) {
            runWorker = new SwingWorker<Void, boolean[][]>() {
                @Override
                protected Void doInBackground() throws Exception {
                    try {
                        while (runWorker == this) {
                            publish(generateExpand());
                            Thread.sleep(200);
                        }
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    return null;
                }
                @Override
                protected void process(List<boolean[][]> chunks) {
                    life.data(chunks.get(chunks.size()-1));
                }
            };
            runWorker.execute();
        } else {
            runWorker.cancel(true);
            runWorker = null;
        }
    }
    
    private void doQuit(ActionEvent ev) {
        frame.dispose();
    }
    
//    private void generateNoWrap() {
//        boolean tmp[][] = new boolean[life.h][life.w];
//        for (var j = 1; j < life.h-1; j++) {
//            for (var i = 1; i < life.w-1; i++) {
//                var cnt = 0;
//                for (var dj = -1 ; dj <= +1 ; dj++) {
//                    for (var di = -1; di <= +1; di += (dj==0 ? 2 : 1)) {
//                        if (life.data[j+dj][i+di]) {
//                            cnt += 1;
//                        }
//                    }
//                }
//                tmp[j][i] = (cnt == 3) || (cnt == 2 && life.data[j][i]);
//            }
//        }
//        life.data(tmp);
//    }
//
//    private void generateWrap() {
//        boolean tmp[][] = new boolean[life.h][life.w];
//        for (var j = 0; j < life.h; j++) {
//            for (var i = 0; i < life.w; i++) {
//                var cnt = 0;
//                for (var dj = -1 ; dj <= +1 ; dj++) {
//                    for (var di = -1; di <= +1; di += (dj==0 ? 2 : 1)) {
//                        if (life.data[(j+dj+life.h)%life.h][(i+di+life.w)%life.w]) {
//                            cnt += 1;
//                        }
//                    }
//                }
//                tmp[j][i] = (cnt == 3) || (cnt == 2 && life.data[j][i]);
//            }
//        }
//        life.data(tmp);
//    }
    
    private boolean[][] generateExpand() {
        boolean tmp[][] = new boolean[life.h+2][life.w+2];
        for (var j = -1; j < life.h+1; j++) {
            for (var i = -1; i < life.w+1; i++) {
                var cnt = 0;
                for (var dj = -1 ; dj <= +1 ; dj++) {
                    var sj = j + dj;
                    if (0 <= sj && sj < life.h) {
                        for (var di = -1; di <= +1; di += (dj==0 ? 2 : 1)) {
                            var si = i + di;
                            if (0 <= si && si < life.w && life.data[sj][si]) {
                                cnt += 1;
                            }
                        }
                    }
                }
                tmp[j+1][i+1] = (cnt == 3) || (cnt == 2 && 0<=j && j<life.h && 0<=i && i<life.w && life.data[j][i]);
            }
        }
        
        var top = false;
        for (var i = 0; i < life.w+2; i++) {
            if (tmp[0][i]) {
                top = true;
                break;
            }
        }
        if (!top) {
            tmp = Arrays.copyOfRange(tmp, 1, tmp.length);
        }
        
        var bottom = false;
        for (var i = 0; i < life.w+2; i++) {
            if (tmp[tmp.length-1][i]) {
                bottom = true;
                break;
            }
        }
        if (!bottom) {
            tmp = Arrays.copyOf(tmp, tmp.length-1);
        }
        
        var left = false;
        for (var j = 0; j < tmp.length; j++) {
            if (tmp[j][0]) {
                left = true;
                break;
            }
        }
        if (!left) {
            for (var j = 0; j < tmp.length; j++) {
                tmp[j] = Arrays.copyOfRange(tmp[j], 1, tmp[j].length);
            }
        }
        
        var right = false;
        for (var j = 0; j < tmp.length; j++) {
            if (tmp[j][tmp[j].length-1]) {
                right = true;
                break;
            }
        }
        if (!right) {
            for (var j = 0; j < tmp.length; j++) {
                tmp[j] = Arrays.copyOf(tmp[j], tmp[j].length-1);
            }
        }

        return tmp;
    }
}
