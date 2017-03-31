package com.builtbroken.mc.modflag;

import com.builtbroken.mc.core.commands.CommandVE;
import com.builtbroken.mc.core.commands.ext.ModularCommandRemoveAdd;
import com.builtbroken.mc.core.commands.prefab.ModularCommand;
import com.builtbroken.mc.modflag.commands.*;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/30/2017.
 */
public class Mod
{
    public void loadCommands()
    {
        if (!CommandVE.disableModflagCommands)
        {
            ModularCommand region_add = new ModularCommandRemoveAdd("region", "region", false);
            ModularCommand region_remove = new ModularCommandRemoveAdd("region", "region", true);
            ModularCommand region = new CommandRegion();

            CommandVE.INSTANCE.addToNewCommand(new CommandNewRegion());
            CommandVE.INSTANCE.addToRemoveCommand(new CommandRemoveRegion());

            region_add.addCommand(new CommandAddUserToRegion());
            region_remove.addCommand(new CommandRemoveUserFromRegion());

            region.addCommand(region_add);
            region.addCommand(region_remove);
            CommandVE.INSTANCE.addCommand(region);
        }
    }
}
