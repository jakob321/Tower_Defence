package se.liu.antos931jakos322.towerdefence.userinterface;

import se.liu.antos931jakos322.towerdefence.entities.EntityFactory;
import se.liu.antos931jakos322.towerdefence.entities.towers.Tower;
import se.liu.antos931jakos322.towerdefence.entities.towers.TowerType;
import se.liu.antos931jakos322.towerdefence.gamelogic.GameHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * GameViewer controls the main user interface of the game and displays the game. GameViewer takes input from the user in the form of
 * buttons presses and clicks on its panels. GameViewer can therefore for example decide where game objects should be placed by calculating
 * the pixel to gamescale relation when the player clicks on the game panel
 * <p>
 * This means the entire game can be played and be seen by the player becuase of gameviewer
 */
public class GameViewer
{
    private GameHandler gameHandler;
    private JFrame frame;
    private TowerType selectedTower;
    private JTextArea towerDescription;
    private Tower clickedTower;
    private int gameScale;
    private final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * Constructs a GameViewer object which contains the game userinterface
     *
     * @param gameHandler the gamehandler object to play the game on
     * @param gameScale   the graphical scale of the game
     */

    public GameViewer(GameHandler gameHandler, int gameScale) {
	this.gameHandler = gameHandler;
	this.selectedTower = TowerType.NONE;
	this.frame = null;
	this.towerDescription = null;
	this.clickedTower = null;
	this.gameScale = gameScale;
    }

    /**
     * Creates the entire user interface on which the game is played
     * <p>
     * Also shows the frame after completion.
     */

