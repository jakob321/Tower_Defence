package se.liu.antos931jakos322.towerdefence.entities.enemies;


import se.liu.antos931jakos322.towerdefence.entities.Entity;
import se.liu.antos931jakos322.towerdefence.entities.EntityFactory;
import se.liu.antos931jakos322.towerdefence.other.HelperFunctions;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Enemy is the Abstract class for offensive entities (enemies) in the game which also extends Entity
 * <p>
 * Enemies moves along a path, when the enemy reaches the end, the flag finished is set to true. When an anemy is finished GameHandler
 * removes the enemy from the game and give the player damage.
 * <p>
 * Before an enemy is removed split() is called. Split creates a set number of new enemies. By defult the new number of created enemies is
 * 0.
 */

public abstract class Enemy extends Entity
{
    protected int pathProgress;
    protected final int maxHealth;
    protected final int rewardMoney;
    protected boolean finished;
    protected int lastPosition;
    protected final static Random RND = new Random();
    protected EnemyType splitEnemyType;
    protected int numberOfSplits;
    protected int splitDistance;


    /**
     * Constructs an Enemy with inital speed and health points Used by enemies who do not split when health reaches 0
     *
     * @param maxHealth   the enemy health points
     * @param speed       the speed of the enemy
     * @param color       the color of the enemy
     * @param size        the size of an enemy relative to the size of a tile
     * @param attackPower attackpower or "damage" the the enemy can give to the player
     */
    protected Enemy(final int maxHealth, final double speed, final Color color, final double size, int attackPower) {
	super(color, size, speed, attackPower, maxHealth);
	this.maxHealth = maxHealth;
	this.pathProgress = 0;
	this.rewardMoney = 1;
	this.finished = false;
	this.lastPosition = -1;
	this.numberOfSplits = 0;
	this.splitEnemyType = null;
	this.splitDistance = 0;
    }

    /**
     * Constructs an Enemy with inital speed and health points Used by enemies who split when health reaches 0
     *
     * @param maxHealth      the enemy health points
     * @param speed          the speed of the enemy
     * @param color          the color of the enemy
     * @param size           the size of an enemy relative to the size of a tile
     * @param attackPower    attackpower or "damage" the the enemy can give to the player
     * @param numberOfSplits how many enemies this enemy can create
     * @param splitEnemyType the type of enemy this enemy creates
     * @param splitDistance  the distans relative to this enemy postion the new enemies are created
     */
    protected Enemy(final int maxHealth, final double speed, final Color color, final double size, int attackPower, int numberOfSplits,
		    EnemyType splitEnemyType, int splitDistance)
    {

	super(color, size, speed, attackPower, maxHealth);
	this.maxHealth = maxHealth;
	this.pathProgress = 0;
	this.rewardMoney = 5;
	this.finished = false;
	this.lastPosition = -1;
	this.numberOfSplits = numberOfSplits;
	this.splitEnemyType = splitEnemyType;
	this.splitDistance = splitDistance;

    }

    /**
     * Returns if the Enemy can be attacked. False if health is less than 0 or the enemy is finished
     *
     * @return if the entity can be attacked
     */

    @Override public boolean canBeAttacked() {
	if (isFinished()) {
	    return false;
	}
	if (health <= 0) {
	    return false;
	} else {
	    return true;
	}
    }

    /**
     * Draws the enemy on the position it is in the game. Also draws a healthbar for the enemy
     *
     * @param g2d       the graphics object to draw with
     * @param gameScale the scale of the game graphics
     */

    public void draw(final Graphics2D g2d, final int gameScale) {


	g2d.setColor(color);
	int drawPositionX = (int) (position.getX() * gameScale);
	int drawPositionY = (int) (position.getY() * gameScale);

	final int size = (int) (gameScale * this.size);
	// since the object is drawn from the "corner" and not in the middle
	// we find this offest by writing up two squares on a piece of paper calculating how much x and y must move to center the small one.
	final int offset = gameScale / 2 - size / 2;
	double percentageHealth = (double) health / maxHealth;
	g2d.fillOval(drawPositionX + offset, drawPositionY + offset, size, size);

	// below is for the healthbar
	// first we add a red bar to background...
	final int healthBarScale = 5;
	final int healthBarHeight = gameScale / healthBarScale;
	g2d.setColor(Color.red);
	g2d.fillRect(drawPositionX + offset, drawPositionY, size, healthBarHeight);

	// then on top of the red bar we add the green representing the current health
	// which gets lower with the remaning percentageHealth
	g2d.setColor(Color.green);
	g2d.fillRect(drawPositionX + offset, drawPositionY, (int) (percentageHealth * size), healthBarHeight);

    }

    /**
     * Moves the enemy towards movePosition. The pathProgress is increased once the Enemy reaches the position it was moving towerds This
     * allows the Enemy to follow the path
     */
    @Override public void move() {
	// Gives Enemy a starting position
	if (position == null) {
	    position = movePosition;
	    pathProgress += 1;
	}
	// move enemy towards the movePosition
	super.move();
	// If this is the last block --> Enemy is done with the path
	final double distance = 0.2;

	// if the enemy is near the position it was moving towards
	// increase the path progression
	if (HelperFunctions.isNear(position, movePosition, distance)) {
	    if (pathProgress == lastPosition) {
		finished = true;
		return;
	    }
	    pathProgress += 1;

	}
    }


    /**
     * Creates severeal more enemies. The amount of enemies added is numberOfSplits and they are spawned a random direction and distance
     * from the Enemy. The maximum distance is SplitDistance
     * <p>
     * and the type of enemy created is the splitEnemyType
     *
     * @return a list of the created enemies
     */
    public List<Enemy> split() {
	List<Enemy> enemies = new ArrayList<>();

	for (int i = 0; i < numberOfSplits; i++) {
	    Enemy s = EntityFactory.getEnemy(splitEnemyType);
	    //----- There is no problem with null. It is handled so the inspections are incorrent ------
	    s.setPosition(
		    new Point2D.Double(position.getX() + splitRandomPos(splitDistance), position.getY() + splitRandomPos(splitDistance)));
	    s.setPathProgress(pathProgress);
	    enemies.add(s);
	}

	return enemies;
    }

    private void setPathProgress(final int pathProgress) {
	this.pathProgress = pathProgress;
    }

    /**
     * creates a random number in the span. The range is -span/2 to span/2
     *
     * @param span the range in the random number can generate
     *
     * @return a random number within the range of the span
     */
    private int splitRandomPos(final int span) {
	final int minPosition = -(span / 2);
	int randomPos = RND.nextInt(span) + minPosition;
	return randomPos;
    }

    public boolean isFinished() {
	return finished;
    }

    public int getRewardMoney() {
	return rewardMoney;
    }

    public void setLastPosition(int position) {
	lastPosition = position;
    }

    public int getPathProgress() {
	return pathProgress;
    }


}
