package pathx.data;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Line2D;
import pathx.data.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import pathx.PathX.PathXPropertyType;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import mini_game.SpriteType;
import mini_game.Sprite;
import mini_game.Viewport;
import properties_manager.PropertiesManager;
import static pathx.PathXConstants.*;
import pathx.ui.PathXMiniGame;
import pathx.ui.PathXPanel;
import pathx.ui.PathXSpecialState;
import pathx.ui.PathXState;

/**
 * This class manages the game data for PathX.
 * 
 * @author Brian Sabz
 */
public class PathXDataModel extends MiniGameDataModel{

    // THIS CLASS HAS A REFERERENCE TO THE MINI GAME SO THAT IT
    // CAN NOTIFY IT TO UPDATE THE DISPLAY WHEN THE DATA MODEL CHANGES
    private MiniGame miniGame;
    private PathXMiniGame mG;
    
    // THIS STORES THE TILES ON THE GRID DURING THE GAME
    private Player player;
    
    // USED TO MANAGE WHAT THE USER IS CURRENTLY EDITING
    PathXSpecialState specialMode;
    
    // GAME GRID AND TILE DATA
    private int numGameGridColumns;
    private int numGameGridRows;
    
     // THESE ARE USED FOR TIMING THE GAME
    private GregorianCalendar startTime;
    private GregorianCalendar endTime;

    // LEVEL
    private String currentLevel;
    
    // THIS IS THE LEVEL CURRENTLY BEING EDITING
    private PathXLevel level;

    // DATA FOR RENDERING
   // Viewport viewport;
    
    // WE ONLY NEED TO TURN THIS ON ONCE
    boolean levelBeingEdited;
    private Image backgroundImage;
    private Image startingLocationImage;
    private Image destinationImage;

    // THE SELECTED INTERSECTION OR ROAD MIGHT BE EDITED OR DELETED
    // AND IS RENDERED DIFFERENTLY
    private Intersection selectedIntersection;
    private Road selectedRoad;
    
    // WE'LL USE THIS WHEN WE'RE ADDING A NEW ROAD
    private Intersection startRoadIntersection;
    
    // IN CASE WE WANT TO TRACK MOVEMENTS
    int lastMouseLocX;
    int lastMouseLocY;    
    
    // THESE BOOLEANS HELP US KEEP TRACK OF
    // @todo DO WE NEED THESE?
    boolean isMousePressed;
    boolean isDragging;
    boolean dataUpdatedSinceLastSave;

//    // THIS IS THE UI, WE'LL NOTIFY IT WHENEVER THE DATA CHANGES SO
//    // THAT THE UI RENDERING CAN BE UPDATED AT THAT TIME
//    PathXPanel view;
//    
    //LEVEL SELECT ATTRIBUTES
    private int balance;
    private int goal;
    private int [] moneyEarned = {100,120,140,160,180,200,220,240,260,280,300,320,340,360,380,400,420,440,460,480,500};
    private String[] levelName = {"San Diego, Cali", "San Francisco, Cali", "Los Angeles, Cali",
                                  "Silicon Valley, Cali", "Oracle, Cali", "Google, Cali",
                                  "Bellagio, Vegas", "MGM, Vegas", "Cosmopolitan, Vegas",
                                  "Dodge City, Kansas", "Springfield, Missouri", "Okc, Oklahoma",
                                  "Tampa, Florida", "Orlando, Florida", "Miami, Florida",
                                  "South of the Border", "Congress", "Fort Knox",
                                  "Long Island, NY", "5th Ave, NY", "Wall Street, NY"};
    
    
    /**
     * Constructor for initializing this data model, it will create the data
     * structures for storing tiles, but not the tile grid itself, that is
     * dependent on file loading, and so should be subsequently initialized.
     *
     * @param initMiniGame PathX game UI.
     */
    public PathXDataModel(MiniGame initMiniGame)
    {
        // KEEP THE GAME FOR LATER
        miniGame = initMiniGame;
        mG = (PathXMiniGame)initMiniGame;
        balance = 0;
        goal = 10000;
        level = new PathXLevel();
        startRoadIntersection = null;
        specialMode = PathXSpecialState.NOTHING_SELECTED;
        levelBeingEdited = false;
        /*levelName = new ArrayList<String>();
        moneyEarned = new ArrayList<String>();*/
    }
    
