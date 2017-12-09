import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/*
*  @author Anthony Nardomarino
*/

public class Game extends JPanel implements Runnable, KeyListener{
    private boolean leftUp = false, leftDown = false, rightUp = false, rightDown = false,
    ballUp = false, ballDown = false, rightServe = false, leftServe = true,
    win = false, canSpace = true, suspended = false, ballRight = false, ballLeft = false, enter = false,
    gameStart = false;
    private double ballSpeedY = 0.5; //.8
    private double ballSpeedX = 0.5; //.6
    private Thread thread;
    private int leftScore = 0, rightScore = 0;
    private int paddleHeight = 64;
    private int ballSize = 8;
    private int paddleSpeed = 1; //.8?
    private int menuSlide = 0;
    private double leftY = 225, rightY = 225;
    private double ballY = 250, ballX = 500;
    private Color ballColor = Color.GREEN, rectColor = Color.GREEN, backGroundColor = Color.RED, midLineColor = Color.GREEN,
    menuTextColor = Color.GREEN;
    private final Color defBallColor = Color.GREEN, defRectColor = Color.GREEN, defBackGroundColor = Color.RED,
    defMidLineColor = Color.GREEN;

    public Game(){
        super();
        this.addKeyListener(this);
    }

    public void swap(){
        if(ballColor == Color.RED){
            ballColor = Color.GREEN;
        }else{
            ballColor = Color.RED;
        }
        if(rectColor == Color.RED){
            rectColor = Color.GREEN;
        }else{
            rectColor = Color.RED;
        }
        if(backGroundColor == Color.RED){
            backGroundColor = Color.GREEN;
        }else{
            backGroundColor = Color.RED;
        }
        if(midLineColor == Color.RED){
            midLineColor = Color.GREEN;
        }else{
            midLineColor = Color.RED;
        }
    }

    public void addNotify(){
        super.addNotify();
        thread = new Thread(this);
        thread.start();
    }

    public void win(){
        gameStart = false;
        enter = false;
        backGroundColor = defBackGroundColor;
        ballColor = defBallColor;
        
        run();
    }

    public void lose(){

    }

    public void paddle(){

    }

    public void wall(){

    }

