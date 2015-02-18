package pathx.file;

import java.io.File;
import pathx.data.PathXLevel;
import pathx.data.PathXDataModel;

/**
 *
 * @author Brian Sabzjadid
 */
public interface PathXIO
{
    public boolean loadLevel(File levelFile, PathXDataModel model);
    public boolean saveLevel(File levelFile, PathXLevel levelToSave);
}