     // ACCESSOR METHODS
     public int getNumGameGridColumns()
    {   return numGameGridColumns;  }
    public int getNumGameGridRows()
    {   return numGameGridRows; }
    public String getCurrentLevel()
    {   return currentLevel;    }
    
    
    
    // MUTATOR METHODS
    public void setCurrentLevel(String initCurrentLevel)
    {
        currentLevel = initCurrentLevel;
    }
    
    public PathXLevel       getLevel()                  {   return level;                   }
 //   public PathXPanel        getView()                  {   return view;                    }
    public Viewport         getViewport()               {   return viewport;                }
    public Image            getBackgroundImage()        {   return backgroundImage;         }
    public Image            getStartingLocationImage()  {   return startingLocationImage;   }
    public Image            getDesinationImage()        {   return destinationImage;        }
    public Intersection     getSelectedIntersection()   {   return selectedIntersection;    }
    public Road             getSelectedRoad()           {   return selectedRoad;            }
    public Intersection     getStartRoadIntersection()  {   return startRoadIntersection;   }
    public int              getLastMouseLocX()             {   return lastMouseLocX;              }
    public int              getLastMouseLocY()             {   return lastMouseLocY;              }
    public Intersection     getStartingLocation()       {   return level.startingLocation;  }
    public Intersection     getDestination()            {   return level.destination;       }
//    public boolean          isDataUpdatedSinceLastSave(){   return dataUpdatedSinceLastSave;}    
    public boolean          isStartingLocation(Intersection testInt)  
    {   return testInt == level.startingLocation;           }
    public boolean isDestination(Intersection testInt)
    {   return testInt == level.destination;                }
    public boolean isSelectedIntersection(Intersection testIntersection)
    {   return testIntersection == selectedIntersection;    }
    public boolean isSelectedRoad(Road testRoad)
    {   return testRoad == selectedRoad;                    }

    // ITERATOR METHODS FOR GOING THROUGH THE GRAPH

    public Iterator intersectionsIterator()
    {
        ArrayList<Intersection> intersections = level.getIntersections();
        return intersections.iterator();
    }
    public Iterator roadsIterator()
    {
        ArrayList<Road> roads = level.roads;
        return roads.iterator();
    }
    
    public Iterator banditsIterator(){
        ArrayList<Bandit> bandits = level.bandits;
        return bandits.iterator();
    }
    
    public Iterator policeIterator(){
        ArrayList<Police> police = level.police;
        return police.iterator();
    }
    
    public Iterator zombieIterator(){
        ArrayList<Zombie> zombies = level.zombies;
        return zombies.iterator();
    }
        
    public boolean isNothingSelected()      { return specialMode == PathXSpecialState.NOTHING_SELECTED; }
    public boolean isIntersectionSelected() { return specialMode == PathXSpecialState.INTERSECTION_SELECTED; }
    public boolean isIntersectionDragged()  { return specialMode == PathXSpecialState.INTERSECTION_DRAGGED; }
    public boolean isRoadSelected()         { return specialMode == PathXSpecialState.ROAD_SELECTED; }
    public boolean isAddingIntersection()   { return specialMode == PathXSpecialState.ADDING_INTERSECTION; }
    public boolean isAddingRoadStart()      { return specialMode == PathXSpecialState.ADDING_ROAD_START; }
    public boolean isAddingRoadEnd()        { return specialMode == PathXSpecialState.ADDING_ROAD_END; }
    
    // MUTATOR METHODS

//    public void setView(PathXPanel initView)
//    {   view = initView;    }
    public void setLastMousePosition(int initX, int initY)
    {
        lastMouseLocX = initX;
        lastMouseLocY = initY;
        mG.getCanvas().repaint();
    }    
    public void setSelectedIntersection(Intersection i)
    {
        selectedIntersection = i;
        selectedRoad = null;
        mG.getCanvas().repaint();
    }    
    public void setSelectedRoad(Road r)
    {
        selectedRoad = r;
        selectedIntersection = null;
        mG.getCanvas().repaint();
    }
    
    // AND THEN ALL THE SERVICE METHODS FOR UPDATING THE LEVEL
    // AND APP STATE

    /**
     * For selecting the first intersection when making a road. It will
     * find the road at the (canvasX, canvasY) location.
     */
    public void selectStartRoadIntersection(int canvasX, int canvasY)
    {
        startRoadIntersection = findIntersectionAtCanvasLocation(canvasX, canvasY);
        if (startRoadIntersection != null)
        {
            // NOW WE NEED THE SECOND INTERSECTION
            switchSpecialMode(PathXSpecialState.ADDING_ROAD_END);
        }
    }
    
