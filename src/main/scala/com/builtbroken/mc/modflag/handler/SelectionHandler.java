package com.builtbroken.mc.modflag.handler;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.packet.PacketSelectionData;
import com.builtbroken.mc.imp.transform.region.Cube;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.lib.world.edit.Selection;
import com.builtbroken.mc.modflag.Region;
import com.builtbroken.mc.modflag.RegionManager;
import com.google.common.collect.Maps;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Keeps track of the world cuboid selections for each player. As well which selections
 * should be rendered on the player's client.
 * <p/>
 * Created by robert on 2/15/2015.
 */
public class SelectionHandler
{
    public static final SelectionHandler INSTANCE = new SelectionHandler();

    private static final HashMap<String, Selection> selections = Maps.newHashMap();

    private SelectionHandler()
    {
    }

    /**
     * gets the selection of the player, or creates one if none exists.
     * This is mainly so we dont have to deal with NPEs later.
     */
    public static Selection getSelection(EntityPlayer player)
    {
        Selection out = selections.get(player.getCommandSenderName());

        if (out == null)
        {
            out = new Selection();
            selections.put(player.getCommandSenderName(), out);
        }

        return out;
    }

    /**
     * gets the selection of the player, or creates one if none exists.
     * This is mainly so we dont have to deal with NPEs later.
     */
    public static void setSelection(EntityPlayer player, Selection cuboid)
    {
        selections.put(player.getCommandSenderName(), cuboid);
        if (!player.worldObj.isRemote)
        {
            if (player instanceof EntityPlayerMP)
                updatePlayerRenderData((EntityPlayerMP) player);
        }
    }

    /**
     * Resets the selection of the player to a cube with null components
     */
    private void clearSelection(EntityPlayer player)
    {
        Selection select = selections.get(player);
        if (select != null)
        {
            select.set(null, null);
        }
    }

    public static void updatePlayerRenderData(EntityPlayerMP player)
    {
        List<Cube> cubes = new ArrayList();
        List<Cube> regions = new ArrayList();
        Cube selection = getSelection(player);

        for (Cube cube : selections.values())
        {
            if (cube != selection && cube.distance(new Pos(player)) <= 160)
            {
                cubes.add(cube);
            }
        }
        for(Region region : RegionManager.getControllerForWorld(player.worldObj).getRegionsNear(player, 160))
        {
            for(Cube cube : region.segments)
            {
                if(cube.isCloseToAnyCorner(new Pos(player), 160))
                {
                    regions.add(cube);
                }
            }
        }

        Engine.instance.packetHandler.sendToPlayer(new PacketSelectionData(selection, cubes, regions), player);
    }

    // ===========================================
    // player tracker things here.
    // tHandlers clearing of the selections
    // ===========================================


    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        clearSelection(event.player);
        if (event.player instanceof EntityPlayerMP)
            updatePlayerRenderData((EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event)
    {
        clearSelection(event.player);
        if (event.player instanceof EntityPlayerMP)
            updatePlayerRenderData((EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (!event.world.isRemote && event.phase == TickEvent.Phase.END && event.world.getWorldInfo().getWorldTime() % 20 == 0)
        {
            //Sort threw all players in world and update render data
            for (Object obj : event.world.playerEntities)
            {
                if (obj instanceof EntityPlayerMP)
                    updatePlayerRenderData((EntityPlayerMP) obj);
            }
        }
    }
}
