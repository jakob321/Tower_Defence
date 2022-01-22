package se.liu.antos931jakos322.towerdefence.gamelogic;

import se.liu.antos931jakos322.towerdefence.entities.enemies.Enemy;
import se.liu.antos931jakos322.towerdefence.entities.projectiles.Projectile;
import se.liu.antos931jakos322.towerdefence.entities.towers.Tower;
import se.liu.antos931jakos322.towerdefence.other.HelperFunctions;
import se.liu.antos931jakos322.towerdefence.userinterface.GameListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Gamehandler is the core class which contains the logic for starting and running the game. Gamehandlers main way of running the game is by
 * using the tick() method which is activated by a timer.
 * <p>
 * when the tick() method is called the game progresses one step by calling the methods handeling entity's Since GameHandler controls the
 * game that also means it handles the interactions between the GameMap and entities. Which means for example GameHandler knows when enemies
 * have come to the end of the game map and the player should take damage.
 * <p>
 * Therefore GameHandler contains all Enemy, Projectile and Tower objects placed on the map. These are represented by the fields towers,
 * enemies, and projectiles respectively. Since these objects are all Entity this could be handled in one list by using polymorphism.
 * However it was decided it would make the handling of the objects unnecesarily complex since every Entity type has its own way of being
 * handeld in GameHandler
 * <p>
 * <p>
 * GameHandler also has information for player health, money and wheter the game is over or not.
 * <p>
 * GameHandler contains a waveMaker object which creates waves of enemies by returning a list with enemies if there is a wave happening.
 */

public class GameHandler
{
    private GameMap gameMap;
    private List<Tower> towers;
    private List<Enemy> enemies;
    private List<Projectile> projectiles;
    private int health;
    private int money;
    private List<GameListener> gameListeners;
    private WaveMaker waveMaker;
    private Timer tickTimer;
    private boolean gamePaused;
    private boolean gameOver;

    /**
     * Constructs a GameHandler object with the specified gameMap
     *
     * @param gameMap the gameMap which the game should be played on
     */
    public GameHandler(GameMap gameMap) {
	this.gameMap = gameMap;
	this.enemies = new ArrayList<>();
	this.towers = new ArrayList<>();
	this.projectiles = new ArrayList<>();
	this.health = 100;
	this.money = 10;
	this.gameListeners = new ArrayList<>();
	this.waveMaker = new WaveMaker();
	this.tickTimer = null; // timer is set when the game starts in method startgame
	this.gamePaused = true;
	this.gameOver = false;
    }

    /**
     * Calls all methods which progress the game one step or "tick"
     */

    public void tick() {

	inspectHealth();
	activateTowers();
	moveProjectiles();
	createEnemies();
	moveEnemy();
	notifyListeners();

    }

    /**
     * Adds all enemies waveMaker decides make up a new wave
     */

    public void createEnemies() {
	addAllEnemies(waveMaker.update());
    }

    /**
     * Adds all enemies in a list to the game
     *
     * @param enemiesToAdd enemies which should be added to the game
     */

    public void addAllEnemies(List<Enemy> enemiesToAdd) {
	for (Enemy enemy : enemiesToAdd) {
	    enemy.setLastPosition(gameMap.getLastPath());
	    enemies.add(enemy);
	}
    }

    /**
     * Sets game over and pauses the game if the player's life is 0 or less
     */
    public void inspectHealth() {

	if (health <= 0) {
	    pauseGame();
	    gameOver = true;
	}
    }

    /**
     * Initialises the game timer if it is not set and starts the tickertimer. Also sets gamePaused to false
     */

    public void startGame() {
	if (tickTimer == null) {
	    final int tickDelay = 30;
	    tickTimer = new Timer(tickDelay, new DoOneStep());
	}

	tickTimer.start();
	gamePaused = false;
    }

    /**
     * Stops the game timer and sets gamePaused to false
     */
    public void pauseGame() {
	tickTimer.stop();
	gamePaused = true;
    }

    /**
     * Handles the logic for game projectiles. Does so by moving the projectiles, checking enemies if there is anyone the projectile can
     * attack. Removes the projectiles that cannot penetrate more enemies. Also removes projectiles which are outside of the game map.
     */

    public void moveProjectiles() {
	List<Projectile> projectilesToRemove = new ArrayList<>();

	for (Projectile projectile : projectiles) {

	    projectile.move();
	    // check if a projectile can attack an enemy and in that case attack the enemy
	    for (Enemy enemy : enemies) {
		if (projectile.canAttack(enemy)) {
		    projectile.attack(enemy);

		}
	    }
	    // if the projectile cannot penetrate through any more enemies, remove it
	    if (projectile.getPenetrationAmount() <= 0) {
		projectilesToRemove.add(projectile);
	    }

	    Point2D projectilePosition = projectile.getPosition();
	    // if there are any projectiles outside the game add them to the remove list
	    boolean lessThanBounds = projectilePosition.getY() < 0 || projectilePosition.getX() < 0;
	    boolean greaterThanBounds =
		    projectilePosition.getX() > gameMap.getDimension().x || projectilePosition.getY() > gameMap.getDimension().y;
	    if (lessThanBounds || greaterThanBounds) {
		projectilesToRemove.add(projectile);
	    }
	}
	// remove the projectiles designated to be removed
	projectiles.removeAll(projectilesToRemove);
    }

    /**
     * Handles the logic for towers every tick. Does so by first activating the Tower's special functionallity method Then all towers decide
     * which enemy to target. Lastley towers attack the enemies which it has chosen to target
     */

