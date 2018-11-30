package checkers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Board {
    
    public static char[][] boardC = new char[8][8];
            
            
            /*{{'w','b','w','b','w','b','w','b'},
                                    {'b','w','b','w','b','w','b','w'},
                                    {'w','b','w','b','w','b','w','b'},
                                    {'b','w','b','w','b','w','b','w'},
                                    {'w','b','w','b','w','b','w','b'},
                                    {'b','w','b','w','b','w','b','w'},
                                    {'w','b','w','b','w','b','w','b'},
                                    {'b','w','b','w','b','w','b','w'},
                                   };*/
    
    int boardSize = 800;
    int squareSize = 800/8;
    private final int offset = 5;
    
    Image whitetile;
    Image blacktile;
    Image pinktile;
    
    
    public Board() throws IOException
    {
        this.whitetile = ImageIO.read(new File("images\\whitetile.png"));
        this.blacktile = ImageIO.read(new File("images\\blacktile.png"));
        this.pinktile = ImageIO.read(new File("images\\pinktile.png"));
        resetboard();
    }

    public void paint(Graphics window)
    {
        window.fillRect(0, 0, boardSize+25, boardSize+offset+25);
        for(int y=0; y < boardC.length;y++)
        {
            for(int x=0; x < boardC[y].length;x++)
            {
                if(boardC[y][x] == 'w'){window.drawImage(whitetile, (x*squareSize)+offset, (y*squareSize)+offset,null);}                                    //window.fillRect((x*squareSize)+offset, (y*squareSize)+offset, squareSize, squareSize);}
                if(boardC[y][x] == 'b'){window.drawImage(blacktile, (x*squareSize)+offset, (y*squareSize)+offset,null);}                 //window.fillRect((x*squareSize)+offset, (y*squareSize)+offset, squareSize, squareSize);}
                if(boardC[y][x] == 'p'){window.drawImage(pinktile, (x*squareSize)+offset, (y*squareSize)+offset,null);}
            } 
        }   
    }
    
    static public void resetboard()
    {
        boolean white = true;
        for(int y=0; y < boardC.length;y++)
        {
            for(int x=0; x < boardC[y].length;x++)
            {
                if(white){boardC[y][x] = 'w';white = !white;}
                else{boardC[y][x] = 'b';white = !white;}
            }
            white = !white;
        }
    }
    
    
}

