package com.builtbroken.mc.modflag.mod;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.core.commands.CommandVE;
import com.builtbroken.mc.core.commands.ext.ModularCommandRemoveAdd;
import com.builtbroken.mc.core.commands.prefab.ModularCommand;
import com.builtbroken.mc.framework.mod.AbstractMod;
import com.builtbroken.mc.framework.mod.AbstractProxy;
import com.builtbroken.mc.modflag.commands.*;
import com.builtbroken.mc.modflag.handler.SelectionHandler;
import com.builtbroken.mc.modflag.mod.content.ItemSelectionWand;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/30/2017.
 */
@Mod(modid = ModFlagLoader.DOMAIN, name = "VoltzEngine mod protection, flag, and region system", version = References.VERSION, dependencies = "required-after:voltzengine")
public class ModFlagLoader extends AbstractMod
{
    public static final String DOMAIN = References.DOMAIN + "modflag";

    @SidedProxy(modId = ModFlagLoader.DOMAIN, clientSide = "com.builtbroken.mc.modflag.mod.client.ClientProxy", serverSide = "com.builtbroken.mc.modflag.mod.CommonProxy")
    public static CommonProxy proxy;

    public ModFlagLoader()
    {
        super(DOMAIN);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        MinecraftForge.EVENT_BUS.register(SelectionHandler.INSTANCE);
        FMLCommonHandler.instance().bus().register(SelectionHandler.INSTANCE);


        if (getConfig().get("Content", "LoadSelectionTool", true, "Admin tool for selecting areas on the ground for world manipulation or other tasks.").getBoolean(true))
        {
            getManager().newItem("ve.selectiontool", new ItemSelectionWand());
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event)
    {
        super.loadComplete(event);
    }

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

    @Override
    public AbstractProxy getProxy()
    {
        return proxy;
    }
}
