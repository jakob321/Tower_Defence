package se.liu.antos931jakos322.towerdefence.entities.projectiles;

import java.awt.*;

/**
 * PenetratingProjectile is a projectile that is comparatively strong when looking at other projectiles. It moves at a slow speed, has a
 * large size and it penetrates 20 enemies.
 */


public class PenetratingProjectile extends Projectile
{

    private static final double SIZE = 0.35;
    private static final double SPEED = 0.15;
    private static final int PENETRATION_AMOUNT = 20;
    private final static Color COLOR = Color.LIGHT_GRAY;

    public PenetratingProjectile() {
	super(COLOR, SIZE, SPEED, PENETRATION_AMOUNT);
    }

}
