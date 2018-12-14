package cfh.life;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

import javax.swing.JComponent;

import cfh.life.Life.Listener;

@SuppressWarnings("serial")
public class LifePanel extends JComponent {
    
    private final Life life;
    private int step;
    private boolean grid;
    private int offx;
    private int offy;
    private int maxx;
    private int maxy;
    
    LifePanel(Life life) {
        this.life = Objects.requireNonNull(life);
        life.addListener(new Listener() {
            @Override
            public void resized() {
                doComponentResized();
            }
            @Override
            public void changed() {
                doComponentResized();
                repaint();
            }
        });
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent ev) {
                doComponentResized();
            }
        });
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent ev) {
                doMouseClicked(ev);
            }
        });
    }
    
    @Override
    public void doLayout() {
        super.doLayout();
        doComponentResized();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        var gg = (Graphics2D) g;
        try {
        
            gg.translate(offx, offy);

            if (grid) {
                gg.setColor(Color.GRAY);
                drawGrid(gg);
            } else {
                gg.setColor(Color.BLUE);
                gg.drawRect(-1, -1, maxx+2, maxy+2);
            }
            
            gg.setColor(Color.BLACK);
            drawData(gg);
            
        } finally {
            gg.dispose();
        }
    }

    private void drawGrid(Graphics2D gg) {
        for (var y = 0; y <= maxy; y += step) {
            gg.drawLine(0, y, maxx, y);
        }
        for (var x = 0; x <= maxx; x += step) {
            gg.drawLine(x, 0, x, maxy);
        }
    }

    private void drawData(Graphics2D gg) {
        var delta = grid ? 1 : 0;
        for (var j = 0; j < life.h; j += 1) {
            for (var i = 0; i < life.w; i += 1) {
                if (life.data[j][i]) {
                    var x = i*step;
                    var y = j*step;
                    gg.fillRect(x+delta, y+delta, step-delta, step-delta);
                }
            }
        }
    }
    
    private void doComponentResized() {
        var width = getWidth();
        var height = getHeight();

        var stepx = (width-3) / life.w;
        var stepy = (height-3) / life.h;
        step = Math.min(stepx, stepy);
        grid = step > 20;

        maxx = step * life.w;
        maxy = step * life.h;

        offx = (width - maxx) / 2 + 1;
        offy = (height - maxy) / 2 + 1;
    }
    
    private void doMouseClicked(MouseEvent ev) {
        if (ev.getButton() == ev.BUTTON1) {
            if (ev.getX() >= offx && ev.getY() >= offy) {
                var x = (ev.getX() - offx) / step;
                var y = (ev.getY() - offy) / step;
                if (0 <= x && x < life.w && 0 <= y && y < life.h) {
                    life.data[y][x] ^= true;
                }
                repaint();
            }
        }
    }
}
