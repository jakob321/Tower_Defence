package se.liu.antos931jakos322.towerdefence.userinterface;

import com.google.gson.JsonSyntaxException;
import se.liu.antos931jakos322.towerdefence.gamelogic.GameHandler;
import se.liu.antos931jakos322.towerdefence.gamelogic.GameMap;
import se.liu.antos931jakos322.towerdefence.gamelogic.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * StartMenu creates the first interface the player sees when starting the game. Using the createStartMenu() method a new start menu is
 * created. StartMenu also loads the maps.json file by calling GameMap with readNewMap(). Which means StartMenu also handles the eventual
 * exception from reading a resource file.
 * <p>
 * The start menu has buttons which starts the game. All buttons have a map drawn on them, pressing on a button loads GameMap with that map
 * and the startGame() method initialises a new GameHandler and GameViewer with the GameMap object containing the load game map. GameHandler
 * and GameViewer are used for game logic and the main graphical user interface respectily.
 */

public class StartMenu implements GameListener
{
    // ---------- in response the the inspection about similar children ------
    //------- While GameComponent and menuComponent could have an abstract class in this instance it feels redundat -------
    // ------ it will be one class with gameHandler and the method gamechanged. ------
    // ------ in the future if adding more componentes who uses this code it would make sense more sense. ------
    // ----- Otherwise it is just bloating the project with classes -------
    // -----also because Startmenu who uses the gamelistner interface implements its own gameChanged and does not extend JComponent -----
    private JFrame frame;
    private GameHandler gameHandler;
    private GameViewer viewer;
    private final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private GameMap gameMap;


    public StartMenu() {
	this.gameMap = new GameMap();
	this.frame = null;
	this.gameHandler = null;
	this.viewer = null;
    }

    public void createStartMenu() {
	// Load the maps saved in a json file
	readNewMap(gameMap);

	// A gray background
	Color background = new Color(100, 100, 100);

	JPanel mainPanel = new JPanel(new GridLayout(2, 1));
	JPanel header = new JPanel();                // The header is in to top half
	JPanel mapSelect = new JPanel(new GridLayout(2, 4));

	JLabel label = new JLabel("Select a map to play");
	label.setSize(new Dimension(200, 200));

	label.setFont(new Font("TimesRoman", Font.PLAIN, 50));
	label.setForeground(Color.WHITE);
	header.setBackground(background);
	header.add(label);


	for (int i = 0; i < gameMap.getNumberOfMaps(); i++) {
	    ButtonEvent buttonListener = new ButtonEvent(i);
	    gameMap.loadMap(i);
	    JButton b = new JButton();

	    // Every block drawn on the button is 7px * 7px
	    final int tileSize = 7;
	    int mapY = gameMap.getDimension().y;
	    int mapX = gameMap.getDimension().x;
	    final int bufferedImageWidth = mapX * tileSize;
	    final int bufferedImageHeight = mapY * tileSize;
	    BufferedImage lineImage = new BufferedImage(bufferedImageWidth, bufferedImageHeight, BufferedImage.TYPE_INT_RGB);

	    Graphics2D bg2d = lineImage.createGraphics();

	    // Draws every block in the map on the button
	    final int margin = 0;
	    for (int y = 0; y < mapY; y++) {
		for (int x = 0; x < mapX; x++) {
		    Tile currentTile = gameMap.getTile(new Point(x, y));
		    currentTile.draw(bg2d, margin, tileSize);
		}
	    }
	    b.addActionListener(buttonListener);
	    b.setIcon(new ImageIcon(lineImage));
	    b.setSize(new Dimension(bufferedImageWidth, bufferedImageHeight));
	    b.setPreferredSize(new Dimension(bufferedImageWidth, bufferedImageHeight));
	    b.setBorderPainted(false);
	    b.setBackground(background);        // Otherwise the button have a blue outline
	    mapSelect.add(b);
	}

	// Adds the header and the buttons to the main panel
	mainPanel.add(header);
	mainPanel.add(mapSelect);
	mapSelect.setBackground(background);


	frame = new JFrame();
	final int frameHeight = 500;
	final int frameWidth = 700;
	frame.setPreferredSize(new Dimension(frameWidth, frameHeight));

	frame.setLayout(new BorderLayout());
	frame.add(mainPanel);
	frame.pack();
	frame.setVisible(true);
	// set the program to exit when the frame is closed
	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }


    public void readNewMapError(Exception e, GameMap gameMap) {

	String errorMessage = e + "\n Error reading maps.json file. \n Do you want to try again?";
	int userAnswer = JOptionPane.showConfirmDialog(null, errorMessage);
	if (userAnswer == JOptionPane.YES_OPTION) {
	    readNewMap(gameMap);
	} else {
	    System.exit(1);
	}
    }

    /**
     * Load the map file in the class GameMap
     */
    public void readNewMap(GameMap gameMap) {

	try {
	    gameMap.readMap();
	    logger.fine("succesfully loaded maps.json file");

	} catch (IOException ioException) {
	    // this is not a catch and forget. If we cant load the file we ask the user to try again.
	    // and do it until the user has fixed the issue.
	    // Since the issue is not with the code itself but rather likely folder permissions

	    // attempt ask the user to try again
	    // write what has happend and the stacktrace to the log file
	    // the stacktrace is also found in the console
	    String ioError = " could not find or read maps.json file, asking user to try again";
	    logger.log(Level.WARNING, ioError, ioException);
	    readNewMapError(ioException, gameMap);
	} catch (JsonSyntaxException jsonSyntaxException) {
	    // handled the same way as ioExceltion with the difference being non valid text was inside the file
	    // the inspection error about catch and forget also has the same explination
	    String jsonError = "maps.json does not contain valid json syntax, asking user to try again";
	    logger.log(Level.WARNING, jsonError, jsonSyntaxException);
	    readNewMapError(jsonSyntaxException, gameMap);
	}

    }

    /**
     * StartGame creates the class objects to run the game.
     */
    public void startGame(int mapIndex) {

	gameMap.loadMap(mapIndex);

	gameHandler = new GameHandler(gameMap);
	viewer = new GameViewer(gameHandler, GAME_SCALE);
	gameHandler.addListener(this);
	viewer.createInterface();

    }

    @Override public void gameChanged() {
	boolean gameOver = gameHandler.isGameOver();
	if (gameOver) {
	    int option = JOptionPane
		    .showOptionDialog(null, "Do you want to play again?", "Choose", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
				      null, null, null);
	    if (option == JOptionPane.YES_OPTION) {
		gameHandler = null;
		viewer.dispose();
		viewer = null;
		frame.setVisible(true);
	    } else {
		System.exit(0);
	    }
	}
    }

    /**
     * The class is created with a index representing a map. When a button is clicked the method actionPerformed is activated.
     */
    public class ButtonEvent extends AbstractAction
    {

	private int index;

	public ButtonEvent(int i) {
	    this.index = i;
	}

	@Override public void actionPerformed(final ActionEvent e) {

	    startGame(index);
	    frame.setVisible(false);
	}
    }

}
