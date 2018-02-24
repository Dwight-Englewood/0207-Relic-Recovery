# Bot Class

## Rationale
The Bot Class is the way in which we organize various ways of interfacing with the robot. There are many functions that are complicated - having to rewrite them for all of the teleops/autonomous modes would be very tedious, as well as error prone - if there is an error, we would need to make sure that the offending code is replaced in every instance it appears; forgetting to replace a single instance would be hard to debug later. In the spirit of true Object Oriented code, we created the Bot class. This allows us to place all complicated, not teleop/auton specific code in a single place, allowing for use in all of the Teleops and Autons

## Implementation

Our bot class is located in Utility/Bot.java, located under Appendix fuckls its face

Each method contained in the Bot Class is documented; however, the true "implementation" of the Bot Class is just the object. All the methods contained within are an application of the code.

TODO: MAKE SURE BOT CLASS IS DOCUMENTED

## Results

Every single Teleop and Auton we have utilizes the robot class in some form. Hardware items on the bot, such as sensors and motors, are instance fields of the robot class. Functions related to driving, sensor applications, and other things are contained in the robot class. This allows for the autons/teleops to exclusively deal with interpreting user inputs, or items dealing with counting iterations of a loop or timings. By segmenting the code in this way, it speerates the 2 aspects of programming and makes it much easier to debug programs based on what went wrong. 
