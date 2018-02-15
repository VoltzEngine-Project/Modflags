package com.builtbroken.mc.modflag;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.ISave;
import com.builtbroken.mc.framework.access.AccessProfile;
import com.builtbroken.mc.framework.access.api.IProfileContainer;
import com.builtbroken.mc.imp.transform.region.Cube;
import com.builtbroken.mc.imp.transform.vector.Location;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 2/16/2015.
 */
public class Region implements ISave, IProfileContainer
{
    //TODO crease super cube bounds to make checking if something is inside the region easier
    public List<Cube> segments = new ArrayList();
    public List<EntityPlayer> players_in_region = new ArrayList();
    public List<String> flags = new ArrayList<>();

    public AccessProfile profile;

    public final String name;

    public Region(String name)
    {
        this.name = name;
    }

    public boolean doesContainPoint(IPos3D pos)
    {
        //TODO look at reducing this list in case segments contains a lot of complex cubes
        for (Cube cube : segments)
        {
            if (cube.isWithin(pos))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        if (nbt.hasKey("segments"))
        {
            NBTTagList list = nbt.getTagList("segments", 10);
            for (int i = 0; i < list.tagCount(); i++)
            {
                segments.add(new Cube(list.getCompoundTagAt(i)));
            }
        }
        if (nbt.hasKey("profile"))
        {
            setAccessProfile(new AccessProfile());
            getAccessProfile().load(nbt.getCompoundTag("profile"));
        }
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbt)
    {
        if (segments.size() > 0)
        {
            NBTTagList list = new NBTTagList();
            for (Cube cube : segments)
            {
                list.appendTag(cube.toNBT());
            }
            nbt.setTag("segments", list);
        }

        NBTTagCompound tag = new NBTTagCompound();
        profile.save(tag);
        nbt.setTag("profile", tag);

        return nbt;
    }

    @Override
    public AccessProfile getAccessProfile()
    {
        if (profile == null)
        {
            profile = RegionManager.generateDefaultAccessProfile();
        }
        return profile;
    }

    @Override
    public void setAccessProfile(AccessProfile profile)
    {
        this.profile = profile;
    }

    @Override
    public boolean canAccess(String username)
    {
        return getAccessProfile().getUserAccess(username).getGroup() != null;
    }

    @Override
    public boolean hasNode(EntityPlayer player, String node)
    {
        return profile != null && getAccessProfile().hasNode(player, node);
    }

    @Override
    public boolean hasNode(String username, String node)
    {
        return profile != null && getAccessProfile().hasNode(username, node);
    }

    @Override
    public void onProfileChange()
    {

    }

    public boolean isCloseToAnyCorner(Location location, int distance)
    {
        for (Cube cube : segments)
        {
            if (cube.isCloseToAnyCorner(location, distance))
            {
                return true;
            }
        }
        return false;
    }
}
