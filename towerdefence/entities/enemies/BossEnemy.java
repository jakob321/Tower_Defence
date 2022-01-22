package se.liu.antos931jakos322.towerdefence.entities.enemies;

import java.awt.*;

/**
 * BossEnemy is an enemy type that extends the abstract class Enemy. BossEnemy have a lot of health but moves slow. BossEnemy are created by
 * WaveMaker
 */
public class BossEnemy extends Enemy
{
    private static final int HEALTH = 1000;
    private static final double SPEED = 0.03;
    private static final Color COLOR = Color.GRAY;
    private static final double SIZE = 0.9;
    private static final int DAMAGE = 10;

    private static final int NUMBER_OF_SPLITS = 5;
    private static final EnemyType SPLIT_TYPE = EnemyType.STANDARD;
    private static final int SPLIT_DISTANCE = 3;

    public BossEnemy() {
	super(HEALTH, SPEED, COLOR, SIZE, DAMAGE, NUMBER_OF_SPLITS, SPLIT_TYPE, SPLIT_DISTANCE);
    }
}
