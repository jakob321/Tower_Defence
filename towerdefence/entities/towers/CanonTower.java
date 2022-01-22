package se.liu.antos931jakos322.towerdefence.entities.towers;

import se.liu.antos931jakos322.towerdefence.entities.projectiles.ProjectileType;

import java.awt.*;

/**
 * CanopnTower is a Tower object CanonTower has the added functionallity of changing its projectiles to ExplosiveProjectiles once it reaches
 * level 3. CanonTower has low attack speed, extra high damage, medium range and uses Canon projectiles
 */

public class CanonTower extends Tower
{
    // ------ the inspections are false positive these constants are random properties and should not be enum's ------
    private final static int ATTACK_POWER = 5;
    private final static int COST = 6;
    private final static int RANGE = 3;
    private final static int UPGRADE_COST = 4;
    private final static int ATTACK_SPEED = 20;
    private final static double SIZE = 0.6;
    private final static TowerType TOWER_TYPE = TowerType.CANON;
    private final static Color COLOR = Color.ORANGE;
    private final static ProjectileType PROJECTILE_TYPE = ProjectileType.PENETRATING;

    public CanonTower()
    {
	super(TOWER_TYPE, COLOR, COST, ATTACK_POWER, RANGE, UPGRADE_COST, PROJECTILE_TYPE, ATTACK_SPEED, SIZE);
    }

    /**
     * Upgrades the Tower attackpower and increses tower level When canon tower reaches level 3 it's projecticles are replaced with
     * exploding projectiles
     */

    @Override public void upgrade() {
	super.upgrade();
	final int towerLevel = 3;
	if (level >= towerLevel) {
	    projectileType = ProjectileType.EXPLODING;
	}
    }

}
