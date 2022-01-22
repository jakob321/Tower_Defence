package se.liu.antos931jakos322.towerdefence.entities;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * EntityAttacker is a type of Entity with the extended functionallity of attacking other entities.
 * <p>
 * EntityAttackers use the logic in decideTarget(Entity) to choose an Entity target. EntityAttackers can use this in conjunction
 * canAttack(Entity) to check if the specifik target can be attack By default EntityAttacker can not be attacked which is decided by the
 * overridden canBeAttacked() method
 * <p>
 * EntityAttacker is primarily used by Towers and Projectiles to attack Enemy objects.
 */


public abstract class EntityAttacker extends Entity
{
    protected Entity targetEntity;
    protected Point2D startPosition;

    /**
     * Constructs an EntityAttacker that also has inital speed on creation
     *
     * @param color       the color of the entity
     * @param size        the size of the entity
     * @param speed       movement speed of the entity
     * @param attackPower the amount of damage the entity deals
     */

    protected EntityAttacker(final Color color, final double size, final double speed, final int attackPower) {
	super(color, size, speed, attackPower);
	this.targetEntity = null;
	this.startPosition = null;
    }

    /**
     * Constructs an EntityAttacker without inital speed
     *
     * @param size        the size of the entity
     * @param speed       movement speed of the entity
     * @param attackPower the amount of damage the entity deals
     */
    protected EntityAttacker(final Color color, final double size, final int attackPower) {
	super(color, size, attackPower);
	this.targetEntity = null;
	this.startPosition = null;
    }

    /**
     * Decides wheter to change the currently targeted enemy Can be used by subclases to implement further logic about how to choose an
     * Entity target
     *
     * @param entity the entity to attack
     */

    protected void decideTarget(Entity entity) {

	if (targetEntity == null) {
	    targetEntity = entity;
	}

    }

    /**
     * The method entites used to decide if this Entity can be attacked By default EntityAttackers can not be attacked.
     *
     * @return false
     */
    @Override public boolean canBeAttacked() {
	return false;
    }

    /**
     * Decides wheter the targeted Entity can be attacked or not. Can be used by subclases to implement further logic if an entity can be
     * attacked or not
     *
     * @param entity the entity to attack
     */

    protected boolean canAttack(Entity entity) {

	if (entity == null) {
	    return false;
	}
	if (!entity.canBeAttacked()) {
	    return false;
	}

	return true;
    }

    public Entity getTargetEntity() {
	return targetEntity;
    }

    /**
     * sets the position for the EntityAttacker Also sets startPosition to that position
     *
     * @param position
     */
    @Override public void setPosition(final Point2D position) {
	super.setPosition(position);
	if (startPosition == null) {
	    startPosition = position;
	}
    }
}
