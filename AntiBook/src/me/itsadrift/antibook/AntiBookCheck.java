package me.itsadrift.antibook;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class AntiBookCheck extends BukkitRunnable {

    private AntiBook main;
    private Player player = null;
    public AntiBookCheck(AntiBook main) {
        this.main = main;
    }

    public AntiBookCheck(AntiBook main, Player player) {
        this.main = main;
        this.player = player;
    }

    @Override
    public void run() {
        if (player != null) {
            runCheck(player);
        } else if (Bukkit.getOnlinePlayers() != null) {
            for (Player a : Bukkit.getOnlinePlayers()) {
                runCheck(a);
            }
        }
    }

    private void runCheck(Player player) {
        if (player.hasPermission("antibook.bypass"))
            return;
        if (player.getInventory().getItemInMainHand() != null) {
            ItemStack newItem = player.getInventory().getItemInMainHand();
            if (newItem.getType() != Material.WRITABLE_BOOK)
                return;

            boolean changedSlot = false;
            for (int i = 0; i < 9; i++) {
                ItemStack a = player.getInventory().getItem(i);

                if (a == null || (a.getType() != Material.WRITABLE_BOOK)) {

                    changedSlot = true;
                    player.getInventory().setHeldItemSlot(i);

                    main.sendMessage(player, colour(main.prefix + main.noHold));

                    break;
                }

            }

            if (changedSlot)
                return;

            player.getWorld().dropItem(player.getEyeLocation().add(player.getFacing().getDirection()), newItem);
            newItem.setAmount(0);

            main.sendMessage(player, colour(main.prefix + main.slotsFull));

        }
    }

    private String colour(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
