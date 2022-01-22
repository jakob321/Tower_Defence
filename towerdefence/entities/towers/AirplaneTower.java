package se.liu.antos931jakos322.towerdefence.entities.towers;


import se.liu.antos931jakos322.towerdefence.entities.projectiles.ProjectileType;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * AirplaneTower extends Tower and is a Tower that can move. AirplaneTower moves in circles around the startPosition to represent an
 * airplane flying around.
 * <p>
 * AirplaneTower shoots Missile projectiles and has average stats
 */


public class AirplaneTower extends Tower
{


    private double angle;
    private double positionRadius;
    // ------ the inspections are false positive these constants are random properties and should not be enum's ------
    private final static int ATTACK_POWER = 3;
    private final static int COST = 10;
    private final static int RANGE = 4;
    private final static int UPGRADE_COST = 3;
    private final static int ATTACK_SPEED = 3;
    private final static double SPEED = 0.03;
    private final static double SIZE = 0.5;
    private final static Color COLOR = Color.red;
    private final static TowerType TOWER_TYPE = TowerType.AIRPLANE;
    private final static ProjectileType PROJECTILE_TYPE = ProjectileType.STICKY;

    public AirplaneTower()
    {
	super(TOWER_TYPE, COLOR, COST, ATTACK_POWER, RANGE, UPGRADE_COST, PROJECTILE_TYPE, ATTACK_SPEED, SPEED, SIZE);
	this.angle = 0;
	this.positionRadius = 2;
    }

    /**
     * Airplane tower has the "unique action" of moving in a circle So activate moves the tower in a circle
     */

    @Override public void activate() {
	move();
    }

    /**
     * Upgrades the tower but also increases the radius of circle it spins around.
     */

    @Override public void upgrade() {
	super.upgrade();
	final double radiusIncrease = 0.5;
	positionRadius += radiusIncrease;
    }

    /**
     * Moves the tower in a circle around the starting position
     */

    @Override public void move() {
	angle += SPEED; // in this context speed is a angle speed
	// use sin and cos combined with the changing angle to circle the tower
	// around where it was first placed
	double newX = startPosition.getX() + positionRadius * Math.sin(angle);
	double newY = startPosition.getY() + positionRadius * Math.cos(angle);

	Point2D newPos = new Point2D.Double(newX, newY);
	position = newPos;
    }
}
