package se.liu.antos931jakos322.towerdefence.userinterface;


import se.liu.antos931jakos322.towerdefence.gamelogic.GameHandler;

import javax.swing.*;
import java.awt.*;

/**
 * MenuComponent draws the text graphics for game health, money and wave level MenuComponent implements GameListener which means it can
 * listen to game changes and update the grapchis accordingly
 */
public class MenuComponent extends JComponent implements GameListener
{

    // ---------- in response the the inspection about similar children ------
    //------- While GameComponent and menuComponent could have an abstract class in this instance it feels redundat -------
    // ------ it will be one class with gameHandler and the method gamechanged. ------
    // ------ in the future if adding more componentes who uses this code it would make sense more sense. ------
    // ----- Otherwise it is just bloating the project with classes -------
    // -----also because Startmenu who uses the gamelistner interface implements its own gameChanged and does not extend JComponent -----
    private GameHandler gameHandler;


    public MenuComponent(final GameHandler gameHandler) {
	this.gameHandler = gameHandler;

    }

    /**
     * GameComponent repaints the game when the game changes
     */
    @Override public void gameChanged() {
	repaint();
    }

    /**
     * Draws the Life, money and wave level text. Draws it from the up and down.
     *
     * @param g the graphics object to draw with
     */

    @Override protected void paintComponent(final Graphics g) {
	super.paintComponent(g);
	final Graphics2D g2d = (Graphics2D) g;
	String health = "Life: " + gameHandler.getHealth();
	String currentMoney = "Money: " + gameHandler.getMoney();
	String level = "Wave level: " + gameHandler.getLevel();
	g2d.setColor(Color.BLACK);
	g2d.setFont(new Font("serif", Font.PLAIN, GAME_SCALE / 2));
	final int positionX = 0;

	final int margin = GAME_SCALE / 2;
	final int healthPositionY = GAME_SCALE;
	final int moneyPositionY = GAME_SCALE + margin;
	final int levelPositionY = GAME_SCALE + margin * 2;
	g2d.drawString(health, positionX, healthPositionY);
	g2d.drawString(currentMoney, positionX, moneyPositionY);
	g2d.drawString(level, positionX, levelPositionY);
    }
}
