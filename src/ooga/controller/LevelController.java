package ooga.controller;

import java.io.FileNotFoundException;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import ooga.engine.loop.LevelLoop;
import ooga.exceptions.ExceptionFeedback;
import ooga.model.data.Level;
import ooga.model.data.User;
import ooga.model.entity.Entity;
import ooga.model.entity.EntityList;
import ooga.utility.event.Event;
import ooga.view.GameScreen;

public class LevelController implements Communicable{

  private static final int INITIAL_WINDOW_WIDTH = 800;
  private static final int INITIAL_WINDOW_HEIGHT = 600;

  private User myUser;
  private LevelLoop myLevelLoop;

  private Group myVisualGroup = new Group();

  public LevelController(GameScreen gs, User user, int levelNumber) {
    myUser = user;
    Level level = null;
    try {
      level = LevelBuilder.buildLevel(levelNumber);
    } catch (FileNotFoundException e) {
      ExceptionFeedback.throwException(e, "File not found");
    }
    assert level != null;
    myLevelLoop = new LevelLoop(
        this, level.getEntities(), INITIAL_WINDOW_HEIGHT, INITIAL_WINDOW_WIDTH);
    EntityList visibleEntityList = myLevelLoop.getInitialVisibleEntityList();
    myVisualGroup.getChildren().addAll(visibleEntityList.getAsList());
    gs.setVisibleGroup(myVisualGroup);
  }

  @Override
  public void addEntity(Entity entity) {
    myVisualGroup.getChildren().add(entity);
  }

  @Override
  public void removeEntity(Entity entity) {
    myVisualGroup.getChildren().remove(entity);
  }

  @Override
  public void addAllEntities(EntityList entities) {
    myVisualGroup.getChildren().addAll(entities.getAsList());
  }

  @Override
  public void removeAllEntities(EntityList entities) {
    myVisualGroup.getChildren().removeAll(entities.getAsList());
  }

  public void beginLevel() {
    myLevelLoop.begin();
  }

  public void endLevel() {
    myLevelLoop.end();
  }

  public void pauseLevel() {
    myLevelLoop.pause();
  }

  public void resumeLevel() {
    myLevelLoop.resume();
  }


  public void handleUserInput(KeyEvent event){
    myLevelLoop.processInput(event);
  }

  //TODO: Add ability for LevelLoop to pass up events that could effect the user (ex: extra life
  // or addition of coins etc).
}
