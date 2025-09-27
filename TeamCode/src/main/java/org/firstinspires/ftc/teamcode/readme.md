# Sorter Sys 

---

## Overview

- **Robot capacity**: 3 artifacts
- **Artifact colors**: *Green* or *Purple*
- **Sensors**: 3 color sensors to detect each artifact’s color
- **Storage**: `artifactArray[3]` holds the current inventory
- **Scoring**:
    - Each scored artifact = 1 point
    - If a complete motif is scored, points are doubled (×2)

---

## Motifs

The robot can earn double points for any one of the following color sequences.  
The chosen motif is fixed **before** the match starts.

| Motif # | Sequence |
|---------|----------|
| 1 | GREEN → PURPLE → PURPLE |
| 2 | PURPLE → GREEN → PURPLE |
| 3 | PURPLE → PURPLE → GREEN |

---

## Class Responsibilities

| Responsibility | Key Methods / Logic |
|-----------------|---------------------|
| **Inventory Management** | Store, update, and clear the `artifactArray[3]`. |
| **Sorting & Release** | `releaseNext()` – releases artifacts in motif‑compliant order. |
| **Scoring** | Keep track of points; apply double‑point bonus when a motif is completed. |
| **History Tracking** | Record all released artifacts to determine the next artifact for the motif and whether a double point condition has been met. |

---

## `releaseNext()` Logic

1. **Determine Next Artifact in Motif**
    - Look at history: how many artifacts have already been released that match the motif so far.
    - Identify the next expected color (e.g., if motif is *GREEN, PURPLE, PURPLE* and two PURPLEs have already been released, the next should be GREEN).

2. **Search Inventory**
    - If an artifact of the required color exists in `artifactArray`, release that one.
    - Otherwise, release the first available artifact (any color).

3. **Update State**
    - Remove the released artifact from `artifactArray`.
    - Append it to a *release history* list.
    - Update score: +1 point; if the motif is now complete, apply double‑point bonus.

4. **Edge Cases**
    - If inventory is empty → do nothing.
    - If no artifact matches the expected color → fallback to the first available artifact.

---

## Example Flow

Given:
- Motif = `GREEN, PURPLE, PURPLE`
- Inventory (in order): `[PURPLE, PURPLE, GREEN]`

| Call | Expected Release | Remaining Inventory |
|------|------------------|---------------------|
| 1    | GREEN            | `[PURPLE, PURPLE]`  |
| 2    | PURPLE (first)   | `[PURPLE]`          |
| 3    | PURPLE (second)  | `[]`                |

After the third call, the motif is completed and the robot receives **double points** for that set.

---

## Suggested Data Structures

```java
enum Color { GREEN, PURPLE }

class SorterSys {
    private Color[] inventory = new Color[3];
    private List<Color> releaseHistory = new ArrayList<>();
    private int score = 0;
    private Color[] motif;          // e.g., {GREEN, PURPLE, PURPLE}
    
    // constructor, setters/getters ...
    
    public void releaseNext() {
        if (Arrays.stream(inventory).allMatch(c -> c == ARTIFACT_COLOR.EMPTY)) return -1;

        ARTIFACT_COLOR expected = motifPattern[releaseHistory.size() % 3];

        // Try to find a matching color in the inventory
        for (int i = 0; i < 3; i++) {
            if (inventory[i] == expected) {
                releaseHistory.add(expected);
                return i;
            }
        }

        // Now release the next available artifact
        for (int i = 0; i < 3; i++) {
            if (inventory[i] != ARTIFACT_COLOR.EMPTY) {
                releaseHistory.add(inventory[i]);
                return i;
            }
        }
        return -1;
    }

    // helper methods: inventoryEmpty(), determineNextColorForMotif(),
    // findFirstMatching(), removeFromInventory(), isMotifCompleted() ...
}
```

---

## Summary

- **Capacity**: 3 artifacts
- **Colors**: Green / Purple
- **Scoring**: 1 point per artifact; double points for a complete motif
- **Core method**: `releaseNext()` orchestrates artifact release, inventory updates, and scoring based on the chosen motif.

Implement the helper functions and state tracking to satisfy these rules.


## TeamCode Module

Welcome!

This module, TeamCode, is the place where you will write/paste the code for your team's
robot controller App. This module is currently empty (a clean slate) but the
process for adding OpModes is straightforward.

## Creating your own OpModes

The easiest way to create your own OpMode is to copy a Sample OpMode and make it your own.

Sample opmodes exist in the FtcRobotController module.
To locate these samples, find the FtcRobotController module in the "Project/Android" tab.