    public void run(){
        ballUp = true;
        ballRight = true;
        while(!enter){
            repaint();
            try{
                Thread.sleep(1);
            }catch(Exception e){

            }
            midLineColor = backGroundColor;
            rectColor = backGroundColor;
            if(ballUp){
                ballY -= ballSpeedY;
                if(ballY <= 0){
                    ballDown = true;
                    ballUp = false;
                    if(ballLeft || ballRight){
                        try{
                            wall();
                        }catch(Exception e){}
                    }
                }
            }else if(ballDown){
                ballY += ballSpeedY;
                if(ballY >= 480-ballSize){
                    ballUp = true;
                    ballDown = false;
                    if(ballLeft || ballRight){
                        try{
                            wall();
                        }catch(Exception e){}
                    }
                }
            }
            if(ballRight){
                ballX += ballSpeedX;
                if(ballX > 1000-ballSize){
                    ballRight = false;
                    ballLeft = true;
                }
            }
            if(ballLeft){
                ballX -= ballSpeedX;
                if(ballX < 0){
                    ballLeft = false;
                    ballRight = true;
                }
            }
            if(enter){
                for(int i = 0; i < 600; i++){
                    menuSlide++;
                    try{
                        thread.sleep(4);
                    }catch(Exception e){}
                    repaint();
                }
            }
        }
        menuSlide = 0;
        leftScore = 0;
        rightScore = 0;
        midLineColor = defMidLineColor;
        rectColor = defRectColor;
        gameStart = true;
        ballUp = false;
        ballRight = false;
        ballLeft = false;
        ballDown = false;
        leftDown = false;
        leftUp = false;
        rightDown = false;
        rightUp = false;
        ballX = 500;
        leftY = 250;
        rightY = 250;
        ballY = 250;
        win = false;
        canSpace = true;
        rightServe = false;
        leftServe = true;
        while (enter){
            repaint();
            try{
                Thread.sleep(1);
            }catch(Exception e){

            }
            keyChecks();
            if(win){
                win();
            }
            if(!win){
                if(ballUp){
                    ballY -= ballSpeedY;
                    if(ballY <= 0){
                        ballDown = true;
                        ballUp = false;
                        if(ballLeft || ballRight){
                            try{
                                wall();
                            }catch(Exception e){}
                        }
                    }
                }else if(ballDown){
                    ballY += ballSpeedY;
                    if(ballY >= 480-ballSize){
                        ballUp = true;
                        ballDown = false;
                        if(ballLeft || ballRight){
                            try{
                                wall();
                            }catch(Exception e){}
                        }
                    }
                }
                if(ballRight){
                    ballX += ballSpeedX;
                    if(ballX+ballSize >= 900 && ballX+ballSize <= 910 && ballY >= rightY-ballSize
                    && ballY <= rightY+paddleHeight){
                        // speed changes based on part of the paddle
                        if(rightUp){
                            ballDown = true;
                            ballUp = false;
                        }else if(rightDown){
                            ballUp = true;
                            ballDown = false;
                        }
                        ballLeft = true;
                        ballRight = false;
                        try{
                            paddle();
                        }catch(Exception e){

                        }
                    }
                    if(ballX > 1000-ballSize){
                        leftScore ++;
                        ballRight = false;
                        ballDown = false;
                        ballUp = false;
                        ballX = 500;
                        ballY = 250;
                        if(leftScore == 10){
                            win = true;
                        }
                        swap();
                        rightServe = true;
                        leftServe = false;
                        canSpace = true;
                        try{
                            lose();
                        }catch(Exception e){}
                    }
                }
                if(ballLeft){
                    ballX -= ballSpeedX;
                    if(ballX <= 90 && ballX >= 80 && ballY >= leftY-ballSize && ballY <= leftY + paddleHeight){
                        // speed changes based on part of the paddle
                        if(leftUp){
                            ballDown = true;
                            ballUp = false;
                        }else if(leftDown){
                            ballDown = false;
                            ballUp = true;
                        }
                        ballRight = true;
                        ballLeft = false;
                        try{
                            paddle();
                        }catch(Exception e){}
                    }
                    if(ballX < 0){
                        rightScore ++;
                        ballLeft = false;
                        ballDown = false;
                        ballUp = false;
                        ballX = 500;
                        ballY = 250;
                        swap();
                        if(rightScore == 10){
                            win();
                        }
                        leftServe = true;
                        rightServe = false;
                        canSpace = true;
                        try{
                            lose();
                        }catch(Exception e){}
                    }
                }
            }
        }
    }