    public void activateTowers() {
	for (Tower tower : towers) {

	    tower.activate();
	    // let the tower decide who to attack
	    for (Enemy enemy : enemies) {
		tower.decideTarget(enemy);
	    }
	    // if the enemy can be attacked
	    if (tower.canAttack(tower.getTargetEntity())) {
		// then send a projectile to that enemy
		Projectile projectile = tower.createProjectileAttack();
		projectiles.add(projectile);
	    }
	}
    }

    /**
     * Damages the player with the specified amount
     *
     * @param damage amount of damamge player should take
     */
    public void takeDamage(int damage) {
	health -= damage;
    }


    /**
     * Handles the main logic for enemies. Does so by mainly moving all enemies along the pre decided game path. This means it also handles
     * wheter an enemy is defeated or has come to the end of the path. In both cases the enemy is removed from the game. If it came to the
     * end the player takes damage.
     */

    public void moveEnemy() {

	List<Enemy> removeEnemies = new ArrayList<>();
	List<Enemy> addEnemies = new ArrayList<>();
	for (Enemy enemy : enemies) {
	    int pathProgress = enemy.getPathProgress();

	    enemy.setMovePosition(gameMap.getPathPosition(pathProgress));
	    enemy.move();
	    // if the enemy has come to the end of the map damage the player
	    if (enemy.isFinished()) {

		takeDamage(enemy.getAttackPower());
		removeEnemies.add(enemy);
	    }
	    // if an enemy is defeated remove it and give the player money
	    else if (enemy.getHealth() <= 0) {
		addEnemies.addAll(enemy.split());          // If the enemy is "splittable" then add the splitts
		removeEnemies.add(enemy);
		money += enemy.getRewardMoney();
	    }

	}
	addAllEnemies(addEnemies);
	enemies.removeAll(removeEnemies);

    }


    /**
     * Adds a tower and takes away the cost from the  money
     *
     * @param tower the tower to add to the game
     */

    public void addTower(Tower tower) {
	money -= tower.getCost();
	towers.add(tower);
	notifyListeners();
    }

    /**
     * checks if the player can afford to place the tower and also if the tower itself can be placed on that position.
     *
     * @param tower the tower to check
     *
     * @return if the tower can be placed
     */

    public boolean canPlaceTower(Tower tower) {
	Point2D towerPos = tower.getPosition();
	Point towerPosPoint = new Point((int) towerPos.getX(), (int) towerPos.getY());

	TileType desiredPlacementTile = gameMap.getTile(towerPosPoint).getTileType();

	// if the player attempts to place the tower on something thats not grass return false
	if (desiredPlacementTile != TileType.GRASS) {
	    return false;
	}
	// if there is another tower on the tile return fakse
	for (Tower otherTowers : towers) {
	    if (otherTowers.getPosition().equals(towerPos)) {
		return false;
	    }
	}
	// if the player cannot afford the tower return false
	if (money - tower.getCost() < 0) {
	    return false;
	}
	return true;
    }


    public int getMoney() {
	return money;
    }

    /**
     * Notifies all graphics listners that the game some graphical elements have moved
     */

    public void notifyListeners() {
	for (GameListener listener : gameListeners) {
	    listener.gameChanged();
	}
    }

    public void addListener(GameListener listener) {
	gameListeners.add(listener);
    }

    /**
     * Returns the tower near the position. If there is no tower returns null.
     *
     * @param position the position to check for
     *
     * @return the tower near the position
     */
    public Tower getTowerNearPosition(Point2D position) {
	final double distance = 0.5;
	for (Tower tower : towers) {
	    if (HelperFunctions.isNear(tower.getPosition(), position, distance)) {
		return tower;
	    }
	}
	return null;
    }

    /**
     * Checks if the player can afford to upgrade the tower
     *
     * @param tower the tower to check
     *
     * @return if the tower is upgradable
     */

    public boolean isTowerUpgradable(Tower tower) {
	if (money - tower.getUpgradeCost() < 0) {
	    return false;
	}
	return true;
    }

    /**
     * upgrades a tower
     *
     * @param tower the tower to upgrade
     */

    public void upgradeTower(Tower tower) {
	money -= tower.getUpgradeCost();
	tower.upgrade();
	notifyListeners();
    }

    /**
     * sets the tower to be selected
     *
     * @param tower the tower to select
     */
    public void selectTower(Tower tower) {
	tower.setSelected(true);
	notifyListeners();
    }

    /**
     * Sets the tower to not be selected
     *
     * @param tower the tower to deselect
     */

    public void deselectTower(Tower tower) {
	tower.setSelected(false);
	notifyListeners();
    }

    public Point getMapDimensions() {
	return gameMap.getDimension();
    }

    public Tile getMapTile(Point pos) {
	return gameMap.getTile(pos);
    }

    public boolean isGamePaused() {
	return gamePaused;
    }

    public int getEnemyAmount() {
	return enemies.size();
    }

    public int getProjectileAmount() {
	return projectiles.size();
    }

    public int getTowerAmount() {
	return towers.size();
    }

    public Projectile getProjectile(int number) {

	return projectiles.get(number);
    }

    public Tower getTower(int number) {
	return towers.get(number);
    }

    public Enemy getEnemy(int index) {

	return enemies.get(index);
    }

    public boolean isGameOver() {
	return gameOver;
    }

    public int getHealth() {
	return health;
    }

    public int getLevel() {
	return waveMaker.getWaveLevel();
    }

    /**
     * The Action which the GameHandler timer performs every time it activates
     */
    public class DoOneStep extends AbstractAction
    {

	@Override public void actionPerformed(final ActionEvent e) {
	    tick();

	}
    }


}
