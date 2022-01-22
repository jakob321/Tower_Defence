package se.liu.antos931jakos322.towerdefence.gamelogic;

import se.liu.antos931jakos322.towerdefence.entities.EntityFactory;
import se.liu.antos931jakos322.towerdefence.entities.enemies.Enemy;
import se.liu.antos931jakos322.towerdefence.entities.enemies.EnemyType;


import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

/**
 * WaveMaker creates all Enemy objects which should be used by GameHandler to play the game. The primary function of WaveMaker is to add
 * enemies to the game in the form of "waves". Each tick of the game wavemaker's update() method should be called and all enemies returned
 * from update() should be added to the game. If there are no enemies then update() returns an empty list.
 * <p>
 * WaveMaker controls the waves by counting up field tickCounter each time update() is called. tickCounter is then used to decide when a new
 * wave should be started.
 */
public class WaveMaker
{
    private int waveLevel;
    private int tickCounter;
    private boolean activeWave;
    private int activeWaveCounter;
    private int waveTimer;
    private int waveActiveTime;
    private List<Enemy> enemies;
    private AbstractMap<EnemyType, List<Integer>> spawningRate;

    public WaveMaker() {

	this.waveLevel = 1;
	this.tickCounter = 1;
	this.activeWave = false;
	this.activeWaveCounter = 0;
	this.waveTimer = 400;               // After how many ticks a new wave is made
	this.tickCounter = waveTimer - 1;    // The first wave start after 10 tick
	this.waveActiveTime = 100;          // the wave duration
	this.enemies = new ArrayList<>();
	this.spawningRate = createEnumSpawningRate();

    }

    private static AbstractMap<EnemyType, List<Integer>> createEnumSpawningRate() {
	AbstractMap<EnemyType, List<Integer>> spawningRate = new EnumMap<>(EnemyType.class);

	// ------ the inspections are false positive. These constants are random and are stored in and EnumMap ------
	// ------ the alternativ would be to have 20 varibles declairing these constants, which was decided to not be preferable. ------
	// In the arrayList, the number represent the spawningrate in diffrent phases. Starting at index 0.
	// as an example: in the first phase two STANDARD enemy are spawned. zero Speed enemies zero flying etc.
	// in the second phase 3 STANDARD enemies are spawn. one SPEED, one FLYING etc.
	// this is later used in conjunction with the waveLevel to decide how many in total to spawn.
	spawningRate.put(EnemyType.STANDARD, Arrays.asList(2, 3, 4, 5));
	spawningRate.put(EnemyType.SPEED, Arrays.asList(0, 1, 4, 5));
	spawningRate.put(EnemyType.FLYING, Arrays.asList(0, 1, 2, 3));
	spawningRate.put(EnemyType.EXPLODING, Arrays.asList(0, 1, 2, 3));
	spawningRate.put(EnemyType.BOSS, Arrays.asList(0, 0, 0, 1));

	return spawningRate;
    }

    /**
     * update is activated every tick. update spawn enemies in waves.
     *
     * @return a list of enemies that will be added to the game
     */
    public List<Enemy> update() {
	enemies.clear();
	// if tickcounter is divisible by the wait time ->
	// new wave is started after the wavetimer delay.
	if (tickCounter % waveTimer == 0) {
	    activeWave = true;
	}
	// if there currently is an active wave -> handle the wave
	if (activeWave) {
	    // if the waveCounter is ticked up to how long time the wave should be
	    // then we set that there is no active wave and reset the waveCounter
	    if (activeWaveCounter == waveActiveTime) {
		waveLevel++;
		activeWaveCounter = 0;
		activeWave = false;

	    }
	    // otherwise if there is an active wave we increase the counter and create the wave
	    // for the wave the game is currently on
	    else {
		activeWaveCounter++;
		// during a wave createWave is activated every tick
		// This returns the enemies that will be added
		return createWave();
	    }
	}
	// tick up how far the waveMaker has been active when not in active waves
	tickCounter++;
	// This returns always an empty list
	return enemies;
    }

    public int getWaveLevel() {
	return waveLevel;
    }

    private List<Enemy> createWave() {
	// phases:
	//      1. Generic
	//      2. Generic + speedy + flying + exploding
	//      3. Generic + speedy + flying + exploding
	//      4. Endless (all types)

	spawnEnemy(getSpawningRate(EnemyType.STANDARD), EnemyType.STANDARD);
	spawnEnemy(getSpawningRate(EnemyType.SPEED), EnemyType.SPEED);
	spawnEnemy(getSpawningRate(EnemyType.FLYING), EnemyType.FLYING);
	spawnEnemy(getSpawningRate(EnemyType.EXPLODING), EnemyType.EXPLODING);
	spawnEnemy(getSpawningRate(EnemyType.BOSS), EnemyType.BOSS);


	return enemies;
    }

    /**
     * Uses the Enummap spawningrate to get the spawningrate for enemies in diffrent phases. Increase the spawningrate for every level in a
     * phase.
     *
     * @param enemyType is the enemy the method will return the spawningrate
     *
     * @return how many enemies that of enemytype that will spawn in this level
     */
    private int getSpawningRate(EnemyType enemyType) {
	// The diffrent phases. Starting from index one. To add a phase you need to add a number in the list in the enummap spawningRate
	// The phase are active in the levels bethween them. Example phaseZero is active until the game is at phaseOne's level.
	final int phaseZero = 0;
	final int phaseOne = 5;
	final int phaseTwo = 10;
	final int phaseThree = 15;
	final List<Integer> phases = Arrays.asList(phaseZero, phaseOne, phaseTwo, phaseThree);

	// loop through all phases until we find which phase we are on
	// then decide the spawningrate of the specified EnemyType for that phase
	for (int i = 0; i < phases.size(); i++) {
	    if (waveLevel >= phases.get(i)) {
		// If the enummap have a 0 as spawningrate -> no enemies of specified type will spawn this phase.
		int enemyTypeSpawnAmount = spawningRate.get(enemyType).get(i);
		int specificWaveSpawnAmount = waveLevel - phases.get(i);
		if (enemyTypeSpawnAmount != 0) {
		    return enemyTypeSpawnAmount + specificWaveSpawnAmount;
		}
	    }
	}
	// return 0 by default if for some reason the spawning rate is not returned
	return 0;
    }

    /**
     * Inputs the number of enemies to add during a wave, then adds the enemies evenly during the wave duration.
     *
     * @param amount    is how many enemies that will be added during the wave
     * @param enemyType what type of enemy to add
     */
    private void spawnEnemy(int amount, EnemyType enemyType) {
	if (amount != 0) {
	    // spawn speed is how fast the enemies spawn during a wave.
	    // Lower spawnspeed means fast spawning of the enemy
	    int spawnSpeed = waveActiveTime / amount;
	    // since spawnspeed is an integer we round it up after the division if it is 0
	    if (spawnSpeed == 0) {
		spawnSpeed = 1;
	    }
	    if (activeWaveCounter % spawnSpeed == 0) {
		enemies.add(EntityFactory.getEnemy(enemyType));
	    }
	}
    }

}
