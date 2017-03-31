package com.builtbroken.mc.modflag.commands;

import com.builtbroken.mc.core.commands.prefab.SubCommand;
import com.builtbroken.mc.core.handler.SelectionHandler;
import com.builtbroken.mc.imp.transform.region.Cube;
import com.builtbroken.mc.modflag.Region;
import com.builtbroken.mc.modflag.RegionManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

/**
 * Created by robert on 2/17/2015.
 */
public class CommandNewRegion extends SubCommand
{
    public CommandNewRegion()
    {
        super("region");
    }

    @Override
    public boolean handleEntityPlayerCommand(EntityPlayer player, String[] args)
    {
        if(args.length > 0)
        {
            String name = args[0];
            if(RegionManager.getControllerForWorld(player.worldObj).getRegion(name) == null)
            {
                Cube cube = SelectionHandler.getSelection(player);
                if(cube != null && cube.isValid())
                {
                    Region region = RegionManager.getControllerForWorld(player.worldObj).createNewRegion(name, cube);
                    if(region != null)
                    {
                        region.getAccessProfile().getGroup("owner").addMember(player);
                        player.addChatMessage(new ChatComponentText("Region created"));
                    }
                    else
                    {
                        player.addChatMessage(new ChatComponentText("Error creating region"));
                    }
                }
                else
                {
                    player.addChatMessage(new ChatComponentText("Invalid selection to create region"));
                }
            }
            else
            {
                player.addChatMessage(new ChatComponentText("A region by that name already exists"));
            }
        }
        else
        {
            player.addChatMessage(new ChatComponentText("Need a region name"));
        }
        return true;
    }

    @Override
    public boolean isHelpCommand(String[] args)
    {
        return args.length > 0 && (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"));
    }

    @Override
    public boolean handleConsoleCommand(ICommandSender sender, String[] args)
    {
        sender.addChatMessage(new ChatComponentText("This command can't be used via the console"));
        return true;
    }
}
