package me.itsadrift.antibook;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AntiBook extends JavaPlugin {

    public String prefix = "";
    public String noHold = "";
    public String slotsFull = "";
    public String noPickup = "";
    public String inventory = "";

    // Set Responses
    public String invalidArguments = "&cInvalid Arguments";
    public String noPermission = "&cNo Permission";
    public String configReloaded = "Config Reloaded";

    // Autocheck
    public boolean enabled = false;
    public int interval = 0;

    public BukkitTask antiBookCheck = null;

    private List<UUID> msgCooldown = new ArrayList<>();

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        setValues();

        getCommand("antibook").setExecutor(new AntiBookCommand(this));
        Bukkit.getPluginManager().registerEvents(new AntiBookListener(this), this);

        startAntiBookCheck();

        System.out.println("AntiBook by ItsAdrift Loaded! \nAntiBook Settings:\nAutoCheck Enabled: " + enabled + "\nAutoCheck Interval: " + interval);

    }
    @Override
    public void onDisable() {
        msgCooldown.clear();
    }

    public void reloadData() {
        reloadConfig();
        saveConfig();

        setValues();

        if (antiBookCheck != null) {
            antiBookCheck.cancel();
        }

        startAntiBookCheck();

    }

    public void startAntiBookCheck() {
        if (!enabled)
            return;
        antiBookCheck = new AntiBookCheck(this).runTaskTimer(this, interval, interval);
    }

    public void setValues() {
        this.prefix = getConfig().getString("messages.prefix");
        this.noHold = getConfig().getString("messages.noHold");
        this.slotsFull = getConfig().getString("messages.slotsFull");
        this.noPickup = getConfig().getString("messages.noPickup");
        this.inventory = getConfig().getString("messages.inventory");

        this.enabled = getConfig().getBoolean("autocheck.enabled");
        this.interval = getConfig().getInt("autocheck.interval") * 20;
    }

    public void msgCooldown(UUID uuid) {
        msgCooldown.add(uuid);
        Bukkit.getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                msgCooldown.remove(uuid);
            }
        }, 10);
    }

    public boolean isOnMSGCooldown(UUID uuid) {
        return msgCooldown.contains(uuid);
    }

    public void sendMessage(Player player, String message) {
        UUID uuid = player.getUniqueId();
        if (!isOnMSGCooldown(uuid)) {
            player.sendMessage(message);
            msgCooldown(uuid);
        }
    }

    private String colour(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
