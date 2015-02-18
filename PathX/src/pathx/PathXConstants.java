package pathx;

import java.awt.Color;
import java.awt.Font;

/**
 * This class stores all the constants used by The PathX application. We'll
 * do this here rather than load them from files because many of these are
 * derived from each other.
 * 
 * @author Brian Sabzjadid
 */
public class PathXConstants {

    public static String PROPERTY_TYPES_LIST = "property_types.txt";
    public static String PROPERTIES_FILE_NAME = "properties.xml";
    public static String PROPERTIES_SCHEMA_FILE_NAME = "properties_schema.xsd";
    public static String PATH_DATA = "./data/";
    
    // LEVEL PATHS
    public static final String  DATA_PATH               = "data/";
    public static final String  LEVELS_PATH             = DATA_PATH + "levels/";

    // THESE ARE THE TYPES OF CONTROLS, WE USE THESE CONSTANTS BECAUSE WE'LL
    // STORE THEM BY TYPE, SO THESE WILL PROVIDE A MEANS OF IDENTIFYING THEM
    // EACH SCREEN HAS ITS OWN BACKGROUND TYPE
    public static final String BACKGROUND_TYPE = "BACKGROUND_TYPE";
    public static final String MENU_BACKGROUND_TYPE = "MENU_BACKGROUND_TYPE";
    public static final String LEVEL_SELECT_BACKGROUND_TYPE = "LEVEL_SELECT_BACKGROUND_TYPE";
    public static final String HELP_BACKGROUND_TYPE = "HELP_BACKGROUND_TYPE";
    public static final String SETTINGS_BACKGROUND_TYPE = "SETTINGS_BACKGROUND_TYPE";
    public static final String MAP_TYPE = "MAP_TYPE";

    
    // THIS REPRESENTS THE BUTTONS ON THE MENU SCREEN FOR LEVEL SELECTION
    public static final String PLAY_BUTTON_TYPE = "PLAY_BUTTON_TYPE";
    public static final String SETTINGS_SELECT_BUTTON_TYPE = "SETTINGS_SELECT_BUTTON_TYPE";
    public static final String RESET_SELECT_BUTTON_TYPE = "RESET_SELECT_BUTTON_TYPE";
    public static final String HELP_SELECT_BUTTON_TYPE = "HELP_SELECT_BUTTON_TYPE";
    public static final String EXIT_MENU_BUTTON_TYPE = "EXIT_MENU_BUTTON_TYPE";
    public static final String EXIT_GAME_BUTTON_TYPE = "EXIT_GAME_BUTTON_TYPE";
    public static final String EXIT_INFO_BUTTON_TYPE = "EXIT_INFO_BUTTON_TYPE";
    public static final String HOMETOMENU_SELECT_BUTTON_TYPE = "HOMETOMENU_SELECT_BUTTON_TYPE";
    public static final String HOMETOLEVEL_SELECT_BUTTON_TYPE = "HOMETOLEVEL_SELECT_BUTTON_TYPE";
    public static final String START_BUTTON_TYPE = "START_BUTTON_TYPE";
    public static final String SCROLL_UP_BUTTON_TYPE = "SCROLL_UP_BUTTON_TYPE";
    public static final String SCROLL_DOWN_BUTTON_TYPE = "SCROLL_DOWN_BUTTON_TYPE";
    public static final String SCROLL_RIGHT_BUTTON_TYPE = "SCROLL_RIGHT_BUTTON_TYPE";
    public static final String SCROLL_LEFT_BUTTON_TYPE = "SCROLL_LEFT_BUTTON_TYPE";
    public static final String PLAY_GAME_BUTTON_TYPE = "PLAY_GAME_BUTTON_TYPE";
    public static final String PAUSE_GAME_BUTTON_TYPE = "PAUSE_GAME_BUTTON_TYPE";
    public static final String LOCATION_BUTTON_TYPE = "LOCATION_BUTTON_TYPE";
    public static final String LOCATION_LOCKED_BUTTON_TYPE = "LOCATION_LOCKED_BUTTON_TYPE";
    public static final String LOCATION_COMPLETE_BUTTON_TYPE = "SCROLL_LEFT_BUTTON_TYPE";
    public static final String CHECKED_MUSIC_BOX_BUTTON_TYPE = "CHECKED_MUSIC_BOX_BUTTON_TYPE";
    public static final String UNCHECKED_MUSIC_BOX_BUTTON_TYPE = "UNCHECKED_MUSIC_BOX_BUTTON_TYPE";
    public static final String CHECKED_SOUND_BOX_BUTTON_TYPE = "CHECKED_SOUND_BOX_BUTTON_TYPE";
    public static final String UNCHECKED_SOUND_BOX_BUTTON_TYPE = "UNCHECKED_SOUND_BOX_BUTTON_TYPE";
    
    
    
