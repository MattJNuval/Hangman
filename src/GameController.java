import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class GameController {

	private final ExecutorService executorService;
	private final Game game;	
	
	public GameController(Game game) {
		this.game = game;
		executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r);
				thread.setDaemon(true);
				return thread;
			}
		});
	}

	@FXML
	private VBox board ;
	@FXML
	private Label statusLabel ;
	@FXML
	private Label enterALetterLabel ;
	@FXML
	private TextField textField ;
	@FXML
	private Label answerBox;
	@FXML
	private Label answerField;

    public void initialize() throws IOException {
		System.out.println("in initialize");
		drawHangman();
		addTextBoxListener();
		setUpStatusLabelBindings();
		setUpAnswerLabelBindings();
		setUpAnswerField();
	}

	private void addTextBoxListener() {
		textField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
				if(newValue.length() > 0) {
					System.out.print(newValue);
					game.makeMove(newValue);
					updateHangman();
					textField.clear();
				}
			}
		});
	}

	private void setUpStatusLabelBindings() {

		System.out.println("in setUpStatusLabelBindings");
		statusLabel.textProperty().bind(Bindings.format("%s", game.gameStatusProperty()));
		enterALetterLabel.textProperty().bind(Bindings.format("%s", "Enter a letter:"));
		/*	Bindings.when(
					game.currentPlayerProperty().isNotNull()
			).then(
				Bindings.format("To play: %s", game.currentPlayerProperty())
			).otherwise(
				""
			)
		);
		*/
	}

	private void setUpAnswerLabelBindings() {
		answerBox.textProperty().bind(Bindings.format("%s", "Answer Box:"));

	}

	private void setUpAnswerField() {
		answerField.textProperty().bind(Bindings.format("%s", game.getTmpAnswer()));

	}

	private void drawHangman() {

    	Group man = new Group();
		Line rope = new Line(25.0f, 0.0f, 25.0f,25.0f);
		Circle head = new Circle(10);
		Line body = new Line(25.0f, 35.0f, 25.0f, 75.0f);
		Line left_arm = new Line(25.0f, 50.0f, 10.0f, 35.0f);
		Line right_arm = new Line(25.0f, 50.0f, 40.0f, 35.0f);
		Line left_leg = new Line(25.0f, 75.0f, 15.0f, 90.0f);
		Line right_leg = new Line(25.0f, 75.0f, 35.0f, 90.0f);

		head.setTranslateX(25.0f);
		head.setTranslateY(25.0f);
		man.getChildren().addAll(rope, head, body, left_arm, right_arm, left_leg, right_leg);
		board.getChildren().add(man);
		initHangman();
	}

	private void initHangman(){
    	Group man = (Group)board.getChildren().get(0);
    	for(int i = 0; i < 6; i++){
    		man.getChildren().get(i+1).setVisible(false);
		}
		board.getChildren().clear();
    	board.getChildren().add(man);
	}

	private void updateHangman(){
		Group man = (Group)board.getChildren().get(0);

		if(game.getGameStatus() == Game.GameStatus.BAD_GUESS){
			switch (game.getMoves()){
				case 1: // Head
					man.getChildren().get(1).setVisible(true);
					board.getChildren().clear();
					board.getChildren().add(man);
					break;
				case 2: // Body
					man.getChildren().get(2).setVisible(true);
					board.getChildren().clear();
					board.getChildren().add(man);
					break;
				case 3: // Left Arm
					man.getChildren().get(3).setVisible(true);
					board.getChildren().clear();
					board.getChildren().add(man);
					break;
				case 4: // Right Arm
					man.getChildren().get(4).setVisible(true);
					board.getChildren().clear();
					board.getChildren().add(man);
					break;
				case 5: // Left Leg
					man.getChildren().get(5).setVisible(true);
					board.getChildren().clear();
					board.getChildren().add(man);
					break;
				case 6: // Right Leg
					man.getChildren().get(6).setVisible(true);
					board.getChildren().clear();
					board.getChildren().add(man);
					promptNewGame();
					break;
				default:
					break;
			}
		}
	}
		
	@FXML 
	private void newHangman() {
		game.reset();
		initHangman();
	}

	@FXML
	private void quit() {
		board.getScene().getWindow().hide();
	}

	@FXML
	private void addWords() throws IOException {

    	String newWord;

		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Add a Word to Game");
		dialog.setHeaderText("Input a valid word");
		dialog.setContentText("Word");


		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){

			boolean isValid = true;
			char wordArr[] = result.get().toCharArray();

			for(int i = 0; i < wordArr.length; i++) {
				if(!(Character.isAlphabetic(wordArr[i]))) {
					isValid = false;
				}
			}

if(isValid) {
	newWord = result.get();
	game.addNewWord(newWord);
	System.out.println(newWord);

} else {
	Alert alert = new Alert(Alert.AlertType.WARNING);
	alert.setTitle("Invalid Word");
	alert.setHeaderText("Word inputed was invalid");
	alert.setContentText("Make sure you word is a valid english word and contains no non-alphabetic characters");

	alert.showAndWait();
}




		}

	}

	@FXML
	private void setDefault() {
    	game.defaultWordBank = true;
    	game.easy = false;
    	game.medium = false;
    	game.hard = false;
	}
	@FXML
	private void setEasy() {
	   game.easy = true;

		game.defaultWordBank = false;
		game.medium = false;
		game.hard = false;
	}
	@FXML
	private void setMedium() {
		game.medium = true;

		game.easy = false;
		game.defaultWordBank = false;
		game.hard = false;
	}
	@FXML
	private void setHard() {
		game.hard = true;

		game.easy = false;
		game.medium = false;
		game.defaultWordBank = false;
	}



	private void promptNewGame(){
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Game Over");
		alert.setHeaderText("Answer: " + game.getAnswer());
		alert.setContentText("You are out of guesses. Select new game or quit.");

		ButtonType newGame = new ButtonType("New Game");
		ButtonType quit = new ButtonType("Quit");

		alert.getButtonTypes().setAll(newGame, quit);

		Optional<ButtonType> result = alert.showAndWait();

		if(result.get() == newGame){
			newHangman();
		}else if(result.get() == quit){
			quit();
		}

	}

}