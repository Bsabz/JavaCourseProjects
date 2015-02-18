package pathx.ui;

/**
 * This enum helps the model keep track of what edit operation
 * the user is currently doing so that the UI can provide the
 * appropriate response.
 * 
 * @author Brian Sabzjadid
 */
public enum PathXSpecialState
{
    NOTHING_SELECTED,
    INTERSECTION_SELECTED,
    INTERSECTION_DRAGGED,
    ROAD_SELECTED,
    ADDING_INTERSECTION,
    ADDING_ROAD_START,
    ADDING_ROAD_END
}