    // IN-GAME UI CONTROL TYPES
    public static final String GREENLIGHT_BUTTON_TYPE = "GREENLIGHT_BUTTON_TYPE";
    public static final String REDLIGHT_BUTTON_TYPE = "REDLIGHT_BUTTON_TYPE";
    public static final String FLAT_TIRE_BUTTON_TYPE = "FLAT_TIRE_BUTTON_TYPE";
    public static final String EMPTYTANK_BUTTON_TYPE = "EMPTYTANK_BUTTON_TYPE";
    public static final String INCSPEED_BUTTON_TYPE = "INCSPEED_BUTTON_TYPE";
    public static final String DECSPEED_BUTTON_TYPE = "DECSPEED_BUTTON_TYPE";
    public static final String INCPLAYERSPEED_BUTTON_TYPE = "INCPLAYERSPEED_BUTTON_TYPE";
    public static final String CLOSE_ROAD_BUTTON_TYPE = "CLOSE_ROAD_BUTTON_TYPE";
    public static final String CLOSE_INTERSECTION_BUTTON_TYPE = "CLOSE_INTERSECTION_BUTTON_TYPE";
    public static final String OPEN_INTERSECTION_BUTTON_TYPE = "OPEN_INTERSECTION_BUTTON_TYPE";
    public static final String MINDCONTROL_BUTTON_TYPE = "MINDCONTROL_BUTTON_TYPE";
    public static final String MINDLESS_TERROR_BUTTON_TYPE = "MINDLESS_TERROR_BUTTON_TYPE";
    public static final String STEAL_BUTTON_TYPE = "STEAL_BUTTON_TYPE";
    public static final String INTANGIBILITY_BUTTON_TYPE = "INTANGIBILITY_BUTTON_TYPE";
    public static final String FLYING_BUTTON_TYPE = "FLYING_BUTTON_TYPE";
    public static final String INVINCIBILITY_BUTTON_TYPE = "INVINCIBILITY_BUTTON_TYPE";
    
    //CHARACTER SPRITE TYPES
    public static final String PLAYER_SPRITE_TYPE = "PLAYER_SPRITE_TYPE";
    public static final String POLICE_SPRITE_TYPE = "POLICE_SPRITE_TYPE";
    public static final String ZOMBIE_SPRITE_TYPE = "ZOMBIE_SPRITE_TYPE";
    public static final String BANDIT_SPRITE_TYPE = "BANDIT_SPRITE_TYPE";
    
    //IDK OTHER STUFF
    public static final String BACK_BUTTON_TYPE = "BACK_BUTTON_TYPE";
    public static final String MISCASTS_COUNT_TYPE = "TILE_COUNT_TYPE";
    public static final String BALANCE_TYPE = "BALANCE_TYPE";
    public static final String GOAL_TYPE = "GOAL_TYPE";
    public static final String SETTINGS_TYPE = "SETTINGS_TYPE";
    public static final String TILE_BACKGROUND_TYPE = "TILE_BACKGROUND_TYPE";
    public static final String ALGORITHM_TYPE = "ALGORITHM_TYPE";

