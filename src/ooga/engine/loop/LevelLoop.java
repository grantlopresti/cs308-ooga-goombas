package ooga.engine.loop;

import java.awt.event.KeyListener;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import ooga.controller.Communicable;
import ooga.engine.manager.CameraManager;
import ooga.engine.manager.CollisionManager;
import ooga.engine.manager.EntityManager;
import ooga.engine.manager.InputManager;
import ooga.model.data.Level;
import ooga.model.data.User;
import ooga.model.entity.Entity;
import ooga.model.entity.EntityList;
import java.awt.event.KeyAdapter;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class LevelLoop implements Loopable {

  private Communicable myLevelController;
  private EntityManager myEntityManager;
  private CameraManager myCameraManager;
  private InputManager myInputManager;
  private CollisionManager myCollisionManager;
  //private EntityList myEntities;
  private EntityList myVisibleEntities;
  private static final int FRAMES_PER_SECOND = 600;
  private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
  private Timeline myTimeline;
  private Object KeyEvent;

  public LevelLoop(Communicable levelController, EntityList myEntities, double screenHeight, double screenWidth) {
    myLevelController = levelController;
    myEntityManager = new EntityManager(myEntities);
    myCameraManager = new CameraManager(myEntities.getMainEntity(), screenHeight, screenWidth);
    myInputManager = new InputManager(myEntities.getMainEntity());
    myCollisionManager = new CollisionManager();
    EntityList entitiesOnScreen = myCameraManager.initializeActiveEntities(myEntities);
    myVisibleEntities =  entitiesOnScreen;
    myEntityManager.addAllEntities(entitiesOnScreen);
    myEntityManager.initializeEntityLists();
    createTimeline();
  }

  private void createTimeline() {
    KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> {
      loop();
    });
    myTimeline = new Timeline();
    myTimeline.setCycleCount(Timeline.INDEFINITE);
    myTimeline.getKeyFrames().add(frame);
  }

  private void loop() {
    reinitializeEntities();
    manageCollisions();
    //System.out.println(myEntityManager.getAddedEntities());
    updateEntities();
    // tell the entities to update gravity and stuff
    updateCamera();
    sendEntities();
  }

  public void reinitializeEntities(){
    myEntityManager.initializeEntityLists();
    myCameraManager.initializeActivationStorage();

  }

  public void processInput(KeyEvent e) {
    myInputManager.handleKeyInput(e);
  }

  private void manageCollisions() {
    myCollisionManager.manageCollisions(myCameraManager.getOnScreenEntities());
    myEntityManager.addAllEntities(myCollisionManager.getEntitiesReceived());
  }

  private void updateEntities() {
    for(Entity entity: myCameraManager.getOnScreenEntities()){
      entity.updateVisualization();
      //is this the correct method?
    }
  }

  private void updateCamera() {
    myCameraManager.updateCamera(myEntityManager.getEntities());
    if(myCameraManager.getActivatedEntities().size()!=0) {
      myEntityManager.addAllEntities(myCameraManager.getActivatedEntities());
    }
    if(myCameraManager.getDeactivatedEntities().size()!=0) {
      myEntityManager.removeAllEntities(myCameraManager.getDeactivatedEntities());
    }
  }

  private void sendEntities(){
    if(myEntityManager.getAddedEntities().size()!=0) {
      myLevelController.addAllEntities(myEntityManager.getAddedEntities());
    }
    if(myEntityManager.getRemovedEntities().size()!=0){
      myLevelController.removeAllEntities(myEntityManager.getRemovedEntities());
    }
  }


  /*private void sendEntitiesToController(EntityList activatedEntities, EntityList deactivedEntities) {
    myLevelController.addAllEntities(activatedEntities);
    myLevelController.removeAllEntities(deactivedEntities);
  }*/

  public void begin() {
    myTimeline.play();
  }

  public void end() {
    myTimeline.stop();
  }

  public void pause() {
    myTimeline.pause();
  }

  public void resume() {
    myTimeline.play();
  }

  public void exit() {
    myTimeline.stop();
  }

  public EntityList getInitialVisibleEntityList() { return myVisibleEntities; }

}
