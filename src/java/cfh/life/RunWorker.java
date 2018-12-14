package cfh.life;

import java.util.List;
import javax.swing.SwingWorker;

public class RunWorker extends SwingWorker<Void, boolean[][]> {

    private final Life life;
    private final Generator generator;
    private final Runnable done;
    
    volatile int delay = 200;
    
    RunWorker(Life life, Generator generator, Runnable done) {
        this.life = life;
        this.generator = generator;
        this.done = done;
    }
    
    void slower() {
        delay *= 1.5;
        if (delay > 2000) {
            delay = 2000;
        }
    }
    
    void faster() {
        delay /= 1.5;
        if (delay < 2) {
            delay = 2;
        }
    }
    
    @Override
    protected Void doInBackground() throws Exception {
        try {
            while (true) {
                publish(generator.apply(life.data));
                Thread.sleep(delay);
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
    
    @Override
    protected void done() {
        done.run();
    }
}
