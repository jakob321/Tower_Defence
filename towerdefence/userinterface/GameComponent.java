package se.liu.antos931jakos322.towerdefence.userinterface;

import se.liu.antos931jakos322.towerdefence.gamelogic.GameHandler;
import se.liu.antos931jakos322.towerdefence.gamelogic.Tile;

import javax.swing.*;
import java.awt.*;

/**
 * MapComponent controls the main drawing of objects that exist on the game map MapComponent draws objects by using their own drawing
 * methods That means MapComponent only controls how objects are drawn in relation to eachother but does not contain information on how to
 * draw indivudual objects.
 * <p>
 * MapComponent also implements gameListener which lets MapComponent know of changes made to the game. In which case it needs act on those
 * changes to update the UI.
 */

public class GameComponent extends JComponent implements GameListener
{
    // ---------- in response the the inspection about similar children ------
    //------- While GameComponent and menuComponent could have an abstract class in this instance it feels redundat -------
    // ------ it will be one class with gameHandler and the method gamechanged. ------
    // ------ in the future if adding more componentes who uses this code it would make sense more sense. ------
    // ----- Otherwise it is just bloating the project with classes -------
    // -----also because Startmenu who uses the gamelistner interface implements its own gameChanged and does not extend JComponent -----
    private GameHandler gameHandler;
    private final int mapWidth;
    private final int mapHeight;
    private final static int MARGIN = 0;

    public GameComponent(GameHandler gameHandler) {
	this.gameHandler = gameHandler;
	Point mapDimensions = gameHandler.getMapDimensions();
	this.mapHeight = mapDimensions.y;
	this.mapWidth = mapDimensions.x;
    }


    public Dimension getPreferredSize() {

	int gameWidth = mapWidth * (MARGIN + GAME_SCALE);
	int gameHeight = mapHeight * (MARGIN + GAME_SCALE);
	return new Dimension(gameWidth, gameHeight);

    }

    @Override public void gameChanged() {
	repaint();
    }

    @Override protected void paintComponent(final Graphics g) {
	super.paintComponent(g);

	final Graphics2D g2d = (Graphics2D) g;

	for (int y = 0; y < mapHeight; y++) {
	    for (int x = 0; x < mapWidth; x++) {
		Tile currentTile = gameHandler.getMapTile(new Point(x, y));
		currentTile.draw(g2d, MARGIN, GAME_SCALE);
	    }
	}

	// since we want projectiles to be drawn "in the bottom" and tower "on top of enemies"
	// We do draw every Entity in an independat loop. However a solution which maybe looks nicer is to use
	// polymorphism by instead getting all Entity objects in Gamehandler and then drawing them.
	// Then these three for loops would be one and only entity.draw() would have to be written.
	for (int i = 0; i < gameHandler.getProjectileAmount(); i++) {
	    gameHandler.getProjectile(i).draw(g2d, GAME_SCALE);
	}
	for (int i = 0; i < gameHandler.getEnemyAmount(); i++) {
	    gameHandler.getEnemy(i).draw(g2d, GAME_SCALE);
	}
	for (int i = 0; i < gameHandler.getTowerAmount(); i++) {
	    gameHandler.getTower(i).draw(g2d, GAME_SCALE);
	}

    }

}
