
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.imageio.*;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.*;


public class Board  extends JPanel implements Runnable, MouseListener
{
boolean ingame = false;
private Dimension d;
int BOARD_WIDTH=500;
int BOARD_HEIGHT=500;
int x = 0;
BufferedImage img;
String message = "Click Board to Start";
int score = 0;
String myScore = "Score: " + score;
int lives = 3;
String lifeCount = "Lives: " + lives;
private Thread animator;
Player player =  new Player("spaceinvadersgraphics/player.png", 250, 430); //creates object player
ArrayList<Shots> playerShots = new ArrayList<Shots>(); //creates arrays for shots, alien and player
ArrayList<Shots> alienShots = new ArrayList<Shots>();
boolean[][] aliens = new boolean[10][3];
Aliens alien =  new Aliens("spaceinvadersgraphics/alien.png", 0, 0); //creates object alien
int alienCount = 30;
BufferedImage bulletImg;
// System.exit(1);
BufferedImage alienImg;
// System.exit(1);
BufferedImage alienbulletImg;
// System.exit(1);
double xmoveInc = .5; //declares increment of side-to-side movement for aliens
int ymoveInc = 0; //declares increment of top-down movement for aliens
int xdirec = 1;
BufferedImage shipImg;
boolean[] ships = new boolean[3]; //creates array of ship barriers

public Board()
{
      addKeyListener(new TAdapter());
      addMouseListener(this);
      setFocusable(true);
      d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
      setBackground(Color.black);
      
         try {
            bulletImg = ImageIO.read(this.getClass().getResource("spaceinvadersgraphics/pshot.png"));
        } catch (IOException e) {
             System.out.println("Image could not be read");
            // System.exit(1);
        }
        
         try {
            alienImg = ImageIO.read(this.getClass().getResource("spaceinvadersgraphics/alien.png"));
         } catch (IOException e) {
             System.out.println("Image could not be read");
             // System.exit(1);
         }
        
         try {
            alienbulletImg = ImageIO.read(this.getClass().getResource("spaceinvadersgraphics/ashot.png"));
        } catch (IOException e) {
             System.out.println("Image could not be read");
             // System.exit(1);
        }
        
         try {
            shipImg = ImageIO.read(this.getClass().getResource("spaceinvadersgraphics/ship.png"));
        } catch (IOException e) {
             System.out.println("Image could not be read");
             // System.exit(1);
        }
        
        
        if (animator == null || !ingame) {
            animator = new Thread(this);
            animator.start();
        }
                

    setDoubleBuffered(true);
}

