package se.liu.antos931jakos322.towerdefence.entities;

import se.liu.antos931jakos322.towerdefence.entities.enemies.BossEnemy;
import se.liu.antos931jakos322.towerdefence.entities.enemies.Enemy;
import se.liu.antos931jakos322.towerdefence.entities.enemies.EnemyType;
import se.liu.antos931jakos322.towerdefence.entities.enemies.ExplodingEnemy;
import se.liu.antos931jakos322.towerdefence.entities.enemies.FlyingEnemy;
import se.liu.antos931jakos322.towerdefence.entities.enemies.SpeedEnemy;
import se.liu.antos931jakos322.towerdefence.entities.enemies.StandardEnemy;
import se.liu.antos931jakos322.towerdefence.entities.projectiles.ArrowProjectile;
import se.liu.antos931jakos322.towerdefence.entities.projectiles.BulletProjectile;
import se.liu.antos931jakos322.towerdefence.entities.projectiles.ExplodingProjectile;
import se.liu.antos931jakos322.towerdefence.entities.projectiles.PenetratingProjectile;
import se.liu.antos931jakos322.towerdefence.entities.projectiles.Projectile;
import se.liu.antos931jakos322.towerdefence.entities.projectiles.ProjectileType;
import se.liu.antos931jakos322.towerdefence.entities.projectiles.StickyProjectile;
import se.liu.antos931jakos322.towerdefence.entities.towers.AirplaneTower;
import se.liu.antos931jakos322.towerdefence.entities.towers.ArrowTower;
import se.liu.antos931jakos322.towerdefence.entities.towers.CanonTower;
import se.liu.antos931jakos322.towerdefence.entities.towers.MachineGunTower;
import se.liu.antos931jakos322.towerdefence.entities.towers.Tower;
import se.liu.antos931jakos322.towerdefence.entities.towers.TowerType;

import java.util.ArrayList;
import java.util.List;

/**
 * EntityGetter is a class with static methods that can return a new Entity object with the Entity specific Enum as argument. Example:
 * EntityGetter.getTower(TowerType.CANON) returns a new CanonTower object. Can also return the list of avaible TowerType's by using
 * EntityGetter.getAllTowers()
 */

public class EntityFactory
{

    /**
     * Returns a projectile of the specified projectileType
     *
     * @param projectileType the projectile to get
     *
     * @return a new Projectile object of the specified splitEnemyType
     */

    public static Projectile getProjectile(ProjectileType projectileType) throws IllegalArgumentException {
	// A map might look nicer and be more modular in the future, but this can be added if the future requires it.
	// A map could be used instead of switch case but this means new objects will be unneccesarily created.
	// when we only want to return a specific one. So a switch case seems more appropriate in this case
	switch (projectileType) {
	    case ARROW:
		return new ArrowProjectile();
	    case EXPLODING:
		return new ExplodingProjectile();
	    case BULLET:
		return new BulletProjectile();
	    case STICKY:
		return new StickyProjectile();
	    case PENETRATING:
		return new PenetratingProjectile();
	    default:
		return null;
	}
    }

    /**
     * Returns an enemy of the specified towerType
     *
     * @param enemyType the enemy to get
     *
     * @return a new Enemy object of the specified splitEnemyType
     */

    public static Enemy getEnemy(EnemyType enemyType) throws IllegalArgumentException {
	// A map might look nicer and be more modular in the future, but this can be added if the future requires it.
	// A map could be used instead of switch case but this means new objects will be unneccesarily created.
	// when we only want to return a specific one. So a switch case seems more appropriate in this case
	switch (enemyType) {
	    case FLYING:
		return new FlyingEnemy();
	    case BOSS:
		return new BossEnemy();
	    case SPEED:
		return new SpeedEnemy();
	    case EXPLODING:
		return new ExplodingEnemy();
	    case STANDARD:
		return new StandardEnemy();

	    default:
		return null;
	}
    }

    /**
     * Returns a list of the towerTypes that can be used in the game
     *
     * @return list with all towerTypes
     */
    public static List<TowerType> getAllTowers() {

	List<TowerType> towers = new ArrayList<>();
	for (TowerType towerType : TowerType.values()) {
	    if (towerType != TowerType.NONE) {
		towers.add(towerType);
	    }
	}
	return towers;
    }

    /**
     * Returns a tower of the specified towerType
     *
     * @param towerType the towerType to get
     *
     * @return a new Tower object of the specified towerType
     */
    public static Tower getTower(TowerType towerType) {
	// A map might look nicer and be more modular in the future, but this can be added if the future requires it.
	// A map could be used instead of switch case but this means new objects will be unneccesarily created.
	// when we only want to return a specific one. So a switch case seems more appropriate in this case
	switch (towerType) {
	    case ARROW:
		return new ArrowTower();
	    case CANON:
		return new CanonTower();
	    case MACHINE_GUN:
		return new MachineGunTower();
	    case AIRPLANE:
		return new AirplaneTower();
	    default:
		return null;
	}
    }
}
