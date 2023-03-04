
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {
//        Calendar cal = Calendar.getInstance();

    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = 555;
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    int num1 = 0;
    int num2 = 0;
    static int num11 = 0, num12 = 0;

    public static int getBALL_DIAMETER() {
        return BALL_DIAMETER;
    }

    public static void setBALL_DIAMETER(int BALL_DIAMETER) {
        GamePanel.BALL_DIAMETER = BALL_DIAMETER;
    }
    static int BALL_DIAMETER = 0;
    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = 100;
    private JFrame Frame;
    Thread gameThread;
    Image image, img;
    Graphics graphics;
    Random random;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball;
    Score score;
    File sound;

    GamePanel() {
        newPaddles();
        newBall();
        Frame = new JFrame();
        score = new Score(GAME_WIDTH, GAME_HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);
        Frame.add(this);
        Frame.pack();
        Frame.setVisible(true);
        Frame.setLocationRelativeTo(null);
        gameThread = new Thread(this);
        gameThread.start();
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void newBall() {
        random = new Random();
        ball = new Ball((GAME_WIDTH / 2) - (BALL_DIAMETER / 2), random.nextInt(GAME_HEIGHT - BALL_DIAMETER), BALL_DIAMETER, BALL_DIAMETER);
    }

    public void newPaddles() {
        paddle1 = new Paddle(0, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 1);
        paddle2 = new Paddle(GAME_WIDTH - PADDLE_WIDTH, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 2);
    }

    public void paint(Graphics g) {
        if(getWidth()==1016 && getHeight()==571){
//            Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH-16, GAME_HEIGHT-16);
                image = createImage(1000, 555);
//                System.out.println("www"+getWidth());
//                System.out.println("HH"+getHeight());
                graphics = image.getGraphics();
                draw(graphics);
        //        System.out.println(graphics);
                g.drawImage(image, 0, 0, GAME_WIDTH+16 , GAME_HEIGHT+16, this);
                System.out.println("true");
 
            
        }
        else{
            image = createImage(1000, 555);
//            System.out.println("www"+getWidth());
//            System.out.println("HH"+getHeight());
            graphics = image.getGraphics();
            draw(graphics);
    //        System.out.println(graphics);
            g.drawImage(image, 0, 0, GAME_WIDTH , GAME_HEIGHT , this);
        }
    }

    public void draw(Graphics g) {
        try {
            BufferedImage img = ImageIO.read(new File("image/field.png"));
            g.drawImage(img, 0, 0, GAME_WIDTH + 25, GAME_HEIGHT + 20, this);
            paddle1.draw(g);
            paddle2.draw(g);
            ball.draw(g);
            score.draw(g);
        } catch (IOException ex) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        }

//Toolkit.getDefaultToolkit().sync(); // I forgot to add this line of code in the video, it helps with the animation
    }

    public void move() {
        paddle1.move();
        paddle2.move();
        ball.move();
    }

    public void checkCollision() {

        //bounce ball off top & bottom window edges
        if (ball.y <= 0) {
            ball.setYDirection(-ball.yVelocity);
        }
        if (ball.y >= GAME_HEIGHT - BALL_DIAMETER) {
            ball.setYDirection(-ball.yVelocity);
        }
        //bounce ball off paddles
        if (ball.intersects(paddle1)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++; //optional for more difficulty
            if (ball.yVelocity > 0) {
                ball.yVelocity++; //optional for more difficulty
            } else {
                ball.yVelocity--;
            }
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        if (ball.intersects(paddle2)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++; //optional for more difficulty
            if (ball.yVelocity > 0) {
                ball.yVelocity++; //optional for more difficulty
            } else {
                ball.yVelocity--;
            }
            ball.setXDirection(-ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        //stops paddles at window edges
        if (paddle1.y <= 0) {
            paddle1.y = 0;
        }
        if (paddle1.y >= (GAME_HEIGHT - PADDLE_HEIGHT)) {
            paddle1.y = GAME_HEIGHT - PADDLE_HEIGHT;
        }
        if (paddle2.y <= 0) {
            paddle2.y = 0;
        }
        if (paddle2.y >= (GAME_HEIGHT - PADDLE_HEIGHT)) {
            paddle2.y = GAME_HEIGHT - PADDLE_HEIGHT;
        }
        //give a player 1 point and creates new paddles & ball
        if (ball.x <= 0) {
            //soundmain(new File("sound/getpoint.wav"));
            soundmain(new File("sound/getpoint.wav"));
            score.player2++;
            num2 = score.player2;
            newPaddles();
            newBall();
            System.out.println("Player 2: " + score.player2);
        }
        if (ball.x >= GAME_WIDTH - BALL_DIAMETER) {
            //soundmain(new File("sound/getpoint.wav"));
            soundmain(new File("sound/getpoint.wav"));
            score.player1++;
            num1 = score.player1;
            newPaddles();
            newBall();
            System.out.println("Player 1: " + score.player1);
        }
    }

    public void run() {
        //game loop
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        //3นาที
       long finish = System.currentTimeMillis() + (60000 * 3);
        //1นาที
        //        long finish = System.currentTimeMillis() + 60000; 
        //10วิ
        //long finish = System.currentTimeMillis() + 10000;
        while (true) {
            if (System.currentTimeMillis() <= finish) {
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;
                if (delta >= 1) {
                    move();
                    checkCollision();
                    repaint();
                    delta--;
                }
//                        System.out.println("Start time is "+startTime);
//                        currentTime = cal.getTimeInMillis();
//                        System.out.println("Current time is "+currentTime);
//                        System.out.println("Timer will has stopped");
//                        System.out.println(System.currentTimeMillis());
//                        System.out.println("-----"+finish);
            } else {
                // System.out.println("1");
                num11 = num1;
                num12 = num2;
                soundmain(new File("sound/gameover.wav"));
                new TimeoutNew().setVisible(true);
                this.setVisible(false);
                Frame.setVisible(false);
                break;
            }

//                        break;
        }

    }

    public class AL extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            paddle1.keyPressed(e);
            paddle2.keyPressed(e);
        }

        public void keyReleased(KeyEvent e) {
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);
        }
    }

    public void soundmain(File sound) {
        File lol = sound;
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(lol));
            //clip.loop(Clip.LOOP_CONTINUOUSLY);
            FloatControl gainControl
                    = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-25.0f); // Reduce volume by 10 decibels.
            clip.start();
            System.out.println("Make sound!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
