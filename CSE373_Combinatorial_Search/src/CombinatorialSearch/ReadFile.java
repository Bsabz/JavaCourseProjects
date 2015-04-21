/*
 * Copyright (C) 2015 Brian Sabz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package CombinatorialSearch;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class reads a file from disk and returns a string array containing one
 * line of text per element.
 *
 * @author Brian Sabz
 */
public class ReadFile {

    int[][] data;

    public ReadFile(String fileName) throws Exception {

        FileReader reader = new FileReader(fileName);
        BufferedReader buffer = new BufferedReader(reader);

        StringBuilder builder = new StringBuilder();

        String line = buffer.readLine(); // read the number of edges (first line)
        int numNodes = Integer.parseInt(line);

        line = buffer.readLine(); // read the number of vertices (second line)
        int numEdges = Integer.parseInt(line);

        // create an array:
        int i = 2;
        String fileText[] = new String[numEdges + 2];
        fileText[0] = "" + numNodes;
        fileText[1] = "" + numEdges;

        line = buffer.readLine();
        while (line != null) {
            fileText[i++] = line;
            line = buffer.readLine();
        }

        buffer.close();

        // Turn this into an integer array and return it:
        this.data = new int[numEdges + 2][2];
        data[0][0] = numEdges;
        data[1][0] = numNodes;
        for (int j = 2; j < data.length; j++) {
            String parts[] = fileText[j].split("    ");
            data[j][0] = Integer.parseInt(parts[0]);
            data[j][1] = Integer.parseInt(parts[1]);
        }
    }

    public int[][] getData() {
        return this.data;
    }
}
