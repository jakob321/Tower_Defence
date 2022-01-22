package se.liu.antos931jakos322.towerdefence.entities.towers;

import se.liu.antos931jakos322.towerdefence.entities.Entity;
import se.liu.antos931jakos322.towerdefence.entities.EntityAttacker;
import se.liu.antos931jakos322.towerdefence.entities.EntityFactory;
import se.liu.antos931jakos322.towerdefence.entities.projectiles.Projectile;
import se.liu.antos931jakos322.towerdefence.entities.projectiles.ProjectileType;
import se.liu.antos931jakos322.towerdefence.other.HelperFunctions;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Tower is the Abstract class for defensive structures in the game which also extends EntityAttacker. Which means it inherits logic for
 * attacking other Entity objects
 * <p>
 * Tower can attack Entities by first deciding what entity to target with decideTarget(Entity) then using canAttack(Entity) to make sure it
 * is following the specific tower's attacking rules. When the target is decided by decideTarget the method createProjectileAttack() can be
 * used to attack the target with a projectile.
 * <p>
 * Towers shoot projectiles and also has various properties
 * <p>
 * These properties are: cost - represents how much a tower costs to create attackspeed - how often a tower can shoot a projectile range -
 * how close the tower needs to be a target to shoot a projectile Level - represents how much Tower has upgraded where each level increases
 * the strength of Tower
 */


public abstract class Tower extends EntityAttacker
{
    protected int range;
    protected int cost;
    protected int upgradeCost;
    protected int attackSpeed;
    protected int attackSpeedCharge;
    protected TowerType towerType;
    protected ProjectileType projectileType;
    protected boolean selected;
    protected int level;


    /**
     * Constructs a Tower for towers with an inital speed
     *
     * @param speed          the speed which the tower moves with
     * @param towerType      the type of tower
     * @param color          the color of the tower
     * @param cost           the cost of the tower
     * @param attackPower    the attackpower or "damage" of the tower
     * @param range          the range in which the tower can attack
     * @param upgradeCost    the cost to upgrade the tower
     * @param projectileType the type of projectile the tower uses
     * @param attackSpeed    the attackspeed of the tower. Higher attackspeed means a longer time to attack
     * @param size           the size of the tower
     */
    protected Tower(TowerType towerType, Color color, int cost, int attackPower, int range, int upgradeCost, ProjectileType projectileType,
		    int attackSpeed, double speed, double size)
    {
	super(color, size, speed, attackPower);
	this.cost = cost;
	this.range = range;
	this.towerType = towerType;
	this.upgradeCost = upgradeCost;
	this.projectileType = projectileType;
	this.attackSpeed = attackSpeed;
	this.attackSpeedCharge = 0;
	this.selected = false;
	this.level = 1;
    }

    /**
     * Constructs a Tower for Towers without an inital moving speed
     *
     * @param towerType      the type of tower
     * @param color          the color of the tower
     * @param cost           the cost of the tower
     * @param attackPower    the attackpower or "damage" of the tower
     * @param range          the range in which the tower can attack
     * @param upgradeCost    the cost to upgrade the tower
     * @param projectileType the type of projectile the tower uses
     * @param attackSpeed    the attackspeed of the tower. Higher attackspeed means a longer time to attack
     * @param size           the size of the tower
     */
    protected Tower(TowerType towerType, Color color, int cost, int attackPower, int range, int upgradeCost, ProjectileType projectileType,
		    int attackSpeed, double size)
    {
	super(color, size, attackPower);
	this.cost = cost;
	this.range = range;
	this.towerType = towerType;
	this.upgradeCost = upgradeCost;
	this.projectileType = projectileType;
	this.attackSpeed = attackSpeed;
	this.attackSpeedCharge = 0;
	this.selected = false;
	this.level = 1;
    }

    /**
     * returns wheter a attack on the targeted entity is possible
     *
     * @return true if attack is possible otherwise false
     */
    @Override public boolean canAttack(Entity entity) {

	// the tower has a recharge rate and if it has not yet
	// recharged then charge up a bit and return false
	// ----- the inspection warning about using map here does not fit as we are not only returning a constant value ----
	// ----- but also adding logic to how towers "charge" their firing rate. -----
	if (attackSpeedCharge != attackSpeed) {
	    attackSpeedCharge++;
	    return false;
	} else if (!super.canAttack(entity)) {
	    return false;
	} else if (HelperFunctions.isNear(position, entity.getPosition(), range)) {
	    // set that the tower needs to recharge...
	    attackSpeedCharge = 0;
	    // and return that the tower can attack
	    return true;
	} else {
	    return false;
	}

    }

