import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

    public void initialize() throws IOException {
		System.out.println("in initialize");
		drawHangman();
		addTextBoxListener();
		setUpStatusLabelBindings();
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
					break;
				default:
					break;
			}
		}
	}
		
	@FXML 
	private void newHangman() {
		game.reset();
	}

	@FXML
	private void quit() {
		board.getScene().getWindow().hide();
	}

}