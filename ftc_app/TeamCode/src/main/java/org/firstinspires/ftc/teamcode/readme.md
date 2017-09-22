# Teleops
  * Telebop
    * Basic teleop
  * FieldCentricDrive
    * Teleop which implements Field Centric Drive, so based on coordinates of field
# Autons
  * RedAuton
    * The auton for use on red team
  * BlueAuton
    * The auton for use on blue team
# Enums
  * BotActions
    * Common actions for use as function arguments, to keep code clean
# Other Stuff
  * Bot
    * Main class which contains functions which directly deals with the bot. This allows for repeated usage in many different autons and teleops, without having to remake a bot everytime.