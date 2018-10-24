package Project03;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Controller {

    private final ExecutorService executorService;


    public Controller() {
        this.executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            }
        });
    }

    @FXML
    private VBox board;

    @FXML
    private Label statusLabel;

    @FXML
    private Label enterALetterLabel;

    @FXML
    private TextField textField;

    @FXML
    private void neww() { }

    @FXML
    private void quit() {
        board.getScene().getWindow().hide();
    }



}
