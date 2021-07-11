package pt.m0rais.crates.util;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Support for 1.8 to 1.17+
 */
public class NBTUtil {

    private final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    private Class<?> NBTTagCompoundClass;
    private Method asNMSCopy;
    private Method asCraftMirror;
    private Method setNBTTagCompound;
    private Method hasNBTTagCompound;
    private Method getNBTTagCompound;

    private Method hasKey;

    private Method getString;
    private Method setString;

    public ItemStack setString(ItemStack item, String key, String value) {
        try {
            Object nmsItem = asNMSCopy.invoke(null, item);
            boolean hasNBTTag = (boolean) hasNBTTagCompound.invoke(nmsItem);
            Object NBTTagCompound = hasNBTTag ? getNBTTagCompound.invoke(nmsItem) : NBTTagCompoundClass.newInstance();
            setString.invoke(NBTTagCompound, key, value);
            setNBTTagCompound.invoke(nmsItem, NBTTagCompound);
            return (ItemStack) asCraftMirror.invoke(null, nmsItem);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return item;
    }

    public String getString(ItemStack item, String key) {
        try {
            Object nmsItem = asNMSCopy.invoke(null, item);
            boolean hasNBTTag = (boolean) hasNBTTagCompound.invoke(nmsItem);
            if (hasNBTTag) {
                Object NBTTagCompound = getNBTTagCompound.invoke(nmsItem);
                return getString.invoke(NBTTagCompound, key).toString();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean hasKey(ItemStack item, String key) {
        try {
            Object nmsItem = asNMSCopy.invoke(null, item);
            boolean hasNBTTag = (boolean) hasNBTTagCompound.invoke(nmsItem);
            if (hasNBTTag) {
                Object NBTTagCompound = getNBTTagCompound.invoke(nmsItem);
                return (boolean) hasKey.invoke(NBTTagCompound, key);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    {
        try {

            int vID = Integer.parseInt(version.split("_")[1]);

            // Item Classes
            Class<?> itemStackClass;
            Class<?> craftItemStackClass = getOBClass("inventory.CraftItemStack");

            if (vID < 17) {
                itemStackClass = getNMSClass("ItemStack");
                NBTTagCompoundClass = getNMSClass("NBTTagCompound");
                Field map = NBTTagCompoundClass.getDeclaredField("map");
                map.setAccessible(true);
            } else {
                itemStackClass = Class.forName("net.minecraft.world.item.ItemStack");
                NBTTagCompoundClass = Class.forName("net.minecraft.nbt.NBTTagCompound");
                Field map = NBTTagCompoundClass.getDeclaredField("x");
                map.setAccessible(true);
            }

            // Item Handle Methods
            asNMSCopy = craftItemStackClass.getDeclaredMethod("asNMSCopy", ItemStack.class);
            asCraftMirror = craftItemStackClass.getDeclaredMethod("asCraftMirror", itemStackClass);


            // Item NBTTag Methods
            getNBTTagCompound = itemStackClass.getDeclaredMethod("getTag");
            hasNBTTagCompound = itemStackClass.getDeclaredMethod("hasTag");
            setNBTTagCompound = itemStackClass.getDeclaredMethod("setTag", NBTTagCompoundClass);

            // Basic NBTTag Handle Methods
            hasKey = NBTTagCompoundClass.getDeclaredMethod("hasKey", String.class);

            // String Access Handle

            getString = NBTTagCompoundClass.getDeclaredMethod("getString", String.class);
            setString = NBTTagCompoundClass.getDeclaredMethod("setString", String.class, String.class);

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private Class<?> getNMSClass(String name) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + version + "." + name);
    }

    private Class<?> getOBClass(String name) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
    }

}
