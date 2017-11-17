---
geometry: landscape, left=1cm,right=1cm,top=1cm,bottom=1cm
header-includes:
    - \usepackage{setspace}
    - \setstretch{0.5}
    - \usepackage{listings}
    - \lstset{ breakatwhitespace=false, breaklines=false, extendedchars=true}
    - \setlength{\parskip}{0em}
---

\begin{spacing}{0}

┏ --  7d8b50a  ::  2017-08-04, by Robert  ::  Initial commit 

┣ --  05944d2  ::  2017-08-04, by Rob A  ::  Updated FTC App to 3.2 Beta 

┣ --  d922861  ::  2017-08-04, by Rob A  ::  fixing submodule issue. Removed toTC.sh 

┣ --  0bf0f57  ::  2017-08-04, by Rob A  ::  Fixed submodule issue 

┣ --  a10c785  ::  2017-08-05, by Rob A  ::  lol notes 

┣ --  a426297  ::  2017-08-06, by Rob A  ::  Added enum for bot actions and the bot class to be filled in. 

┣ --  c16594f  ::  2017-08-06, by Rob A  ::  testing for slack integration 

┣ --  ab6379d  ::  2017-08-07, by Rob A  ::  removed slack test file 

┣ --  bdfb595  ::  2017-08-07, by Rob A  ::  slight improvements to bot class 

┣ --  e77f0d9  ::  2017-08-07, by Wen P  ::  Added some basic enums; we should add more as we figure out the bot's hardware 

┣ --  51efa9d  ::  2017-08-07, by Rob A  ::  minor updates to bot actions for future us to fill in (actions enum). 

┣ --  255bb0e  ::  2017-08-07, by Rob A  ::  updates to bot class 

┣ --  775db69  ::  2017-08-18, by Rob A  ::  update to bot 

┣ --  17319a8  ::  2017-08-29, by Rob A  ::  updated gitignore 

┣ --  12ad864  ::  2017-09-09, by Wen P  ::  Updated sdk? 

┣ --  a3c0292  ::  2017-09-09, by Wen P  ::  Updated sdk 

┣ --  c2e111a  ::  2017-09-09, by Wen P  ::  Finish updating 

┣ --  94f2f8b  ::  2017-09-09, by Wen P  ::  Created standalone package for enums 

┣ --  c2165f3  ::  2017-09-10, by Rob A  ::  added telebop class and 2 auton classes -- unfilled 

┣ --  13b7c9e  ::  2017-09-11, by Rob A  ::  Added PDFs 

┣ --  d1bcac3  ::  2017-09-14, by Rob A  ::  Learning rev stuff and making robot do things for activities fair 

┣ --  4c17073  ::  2017-09-15, by Rob A  ::  Finished tester bot 

┣ --  75f169e  ::  2017-09-16, by Rob A  ::  moved test bot stuff into a seperate folder and made some minor adjustments to the bot class. 

┣ --  2f7d080  ::  2017-09-18, by Rob A  ::  Some changes to telebop -- started wrok on field centric drive 

┣ --  b8f69d2  ::  2017-09-20, by Rob A  ::  Split field centric drive work into a seperate class 

┣ --  f707fe1  ::  2017-09-22, by Rob A  ::  lol field centric drive in 2017 

┣ --  0d28392  ::  2017-09-22, by Wen P  ::  bzzt bzzt 

┣ --  35327ec  ::  2017-09-22, by Wen P  ::  Made better readme for teamcode 

┣ --  be64251  ::  2017-09-22, by Wen P  ::  added motor related stuff to Bot class, made init function have arguments 

┣ --  110b66e  ::  2017-09-22, by Wen P  ::  hyperlul fixed that interesting drive orientation 

┣━┓

┃ ┣ --  7f16776  ::  2017-09-22, by Wen P  ::  Worked on enums. Migrated movement enum to ADT to allow for Angle and easier drive function maker 

┃ ┣ --  97ebcb4  ::  2017-09-22, by Wen P  ::  removed impo from Bot.java 

┣ ┃ --  9ca949b  ::  2017-09-22, by Rob A  ::  Worked on Field Centric Driving, NOTE: problem with setting negative powers on wheels 

┣━┛  

┣ --  fb7d18d  ::  2017-09-22, by Rob A  ::  Merge branch 'master' of https://github.com/0207-Critical-Mass/0207-Relic-Recovery 

┣ --  a91f55b  ::  2017-09-22, by Rob A  ::  fixed the field centric drive (I think) 

┣ --  ebfe628  ::  2017-09-22, by Rob A  ::  commented the field centric drive code -- requires testing still. 

┣ --  9dc5483  ::  2017-09-22, by Rob A  ::  Moved around old testbot opmodes 

┣ --  686665b  ::  2017-09-22, by Rob A  ::  Minor changes to the field centric drive opmode 

┣ --  6a16c6b  ::  2017-09-22, by Robert  ::  Update README.md 

┣ --  bfedca0  ::  2017-09-23, by Rob A  ::  minor change to bot class and bug fixes in Field Centric 

