package ooga.model.entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ooga.model.ability.Ability;
import ooga.model.ability.CollectiblePackage;
import ooga.model.ability.Health;
import ooga.model.ability.Movement;
import ooga.model.behavior.Collidible;
import ooga.model.physics.Physics;
import ooga.utility.event.CollisionEvent;

public class Entity extends ImageView implements Collidible, Manageable, Renderable {

  private static final String HARMLESS = "Harmless";
  private static final String DEFAULT_PACKAGE_CONTENT = "nothing 0";
  private static final String COLLISIONS_HANDLING_PATH = "entities/collisions/";
  private static final String ADD = "add";
  private static final double INITIAL_SCORE = 0;
  private static final double DEFAULT_SCALE = 1;

  private Health health;
  private Movement movement;
  private CollectiblePackage myPackage;
  private String side, top, bottom;
  private List<Ability> myAbilities;
  private String debuggingName;
  private double score, scale;
  private boolean dead, haveMovement, levelEnded, success;

  /**
   * Create default health and attacks, which can be overwritten later
   * using the addAbility method
   */
  public Entity(Image image, String name){
    super(image);
    debuggingName = name;
    myAbilities = new ArrayList<Ability>();
    health = new Health();
    side = HARMLESS;
    top = HARMLESS;
    bottom = HARMLESS;
    haveMovement = false;
    myPackage = new CollectiblePackage(DEFAULT_PACKAGE_CONTENT);
    score = INITIAL_SCORE;
    scale = DEFAULT_SCALE;
  }

