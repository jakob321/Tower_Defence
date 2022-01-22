package se.liu.antos931jakos322.towerdefence.entities.enemies;

import java.awt.*;

/**
 * A standard enemy. StandardEnemy extends the abstract class Enemy StandardEnemy have low/medium health, damage and speed. Therefor
 * StandardEnemy is used in the first rounds. StandardEnemy are created by WaveMaker.
 */

public class StandardEnemy extends Enemy
{
    private static final int HEALTH = 100;
    private static final double SPEED = 0.1;
    private static final Color COLOR = Color.RED;
    private static final double SIZE = 0.5;
    private static final int DAMAGE = 1;

    public StandardEnemy() {
	super(HEALTH, SPEED, COLOR, SIZE, DAMAGE);
    }
}