    public void paint(Graphics g){
        super.paint(g);

        this.setFocusable(true);
        setBackground(backGroundColor);

        g.setColor(midLineColor);
        for(int i = 0; i < 510; i += 16){
            g.fillRect(500, i+5,1, 8);
        }

        g.fillRect(250, 34, 38, 64);
        g.fillRect(750, 34, 38, 64);
        g.setColor(backGroundColor);
        switch(leftScore){
            case 0: g.fillRect(260, 44, 18, 44);
            break;
            case 1: g.fillRect(250, 34, 28, 64);
            break;
            case 2: g.fillRect(240, 44, 28, 17);
            g.fillRect(260, 71, 28, 17);
            break;
            case 3: g.fillRect(250, 44, 28, 17);
            g.fillRect(250, 71, 28, 17);
            break;
            case 4: g.fillRect(260, 34, 18, 27);
            g.fillRect(250, 71, 28, 27);
            break;
            case 5: g.fillRect(260, 44, 28, 17);
            g.fillRect(250, 71, 18, 17);
            break;
            case 6: g.fillRect(260, 44, 28, 17);
            g.fillRect(260, 71, 18, 17);
            break;
            case 7: g.fillRect(250, 44, 28, 54);
            break;
            case 8: g.fillRect(260, 44, 18, 17);
            g.fillRect(260, 71, 18, 17);
            break;
            case 9: g.fillRect(260, 44, 18, 17);
            g.fillRect(250, 71, 28, 27);
            break;
            case 10: g.fillRect(260, 44, 18, 44);
            g.fillRect(230, 34, 10, 64);
            break;
        }switch(rightScore){
            case 0: g.fillRect(760, 44, 18, 44);
            break;
            case 1: g.fillRect(750, 34, 28, 64);
            break;
            case 2: g.fillRect(750, 44, 28, 17);
            g.fillRect(760, 71, 28, 17);
            break;
            case 3: g.fillRect(750, 44, 28, 17);
            g.fillRect(750, 71, 28, 17);
            break;
            case 4: g.fillRect(760, 34, 18, 27);
            g.fillRect(750, 71, 28, 27);
            break;
            case 5: g.fillRect(760, 44, 28, 17);
            g.fillRect(750, 71, 18, 17);
            break;
            case 6: g.fillRect(760, 44, 28, 17);
            g.fillRect(760, 71, 18, 17);
            break;
            case 7: g.fillRect(750, 44, 28, 54);
            break;
            case 8: g.fillRect(760, 44, 18, 17);
            g.fillRect(760, 71, 18, 17);
            break;
            case 9: g.fillRect(760, 44, 18, 17);
            g.fillRect(750, 71, 28, 27);
            break;
            case 10: g.fillRect(760, 44, 18, 44);
            g.fillRect(730, 34, 10, 64);
            break;
        }

        g.setColor(rectColor);
        g.fillRect(80, (int)leftY, 10, paddleHeight);
        g.fillRect(910, (int)rightY, 10, paddleHeight);
        g.setColor(ballColor);
        g.fillRect((int)(ballX), (int)(ballY), ballSize, ballSize);
        if(!gameStart){
            int pressEnterX = 210;
            int pressEnterY = 336-menuSlide;
            g.fillRect(pressEnterX, pressEnterY, 10, 60);           //P
            g.fillRect(pressEnterX + 10, pressEnterY, 20, 10);
            g.fillRect(pressEnterX + 30, pressEnterY + 10, 10, 20);
            g.fillRect(pressEnterX + 10, pressEnterY + 30, 20, 10);
            pressEnterX += 50;
            
            g.fillRect(pressEnterX, pressEnterY, 10, 60);           //R
            g.fillRect(pressEnterX + 10, pressEnterY, 20, 10);
            g.fillRect(pressEnterX + 30, pressEnterY + 10, 10, 20);
            g.fillRect(pressEnterX + 10, pressEnterY + 30, 20, 10);
            g.fillRect(pressEnterX + 20, pressEnterY + 40, 10, 10);
            g.fillRect(pressEnterX + 30, pressEnterY + 50, 10, 10);
            
            pressEnterX += 50;
            g.fillRect(pressEnterX, pressEnterY, 10, 60);           //E
            g.fillRect(pressEnterX + 10, pressEnterY, 30, 10);
            g.fillRect(pressEnterX + 10, pressEnterY + 25, 30, 10);
            g.fillRect(pressEnterX + 10, pressEnterY + 50, 30, 10);
            
            pressEnterX += 50;
            g.fillRect(pressEnterX, pressEnterY, 40, 10);           //S
            g.fillRect(pressEnterX, pressEnterY, 10, 35);
            g.fillRect(pressEnterX, pressEnterY + 25, 40, 10);
            g.fillRect(pressEnterX + 30, pressEnterY + 25, 10, 30);
            g.fillRect(pressEnterX, pressEnterY + 50, 40, 10);
            
            pressEnterX += 50;
            g.fillRect(pressEnterX, pressEnterY, 40, 10);           //S
            g.fillRect(pressEnterX, pressEnterY, 10, 35);
            g.fillRect(pressEnterX, pressEnterY + 25, 40, 10);
            g.fillRect(pressEnterX + 30, pressEnterY + 25, 10, 30);
            g.fillRect(pressEnterX, pressEnterY + 50, 40, 10);
            
            pressEnterX += 100;
            g.fillRect(pressEnterX, pressEnterY, 10, 60);           //E
            g.fillRect(pressEnterX + 10, pressEnterY, 30, 10);
            g.fillRect(pressEnterX + 10, pressEnterY + 25, 30, 10);
            g.fillRect(pressEnterX + 10, pressEnterY + 50, 30, 10);
            
            pressEnterX += 50;
            g.fillRect(pressEnterX, pressEnterY, 10, 60);           //N
            g.fillRect(pressEnterX + 10, pressEnterY, 10, 20);
            g.fillRect(pressEnterX + 20, pressEnterY + 20, 10, 10);
            g.fillRect(pressEnterX + 30, pressEnterY + 30, 10, 20);
            g.fillRect(pressEnterX + 40, pressEnterY, 10, 60);
            
            pressEnterX += 60;
            g.fillRect(pressEnterX, pressEnterY, 40, 10);           //T
            g.fillRect(pressEnterX + 15, pressEnterY, 10, 60);
            
            pressEnterX += 50;
            g.fillRect(pressEnterX, pressEnterY, 10, 60);           //E
            g.fillRect(pressEnterX + 10, pressEnterY, 30, 10);
            g.fillRect(pressEnterX + 10, pressEnterY + 25, 30, 10);
            g.fillRect(pressEnterX + 10, pressEnterY + 50, 30, 10);
            
            pressEnterX += 50;
            g.fillRect(pressEnterX, pressEnterY, 10, 60);           //R
            g.fillRect(pressEnterX + 10, pressEnterY, 20, 10);
            g.fillRect(pressEnterX + 30, pressEnterY + 10, 10, 20);
            g.fillRect(pressEnterX + 10, pressEnterY + 30, 20, 10);
            g.fillRect(pressEnterX + 20, pressEnterY + 40, 10, 10);
            g.fillRect(pressEnterX + 30, pressEnterY + 50, 10, 10);
            
            int holidayX = 150;
            int holidayY = 50+menuSlide;
            
            g.fillRect(holidayX, holidayY, 10, 80);         //H
            g.fillRect(holidayX, holidayY + 35, 40, 10);
            g.fillRect(holidayX + 40, holidayY, 10, 80);
            
            holidayX += 60;
            g.fillRect(holidayX, holidayY, 50, 10);         //O
            g.fillRect(holidayX, holidayY, 10, 80);
            g.fillRect(holidayX, holidayY + 70, 50, 10);
            g.fillRect(holidayX + 40, holidayY, 10, 80);
            
            holidayX += 60;
            g.fillRect(holidayX, holidayY, 10, 80);         //L
            g.fillRect(holidayX, holidayY + 70, 50, 10);
            
            holidayX += 60;
            g.fillRect(holidayX, holidayY, 10, 80);         //I
            
            holidayX += 20;
            g.fillRect(holidayX, holidayY, 10, 80);         //D
            g.fillRect(holidayX, holidayY, 30, 10);
            g.fillRect(holidayX, holidayY + 70, 30, 10);
            g.fillRect(holidayX + 30, holidayY + 10, 10, 10);
            g.fillRect(holidayX + 30, holidayY + 60, 10, 10);
            g.fillRect(holidayX + 40, holidayY + 20, 10, 40);
            
            holidayX += 60;
            g.fillRect(holidayX, holidayY + 10, 10, 70);    //A
            g.fillRect(holidayX + 10, holidayY, 30, 10);
            g.fillRect(holidayX + 40, holidayY + 10, 10, 70);
            g.fillRect(holidayX, holidayY + 35, 50, 10);
            
            holidayX += 60;
            g.fillRect(holidayX, holidayY, 10, 20);         //Y
            g.fillRect(holidayX + 10, holidayY + 20, 10, 20);
            g.fillRect(holidayX + 20, holidayY + 40, 10, 40);
            g.fillRect(holidayX + 30, holidayY + 20, 10, 20);
            g.fillRect(holidayX + 40, holidayY, 10, 20);
            
            holidayX += 120;
            g.fillRect(holidayX, holidayY, 10, 80);         //P
            g.fillRect(holidayX, holidayY, 40, 10);
            g.fillRect(holidayX + 40, holidayY + 10, 10, 30);
            g.fillRect(holidayX, holidayY + 40, 40, 10);
            
            holidayX += 60;
            g.fillRect(holidayX, holidayY, 50, 10);         //O
            g.fillRect(holidayX, holidayY, 10, 80);
            g.fillRect(holidayX, holidayY + 70, 50, 10);
            g.fillRect(holidayX + 40, holidayY, 10, 80);
            
            holidayX += 60;
            g.fillRect(holidayX, holidayY, 10, 80);         //N
            g.fillRect(holidayX + 10, holidayY + 10, 10, 30);
            g.fillRect(holidayX + 20, holidayY + 30, 10, 20);
            g.fillRect(holidayX + 30, holidayY + 50, 10, 30);
            g.fillRect(holidayX + 40, holidayY, 10, 80);
            
            holidayX += 60;
            g.fillRect(holidayX, holidayY + 10, 10, 60);
            g.fillRect(holidayX + 10, holidayY, 30, 10);
            g.fillRect(holidayX + 40, holidayY + 10, 10, 10);
            g.fillRect(holidayX + 10, holidayY + 70, 30, 10);
            g.fillRect(holidayX + 40, holidayY + 40, 10, 30);
            g.fillRect(holidayX + 20, holidayY + 40, 20, 10);
        }
    }

