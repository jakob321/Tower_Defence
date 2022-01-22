package se.liu.antos931jakos322.towerdefence.entities.projectiles;

import se.liu.antos931jakos322.towerdefence.entities.Entity;
import se.liu.antos931jakos322.towerdefence.entities.EntityAttacker;
import se.liu.antos931jakos322.towerdefence.other.HelperFunctions;

import java.awt.*;
import java.awt.geom.Point2D;


/**
 * Projectile is the abstract class for all projectiles in the game. Projectile extends the EntityAttacker class which means it inherits
 * logic for attacking other Entity objects.
 * <p>
 * Projectile represents an object that moves and can do damage to Entity objects
 * <p>
 * <p>
 * Projectiles have various unique properties: PenetrationAmount - represents how many times the projectile can do damage before being
 * "destroyed" Example use: Projectile gets created by another entity that tells projectile what to target when the projectile is near the
 * target it does damage to it.
 */


public abstract class Projectile extends EntityAttacker
{

    protected int penetrationAmount;
    protected Entity prevAttackedEntity;

    protected Projectile(final Color color, final double size, double speed, int penetrationAmount) {
	super(color, size, speed, 0);
	this.penetrationAmount = penetrationAmount;
	this.prevAttackedEntity = null;
    }

    /**
     * Move moves the projectile forwards towards the designated moveposition. To keep the projectile from stopping the movePosition is
     * increased with the delta x and y coordinates
     */

    @Override public void move() {
	super.move();
	double moveX = movePosition.getX();
	double moveY = movePosition.getY();
	final int deltaScale = 10;
	// we increse the delta with a constant to make sure when the projectile
	// is shot near an enemy it does not stop from being to near
	double deltaX = deltaScale * (moveX - position.getX());
	double deltaY = deltaScale * (moveY - position.getY());
	// we then increase the target position with the deltax and deltay to keep the projectile from stopping once it reaches the position
	Point2D newPos = new Point2D.Double(movePosition.getX() + deltaX, movePosition.getY() + deltaY);
	movePosition = newPos;
    }

    /**
     * canAttack returns wheter a projectile can attack an entity
     *
     * @param entity the entity object that is being checked if it is possible to attack
     *
     * @return true or false if an attack is possible or not
     */

    @Override public boolean canAttack(Entity entity) {

	if (!super.canAttack(entity)) {
	    return false;
	}

	// the projectile hit range is increased with a constant to make sure it actually hits when near an entity
	final double hitRangeScale = 1.5;
	double hitRange = size * hitRangeScale;
	if (HelperFunctions.isNear(position, entity.getPosition(), hitRange)) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * Projectile contains no extra logic for selecting target. Sets the Entity given as the current target.
     * <p>
     * Also changes movePostion so the projectile moves towards the Entity
     *
     * @param entity the entity to attack
     */
    @Override public void decideTarget(Entity entity) {
	super.decideTarget(entity);
	targetEntity = entity;
	movePosition = entity.getPosition();
	position = startPosition;
    }

    /**
     * attacks an entity and ignores if it has already attack that enemy
     *
     * @param entity the entity object to attack
     */

    public void attack(Entity entity) {
	if (prevAttackedEntity == null) {
	    prevAttackedEntity = entity;
	} else if (entity.equals(prevAttackedEntity)) {
	    return;
	}
	entity.takeDamage(attackPower);
	reducePenetration();
	prevAttackedEntity = entity;
    }

    /**
     * Reduces the penetration value for the projectile but not lower than 0
     */
    public void reducePenetration() {
	if (penetrationAmount > 0) {
	    penetrationAmount -= 1;
	}

    }


    /**
     * draws the projectile as a circle with the specified size and color fields with an offset to center the projectile
     *
     * @param g2d       grapchis object
     * @param gameScale the scale of the game graphics
     */

    @Override public void draw(final Graphics2D g2d, final int gameScale) {

	g2d.setColor(color);

	final int size = (int) (gameScale * this.size);
	final int offset = gameScale / 2 - size / 2;

	int drawPositionX = (int) (position.getX() * gameScale) + offset;
	int drawPositionY = (int) (position.getY() * gameScale) + offset;

	g2d.fillOval(drawPositionX, drawPositionY, size, size);
    }

    public int getPenetrationAmount() {
	return penetrationAmount;
    }

}
