package se.liu.antos931jakos322.towerdefence.gamelogic;

/**
 * Types of tiles used by other classes to play the game in the context of the game Road tiles are used by enemies to contain what path to
 * walk while grass tiles cover the rest of the map which do not contain any road tiles. grass tiles are therefore where Towers and other
 * defensive objects can be placed
 */

public enum TileType
{
    GRASS, ROAD
}
