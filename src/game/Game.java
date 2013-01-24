package game;

import gfx.Screen;
import gfx.SpreetSheet;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;

/**
 *
 * @author willy
 */
public class Game extends Canvas implements Runnable {

    private static final long serialVersionUID = 1L;
    public static final int WIDTH = 160;
    public static final int HEIGHT = WIDTH / 12 * 9;
    public static final int SCALE = 3;
    public static final String NAME = "Game";
    private JFrame frame;
    public boolean running = false;
    public int tickCount = 0;
    
    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer() ).getData();  
        
    private SpreetSheet spreetSheet = new SpreetSheet("/sprite_sheet.png");
    
    private Screen screen;

    public Game() {
        setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        frame = new JFrame(NAME);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.add(this, BorderLayout.CENTER);
        frame.pack();

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
    
    public void init(){
        screen = new Screen(WIDTH, HEIGHT, new SpreetSheet("/sprite_sheet.png"));
    }
    
    private synchronized void start() {
        running = true;
        new Thread(this).start();
    }

    private synchronized void stop() {
        running = false;
    }

    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D / 60D;

        int frames = 0;
        int ticks = 0;

        long lastTimer = System.currentTimeMillis();
        double delta = 0;
        
        init();

        while (running) {
            //System.out.println("GAME RUNNING!!");
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean shouldRender = true;

            while (delta >= 1) {
                ticks++;
                tick();
                delta -= 1;
                shouldRender = true;
            }
            try {
                Thread.sleep(2);                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            if (shouldRender) {
                frames++;
                render();
            }
            //System.out.println(frames + " - " + ticks);
            if (System.currentTimeMillis() - lastTimer >= 1000) {
                lastTimer += 1000;
                System.out.println( "Frames: " + frames + " - " + "Ticks: " + ticks);
                frames = 0;
                ticks = 0;
            }
        }
    }

    public void tick() {
        tickCount++;
        //screen.xOffest++;
        /*for(int i = 0; i < pixels.length; i++ ){
            pixels[i] = i + tickCount;
        }*/
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if(bs == null){
            createBufferStrategy(3);
            return;
        }
        
        screen.render(pixels, 0, WIDTH);
        
        System.out.println("PING");
        Graphics g = bs.getDrawGraphics();
        //g.setColor(Color.BLACK);
        //g.fillRect(0, 0, getWidth(), getHeight()); 
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        //g.drawRect(0, 0, getWidth(), getHeight());
        
        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        // TODO code application logic here
        new Game().start();
    }
}
