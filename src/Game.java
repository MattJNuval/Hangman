import javafx.beans.Observable;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {

	private String answer;
	private String tmpAnswer;
	private String[] letterAndPosArray;
	private String[] words;
	private int moves;
	private int index;
	private final ReadOnlyObjectWrapper<GameStatus> gameStatus;
	private ObjectProperty<Boolean> gameState = new ReadOnlyObjectWrapper<Boolean>();


	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//variable inputedLetter - used for getting the recently inputed letter
	// variable gameStarted - used to indicate whether the Game was started - implemented in ComputeValues
	private String inputedLetter;
	private boolean gameStarted = false;
	

	public enum GameStatus {
		GAME_OVER {
			@Override
			public String toString() {
				return "Game over!";
			}
			
		},
		BAD_GUESS {
			@Override
			public String toString() { return "Bad guess..."; }
		},
		GOOD_GUESS {
			@Override
			public String toString() {
				return "Good guess!";
			}
		},
		WON {
			@Override
			public String toString() {
				return "You won!";
			}
		},
		OPEN {
			@Override
			public String toString() {
				return "Game on, let's go!";
			}
		}
	}

	public Game() {
		gameStatus = new ReadOnlyObjectWrapper<GameStatus>(this, "gameStatus", GameStatus.OPEN);
		gameStatus.addListener(new ChangeListener<GameStatus>() {
			@Override
			public void changed(ObservableValue<? extends GameStatus> observable,
								GameStatus oldValue, GameStatus newValue) {
				if (gameStatus.get() != GameStatus.OPEN) {
					log("in Game: in changed");
					//currentPlayer.set(null);
				}
			}

		});

		try {
			setRandomWord();
		} catch (Exception e) {
			log("file was not found");
		}
		prepTmpAnswer();
		prepLetterAndPosArray();
		moves = 0;

		gameState.setValue(false); // initial state
		createGameStatusBinding();
	}

	private void createGameStatusBinding() {
		List<Observable> allObservableThings = new ArrayList<>();
		ObjectBinding<GameStatus> gameStatusBinding = new ObjectBinding<GameStatus>() {
			{
				super.bind(gameState);
			}
			@Override
			public GameStatus computeValue() {
				log("in computeValue");
				System.out.println("tempAnswer is " + tmpAnswer);
				System.out.println("Answer is " + answer);
				GameStatus check = checkForWinner(index);
				if(check != null ) {
					return check;
				}

				if(tmpAnswer.trim().length() == 0 && !gameStarted){
					log("new game");
					gameStarted = true;
					return GameStatus.OPEN;
				}
				else if (index != -1){
					log("good guess");
					return GameStatus.GOOD_GUESS;
				}
				else {
					moves++;
					log("bad guess");
					return GameStatus.BAD_GUESS;
				}
			}
		};
		gameStatus.bind(gameStatusBinding);
	}

	public ReadOnlyObjectProperty<GameStatus> gameStatusProperty() {
		return gameStatus.getReadOnlyProperty();
	}
	public GameStatus getGameStatus() {
		return gameStatus.get();
	}

	private void setRandomWord() throws FileNotFoundException {

		// get number of words in textfile
		// allows new names to be added to the text file without breaking anything

		File file = new File(".idea/words.txt");
		Scanner input = new Scanner(file);
		int numWords = 0;
		while (input.hasNextLine() != false) {
			numWords++;
			input.nextLine();
		}
		input.close();
		// generate a random number between 0 and numWords
		Random random = new Random();
		int lineNumber = random.nextInt(numWords);

		// retrieve a random word from the file and set answer to it
		Scanner inputFind = new Scanner(file);
		int i = 0;
		while (inputFind.hasNextLine() != false) {
			String word = inputFind.nextLine();
			if (i == lineNumber) {
				answer = word;
				break;
			} else {
				i++;
			}
		}
		input.close();

		//answer = "apple";//words[idx].trim(); // remove new line character

	}

	private void prepTmpAnswer() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < answer.length(); i++) {
			sb.append(" ");
		}
		tmpAnswer = sb.toString();
	}

	private String getTemp(){
		return tmpAnswer;
	}

	private void prepLetterAndPosArray() {
		letterAndPosArray = new String[answer.length()];
		for(int i = 0; i < answer.length(); i++) {
			letterAndPosArray[i] = answer.substring(i,i+1);
		}
	}

	private int getValidIndex(String input) {
		int index = -1;
		for(int i = 0; i < letterAndPosArray.length; i++) {
			if(letterAndPosArray[i].equals(input)) {
				index = i;
				letterAndPosArray[i] = "";
				break;
			}
		}
		return index;
	}

	private int update(String input) {
		int index = getValidIndex(input);
		if(index != -1) {
			System.out.println("index is not -1");
			StringBuilder sb = new StringBuilder(tmpAnswer);
			sb.setCharAt(index, input.charAt(0));
			tmpAnswer = sb.toString();
		}
		System.out.println("index is " + index);
		return index;
	}

	public void makeMove(String letter) {
		log("\nin makeMove: " + letter);
		inputedLetter = letter;
		index = update(letter);
		// this will toggle the state of the game
		gameState.setValue(!gameState.getValue());
	}

	public int getMoves(){
	    return moves;
    }

	// allows you to get tmpAnswer for displaying what user has gotten so far
	public String getTmpAnswer() {
		return tmpAnswer;
	}

	// allows you to get inputedLetter for displaying
	public String getInputedLetter(){
		return inputedLetter;
	}

	// where game will reset
	// set moves to 0
	// reset tmpAnswer
	// reset the letterAndPosArray
	// reset the random word
	// set gameStarted to false
	// reset gameStatusBinding to new game
	public void reset() {

		// important that the random word is set first

		try {
			setRandomWord();
		} catch (Exception e) {
			log("couldnt find file");
		}

		moves = 0;
		prepTmpAnswer();
		prepLetterAndPosArray();

		gameStarted = false;
		createGameStatusBinding();


	}

	private int numOfTries() {
		return 6; // Based on the number of body parts of the stick figure
	}

	public static void log(String s) {
		System.out.println(s);
	}

	private GameStatus checkForWinner(int status) {
		log("in checkForWinner");
		if(tmpAnswer.equals(answer)) {
			log("won");
			return GameStatus.WON;
		}
		else if(moves == numOfTries()) {
			log("game over");
			log(answer);
			return GameStatus.GAME_OVER;
		}
		else {
			return null;
		}
	}
}
