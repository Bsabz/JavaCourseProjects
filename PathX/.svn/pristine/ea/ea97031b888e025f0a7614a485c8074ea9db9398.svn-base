package pathx.ui;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import properties_manager.PropertiesManager;
import static pathx.PathXConstants.*;
import pathx.PathX;
import pathx.PathX.PathXPropertyType;
import static pathx.PathX.PathXPropertyType.SONG_CUE_GAME_SCREEN;
import pathx.data.Intersection;
import pathx.data.PathXDataModel;
import pathx.data.Road;
import pathx.file.PathXFileManager;
import pathx.file.PathXLevelIO;

/**
 *
 * @author Brian Sabz
 */
public class PathXSpecialsHandler implements MouseListener, MouseMotionListener{
    
    // THE PATH X GAME, IT PROVIDES ACCESS TO EVERYTHING
    private PathXMiniGame game;
    
    boolean activeSpecial = false;
    
    PathXSpecialsHandler(PathXMiniGame initGame) {
        game = initGame;
    }
    
    /**
     * Called when the user presses a key on the keyboard.
     */    
    public void respondToSpecialsKeyPress(int keyCode)
    {
        //PathXDataModel data = (PathXDataModel)game.getDataModel();

        // CHEAT BY ONE MOVE. NOTE THAT IF WE HOLD THE A
        // KEY DOWN IT WILL CONTINUALLY CHEAT
        if (keyCode == KeyEvent.VK_A) {
        } //INTANGIBILITY SPECIAL
        else if (keyCode == KeyEvent.VK_B) {
            PathXDataModel data = (PathXDataModel) game.getDataModel();

            int playerCash = data.getBalance();
            if (game.isCurrentScreenState(GAME_SCREEN_STATE) && (playerCash > 30)) {
                data.setBalance(playerCash - 30);

                if (game.getEventHandler().isPlaySound() == true) {
                    game.getAudio().play(PathXPropertyType.AUDIO_CUE_COLLISION.toString(), false);
                }
            }

        } //CLOSE INTERSECTION SPECIAL
        else if (keyCode == KeyEvent.VK_C) {
            PathXDataModel data = (PathXDataModel) game.getDataModel();

            int playerCash = data.getBalance();
            if (game.isCurrentScreenState(GAME_SCREEN_STATE) && (playerCash > 25)) {
                data.setBalance(playerCash - 25);

                if (game.getEventHandler().isPlaySound() == true) {
                    game.getAudio().play(PathXPropertyType.AUDIO_CUE_COLLISION.toString(), false);
                }
            }

        } //EMPTY GAS TANK SPECIAL
        else if (keyCode == KeyEvent.VK_E) {
            PathXDataModel data = (PathXDataModel) game.getDataModel();

            int playerCash = data.getBalance();
            if (game.isCurrentScreenState(GAME_SCREEN_STATE) && (playerCash > 20)) {
                data.setBalance(playerCash - 20);

                if (game.getEventHandler().isPlaySound() == true) {
                    game.getAudio().play(PathXPropertyType.AUDIO_CUE_COLLISION.toString(), false);
                }
            }

        } //GREEN LIGHT SPECIAL
        else if (keyCode == KeyEvent.VK_G) {
            PathXDataModel data = (PathXDataModel) game.getDataModel();

            activeSpecial = true;

            int playerCash = data.getBalance();
            if (game.isCurrentScreenState(GAME_SCREEN_STATE) && (playerCash > 5)) {
                data.setBalance(playerCash - 5);

                if (game.getEventHandler().isPlaySound() == true) {
                    game.getAudio().play(PathXPropertyType.AUDIO_CUE_COLLISION.toString(), false);
                }
            }

        } //CLOSE ROAD SPECIAL
        else if (keyCode == KeyEvent.VK_H) {
            PathXDataModel data = (PathXDataModel) game.getDataModel();

            int playerCash = data.getBalance();
            if (game.isCurrentScreenState(GAME_SCREEN_STATE) && (playerCash > 25)) {
                data.setBalance(playerCash - 25);

                if (game.getEventHandler().isPlaySound() == true) {
                    game.getAudio().play(PathXPropertyType.AUDIO_CUE_COLLISION.toString(), false);
                }
            }

        } //MINDLESS TERROR SPECIAL
        else if (keyCode == KeyEvent.VK_L) {
            PathXDataModel data = (PathXDataModel) game.getDataModel();

            int playerCash = data.getBalance();
            if (game.isCurrentScreenState(GAME_SCREEN_STATE) && (playerCash > 30)) {
                data.setBalance(playerCash - 30);

                if (game.getEventHandler().isPlaySound() == true) {
                    game.getAudio().play(PathXPropertyType.AUDIO_CUE_COLLISION.toString(), false);
                }
            }

        } //MIND CONTROL SPECIAL
        else if (keyCode == KeyEvent.VK_M) {
            PathXDataModel data = (PathXDataModel) game.getDataModel();

            int playerCash = data.getBalance();
            if (game.isCurrentScreenState(GAME_SCREEN_STATE) && (playerCash > 30)) {
                data.setBalance(playerCash - 30);

                if (game.getEventHandler().isPlaySound() == true) {
                    game.getAudio().play(PathXPropertyType.AUDIO_CUE_COLLISION.toString(), false);
                }
            }
        } //OPEN INTERSECTION SPECIAL
        else if (keyCode == KeyEvent.VK_O) {
            PathXDataModel data = (PathXDataModel) game.getDataModel();

            int playerCash = data.getBalance();
            if (game.isCurrentScreenState(GAME_SCREEN_STATE) && (playerCash > 25)) {
                data.setBalance(playerCash - 25);

                if (game.getEventHandler().isPlaySound() == true) {
                    game.getAudio().play(PathXPropertyType.AUDIO_CUE_COLLISION.toString(), false);
                }
            }
        } //INC PLAYER SPEED
        else if (keyCode == KeyEvent.VK_P) {
            PathXDataModel data = (PathXDataModel) game.getDataModel();

            int playerCash = data.getBalance();
            if (game.isCurrentScreenState(GAME_SCREEN_STATE) && (playerCash > 20)) {
                data.setBalance(playerCash - 20);

                if (game.getEventHandler().isPlaySound() == true) {
                    game.getAudio().play(PathXPropertyType.AUDIO_CUE_COLLISION.toString(), false);
                }
            }

        } //STEAL SPECIAL
        else if (keyCode == KeyEvent.VK_Q) {
            PathXDataModel data = (PathXDataModel) game.getDataModel();

            int playerCash = data.getBalance();
            if (game.isCurrentScreenState(GAME_SCREEN_STATE) && (playerCash > 30)) {
                data.setBalance(playerCash - 30);

                if (game.getEventHandler().isPlaySound() == true) {
                    game.getAudio().play(PathXPropertyType.AUDIO_CUE_COLLISION.toString(), false);
                }
            }

        } //RED LIGHT SPECIAL
        else if (keyCode == KeyEvent.VK_R) {
            PathXDataModel data = (PathXDataModel) game.getDataModel();

            int playerCash = data.getBalance();
            if (game.isCurrentScreenState(GAME_SCREEN_STATE) && (playerCash > 5)) {
                data.setBalance(playerCash - 5);

                if (game.getEventHandler().isPlaySound() == true) {
                    game.getAudio().play(PathXPropertyType.AUDIO_CUE_COLLISION.toString(), false);
                }
            }

        } //FLAT TIRE SPECIAL
        else if (keyCode == KeyEvent.VK_T) {
            PathXDataModel data = (PathXDataModel) game.getDataModel();

            int playerCash = data.getBalance();
            if (game.isCurrentScreenState(GAME_SCREEN_STATE) && (playerCash > 20)) {
                data.setBalance(playerCash - 20);

                if (game.getEventHandler().isPlaySound() == true) {
                    game.getAudio().play(PathXPropertyType.AUDIO_CUE_COLLISION.toString(), false);
                }
            }

        } //INVINCIBILITYSPECIAL
        else if (keyCode == KeyEvent.VK_V) {
            PathXDataModel data = (PathXDataModel) game.getDataModel();

            int playerCash = data.getBalance();
            if (game.isCurrentScreenState(GAME_SCREEN_STATE) && (playerCash > 40)) {
                data.setBalance(playerCash - 40);

                if (game.getEventHandler().isPlaySound() == true) {
                    game.getAudio().play(PathXPropertyType.AUDIO_CUE_COLLISION.toString(), false);
                }
            }

        } //INC SPEED LIMIT
        else if (keyCode == KeyEvent.VK_X) {
            PathXDataModel data = (PathXDataModel) game.getDataModel();

            int playerCash = data.getBalance();
            if (game.isCurrentScreenState(GAME_SCREEN_STATE) && (playerCash > 15)) {
                data.setBalance(playerCash - 15);

                if (game.getEventHandler().isPlaySound() == true) {
                    game.getAudio().play(PathXPropertyType.AUDIO_CUE_COLLISION.toString(), false);
                }
            }

        } //FLYING SPECIAL
        else if (keyCode == KeyEvent.VK_Y) {
            PathXDataModel data = (PathXDataModel) game.getDataModel();

            int playerCash = data.getBalance();
            if (game.isCurrentScreenState(GAME_SCREEN_STATE) && (playerCash > 40)) {
                data.setBalance(playerCash - 40);

                if (game.getEventHandler().isPlaySound() == true) {
                    game.getAudio().play(PathXPropertyType.AUDIO_CUE_COLLISION.toString(), false);
                }
            }

        } //DEC SPEED LIMIT
        else if (keyCode == KeyEvent.VK_Z) {
            PathXDataModel data = (PathXDataModel) game.getDataModel();

            int playerCash = data.getBalance();
            if (game.isCurrentScreenState(GAME_SCREEN_STATE) && (playerCash > 15)) {
                data.setBalance(playerCash - 15);

                if (game.getEventHandler().isPlaySound() == true) {
                    game.getAudio().play(PathXPropertyType.AUDIO_CUE_COLLISION.toString(), false);
                }
            }
        }
    }