    // DIALOG TYPES
    public static final String WIN_DIALOG_TYPE = "WIN_DIALOG_TYPE";
    public static final String INFO_DIALOG_TYPE = "INFO_DIALOG_TYPE";
    public static final String FAIL_DIALOG_TYPE = "FAIL_DIALOG_TYPE";

    // WE'LL USE THESE STATES TO CONTROL SWITCHING BETWEEN THE TWO
    public static final String MENU_SCREEN_STATE = "MENU_SCREEN_STATE";
    public static final String LEVEL_SELECT_SCREEN_STATE = "LEVEL_SELECT_SCREEN_STATE";
    public static final String GAME_SCREEN_STATE = "GAME_SCREEN_STATE";
    public static final String SETTINGS_SCREEN_STATE = "SETTINGS_SCREEN_STATE";
    public static final String HELP_SCREEN_STATE = "HELP_SCREEN_STATE";
    
    // ANIMATION SPEED
    public static final int FPS = 30;

    // UI CONTROL SIZE AND POSITION SETTINGS
    public static final int WINDOW_WIDTH = 640;
    public static final int WINDOW_HEIGHT = 500;
    public static final int VIEWPORT_WIDTH = 620;
    public static final int VIEWPORT_HEIGHT = 380;
    public static final int VIEWPORT_GAME_WIDTH = 627;
    public static final int VIEWPORT_GAME_HEIGHT = 465;
    public static final int GAME_LEVEL_OFFSET_X = 135;
    public static final int GAME_LEVEL_OFFSET_Y = 5;
    public static final int GAME_LEVEL_HEIGHT = 700;
    public static final int GAME_LEVEL_WIDTH = 1230;
    public static final int VIEWPORT_MARGIN_LEFT = 20;
    public static final int VIEWPORT_MARGIN_RIGHT = 20;
    public static final int VIEWPORT_MARGIN_TOP = 20;
    public static final int VIEWPORT_MARGIN_BOTTOM = 20;
    public static final int VIEWPORT_OFFSET_X = 5;
    public static final int VIEWPORT_OFFSET_Y = 100;
    public static final int VIEWPORT_GAME_OFFSET_X = 135;
    public static final int VIEWPORT_GAME_OFFSET_Y = 10;
    public static final int MAP_WIDTH = 1122;
    public static final int MAP_HEIGHT = 550;
    public static final int LEVEL_BUTTON_WIDTH = 200;
    public static final int LEVEL_BUTTON_MARGIN = 5;
    public static final int LEVEL_BUTTON_Y = 280;
    public static final int VIEWPORT_INC = 10;

    // FOR TILE RENDERING
    public static final int NUM_TILES = 30;
    public static final int TILE_WIDTH = 135;
    public static final int TILE_HEIGHT = 126;
    public static final int TILE_IMAGE_OFFSET_X = 45;
    public static final int TILE_IMAGE_OFFSET_Y = 30;
    public static final String TILE_SPRITE_TYPE_PREFIX = "TILE_";
    public static final int COLOR_INC = 10;

    // FOR MOVING TILES AROUND
    public static final int MAX_TILE_VELOCITY = 20;

