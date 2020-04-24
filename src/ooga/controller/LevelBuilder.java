package ooga.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.MalformedInputException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;
import ooga.exceptions.ExceptionFeedback;
import ooga.controller.levels.BasicLevel;
import ooga.controller.levels.CompleteLevel;
import ooga.model.entity.Entity;
import ooga.model.entity.EntityBuilder;
import ooga.model.entity.EntityList;

public final class LevelBuilder {

  private static final String HEADER_TAG = "#HEADER";
  private static final String ENTITIES_TAG = "#ENTITIES";
  private static final String LEVEL_TAG = "#LEVEL";
  private static final String TAG_DELIMITER = "#";
  private static final String KEY_VAL_SEPARATOR = ":";
  private static final String LEVEL_OBJ_SEPARATOR = " ";
  private static final String MAIN_ENTITY_SYMBOL = "X";
  private static final String EMPTY_SPACE_SYMBOL = ".";
  private static final int KEY_INDEX = 0;
  private static final int VALUE_INDEX = 1;
  private static final double PADDING = 0.01;
  private static final String LEVEL_HEIGHT_SPECIFIER = "levelHeight";
  private static final String LEVEL_WIDTH_SPECIFIER = "levelWidth";

  public static BasicLevel buildBasicLevel(int levelNumber, File levelFile) throws FileNotFoundException {
    Map<String,String> headerInfo = getMapFromFile(levelFile, HEADER_TAG);

    return new BasicLevel(levelNumber, levelFile, headerInfo);
  }

  public static CompleteLevel buildCompleteLevel(BasicLevel basicLevel, double gameWindowHeight,
      double gameWindowWidth) throws FileNotFoundException {

    File levelFile = basicLevel.getLevelFile();
    Map<String, String> headerInfo = basicLevel.getHeaderInfo();
    int levelHeight = Integer.parseInt(headerInfo.get(LEVEL_HEIGHT_SPECIFIER));
    int levelWidth = Integer.parseInt(headerInfo.get(LEVEL_WIDTH_SPECIFIER));

    Map<String,String> entityInfo = getMapFromFile(levelFile, ENTITIES_TAG);

    EntityList levelEntities = buildEntities(levelFile, entityInfo, levelHeight, levelWidth,
        basicLevel.getGameType(), gameWindowHeight, gameWindowWidth);

    return new CompleteLevel(basicLevel, levelEntities);
  }

  private static Map<String, String> getMapFromFile(File levelFile, String sectionTag)
      throws FileNotFoundException {

    Map<String, String> sectionMap = new HashMap<>();

    Scanner sc = new Scanner(levelFile);
    moveToSection(sectionTag, sc);

    addDataToMap(sectionMap, sc);

    return sectionMap;
  }

  private static void moveToSection(String sectionTag, Scanner sc) {
    String nextLine = sc.nextLine();
    while (!nextLine.contains(sectionTag)){
      nextLine = sc.nextLine();
    }
  }

  private static void addDataToMap(Map<String, String> sectionMap, Scanner sc) {
    String nextLine = sc.nextLine();
    while (!nextLine.contains(TAG_DELIMITER)){
      String[] sectionLine = nextLine.split(KEY_VAL_SEPARATOR);
      if (sectionLine.length == 2){
        sectionMap.put(sectionLine[KEY_INDEX], sectionLine[VALUE_INDEX]);
      } else {
        ExceptionFeedback.throwBreakingException(
            new MalformedInputException(0), "Invalid Level File. Invalid info in section");
      }
      nextLine = sc.nextLine();
    }
  }

  private static EntityList buildEntities(File levelFile, Map<String, String> entityInfo,
      int levelHeight, int levelWidth, String gameType, double gameWindowHeight,
      double gameWindowWidth) throws FileNotFoundException {
    EntityList myEntities = new EntityList();

    Scanner sc = new Scanner(levelFile);
    moveToSection(LEVEL_TAG, sc);

    double scaleFactor = getScaleFactor(levelHeight, levelWidth, gameWindowHeight, gameWindowWidth);

    for (int j = 0; j < levelHeight; j++){
      String[] levelLine = sc.nextLine().split(LEVEL_OBJ_SEPARATOR);
      for (int i = 0; i < levelWidth; i++){
        String entityCode = levelLine[i];
        if (!entityCode.equals(EMPTY_SPACE_SYMBOL)){
          String entityName = entityInfo.get(entityCode);
          Entity myEntity = EntityBuilder.getEntity(entityName, gameType);
          setEntitySize(myEntity, scaleFactor);
          setEntityCoordinates(j, i, myEntity, scaleFactor);
          addNewEntityToEntitiesList(myEntities, entityCode, myEntity);
        }
      }
    }
    return myEntities;
  }

  private static double getScaleFactor(int levelHeight, int levelWidth, double gameWindowHeight,
      double gameWindowWidth) {
    return Math.max(gameWindowHeight/levelHeight, gameWindowWidth/levelWidth);
  }

  private static void setEntitySize(Entity myEntity, double scaleFactor) {
    double scalingFactor = myEntity.getBoundsInLocal().getWidth()/scaleFactor;
    myEntity.setFitWidth(myEntity.getBoundsInLocal().getWidth()/scalingFactor - PADDING);
    myEntity.setFitHeight(myEntity.getBoundsInLocal().getHeight()/scalingFactor - PADDING);
  }

  private static void addNewEntityToEntitiesList(EntityList myEntities, String symbol, Entity myEntity) {
    if (symbol.equals(MAIN_ENTITY_SYMBOL)) {
      myEntities.setMainEntity(myEntity);
    }
    myEntities.addEntity(myEntity);
  }

  private static void setEntityCoordinates(int j, int i, Entity myEntity,
      double scaleFactor) {
    myEntity.setX(getRelativeX(i, scaleFactor));
    myEntity.setY(getRelativeY(myEntity.getBoundsInLocal().getHeight(), j, scaleFactor));
  }

  private static double getRelativeY(double entityHeight, int j, double scaleFactor) {
    return (j*scaleFactor) - entityHeight + scaleFactor + PADDING/2;
  }

  private static double getRelativeX(int i, double scaleFactor) {
    return i*scaleFactor + PADDING/2;
  }

}