    /**
     * For selecting the second intersection when making a road. It will
     * find the road at the (canvasX, canvasY) location.
     */
    public void selectEndRoadIntersection(int canvasX, int canvasY)
    {
        Intersection endRoadIntersection = findIntersectionAtCanvasLocation(canvasX, canvasY);
        if (endRoadIntersection != null)
        {
            // MAKE AND ADD A NEW ROAD
            Road newRoad = new Road();
            newRoad.node1 = startRoadIntersection;
            newRoad.node2 = endRoadIntersection;
            newRoad.oneWay = false;
            newRoad.speedLimit = DEFAULT_SPEED_LIMIT;
            level.roads.add(newRoad);
            
            // AND LET'S GO BACK TO ADDING ANOTHER ROAD
            switchSpecialMode(PathXSpecialState.ADDING_ROAD_START);
            startRoadIntersection = null;

            // RENDER
            mG.getCanvas().repaint();
        }
    }
    
    // INIT METHODS - AFTER CONSTRUCTION, THESE METHODS SETUP A GAME FOR USE
    // - initLevel
    // - initTiles
    // - initTile
    /**
     * Called after a level has been selected, it initializes the grid so that
     * it is the proper dimensions.
     */
    public void initLevel(String levelName)
    {
        /* KEEP THE TILE ORDER AND SORTING ALGORITHM FOR LATER
        snake = initSnake;
        sortingAlgorithm = initSortingAlgorithm;*/

        // UPDATE THE VIEWPORT IF WE ARE SCROLLING (WHICH WE'RE NOT)
        viewport.updateViewportBoundaries();

       // INTERACTIVE SETTINGS
        isMousePressed = false;
        isDragging = false;
        selectedIntersection = null;
        selectedRoad = null;
    }
    
    /**
     * Updates the background image.
     */
    public void updateBackgroundImage(String newBgImage)
    {
        
        // UPDATE THE LEVEL TO FIT THE BACKGROUDN IMAGE SIZE
        level.backgroundImageFileName = newBgImage;
        backgroundImage = mG.loadImage(LEVELS_PATH + level.backgroundImageFileName);
        int levelWidth = backgroundImage.getWidth(null);
        int levelHeight = backgroundImage.getHeight(null);
        viewport.setLevelDimensions(levelWidth, levelHeight);
        mG.getCanvas().repaint();
    }
    
    /**
     * Updates the image used for the starting location and forces rendering.
     */
    public void updateStartingLocationImage(String newStartImage)
    {
        level.startingLocationImageFileName = newStartImage;
        startingLocationImage = mG.loadImage(LEVELS_PATH + level.startingLocationImageFileName);
        mG.getCanvas().repaint();
    }

    /**
     * Updates the image used for the destination and forces rendering.
     */
    public void updateDestinationImage(String newDestImage)
    {
        level.destinationImageFileName = newDestImage;
        destinationImage = mG.loadImage(LEVELS_PATH + level.destinationImageFileName);
        mG.getCanvas().repaint();
    }
    
        /**
     * For changing the edit mode, and thus what edit operations
     * the user may perform.
     */
    public void switchSpecialMode(PathXSpecialState initSpecialMode)
    {
        if (levelBeingEdited)
        {
            // SET THE NEW EDIT MODE
            specialMode = initSpecialMode;
            
            // IF WE'RE ADDING A ROAD, THEN NOTHING SHOULD BE SELECTED 
            if (specialMode == PathXSpecialState.ADDING_ROAD_START)
            {
                selectedIntersection = null;
                selectedRoad = null;            
            }
            
            // RENDER
            mG.getCanvas().repaint();
        }
    }

    /**
     * Adds an intersection to the graph
     */
    public void addIntersection(Intersection intToAdd)
    {
        ArrayList<Intersection> intersections = level.getIntersections();
        intersections.add(intToAdd);
        mG.getCanvas().repaint();
    }

    /**
     * Calculates and returns the distance between two points.
     */
    public double calculateDistanceBetweenPoints(int x1, int y1, int x2, int y2)
    {
        double diffXSquared = Math.pow(x1 - x2, 2);
        double diffYSquared = Math.pow(y1 - y2, 2);
        return Math.sqrt(diffXSquared + diffYSquared);
    }

