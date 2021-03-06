package ooga.model.entity;

import static org.junit.jupiter.api.Assertions.*;

import ooga.model.ability.attacktypes.Attack;
import ooga.utility.event.CollisionEvent;
import org.junit.jupiter.api.Test;

class EntityTest {

  private Entity buildEntity(String filename){
    EntityBuilder eb = new EntityBuilder();
    return eb.getEntity(filename);
  }

  private CollisionEvent buildCollisionEvent(String location, String attack){
    return new CollisionEvent(location, attack);
  }

  @Test
  void testUpdateAttack() {

  }

  @Test
  void testAddAbility() {
  }

  @Test
  void testHandleCollision() {
    Entity mario = buildEntity("Mario.png");
    CollisionEvent sideDamage = buildCollisionEvent("SIDE", "Damage");
    CollisionEvent topDamage = buildCollisionEvent("TOP", "Damage");
    CollisionEvent bottomDamage = buildCollisionEvent("BOTTOM", "Damage");

    CollisionEvent sideBounce = buildCollisionEvent("SIDE", "BounceX");
    CollisionEvent topBounce = buildCollisionEvent("TOP", "BounceY");
    CollisionEvent bottomBounce = buildCollisionEvent("BOTTOM", "BounceY");

    CollisionEvent sideSupport = buildCollisionEvent("SIDE", "SupportX");
    CollisionEvent topSupport = buildCollisionEvent("TOP", "SupportY");
    CollisionEvent bottomSupport = buildCollisionEvent("BOTTOM", "SupportY");

    CollisionEvent sideStun = buildCollisionEvent("SIDE", "Stun");
    CollisionEvent topStun = buildCollisionEvent("TOP", "Stun");
    CollisionEvent bottomStun = buildCollisionEvent("BOTTOM", "Stun");

    CollisionEvent sideHarmless = buildCollisionEvent("SIDE", "Harmless");
    CollisionEvent topHarmless = buildCollisionEvent("TOP", "Harmless");
    CollisionEvent bottomHarmless = buildCollisionEvent("BOTTOM", "Harmless");

    mario.handleCollision(sideDamage);
    assertEquals(true, mario.isDead());

    mario = buildEntity("Mario.png");
    mario.handleCollision(topDamage);
    assertEquals(true, mario.isDead());

    mario = buildEntity("Mario.png");
    mario.handleCollision(bottomDamage);
    assertEquals(true, mario.isDead());

    mario = buildEntity("Mario.png");
    mario.handleCollision(sideBounce);
    assertEquals(false, mario.isDead());

    mario = buildEntity("Mario.png");
    mario.handleCollision(topBounce);
    assertEquals(false, mario.isDead());

    mario = buildEntity("Mario.png");
    mario.handleCollision(bottomBounce);
    assertEquals(false, mario.isDead());

    mario = buildEntity("Mario.png");
    mario.handleCollision(sideSupport);
    assertEquals(false, mario.isDead());

    mario = buildEntity("Mario.png");
    mario.handleCollision(bottomSupport);
    assertEquals(false, mario.isDead());

    mario = buildEntity("Mario.png");
    mario.handleCollision(topSupport);
    assertEquals(false, mario.isDead());

  }

  @Test
  void testUpdateVisualization() {
  }

  @Test
  void testMoveRight() {

  }

  @Test
  void testMoveLeft() {
  }

  @Test
  void testJump() {
  }
}