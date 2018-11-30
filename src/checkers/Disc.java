package checkers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Disc {
    
    int player_number;
    int discSize = 100;
    private int Xpos;
    private int Ypos;
    private boolean king = false;
    boolean mustMove = false;
    Image checker;
    
    public Disc(int player_number) throws IOException
    {
        this.player_number = player_number;
        if(this.player_number == 1){this.checker = ImageIO.read(new File("images\\whitechecker.png"));}
        else{this.checker = ImageIO.read(new File("images\\blackchecker.png"));}
    }

    public void paint(Graphics window)
    {
        checkKing();
        
        window.drawImage(checker,Xpos-15,Ypos-15,null);
        
        if(king)
        {
            window.setColor(Color.WHITE);
            window.setFont(new Font("Serif", Font.BOLD, 24));
            window.drawString("K", Xpos + discSize/2, Ypos + discSize/2);
        }
    }
    
    public int getX(){return this.Xpos;}
    public void setX(int x){this.Xpos = x;}
    
    public int getY(){return this.Ypos;}
    public void setY(int y){this.Ypos = y;}
    
    public boolean isKing(){return this.king;}
    public void makeKing(){this.king = true;}

    private void checkKing()
    {
        if(player_number == 1)
        {
            if(Ypos > 700){this.makeKing();}
        }
        if(player_number == 2)
        {
            if(Ypos < 100){this.makeKing();}
        }  
    }
    
}