    public void paint(Graphics g) {
        {
                super.paint(g);
                
                g.setColor(Color.black);
                g.fillRect(0, 0, d.width, d.height);
                //g.fillOval(x,y,r,r);
                
                        Font small = new Font("Helvetica", Font.BOLD, 14);
                        FontMetrics metr = this.getFontMetrics(small);
                        g.setColor(Color.white);
                       g.setFont(small);
                        g.drawString(message, 10, d.height-60);
        
            if (ingame) {
                        g.drawString(lifeCount, 10, 20); //displays life count and score live
                        g.drawString(myScore, d.width - 100, 20);
                        try { 
                            img = ImageIO.read(this.getClass().getResource(player.img)); //draws player
                        }
                        catch (IOException e) {
                                         System.out.println("Image could not be read");
                                    // System.exit(1);
                        }
                        g.drawImage(img,player.xval,player.yval,30,30 ,null); //prevents player from moving offscreen
                        if(player.xval < 0) {
                            player.xval = 0;
                        }
                        if(player.xval > 450) {
                            player.xval = 450;
                        }
                        message = " ";
                
                        for(alien.yval = 0; alien.yval < 3; alien.yval++) {
                                for(alien.xval = 0; alien.xval < 10; alien.xval++){
                                    //for every alien in the 10 x 3 grid
                                    if(aliens[alien.xval][alien.yval] == false){
                                            boolean destroyed = false;
                                            for (int i = 0; i < playerShots.size(); i++) {
                                                if(playerShots.get(i).xval > 15 + alien.xval*30 + xmoveInc && playerShots.get(i).xval < 35 + alien.xval*30 + xmoveInc && playerShots.get(i).yval > 30 + alien.yval*20 + ymoveInc && playerShots.get(i).yval < 50 + alien.yval*20 + ymoveInc) {
                                                playerShots.remove(i);
                                                aliens[alien.xval][alien.yval] = true;
                                                alienCount--;
                                                score += 50;
                                                myScore = "Score: " + score;
                                                //when alien is shot, its place in array is returned false, the alien is removed, and the score is added to
                                                destroyed = true;
                                                }
                                            }
                                            if(destroyed == false) { //for all "alive" aliens to shoot at player
                                               g.drawImage(alienImg,15 + alien.xval*30 + (int)xmoveInc,30 + alien.yval*40 + ymoveInc,20,20, null);
                                               int aliensShoot = (int)Math.random()*10 + 1;
                                               if(aliensShoot < 2 && alienShots.size() < 1) {
                                                   if(15 + alien.xval*30 + (int)xmoveInc > player.xval - 5 && 15 + alien.xval*30 + (int)xmoveInc < player.xval + 35) {
                                                    alienShots.add(new Shots(15 + alien.xval*30 + (int)xmoveInc + 12, 30 + alien.yval*40 + ymoveInc + 5));
                                                    
                                                   } 
                                                   
                                                }
                            
                                        }  
                                        
                                    }
                                    
                                }
                                    
                        }
                     
                        for(int i = 0; i < ships.length; i++) {
                                if(ships[i] == false) {
                                    boolean destroyed = false; //BONUS: 3 ships as barriers protecting the player
                                    
                                    for (int j = 0; j < alienShots.size(); j++) {
                                                if(alienShots.get(j).xval > 100 + i*100 && alienShots.get(j).xval < 100 + i*100 + 80 && alienShots.get(j).yval > 400  && alienShots.get(j).yval < 430 + alien.yval*20 + ymoveInc) {
                                                alienShots.remove(j);
                                                ships[i] = true;
                                                //when a barrier is shot once by the aliens, the place is returned false, the barrier is removed
                                                destroyed = true;
                                               
                                               }
                                            }
                                       
                                     if(destroyed == false) {
                                        g.drawImage(shipImg, 100 + i*100, 400, 80, 30, null);
                                      }
                              }
                            }
                        
                        if(15 + alien.xval*50 + xmoveInc <= 500) { //movement of aliens in x, go down by 10px when hits a side
                           xdirec = 1;
                           ymoveInc += 10;
                        } 
                        if(15 + alien.xval*50 + xmoveInc >= 720) {
                           xdirec = -1;  
                           ymoveInc += 10;
                         }
                         xmoveInc += xdirec;                
                        for (int i = 0; i < alienShots.size(); i++) {
                            if (alienShots.get(i).yval < 500) {
                                g.drawImage(alienbulletImg,alienShots.get(i).xval,alienShots.get(i).yval,5,10 ,null);
                                alienShots.get(i).yval += 1; //alien shoots at player, removed once they leave the screen
                                
                                if(alienShots.get(i).xval > player.xval - 5 && alienShots.get(i).xval < player.xval + 35 && alienShots.get(i).yval > player.yval && alienShots.get(i).yval < player.yval + 30) {
                                    lives -= 1;
                                    lifeCount = "Lives: " + lives;
                                    alienShots.remove(i); //remove life when alien shoots player successfully
                                }
                            } else {
                                alienShots.remove(i);
                            }
                        }
                    
                        for (int i = 0; i < playerShots.size(); i++) {
                            if (playerShots.get(i).yval > 0) {
                                g.drawImage(bulletImg,playerShots.get(i).xval,playerShots.get(i).yval,5,10 ,null);
                                playerShots.get(i).yval -= 7; //player shoots bullets, removed once they leave the screen
                                
                            } else {
                                playerShots.remove(i);
                            }
                        }
                                                
                        if(score <= 0){
                           score = 0;
                           //prevents score from being negative
                        }
                
                    
                        Toolkit.getDefaultToolkit().sync();
                        g.dispose();
                }
            }
        }
    
       private class TAdapter extends KeyAdapter {
                