┣ --  0fd860a  ::  2017-09-25, by Wen P  ::  made it lowercase folder 

┣ --  7c3b8c8  ::  2017-09-25, by Wen P  ::  Added some comments 

┣━┓

┃ ┣ --  033b1f4  ::  2017-09-26, by Rob A  ::  Added OpMode for calibration of the gyro. 

┃ ┣ --  8dac403  ::  2017-09-26, by Rob A  ::  Minor changes to Field Centric so that we can effectively test the new CalibrateGyro opmode 

┣ ┃ --  990d96c  ::  2017-09-29, by Wen P  ::  Worked on bot clas, added imu related code, added simple example drive function 

┣━┛  

┣ --  c0f39f7  ::  2017-09-29, by Wen P  ::  Merge branch 'master' of https://github.com/0207-Critical-Mass/0207-Relic-Recovery 

┣ --  322df22  ::  2017-09-29, by Wen P  ::  created the teleopautobalance teleop 

┣ --  9d6d133  ::  2017-09-29, by Wen P  ::  removed extra created by comment 

┣ --  a8bc827  ::  2017-09-29, by Wen P  ::  added a comment, but finishing yp for the day so logging it even though its small 

┣ --  7a8e6f5  ::  2017-10-08, by Rob A  ::  field centric thing 

┣ --  72abd86  ::  2017-10-08, by Rob A  ::  stuff for workshop 

┣ --  d97f8be  ::  2017-10-08, by Wen P  ::  Added basic tank drive teleop for 10/8/17 

┣ --  bdae268  ::  2017-10-08, by Rob A  ::  Rob stuff, pushing so wen can fix stuff 

┣ --  531dfd7  ::  2017-10-20, by Rob A  ::  updates from work session 10/20/17 

┣ --  ac631b8  ::  2017-10-20, by Rob A  ::  updates from work session 10/20/17 

┣ --  feb2bf9  ::  2017-10-23, by Rob A  ::  Bot class updates 

┣ --  95786bf  ::  2017-10-23, by Rob A  ::  bot changes + vuforia test 

┣ --  d3a3a12  ::  2017-10-23, by Rob A  ::  updates to autons and bot class 

┣ --  34ea701  ::  2017-10-23, by Rob A  ::  Added subclass autonbot and teleopbot 

┣ --  d4c1fcc  ::  2017-10-23, by Rob A  ::  removed constructor from bot class 

┣ --  55404b0  ::  2017-10-23, by Rob A  ::  removed the autonbot and telebopbot classes to make room for wen's new system 

┣ --  101441f  ::  2017-10-27, by Rob A  ::  updated to 3.0--> gradles etx change 

┣ --  e0ac1bd  ::  2017-10-27, by Rob A  ::  PUSH 

┣ --  a7e7e08  ::  2017-10-30, by Rob A  ::  edits to bot class and telebop 

┣ --  cab994f  ::  2017-10-30, by Rob A  ::  removed field centric drive opmode 

┣ --  8897bf7  ::  2017-10-31, by Rob A  ::  Added more functions and motors to bot class, cleaned autons up using improved class. 

┣ --  8e9a547  ::  2017-11-01, by Wen P  ::  updated sdk to 3.5 

┣ --  501e3f7  ::  2017-11-01, by Wen P  ::  added arm servo stuff to bot, hopefully it works 

┣ --  5f64187  ::  2017-11-01, by Wen P  ::  idk possibly added arm stuff 

┣━┓

┃ ┣ --  b5daab5  ::  2017-11-02, by Wen P  ::  added comments for burto 

┃┏┫

┣┃┃ --  c7f5bf3  ::  2017-11-02, by Rob A  ::  telebop update 

┣┛┃  

┣ ┃ --  3e66963  ::  2017-11-02, by Rob A  ::  Merge branch 'master' of https://github.com/Dwight-Englewood-Robotics/0207-Relic-Recovery 

┃ ┣ --  24d0854  ::  2017-11-03, by Wen P  ::  added more comment: 

┃ ┣ --  844d263  ::  2017-11-03, by Wen P  ::  added OneButtonMan Teleop for testing motor and configuration 

┃ ┣ --  93b6c0a  ::  2017-11-03, by Wen P  ::  chagned some stuff in bot and teleop to make other stuff work 

┃ ┣ --  52c710f  ::  2017-11-03, by Wen P  ::  added start of system for the ASM based robot/subsystem design: 

┃ ┣━┓

┃ ┣ ┃ --  fc1f16e  ::  2017-11-03, by Wen P  ::  fixed a type making the armBottomExtendyServo not work properly 

┣ ┃ ┃ --  8de32d8  ::  2017-11-03, by Rob A  ::  rob bot stuff 

┣━☰━┛  

┣ ┃ --  3a80428  ::  2017-11-03, by Rob A  ::  Merge branch 'master' of https://github.com/Dwight-Englewood-Robotics/0207-Relic-Recovery 

┣┓┃

┃┗┫  

