package se.liu.antos931jakos322.towerdefence.entities.projectiles;

import java.awt.*;

/**
 * StickyProjectile is a projectile that is comparatively average when looking at other projectiles. It moves at a medium speed, has a
 * medium size and it penetrates two enemies. StickyProjectile also implements a new move() method to imitate something sticking to the
 * enemy. also plants itself on the ground after the enemy is defeated.
 */


public class StickyProjectile extends Projectile
{
    private static final double SIZE = 0.20;
    private static final double SPEED = 0.26;
    private static final int PENETRATION_AMOUNT = 2;
    private static final Color COLOR = Color.CYAN;


    @Override public void move() {
	movePosition = targetEntity.getPosition();
	super.move();

    }

    public StickyProjectile() {
	super(COLOR, SIZE, SPEED, PENETRATION_AMOUNT);
    }
}