  /**
   * Override any attack types as specified by the new attack type and location
   * @param location where the attack goes
   * @param attackType new type to replace old one at location
   */
  public void updateAttack(String location, String attackType) {
    try {
      Method method = Entity.class.getDeclaredMethod(ADD+location, String.class);
      method.invoke(Entity.this, attackType);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Add abilities to the entity. This is a general method that allows all
   * ability objects to be added to the entity
   * @param abilityType what ability to add, directs it to the correct specific method
   * @param ability the ability object to be handed to this entity
   */
  public void addAbility(String abilityType, Ability ability){
    try {
      Method method = Entity.class.getDeclaredMethod(ADD+abilityType, Ability.class);
      method.invoke(Entity.this, ability);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  //used for reflection DO NOT DELETE
  private void addHealth(Ability h){
    health = (Health) h;
  }

  //used for reflection DO NOT DELETE
  public void addCollectiblePackage(Ability p){
    myPackage = (CollectiblePackage) p;
  }

  //used for reflection DO NOT DELETE
  private void addMovement(Ability m){
    movement = (Movement) m;
    haveMovement = true;
  }

  //used for reflection DO NOT DELETE
  private void addSideAttack(String a){
    side = a;
  }

  //used for reflection DO NOT DELETE
  private void addTopAttack(String a){
    top = a;
  }

  //used for reflection DO NOT DELETE
  private void addBottomAttack(String a){
    bottom = a;
  }

  @Override
  public String[] getTags() {
    return new String[0];
  }

  /**
   * returns if the entity has health greater than 0;
   * aka if it is dead or not
   * @return boolean tracking health status
   */
  public boolean isDead(){
    return dead;
  }

  //todo delete when finished
  public String debug(){
    if(debuggingName.equals("Mario.png")){
     // System.out.println("Side: "+side.toString()+" Bottom: "+bottom.toString()+" top: "+top.toString());
    }
    return debuggingName;
  }

  /**
   * Get the attack of the specific side
   * @param location
   * @return call method to get the attack of the specific location
   */
  public String getAttack(String location){
    try {
      Method method = Entity.class.getDeclaredMethod("get"+location+"Attack");
      return (String) method.invoke(Entity.this);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  //used for reflection DO NOT DELETE
  private String getSideAttack(){
    return side;
  }

  //used for reflection DO NOT DELETE
  private String getTopAttack(){
    return top;
  }

  //used for reflection DO NOT DELETE
  private String getBottomAttack(){
    return bottom;
  }

  @Override
  /**
   * handle collisions based on a given collision event
   * find the attack the entity has at the location of the collision,
   * search in the file of the attack of the OTHER entity for the methods
   * that should be executed based on this.attackType, then execute those methods
   */
  public Entity handleCollision(CollisionEvent ce) {
    String location = ce.getCollisionLocation();
    String otherAttack = ce.getAttackType();
    String myAttack = this.getAttack(location);

    try {
      ResourceBundle myAttackSpecificResponseBundle = ResourceBundle.getBundle(COLLISIONS_HANDLING_PATH+otherAttack.toString());
      String[] methodsToCall = myAttackSpecificResponseBundle.getString(myAttack).split(" ");
      for(String s : methodsToCall) {
        Method method = Entity.class.getDeclaredMethod(s);
        method.invoke(Entity.this);
      }
    } catch (MissingResourceException e) {
      System.out.println("Couldn't find key in bundle I'm:"+ debuggingName+"; we're at: "+location);
      throw new RuntimeException(e);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    }
    return this;
  }

  //used for reflection DO NOT DELETE
  private void damage(){
    health.hit();
    dead = health.isDead();
  }

  //used for reflection DO NOT DELETE
  /**
   * chooses which bounce method to use based on which speed is greater; x or y
   */
  private void bounce(){
    if(haveMovement && (Math.abs(movement.getYVelocity()) >= Math.abs(movement.getXVelocity()))){
      bounceY();
    } else if (haveMovement && Math.abs(movement.getYVelocity()) < Math.abs(movement.getXVelocity())){
      bounceX();
    }
  }

  //used for reflection DO NOT DELETE
  /**
   * note: the effects of a collectible entity will only
   * be shown within that entity in order to hide other objects
   */
  private void collectMe(){
    //todo create a bonus ability that can change score, height, etc. have that happen here as the entity is collected
    String methodToCall = myPackage.toString();
    double value = myPackage.getPackageValue();
    if(!dead && !levelEnded){
      try {
        Method method = Entity.class.getDeclaredMethod(methodToCall, Double.class);
        method.invoke(Entity.this, value);
      } catch (NoSuchMethodException e) {
        throw new RuntimeException(e);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      } catch (InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    }
  }

  //used for reflection DO NOT DELETE
  private void points(Double value){
    score += value;
    //System.out.println("score: " + score);
  }

  //used for reflection DO NOT DELETE
  private void health(Double value){
    health.addLives((int) Math.floor(value));
  }

  //used for reflection DO NOT DELETE
  private void size(Double value){
    scale = value;
  }

  //used for reflection DO NOT DELETE
  private void levelEnd(Double value){
    //System.out.println("we did it");
    levelEnded = true;
    success = (value!=0);
  }

  //used for reflection DO NOT DELETE
  private void collect(){
    //do nothing
  }

  //used for reflection DO NOT DELETE
  private void supportY(){
    if(haveMovement) {
      //setY(getY()-Physics.TINY_DISTANCE);
      movement.standY();
    }
  }

  //used for reflection DO NOT DELETE
  private void supportX(){
    if(haveMovement) {
      //setY(getY()-Physics.TINY_DISTANCE);
      movement.standX();
    }
  }

  //used for reflection DO NOT DELETE
  private void bounceY(){
    if(haveMovement) {
      movement.bounceY();
    }
  }

  //used for reflection DO NOT DELETE
  private void bounceX(){
    if(haveMovement) {
      movement.bounceX();
    }
  }

  //used for reflection DO NOT DELETE
  private void nothing(){
    //do nothing
  }

  @Override
  /**
   * called every cycle, move the entity, and check for entity death
   */
  public void updateVisualization() {
    if (haveMovement) {
      movement.update(this);
    }
    //todo this isn't changeing the scale of the player bc it's changing the scale of the mushroom
    this.setScaleX(scale);
    this.setScaleY(scale);
    dead = health.isDead();
  }

  //used for reflection DO NOT DELETE
  /**
   * return is a level is over for the specific entity
   * @return levelEnded
   */
  public boolean endedLevel(){
    return levelEnded;
  }

  //used for reflection DO NOT DELETE
  //fixme because this isn't in the player entity, this method will always be false for players
  // and always true for collected level end entities. ask cayla if you can delete this
  /**
   * True if a level was ended by collecting a level end entity.
   * @return success
   */
  public boolean isSuccess(){
    return success;
  }

  //used for reflection DO NOT DELETE
  //fixme where is this being used? would it be ok to delete/replace with a clear score method?
  /**
   * set the score of an entity to an incoming value
   * @param newScore new score to set the entity to
   */
  public void setScore(double newScore){
    score = newScore;
  }

  //used for reflection DO NOT DELETE
  /**
   * return the score of a collected entity
   * @return
   */
  public double getScore(){
    return score;
  }

  //used for reflection DO NOT DELETE
  /**
   * Move the entity to the right if it can do so
   */
  public void moveRight(){
    setScaleX(1);
    movement.right();
  }

  //used for reflection DO NOT DELETE
  /**
   * Move the entity to the left if it can do so
   */
  public void moveLeft(){
    setScaleX(-1);
    movement.left();
  }

  //used for reflection DO NOT DELETE
  /**
   * Move the entity up if it can do so
   */
  public void jump(){
    // Old Line (changed because TINY_DISTANCE shouldn't be static)
    //setY(getY()-Physics.TINY_DISTANCE);
    setY(getY()-5);
    movement.jump();
  }
}