                public void keyReleased(KeyEvent e) {
                     int key = e.getKeyCode();
                     
                }
                
                public void keyPressed(KeyEvent e) {
                    //System.out.println( e.getKeyCode());
                       // message = "Key Pressed: " + e.getKeyCode();
                        int key = e.getKeyCode();
                            if(key==39){
                              player.xval += 7; //moves player right to left with arrow keys
                            }
                            if(key==37){
                              player.xval -= 7;
                            }
                            if(key==32){
                               playerShots.add(new Shots(player.xval + 12, player.yval + 5)); //adds shot to playershots array on space bar
                               
                            }
                           
                
                }
                
       }
            
            
            
            
        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            //message = "mousePressed: x: " + x + " y: " + y;
            if(e.getButton() == MouseEvent.BUTTON1) {
                ingame = true;
            }
            if(ingame == false && e.getButton() == MouseEvent.BUTTON1){
                ingame = true;
                myScore = "Score: " + score;
                lifeCount = "Lives: " + lives;
                message = " ";
                //restart game with a click
            }
        }
        
        public void mouseReleased(MouseEvent e) {
        
        }
        
        public void mouseEntered(MouseEvent e) {
        
        }
        
        public void mouseExited(MouseEvent e) {
        
        }
        
        public void mouseClicked(MouseEvent e) {
        
        }
        
        public void run() {
            
            long beforeTime, timeDiff, sleep;
            
            beforeTime = System.currentTimeMillis();
             int animationDelay = 5;
             long time = 
                        System.currentTimeMillis();
                while (true) {//infinite loop
                 // spriteManager.update();
                  repaint();
                  if(lives == 0) {
                        ingame = false;
                        message = ("You were invaded with a score of " + score + "! Click to play again.");
                        //ADDITIONAL FEATURE: Displays score at Game Over
                        
                        lives = 3;
                        score = 0;
                        alienCount = 30;
                        myScore = "Score: " + score;
                        ymoveInc = 0;
                        //board is reset for next game start
                        for (alien.yval = 0; alien.yval < 3; alien.yval++) {//resets alien grid
                              for (alien.xval = 0; alien.xval < 10; alien.xval++) {
                                 aliens[alien.xval][alien.yval] = false;
                               }
                         }
                         
                         for(int i = 0; i < ships.length; i++) { //resets barriers
                                  ships[i] = false;
                                }
                    }
                   if(30 + alien.yval*40 + ymoveInc > 500){
                       ingame = false;
                       message = ("You were invaded with a score of " + score + "! Click to play again.");
                        //ADDITIONAL FEATURE: Displays score at Game Over
                        
                        lives = 3;
                        score = 0;
                        alienCount = 30;
                        myScore = "Score: " + score;
                        ymoveInc = 0;
                        //board is reset for next game start
                        for (alien.yval = 0; alien.yval < 3; alien.yval++) {//resets alien grid
                              for (alien.xval = 0; alien.xval < 10; alien.xval++) {
                                 aliens[alien.xval][alien.yval] = false;
                               }
                         }
                         for(int i = 0; i < ships.length; i++) {//resets barriers
                                  ships[i] = false;
                                }
                    }
                  if(alienCount == 0) {
                    ingame = false;
                    message = ("You defeated the aliens with a score of " + score + "! Click to play again");
                    //ADDITIONAL FEATURE: Displays score at Game Win
                    
                    lives = 3;
                    score = 0;
                    alienCount = 30;
                    myScore = "Score: " + score;
                    ymoveInc = 0;
                    //board is reset for next game start
                    for (alien.yval = 0; alien.yval < 3; alien.yval++) {//resets alien grid
                          for (alien.xval = 0; alien.xval < 10; alien.xval++) {
                             aliens[alien.xval][alien.yval] = false;
                           }
                     }
                     for(int i = 0; i < ships.length; i++) { //resets barriers
                                  ships[i] = false;
                                }
                    }
                  try {
                    time += animationDelay;
                    Thread.sleep(Math.max(0,time - 
                      System.currentTimeMillis()));
                  }catch (InterruptedException e) {
                    System.out.println(e);
                  }//end catch
                }//end while loop
            
                
            
        
        }//end of run

}//end of class
