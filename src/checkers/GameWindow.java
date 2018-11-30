package checkers;

import java.awt.BorderLayout;
import java.io.IOException;
import javax.swing.JFrame;

public class GameWindow extends JFrame{
    
    private final Game gp;
      
    public GameWindow(int width,int height) throws IOException{
        
        super("Checkers");
      
        gp = new Game(width,height);
        
        super.getContentPane().add(gp, BorderLayout.CENTER);      
        super.pack();     
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setResizable(false);
        super.setVisible(true);
        super.setFocusable(true);
    }

    public static void main(String[] args) throws IOException{
        GameWindow w = new GameWindow(800,850);
    }  
}