    /**
     * This method will be used to respond to right-button mouse clicks
     * on intersections so that we can toggle them open or closed.
     */
    @Override
    public void mouseClicked(MouseEvent me)
    {
        PathXDataModel data = (PathXDataModel) game.getDataModel();
        // RIGHT MOUSE BUTTON IS TO TOGGLE OPEN/CLOSE INTERSECTION
        if (me.getButton() == MouseEvent.BUTTON3)
        {
            // SEE IF WE CLICKED ON AN INTERSECTION
            Intersection i = data.findIntersectionAtCanvasLocation(me.getX(), me.getY());
            if (i != null)
            {
                // TOGGLE THE INTERSECTION
                i.toggleOpen();
                activeSpecial = false;
            }            
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
        
        PathXDataModel data = (PathXDataModel) game.getDataModel();
        // MAKE SURE THE CANVAS HAS FOCUS SO THAT IT
        // WILL PROCESS THE PROPER KEY PRESSES
        ((JPanel)me.getSource()).requestFocusInWindow();
        
        // THESE ARE CANVAS COORDINATES
        int canvasX = me.getX();
        int canvasY = me.getY();
        
        // IF WE ARE IN ONE OF THESE MODES WE MAY WANT TO SELECT
        // ANOTHER INTERSECTION ROAD
        if (data.isNothingSelected()
                || data.isIntersectionSelected()
                || data.isRoadSelected())
        {
            // CHECK TO SEE IF THE USER IS SELECTING AN INTERSECTION
            Intersection i = data.findIntersectionAtCanvasLocation(canvasX, canvasY);
            if (i != null)
            {
                // MAKE THIS THE SELECTED INTERSECTION
                data.setSelectedIntersection(i);
                data.switchSpecialMode(PathXSpecialState.INTERSECTION_DRAGGED);
                return;
            }                      
            
            // IF NO INTERSECTION WAS SELECTED THEN CHECK TO SEE IF 
            // THE USER IS SELECTING A ROAD
            Road r = data.selectRoadAtCanvasLocation(canvasX, canvasY);
            if (r != null)
            {
                // MAKE THIS THE SELECTED ROAD
                data.setSelectedRoad(r);
                data.switchSpecialMode(PathXSpecialState.ROAD_SELECTED);
                return;
            }

            // OTHERWISE DESELECT EVERYTHING
            data.unselectEverything();            
        }
        // PERHAPS THE USER IS WANTING TO ADD THE FIRST INTERSECTION OF A ROAD
        else if (data.isAddingRoadStart())
        {
            // TRY SELECTING A ROAD
            data.selectStartRoadIntersection(canvasX, canvasY);
        }
        // PERHAPS THE USER IS IN THE PROCESS OF ADDING A ROAD
        else if (data.isAddingRoadEnd())
        {
            // TRY ADDING THE SECOND NODE FOR A ROAD
            data.selectEndRoadIntersection(canvasX, canvasY);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseDragged(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseMoved(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
