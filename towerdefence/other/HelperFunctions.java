package se.liu.antos931jakos322.towerdefence.other;

import java.awt.geom.Point2D;

/**
 * Class with various helper functions The helper functions do not directly have anyhting with the game to do for example mathematical
 * calculation
 */


public class HelperFunctions
{

    /**
     * Returns wheter two positions are within a certain distance
     *
     * @param position1 the first position to compare
     * @param position2 the second position to compare
     * @param distance  the allowed distance for the two positions
     *
     * @return if the two positions are within the distance from eachother
     */

    public static boolean isNear(Point2D position1, Point2D position2, double distance) {

	double deltaPositionX = position2.getX() - position1.getX();
	double deltaPositionY = position2.getY() - position1.getY();
	double distanceFrom = Math.hypot(deltaPositionX, deltaPositionY);
	if (distanceFrom < distance) {
	    return true;
	}
	return false;
    }
}
