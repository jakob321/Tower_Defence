package se.liu.antos931jakos322.towerdefence.entities.towers;

import se.liu.antos931jakos322.towerdefence.entities.Entity;
import se.liu.antos931jakos322.towerdefence.entities.projectiles.ProjectileType;

import java.awt.*;

/**
 * ArrowTower is a tower with burst-fire. This means the tower shoots 3 projectiles at once ArrowTower has medium attack speed, medium
 * damage, medium range and uses Arrow projectiles
 */


public class ArrowTower extends Tower
{

    // ------ the inspections are false positive these constants are random properties and should not be enum's ------
    private static final int ATTACK_POWER = 5;
    private static final int COST = 6;
    private static final int RANGE = 5;
    private static final int UPGRADE_COST = 2;
    private static final int ATTACK_SPEED = 15;
    private static final Color COLOR = Color.BLUE;
    private static final double SIZE = 0.6;
    private static final ProjectileType PROJECTILE_TYPE = ProjectileType.ARROW;
    private static final TowerType TOWER_TYPE = TowerType.ARROW;
    private int burstFireCharge = 0;


    public ArrowTower()
    {
	super(TOWER_TYPE, COLOR, COST, ATTACK_POWER, RANGE, UPGRADE_COST, PROJECTILE_TYPE, ATTACK_SPEED, SIZE);
    }

    /**
     * ArrowTower implements burst shooting This means canAttack returns true three times in a row when there is a valid target
     *
     * @param entity entity to check for attack
     *
     * @return if the tower can attack
     */
    @Override public boolean canAttack(final Entity entity) {
	// since the attackSpeedCharge decides how much the tower has "charged" we set it to be attackspeed three times
	// before needing to "charge" again
	final int burstAmount = 3;
	if (super.canAttack(entity) && burstFireCharge < burstAmount) {
	    attackSpeedCharge = attackSpeed;
	    burstFireCharge++;
	    return true;
	} else {
	    burstFireCharge = 0;
	    return false;
	}
    }

}
