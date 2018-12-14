package cfh.life;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        new Main();
    }
    
    private Main() {
        SwingUtilities.invokeLater(this::initDB);
    }
    
    private void initDB() {
        var life = new Life(20, 20);
        life.data[2][2] = true;
        life.data[2][3] = true;
        life.data[2][4] = true;
        life.data[3][4] = true;
        
        var panel = new LifePanel(life);
        panel.setPreferredSize(new Dimension(500, 500));
        
        var frame = new JFrame("Life");
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