    /**
     * Moves the selected intersection to (canvasX, canvasY),
     * translating it into level coordinates.
     */
    public void moveSelectedIntersection(int canvasX, int canvasY)
    {
        selectedIntersection.x = canvasX + this.getViewport().getViewportX();
        selectedIntersection.y = canvasY + this.getViewport().getViewportY();
        mG.getCanvas().repaint();
    }

    /**
     * Searches the level graph and finds and returns the intersection
     * that overlaps (canvasX, canvasY).
     */
    public Intersection findIntersectionAtCanvasLocation(int canvasX, int canvasY)
    {
        // CHECK TO SEE IF THE USER IS SELECTING AN INTERSECTION
        for (Intersection i : level.intersections)
        {
            double distance = calculateDistanceBetweenPoints(i.x, i.y, canvasX + this.getViewport().getViewportX(), 
                    canvasY + this.getViewport().getViewportY());
            if (distance < INTERSECTION_RADIUS)
            {
                // MAKE THIS THE SELECTED INTERSECTION
                return i;
            }
        }
        return null;
    }

    /**
     * Deletes the selected item from the graph, which might be either
     * an intersection or a road.
     */
    public void deleteSelectedItem()
    {
        // DELETE THE SELECTED INTERSECTION, BUT MAKE SURE IT'S 
        // NOT THE STARTING LOCATION OR DESTINATION
        if ((selectedIntersection != null)
                && (selectedIntersection != level.startingLocation)
                && (selectedIntersection != level.destination))
        {
            // REMOVE ALL THE ROADS THE INTERSECTION IS CONNECTED TO
            ArrayList<Road> roadsMarkedForDeletion = new ArrayList();
            for (Road r : level.roads)
            {
                if ((r.node1 == selectedIntersection)
                        || (r.node2 == selectedIntersection))
                    roadsMarkedForDeletion.add(r);
            }
            
            // NOW REMOVE ALL THE ROADS MARKED FOR DELETION
            for (Road r : roadsMarkedForDeletion)
            {
                level.roads.remove(r);
            }
            
            // THEN REMOVE THE INTERSECTION ITSELF
            level.intersections.remove(selectedIntersection);
            
            // AND FINALLY NOTHING IS SELECTED ANYMORE
            selectedIntersection = null;
           // switchEditMode(PXLE_EditMode.NOTHING_SELECTED);            
        }
        // THE SELECTED ITEM MIGHT BE A ROAD
        else if (selectedRoad != null)
        {
            // JUST REMOVE THE NODE, BUT NOT ANY OF THE INTERSECTIONS
            level.roads.remove(selectedRoad);
            selectedRoad = null;
           // switchEditMode(PXLE_EditMode.NOTHING_SELECTED);
        }
    }
    
    /**
     * Unselects any intersection or road that might be selected.
     */
    public void unselectEverything()
    {
        selectedIntersection = null;
        selectedRoad = null;
        startRoadIntersection = null;
        mG.getCanvas().repaint();
    }

    /**
     * Searches to see if there is a road at (canvasX, canvasY), and if
     * there is, it selects and returns it.
     */
    public Road selectRoadAtCanvasLocation(int canvasX, int canvasY)
    {
        Iterator<Road> it = level.roads.iterator();
        Line2D.Double tempLine = new Line2D.Double();
        while (it.hasNext())
        {
            Road r = it.next();
            tempLine.x1 = r.node1.x;
            tempLine.y1 = r.node1.y;
            tempLine.x2 = r.node2.x;
            tempLine.y2 = r.node2.y;
            double distance = tempLine.ptSegDist(canvasX+this.getViewport().getViewportX(), canvasY+this.getViewport().getViewportX());
            
            // IS IT CLOSE ENOUGH?
            if (distance <= INT_STROKE)
            {
                // SELECT IT
                this.selectedRoad = r;
                //this.switchEditMode(PXLE_EditMode.ROAD_SELECTED);
                return selectedRoad;
            }
        }
        return null;
    }

