package ooga.engine.manager.CameraManager.DirectionControllers.VerticalDirectionControllers;

import ooga.model.entity.Entity;
import ooga.model.entity.EntityList;

/**
 * Subclass of VerticalDirectionController
 * specifies movement of entity so that screen is only able to move up and down with the main entity's y position
 * @author Cayla Schuval
 */
public class UpAndDownDirectionController extends VerticalDirectionController {

  private static final int OFFSET = 100;

  /**
   * @param entities EntityList containing all of the entities in the game whose positions update as the main entity moves
   * @param myScreenHeight screen height of the game used to determine if entities move on or off screen
   * @param myScreenWidth screen width of the game used to determine if entities move on or off screen
   * checks the X position of the main entity and updates all entity positions respectively
   */
  public void updateCameraPosition(EntityList entities, double myScreenHeight, double myScreenWidth) {
    Entity mainEntity = entities.getMainEntity();
    double yCenter = myScreenHeight/2- mainEntity.getBoundsInLocal().getHeight()/2;
    if (mainEntity.getY() > yCenter + OFFSET){
      setToCenter(entities, OFFSET);
    }
    else if (mainEntity.getY() < yCenter - OFFSET) {
      setToCenter(entities, - OFFSET);
    }
    checkIfMarioTouchesSidesOfScreen();
  }

  /**
   * @return int value associated with the offset from the center which should cause the screen to shift
   */
  public int getOffset() {
    return OFFSET;
  }
}