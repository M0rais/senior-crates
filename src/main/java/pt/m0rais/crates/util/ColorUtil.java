package pt.m0rais.crates.util;

public class ColorUtil {

    public static String getChatColorByDyeColor(int color) {
        switch (color) {
            case 0:
                return "§f";
            case 1:
                return "§6";
            case 2:
            case 10:
                return "§5";
            case 3:
                return "§b";
            case 4:
                return "§e";
            case 5:
                return "§a";
            case 6:
                return "§d";
            case 7:
            case 12:
                return "§8";
            case 8:
                return "§7";
            case 9:
                return "§3";
            case 11:
                return "§9";
            case 13:
                return "§2";
            case 14:
                return "§c";
            default:
                return "§0";
        }
    }

}
