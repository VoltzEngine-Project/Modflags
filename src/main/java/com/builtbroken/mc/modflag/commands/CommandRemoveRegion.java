package com.builtbroken.mc.modflag.commands;

import com.builtbroken.mc.modflag.Region;
import com.builtbroken.mc.modflag.RegionManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

/**
 * Created by robert on 2/17/2015.
 */
public class CommandRemoveRegion extends SubCommandRegion
{
    @Override
    public boolean handle(ICommandSender sender, Region region, String[] args)
    {
        if(sender.getEntityWorld() != null)
        {
            if(RegionManager.getControllerForWorld(sender.getEntityWorld()).removeRegion(region))
            {
                sender.addChatMessage(new ChatComponentText("Region removed"));
            }
            else
            {
                sender.addChatMessage(new ChatComponentText("Error removing region"));
            }
        }
        else
        {
            sender.addChatMessage(new ChatComponentText("You need to be in the same world as the region to remove it"));
        }
        return true;
    }
}
