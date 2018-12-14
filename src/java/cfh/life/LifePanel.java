package cfh.life;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Objects;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class LifePanel extends JComponent {
    
    private final Life life;
    
    LifePanel(Life life) {
        this.life = Objects.requireNonNull(life);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        var gg = (Graphics2D) g;
        try {
        
            var width = getWidth();
            var height = getHeight();

            var stepx = (width-1) / life.w;
            var stepy = (height-1) / life.h;
            var step = Math.min(stepx, stepy);

            var maxx = step * life.w;
            var maxy = step * life.h;
            var offx = (width - maxx) / 2;
            var offy = (height - maxy) / 2;
            var delta = 0;
            gg.translate(offx, offy);

            if (step > 20) {
                delta = 1;
                gg.setColor(Color.GRAY);
                for (var y = 0; y <= maxy; y += step) {
                    gg.drawLine(0, y, maxx, y);
                }
                for (var x = 0; x <= maxx; x += step) {
                    gg.drawLine(x, 0, x, maxy);
                }
            }
            
            for (var j=0; j<life.h; j+=1) {
                gg.setColor(Color.BLACK);
                for (var i=0; i<life.w; i+=1) {
                    if (life.data[j][i]) {
                        var x = i*step;
                        var y = j*step;
                        gg.fillRect(x+delta, y+delta, step-delta, step-delta);
                    }
                }
            }
            
        } finally {
            gg.dispose();
        }
    }
}
