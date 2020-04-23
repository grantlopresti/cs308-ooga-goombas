package ooga.controller.levels;

import java.util.HashMap;
import java.util.Map;

public class BasicLevelList {

  Map<Integer, BasicLevel> myLevels = new HashMap<>();

  public void addBasicLevel(BasicLevel basicLevel){
    myLevels.put(basicLevel.getLevelIndex(), basicLevel);
  }

  public int size() {
    return myLevels.keySet().size();
  }

  public BasicLevel getBasicLevel(int levelIndex){
    return myLevels.get(levelIndex);
  }

}