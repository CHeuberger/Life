package cfh.life;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class Main {

    private Life life;
    private JFrame frame;

    public static void main(String[] args) {
        new Main();
    }
    
    private Main() {
        SwingUtilities.invokeLater(this::initDB);
    }
    
    private void initDB() {
        life = new Life(20, 20);
        
        var panel = new LifePanel(life);
        panel.setPreferredSize(new Dimension(500, 500));
        
        var step = new JButton("Step");
        step.addActionListener(this::doStep);
        
        var quit = new JButton("Quit");
        quit.addActionListener(this::doQuit);
        
        var buttons = Box.createHorizontalBox();
        buttons.add(Box.createHorizontalGlue());
        buttons.add(step);
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
    
    private void doQuit(ActionEvent ev) {
        frame.dispose();
    }
    
    private void doStep(ActionEvent ev) {
        boolean tmp[][] = new boolean[life.h][life.w];
        for (var j = 1; j < life.h-1; j += 1) {
            for (var i = 1; i < life.w-1; i += 1) {
                var cnt = 0;
                for (var dj = -1 ; dj <= +1 ; dj += 1) {
                    for (var di = -1; di <= +1; di += (dj==0 ? 2 : 1)) {
                        if (life.data[j+dj][i+di]) {
                            cnt += 1;
                        }
                    }
                }
                tmp[j][i] = (cnt == 3) || (cnt == 2 && life.data[j][i]);
            }
        }
        life.data(tmp);
    }
}
