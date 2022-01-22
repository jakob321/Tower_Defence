package se.liu.antos931jakos322.towerdefence.entities.projectiles;

import se.liu.antos931jakos322.towerdefence.entities.Entity;

import java.awt.*;

/**
 * ExplodingProjectile is a projectile that explodes when hitting an enemy. It moves at a slow speed, has a large size after explosion and
 * remains in place for a couple of ticks after exploding
 */


public class ExplodingProjectile extends Projectile
{
    private static final double SIZE = 0.35;
    private static final double SPEED = 0.15;
    private static final int PENETRATION_AMOUNT = 10;
    private final static Color COLOR = Color.DARK_GRAY;
    private boolean isExploding;

    public ExplodingProjectile() {
	super(COLOR, SIZE, SPEED, PENETRATION_AMOUNT);
	this.isExploding = false;
    }

    /**
     * Moves the projectiles towards the targetEnemy and reduces the penertation while the projectile is exploding.
     */
    @Override public void move() {
	super.move();
	if (isExploding) {
	    // reducing the penertation means the projectile will be removed after when it hits 0
	    // this is to simulate the explosion "going away"
	    reducePenetration();
	}
    }

    /**
     * When the projectile hits an enemy the projectile explodes and stops moving. The projectile can attack the same target several times
     *
     * @param entity the entity object to attack
     */

    @Override public void attack(final Entity entity) {
	super.attack(entity);
	// set prevousily attacked enemy to null so that the same target inside the explosion takes damage
	prevAttackedEntity = null;
	if (!isExploding) {
	    movePosition = position;
	    isExploding = true;
	    this.color = Color.ORANGE;
	    final int sizeScale = 3;
	    size = SIZE * sizeScale;
	}
    }
}