    // UI CONTROLS POSITIONS IN THE GAME SCREEN
    public static final int NORTH_PANEL_HEIGHT = 130;
    public static final int CONTROLS_MARGIN = 0;
    public static final int GAME_CONTROLS_MARGIN = 0;
    public static final int PLAY_BUTTON_X = 70;
    public static final int PLAY_BUTTON_Y = 380;
    public static final int RESET_BUTTON_X = PLAY_BUTTON_X + 130 + CONTROLS_MARGIN;
    public static final int RESET_BUTTON_Y = 380;
    public static final int SETTINGS_BUTTON_X = RESET_BUTTON_X + 130 + CONTROLS_MARGIN;
    public static final int SETTINGS_BUTTON_Y = 380;
    public static final int HELP_BUTTON_X = SETTINGS_BUTTON_X + 130 + CONTROLS_MARGIN;
    public static final int HELP_BUTTON_Y = 380;
    public static final int CLOSE_BUTTON_MENU_X = 590;
    public static final int CLOSE_BUTTON_MENU_Y = 0;
    public static final int HOME_BUTTON_MENU_X = 540;
    public static final int HOME_BUTTON_MENU_Y = 0;
    public static final int CLOSE_BUTTON_GAME_X = 80;
    public static final int CLOSE_BUTTON_GAME_Y = 100;
    public static final int CLOSE_BUTTON_INFO_X = 435;
    public static final int CLOSE_BUTTON_INFO_Y = 70;
    public static final int INFO_DIALOG_X = 220;
    public static final int INFO_DIALOG_Y = 50;
    public static final int HOME_BUTTON_GAME_X = 30;
    public static final int HOME_BUTTON_GAME_Y = 100;
    public static final int START_BUTTON_GAME_X = 13;
    public static final int START_BUTTON_GAME_Y = 157;
    public static final int PLAYPAUSE_BUTTON_GAME_X = 52;
    public static final int PLAYPAUSE_BUTTON_GAME_Y = 387;
    public static final int LEFT_BUTTON_GAME_X = 25;
    public static final int LEFT_BUTTON_GAME_Y = 390;
    public static final int UP_BUTTON_GAME_X = LEFT_BUTTON_GAME_X + 30;
    public static final int UP_BUTTON_GAME_Y = 360;
    public static final int DOWN_BUTTON_GAME_X = LEFT_BUTTON_GAME_X + 30 + GAME_CONTROLS_MARGIN;
    public static final int DOWN_BUTTON_GAME_Y = 430;
    public static final int RIGHT_BUTTON_GAME_X = DOWN_BUTTON_GAME_X + 40 + GAME_CONTROLS_MARGIN;
    public static final int RIGHT_BUTTON_GAME_Y = 390;
    
    public static final int CHECKED_MUSIC_BOX_X = 190;
    public static final int CHECKED_MUSIC_BOX_Y = 230;
    public static final int UNCHECKED_MUSIC_BOX_X = 190;
    public static final int UNCHECKED_MUSIC_BOX_Y = 230;
    public static final int CHECKED_SOUND_BOX_X = 190;
    public static final int CHECKED_SOUND_BOX_Y = 180;
    public static final int UNCHECKED_SOUND_BOX_X = 190;
    public static final int UNCHECKED_SOUND_BOX_Y = 180;
    
    //SPECIALS LOCATON
    public static final int SPECIAL_BUTTON_OFFSET_X = 30;
    public static final int SPECIAL_BUTTON_OFFSET_Y = 35;
    
