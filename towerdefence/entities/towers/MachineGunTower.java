package se.liu.antos931jakos322.towerdefence.entities.towers;

import se.liu.antos931jakos322.towerdefence.entities.projectiles.ProjectileType;

import java.awt.*;

/**
 * BulletProjectile extends Tower and has no extra functionallity
 * <p>
 * MachineGunTower has high attack speed, low damage, high range and uses Bullet projectiles
 */

public class MachineGunTower extends Tower
{
    // ------ the inspections are false positive these constants are randomly chosen properties and should not be enum's ------
    private final static int ATTACK_POWER = 3;
    private final static int COST = 4;
    private final static int RANGE = 7;
    private final static int UPGRADE_COST = 1;
    private final static int ATTACK_SPEED = 1;
    private final static Color COLOR = Color.PINK;
    private final static TowerType TOWER_TYPE = TowerType.MACHINE_GUN;
    private final static double SIZE = 0.6;
    private final static ProjectileType PROJECTILE_TYPE = ProjectileType.BULLET;

    public MachineGunTower()
    {
	super(TOWER_TYPE, COLOR, COST, ATTACK_POWER, RANGE, UPGRADE_COST, PROJECTILE_TYPE, ATTACK_SPEED, SIZE);
    }

}