Expand the following tree elements:
 FtcRobotController/java/org.firstinspires.ftc.robotcontroller/external/samples

### Naming of Samples

To gain a better understanding of how the samples are organized, and how to interpret the
naming system, it will help to understand the conventions that were used during their creation.

These conventions are described (in detail) in the sample_conventions.md file in this folder.

To summarize: A range of different samples classes will reside in the java/external/samples.
The class names will follow a naming convention which indicates the purpose of each class.
The prefix of the name will be one of the following:

Basic:  	This is a minimally functional OpMode used to illustrate the skeleton/structure
            of a particular style of OpMode.  These are bare bones examples.

Sensor:    	This is a Sample OpMode that shows how to use a specific sensor.
            It is not intended to drive a functioning robot, it is simply showing the minimal code
            required to read and display the sensor values.

Robot:	    This is a Sample OpMode that assumes a simple two-motor (differential) drive base.
            It may be used to provide a common baseline driving OpMode, or
            to demonstrate how a particular sensor or concept can be used to navigate.

Concept:	This is a sample OpMode that illustrates performing a specific function or concept.
            These may be complex, but their operation should be explained clearly in the comments,
            or the comments should reference an external doc, guide or tutorial.
            Each OpMode should try to only demonstrate a single concept so they are easy to
            locate based on their name.  These OpModes may not produce a drivable robot.

After the prefix, other conventions will apply:

* Sensor class names are constructed as:    Sensor - Company - Type
* Robot class names are constructed as:     Robot - Mode - Action - OpModetype
* Concept class names are constructed as:   Concept - Topic - OpModetype

Once you are familiar with the range of samples available, you can choose one to be the
basis for your own robot.  In all cases, the desired sample(s) needs to be copied into
your TeamCode module to be used.

This is done inside Android Studio directly, using the following steps:

 1) Locate the desired sample class in the Project/Android tree.

 2) Right click on the sample class and select "Copy"

 3) Expand the  TeamCode/java folder

 4) Right click on the org.firstinspires.ftc.teamcode folder and select "Paste"

 5) You will be prompted for a class name for the copy.
    Choose something meaningful based on the purpose of this class.
    Start with a capital letter, and remember that there may be more similar classes later.

Once your copy has been created, you should prepare it for use on your robot.
This is done by adjusting the OpMode's name, and enabling it to be displayed on the
Driver Station's OpMode list.

Each OpMode sample class begins with several lines of code like the ones shown below:

```
 @TeleOp(name="Template: Linear OpMode", group="Linear Opmode")
 @Disabled
```

The name that will appear on the driver station's "opmode list" is defined by the code:
 ``name="Template: Linear OpMode"``
You can change what appears between the quotes to better describe your opmode.
The "group=" portion of the code can be used to help organize your list of OpModes.

As shown, the current OpMode will NOT appear on the driver station's OpMode list because of the
  ``@Disabled`` annotation which has been included.
This line can simply be deleted , or commented out, to make the OpMode visible.



## ADVANCED Multi-Team App management:  Cloning the TeamCode Module

In some situations, you have multiple teams in your club and you want them to all share
a common code organization, with each being able to *see* the others code but each having
their own team module with their own code that they maintain themselves.

In this situation, you might wish to clone the TeamCode module, once for each of these teams.
Each of the clones would then appear along side each other in the Android Studio module list,
together with the FtcRobotController module (and the original TeamCode module).

Selective Team phones can then be programmed by selecting the desired Module from the pulldown list
prior to clicking to the green Run arrow.

Warning:  This is not for the inexperienced Software developer.
You will need to be comfortable with File manipulations and managing Android Studio Modules.
These changes are performed OUTSIDE of Android Studios, so close Android Studios before you do this.
 
Also.. Make a full project backup before you start this :)

To clone TeamCode, do the following:

Note: Some names start with "Team" and others start with "team".  This is intentional.

1)  Using your operating system file management tools, copy the whole "TeamCode"
    folder to a sibling folder with a corresponding new name, eg: "Team0417".

2)  In the new Team0417 folder, delete the TeamCode.iml file.

3)  the new Team0417 folder, rename the "src/main/java/org/firstinspires/ftc/teamcode" folder
    to a matching name with a lowercase 'team' eg:  "team0417".

4)  In the new Team0417/src/main folder, edit the "AndroidManifest.xml" file, change the line that contains
         package="org.firstinspires.ftc.teamcode"
    to be
         package="org.firstinspires.ftc.team0417"

5)  Add:    include ':Team0417' to the "/settings.gradle" file.
    
6)  Open up Android Studios and clean out any old files by using the menu to "Build/Clean Project""