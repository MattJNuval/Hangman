import javafx.beans.Observable;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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


	// !!For Robert!! the progress part
	// setsize in init
	// in update --- set the progress
	// in reset --- reset the size

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//variable inputedLetter - used for getting the recently inputed letter
	// variable gameStarted - used to indicate whether the Game was started - implemented in ComputeValues
	private String inputedLetter = "";
	private String wrongLetter =" ";
	private boolean gameStarted = false;

	// array that changes based on progress
	private String progressArr[];

	// String used in the label to display progress
	private String progressDisp;

	boolean defaultWordBank = true;
	boolean easy = false;
	boolean medium = false;
	boolean hard = false;


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
		setProgressArr();
		setProgressDisp();

		log("for DEV rm later answer is " + answer);

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
				System.out.println("Answer is: " + answer);
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

					updateProgessArr(index, inputedLetter);

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

		File file;

		String wordDefault = ".idea/words.txt";
		String wordEasy = ".idea/wordEasy.txt";
		String wordMedium = ".idea/wordMedium.txt";
		String wordHard = ".idea/wordHard.txt";


		if(defaultWordBank) {
			file = new File(wordDefault);

		} else if(easy){
			file = new File(wordEasy);

		} else if(medium) {
			file = new File(wordMedium);

		} else if(hard) {
			file = new File(wordHard);

		} else {
			//shouldnt get here but covering my bases
			file = new File(wordDefault);
		}




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

	}

	// in this we add to the main bank and then add the word to its appropriate difficulty bank
	public void addNewWord(String newWord) throws IOException {

        // strings for the paths
		String wordDefault = ".idea/words.txt";
		String wordEasy = ".idea/wordEasy.txt";
		String wordMedium = ".idea/wordMedium.txt";
		String wordHard = ".idea/wordHard.txt";


		String totalStr = "";
		String readStr = "";

		File file;


		if(newWord.length() <= 3) {

			file = new File(wordEasy);

		} else if(newWord.length() > 3 && newWord.length() <= 5) {
			file = new File(wordMedium);

		} else if(newWord.length() > 5) {
			file = new File(wordHard);

		} else {
			// should take care of any odd behavior for now
			file = new File(wordDefault);

		}



		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);


		while((readStr = br.readLine()) != null) {
			totalStr = totalStr + readStr + "\n";
		}

		br.close();

		totalStr = totalStr + newWord;


		FileWriter fw = new FileWriter(file);
		fw.write(totalStr);
		fw.close();



		// add the word to the master list
		totalStr = "";
		readStr = "";

		File fileWords = new File(wordDefault);
		FileReader frW = new FileReader(fileWords);
		BufferedReader brW = new BufferedReader(frW);


		while((readStr = brW.readLine()) != null) {
			totalStr = totalStr + readStr + "\n";
		}

		brW.close();

		totalStr = totalStr + newWord;


		FileWriter fwW = new FileWriter(fileWords);
		fwW.write(totalStr);
		fwW.close();



	}

	private void prepTmpAnswer() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < answer.length(); i++) {
			sb.append(" ");
		}
		tmpAnswer = sb.toString();
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

		if(index != -1) {
			updateProgessArr(index, inputedLetter);
		}

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
	public String getwrongLetter(){

		if(this.getGameStatus() == Game.GameStatus.BAD_GUESS)
		{
			wrongLetter = wrongLetter +inputedLetter;
		}
		return wrongLetter;
	}


	// allows controller to get the labels display
	public String getProgressDisp() {
		return progressDisp;
	}

	//must call after the progressArr is instantiated or nullptr will occur
	public void setProgressArr() {

		int progressArrSize = answer.length() * 2;
		progressArr = new String[progressArrSize];
		Arrays.fill(progressArr, "");

		for(int i = 0; i < answer.length(); i++) {

			int progressIndex = 2 * i;
			progressArr[progressIndex] = "_";
			progressDisp= progressDisp +"_";
		}

		log("progressArr set " + Arrays.toString(progressArr));
	}

	public void updateProgessArr(int newIndex, String inputedLetter) {

		newIndex = newIndex * 2;
		progressArr[newIndex] = inputedLetter;

		log("progress array is updated to " + Arrays.toString(progressArr));
		setProgressDisp();

		log("progressDisp is " + progressDisp);

	}

	// updates the display string
	// doing this because a simple .toString on progressArr would involve an object wrapping that
	// results in progressDisp looking like the following { a, p, p, l, e }
	public void setProgressDisp() {

		progressDisp = "";

		for(int j = 0; j < progressArr.length; j++) {
			progressDisp = progressDisp + progressArr[j] + " ";

		}

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
		setProgressArr();


		
		log("DEV new game answer is" + answer);


		progressDisp = "";
		setProgressDisp();
		wrongLetter = " ";

		gameStarted = false;
		createGameStatusBinding();

	}

	public void continueGame() {
		try {
			setRandomWord();
		} catch (Exception e) {
			log("couldnt find file");
		}

		moves = getMoves();
		prepTmpAnswer();;
		prepLetterAndPosArray();
        setProgressArr();
        progressDisp = "";
        setProgressDisp();
        wrongLetter = " ";
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

	public String getAnswer(){
		return answer;
	}
}
