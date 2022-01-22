package se.liu.antos931jakos322.towerdefence.gamelogic;

import java.awt.*;
import java.util.List;

/**
 * Mapinfo contains the information of a map. This includes the path, and the map diementions. Mapinfo is used to load maps.json file and
 * does not explicitely get constructed This is because the code for creating a map is redundant when the maps.json file exists and
 * therefore it has been removed. It was previously located in GameMap
 */
public class MapInfo
{
    private Point dimensions = null;
    private List<Point> path = null;

    public Point getDimensions() {
	return dimensions;
    }

    public List<Point> getPath() {
	return path;
    }
}
