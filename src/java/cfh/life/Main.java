package cfh.life;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.SwingUtilities;


public class Main {
    
    public static void main(String[] args) {
        new Main();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    private Life life;
    private JFrame frame;
    private JComboBox<Generator> generatorCombo;
    
    private final List<Generator> generators = new ArrayList<>();
    
    private RunWorker runWorker = null;
    
    
    private Main() {
        generators.add(new Plus());
        generators.add(new Asterisk());
        
        ServiceLoader<Generator> loader = ServiceLoader.load(Generator.class);
        for(var generator : loader) {
            generators.add(generator);
        }
        
        SwingUtilities.invokeLater(this::initDB);
    }
    
    private void initDB() {
        life = new Life(20, 20);
        
        var panel = new LifePanel(life);
        panel.setPreferredSize(new Dimension(500, 500));
        
        generatorCombo = new JComboBox<Generator>(generators.toArray(new Generator[0]));
        generatorCombo.setRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 8916286147138240731L;

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean selected, boolean focus) {
                if (value instanceof Generator) {
                    value = ((Generator) value).name();
                }
                return super.getListCellRendererComponent(list, value, index, selected, focus);
            }
        });
        
        var step = new JButton("Step");
        step.addActionListener(this::doStep);
        
        var run = new JButton("Run");
        run.addActionListener(this::doRun);
        
        var slow = new JButton("-");
        slow.addActionListener(this::doSlow);
        
        var fast = new JButton("+");
        fast.addActionListener(this::doFast);
        
        var quit = new JButton("Quit");
        quit.addActionListener(this::doQuit);
        
        var buttons = Box.createHorizontalBox();
        buttons.add(Box.createHorizontalGlue());
        buttons.add(generatorCombo);
        buttons.add(Box.createHorizontalGlue());
        buttons.add(step);
        buttons.add(run);
        buttons.add(Box.createHorizontalStrut(10));
        buttons.add(slow);
        buttons.add(fast);
        buttons.add(Box.createHorizontalGlue());
        buttons.add(quit);
        buttons.add(Box.createHorizontalGlue());
        
        frame = new JFrame("Life");
        frame.setDefaultCloseOperation(frame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent ev) {
                doQuit(null);
            }
        });
        frame.add(buttons, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private void doStep(ActionEvent ev) {
        if (runWorker != null) {
            runWorker.cancel(true);
            runWorker = null;
        } else {
            Generator generator = (Generator) generatorCombo.getSelectedItem();
            life.data(generator.apply(life.data));
        }
    }
    
    private void doRun(ActionEvent ev) {
        if (runWorker == null) {
            generatorCombo.setEnabled(false);
            runWorker = new RunWorker(life, (Generator) generatorCombo.getSelectedItem(), () -> generatorCombo.setEnabled(true)); 
            runWorker.execute();
        } else {
            runWorker.cancel(true);
            runWorker = null;
        }
    }
    
    private void doSlow(ActionEvent ev) {
        if (runWorker != null) {
            runWorker.slower();
        }
    }
    
    private void doFast(ActionEvent ev) {
        if (runWorker != null) {
            runWorker.faster();
        }
    }
    
    private void doQuit(ActionEvent ev) {
        if (runWorker != null) {
            runWorker.cancel(true);
            runWorker = null;
        }
        frame.dispose();
    }
}
