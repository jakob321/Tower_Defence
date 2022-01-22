package se.liu.antos931jakos322.towerdefence.entities.enemies;

import java.awt.*;

/**
 * SpeedEnemy is an enemy type that extends the abstract class Enemy SpeedEnemy have low health but are fast. SpeedEnemy are created by
 * WaveMaker
 */
public class SpeedEnemy extends Enemy
{
    private static final int HEALTH = 70;
    private static final double SPEED = 0.15;
    private static final Color COLOR = Color.PINK;
    private static final double SIZE = 0.3;
    private static final int DAMAGE = 3;


    public SpeedEnemy() {
	super(HEALTH, SPEED, COLOR, SIZE, DAMAGE);
    }
}
