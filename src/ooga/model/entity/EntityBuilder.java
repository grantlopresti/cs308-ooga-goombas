package ooga.model.entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import ooga.exceptions.ExceptionFeedback;
import ooga.model.ability.Ability;

public class EntityBuilder {

  private static final String ABILITY_PACKAGE = "ooga.model.ability.";
  public static final String IMAGE_KEY = "Image";
  private static final String GAME_DATA_FOLDER = "gamedata/";
  private static final String USER_INPUT_INFORMATION = "/entities/entities";
  private static ResourceBundle myEntityResources;


  //TODO take out throwing runtime exceptions, throw actual ones
  /**
   * reflection to create the Ability object. Assumes that the stats string is an integer
   * @param abilityType specific Ability object to create
   * @param stats String containing the specific type of the sub Ability
   * @return created Ability
   */
  private static Ability makeAbility(String abilityType, String stats){
    try{
      Class abilityClass = Class.forName(ABILITY_PACKAGE + abilityType);
       Constructor abilityClassConstructor = abilityClass.getConstructor(String.class);
      return (Ability) abilityClassConstructor.newInstance(stats);
    } catch (ClassNotFoundException e){
      System.out.println("ClassNotFoundException");
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      System.out.println("IllegalAccessException");
      throw new RuntimeException(e);
    } catch (NoSuchMethodException e) {
      System.out.println("NoSuchMethodException");
      throw new RuntimeException(e);
    } catch (InstantiationException e) {
      System.out.println("InstantiationException");
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      System.out.println("InvocationTargetException");
      throw new RuntimeException(e);
    }
  }

  //TODO check that file path is valid
  /**
   * Getter for created entity
   * Must be passed in the proper strings following the format of an
   * ability building file, where keys are valid ability classes
   * and values are valid ability types
   * @param statsFilename file name for the entity stat resource file
   * @return created entity
   */


  public static Entity getEntity(String statsFilename, String gameType) {
    String UserInputResources = GAME_DATA_FOLDER+gameType+USER_INPUT_INFORMATION;
    myEntityResources = ResourceBundle.getBundle(UserInputResources);
    String[] entityInformation = getEntityInfo(statsFilename);
    String entityType = entityInformation[0];
    String imageFile = entityInformation[1];
    try {
      String gameSpecificFilePath = "gamedata/" + gameType + "/entities/";
      ResourceBundle resources = ResourceBundle.getBundle(gameSpecificFilePath + "behavior/" + entityType);
      Image image = new Image(gameSpecificFilePath + "images/" + imageFile);
      Entity entity = new Entity(image, imageFile, gameType);

      for (String s : Collections.list(resources.getKeys())) {
        //todo remove this if?
        if (!s.equals("Image")) {
          //reflection!
          if (s.contains("Attack")) {
            entity.updateAttack(s.split("Attack")[0], resources.getString(s));
          } else {
            Ability a = makeAbility(s, resources.getString(s));
            entity.addAbility(s, a);
          }
        }
      }
      return entity;
    } catch (MissingResourceException e){
      ExceptionFeedback.throwHandledException(new RuntimeException(), "You didn't edit the level file correctly. Can't find the properties file for a type! Either add the file or remove the type from the level");
      throw new RuntimeException();
    }
  }

  private static String[] getEntityInfo(String entityCode){
    System.out.println(entityCode);
    return myEntityResources.getString(entityCode).split(",");
  }
}
