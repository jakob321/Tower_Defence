package se.liu.antos931jakos322.towerdefence.gamelogic;


import java.awt.*;
import java.util.EnumMap;
import java.util.Random;

/**
 * Uses a tileType to decide which type of tile itself is. Depending on what type of tile a tile is it is drawn in different ways.
 * <p>
 * Tile is used to build every "block" or "tile" of the game map
 */

public class Tile
{
    private TileType tileType;
    private Color tileColor;
    private Point position;
    private final static Random RND = new Random();


    public Tile(Point position, final TileType tileType) {
	this.tileType = tileType;
	this.tileColor = getRandomNuance();
	this.position = position;
    }

    /**
     * Draws a square on the position of the tile object
     *
     * @param g2d      the grapchis object to draw with
     * @param margin   the margin between each tile
     * @param tileSize The size of the tile
     */
    public void draw(final Graphics2D g2d, final int margin, final int tileSize) {

	g2d.setColor(tileColor);
	g2d.fillRect(position.x * (margin + tileSize), position.y * (margin + tileSize), tileSize, tileSize);

    }

    @Override public String toString() {
	return tileType + " ";
    }

    /**
     * Creates a random nuance to the color of the tile
     *
     * @return a color with a random nuance on all RGB values
     */
    public Color getRandomNuance() {
	Color oldColor = getStandardTileColor(tileType);
	return new Color(getRandomColorChannel(oldColor.getRed()), getRandomColorChannel(oldColor.getGreen()),
			 getRandomColorChannel(oldColor.getBlue()));
    }

    /**
     * Returns a new semi random int. The new integer is in the span of 50 from the oldColor. If the integer is less than or equal to 50
     * then This means the integer can be used for RBG values given an integer from 0-255 will always return a valid rgb value
     *
     * @param oldColor the integer which should be
     *
     * @return a random int in the span of 50 from the original
     */

    private int getRandomColorChannel(int oldColor) {
	final int colorSpan = 50;
	final int randomInt = RND.nextInt(colorSpan);
	if (oldColor - randomInt >= 0) {
	    return oldColor - randomInt;
	} else {
	    return oldColor + randomInt;
	}

    }

    /**
     * Returns the color of the given tileType
     *
     * @param tileType the type of tile
     *
     * @return The color of the tileType
     */
    private static Color getStandardTileColor(TileType tileType) {
	EnumMap<TileType, Color> tileColors = new EnumMap<>(TileType.class);

	tileColors.put(TileType.GRASS, Color.GREEN);
	tileColors.put(TileType.ROAD, Color.BLACK);

	return tileColors.get(tileType);
    }

    public TileType getTileType() {
	return tileType;
    }


}
