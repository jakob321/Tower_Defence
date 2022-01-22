package se.liu.antos931jakos322.towerdefence.entities.enemies;

import java.awt.*;

/**
 * FlyingEnemy is an Enemy object. FlyingEnemy moves direcetly towards the end of the game path. Ignoring the path FlyingEnemy are created
 * by WaveMaker
 */


public class FlyingEnemy extends Enemy
{
    private static final int HEALTH = 200;
    private static final double SPEED = 0.1;
    private static final Color COLOR = Color.WHITE;
    private static final double SIZE = 0.5;
    private static final int DAMAGE = 3;

    public FlyingEnemy() {
	super(HEALTH, SPEED, COLOR, SIZE, DAMAGE);
    }

    /**
     * Moves the FlyingEnemy directly towards the end of the game path
     */

    @Override public void move() {
	// Skipp all positions to move to except for the last
	if (position == null) {
	    position = movePosition;
	} else if (pathProgress == lastPosition) {
	    super.move();
	} else {
	    pathProgress++;
	}
    }
}