    public static final int GREENLIGHT_BUTTON_GAME_X = 15;
    public static final int GREENLIGHT_BUTTON_GAME_Y = 210;
    public static final int REDLIGHT_BUTTON_GAME_X = GREENLIGHT_BUTTON_GAME_X + SPECIAL_BUTTON_OFFSET_X;
    public static final int REDLIGHT_BUTTON_GAME_Y = 210;
    public static final int FLAT_TIRE_BUTTON_GAME_X = REDLIGHT_BUTTON_GAME_X + SPECIAL_BUTTON_OFFSET_X;
    public static final int FLAT_TIRE_BUTTON_GAME_Y = 210;
    public static final int EMPTYTANK_BUTTON_GAME_X = FLAT_TIRE_BUTTON_GAME_X + SPECIAL_BUTTON_OFFSET_X;
    public static final int EMPTYTANK_BUTTON_GAME_Y = 210;
    public static final int INCSPEED_BUTTON_GAME_X = 15;
    public static final int INCSPEED_BUTTON_GAME_Y = EMPTYTANK_BUTTON_GAME_Y + SPECIAL_BUTTON_OFFSET_Y;
    public static final int DECSPEED_BUTTON_GAME_X = INCSPEED_BUTTON_GAME_X + SPECIAL_BUTTON_OFFSET_X;
    public static final int DECSPEED_BUTTON_GAME_Y = INCSPEED_BUTTON_GAME_Y;
    public static final int INCPLAYERSPEED_BUTTON_GAME_X = DECSPEED_BUTTON_GAME_X + SPECIAL_BUTTON_OFFSET_X;
    public static final int INCPLAYERSPEED_BUTTON_GAME_Y = INCSPEED_BUTTON_GAME_Y;
    public static final int CLOSE_ROAD_BUTTON_GAME_X = INCPLAYERSPEED_BUTTON_GAME_X + SPECIAL_BUTTON_OFFSET_X;
    public static final int CLOSE_ROAD_BUTTON_GAME_Y = INCSPEED_BUTTON_GAME_Y;
    public static final int CLOSE_INTERSECTION_BUTTON_GAME_X = 15;
    public static final int CLOSE_INTERSECTION_BUTTON_GAME_Y = INCSPEED_BUTTON_GAME_Y + SPECIAL_BUTTON_OFFSET_Y;
    public static final int OPEN_INTERSECTION_BUTTON_GAME_X = CLOSE_INTERSECTION_BUTTON_GAME_X + SPECIAL_BUTTON_OFFSET_X;
    public static final int OPEN_INTERSECTION_BUTTON_GAME_Y = CLOSE_INTERSECTION_BUTTON_GAME_Y;
    public static final int MINDCONTROL_BUTTON_GAME_X = OPEN_INTERSECTION_BUTTON_GAME_X + SPECIAL_BUTTON_OFFSET_X;
    public static final int MINDCONTROL_BUTTON_GAME_Y = CLOSE_INTERSECTION_BUTTON_GAME_Y;
    public static final int MINDLESS_TERROR_BUTTON_GAME_X = MINDCONTROL_BUTTON_GAME_X + SPECIAL_BUTTON_OFFSET_X;
    public static final int MINDLESS_TERROR_BUTTON_GAME_Y = CLOSE_INTERSECTION_BUTTON_GAME_Y;
    public static final int STEAL_BUTTON_GAME_X = 15;
    public static final int STEAL_BUTTON_GAME_Y = CLOSE_INTERSECTION_BUTTON_GAME_Y + SPECIAL_BUTTON_OFFSET_Y;
    public static final int INTANGIBILITY_BUTTON_GAME_X = STEAL_BUTTON_GAME_X + SPECIAL_BUTTON_OFFSET_X;
    public static final int INTANGIBILITY_BUTTON_GAME_Y = STEAL_BUTTON_GAME_Y;
    public static final int FLYING_BUTTON_GAME_X = INTANGIBILITY_BUTTON_GAME_X + SPECIAL_BUTTON_OFFSET_X;
    public static final int FLYING_BUTTON_GAME_Y = STEAL_BUTTON_GAME_Y;
    public static final int INVINCIBILITY_BUTTON_GAME_X = FLYING_BUTTON_GAME_X + SPECIAL_BUTTON_OFFSET_X;
    public static final int INVINCIBILITY_BUTTON_GAME_Y = STEAL_BUTTON_GAME_Y;

    //LOCATIONS
    public static final int CALI1_X = 100;
    public static final int CALI1_Y = 450;
    public static final int CALI2_X = 25;
    public static final int CALI2_Y = 300;
    public static final int CALI3_X = 70;
    public static final int CALI3_Y = 410;
    
    public static final int SILICON1_X = 30;
    public static final int SILICON1_Y = 350;
    public static final int SILICON2_X = 53;
    public static final int SILICON2_Y = 342;
    public static final int SILICON3_X = 43;
    public static final int SILICON3_Y = 320;
    
