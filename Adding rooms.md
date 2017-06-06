# Adding a room to Daedalus' Labyrinth via the config folder

## Part 1: Preliminaries

Rooms consist of two files. A structure file (.nbt) containing the blocks of the room and a properties file (.json) containing more info.

First decide on a name for the room. It must be unique. My room will be called "campsite".

Then decide what size your room will be. The following sizes are currently available:

    SINGLE (chunkX = 1, chunkZ = 1)
    X_2 (chunkX = 2, chunkZ = 1)
    X_3 (chunkX = 3, chunkZ = 1)
    X_4 (chunkX = 4, chunkZ = 1)
    DOUBLE (chunkX = 2, chunkZ = 2)
    Z_2 (chunkX = 1, chunkZ = 2)
    Z_3 (chunkX = 1, chunkZ = 3)
    Z_4 (chunkX = 1, chunkZ = 4)
    TRIPLE (chunkX = 3, chunkZ = 3)

chunkX is the size in chunks along the x axis and chunkZ with the z axis accordingly.
The actual size in blocks will be

    size_in_blocks = size_in_chunks * 16 - 1

For my "campsite" I have decided on a SINGLE room, so it will be 15x15 blocks in x/z direction.
A room is 7 blocks high where the lowest layer is the floor. If you want your room to extend up or down that is also okay.

## Part 2: The Structure File

To make sure I build the proper size, I have constructed the walls of my room out of bedrock like they would be in the labyrinth.

[Picture](http://i.imgur.com/upcZ9OC.png)

Then I have built the room itself.

[Picture](http://i.imgur.com/SaO443W.png)

Now that the room is built you'll need to save it as a structure. (The walls are not part of the room.) You can use
* [Minecrafts Structure Block](http://minecraft.gamepedia.com/Structure_Block)
* the command "/labyrinth save-template x1 y1 z1 x2 y2 z2 name". Here \<x1 y1 z1\> and \<x2 y2 z2\> are the corners of the structure, both part of it.

I used the structure block. As you can see, the borders line up with the area the room is in.
You'll need to adjust "Relative Position" and "Structure Size" accordingly.

[Picture: Settings of the Structure Block](http://i.imgur.com/20G3oX7.png)

[Picture: Room with Structure Block outline](http://i.imgur.com/UmzRiuJ.png)

You'll now find the first file needed in .minecraft/worlds/YourWorldName/structures/YourRoomName.nbt

## Part 3: The Properties File

Create a file named YourRoomName.json; so mine will be called "campsite.json".
This file has two mandatory elements:
* an integer named "weight": the weight of the room, i.e. a room of weight 2 will be twice as common as a room of weight 1
* an array of strings containing the sizes this room fits in. For my room this will only be "SINGLE"

My file now looks like this:

    {
        "weight":1,
        "size":
        [
            "SINGLE"
        ]
    }

### Extending up and down

If your room extends up or down from the normal y-levels, add the amount as integers to the root object in the properties file, for example:

    "up":2,
    "down":4

### Transformations

With transformations you can do two things to your room:
* rotation: Put "any", "CLOCKWISE_90", "CLOCKWISE_180" or "COUNTERCLOCKWISE_90" here to rotate your room. "any" picks a random rotation.
* name: Put the name of the nbt file here to specify the file that the room is in. This means you can have multiple rooms with one json file depending on the surrounding rooms.

You can use triggers to choose when to apply transformations. Without a trigger the transformation is applied every time the room gets generated.
* size: This string value must match the size of the room.
* exit*: This is a boolean that must match whether there is an exit in that direction. (Valid directions: "North", "East", "South", "West").

"Transformations" is an array containing objects. Each object is one transformations and has the above mentioned parameters. Only the first transformations that fits on the generated room is executed and the order in which the transformations are read is not determined. See [canal](https://github.com/abecderic/labyrinth/blob/master/src/main/resources/assets/labyrinth/structures/canal.json) and [checkpoint](https://github.com/abecderic/labyrinth/blob/master/src/main/resources/assets/labyrinth/structures/checkpoint.json) for examples.

### Replacements

With replacements you can change blocks in your room once it was spawned. One replacement object contains the following:
* original: containing the block that is in the nbt file and will be replaced
* replacement: containing multiple blocks to replace the original block with
* type: "all" (will replace all original blocks with the same replacement) or "single" (will choose a new replacement for every original block)

A block here is an object containing the "name" of the block, for example "minecraft:stone" and optionally an array of properties. See [Block states](http://minecraft.gamepedia.com/Block_states) for valid properties.

In the room [lab](https://github.com/abecderic/labyrinth/blob/master/src/main/resources/assets/labyrinth/structures/lab.json), Stone Bricks are replaced by Stone Bricks, Cracked Stone Bricks and Chiseled Stone Bricks ([properties of Stone Bricks](http://minecraft.gamepedia.com/Stone_Bricks#Block_state)). Some replacements are repeated multiple times to adjust the ratio.

If a replacement block is not found, for example if you mistyped the name or the mod the block is from is not installed, the Labyrinth will simply put an Air block there.

Instead of specifying the replacement block itself, you can use the OreDictionary: A replacement name of "-ore:plankWood" will choose any block that has an OreDictionary name of "plankWood". The replacement "-ore-prefix:ore" will find a block which has an OreDictionary name that begins with "ore". This will find "oreIron" and "oreLapis" among others and then choose blocks that have that OreDictionary name. See [cave](https://github.com/abecderic/labyrinth/blob/master/src/main/resources/assets/labyrinth/structures/cave.json) for an example.

Multiple replacements will be executed in a not determined order. (One replacement meaning everything in one object here, so replacing all of the original block with a new block.)

You can find all rooms currently in the mod [here on github](https://github.com/abecderic/labyrinth/tree/master/src/main/resources/assets/labyrinth/structures);
use that for reference and for ideas what you can do with properties files.

## Part 4: Putting it all together

Put both files (i.e. YourRoomName.nbt and YourRoomName.json) in the folder .minecraft/config/labyrinth/structures/
Create that folder if it doesn't exist.

Then edit the labyrinth.cfg file and add the name of your room to the list of rooms in the file.

Start the game and watch the console. In the initialisation phase it will print out

    [labyrinth]: Reading room info for campsite

Make sure it doesn't crash at that point and doesn't print an error. All rooms are loaded when "Finished reading all rooms" is printed to the console. Then explore the labyrinth to make sure the room gets generated correctly.
(Tip: Change the config file to remove the roof from the labyrinth rooms for easier exploring.) You can also force-spawn the room where you currently are using the "spawn-room" command.

## Closing Words

If you encounter a problem while following this guide, leave a bug report in the issues section. Please be very descriptive about what you did, what you expected to happen and what did happen instead. Also upload the files for any rooms you made that might be related to the problem.

You made a few nice rooms and would like to see them in the mod? I'm open to pull requests containing rooms.
