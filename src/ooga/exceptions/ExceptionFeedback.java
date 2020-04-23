package ooga.exceptions;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ExceptionFeedback {

  public static final int MESSAGE_LINE_LENGTH = 200;

  /**
   * Throws an exception dialog to the user with a given type and message.
   *
   * @param e       Exception to throw
   * @param message message to include
   */
  public static void throwBreakingException(Exception e, String message) {
    JOptionPane.showConfirmDialog(new JFrame(),
        message,
        e.getClass().getSimpleName(), JOptionPane.DEFAULT_OPTION);
    System.exit(0);
  }

  public static void throwHandledException(Exception e, String message) {
    ButtonType close = new ButtonType("CLOSE", ButtonData.OK_DONE);
    showAlert(e.getClass().getSimpleName(), message, close);
  }

  private static void showAlert(String header, String message, ButtonType buttonType) {
    Alert alert = new Alert(AlertType.ERROR, "", buttonType);
    alert.setHeaderText(header);

    StringBuilder sb = new StringBuilder(message);
    for (int i = 0; i < message.length(); i += MESSAGE_LINE_LENGTH) {
      sb.insert(i, "\n");
    }

    Label t = new Label(sb.toString());
    alert.getDialogPane().setContent(t);

    alert.show();
  }
}
