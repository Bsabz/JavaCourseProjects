package pathx.ui;

import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JOptionPane;
import properties_manager.PropertiesManager;
import static pathx.PathXConstants.*;
import pathx.PathX;
import pathx.PathX.PathXPropertyType;
import static pathx.PathX.PathXPropertyType.SONG_CUE_GAME_SCREEN;
import pathx.data.PathXDataModel;
import pathx.file.PathXFileManager;
import pathx.file.PathXLevelIO;

/**
 *
 * @author Brian Sabz
 */
public class PathXEventHandler {

    // THE PATH X GAME, IT PROVIDES ACCESS TO EVERYTHING
    private PathXMiniGame game;
    
    private boolean playMusic = true;
    private boolean playSound = true;

    PathXEventHandler(PathXMiniGame initGame) {
        game = initGame;
    }

    /**
     * Called when the user clicks the Settings button.
     */
    public void respondToHomeRequest()
    {       
        // SWITCH TO THE SETTINGS SCREEN SO THE 
        // USER MAY CHOOSE SETTINGS
        game.switchToMenuScreen();        
    }
    
    /**
     * Called when the user clicks the Play button.
     */
    public void respondToPlayAgainRequest()
    {
        
        // GET THE GAME'S DATA MODEL, WHICH IS ALREADY LOCKED FOR US
        PathXDataModel data = (PathXDataModel)game.getDataModel();
        
        // IF A GAME IS IN PROGRESS, THE PLAYER LOSES
        if (game.getDataModel().inProgress())
            game.getDataModel().endGameAsLoss();
        
        // SWITCH TO THE LEVEL SELECT SCREEN SO THE 
        // USER MAY CHOOSE ANOTHER LEVEL
        game.switchToLevelSelectScreen();
        game.initViewport();
    }
    
    /**
     * Called when the user clicks the Start button.
     */
    public void respondToStartGameRequest()
    {
        // IF A GAME IS NOT IN PROGRESS, START GAME
        if (!(game.getDataModel().inProgress()))
            game.getDataModel().beginGame();       
    }
    
    /**
     * Called when the user clicks the Play Game button.
     */
    public void respondToPlayGameRequest()
    {
        if (game.isCurrentScreenState(GAME_SCREEN_STATE))
        {
        // IF A GAME IS IN PROGRESS, PLAY GAME
        if (game.getDataModel().inProgress())
            game.getDataModel().unpause();
        
        if(isPlaySound() == true){
        game.getAudio().play(PathXPropertyType.AUDIO_CUE_COLLISION.toString(), false);
        }
        
        // SWITCH TO THE LEVEL SELECT SCREEN SO THE 
        // USER MAY CHOOSE ANOTHER LEVEL
        game.switchToPauseButton();   
        }
    }
    
    /**
     * Called when the user clicks the Pause button.
     */
    public void respondToPauseGameRequest()
    {
        
        PathXDataModel data = (PathXDataModel)game.getDataModel();
    
        int playerCash = data.getBalance();
                    
        if (game.isCurrentScreenState(GAME_SCREEN_STATE) && (playerCash > 10))
        {
            
        data.setBalance(playerCash - 10);
        // IF A GAME IS IN PROGRESS, PAUSE GAME
        if (game.getDataModel().inProgress())
            game.getDataModel().pause();
        
        if(isPlaySound() == true){
        ((PathXMiniGame)game).getAudio().play(PathXPropertyType.AUDIO_CUE_COLLISION.toString(), true);
        }
        // SWITCH TO THE LEVEL SELECT SCREEN SO THE 
        // USER MAY CHOOSE ANOTHER LEVEL
        game.switchToPlayButton();   
        }
    }
    
    /**
     * Called when the user clicks the Settings button.
     */
    public void respondToSettingsRequest()
    {       
        // SWITCH TO THE SETTINGS SCREEN SO THE 
        // USER MAY CHOOSE SETTINGS
        game.switchToSettingsScreen();        
    }
    
    public void respondToUncheckMusicRequest()
    {    
        game.getAudio().stop(PathXPropertyType.SONG_CUE_GAME_SCREEN.toString());
        game.getAudio().stop(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString());
        setPlayMusic(false);
        game.switchToUncheckMusicButton();
    }
    
    public void respondToCheckMusicRequest()
    {    
        game.getAudio().play(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString(), true);
        game.switchToCheckMusicButton();
        setPlayMusic(true);
    }
    
    public void respondToUncheckSoundRequest()
    {    
        game.getAudio().stop(PathXPropertyType.AUDIO_CUE_COLLISION.toString());
        game.getAudio().stop(PathXPropertyType.AUDIO_CUE_FAIL.toString());
        game.getAudio().stop(PathXPropertyType.AUDIO_CUE_WIN.toString());
        game.switchToUncheckSoundButton();
        setPlaySound(false);
    }
    
    public void respondToCheckSoundRequest()
    {    
        game.getAudio().play(PathXPropertyType.AUDIO_CUE_FAIL.toString(), false);
        game.switchToCheckSoundButton();
        setPlaySound(true);
    }
    
    /**
     * Called when the user clicks the Help button.
     */
    public void respondToHelpRequest()
    {       
        // SWITCH TO THE SETTINGS SCREEN SO THE 
        // USER MAY CHOOSE SETTINGS
        game.switchToHelpScreen();        
    }
    