    public void keyTyped(KeyEvent e){

    }

    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();

        if(key == KeyEvent.VK_UP){
            rightUp = true;
        }
        if(key == KeyEvent.VK_DOWN){
            rightDown = true;
        }
        if(key == KeyEvent.VK_W){
            leftUp = true;
        }
        if(key == KeyEvent.VK_S){
            leftDown = true;
        }
        if(key == KeyEvent.VK_ESCAPE){
            if(!suspended){
                thread.suspend();
                suspended = true;
            }else{
                thread.resume();
                suspended = false;
            }
        }
        if(key == KeyEvent.VK_ENTER){
            enter = true;
        }
        if(key == KeyEvent.VK_SPACE && gameStart){
            if(canSpace){
                if(rightServe){
                    ballRight = true;
                    ballUp = true;
                }
                if(leftServe){
                    ballLeft = true;
                    ballUp = true;
                }
                canSpace = false;
            }
        }
    }

    public void keyChecks(){
        if(rightUp && rightY > 0){
            rightY -= paddleSpeed;
            if(rightY < 0){
                rightY = 0;
            }
        }
        if(leftUp && leftY > 0){
            leftY -= paddleSpeed;
            if(leftY < 0){
                leftY = 0;
            }
        }
        if(leftDown && leftY < 472-paddleHeight){
            leftY += paddleSpeed;
            if(leftY > 472-paddleHeight){
                leftY = 472-paddleHeight;
            }
        }
        if(rightDown && rightY < 472-paddleHeight){
            rightY += paddleSpeed;
            if(rightY > 472-paddleHeight){
                rightY = 472-paddleHeight;
            }
        }
    }

    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();

        if(key == KeyEvent.VK_UP){
            rightUp = false;
        }
        if(key == KeyEvent.VK_DOWN){
            rightDown = false;
        }
        if(key == KeyEvent.VK_W){
            leftUp = false;
        }
        if(key == KeyEvent.VK_S){
            leftDown = false;
        }
    }
}
