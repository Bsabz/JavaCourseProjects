package pathx.ui;

import java.awt.Cursor;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import pathx.data.PathXDataModel;
import mini_game.MiniGame;
import mini_game.MiniGameState;
import static pathx.PathXConstants.*;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
import properties_manager.PropertiesManager;
import pathx.PathXConstants;
import pathx.PathX.PathXPropertyType;
import pathx.data.Bandit;
import pathx.data.Intersection;
import pathx.file.PathXFileManager;
import pathx.data.Player;
import pathx.file.PathXLevelIO;

/**
 * This is the actual mini game, as extended from the mini game framework. It
 * manages all the UI elements.
 *
 * @author Brian Sabz
 */
public class PathXMiniGame extends MiniGame {

    // HANDLES GAME UI EVENTS
    private PathXEventHandler eventHandler;
    
    private PathXSpecialsHandler specialsHandler;

    // HANDLES ERROR CONDITIONS
    private PathXErrorHandler errorHandler;

    //MANAGES THE PLAYER RECORDS FILES
    private PathXFileManager fileManager;

    //MANAGES LOADING OF LEVELS  
    private PathXLevelIO LevelIO;

    // THE SCREEN CURRENTLY BEING PLAYED
    private String currentScreenState;

    // ACCESSOR METHODS
    // - getPlayerRecord
    // - getErrorHandler
    // - getFileManager
    // - isCurrentScreenState
    /**
     * Accessor method for getting the application's error handler.
     *
     * @return The error handler.
     */
    public PathXErrorHandler getErrorHandler() {
        return errorHandler;
    }
    
    public PathXEventHandler getEventHandler(){
        return eventHandler;
    }

    /**
     * Accessor method for getting the app's file manager.
     *
     * @return The file manager.
     */
    public PathXFileManager getFileManager() {
        return fileManager;
    }

    /**
     * Used for testing to see if the current screen state matches the
     * testScreenState argument. If it mates, true is returned, else false.
     *
     * @param testScreenState Screen state to test against the current state.
     *
     * @return true if the current state is testScreenState, false otherwise.
     */
    public boolean isCurrentScreenState(String testScreenState) {
        return testScreenState.equals(getCurrentScreenState());
    }