┃ ┣ --  1c4c1a9  ::  2017-11-03, by Wen P  ::  Merge branch 'master' of https://github.com/Dwight-Englewood-Robotics/0207-Relic-Recovery 

┣ ┃ --  788023e  ::  2017-11-03, by Rob A  ::  Random crap 

┣━┛  

┣ --  3aee82b  ::  2017-11-03, by Rob A  ::  Merge branch 'master' of https://github.com/Dwight-Englewood-Robotics/0207-Relic-Recovery 

┣━┓

┃ ┣ --  749bdff  ::  2017-11-03, by Wen P  ::  changed onebuttonman so it doesnt have the bad releaseTheKraken 

┃┏┫

┣┃┃ --  607df86  ::  2017-11-03, by Rob A  ::  changed to range clip 

┣┛┃  

┣ ┃ --  3a01764  ::  2017-11-03, by Rob A  ::  Merge branch 'master' of https://github.com/Dwight-Englewood-Robotics/0207-Relic-Recovery 

┣┓┃

┃┃┣ --  79f92dd  ::  2017-11-03, by Wen P  ::  releaseTheGiantQuid 

┃┗┫  

┃ ┣ --  8c73387  ::  2017-11-03, by Wen P  ::  Merge branch 'master' of https://github.com/Dwight-Englewood-Robotics/0207-Relic-Recovery 

┣ ┃ --  ce687d2  ::  2017-11-03, by Rob A  ::  idk 

┣━┛  

┣ --  ff04520  ::  2017-11-03, by Rob A  ::  asd 

┣ --  8922528  ::  2017-11-03, by Wen P  ::  chjanged arm extendy values to make them work 

┣ --  4347415  ::  2017-11-03, by Rob A  ::  k 

┣ --  fc14508  ::  2017-11-03, by Rob A  ::  Minor fix to gyro turns 

┣ --  95a13fd  ::  2017-11-05, by Rob A  ::  pre match pushes 

┣ --  12b75b4  ::  2017-11-05, by Wen P  ::  lots of stuff from today from rob/wen 

┣ --  a06109d  ::  2017-11-06, by Wen P  ::  renamed ServoTest teleops made in competition, made them more readable 

┣ --  65994f9  ::  2017-11-06, by Wen P  ::  added intellij templates for easy creation of Teleop/Auton 

┣ --  e6dbe58  ::  2017-11-08, by Wen P  ::  Removed Useless comment and added a newline to readme 

┣ --  32c3e3c  ::  2017-11-09, by Rob A  ::  Finished test bench code and added new servos to bot class for flipper and release servos 

┣ --  6c68edf  ::  2017-11-09, by Rob A  ::  Commented out end game servos from bot class and teleop. 

┣ --  4e0d78f  ::  2017-11-09, by Rob A  ::  Cleaned up different opmodes into respective packages (Auton, Teleop, Testing, etc.) for cleanliness 

┣ --  69b994a  ::  2017-11-09, by Rob A  ::  cleaned up bot class : removed unnecessary comments, added functions for simple automation 

┣ --  977d79d  ::  2017-11-09, by Rob A  ::  Moved hardware mapping to constructor, lowers init phase lag 

┣ --  905902e  ::  2017-11-09, by Rob A  ::  Changed inputs to bot constructor, moved telemetry to bot class 

┣ --  23819de  ::  2017-11-10, by Rob A  ::  Added an invert drive mode to the bot class' tank drive function. 

┣ --  b6265f9  ::  2017-11-10, by Rob A  ::  Minor change to ordering of declarations in bot class to make more sense. 

┣ --  7ab27f4  ::  2017-11-13, by Rob A  ::  renamed wen's bot class to avoid confusion, added machine vision stuff by Enderbots 

┣ --  9b77560  ::  2017-11-13, by Rob A  ::  added enderbot' exception handler for trying to mitigate random crashes 

┣ --  fa11a29  ::  2017-11-13, by Wen P  ::  Added script which pretty prints our git history 

┣ --  92c27bb  ::  2017-11-15, by Rob A  ::  created a vision package for computer vision, minor servo additions to bot class 

┣ --  2b30f85  ::  2017-11-15, by Wen P  ::  Added another shell script to readme, for saving 

┣ --  f453951  ::  2017-11-15, by Wen P  ::  Changed the servos in Bot class to public 

┣ --  b49b32d  ::  2017-11-15, by Wen P  ::  Created a ServoTesterTeleop 

┣ --  dcd536a  ::  2017-11-15, by Wen P  ::  Moved around stuff related to auto generation of notebook 

┣ --  619ea21  ::  2017-11-15, by Wen P  ::  Bot class config edits 

┣ --  1fe41d2  ::  2017-11-15, by Rob A  ::  Maintanence stuff 

┣ --  fe2b338  ::  2017-11-15, by Wen P  ::  Lots of work on auto log stuff 

┣ --  7a7ec28  ::  2017-11-16, by Wen P  ::  Added texts folder, for various text files we need to be able to share 

┗ --  0fad056  ::  2017-11-16, by Wen P  ::  more work on the autolog, finalizing scripts 

\end{spacing}
