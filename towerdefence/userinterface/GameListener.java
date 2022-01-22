package se.liu.antos931jakos322.towerdefence.userinterface;

/**
 * MapListener is used by graphical classes to listen to game Classes implementing this interface can therefore get knowledge when a change
 * has occured that needs to be acted upon. In conjunction with calling gameChanged in GameHandler for the graphical classes. This class
 * also contains constants used by graphics to draw the game
 */

public interface GameListener
{

    final static int GAME_SCALE = 50;

    public void gameChanged();

}