        // VIEWPORT UPDATE METHODS
    // - initViewport
    // - scroll
    // - moveViewport
     // SERVICE METHODS
    // - displayStats
    // - savePlayerRecord
    // - switchToGameScreen
    // - switchToSplashScreen
    // - updateBoundaries
    /**
     * Initializes the viewport to be used by the application.
     */
    @Override
    public void initViewport() {
        Viewport viewport = new Viewport();
        data.setViewport(viewport);
        viewport.setScreenSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        viewport.setGameWorldSize(MAP_WIDTH, MAP_HEIGHT);
        viewport.setViewportOffset(0, 0);
        viewport.setViewportSize(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        viewport.initViewportMargins();
        viewport.updateViewportBoundaries();
    }

    /**
     * This method displays makes the stats dialog display visible, which
     * includes the text inside.
     */
    public void displayLevelInfo() {
        // MAKE SURE ONLY THE PROPER DIALOG IS VISIBLE
        guiDialogs.get(WIN_DIALOG_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiDialogs.get(FAIL_DIALOG_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiDialogs.get(INFO_DIALOG_TYPE).setState(PathXState.VISIBLE_STATE.toString());
    }

    /**
     * This method forces the file manager to save the current player record.
     */
    /* public void savePlayerRecord()
     {
     fileManager.saveRecord(record);
     }*/
    // METHODS OVERRIDDEN FROM MiniGame
    // - initAudioContent
    // - initData
    // - initGUIControls
    // - initGUIHandlers
    // - reset
    // - updateGUI
    @Override
    /**
     * Initializes the sound and music to be used by the application.
     */
    public void initAudioContent() {
        try {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String audioPath = props.getProperty(PathXPropertyType.PATH_AUDIO);

            // LOAD ALL THE AUDIO
            loadAudioCue(PathXPropertyType.AUDIO_CUE_COLLISION);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_FAIL);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_WIN);
            loadAudioCue(PathXPropertyType.SONG_CUE_MENU_SCREEN);
            loadAudioCue(PathXPropertyType.SONG_CUE_GAME_SCREEN);

            // PLAY THE WELCOME SCREEN SONG
            audio.play(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString(), true);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InvalidMidiDataException | MidiUnavailableException e) {
            errorHandler.processError(PathXPropertyType.TEXT_ERROR_LOADING_AUDIO);
        }
    }

    /**
     * This helper method loads the audio file associated with audioCueType,
     * which should have been specified via an XML properties file.
     */
    private void loadAudioCue(PathXPropertyType audioCueType)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException,
            InvalidMidiDataException, MidiUnavailableException {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String audioPath = props.getProperty(PathXPropertyType.PATH_AUDIO);
        String cue = props.getProperty(audioCueType.toString());
        audio.loadAudio(audioCueType.toString(), audioPath + cue);
    }

    /**
     * Initializes the game data used by the application. Note that it is this
     * method's obligation to construct and set this Game's custom GameDataModel
     * object as well as any other needed game objects.
     */
    @Override
    public void initData() {
        // INIT OUR ERROR HANDLER
        errorHandler = new PathXErrorHandler(window);

        // INIT OUR FILE MANAGER
        fileManager = new PathXFileManager((PathXPanel) canvas, (PathXDataModel) data, this);

        LevelIO = new PathXLevelIO(new File(LEVELS_PATH + LEVEL_SCHEMA), this);
        // LOAD THE PLAYER'S RECORD FROM A FILE
        // record = fileManager.loadRecord();

        // INIT OUR DATA MANAGER
        data = new PathXDataModel(this);
    }

    /**
     * Initializes the game controls, like buttons, used by the game
     * application. Note that this includes the tiles, which serve as buttons of
     * sorts.
     */
    @Override
    public void initGUIControls() {
        // WE'LL USE AND REUSE THESE FOR LOADING STUFF
        BufferedImage img;
        float x, y;
        SpriteType sT;
        Sprite s;

        // FIRST PUT THE ICON IN THE WINDOW
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);
        String windowIconFile = props.getProperty(PathXPropertyType.IMAGE_WINDOW_ICON);
        img = loadImage(imgPath + windowIconFile);
        window.setIconImage(img);

        // CONSTRUCT THE PANEL WHERE WE'LL DRAW EVERYTHING
        canvas = new PathXPanel(this, (PathXDataModel) data);

        // LOAD THE BACKGROUNDS, WHICH ARE GUI DECOR
        setCurrentScreenState(MENU_SCREEN_STATE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_MENU));
        sT = new SpriteType(BACKGROUND_TYPE);
        sT.addState(MENU_SCREEN_STATE, img);

        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_LEVEL_SELECT));
        sT.addState(LEVEL_SELECT_SCREEN_STATE, img);

        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_SETTINGS));
        sT.addState(SETTINGS_SCREEN_STATE, img);

        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_GAME));
        sT.addState(GAME_SCREEN_STATE, img);

        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_HELP));
        sT.addState(HELP_SCREEN_STATE, img);

        s = new Sprite(sT, 0, 0, 0, 0, MENU_SCREEN_STATE);
        guiDecor.put(BACKGROUND_TYPE, s);

        // LOAD THE ARROW CURSOR
        String cursorName = props.getProperty(PathXPropertyType.IMAGE_CURSOR_ARROW);
        img = loadImageWithColorKey(imgPath + cursorName, COLOR_KEY);
        Point cursorHotSpot = new Point(0, 0);
        Cursor arrowCursor = Toolkit.getDefaultToolkit().createCustomCursor(img, cursorHotSpot, cursorName);
        window.setCursor(arrowCursor);

        // ADD THE CONTROLS ALONG THE NORTH OF THE GAME SCREEN
        // THEN THE PLAY BUTTON
        String playButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_PLAY);
        sT = new SpriteType(PLAY_BUTTON_TYPE);
        img = loadImage(imgPath + playButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String playMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_PLAY_MOUSE_OVER);
        img = loadImage(imgPath + playMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, PLAY_BUTTON_X, PLAY_BUTTON_Y, 0, 0, PathXState.VISIBLE_STATE.toString());
        guiButtons.put(PLAY_BUTTON_TYPE, s);

        // AND THE RESET BUTTON
        String resetButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_RESET);
        sT = new SpriteType(RESET_SELECT_BUTTON_TYPE);
        img = loadImage(imgPath + resetButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String resetMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_RESET_MOUSE_OVER);
        img = loadImage(imgPath + resetMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, RESET_BUTTON_X, RESET_BUTTON_Y, 0, 0, PathXState.VISIBLE_STATE.toString());
        guiButtons.put(RESET_SELECT_BUTTON_TYPE, s);

        // AND THE SETTINGS BUTTON WITH THE SETTINGS
        String settingButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_SETTINGS);
        sT = new SpriteType(SETTINGS_SELECT_BUTTON_TYPE);
        img = loadImage(imgPath + settingButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String settingMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_SETTINGS_MOUSE_OVER);
        img = loadImage(imgPath + settingMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, SETTINGS_BUTTON_X, SETTINGS_BUTTON_Y, 0, 0, PathXState.VISIBLE_STATE.toString());
        guiButtons.put(SETTINGS_SELECT_BUTTON_TYPE, s);

        String CheckedBoxButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_CHECKEDBOX);
        sT = new SpriteType(CHECKED_MUSIC_BOX_BUTTON_TYPE);
        img = loadImage(imgPath + CheckedBoxButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String CheckedBoxMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_CHECKEDBOX);
        img = loadImage(imgPath + CheckedBoxMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, CHECKED_MUSIC_BOX_X, CHECKED_MUSIC_BOX_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(CHECKED_MUSIC_BOX_BUTTON_TYPE, s);

        String UncheckedBoxButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_UNCHECKEDBOX);
        sT = new SpriteType(UNCHECKED_MUSIC_BOX_BUTTON_TYPE);
        img = loadImage(imgPath + UncheckedBoxButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String UncheckedBoxMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_UNCHECKEDBOX);
        img = loadImage(imgPath + UncheckedBoxMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, UNCHECKED_MUSIC_BOX_X, UNCHECKED_MUSIC_BOX_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(UNCHECKED_MUSIC_BOX_BUTTON_TYPE, s);

        String SoundCheckedBoxButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_CHECKEDBOX);
        sT = new SpriteType(CHECKED_SOUND_BOX_BUTTON_TYPE);
        img = loadImage(imgPath + SoundCheckedBoxButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String SoundCheckedBoxMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_CHECKEDBOX);
        img = loadImage(imgPath + SoundCheckedBoxMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, CHECKED_SOUND_BOX_X, CHECKED_SOUND_BOX_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(CHECKED_SOUND_BOX_BUTTON_TYPE, s);

        String SoundUncheckedBoxButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_UNCHECKEDBOX);
        sT = new SpriteType(UNCHECKED_SOUND_BOX_BUTTON_TYPE);
        img = loadImage(imgPath + SoundUncheckedBoxButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String SoundUncheckedBoxMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_UNCHECKEDBOX);
        img = loadImage(imgPath + SoundUncheckedBoxMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, UNCHECKED_SOUND_BOX_X, UNCHECKED_SOUND_BOX_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(UNCHECKED_SOUND_BOX_BUTTON_TYPE, s);

        // AND THE HELP BUTTON
        String helpButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_HELP);
        sT = new SpriteType(HELP_SELECT_BUTTON_TYPE);
        img = loadImage(imgPath + helpButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String helpMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_HELP_MOUSE_OVER);
        img = loadImage(imgPath + helpMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, HELP_BUTTON_X, HELP_BUTTON_Y, 0, 0, PathXState.VISIBLE_STATE.toString());
        guiButtons.put(HELP_SELECT_BUTTON_TYPE, s);

        // AND THE EXIT BUTTONS
        String closeButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_CLOSE);
        sT = new SpriteType(EXIT_MENU_BUTTON_TYPE);
        img = loadImage(imgPath + closeButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String closeMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_CLOSE_MOUSE_OVER);
        img = loadImage(imgPath + closeMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, CLOSE_BUTTON_MENU_X, CLOSE_BUTTON_MENU_Y, 0, 0, PathXState.VISIBLE_STATE.toString());
        guiButtons.put(EXIT_MENU_BUTTON_TYPE, s);

        String closeGameButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_CLOSE);
        sT = new SpriteType(EXIT_GAME_BUTTON_TYPE);
        img = loadImage(imgPath + closeGameButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String closeGameMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_CLOSE_MOUSE_OVER);
        img = loadImage(imgPath + closeGameMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, CLOSE_BUTTON_GAME_X, CLOSE_BUTTON_GAME_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(EXIT_GAME_BUTTON_TYPE, s);

        // AND THE HOME BUTTONS
        String homeButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_HOME);
        sT = new SpriteType(HOMETOMENU_SELECT_BUTTON_TYPE);
        img = loadImage(imgPath + homeButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String homeMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_HOME_MOUSE_OVER);
        img = loadImage(imgPath + homeMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, HOME_BUTTON_MENU_X, HOME_BUTTON_MENU_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(HOMETOMENU_SELECT_BUTTON_TYPE, s);

        String homeGameButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_HOME);
        sT = new SpriteType(HOMETOLEVEL_SELECT_BUTTON_TYPE);
        img = loadImage(imgPath + homeGameButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String homeGameMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_HOME_MOUSE_OVER);
        img = loadImage(imgPath + homeGameMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, HOME_BUTTON_GAME_X, HOME_BUTTON_GAME_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(HOMETOLEVEL_SELECT_BUTTON_TYPE, s);

        //SCROLL BUTTONS
        String ScrollUpButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_SCROLL_UP);
        sT = new SpriteType(SCROLL_UP_BUTTON_TYPE);
        img = loadImage(imgPath + ScrollUpButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String ScrollUpMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_SCROLL_UP_MOUSE_OVER);
        img = loadImage(imgPath + ScrollUpMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, UP_BUTTON_GAME_X, UP_BUTTON_GAME_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(SCROLL_UP_BUTTON_TYPE, s);

        String ScrollDownButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_SCROLL_DOWN);
        sT = new SpriteType(SCROLL_DOWN_BUTTON_TYPE);
        img = loadImage(imgPath + ScrollDownButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String ScrollDownMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_SCROLL_DOWN_MOUSE_OVER);
        img = loadImage(imgPath + ScrollDownMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, DOWN_BUTTON_GAME_X, DOWN_BUTTON_GAME_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(SCROLL_DOWN_BUTTON_TYPE, s);

        String ScrollLeftButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_SCROLL_LEFT);
        sT = new SpriteType(SCROLL_UP_BUTTON_TYPE);
        img = loadImage(imgPath + ScrollLeftButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String ScrollLeftMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_SCROLL_LEFT_MOUSE_OVER);
        img = loadImage(imgPath + ScrollLeftMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEFT_BUTTON_GAME_X, LEFT_BUTTON_GAME_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(SCROLL_LEFT_BUTTON_TYPE, s);

        String ScrollRightButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_SCROLL_RIGHT);
        sT = new SpriteType(SCROLL_RIGHT_BUTTON_TYPE);
        img = loadImage(imgPath + ScrollRightButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String ScrollRightMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_SCROLL_RIGHT_MOUSE_OVER);
        img = loadImage(imgPath + ScrollRightMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, RIGHT_BUTTON_GAME_X, RIGHT_BUTTON_GAME_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(SCROLL_RIGHT_BUTTON_TYPE, s);

        //LEVEL SELECT BUTTONS
        ArrayList<String> levels = props.getPropertyOptionsList(PathXPropertyType.LEVEL_OPTIONS);
        ArrayList<String> levelImageNames = props.getPropertyOptionsList(PathXPropertyType.LEVEL_IMAGE_OPTIONS);

        for (int i = 0; i < levels.size(); i++) {
            sT = new SpriteType(LOCATION_BUTTON_TYPE);
            img = loadImageWithColorKey(imgPath + levelImageNames.get(i), COLOR_KEY);
            sT.addState(PathXState.VISIBLE_STATE.toString(), img);
            s = new Sprite(sT, VIEWPORT_OFFSET_X + 70 - data.getViewport().getViewportX(),
                    VIEWPORT_OFFSET_Y + 70 - data.getViewport().getViewportY(), 0, 0, PathXState.VISIBLE_STATE.toString());
            guiButtons.put(levels.get(i), s);
        }

        //START BUTTON
        String StartButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_START);
        sT = new SpriteType(START_BUTTON_TYPE);
        img = loadImage(imgPath + StartButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String StartMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_START_MOUSE_OVER);
        img = loadImage(imgPath + StartMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, START_BUTTON_GAME_X, START_BUTTON_GAME_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(START_BUTTON_TYPE, s);

        //PLAY & PAUSE GAME BUTTONS
        String PlayGameButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_PLAY_GAME);
        sT = new SpriteType(PLAY_GAME_BUTTON_TYPE);
        img = loadImage(imgPath + PlayGameButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String PlayGameMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_PLAY_GAME_MOUSE_OVER);
        img = loadImage(imgPath + PlayGameMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, PLAYPAUSE_BUTTON_GAME_X, PLAYPAUSE_BUTTON_GAME_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(PLAY_GAME_BUTTON_TYPE, s);

        String PauseGameButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_PAUSE_GAME);
        sT = new SpriteType(PAUSE_GAME_BUTTON_TYPE);
        img = loadImage(imgPath + PauseGameButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String PauseGameMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_PAUSE_GAME_MOUSE_OVER);
        img = loadImage(imgPath + PauseGameMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, PLAYPAUSE_BUTTON_GAME_X, PLAYPAUSE_BUTTON_GAME_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(PAUSE_GAME_BUTTON_TYPE, s);

        //SPECIALS BUTTONS
        String GreenLightButton = props.getProperty(PathXPropertyType.IMAGE_GREENLIGHT_BUTTON);
        sT = new SpriteType(GREENLIGHT_BUTTON_TYPE);
        img = loadImage(imgPath + GreenLightButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String GreenLightMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_GREENLIGHT_BUTTON);
        img = loadImage(imgPath + GreenLightMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, GREENLIGHT_BUTTON_GAME_X, GREENLIGHT_BUTTON_GAME_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(GREENLIGHT_BUTTON_TYPE, s);

        String RedLightButton = props.getProperty(PathXPropertyType.IMAGE_REDLIGHT_BUTTON);
        sT = new SpriteType(REDLIGHT_BUTTON_TYPE);
        img = loadImage(imgPath + RedLightButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String RedLightMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_REDLIGHT_BUTTON);
        img = loadImage(imgPath + RedLightMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, REDLIGHT_BUTTON_GAME_X, REDLIGHT_BUTTON_GAME_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(REDLIGHT_BUTTON_TYPE, s);
        
        String FlatTireButton = props.getProperty(PathXPropertyType.IMAGE_FLAT_TIRE_BUTTON);
        sT = new SpriteType(FLAT_TIRE_BUTTON_TYPE);
        img = loadImage(imgPath + FlatTireButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String FlatTireMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_FLAT_TIRE_BUTTON);
        img = loadImage(imgPath + FlatTireMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, FLAT_TIRE_BUTTON_GAME_X, FLAT_TIRE_BUTTON_GAME_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(FLAT_TIRE_BUTTON_TYPE, s);

        String EmptyTankButton = props.getProperty(PathXPropertyType.IMAGE_EMPTYTANK_BUTTON);
        sT = new SpriteType(EMPTYTANK_BUTTON_TYPE);
        img = loadImage(imgPath + EmptyTankButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String EmptyTankMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_EMPTYTANK_BUTTON);
        img = loadImage(imgPath + EmptyTankMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, EMPTYTANK_BUTTON_GAME_X, EMPTYTANK_BUTTON_GAME_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(EMPTYTANK_BUTTON_TYPE, s);

        String IncSpeedButton = props.getProperty(PathXPropertyType.IMAGE_INCSPEED_BUTTON);
        sT = new SpriteType(INCSPEED_BUTTON_TYPE);
        img = loadImage(imgPath + IncSpeedButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String IncSpeedMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_INCSPEED_BUTTON);
        img = loadImage(imgPath + IncSpeedMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, INCSPEED_BUTTON_GAME_X, INCSPEED_BUTTON_GAME_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(INCSPEED_BUTTON_TYPE, s);

        String DecSpeedButton = props.getProperty(PathXPropertyType.IMAGE_DECSPEED_BUTTON);
        sT = new SpriteType(DECSPEED_BUTTON_TYPE);
        img = loadImage(imgPath + DecSpeedButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String DecSpeedMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_DECSPEED_BUTTON);
        img = loadImage(imgPath + DecSpeedMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, DECSPEED_BUTTON_GAME_X, DECSPEED_BUTTON_GAME_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(DECSPEED_BUTTON_TYPE, s);

        String IncPlayerSpeedButton = props.getProperty(PathXPropertyType.IMAGE_INCPLAYERSPEED_BUTTON);
        sT = new SpriteType(INCPLAYERSPEED_BUTTON_TYPE);
        img = loadImage(imgPath + IncPlayerSpeedButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String IncPlayerSpeedMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_INCPLAYERSPEED_BUTTON);
        img = loadImage(imgPath + IncPlayerSpeedMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, INCPLAYERSPEED_BUTTON_GAME_X, INCPLAYERSPEED_BUTTON_GAME_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(INCPLAYERSPEED_BUTTON_TYPE, s);

        String CloseRoadButton = props.getProperty(PathXPropertyType.IMAGE_CLOSE_ROAD_BUTTON);
        sT = new SpriteType(CLOSE_ROAD_BUTTON_TYPE);
        img = loadImage(imgPath + CloseRoadButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String CloseRoadMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_CLOSE_ROAD_BUTTON);
        img = loadImage(imgPath + CloseRoadMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, CLOSE_ROAD_BUTTON_GAME_X, CLOSE_ROAD_BUTTON_GAME_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(CLOSE_ROAD_BUTTON_TYPE, s);

        String CloseIntersectionButton = props.getProperty(PathXPropertyType.IMAGE_CLOSE_INTERSECTION_BUTTON);
        sT = new SpriteType(CLOSE_INTERSECTION_BUTTON_TYPE);
        img = loadImage(imgPath + CloseIntersectionButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String CloseIntersectionMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_CLOSE_INTERSECTION_BUTTON);
        img = loadImage(imgPath + CloseIntersectionMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, CLOSE_INTERSECTION_BUTTON_GAME_X, CLOSE_INTERSECTION_BUTTON_GAME_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(CLOSE_INTERSECTION_BUTTON_TYPE, s);

        String OpenIntersectionButton = props.getProperty(PathXPropertyType.IMAGE_OPEN_INTERSECTION_BUTTON);
        sT = new SpriteType(OPEN_INTERSECTION_BUTTON_TYPE);
        img = loadImage(imgPath + OpenIntersectionButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String OpenIntersectionMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_OPEN_INTERSECTION_BUTTON);
        img = loadImage(imgPath + OpenIntersectionMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, OPEN_INTERSECTION_BUTTON_GAME_X, OPEN_INTERSECTION_BUTTON_GAME_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(OPEN_INTERSECTION_BUTTON_TYPE, s);

        String MindControlButton = props.getProperty(PathXPropertyType.IMAGE_MINDCONTROL_BUTTON);
        sT = new SpriteType(MINDCONTROL_BUTTON_TYPE);
        img = loadImage(imgPath + MindControlButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String MindControlMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MINDCONTROL_BUTTON);
        img = loadImage(imgPath + MindControlMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, MINDCONTROL_BUTTON_GAME_X, MINDCONTROL_BUTTON_GAME_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(MINDCONTROL_BUTTON_TYPE, s);
        
        String MindlessTerrorButton = props.getProperty(PathXPropertyType.IMAGE_MINDLESS_TERROR_BUTTON);
        sT = new SpriteType(MINDLESS_TERROR_BUTTON_TYPE);
        img = loadImage(imgPath + MindlessTerrorButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String MindlessTerrorMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MINDLESS_TERROR_BUTTON);
        img = loadImage(imgPath + MindlessTerrorMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, MINDLESS_TERROR_BUTTON_GAME_X, MINDLESS_TERROR_BUTTON_GAME_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(MINDLESS_TERROR_BUTTON_TYPE, s);
        
        String StealButton = props.getProperty(PathXPropertyType.IMAGE_STEAL_BUTTON);
        sT = new SpriteType(STEAL_BUTTON_TYPE);
        img = loadImage(imgPath + StealButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String StealMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_STEAL_BUTTON);
        img = loadImage(imgPath + StealMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, STEAL_BUTTON_GAME_X, STEAL_BUTTON_GAME_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(STEAL_BUTTON_TYPE, s);

        String IntangibilityButton = props.getProperty(PathXPropertyType.IMAGE_INTANGIBILITY_BUTTON);
        sT = new SpriteType(INTANGIBILITY_BUTTON_TYPE);
        img = loadImage(imgPath + IntangibilityButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String IntangibilityMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_INTANGIBILITY_BUTTON);
        img = loadImage(imgPath + IntangibilityMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, INTANGIBILITY_BUTTON_GAME_X, INTANGIBILITY_BUTTON_GAME_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(INTANGIBILITY_BUTTON_TYPE, s);

        String FlyingButton = props.getProperty(PathXPropertyType.IMAGE_FLYING_BUTTON);
        sT = new SpriteType(FLYING_BUTTON_TYPE);
        img = loadImage(imgPath + FlyingButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String FlyingMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_FLYING_BUTTON);
        img = loadImage(imgPath + FlyingMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, FLYING_BUTTON_GAME_X, FLYING_BUTTON_GAME_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(FLYING_BUTTON_TYPE, s);

        String InvincibilityButton = props.getProperty(PathXPropertyType.IMAGE_INVINCIBILITY_BUTTON);
        sT = new SpriteType(INVINCIBILITY_BUTTON_TYPE);
        img = loadImage(imgPath + InvincibilityButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String InvincibilityMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_INVINCIBILITY_BUTTON);
        img = loadImage(imgPath + InvincibilityMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, INVINCIBILITY_BUTTON_GAME_X, INVINCIBILITY_BUTTON_GAME_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(INVINCIBILITY_BUTTON_TYPE, s);

        //MAP VIEWPORT
        String levelMap = props.getProperty(PathXPropertyType.IMAGE_LEVEL_MAP);
        sT = new SpriteType(MAP_TYPE);
        img = loadImageWithColorKey(imgPath + levelMap, COLOR_KEY);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        s = new Sprite(sT, 0, 0, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiDecor.put(MAP_TYPE, s);

        // NOW ADD THE DIALOGS
        // AND THE DIALOGS DISPLAY   
        String infoDialog = props.getProperty(PathXPropertyType.IMAGE_DIALOG_INFO);
        sT = new SpriteType(INFO_DIALOG_TYPE);
        img = loadImage(imgPath + infoDialog);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        s = new Sprite(sT, INFO_DIALOG_X, INFO_DIALOG_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiDialogs.put(INFO_DIALOG_TYPE, s);

        String closeInfoButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_CLOSE);
        sT = new SpriteType(EXIT_INFO_BUTTON_TYPE);
        img = loadImage(imgPath + closeInfoButton);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        String closeInfoMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_CLOSE_MOUSE_OVER);
        img = loadImage(imgPath + closeInfoMouseOverButton);
        sT.addState(PathXState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, CLOSE_BUTTON_INFO_X, CLOSE_BUTTON_INFO_Y, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiButtons.put(EXIT_INFO_BUTTON_TYPE, s);

        //CHARACTER SPRITES
        String playerSprite = props.getProperty(PathXPropertyType.IMAGE_PLAYER_SPRITE);
        sT = new SpriteType(PLAYER_SPRITE_TYPE);
        img = loadImageWithColorKey(imgPath + playerSprite, COLOR_KEY);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        s = new Player(sT, 0, 0, 5, 5, PathXState.INVISIBLE_STATE.toString(), 1, ((PathXDataModel) data).getBalance());

        ((PathXDataModel) data).setPlayer((Player) s);

        guiSprites.put(PLAYER_SPRITE_TYPE, s);

        String zombieSprite = props.getProperty(PathXPropertyType.IMAGE_ZOMBIE_SPRITE);
        sT = new SpriteType(ZOMBIE_SPRITE_TYPE);
        img = loadImageWithColorKey(imgPath + zombieSprite, COLOR_KEY);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        s = new Sprite(sT, 0, 0, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiSprites.put(ZOMBIE_SPRITE_TYPE, s);

        String policeSprite = props.getProperty(PathXPropertyType.IMAGE_POLICE_SPRITE);
        sT = new SpriteType(POLICE_SPRITE_TYPE);
        img = loadImageWithColorKey(imgPath + policeSprite, COLOR_KEY);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        s = new Sprite(sT, 0, 0, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiSprites.put(POLICE_SPRITE_TYPE, s);

        String banditSprite = props.getProperty(PathXPropertyType.IMAGE_BANDIT_SPRITE);
        sT = new SpriteType(BANDIT_SPRITE_TYPE);
        img = loadImageWithColorKey(imgPath + banditSprite, COLOR_KEY);
        sT.addState(PathXState.VISIBLE_STATE.toString(), img);
        s = new Sprite(sT, 0, 0, 0, 0, PathXState.INVISIBLE_STATE.toString());
        guiSprites.put(BANDIT_SPRITE_TYPE, s);

        /*
         // AND THE WIN CONDITION DISPLAY
         String winDisplay = props.getProperty(PathXPropertyType.IMAGE_DIALOG_WIN);
         sT = new SpriteType(WIN_DIALOG_TYPE);
         img = loadImageWithColorKey(imgPath + winDisplay, COLOR_KEY);
         sT.addState(PathXState.VISIBLE_STATE.toString(), img);
         x = (viewport.getScreenWidth()/2) - (img.getWidth(null)/2);
         y = (viewport.getScreenHeight()/2) - (img.getHeight(null)/2);
         s = new Sprite(sT, x, y, 0, 0, PathXState.INVISIBLE_STATE.toString());
         guiDialogs.put(WIN_DIALOG_TYPE, s);
	
         // AND THE LOSE CONDITION DISPLAY
         String loseDisplay = props.getProperty(PathXPropertyType.IMAGE_DIALOG_FAIL);
         sT = new SpriteType(FAIL_DIALOG_TYPE);
         img = loadImageWithColorKey(imgPath + loseDisplay, COLOR_KEY);
         sT.addState(PathXState.VISIBLE_STATE.toString(), img);
         x = (viewport.getScreenWidth()/2) - (img.getWidth(null)/2);
         y = (viewport.getScreenHeight()/2) - (img.getHeight(null)/2);
         s = new Sprite(sT, x, y, 0, 0, PathXState.INVISIBLE_STATE.toString());
         guiDialogs.put(FAIL_DIALOG_TYPE, s);*/
        // THEN THE TILES STACKED TO THE TOP LEFT
        //((SortingHatDataModel)data).initTiles();
    }

    @Override
    public void initGUIHandlers() {

        
        // WE'LL RELAY UI EVENTS TO THIS OBJECT FOR HANDLING
        eventHandler = new PathXEventHandler(this);
        
        specialsHandler = new PathXSpecialsHandler(this);

        // WE'LL HAVE A CUSTOM RESPONSE FOR WHEN THE USER CLOSES THE WINDOW
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                eventHandler.respondToExitRequest();
            }
        });

        // SEND ALL LEVEL SELECTION HANDLING OFF TO THE EVENT HANDLER
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // PLAY GAME EVENT HANDLER
        guiButtons.get(PLAY_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToPlayAgainRequest();
            }
        });

        // RESET BUTTON EVENT HANDLER
        guiButtons.get(RESET_SELECT_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToResetGameRequest();
            }
        });

        // SETTINGS BUTTON EVENT HANDLER
        guiButtons.get(SETTINGS_SELECT_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToSettingsRequest();
            }
        });

        guiButtons.get(CHECKED_MUSIC_BOX_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToUncheckMusicRequest();
            }
        });

        guiButtons.get(UNCHECKED_MUSIC_BOX_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToCheckMusicRequest();
            }
        });

        guiButtons.get(CHECKED_SOUND_BOX_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToUncheckSoundRequest();
            }
        });

        guiButtons.get(UNCHECKED_SOUND_BOX_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToCheckSoundRequest();
            }
        });

        // HELP BUTTON EVENT HANDLER
        guiButtons.get(HELP_SELECT_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToHelpRequest();
            }
        });

        // EXIT MENU & GAME BUTTON EVENT HANDLER
        guiButtons.get(EXIT_MENU_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToExitRequest();
            }
        });
        guiButtons.get(EXIT_GAME_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToExitRequest();
            }
        });
        guiButtons.get(EXIT_INFO_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondtoCloseDialogRequest();
            }
        });

        //LOCATIONS BUTTON HANDLER
        ArrayList<String> levels = props.getPropertyOptionsList(PathXPropertyType.LEVEL_OPTIONS);
        for (int i = 0; i < levels.size(); i++) {
            final String level = levels.get(i);
            guiButtons.get(levels.get(i)).setActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    eventHandler.respondToSelectLevelRequest(level);
                }
            });
        }

        // START GAME EVENT HANDLER
        guiButtons.get(START_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToStartGameRequest();
            }
        });

        // PLAY GAME EVENT HANDLER
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToPlayGameRequest();
            }
        });

        // PAUSE GAME EVENT HANDLER
        guiButtons.get(PAUSE_GAME_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToPauseGameRequest();
                
            }
        });

        // HOME TO MENU & HOME TO LEVEL SELECT BUTTON EVENT HANDLER
        guiButtons.get(HOMETOMENU_SELECT_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToHomeRequest();
            }
        });
        guiButtons.get(HOMETOLEVEL_SELECT_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToPlayAgainRequest();
            }
        });

        // SCROLLING EVENT HANDLER
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToKeyPress(38);
            }
        });
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToKeyPress(40);
            }
        });
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToKeyPress(37);
            }
        });
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToKeyPress(39);
            }
        });
        
        //SPECIALS BUTTONS LISTENERS
        guiButtons.get(GREENLIGHT_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                    specialsHandler.respondToSpecialsKeyPress(71);
            }
        });
        guiButtons.get(REDLIGHT_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                    specialsHandler.respondToSpecialsKeyPress(82);
            }
        });
        guiButtons.get(FLAT_TIRE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                    specialsHandler.respondToSpecialsKeyPress(84);
            }
        });
        guiButtons.get(EMPTYTANK_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                    specialsHandler.respondToSpecialsKeyPress(69);
            }
        });
        guiButtons.get(INCSPEED_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                    specialsHandler.respondToSpecialsKeyPress(88);
            }
        });
        guiButtons.get(DECSPEED_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                    specialsHandler.respondToSpecialsKeyPress(90);
            }
        });
        guiButtons.get(INCPLAYERSPEED_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                    specialsHandler.respondToSpecialsKeyPress(80);
            }
        });
        guiButtons.get(CLOSE_ROAD_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                    specialsHandler.respondToSpecialsKeyPress(72);
            }
        });
        guiButtons.get(CLOSE_INTERSECTION_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                    specialsHandler.respondToSpecialsKeyPress(67);
            }
        });
        guiButtons.get(OPEN_INTERSECTION_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                    specialsHandler.respondToSpecialsKeyPress(79);
            }
        });
        guiButtons.get(MINDCONTROL_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                    specialsHandler.respondToSpecialsKeyPress(77);
            }
        });
        guiButtons.get(MINDLESS_TERROR_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                    specialsHandler.respondToSpecialsKeyPress(76);
            }
        });
        guiButtons.get(STEAL_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                    specialsHandler.respondToSpecialsKeyPress(81);
            }
        });
        guiButtons.get(INTANGIBILITY_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                    specialsHandler.respondToSpecialsKeyPress(66);
            }
        });
        guiButtons.get(FLYING_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                    specialsHandler.respondToSpecialsKeyPress(89);
            }
        });
        guiButtons.get(INVINCIBILITY_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                    specialsHandler.respondToSpecialsKeyPress(86);
            }
        });
        

        // KEY LISTENER - LET'S US PROVIDE CUSTOM RESPONSES
        this.setKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                eventHandler.respondToKeyPress(ke.getKeyCode());
            }
        });
    }

    /**
     * This method switches the application to the game screen, making all the
     * appropriate UI controls visible & invisible.
     */
    public void closeInfoDialog() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        guiDialogs.get(INFO_DIALOG_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiDialogs.get(INFO_DIALOG_TYPE).setEnabled(false);
        guiButtons.get(EXIT_INFO_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(EXIT_INFO_BUTTON_TYPE).setEnabled(false);
        canvas.repaint();
    }

    /**
     * This method switches the application Play/Pause Buttons, making all the
     * appropriate UI controls visible & invisible.
     */
    public void switchToPauseButton() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // CHANGE THE BACKGROUND
        canvas.repaint();

        // ACTIVATE THE TOOLBAR AND ITS CONTROLS
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(PAUSE_GAME_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(PAUSE_GAME_BUTTON_TYPE).setEnabled(true);

    }

    /**
     * This method switches the application Play/Pause Buttons, making all the
     * appropriate UI controls visible & invisible.
     */
    public void switchToPlayButton() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // CHANGE THE BACKGROUND
        canvas.repaint();

        // ACTIVATE THE TOOLBAR AND ITS CONTROLS
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(PAUSE_GAME_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(PAUSE_GAME_BUTTON_TYPE).setEnabled(false);

    }

    /**
     * This method switches the application Check/Uncheck Buttons, making all
     * the appropriate UI controls visible & invisible.
     */
    public void switchToUncheckMusicButton() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // CHANGE THE BACKGROUND
        canvas.repaint();

        // ACTIVATE THE TOOLBAR AND ITS CONTROLS
        guiButtons.get(CHECKED_MUSIC_BOX_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(CHECKED_MUSIC_BOX_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(UNCHECKED_MUSIC_BOX_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(UNCHECKED_MUSIC_BOX_BUTTON_TYPE).setEnabled(true);
    }

    /**
     * This method switches the application Check/Uncheck Buttons, making all
     * the appropriate UI controls visible & invisible.
     */
    public void switchToCheckMusicButton() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // CHANGE THE BACKGROUND
        canvas.repaint();

        // ACTIVATE THE TOOLBAR AND ITS CONTROLS
        guiButtons.get(CHECKED_MUSIC_BOX_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(CHECKED_MUSIC_BOX_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(UNCHECKED_MUSIC_BOX_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(UNCHECKED_MUSIC_BOX_BUTTON_TYPE).setEnabled(false);
    }

    /**
     * This method switches the application Check/Uncheck Buttons, making all
     * the appropriate UI controls visible & invisible.
     */
    public void switchToCheckSoundButton() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // CHANGE THE BACKGROUND
        canvas.repaint();

        // ACTIVATE THE TOOLBAR AND ITS CONTROLS
        guiButtons.get(CHECKED_SOUND_BOX_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(CHECKED_SOUND_BOX_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(UNCHECKED_SOUND_BOX_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(UNCHECKED_SOUND_BOX_BUTTON_TYPE).setEnabled(false);
    }

    /**
     * This method switches the application Check/Uncheck Buttons, making all
     * the appropriate UI controls visible & invisible.
     */
    public void switchToUncheckSoundButton() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // CHANGE THE BACKGROUND
        canvas.repaint();

        // ACTIVATE THE TOOLBAR AND ITS CONTROLS
        guiButtons.get(CHECKED_SOUND_BOX_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(CHECKED_SOUND_BOX_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(UNCHECKED_SOUND_BOX_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(UNCHECKED_SOUND_BOX_BUTTON_TYPE).setEnabled(true);
    }

    /**
     * This method switches the application to the game screen, making all the
     * appropriate UI controls visible & invisible.
     */
    public void switchToGameScreen() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(GAME_SCREEN_STATE);
        canvas.repaint();

        // ACTIVATE THE TOOLBAR AND ITS CONTROLS
        guiButtons.get(PLAY_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RESET_SELECT_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(RESET_SELECT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SETTINGS_SELECT_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_SELECT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(HELP_SELECT_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(HELP_SELECT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(PAUSE_GAME_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(PAUSE_GAME_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(EXIT_MENU_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(EXIT_MENU_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(HOMETOMENU_SELECT_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(HOMETOMENU_SELECT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(HOMETOLEVEL_SELECT_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(HOMETOLEVEL_SELECT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(EXIT_GAME_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(EXIT_GAME_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(START_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(START_BUTTON_TYPE).setEnabled(true);
        guiDialogs.get(INFO_DIALOG_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiDialogs.get(INFO_DIALOG_TYPE).setEnabled(true);
        guiButtons.get(EXIT_INFO_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(EXIT_INFO_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setEnabled(true);

        //ACTIVATE SPECIALS
         String[] specials = {GREENLIGHT_BUTTON_TYPE, REDLIGHT_BUTTON_TYPE, FLAT_TIRE_BUTTON_TYPE, EMPTYTANK_BUTTON_TYPE,   
         INCSPEED_BUTTON_TYPE, DECSPEED_BUTTON_TYPE, INCPLAYERSPEED_BUTTON_TYPE, CLOSE_ROAD_BUTTON_TYPE,  
         CLOSE_INTERSECTION_BUTTON_TYPE, OPEN_INTERSECTION_BUTTON_TYPE, MINDLESS_TERROR_BUTTON_TYPE, MINDCONTROL_BUTTON_TYPE, 
         STEAL_BUTTON_TYPE, INTANGIBILITY_BUTTON_TYPE, FLYING_BUTTON_TYPE, INVINCIBILITY_BUTTON_TYPE};
        
         ArrayList<String> specialsList = new ArrayList();
         for (String special : specials) {
         specialsList.add(special);
         }
        
         ArrayList<String> levels = props.getPropertyOptionsList(PathXPropertyType.LEVEL_OPTIONS);
         for (int i = 0; i < levels.size(); i++)
         {
         if(i < specialsList.size()){
         guiButtons.get(specialsList.get(i)).setState(PathXState.VISIBLE_STATE.toString());
         guiButtons.get(specialsList.get(i)).setEnabled(true);
         }
         }
        // AND CHANGE THE SCREEN STATE
        setCurrentScreenState(GAME_SCREEN_STATE);

        // PLAY THE GAMEPLAY SCREEN SONG
        if (eventHandler.isPlayMusic() == true) {
            audio.stop(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString());
            audio.play(PathXPropertyType.SONG_CUE_GAME_SCREEN.toString(), true);
        }
    }

    /**
     * This method switches the application to the game screen, making all the
     * appropriate UI controls visible & invisible.
     */
    public void switchToLevelSelectScreen() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(LEVEL_SELECT_SCREEN_STATE);
        guiDecor.get(MAP_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiDecor.get(MAP_TYPE).setEnabled(true);
        canvas.repaint();

        // ACTIVATE THE TOOLBAR AND ITS CONTROLS
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(PLAY_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RESET_SELECT_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(RESET_SELECT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(PAUSE_GAME_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(PAUSE_GAME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SETTINGS_SELECT_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_SELECT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(HELP_SELECT_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(HELP_SELECT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(EXIT_MENU_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(EXIT_MENU_BUTTON_TYPE).setEnabled(true);
        guiDialogs.get(INFO_DIALOG_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiDialogs.get(INFO_DIALOG_TYPE).setEnabled(false);
        guiButtons.get(EXIT_INFO_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(EXIT_INFO_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(HOMETOMENU_SELECT_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(HOMETOMENU_SELECT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(HOMETOLEVEL_SELECT_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(HOMETOLEVEL_SELECT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(EXIT_GAME_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(EXIT_GAME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(START_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(START_BUTTON_TYPE).setEnabled(false);
        data.getViewport().setViewportOffset(VIEWPORT_OFFSET_X, VIEWPORT_OFFSET_Y);

        // ACTIVATE THE LEVEL SELECT BUTTONS
        ArrayList<String> levels = props.getPropertyOptionsList(PathXPropertyType.LEVEL_OPTIONS);

        for (int i = 0; i < levels.size(); i++) {
            guiButtons.get(levels.get(i)).setState(PathXState.VISIBLE_STATE.toString());
            guiButtons.get(levels.get(i)).setEnabled(true);
        }
        
        //ACTIVATE SPECIALS
         String[] specials = {GREENLIGHT_BUTTON_TYPE, REDLIGHT_BUTTON_TYPE, FLAT_TIRE_BUTTON_TYPE, EMPTYTANK_BUTTON_TYPE,   
         INCSPEED_BUTTON_TYPE, DECSPEED_BUTTON_TYPE, INCPLAYERSPEED_BUTTON_TYPE, CLOSE_ROAD_BUTTON_TYPE,  
         CLOSE_INTERSECTION_BUTTON_TYPE, OPEN_INTERSECTION_BUTTON_TYPE, MINDLESS_TERROR_BUTTON_TYPE, MINDCONTROL_BUTTON_TYPE, 
         STEAL_BUTTON_TYPE, INTANGIBILITY_BUTTON_TYPE, FLYING_BUTTON_TYPE, INVINCIBILITY_BUTTON_TYPE};
        
         ArrayList<String> specialsList = new ArrayList();
         for (String special : specials) {
         specialsList.add(special);
         }
         
         for (int i = 0; i < levels.size(); i++)
         {
         if(i < specialsList.size()){
         guiButtons.get(specialsList.get(i)).setState(PathXState.INVISIBLE_STATE.toString());
         guiButtons.get(specialsList.get(i)).setEnabled(false);
         }
         }

        // PLAY THE WELCOME SCREEN SONG
        if (getCurrentScreenState().equals(GAME_SCREEN_STATE)
                && eventHandler.isPlayMusic() == true) {
            audio.play(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString(), true);
            audio.stop(PathXPropertyType.SONG_CUE_GAME_SCREEN.toString());
        }

        // AND CHANGE THE SCREEN STATE
        setCurrentScreenState(LEVEL_SELECT_SCREEN_STATE);
    }

    /**
     * This method switches the application to the menu screen, making all the
     * appropriate UI controls visible & invisible.
     */
    public void switchToMenuScreen() {
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(MENU_SCREEN_STATE);
        guiDecor.get(MAP_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiDecor.get(MAP_TYPE).setEnabled(false);
        canvas.repaint();

        // ACTIVATE THE TOOLBAR AND ITS CONTROLS
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(PLAY_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(RESET_SELECT_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(RESET_SELECT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SETTINGS_SELECT_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_SELECT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(HELP_SELECT_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(HELP_SELECT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(EXIT_MENU_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(EXIT_MENU_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(PAUSE_GAME_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(PAUSE_GAME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(HOMETOMENU_SELECT_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(HOMETOMENU_SELECT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(HOMETOLEVEL_SELECT_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(HOMETOLEVEL_SELECT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(EXIT_GAME_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(EXIT_GAME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(START_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(START_BUTTON_TYPE).setEnabled(false);

        guiButtons.get(CHECKED_MUSIC_BOX_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CHECKED_MUSIC_BOX_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(CHECKED_SOUND_BOX_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CHECKED_SOUND_BOX_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(UNCHECKED_MUSIC_BOX_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(UNCHECKED_MUSIC_BOX_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(UNCHECKED_SOUND_BOX_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(UNCHECKED_SOUND_BOX_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());

        // DEACTIVATE THE LEVEL SELECT BUTTONS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> levels = props.getPropertyOptionsList(PathXPropertyType.LEVEL_OPTIONS);

        for (int i = 0; i < levels.size(); i++) {
            guiButtons.get(levels.get(i)).setState(PathXState.INVISIBLE_STATE.toString());
            guiButtons.get(levels.get(i)).setEnabled(false);
        }

        // DEACTIVATE ALL DIALOGS
/*        guiDialogs.get(WIN_DIALOG_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
         guiDialogs.get(FAIL_DIALOG_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
         guiDialogs.get(INFO_DIALOG_TYPE).setState(PathXState.INVISIBLE_STATE.toString());*/
        // MAKE THE CURRENT SCREEN THE MENU SCREEN
        setCurrentScreenState(MENU_SCREEN_STATE);

        // AND UPDATE THE DATA GAME STATE
        data.setGameState(MiniGameState.NOT_STARTED);

        // PLAY THE WELCOME SCREEN SONG
        // audio.play(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString(), true); 
        //audio.stop(PathXPropertyType.SONG_CUE_GAME_SCREEN.toString());
    }

    /**
     * This method switches the application to the menu screen, making all the
     * appropriate UI controls visible & invisible.
     */
    public void switchToSettingsScreen() {
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(SETTINGS_SCREEN_STATE);
        canvas.repaint();

        // ACTIVATE THE TOOLBAR AND ITS CONTROLS
        guiButtons.get(PLAY_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RESET_SELECT_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(RESET_SELECT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SETTINGS_SELECT_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_SELECT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(HELP_SELECT_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(HELP_SELECT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(EXIT_MENU_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(EXIT_MENU_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(HOMETOMENU_SELECT_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(HOMETOMENU_SELECT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(HOMETOLEVEL_SELECT_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(HOMETOLEVEL_SELECT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(EXIT_GAME_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(EXIT_GAME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(PAUSE_GAME_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(PAUSE_GAME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(START_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(START_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setEnabled(false);

        if (eventHandler.isPlayMusic() == true) {
            guiButtons.get(CHECKED_MUSIC_BOX_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
            guiButtons.get(CHECKED_MUSIC_BOX_BUTTON_TYPE).setEnabled(true);
            guiButtons.get(UNCHECKED_MUSIC_BOX_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
            guiButtons.get(UNCHECKED_MUSIC_BOX_BUTTON_TYPE).setEnabled(false);
        } else {
            guiButtons.get(CHECKED_MUSIC_BOX_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
            guiButtons.get(CHECKED_MUSIC_BOX_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(UNCHECKED_MUSIC_BOX_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
            guiButtons.get(UNCHECKED_MUSIC_BOX_BUTTON_TYPE).setEnabled(true);
        }
        if (eventHandler.isPlaySound() == true) {
            guiButtons.get(CHECKED_SOUND_BOX_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
            guiButtons.get(CHECKED_SOUND_BOX_BUTTON_TYPE).setEnabled(true);
            guiButtons.get(UNCHECKED_SOUND_BOX_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
            guiButtons.get(UNCHECKED_SOUND_BOX_BUTTON_TYPE).setEnabled(false);
        } else {
            guiButtons.get(CHECKED_SOUND_BOX_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
            guiButtons.get(CHECKED_SOUND_BOX_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(UNCHECKED_SOUND_BOX_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
            guiButtons.get(UNCHECKED_SOUND_BOX_BUTTON_TYPE).setEnabled(true);
        }

        // MAKE THE CURRENT SCREEN THE MENU SCREEN
        setCurrentScreenState(SETTINGS_SCREEN_STATE);

        // AND UPDATE THE DATA GAME STATE
        data.setGameState(MiniGameState.NOT_STARTED);
    }

    /**
     * This method switches the application to the menu screen, making all the
     * appropriate UI controls visible & invisible.
     */
    public void switchToHelpScreen() {
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(HELP_SCREEN_STATE);
        canvas.repaint();

        // ACTIVATE THE TOOLBAR AND ITS CONTROLS
        guiButtons.get(PLAY_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RESET_SELECT_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(RESET_SELECT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SETTINGS_SELECT_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_SELECT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(HELP_SELECT_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(HELP_SELECT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(EXIT_MENU_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(EXIT_MENU_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(HOMETOMENU_SELECT_BUTTON_TYPE).setState(PathXState.VISIBLE_STATE.toString());
        guiButtons.get(HOMETOMENU_SELECT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(HOMETOLEVEL_SELECT_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(HOMETOLEVEL_SELECT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(EXIT_GAME_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(EXIT_GAME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(PAUSE_GAME_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(PAUSE_GAME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(START_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(START_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setEnabled(false);

        // MAKE THE CURRENT SCREEN THE MENU SCREEN
        setCurrentScreenState(HELP_SCREEN_STATE);

        // AND UPDATE THE DATA GAME STATE
        data.setGameState(MiniGameState.NOT_STARTED);
    }

    /**
     * Invoked when a new game is started, it resets all relevant game data and
     * gui control states.
     */
    @Override
    public void reset() {
        data.reset(this);
    }

    /**
     * Updates the state of all gui controls according to the current game
     * conditions.
     */
    @Override
    public void updateGUI() {

        // GO THROUGH THE VISIBLE BUTTONS TO TRIGGER MOUSE OVERS
        Iterator<Sprite> buttonsIt = guiButtons.values().iterator();
        while (buttonsIt.hasNext()) {
            Sprite button = buttonsIt.next();

            // ARE WE ENTERING A BUTTON?
            if (button.getState().equals(PathXState.VISIBLE_STATE.toString())) {
                if (button.containsPoint(data.getLastMouseX(), data.getLastMouseY())) {
                    button.setState(PathXState.MOUSE_OVER_STATE.toString());
                }
            } // ARE WE EXITING A BUTTON?
            else if (button.getState().equals(PathXState.MOUSE_OVER_STATE.toString())) {
                if (!button.containsPoint(data.getLastMouseX(), data.getLastMouseY())) {
                    button.setState(PathXState.VISIBLE_STATE.toString());
                }
            }
        }
    }

    /**
     * @return the currentScreenState
     */
    public String getCurrentScreenState() {
        return currentScreenState;
    }

    /**
     * @return the LevelIO
     */
    public PathXLevelIO getLevelIO() {
        return LevelIO;
    }

    /**
     * @param currentScreenState the currentScreenState to set
     */
    public void setCurrentScreenState(String currentScreenState) {
        this.currentScreenState = currentScreenState;
    }
}