    /**
     * Used by towers to attack entities. Returns the projectile object the tower is attacking with.
     *
     * @param enemy the enemey to attack
     *
     * @return the projectile to attack with
     */
    public Projectile createProjectileAttack() {

	Projectile projectile = EntityFactory.getProjectile(projectileType);
	//----- There is no problem with targetEntity being null. It is handled so the inspections are incorrent.
	projectile.decideTarget(targetEntity);
	projectile.setPosition(position);
	projectile.setAttackPower(attackPower);

	return projectile;
    }

    /**
     * Decides if the targetEntity should be changed. The chosen target enemy is the closest one in relation to the tower.
     *
     * @param entity the entity to check for an attack
     */

    @Override public void decideTarget(Entity entity) {
	super.decideTarget(entity);

	// the target is chosen by first checking if the currently targeted enemy can be attacked.
	// if not we want to change target to the one the method is called with
	if (!targetEntity.canBeAttacked()) {
	    targetEntity = entity;
	    return;
	}

	// then we check the distance in relation with the tower for both potential enemy and the currently targeted
	// if the current enemy is closer than the targetet one then we change target

	Point2D enemyPos = entity.getPosition();
	Point2D targetEnemyPos = targetEntity.getPosition();

	Point2D targetRelativePosition =
		new Point2D.Double(position.getX() - targetEnemyPos.getX(), position.getY() - targetEnemyPos.getY());
	Point2D potentialTargetRelativeDistance = new Point2D.Double(position.getX() - enemyPos.getX(), position.getY() - enemyPos.getY());

	double targetDistance = Math.hypot(targetRelativePosition.getX(), targetRelativePosition.getY());
	double potentialTargetDistance = Math.hypot(potentialTargetRelativeDistance.getX(), potentialTargetRelativeDistance.getY());

	if (potentialTargetDistance < targetDistance) {
	    targetEntity = entity;
	}
    }


    /**
     * Used by Towers to do something unique.
     */

    public void activate() {
	// empty by default is the desired behaviour since all Tower objects don't do "something unique"
	// the inspection is therefore incorrect
    }

    /**
     * draws a Tower on the position as a square with the designated Color. Also draws the tower level on the tower. When tower is selectd
     * draws the range of the tower
     *
     * @param g2d       the grapichs object to draw with
     * @param gameScale the scale of the game in which the Tower needs to draw with
     */

    @Override public void draw(final Graphics2D g2d, final int gameScale) {

	int towerSize = (int) (gameScale * size);
	double towerPosX = position.getX();
	double towerPosY = position.getY();

	final int towerOffset = gameScale / 2 - towerSize / 2; // the ratio to keep a tower centered on a tile
	int drawX = (int) (towerPosX * gameScale) + towerOffset;
	int drawY = (int) (towerPosY * gameScale) + towerOffset;
	g2d.setColor(getColor());
	g2d.fillRect(drawX, drawY, towerSize, towerSize);

	// if the tower is selected then paint a circle around it to show the range of the tower
	if (selected) {

	    int pixelRange = range * gameScale;

	    int circleX = (int) (towerPosX * gameScale) - pixelRange + gameScale / 2;
	    int circleY = (int) (towerPosY * gameScale) - pixelRange + gameScale / 2;
	    int diameter = range * 2;

	    g2d.drawOval(circleX, circleY, diameter * gameScale, gameScale * diameter);
	}
	// display the level of the currently selected tower
	g2d.setColor(Color.white);
	final int textScale = 3;
	int textSize = gameScale / textScale;
	g2d.setFont(new Font("TimesRoman", Font.PLAIN, textSize));

	// the offset to get the text in the middle of a tower, magic numbers 3 and 2 found by trial and error.
	final int textOffsetScaleX = 3;
	final int textOffsetScaleY = 2;
	final int textOffsetX = gameScale / textOffsetScaleX;
	final int textOffsetY = gameScale / textOffsetScaleY;
	int textPosX = (int) (towerPosX * gameScale) + textOffsetX;
	int textPosY = (int) (towerPosY * gameScale) + textOffsetY;
	g2d.drawString(String.valueOf(level), textPosX, textPosY);
    }

    /**
     * @return String with the tower stats
     */

    public String getDescription() {
	String description =
		towerType + " TOWER\nattack power: " + attackPower + "\ncost: " + cost + "\nupgrade cost: " + upgradeCost + "\nrange: " +
		range + "\nattack speed: " + attackSpeed + "\nprojectiles: " + projectileType;

	return description;
    }


    /**
     * Upgrades the Tower attackpower, increses level and further upgrade cost
     */
    public void upgrade() {
	attackPower += 1;
	upgradeCost += 1;
	level++;

    }

    public void setSelected(final boolean selected) {
	this.selected = selected;
    }

    public int getCost() {
	return cost;
    }

    public int getUpgradeCost() {
	return upgradeCost;
    }

}