    public void createInterface() {
	final Color backGroundColor = new Color(92, 184, 92);
	MenuComponent menuComponent = new MenuComponent(gameHandler);
	GameComponent gameComponent = new GameComponent(gameHandler);

	gameHandler.addListener(menuComponent);
	gameHandler.addListener(gameComponent);

	// create the panels of the UI In the gridLayout the first agrument represent amout of rows and seconds amout of coloumns
	// in the gridlayout
	// 0 stands for unlimited amount
	JPanel mainPanel = new JPanel();
	JPanel gamePanel = new JPanel(new GridLayout(1, 1));
	JPanel mainMenuPanel = new JPanel(new GridLayout(5, 1));
	JPanel pauseAndQuitPanel = new JPanel(new GridLayout(2, 0));
	JPanel towerUpgradesPanel = new JPanel(new BorderLayout());
	JPanel towerDescriptionPanel = new JPanel(new GridLayout(1, 1));
	JPanel textPanel = new JPanel(new BorderLayout());
	JScrollPane scrollableInteractivePanel =
		new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	JScrollPane textScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	// create the interactive panel
	GridLayout interactivePanelLayout = new GridLayout(0, 2);
	JPanel towerButtonPanel = new JPanel(interactivePanelLayout);
	final int marginScale = 50;
	final int gapMargin = gameScale / marginScale;
	interactivePanelLayout.setHgap(gapMargin);
	interactivePanelLayout.setVgap(gapMargin);


	//menupanel has the information the player needs and is used for choosing towers

	// create and set the buttons for placing towers
	ButtonGroup buttonGroup = new ButtonGroup(); // create button group to deselect buttons when antoher is clicked
	List<TowerType> towerTypes = EntityFactory.getAllTowers();
	towerDescription = new JTextArea("No tower selected");
	final int textSize = 10;
	towerDescription.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, textSize));

	for (TowerType towerType : towerTypes) {
	    // when the button is clicked -> buttonEvent class is activated
	    ButtonEvent buttonEvent = new ButtonEvent(towerType, ButtonType.TOWER_BUTTON);
	    // When a button is activated the other buttons in the same group are deselected
	    JToggleButton b = new JToggleButton();

	    Tower tower = EntityFactory.getTower(towerType);
	    // Create a drawing area at the size of the tower
	    //----- There is no problem with null. It is handled so the inspections are incorrent ------
	    final int bufferedImageWidth = (int) (tower.getSize() * gameScale);
	    final int bufferedImageHeight = (int) (tower.getSize() * gameScale);
	    BufferedImage lineImage = new BufferedImage(bufferedImageWidth, bufferedImageHeight, BufferedImage.TYPE_INT_RGB);
	    Graphics2D bg2d = lineImage.createGraphics();

	    double towerOffset = (1 / 2.0 - tower.getSize() / 2);
	    tower.setPosition(new Point2D.Double(-towerOffset, -towerOffset));
	    tower.draw(bg2d, gameScale);
	    b.addActionListener(buttonEvent);
	    b.setIcon(new ImageIcon(lineImage));
	    b.setBorderPainted(false);
	    b.setBackground(backGroundColor);
	    buttonGroup.add(b);
	    towerButtonPanel.add(b);
	    UIManager.put(b.isSelected(), Color.BLACK);
	}

	//add the text component contaning health, wave level and money
	textPanel.add(menuComponent);

	//gamepanel has the GameComponent and shows the running game
	gamePanel.add(gameComponent);
	gamePanel.setBorder(BorderFactory.createLineBorder(Color.black));
	gamePanel.addMouseListener(new MouseEvent(towerDescription, buttonGroup));

	//adds the textarea describing towers to a scrollpanel and then to the description panel
	textScrollPane.getViewport().add(towerDescription);
	towerDescriptionPanel.add(textScrollPane);
	towerDescriptionPanel.setBackground(backGroundColor);

	// adds the tower buttons to a scrollable panel
	towerButtonPanel.setBackground(backGroundColor);
	scrollableInteractivePanel.getViewport().add(towerButtonPanel);

	// creates the upgrade button and places it in a planel
	towerUpgradesPanel.setBackground(Color.blue);
	JButton upgradeButton = new JButton(new ButtonEvent(ButtonType.UPGRADE));
	upgradeButton.setText("Upgrade selected tower");
	towerUpgradesPanel.add(upgradeButton);

	// create the pause and quit buttons
	JButton quitButton = new JButton(new ButtonEvent(ButtonType.QUIT));
	JButton pauseButton = new JButton();
	pauseButton.setAction(new ButtonEvent(ButtonType.PAUSE, pauseButton));
	// add the pause and quit buttons to the panel. Game starts paused to first text should be "start game" on pause button
	pauseButton.setText("Start game");
	quitButton.setText("Quit game");
	pauseAndQuitPanel.add(pauseButton);
	pauseAndQuitPanel.add(quitButton);

	// now add all panels we have created to the mainMenuPanel
	mainMenuPanel.add(menuComponent);                // EX. level, health
	mainMenuPanel.add(scrollableInteractivePanel);        // The tower buttons
	mainMenuPanel.add(towerDescriptionPanel);        // the tower description
	mainMenuPanel.add(towerUpgradesPanel);                // The upgrade button
	mainMenuPanel.add(pauseAndQuitPanel);                // pasuse and quit button
	final int widthScale = 4;
	final int heightScale = 10;
	int mainMenuWidth = gameScale * widthScale;
	int mainMenuHeight = gameScale * heightScale;
	mainMenuPanel.setPreferredSize(new Dimension(mainMenuWidth, mainMenuHeight));
	mainMenuPanel.setBackground(backGroundColor);

	// add the two panels contaning Menus and game to the main panel
	mainPanel.add(gamePanel);
	mainPanel.add(mainMenuPanel);
	mainPanel.setBackground(backGroundColor);

	// create the frame add the main panel and show the frame
	frame = new JFrame();
	frame.setLayout(new BorderLayout());
	frame.add(mainPanel);
	frame.pack();
	frame.setVisible(true);
	// set the program to exit when the frame is closed
	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    public void dispose() {
	frame.dispose();
    }

    public class ButtonEvent extends AbstractAction
    {

	private TowerType towerType;
	private ButtonType buttonType;
	private JButton jButton;

	/**
	 * Constructs a ButtonEvent with a towerType and buttonType
	 */
	public ButtonEvent(TowerType towerType, ButtonType buttonType) {
	    this.towerType = towerType;
	    this.buttonType = buttonType;
	    this.jButton = null;
	}

	/**
	 * Constructs a ButtonEvent with the type of button
	 *
	 * @param buttonType type of button
	 */

	public ButtonEvent(ButtonType buttonType) {
	    this.towerType = TowerType.NONE;
	    this.buttonType = buttonType;
	    this.jButton = null;
	}

	/**
	 * Constructs a ButtonEvent with a buttonType and a jButton object
	 *
	 * @param buttonType the type of button
	 * @param jButton    the used button
	 */
	public ButtonEvent(ButtonType buttonType, JButton jButton) {
	    this.towerType = TowerType.NONE;
	    this.buttonType = buttonType;
	    this.jButton = jButton;
	}

	/**
	 * actionPerformed performs the action that specified Button types should do.
	 *
	 * @param e
	 */
	@Override public void actionPerformed(final ActionEvent e) {
	    switch (buttonType) {
		// if the button is a upgrade button then check if the tower can be upgraded, in that case upgrade the tower
		// and change the description of the textArea towerDescriton
		case UPGRADE:
		    // player is trying to upgrade a tower
		    if (clickedTower != null) {
			if (gameHandler.isTowerUpgradable(clickedTower)) {
			    gameHandler.upgradeTower(clickedTower);
			    towerDescription.setText(clickedTower.getDescription());
			}
		    }
		    break;
		// if the button is a tower button then the player is trying to buy a tower
		// so set the selected tower and change the tower description to that tower
		case TOWER_BUTTON:
		    // player is trying to press a tower on the menu
		    selectedTower = towerType;
		    //----- There is no problem with null. It is handled so the inspections are incorrent ------
		    String towerDesc = EntityFactory.getTower(towerType).getDescription();
		    towerDescription.setText(towerDesc);
		    break;
		// if the button type is a pause button then pause the game when the player presses it, or resume if it is paused.
		case PAUSE:
		    if (gameHandler.isGamePaused()) {
			jButton.setText("Pause game");
			gameHandler.startGame();
		    } else {
			jButton.setText("Start game");
			gameHandler.pauseGame();
		    }
		    break;
		// quit the game if the player presses on a quit button
		case QUIT:
		    System.exit(0);
		default:
		    // give and exception if somehow no valid button type was given.
		    // the exception does not need to be handeld since it is a IllegalArgumentException and there is something
		    // wrong with the game code
		    IllegalArgumentException illegalArgumentException =
			    new IllegalArgumentException("ButtonEvent did not have valid ButtonType");
		    logger.log(Level.SEVERE, "ButtonEvent in GameViewer did not recognize ButtonType.\nButtonType: " + buttonType + "\n",
			       illegalArgumentException);
		    throw illegalArgumentException;
	    }

	}
    }

    /**
     * MouseEvent contains the logic for handelding placing towers and displaying tower info on a specific point. MouseEvent uses
     * MouseAdapter which contains the logic for mouse listening
     */
    public class MouseEvent extends MouseAdapter
    {
	private JTextArea textArea;
	private ButtonGroup buttonGroup;

	public MouseEvent(JTextArea textArea, ButtonGroup buttonGroup) {
	    this.textArea = textArea;
	    this.buttonGroup = buttonGroup;
	}

	/**
	 * Either places a tower on the pressed location or displays info of the tower the player has pressed.
	 *
	 * @param e the MouseEvent object with info about click location
	 */
	@Override public void mouseClicked(final java.awt.event.MouseEvent e) {
	    Point clickedPoint = e.getPoint();
	    // we get the gameScale and translate from pixel coordinates to map coordinates
	    int mapPosX = clickedPoint.x / gameScale;
	    int mapPosY = clickedPoint.y / gameScale;
	    // since we do not want to place the tower "in between" tiles we use a normal point and not Point2D.Double
	    Point mapPoint = new Point(mapPosX, mapPosY);
	    // if no tower is selected the player is trying to get info from a tower placed on the map
	    if (selectedTower == TowerType.NONE) {
		displayTowerInfo(mapPoint);
		return;
	    }
	    // if a tower is selected then the player is trying to place a tower on the map
	    placeTower(mapPoint);

	}

	/**
	 * Displays information of the tower on a specific clicked point. Also sets the tower to be selected and deselctes the previous
	 * tower
	 *
	 * @param clickedPoint the point of the potential tower
	 */
	private void displayTowerInfo(Point clickedPoint) {
	    // if the clicked tower is not null, the player has clicked
	    // on another tower so deselect to current one
	    if (clickedTower != null) {
		gameHandler.deselectTower(clickedTower);
	    }
	    Tower newClickedTower = gameHandler.getTowerNearPosition(clickedPoint);
	    // if we did not get a null tower then there was a tower near the location
	    if (newClickedTower == null) {
		return;
	    }
	    textArea.setText(newClickedTower.getDescription());
	    clickedTower = newClickedTower;
	    gameHandler.selectTower(clickedTower);
	}

	/**
	 * places a tower of the selectedTower type field on the clicked point
	 *
	 * @param clickedPoint the clicked point to place the tower
	 */
	private void placeTower(Point clickedPoint) {
	    /*
	    First check if the selected tower can be placed.
	    If we cant place it give the player an explination.
	    If we can place it add the tower to the GameHandler, clear the button and remove the
	    tower currently selected
	     */

	    Tower newTower = EntityFactory.getTower(selectedTower);
	    //----- There is no problem with null. It is handled so the inspections are incorrent ------
	    newTower.setPosition(clickedPoint);
	    boolean canPlaceTower = gameHandler.canPlaceTower(newTower);

	    // check if the game allows placing the selected tower
	    if (!canPlaceTower) {
		// if not say tell the player he has done something incorrect
		JOptionPane.showMessageDialog(frame, "Not enough money or incorrect placement of tower");
	    } else {
		// otherwise place the tower for where the player has requested
		gameHandler.addTower(newTower);
		// lastely deselct the tower button and set the selcted tower to nothing
		buttonGroup.clearSelection();
		selectedTower = TowerType.NONE;
		//if the player wants to place another
		// he needs to press the tower button again to select tower
	    }
	}


    }

}
