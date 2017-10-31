# Teleops
  * Telebop
    * Basic teleop
        * Implements a field centric driving algorithm
# Autons
  * RedAuton
    * The auton for use on red team
  * BlueAuton
    * The auton for use on blue team
# Enums
  * BotActions
    * Common actions for use as function arguments, to keep code clean
  * MovementEnum
    * Actions to be used for function arguments in drive function within the bot class
# Other Stuff
  * Bot
    * Main class which contains functions which directly deals with the bot. This allows for repeated usage in many different autons and teleops, without having to remake a bot everytime.