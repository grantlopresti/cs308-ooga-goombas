package ooga.view.screen;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import ooga.controller.ScreenController;
import ooga.controller.levels.BasicLevel;

/**
 * Internal splash screen to display upon level failed.
 */
class LevelFailedSplash extends SplashScreen {

  LevelFailedSplash(ScreenController controller, Node parent) {
    super(controller, parent);

    this.getStyleClass().add("loading-screen");

    loadLayout();
  }

}