    /**
     * Called when the user clicks the close window button.
     */
    public void respondToExitRequest() {
        // IF THE GAME IS STILL GOING ON, END IT AS A LOSS
        if (game.getDataModel().inProgress()) {
            game.getDataModel().endGameAsLoss();
        }
        int answer = JOptionPane.showConfirmDialog(game.getCanvas(), "Are you sure you want to quit?", "Exit",  JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        // AND CLOSE THE ALL
        if(answer == 0){
        System.exit(0);
        }
    }
    
    public void respondtoCloseDialogRequest() {
        // WE ONLY LET THIS HAPPEN IF THE GAME SCREEN IS VISIBLE
        if (game.isCurrentScreenState(GAME_SCREEN_STATE))
        {
            game.closeInfoDialog();
        }
    }
    
    /**
     * Called when the user clicks the Reset button.
     */
    public void respondToResetGameRequest()
    {
        
        JOptionPane.showMessageDialog(game.getCanvas(), "The game as been reset.", "RESET", JOptionPane.PLAIN_MESSAGE);
        // IF THERE IS A GAME UNDERWAY, COUNT IT AS A LOSS
        if (game.getDataModel().inProgress())
        {
            game.getDataModel().endGameAsLoss();
        }
        // RESET THE GAME AND ITS DATA
        else        
        game.reset();
        game.switchToMenuScreen();
    }
    
    /**
     * Called when the user clicks a button to select a level.
     */    
    public void respondToSelectLevelRequest(String levelFile)
    {
        // WE ONLY LET THIS HAPPEN IF THE MENU SCREEN IS VISIBLE
        if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE))
        {
            // GET THE GAME'S DATA MODEL, WHICH IS ALREADY LOCKED FOR US
            PathXDataModel data = (PathXDataModel)game.getDataModel();
        
            // UPDATE THE DATA
            //PathXFileManager fM = game.getFileManager();
            //fM.load(file);
            
            //LOAD THE LEVEL
            File file = new File(levelFile);
            
            game.getLevelIO().loadLevel(file, data);
            
            data.setCurrentLevel(levelFile);

            // GO TO THE GAME
            game.switchToGameScreen();
           
           data.getViewport().setScreenSize(WINDOW_WIDTH, WINDOW_HEIGHT);
           data.getViewport().setGameWorldSize(GAME_LEVEL_WIDTH, GAME_LEVEL_HEIGHT);
           data.getViewport().setViewportDimensions(VIEWPORT_GAME_WIDTH, VIEWPORT_GAME_HEIGHT);
           data.getViewport().setViewportOffset(0, 0);
        }        
    }
    
    /**
     * Called when the user presses a key on the keyboard.
     */    
    public void respondToKeyPress(int keyCode)
    {
        //PathXDataModel data = (PathXDataModel)game.getDataModel();

        // WASD MOVES THE VIEWPORT
        if (keyCode == KeyEvent.VK_UP) {
            if (game.getCurrentScreenState().equals(LEVEL_SELECT_SCREEN_STATE)) {
                game.getDataModel().getViewport().scroll(0, VIEWPORT_INC);
            } else {
                game.getDataModel().getViewport().move(0, -VIEWPORT_INC);
            }
        } else if (keyCode == KeyEvent.VK_LEFT) {
            if (game.getCurrentScreenState().equals(LEVEL_SELECT_SCREEN_STATE)) {
                game.getDataModel().getViewport().scroll(VIEWPORT_INC, 0);
            } else {
                game.getDataModel().getViewport().move(-VIEWPORT_INC, 0);
            }
        } else if (keyCode == KeyEvent.VK_DOWN) {
            if (game.getCurrentScreenState().equals(LEVEL_SELECT_SCREEN_STATE)) {
                game.getDataModel().getViewport().scroll(0, -VIEWPORT_INC);
            } else {
                game.getDataModel().getViewport().move(0, VIEWPORT_INC);
            }
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            if (game.getCurrentScreenState().equals(LEVEL_SELECT_SCREEN_STATE)) {
                game.getDataModel().getViewport().scroll(-VIEWPORT_INC, 0);
            } else {
                game.getDataModel().getViewport().move(VIEWPORT_INC, 0);
            }
        }
        else if (keyCode == KeyEvent.VK_F) {
            if (game.isCurrentScreenState(GAME_SCREEN_STATE) && game.getGUIButtons().get(PLAY_GAME_BUTTON_TYPE).isEnabled() == false) {
                respondToPauseGameRequest();
            }
            else if(game.isCurrentScreenState(GAME_SCREEN_STATE) && game.getGUIButtons().get(PLAY_GAME_BUTTON_TYPE).isEnabled() == true){
                respondToPlayGameRequest();
            }
        }
        else if (keyCode == KeyEvent.VK_0){
            if (game.isCurrentScreenState(GAME_SCREEN_STATE) || game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)){
                ((PathXDataModel)game.getDataModel()).setBalance(10000);
            }
        }
    }

    /**
     * @return the playMusic
     */
    public boolean isPlayMusic() {
        return playMusic;
    }

    /**
     * @param playMusic the playMusic to set
     */
    public void setPlayMusic(boolean playMusic) {
        this.playMusic = playMusic;
    }

    /**
     * @return the playSound
     */
    public boolean isPlaySound() {
        return playSound;
    }

    /**
     * @param playSound the playSound to set
     */
    public void setPlaySound(boolean playSound) {
        this.playSound = playSound;
    }
}
