package se.liu.antos931jakos322.towerdefence.userinterface;


import javax.swing.*;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * StartGame starts the game. The class creates a start menu with the class StartMenu Also starts and configures the global logger
 */

public class StartGame
{
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void main(String[] args) {
	// attempt to start the logger.
	// While the logger is not started the game will not start.
	boolean loggerSuccess = false;
	while (!loggerSuccess) {
	    try {
		startLogger();
		loggerSuccess = true;
	    } catch (IOException ioException) {
		// the reason we dont log this exception is because there is no logger file yet.
		// The log file is what we are trying to create
		// this is not a catch and forget. If we cant load the file we ask the user to try again.
		// and do it until the user has fixed the issue
		ioException.printStackTrace();
		String loadErrorMessage = ioException + "\nerror creating logging.txt in source folder do you want to try again?";
		int answer = JOptionPane.showConfirmDialog(null, loadErrorMessage);
		if (answer == JOptionPane.NO_OPTION) {
		    System.exit(1);
		}
	    }
	}
	// logger was created. Start the game
	StartMenu startMenu = new StartMenu();
	startMenu.createStartMenu();
    }

    /**
     * Starts the global logger
     *
     * @throws IOException if the log did not create succsessfully
     */
    private static void startLogger() throws IOException {
	FileHandler fileTxt = new FileHandler("logging.txt");
	SimpleFormatter formatterTxt = new SimpleFormatter();
	fileTxt.setFormatter(formatterTxt);
	LOGGER.setLevel(Level.FINE);
	LOGGER.addHandler(fileTxt);

    }

}
