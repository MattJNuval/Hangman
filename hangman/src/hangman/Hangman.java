package hangman;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Hangman extends Application {

	@Override
	public void start(final Stage primaryStage) throws IOException {
		final Game game = new Game();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Hangman.fxml"));
		loader.setController(new GameController(game));
		Parent root = loader.load();
		Scene scene = new Scene(root, 500, 800);
		scene.getStylesheets().add(getClass().getResource("Hangman.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
