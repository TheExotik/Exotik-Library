package exotik.exotiklibrary.Mechanics;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import exotik.exotiklibrary.ExotikLibrary;

public class Shorts {

    // Simple 'Plugin' prefix
    public static String prefix = "&7[&6Exotik Library&7] &f";

    // Simple 'Donation' link
    public static String donation = "https://bit.ly/Exotik-Donations";

    // Simple 'Discord' invite link
    public static String discord = "https://discord.gg/nJ83UkbYue";

    // Simple 'GitHub' link
    public static String github = "https://github.com/TheExotik";

    // Simple 'Space' line
    public static String space_line = " ";

    // Simple 'Break' line
    public static String break_line = "# -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";

    // Simple 'Break' line to console
    public static String console_end_text = "&6-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";

    // Simple way to return 'getInstance()'
    public static ExotikLibrary plugin = ExotikLibrary.getInstance();

    // Simple way to 'Register' a new 'Event'
    public static void event(Listener event) {
        Bukkit.getPluginManager().registerEvents(event, ExotikLibrary.getInstance());
    }
    
    // Simple format text colors (as 'String')
    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&',message);
    }
    
    // Simple format text colors (as 'String[]')
    public static String[] color(String[] message) {
        String[] formatted = new String[message.length];

        for (int i = 0; i < message.length; i++) {
            formatted[i] = ChatColor.translateAlternateColorCodes('&', message[i]);
        }

        return formatted;
    }

    // Simple format text colors (as 'List<String>')
    public static List<String> color(List<String> message) {
        List<String> formatted = new ArrayList<>();

        for (String line : message) {
            formatted.add(ChatColor.translateAlternateColorCodes('&', line));
        }

        return formatted;
    }

    // Format some number to 2 decimals (example: 5.2164843216 -> 5.22)
    public static String decimals(double value) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat format = new DecimalFormat("#.##", symbols);

        return format.format(value);
    }

    // Format some 'Double' number to 'Integer' number (example: 5.22 -> 5)
    public static Integer toInteger(double value) {
        return (int) Math.round(value);
    }
    
    // Format some 'String' number to 'Integer' number (example: "5" -> 5)
    public static Integer toInteger(String value) {
        double converted = Double.parseDouble(value);
        return (int) Math.round(converted);
    }

    // Send a 'Console' Message
    public static void console(String message) {
        Bukkit.getConsoleSender().sendMessage(color("&f" + message));
    }

    // Send a 'Information' message in 'Console'
    public static void logInfo(String message) {
        plugin.getLogger().info(color("&f" + message));
    }

    // Send a 'Warning' message in 'Console'
    public static void logWarning(String message) {
        plugin.getLogger().warning(color("&f" + message));
    }
    
    // Send a 'Error' message in 'Console'
    public static void logError(String message) {
        plugin.getLogger().log(Level.SEVERE,color("&f" + message));
    }

    // Send a 'Message' to 'Player'
    public static void reply(Player player, String message) {
        player.sendMessage(color("&f" + message));
    }

    // Return the 'Server' where the 'Plugin' is running
    public static Server server() {
        return Bukkit.getServer();
    }

    // Return the 'Version' of the 'Plugin'
    public static String version() {
        return plugin.getDescription().getVersion();
    }

    
}
