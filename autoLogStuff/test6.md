---
geometry: landscape, left=1cm,right=1cm,top=1cm,bottom=1cm
header-includes:
    - \usepackage{setspace}
    - \setstretch{0.5}
    - \usepackage{listings}
    - \lstset{ breakatwhitespace=false, breaklines=false, extendedchars=true}
---

\begin{lstlisting}


* 7a7ec28 - Thu, 16 Nov 2017 12:07:17 -0500 (7 hours ago) (HEAD -> master, origin/master, origin/HEAD)
|           Added texts folder, for various text files we need to be able to share - weznon
* fe2b338 - Wed, 15 Nov 2017 17:45:35 -0500 (25 hours ago)
|           Lots of work on auto log stuff - weznon
* 1fe41d2 - Wed, 15 Nov 2017 17:33:44 -0500 (25 hours ago)
|           Maintanence stuff - Burtorustum
* 619ea21 - Wed, 15 Nov 2017 17:09:27 -0500 (25 hours ago)
|           Bot class config edits - weznon
* dcd536a - Wed, 15 Nov 2017 16:28:21 -0500 (26 hours ago)
|           Moved around stuff related to auto generation of notebook - weznon
* b49b32d - Wed, 15 Nov 2017 16:21:40 -0500 (26 hours ago)
|           Created a ServoTesterTeleop - weznon
* f453951 - Wed, 15 Nov 2017 16:20:53 -0500 (26 hours ago)
|           Changed the servos in Bot class to public - weznon
* 2b30f85 - Wed, 15 Nov 2017 16:20:03 -0500 (26 hours ago)
|           Added another shell script to readme, for saving - weznon
* 92c27bb - Wed, 15 Nov 2017 08:01:49 -0500 (35 hours ago)
|           Created a Vision package for all computer vision stuff. Minor comments in Telebop and additions to bot class for new servos for lift and new motor for lift. - Burtorustum
* fa11a29 - Mon, 13 Nov 2017 20:57:56 -0500 (3 days ago)
|           Added script which pretty prints our git history - weznon
* 9b77560 - Mon, 13 Nov 2017 10:55:13 -0500 (3 days ago)
|           added enderbot's default exception handler for if the bot randomly crashes while on the field. Will auto restart the app. - Burtorustum
* 7ab27f4 - Mon, 13 Nov 2017 09:42:17 -0500 (3 days ago)
|           Renamed wen's wip bot class for confusion reasons. Also added the Enderbot's closable vuforia localizer so that we can use both vuforia and OpenCV in our code. - Burtorustum
* b6265f9 - Fri, 10 Nov 2017 09:54:19 -0500 (6 days ago)
|           Minor change to ordering of declarations in bot class to make more sense. - Burtorustum
* 23819de - Fri, 10 Nov 2017 09:51:48 -0500 (6 days ago)
|           Added an invert drive mode to the bot class' tank drive function. - Burtorustum
* 905902e - Thu, 9 Nov 2017 22:19:37 -0500 (7 days ago)
|           Changed the inputs to the constructor of the bot class to take over the role of setting hardwaremap references to motor, servo, sensor etc objects. Also moved telemetry to the bot class. Each of these now have references within the bot class to eleviate the need for passing them in the future for other methods. This has the additional effect of making our init function faster (Also removed the second unnecessary init). - Burtorustum
* 977d79d - Thu, 9 Nov 2017 22:09:38 -0500 (7 days ago)
|           Moved hardware mapping to the bot class constructor to make loading times faster when pressing init on the drivers station. - Burtorustum
* 69b994a - Thu, 9 Nov 2017 22:05:30 -0500 (7 days ago)
|           cleaned the bot class by removing unnecessary comments and adding functions for things that could be automated. - Burtorustum
* 4e0d78f - Thu, 9 Nov 2017 22:00:48 -0500 (7 days ago)
|           Cleaned up different opmodes into respective packages (Auton, Teleop, Testing, etc.) for cleanliness - Burtorustum
* 6c68edf - Thu, 9 Nov 2017 17:43:25 -0500 (7 days ago)
|           Commented out end game servos from bot class and teleop. - Burtorustum
* 32c3e3c - Thu, 9 Nov 2017 17:33:07 -0500 (7 days ago)
|           Finished the test bench code. Began adding new servos to the bot class for the flipper and two release servos (now tested). TODO: find equilibrium distance for two release servos, find correct points for release and flip actions. - Burtorustum
| * 42454aa - Mon, 13 Nov 2017 12:58:15 -0500 (3 days ago) (origin/beforeRebase)
|/            added log test files - weznon
* e6dbe58 - Wed, 8 Nov 2017 13:32:07 -0500 (8 days ago)
|           Removed Useless comment and added a newline to readme - weznon
* 65994f9 - Mon, 6 Nov 2017 17:11:24 -0500 (10 days ago)
|           added intellij templates - must be manually added to intellij, allows for super easy new teleop/auton creation - weznon
* a06109d - Mon, 6 Nov 2017 17:06:47 -0500 (10 days ago)
|           renamed ServoTest teleops made in competition, made them more readable - weznon
* 12b75b4 - Sun, 5 Nov 2017 12:46:46 -0500 (11 days ago)
|           lots of shit from today from rob/wen - weznon
* 95a13fd - Sun, 5 Nov 2017 06:50:16 -0500 (12 days ago)
|           pre match pushes - Burtorustum
* fc14508 - Fri, 3 Nov 2017 22:30:48 -0400 (13 days ago)
|           Minor fix to gyro turns - Burtorustum
* 4347415 - Fri, 3 Nov 2017 20:15:11 -0400 (13 days ago)
|           k - Burtorustum
* 8922528 - Fri, 3 Nov 2017 13:59:22 -0400 (13 days ago)
|           chjanged arm extendy values to make them work - weznon
*   ff04520 - Fri, 3 Nov 2017 13:56:39 -0400 (13 days ago)
|\            asd - Burtorustum
| *   8c73387 - Fri, 3 Nov 2017 13:50:50 -0400 (13 days ago)
| |\            Merge branch 'master' of https://github.com/Dwight-Englewood-Robotics/0207-Relic-Recovery - weznon
| * | 79f92dd - Fri, 3 Nov 2017 13:50:48 -0400 (13 days ago)
| | |           releaseTheGiantQuid - weznon
* | | ce687d2 - Fri, 3 Nov 2017 13:54:07 -0400 (13 days ago)
| |/            idk - Burtorustum
|/|   
* |   3a01764 - Fri, 3 Nov 2017 12:56:12 -0400 (13 days ago)
|\ \            Merge branch 'master' of https://github.com/Dwight-Englewood-Robotics/0207-Relic-Recovery - Burtorustum
| |/  
| * 749bdff - Fri, 3 Nov 2017 12:52:42 -0400 (13 days ago)
| |           changed onebuttonman so it doesnt have the bad releaseTheKraken - weznon
* | 607df86 - Fri, 3 Nov 2017 12:56:08 -0400 (13 days ago)
|/            changed to range clip - Burtorustum
*   3aee82b - Fri, 3 Nov 2017 12:53:47 -0400 (13 days ago)
|\            Merge branch 'master' of https://github.com/Dwight-Englewood-Robotics/0207-Relic-Recovery - Burtorustum
| *   1c4c1a9 - Fri, 3 Nov 2017 12:50:24 -0400 (13 days ago)
| |\            Merge branch 'master' of https://github.com/Dwight-Englewood-Robotics/0207-Relic-Recovery - weznon
| * | fc1f16e - Fri, 3 Nov 2017 11:53:36 -0400 (13 days ago)
| | |           fixed a type making the armBottomExtendyServo not work properly - weznon
* | | 788023e - Fri, 3 Nov 2017 12:53:45 -0400 (13 days ago)
| |/            Random crap - Burtorustum
|/|   
* |   3a80428 - Fri, 3 Nov 2017 12:43:03 -0400 (13 days ago)
|\ \            Merge branch 'master' of https://github.com/Dwight-Englewood-Robotics/0207-Relic-Recovery - Burtorustum
| |/  
| * 52c710f - Fri, 3 Nov 2017 11:39:23 -0400 (13 days ago)
| |           added start of system for the ASM based robot/subsystem design: - weznon
| * 93b6c0a - Fri, 3 Nov 2017 11:30:03 -0400 (13 days ago)
| |           chagned some stuff in bot and teleop to make other stuff work - weznon
| * 844d263 - Fri, 3 Nov 2017 11:29:27 -0400 (13 days ago)
| |           added OneButtonMan Teleop for testing motor and configuration - weznon
| * 24d0854 - Fri, 3 Nov 2017 10:33:38 -0400 (13 days ago)
| |           added more comment: - weznon
* | 8de32d8 - Fri, 3 Nov 2017 12:43:00 -0400 (13 days ago)
| |           rob bot stuff - Burtorustum
* |   3e66963 - Thu, 2 Nov 2017 14:52:23 -0400 (2 weeks ago)
|\ \            Merge branch 'master' of https://github.com/Dwight-Englewood-Robotics/0207-Relic-Recovery - Burtorustum
| |/  
| * b5daab5 - Thu, 2 Nov 2017 08:52:49 -0400 (2 weeks ago)
| |           added comments for burto - weznon
* | c7f5bf3 - Thu, 2 Nov 2017 14:52:11 -0400 (2 weeks ago)
|/            telebop update - Burtorustum
* 5f64187 - Wed, 1 Nov 2017 15:22:04 -0400 (2 weeks ago)
|           idk possibly added arm stuff - weznon
* 501e3f7 - Wed, 1 Nov 2017 15:14:57 -0400 (2 weeks ago)
|           added arm servo stuff to bot, hopefully it works - weznon
* 8e9a547 - Wed, 1 Nov 2017 14:50:57 -0400 (2 weeks ago)
|           updated sdk to 3.5 - weznon
* 8897bf7 - Tue, 31 Oct 2017 16:57:48 -0400 (2 weeks ago)
|           Added more functions and motors to bot class, cleaned autons up using improved class. - Burtorustum
* cab994f - Mon, 30 Oct 2017 15:10:16 -0400 (2 weeks ago)
|           removed field centric drive opmode - Burtorustum
* a7e7e08 - Mon, 30 Oct 2017 15:08:59 -0400 (2 weeks ago)
|           edits to bot class and telebop - Burtorustum
* e0ac1bd - Fri, 27 Oct 2017 18:43:18 -0400 (3 weeks ago)
|           PUSH - Burtorustum
* 101441f - Fri, 27 Oct 2017 15:51:32 -0400 (3 weeks ago)
|           updated to 3.0--> gradles etx change - Burtorustum
* 55404b0 - Mon, 23 Oct 2017 15:35:42 -0400 (3 weeks ago)
|           removed the autonbot and telebopbot classes to make room for wen's new system - Burtorustum
* d4c1fcc - Mon, 23 Oct 2017 15:18:45 -0400 (3 weeks ago)
|           removed constructor from bot class - Burtorustum
* 34ea701 - Mon, 23 Oct 2017 15:15:17 -0400 (3 weeks ago)
|           Added subclass autonbot and teleopbot - Burtorustum
* d3a3a12 - Mon, 23 Oct 2017 15:00:41 -0400 (3 weeks ago)
|           updates to autons and bot class - Burtorustum
* 95786bf - Mon, 23 Oct 2017 13:58:30 -0400 (3 weeks ago)
|           bot changes + vuforia test - Burtorustum
* feb2bf9 - Mon, 23 Oct 2017 12:46:24 -0400 (3 weeks ago)
|           Bot class updates - Burtorustum
* ac631b8 - Fri, 20 Oct 2017 19:20:02 -0400 (4 weeks ago)
|           updates from work session 10/20/17 - Burtorustum
* 531dfd7 - Fri, 20 Oct 2017 18:19:32 -0400 (4 weeks ago)
|           updates from work session 10/20/17 - Burtorustum
* bdae268 - Sun, 8 Oct 2017 14:02:29 -0400 (6 weeks ago)
|           Rob stuff, pushing so wen can fix shit - Burtorustum
* d97f8be - Sun, 8 Oct 2017 13:01:19 -0400 (6 weeks ago)
|           Added basic tank drive teleop for 10/8/17 - weznon
* 72abd86 - Sun, 8 Oct 2017 10:37:10 -0400 (6 weeks ago)
|           stuff for workshop - Burtorustum
* 7a8e6f5 - Sun, 8 Oct 2017 08:24:57 -0400 (6 weeks ago)
|           field centric thing - Burtorustum
* a8bc827 - Fri, 29 Sep 2017 19:59:06 -0400 (7 weeks ago)
|           added a comment, but finishing yp for the day so logging it even though its small - weznon
* 9d6d133 - Fri, 29 Sep 2017 19:00:14 -0400 (7 weeks ago)
|           removed extra created by comment - weznon
* 322df22 - Fri, 29 Sep 2017 18:56:21 -0400 (7 weeks ago)
|           created the teleopautobalance teleop - weznon
*   c0f39f7 - Fri, 29 Sep 2017 18:33:10 -0400 (7 weeks ago)
|\            Merge branch 'master' of https://github.com/0207-Critical-Mass/0207-Relic-Recovery - weznon
| * 8dac403 - Tue, 26 Sep 2017 12:19:34 -0400 (7 weeks ago)
| |           Minor changes to Field Centric so that we can effectively test the new CalibrateGyro opmode - Burtorustum
| * 033b1f4 - Tue, 26 Sep 2017 12:14:58 -0400 (7 weeks ago)
| |           Added OpMode for calibration of the gyro. - Burtorustum
* | 990d96c - Fri, 29 Sep 2017 18:33:03 -0400 (7 weeks ago)
|/            Worked on bot clas, added imu related code, added simple example drive function - weznon
* 7c3b8c8 - Mon, 25 Sep 2017 21:25:49 -0400 (7 weeks ago)
|           Added some comments - weznon
* 0fd860a - Mon, 25 Sep 2017 21:25:41 -0400 (7 weeks ago)
|           made it lowercase folder - weznon
* bfedca0 - Sat, 23 Sep 2017 12:21:04 -0400 (8 weeks ago)
|           minor change to bot class and bug fixes in Field Centric - Burtorustum
* 6a16c6b - Fri, 22 Sep 2017 23:38:07 -0400 (8 weeks ago)
|           Update README.md - Robert
* 686665b - Fri, 22 Sep 2017 18:18:40 -0400 (8 weeks ago)
|           Minor changes to the field centric drive opmode - Burtorustum
* 9dc5483 - Fri, 22 Sep 2017 15:33:26 -0400 (8 weeks ago)
|           Moved around old testbot opmodes - Burtorustum
* ebfe628 - Fri, 22 Sep 2017 15:26:47 -0400 (8 weeks ago)
|           commented the field centric drive code -- requires testing still. - Burtorustum
* a91f55b - Fri, 22 Sep 2017 15:08:18 -0400 (8 weeks ago)
|           fixed the field centric drive (I think) - Burtorustum
*   fb7d18d - Fri, 22 Sep 2017 13:10:38 -0400 (8 weeks ago)
|\            Merge branch 'master' of https://github.com/0207-Critical-Mass/0207-Relic-Recovery - Burtorustum
| * 97ebcb4 - Fri, 22 Sep 2017 13:02:53 -0400 (8 weeks ago)
| |           removed impo from Bot.java - weznon
| * 7f16776 - Fri, 22 Sep 2017 13:01:42 -0400 (8 weeks ago)
| |           Worked on enums. Migrated movement enum to ADT to allow for Angle and easier drive function maker - weznon
* | 9ca949b - Fri, 22 Sep 2017 13:10:26 -0400 (8 weeks ago)
|/            Worked on Field Centric Driving, NOTE: problem with setting negative powers on wheels - Burtorustum
* 110b66e - Fri, 22 Sep 2017 11:23:01 -0400 (8 weeks ago)
|           hyperlul fixed that interesting drive orientation - weznon
* be64251 - Fri, 22 Sep 2017 11:09:51 -0400 (8 weeks ago)
|           added motor related stuff to Bot class, made init function have arguments - weznon
* 35327ec - Fri, 22 Sep 2017 11:09:33 -0400 (8 weeks ago)
|           Made better readme for teamcode - weznon
* 0d28392 - Fri, 22 Sep 2017 10:06:10 -0400 (8 weeks ago)
|           bzzt bzzt - weznon
* f707fe1 - Fri, 22 Sep 2017 10:07:42 -0400 (8 weeks ago)
|           lol field centric drive in 2017 - Burtorustum
* b8f69d2 - Wed, 20 Sep 2017 16:20:27 -0400 (8 weeks ago)
|           Split field centric drive work into a seperate class - Burtorustum
* 2f7d080 - Mon, 18 Sep 2017 08:12:06 -0400 (8 weeks ago)
|           Some changes to telebop -- started wrok on field centric drive - Burtorustum
* 75f169e - Sat, 16 Sep 2017 12:35:37 -0400 (9 weeks ago)
|           moved test bot stuff into a seperate folder and made some minor adjustments to the bot class. - Burtorustum
* 4c17073 - Fri, 15 Sep 2017 08:07:30 -0400 (9 weeks ago)
|           Finished tester bot - Burtorustum
* d1bcac3 - Thu, 14 Sep 2017 17:42:53 -0400 (9 weeks ago)
|           Learning rev stuff and making robot do things for activities fair - Burtorustum
* 13b7c9e - Mon, 11 Sep 2017 12:08:36 -0400 (9 weeks ago)
|           Added PDFs - Burtorustum
* c2165f3 - Sun, 10 Sep 2017 20:02:11 -0400 (10 weeks ago)
|           added telebop class and 2 auton classes -- unfilled - Burtorustum
* 94f2f8b - Sat, 9 Sep 2017 13:44:19 -0400 (10 weeks ago)
|           Created standalone package for enums - weznon
* c2e111a - Sat, 9 Sep 2017 12:33:47 -0400 (10 weeks ago)
|           Finish updating - weznon
* a3c0292 - Sat, 9 Sep 2017 12:31:54 -0400 (10 weeks ago)
|           Updated sdk - weznon
* 12ad864 - Sat, 9 Sep 2017 12:31:40 -0400 (10 weeks ago)
|           Updated sdk? - weznon
* 17319a8 - Tue, 29 Aug 2017 21:29:25 -0400 (3 months ago)
|           updated gitignore - Burtorustum
* 775db69 - Fri, 18 Aug 2017 17:56:14 -0400 (3 months ago)
|           update to bot - Burtorustum
* 255bb0e - Mon, 7 Aug 2017 23:38:28 -0400 (3 months ago)
|           updates to bot class - Burtorustum
* 51efa9d - Mon, 7 Aug 2017 22:42:32 -0400 (3 months ago)
|           minor updates to bot actions for future us to fill in (actions enum). - Burtorustum
* e77f0d9 - Mon, 7 Aug 2017 18:11:15 -0400 (3 months ago)
|           Added some basic enums; we should add more as we figure out the bot's hardware - weznon
* bdfb595 - Mon, 7 Aug 2017 16:45:42 -0400 (3 months ago)
|           slight improvements to bot class - Burtorustum
* ab6379d - Mon, 7 Aug 2017 13:56:16 -0400 (3 months ago)
|           removed slack test file - Burtorustum
* c16594f - Sun, 6 Aug 2017 17:26:44 -0400 (3 months ago)
|           testing for slack integration - Burtorustum
* a426297 - Sun, 6 Aug 2017 17:13:59 -0400 (3 months ago)
|           Added enum for bot actions and the bot class to be filled in. - Burtorustum
* a10c785 - Sat, 5 Aug 2017 15:52:55 -0400 (3 months ago)
|           lol notes - Burtorustum
* 0bf0f57 - Fri, 4 Aug 2017 18:10:36 -0400 (3 months ago)
|           Fixed submodule issue - Burtorustum
* d922861 - Fri, 4 Aug 2017 18:02:31 -0400 (3 months ago)
|           fixing submodule issue. Removed toTC.sh - Burtorustum
* 05944d2 - Fri, 4 Aug 2017 17:56:09 -0400 (3 months ago)
|           Updated FTC App to 3.2 Beta - Burtorustum
* 7d8b50a - Fri, 4 Aug 2017 17:27:34 -0400 (3 months ago)
            Initial commit - Robert

\end{lstlisting}

