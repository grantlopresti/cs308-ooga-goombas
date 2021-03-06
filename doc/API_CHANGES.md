# API_CHANGES.md

*Note, the API described in plan should not be changed because it affects how other people code
 might work. If it must be changed, the change and its reasons must be clearly described in a
  separate file, doc/API_CHANGES.md, that is updated regularly throughout the project.*
  
##

### `package view`

`Displayable` interface removed, replaced with `Screen` abstract class. The public method `Node getDisplay()` was replaced with the extension of `Pane` by `Screen`, which allows callers in `controller` to directly add screens to the JavaFX Scene graph, instead of calling a method to do so.

`LevelBuilderScreen` removed entirely from the scope of the project, since authorship proved to be too much work for too short a time.

`Screen` public constructors now accept `ScreenController` as an argument for all Screens, allowing control of screens, data transfer, and screen switching.

#### `GameScreen`:
`GameScreen()` constructor changed to accept `BasicLevel` level as parameter, in accordance with backend change to represent levels as `BasicLevels`.

`setLevelController()` method added to allow communication between `LevelController` and screen, since unforeseen instantiation obstacles prohibited passing `LevelController` in the constructor.

`setVisibleGroup()` method added (potentially in original API) to allow `LevelController` to set the visible game group.

####`package dynamicUI`
All 'dynamic' UI elements, meaning all UI elements that rely on dynamic or variable values from the game, are packaged and relocated to dynamicUI.

`setPaneWidth` and `setPaneHeight` added to Selector to enable dynamic layout setting using FXLR, which was added in Sprint 2.

`setPreferredWidth` and `setPreferredHeight` added to LevelProgressBar to enable dynamic layout setting using FXLR, which was added in Sprint 2.

####`package fxlr`
Package added in Sprint 2, with support for data-driven layout creation.

`FXLRParser` added to allow `view` package Screens to instantiate parsers and set their layout from corresponding `.fxlr` files.

`FXLRParser.loadFXLRLayout` added to initiate parsing of layout for Screens.

### `package exceptions`

`throwException` was split into `throwBreakingException` and `throwHandledException` to differentiate exceptions that require program execution to halt vs. exceptions that are handled and inconsequential to program execution.

### `Communicable`
`addAllEntities` was added to add an EntityList of entities to the game screen

`removeAllEntities` was added to remove an EntityList of entities from the game screen

### `Handleable`

`handleKeyPressed` was added to aid in passing key pressed events from the front-end to the back-end where they would be handled.

`handleKeyReleased` was added to aid in passing key releaded events from the front-end to the back-end where they would be handled.


### `GameLevel`

`handleWin` was added to allow both the GameScreen and the LevelLoop to communicate with the LevelController and handle the effects of a button driven and a level event driven gamelevel command.

`adjustPoints` was added to allow the LevelLoop to communicate with the LevelController and handle the effects of a level event driven points adjustment.

`adjustLives` was added to allow the LevelLoop to communicate with the LevelController and handle the effects of a level event driven lives adjustment.

### `Collideable`

`isDead` is used for LevelLoop/Entity Communication, determining the state of entities at each tick.

`getData` uses a string information tag and returns a string value, was added to ease entity communication

`otherCollectMe` allows Level Loop to determine what should happen when one entity is able to collect another.

`size` allows the resizing of an entity after collision with another one.

`getAttack` allows the Collision Manager to get an entity's attack type and pass it to the other entity in question

`otherResetAfterCollect` allows properties of the main entity such as points and lives to be effected by the collision.