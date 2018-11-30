package checkers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

class Game extends JPanel implements MouseListener, ActionListener{

    private final int PANEL_WIDTH;
    private final int PANEL_HEIGHT;
    private final int offset = 5;
    JButton restartBtn;
    
    
    Board board = new Board();
    Disc[][] discs = new Disc[8][8];
    int[] showingcoords = new int[2];
    boolean showing = false;
    private boolean playeroneturn = true;
    int one_score = 0;
    int two_score = 0;
    private boolean end;
    
    public Game(int width, int height) throws IOException{
        
        this.PANEL_WIDTH = width;
        this.PANEL_HEIGHT = height;
        
        Music music = new Music("sounds/out_of_touch.mid");
        music.start();
        
        gameSetup();
        
        //window settings
        super.setBackground(new Color(255,255,255));
        super.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        
        //listeners
        super.addMouseListener(this);
        
        
        //buttons
        this.restartBtn = new JButton ("RESTART");
        super.add(this.restartBtn);
        this.restartBtn.addActionListener(this);
        this.restartBtn.setVisible(false);
    }
    
    
    @Override
    public void paintComponent(Graphics window){
        super.paintComponent(window);//paint on the jframe created in gamewindow.java
        if(!end){
            board.paint(window);
            for(int y=0; y < discs.length;y++)
            {
                for(int x=0; x < discs[y].length;x++)
                {   
                    try{
                        discs[y][x].paint(window);
                    }catch(Exception e){}
                }
            }
        }
        try {drawScore(window);}catch (IOException ex){}
        
     }
    
    
    @Override
    public void mousePressed(MouseEvent e){
        
        //get y and x pos
        int x = e.getX();
        int y = e.getY();
        
        //check which disc clicked 
        int tilex = tileClicked(x,y)[0];
        int tiley = tileClicked(x,y)[1];
        
        
        
        
        if(discs[tiley][tilex] != null)//if ive clicked a piec
        { 
            
            //if what we clicked is the one showing moves then we reset the board and reset showing varibles
            if(tilex == showingcoords[0] && tiley == showingcoords[1])
            {
                Board.resetboard();
                showing = false;
                showingcoords[0] = -1;//showing coords will never equal -1 in a real game
                showingcoords[1] = -1;
            }
            else if(discs[tiley][tilex].player_number == 1 && playeroneturn && !showing && !discs[tiley][tilex].isKing()){movesPlayer1(tilex,tiley);}
            else if(discs[tiley][tilex].player_number == 2 && !playeroneturn && !showing && !discs[tiley][tilex].isKing()){movesPlayer2(tilex,tiley);}
            else if(discs[tiley][tilex].player_number == 1 && playeroneturn && discs[tiley][tilex].isKing()){kingMoves(tilex,tiley,1);}
            else if(discs[tiley][tilex].player_number == 2 && !playeroneturn && discs[tiley][tilex].isKing()){kingMoves(tilex,tiley,2);}
            //if its a player2 peice and its player 2s go show player2 moves
            //if its a player1 peice and its player ones go show player1 move;
        }
        
        
        
        
        
        //if we click an avaible moving square.
        if(Board.boardC[tiley][tilex] == 'p')
        {
            int difx = tilex-showingcoords[0];
            int dify = tiley-showingcoords[1];
            
            discs[tiley][tilex] = discs[showingcoords[1]][showingcoords[0]]; //set the peice the moves came from to the new space.
            discs[showingcoords[1]][showingcoords[0]] = null;//set the original space to nothing
            
            if( difx % 2 == 0x0 && dify % 2 == 0x0)//if theres a difference of two we know that we are removing.
            {
                int removex = showingcoords[0] + ((tilex-showingcoords[0])/2);
                int removey = showingcoords[1] + ((tiley-showingcoords[1])/2);
                discs[removey][removex] = null;
                if(discs[tiley][tilex].player_number == 1){one_score++;}
                else{two_score++;}
                
                int[] next = checkAnotherGo(tilex,tiley);
                
                if(next[0] != -1)
                {
                    discs[next[1]][next[0]] = discs[tiley][tilex];
                    discs[tiley][tilex] = null;
                    if(discs[next[1]][next[0]].player_number == 1){one_score++;}
                    else{two_score++;}
                    
                    removex = tilex + ((next[0]-tilex)/2);
                    removey = tiley + ((next[1]-tiley)/2);
                    discs[removey][removex] = null;
                    tilex = next[0];
                    tiley = next[1];
                }
            }
            
            discs[tiley][tilex].setX((tilex * 100)+offset);//set the new xpos
            discs[tiley][tilex].setY((tiley * 100)+offset);// and y pos
            
            showing = false;
            Board.resetboard();
            showingcoords[0] = -1;
            showingcoords[1] = -1;
            //System.out.println(check[0]);
            
            
            playeroneturn = !playeroneturn;
            
        }
        repaint();
    }
    public void mouseReleased(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mouseClicked(MouseEvent e){}


    //this is our button handler
    @Override
    public void actionPerformed(ActionEvent e){
        //call different methods based on what button was clicked.
        switch(e.getActionCommand()){
            case "RESTART":
                try {restartGame();}catch(IOException ex){}
                repaint();
        }      
    } 
    
    public void gameSetup() throws IOException{
        
        int player_num = 0;
        
        for(int y=0; y < discs.length;y++)
        {
            for(int x=0; x < discs[y].length;x++)
            {
                if(Board.boardC[y][x] == 'b')
                {
                    if(player_num < 12)
                    {
                        discs[y][x] = new Disc(1);
                        discs[y][x].setX((x*100)+offset);
                        discs[y][x].setY((y*100)+offset);
                        player_num++;
                    }
                    else if(y >= 5){
                        discs[y][x] = new Disc(2);
                        discs[y][x].setX((x*100)+offset);
                        discs[y][x].setY((y*100)+offset);
                    }
                } 
            }
        }
    
    }

    private int[] tileClicked(int mouseX, int mouseY){
        int[] tilecoords = new int[2];
        
        for(int y=0; y < discs.length;y++)
        {
            for(int x=0; x < discs[y].length;x++)
            {   
                try{
                    int tileX = x*100;
                    int tileY = y*100;
                    if( (mouseX >= tileX && mouseX <= tileX + 100) && (mouseY >= tileY && mouseY <= tileY + 100)) 
                    {
                        tilecoords[0] = x;
                        tilecoords[1] = y;
                        return tilecoords;
                    }
                }catch(Exception e){}
            }
        }
        return null;
    }
    

    private void movesPlayer1(int discx, int discy)
    {   
        Disc disc = discs[discy][discx];
        Boolean changed = false;
        if(!disc.isKing())//if its not king only display moves going down.
        {
            //dont display if its on the side cols
            if(discx != 7 && discy != 7)
            {
                if(discs[discy+1][discx+1] == null){Board.boardC[discy+1][discx+1] = 'p';changed = true;}//only show if its empty
                if(discx < 6 && discy < 6)//cant take otherwise
                {
                    if(discs[discy+1][discx+1] != null && discs[discy+2][discx+2] == null)//if the spaces are free and theres a player to overtake
                    {
                        if(discs[discy+1][discx+1].player_number == 2){Board.boardC[discy+2][discx+2] = 'p';changed = true;} //if its an enemey player
                    } 
                }
            }
            if(discx != 0 && discy != 7)
            {
                if(discs[discy+1][discx-1] == null){Board.boardC[discy+1][discx-1] = 'p';changed = true;}//only show if its empty
                if(discx > 1 && discy < 6)//cant take otherwise
                {
                    if(discs[discy+1][discx-1] != null && discs[discy+2][discx-2] == null)
                    {
                        if(discs[discy+1][discx-1].player_number == 2){Board.boardC[discy+2][discx-2] = 'p';changed = true;}  
                    }
                }
            }
            if(changed) //if any moves have not been shown allow user to click other things without having to click the same peice first
            {
                showingcoords[0] = discx;
                showingcoords[1] = discy;
                showing = true;
            }   
        }
    }
    
    private void movesPlayer2(int discx, int discy)
    {   
        Disc disc = discs[discy][discx];
        Boolean changed = false;
        if(!disc.isKing())//if its not king only display moves going up.
        {
            //if its on the sides only display one move
            if(discx != 7 && discy != 0)
            {
                if(discs[discy-1][discx+1] == null){Board.boardC[discy-1][discx+1] = 'p';changed = true;}//only show if its empty
                if(discx < 6 && discy > 1)//cant take otherwise
                {
                    if(discs[discy-1][discx+1] != null && discs[discy-2][discx+2] == null)//if the spaces are free and theres a player to overtake
                    {
                        if(discs[discy-1][discx+1].player_number != disc.player_number){Board.boardC[discy-2][discx+2] = 'p';changed = true;} //if its an enemey player
                    }                
                }
            }
            
            if(discx != 0 && discy != 0)
            {
                if(discs[discy-1][discx-1] == null){Board.boardC[discy-1][discx-1] = 'p';changed = true;}//only show if its empty
                if(discx > 1 && discy > 1)//cant take otherwise
                {
                    if(discs[discy-1][discx-1] != null && discs[discy-2][discx-2] == null)
                    {
                        if(discs[discy-1][discx-1].player_number != disc.player_number){Board.boardC[discy-2][discx-2] = 'p';changed = true;}  
                    }
                }
            }
            if(discy == 0){disc.makeKing();}//king him if iam at enemys start
            if(changed) //if any moves have not been shown allow user to click other things without having to click the same peice first
            {
                showingcoords[0] = discx;
                showingcoords[1] = discy;
                showing = true;
            } 
        }
    }

    private void kingMoves(int discx, int discy, int player) {
        
        Disc disc = discs[discy][discx];
        Boolean changed = false;
        
        //player one moves
        if(discx != 7 && discy != 7)
        {
            if(discs[discy+1][discx+1] == null){Board.boardC[discy+1][discx+1] = 'p';changed = true;}//only show if its empty
            if(discx < 6 && discy < 6)//cant take otherwise
            {
                if(discs[discy+1][discx+1] != null && discs[discy+2][discx+2] == null)//if the spaces are free and theres a player to overtake
                {
                    if(discs[discy+1][discx+1].player_number != disc.player_number){Board.boardC[discy+2][discx+2] = 'p';changed = true;} //if its an enemey player
                } 
            }
        }
        if(discx != 0 && discy != 7)
        {
            if(discs[discy+1][discx-1] == null){Board.boardC[discy+1][discx-1] = 'p';changed = true;}//only show if its empty
            if(discx > 1 && discy < 6)//cant take otherwise
            {
                if(discs[discy+1][discx-1] != null && discs[discy+2][discx-2] == null)
                {
                    if(discs[discy+1][discx-1].player_number != disc.player_number){Board.boardC[discy+2][discx-2] = 'p';changed = true;}  
                }
            }
        }
        
        //player two moves
        if(discx != 7 && discy != 0)
        {
            if(discs[discy-1][discx+1] == null){Board.boardC[discy-1][discx+1] = 'p';changed = true;}//only show if its empty
            if(discx < 6 && discy > 1)//cant take otherwise
            {
                if(discs[discy-1][discx+1] != null && discs[discy-2][discx+2] == null)//if the spaces are free and theres a player to overtake
                {
                    if(discs[discy-1][discx+1].player_number != disc.player_number){Board.boardC[discy-2][discx+2] = 'p';changed = true;} //if its an enemey player
                }                
            }
        }
        if(discx != 0 && discy != 0)
        {
            if(discs[discy-1][discx-1] == null){Board.boardC[discy-1][discx-1] = 'p';changed = true;}//only show if its empty
            if(discx > 1 && discy > 1)//cant take otherwise
            {
                if(discs[discy-1][discx-1] != null && discs[discy-2][discx-2] == null)
                {
                    if(discs[discy-1][discx-1].player_number != disc.player_number){Board.boardC[discy-2][discx-2] = 'p';changed = true;}  
                }
            }
        }
        if(changed) //if any moves have not been shown allow user to click other things without having to click the same peice first
        {
            showingcoords[0] = discx;
            showingcoords[1] = discy;
            showing = true;
        }
    }

    private int[] checkAnotherGo(int discx, int discy){
        
        Disc disc = discs[discy][discx];
        
        if(disc.player_number == 1 || disc.isKing())//player1 //check down right and down left
        {
            //check down right
            if(discy < 6 && discx < 6)
            {
                if(discs[discy+1][discx+1] != null && discs[discy+2][discx+2] == null)//ready for taking
                {
                    int coords[] = {discx+2,discy+2};
                    System.out.println("down right checked");
                    if(disc.player_number != discs[discy+1][discx+1].player_number){return coords;}
                }
            }
            //check down left
            if(discy < 6 && discx > 1)
            {
                if(discs[discy+1][discx-1] != null && discs[discy+2][discx-2] == null)//ready for taking
                {
                    int coords[] = {discx-2,discy+2};
                    System.out.println("down left checked");
                    if(disc.player_number != discs[discy+1][discx-1].player_number){return coords;}
                }
            }
        }
        if(disc.player_number == 2 || disc.isKing())//player 2 //check only up right and up left
        {
            //check up right
            if(discy > 1 && discx < 6)
            {
                if(discs[discy-1][discx+1] != null && discs[discy-2][discx+2] == null)//ready for taking
                {
                    int coords[] = {discx+2,discy-2};
                    if(disc.player_number != discs[discy-1][discx+1].player_number){return coords;}
                }
            }
            //check up left
            if(discy > 1 && discx > 1)
            {
                if(discs[discy-1][discx-1] != null && discs[discy-2][discx-2] == null)//ready for taking
                {
                    int coords[] = {discx-2,discy-2};
                    if(disc.player_number != discs[discy-1][discx-1].player_number){return coords;}
                }
            }
        }
        int coords[] = {-1,-1};
        return coords;
    }

    private void drawScore(Graphics window) throws IOException
    {
        Image scoreholder;
        Image player1win = ImageIO.read(new File("images\\player1win.png"));
        Image player2win = ImageIO.read(new File("images\\player2win.png"));
        
        if(one_score != 12 & two_score != 12)
        {
            scoreholder = ImageIO.read(new File("images\\scoreholder.png"));

            window.drawImage(scoreholder, 0,810,null);

            window.setColor(Color.BLACK);
            window.setFont(new Font("Arial",0, 45));
            
            window.drawString(""+one_score, 200, 848);
            
            window.drawString(""+two_score, 670, 848);
        }
        else if(one_score == 12)
        {
            repaint();
            end = true;
            window.setColor(Color.BLACK);
            window.fillRect(0, 0, 900, 900);
            window.drawImage(player1win, 25,100,null);
            this.restartBtn.setVisible(true);
        }
        else if(two_score == 12)
        {
            repaint();
            end = true;
            window.setColor(Color.BLACK);
            window.fillRect(0, 0, 900, 900);
            window.drawImage(player2win, 25,100,null);
            this.restartBtn.setVisible(true);
        }
        
        
        
    }

    private void restartGame() throws IOException
    {
        one_score = 0;
        two_score = 0;
        end = false;
        playeroneturn = true;
        for(int y=0; y < discs.length;y++)
        {
            for(int x=0; x < discs[y].length;x++)
            {
                discs[y][x] = null;
            }
        }
        gameSetup();
        this.restartBtn.setVisible(false);
    }
}