# LoveeeCraft Minecraft Server Plugin

LoveeeCraft is a Minecraft server plugin for Paper that adds End-themed gameplay: a stronger Ender Dragon, an Enderman drop chance, and a craftable Void Blade with bonus damage against End creatures.

## Install

1. Build the plugin with Java 21 and Maven by running `mvn package` from the project directory.
2. Copy `target/plugin.jar` into your Paper server’s `plugins` folder.
3. Start or restart the server.

## Use

- Craft the Void Blade with this pattern in a crafting table: ` E ` / `ENE` / ` D `, where `E` is an Ender Eye, `N` is a Netherite Sword, and `D` is Dragon’s Breath.
- The Void Blade adds 5 damage when used against entities in the End.
- Endermen that die in the End have a 20% chance to drop an Echo Shard.
- Ender Dragons are set to 400 health and given the name “The Ender Dragon.”
- Operators with the `loveeeend.giveblade` permission can run `/loveeeend giveblade`. Run `/loveeeend` for help.

This project is a Java Edition Paper server plugin, not a Minecraft Bedrock add-on. It does not include a Bedrock Behavior Pack or Resource Pack, custom entity textures or animations, armor, structures, or an exported `.mcaddon`.

---

More Minecraft plugin projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/1dd6f481-3a29-40da-82b8-563708a0e646?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=1dd6f481-3a29-40da-82b8-563708a0e646