    /**
     * Checks to see if (canvasX, canvasY) is free (i.e. there isn't
     * already an intersection there, and if not, adds one.
     */
    public void addIntersectionAtCanvasLocation(int canvasX, int canvasY)
    {
        // FIRST MAKE SURE THE ENTIRE INTERSECTION IS INSIDE THE LEVEL
        if ((canvasX - INTERSECTION_RADIUS) < 0) return;
        if ((canvasY - INTERSECTION_RADIUS) < 0) return;
        if ((canvasX + INTERSECTION_RADIUS) > viewport.getGameWorldWidth()) return;
        if ((canvasY + INTERSECTION_RADIUS) > viewport.getGameWorldHeight()) return;
        
        // AND ONLY ADD THE INTERSECTION IF IT DOESN'T OVERLAP WITH
        // AN EXISTING INTERSECTION
        for(Intersection i : level.intersections)
        {
            double distance = calculateDistanceBetweenPoints(i.x-viewport.getViewportX(), i.y-viewport.getViewportY(), canvasX, canvasY);
            if (distance < INTERSECTION_RADIUS)
                return;
        }          
        
        // LET'S ADD A NEW INTERSECTION
        int intX = canvasX + viewport.getViewportX();
        int intY = canvasY + viewport.getViewportY();
        Intersection newInt = new Intersection(intX, intY);
        level.intersections.add(newInt);
        mG.getCanvas().repaint();
    }
    
    /**
     * Retrieves the money, police, bandits, and zombies stats from
     * the view and uses it to refresh the level values.
     */
    public void refreshLevelStats()
    {
        /*if (!view.isRefreshingSpinners())
        {
            // GET THE DATA FROM THE VIEW
            int money = view.getCurrentMoney();
            int numPolice = view.getCurrentPolice();
            int numBandits = view.getCurrentBandits();
            int numZombies = view.getCurrentZombies();
        
            // AND USE IT TO UPDATE THE LEVEL
            level.setMoney(money);
            level.setNumPolice(numPolice);
            level.setNumBandits(numBandits);
            level.setNumZombies(numZombies);
        }*/
    }

    /**
     * Increases the speed limit on the selected road.
     */
    public void increaseSelectedRoadSpeedLimit()
    {
        if (selectedRoad != null)
        {
            int speedLimit = selectedRoad.getSpeedLimit();
            if (speedLimit < MAX_SPEED_LIMIT)
            {
                speedLimit += 50;
                selectedRoad.setSpeedLimit(speedLimit);
                mG.getCanvas().repaint();
            }
        }
    }

    /**
     * Decreases the speed limit on the selected road.
     */
    public void decreaseSelectedRoadSpeedLimit()
    {
        if (selectedRoad != null)
        {
            int speedLimit = selectedRoad.getSpeedLimit();
            if (speedLimit > MIN_SPEED_LIMIT)
            {
                speedLimit -= 50;
                selectedRoad.setSpeedLimit(speedLimit);
                mG.getCanvas().repaint();
            }
        }
    }    

    /**
     * Toggles the selected road, making it one way if it's currently
     * two-way, and two-way if it's currently one way.
     */
    public void toggleSelectedRoadOneWay()
    {
        if (selectedRoad != null)
        {
            selectedRoad.setOneWay(!selectedRoad.isOneWay());
            mG.getCanvas().repaint();
        }
    }
    
    /**
     * Used to calculate the x-axis pixel location in the game grid for a tile
     * placed at column with stack position z.
     *
     * @param column The column in the grid the tile is located.
     *
     * @return The x-axis pixel location of the tile
     */
    public int calculateGridTileX(int column)
    {
        return viewport.getViewportMarginLeft() + (column * TILE_WIDTH) - viewport.getViewportX();
    }

    /**
     * Used to calculate the y-axis pixel location in the game grid for a tile
     * placed at row.
     *
     * @param row The row in the grid the tile is located.
     *
     * @return The y-axis pixel location of the tile
     */
    public int calculateGridTileY(int row)
    {
        return viewport.getViewportMarginTop() + (row * TILE_HEIGHT) - viewport.getViewportY();
    }

    /**
     * Used to calculate the grid column for the x-axis pixel location.
     *
     * @param x The x-axis pixel location for the request.
     *
     * @return The column that corresponds to the x-axis location x.
     */
    public int calculateGridCellColumn(int x)
    {
        // ADJUST FOR THE MARGIN
        x -= viewport.getViewportMarginLeft();

        // ADJUST FOR THE VIEWPORT
        x = x + viewport.getViewportX();

        if (x < 0)
        {
            return -1;
        }

        // AND NOW GET THE COLUMN
        return x / TILE_WIDTH;
    }