    public static final int NEVADA1_X = 155;
    public static final int NEVADA1_Y = 400;
    public static final int NEVADA2_X = 145;
    public static final int NEVADA2_Y = 385;
    public static final int NEVADA3_X = 170;
    public static final int NEVADA3_Y = 385;
    
    public static final int WEST1_X = 470;
    public static final int WEST1_Y = 385;
    public static final int WEST2_X = 635;
    public static final int WEST2_Y = 385;
    public static final int WEST3_X = 545;
    public static final int WEST3_Y = 430;
    
    public static final int FLORIDA1_X = 900;
    public static final int FLORIDA1_Y = 580;
    public static final int FLORIDA2_X = 931;
    public static final int FLORIDA2_Y = 570;
    public static final int FLORIDA3_X = 955;
    public static final int FLORIDA3_Y = 610;
    
    public static final int I951_X = 800;
    public static final int I951_Y = 400;
    public static final int I952_X = 840;
    public static final int I952_Y = 420;
    public static final int I953_X = 780;
    public static final int I953_Y = 389;
    
    public static final int WALLSTREET1_X = 1030;
    public static final int WALLSTREET1_Y = 278;
    public static final int WALLSTREET2_X = 1017;
    public static final int WALLSTREET2_Y = 265;
    public static final int WALLSTREET3_X = 1000;
    public static final int WALLSTREET3_Y = 267;
    
    

    /* USE LATER
    public static final int TILE_COUNT_OFFSET = 145;
    public static final int TILE_TEXT_OFFSET = 60;
    public static final int TIME_X = TILE_COUNT_X + 232 + CONTROLS_MARGIN;
    public static final int TIME_Y = 0;
    public static final int TIME_OFFSET = 130;
    public static final int TIME_TEXT_OFFSET = 55;
    public static final int STATS_X = TIME_X + 310 + CONTROLS_MARGIN;
    public static final int STATS_Y = 0;
    public static final int UNDO_BUTTON_X = STATS_X + 160 + CONTROLS_MARGIN;
    public static final int UNDO_BUTTON_Y = 0;
    public static final int TEMP_TILE_X = UNDO_BUTTON_X + 130 + CONTROLS_MARGIN;
    public static final int TEMP_TILE_Y = 0;
    public static final int TEMP_TILE_OFFSET_X = 20;
    public static final int TEMP_TILE_OFFSET_Y = 10;
    public static final int TEMP_TILE_OFFSET2 = 35;*/

    // STATS DIALOG COORDINATES
    public static final int STATS_LEVEL_INC_Y = 30;
    public static final int STATS_LEVEL_X = 460;
    public static final int STATS_LEVEL_Y = 300;
    public static final int STATS_ALGORITHM_Y = STATS_LEVEL_Y + (STATS_LEVEL_INC_Y * 2);
    public static final int STATS_GAMES_Y = STATS_ALGORITHM_Y + STATS_LEVEL_INC_Y;
    public static final int STATS_WINS_Y = STATS_GAMES_Y + STATS_LEVEL_INC_Y;
    public static final int STATS_PERFECT_WINS_Y = STATS_WINS_Y + STATS_LEVEL_INC_Y;
    public static final int STATS_FASTEST_PERFECT_WIN_Y = STATS_PERFECT_WINS_Y + STATS_LEVEL_INC_Y;

    // THESE ARE USED FOR FORMATTING THE TIME OF GAME
    public static final long MILLIS_IN_A_SECOND = 1000;
    public static final long MILLIS_IN_A_MINUTE = 1000 * 60;
    public static final long MILLIS_IN_AN_HOUR = 1000 * 60 * 60;

    // USED FOR DOING OUR VICTORY ANIMATION
    public static final int WIN_PATH_NODES = 5;
    public static final int WIN_PATH_TOLERANCE = 50;
    public static final int WIN_PATH_COORD = 200;

