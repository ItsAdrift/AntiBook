package me.itsadrift.antibook;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class AntiBookListener implements Listener {

    private AntiBook main;
    public AntiBookListener(AntiBook main) {
        this.main = main;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() instanceof PlayerInventory) {
            if (e.getCursor() != null && e.getCursor().getType() == Material.WRITABLE_BOOK) {
                int clicked = e.getSlot();
                int holding = e.getWhoClicked().getInventory().getHeldItemSlot();
                if (clicked == holding) {
                    e.setCancelled(true);
                    main.sendMessage((Player) e.getWhoClicked(), colour(main.prefix + main.inventory));
                } else {
                    new AntiBookCheck(main, (Player) e.getWhoClicked()).runTaskLater(main, 5);
                }
            }
        }
    }

    @EventHandler
    public void onSlotSwitch(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();
        if (!player.hasPermission("antibook.bypass")) {
            if (player.getInventory().getItem(e.getNewSlot()) != null) {
                ItemStack newItem = player.getInventory().getItem(e.getNewSlot());
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

    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            ItemStack newItem = e.getItem().getItemStack();

            if (player.hasPermission("antibook.bypass"))
                return;

            if (newItem.getType() != Material.WRITABLE_BOOK)
                return;

            int books = 0;
            for (int i = 0; i < 9; i++) {
                ItemStack a = player.getInventory().getItem(i);

                if (a != null && a.getType() == Material.WRITABLE_BOOK) {
                    books++;
                }
            }

            if (books >= 8) {
                e.setCancelled(true);
                main.sendMessage(player, colour(main.prefix + main.noPickup));
                return;
            }

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

        }
    }

    private String colour(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