    /**
     * Used to calculate the grid row for the y-axis pixel location.
     *
     * @param y The y-axis pixel location for the request.
     *
     * @return The row that corresponds to the y-axis location y.
     */
    public int calculateGridCellRow(int y)
    {
        // ADJUST FOR THE MARGIN
        y -= viewport.getViewportMarginTop();

        // ADJUST FOR THE VIEWPORT
        y = y + viewport.getViewportY();

        if (y < 0)
        {
            return -1;
        }

        // AND NOW GET THE ROW
        return y / TILE_HEIGHT;
    }
    
    @Override
    public void checkMousePressOnSprites(MiniGame game, int x, int y) {
        
        // FIGURE OUT THE CELL IN THE GRID
        int col = calculateGridCellColumn(x);
        int row = calculateGridCellRow(y);

        // DISABLE THE STATS DIALOG IF IT IS OPEN
        if (game.getGUIDialogs().get(INFO_DIALOG_TYPE).getState().equals(PathXState.VISIBLE_STATE.toString()))
        {
            game.getGUIDialogs().get(INFO_DIALOG_TYPE).setState(PathXState.INVISIBLE_STATE.toString());
            return;
        }
    }

    @Override
    public void reset(MiniGame game) {
        balance = 0;
    }

    @Override
    public void updateAll(MiniGame game) {
     /*   
        try
        {
            // MAKE SURE THIS THREAD HAS EXCLUSIVE ACCESS TO THE DATA
            game.beginUsingData();

            // WE ONLY NEED TO UPDATE AND MOVE THE MOVING TILES
            for (int i = 0; i < movingTiles.size(); i++)
            {
                // GET THE NEXT TILE
                SortingHatTile tile = movingTiles.get(i);

                // THIS WILL UPDATE IT'S POSITION USING ITS VELOCITY
                tile.update(game);

                // IF IT'S REACHED ITS DESTINATION, REMOVE IT
                // FROM THE LIST OF MOVING TILES
                if (!tile.isMovingToTarget())
                {
                    movingTiles.remove(tile);
                }
            }

            // IF THE GAME IS STILL ON, THE TIMER SHOULD CONTINUE
            if (inProgress())
            {
                // KEEP THE GAME TIMER GOING IF THE GAME STILL IS
                endTime = new GregorianCalendar();

                // LET'S CHECK TO SEE IF THE CURSOR IS OVER A TILE
                int cursorX = getLastMouseX();
                int cursorY = getLastMouseY();
                for (int i = 0; (i < tilesToSort.size()); i++)
                {
                    SortingHatTile tile = tilesToSort.get(i);
                    
                    // ARE WE MOUSING OVER A TILE?
                    if (tile.containsPoint(cursorX, cursorY))
                    {
                        if (tile != selectedTile)
                        {
                            tile.setState(SortingHatTileState.MOUSE_OVER_STATE.toString());
                        }
                    }
                    // NOT MOUSING OVER
                    else
                    {
                        if (tile != selectedTile)
                        {
                            tile.setState(SortingHatTileState.VISIBLE_STATE.toString());
                        }
                    }
                }
            }
        } finally
        {
            // MAKE SURE WE RELEASE THE LOCK WHETHER THERE IS
            // AN EXCEPTION THROWN OR NOT
            game.endUsingData();
        }*/
    }

    @Override
    public void updateDebugText(MiniGame game) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    //GETTERS
    public int getGoal()                      { return goal; }
    public Player getPlayer()                 { return player;}
    public int getBalance()                   { return balance; }
    public int []getMoneyEarned()             { return moneyEarned; }
    public String []getLevelName()            { return levelName; }
    public Image getDestinationImage()        { return destinationImage; }

    //SETTERS
    public void setBalance(int balance)                               {this.balance = balance;}
    public void setGoal(int goal)                                     {this.goal = goal;}
    public void setLevel(PathXLevel level)                            {this.level = level;}
    public void setPlayer(Player player)                              {this.player = player;}      
    public void setBackgroundImage(Image backgroundImage)             {this.backgroundImage = backgroundImage;}
    public void setStartingLocationImage(Image startingLocationImage) {this.startingLocationImage = startingLocationImage;}
    public void setDestinationImage(Image destinationImage)           {this.destinationImage = destinationImage;}
    public void setStartRoadIntersection(Intersection startRoadIntersection) {this.startRoadIntersection = startRoadIntersection;} 
}
