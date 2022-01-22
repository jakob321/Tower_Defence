package se.liu.antos931jakos322.towerdefence.entities;


import se.liu.antos931jakos322.towerdefence.other.HelperFunctions;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Entity is an abstract class which represnts an entity existing in the game which can be interacted with by the player or by other Entity
 * objects Entity can be attacked and take damage, move, has an attackpower and health. However Entity's are only supposed to be able to
 * take damage if the canBeAttacked method allows it.
 * <p>
 * When movePosition is set the Entity will move towards that location if move() is called
 */

public abstract class Entity
{

    protected Point2D position;
    protected Color color;
    protected double size;
    protected double speed;
    protected Point2D movePosition;
    protected int attackPower;
    protected int health;

    /**
     * Constructs an entity with inital speed and health points
     *
     * @param health      the entity health points
     * @param color       the entity color
     * @param size        the entity size
     * @param speed       the entity speed
     * @param attackPower attackpower or "damage" of the Entity
     */

    protected Entity(final Color color, final double size, final double speed, int attackPower, int health)
    {
	this.position = null;
	this.color = color;
	this.size = size;
	this.speed = speed;
	this.movePosition = null;
	this.attackPower = attackPower;
	this.health = health;
    }

    /**
     * Constructs an entity with inital speed
     *
     * @param color       the entity color
     * @param size        the entity size
     * @param speed       the entity speed
     * @param attackPower attackpower or "damage" of the Entity
     */


    protected Entity(final Color color, final double size, final double speed, int attackPower) {
	this.position = null;
	this.color = color;
	this.size = size;
	this.speed = speed;
	this.movePosition = null;
	this.attackPower = attackPower;
	this.health = 0;
    }

    /**
     * Constructs an entity without inital speed
     *
     * @param color       the entity color
     * @param size        the entity size
     * @param attackPower attackpower or "damage" of the Entity
     */
    protected Entity(final Color color, final double size, int attackPower) {
	this.attackPower = attackPower;
	this.position = null;
	this.color = color;
	this.size = size;
	this.speed = 0;
	this.movePosition = null;
	this.health = 0;
    }

    /**
     * Used by entites to move towards movePosition When the entity is near the location it will stop moving
     */

    public void move() {

	// if the entity is not near the movePosition it will continue moving
	double newY;
	double newX;
	final double distance = 0.2;
	if (!HelperFunctions.isNear(position, movePosition, distance)) {

	    double deltaX = movePosition.getX() - position.getX();
	    double deltaY = movePosition.getY() - position.getY();

	    // normalise the delta x and y direction to an angle
	    double direction = Math.atan2(deltaY, deltaX);
	    // use the angle to calcutate the x and y  relations
	    double directionY = Math.sin(direction);
	    double directionX = Math.cos(direction);

	    newY = position.getY() + directionY * speed;
	    newX = position.getX() + directionX * speed;
	}
	// otherwise the entity will stop moving
	else {
	    newY = position.getY();
	    newX = position.getX();
	}
	// change the actual position with the calculated coordinates
	this.position = new Point2D.Double(newX, newY);
    }

    /**
     * Damages the entity by lower its health with the damage amount. When health is 0 it cannot go lower.
     *
     * @param damage amount of damage to deal
     */

    public void takeDamage(int damage) {
	if (health - damage < 0) {
	    health = 0;
	} else {
	    health -= damage;
	}

    }

    /**
     * Implements the method to check wheter an entity can be attacked or not
     *
     * @return if the entity can be attacked
     */
    public abstract boolean canBeAttacked();

    // -------- in respone to inspection about unsued method. While draw is not implementet in any polymorphic way --------
    // -------- it makes sense to force children to implement draw ------
    // -------- also see GameComponent's paintComponent method for explination on why polymorphism is not used -------
    public abstract void draw(Graphics2D g2d, int gameScale);

    public void setPosition(final Point2D position) {
	this.position = position;
    }

    public void setMovePosition(final Point2D movePosition) {
	this.movePosition = movePosition;
    }

    public Point2D getPosition() {
	return position;
    }

    public Color getColor() {
	return color;
    }

    public double getSize() {
	return size;
    }

    public int getHealth() {
	return health;
    }

    public void setAttackPower(final int attackPower) {
	this.attackPower = attackPower;
    }

    public int getAttackPower() {
	return attackPower;
    }
}