    // COLORS USED FOR RENDERING VARIOUS THINGS, INCLUDING THE
    // COLOR KEY, WHICH REFERS TO THE COLOR TO IGNORE WHEN
    // LOADING ART.
    public static final Color COLOR_KEY = new Color(255, 174, 201);
    public static final Color COLOR_DEBUG_TEXT = Color.BLACK;
    public static final Color COLOR_TEXT_DISPLAY = new Color(10, 160, 10);
    public static final Color COLOR_STATS = new Color(0, 60, 0);
    public static final Color COLOR_ALGORITHM_HEADER = Color.WHITE;

    // FONTS USED DURING FOR TEXTUAL GAME DISPLAYS
    public static final Font FONT_TEXT_DISPLAY = new Font(Font.SANS_SERIF, Font.BOLD, 40);
    public static final Font FONT_ALG_DISPLAY = new Font(Font.SANS_SERIF, Font.BOLD, 18);
    public static final Font FONT_DEBUG_TEXT = new Font(Font.MONOSPACED, Font.BOLD, 14);
    public static final Font FONT_STATS = new Font(Font.SANS_SERIF, Font.BOLD, 20);
    
    // FOR LOADING STUFF FROM OUR LEVEL XML FILES    
    // THIS IS THE NAME OF THE SCHEMA
    public static final String  LEVEL_SCHEMA = "PathXLevelSchema.xsd";
    
    // CONSTANTS FOR LOADING DATA FROM THE XML FILES
    // THESE ARE THE XML NODES
    public static final String LEVEL_NODE = "level";
    public static final String INTERSECTIONS_NODE = "intersections";
    public static final String INTERSECTION_NODE = "intersection";
    public static final String ROADS_NODE = "roads";
    public static final String ROAD_NODE = "road";
    public static final String START_INTERSECTION_NODE = "start_intersection";
    public static final String DESTINATION_INTERSECTION_NODE = "destination_intersection";
    public static final String MONEY_NODE = "money";
    public static final String POLICE_NODE = "police";
    public static final String BANDITS_NODE = "bandits";
    public static final String ZOMBIES_NODE = "zombies";

    // AND THE ATTRIBUTES FOR THOSE NODES
    public static final String NAME_ATT = "name";
    public static final String IMAGE_ATT = "image";
    public static final String ID_ATT = "id";
    public static final String X_ATT = "x";
    public static final String Y_ATT = "y";
    public static final String OPEN_ATT = "open";
    public static final String INT_ID1_ATT = "int_id1";
    public static final String INT_ID2_ATT = "int_id2";
    public static final String SPEED_LIMIT_ATT = "speed_limit";
    public static final String ONE_WAY_ATT = "one_way";
    public static final String AMOUNT_ATT = "amount";
    public static final String NUM_ATT = "num";
    
    // FOR NICELY FORMATTED XML OUTPUT
    public static final String XML_INDENT_PROPERTY = "{http://xml.apache.org/xslt}indent-amount";
    public static final String XML_INDENT_VALUE = "5";
    public static final String YES_VALUE = "Yes";
    
    // AND FOR THE ROAD SPEED LIMITS
    public static final int DEFAULT_SPEED_LIMIT = 30;
    public static final int MIN_SPEED_LIMIT = 10;
    public static final int MAX_SPEED_LIMIT = 100;
    public static final int SPEED_LIMIT_STEP = 10;
    
    // RENDERING SETTINGS
    public static final int INTERSECTION_RADIUS = 20;
    public static final int INT_STROKE = 3;
    public static final int ONE_WAY_TRIANGLE_HEIGHT = 40;
    public static final int ONE_WAY_TRIANGLE_WIDTH = 60;
    
    // DEFAULT COLORS
    public static final Color   INT_OUTLINE_COLOR   = Color.BLACK;
    public static final Color   HIGHLIGHTED_COLOR = Color.YELLOW;
    public static final Color   OPEN_INT_COLOR      = Color.GREEN;
    public static final Color   CLOSED_INT_COLOR    = Color.RED;
}
