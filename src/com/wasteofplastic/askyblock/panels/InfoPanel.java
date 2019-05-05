/*******************************************************************************
 * This file is part of ASkyBlock.
 *
 *     ASkyBlock is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     ASkyBlock is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with ASkyBlock.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/

package com.wasteofplastic.askyblock.panels;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Island;
import com.wasteofplastic.askyblock.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class InfoPanel implements Listener {
    // Island Guard Settings Panel
    private ASkyBlock plugin;
    private static final boolean DEBUG = false;


    public InfoPanel(ASkyBlock plugin) {
        this.plugin = plugin;
    }

    public Inventory infoPanel(Island island, HashMap<Material, Long> values) {
        List<IPItem> ip = new ArrayList<IPItem>();
        Inventory newPanel = null;

        if (island == null) {
            return null;
        } else {
            int slot = 0;
            for (Material mat : values.keySet()) {
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GREEN + "Blok Sayısı:");
                lore.add(ChatColor.WHITE + "" + NumberFormat.getNumberInstance(Locale.GERMAN).format(values.get(mat)));
                MaterialData generic = new MaterialData(mat);
                double levelDouble = Double.valueOf((values.get(mat) * Settings.blockValues.get(generic))) / Double.valueOf(Settings.levelCost);
                String level = NumberFormat.getNumberInstance(Locale.GERMAN).format(levelDouble);
                lore.add(ChatColor.GREEN + "Blok Seviyesi:");
                lore.add(ChatColor.WHITE + "" + level);
                if (mat.equals(Material.ENDER_PORTAL_FRAME)) slot = 0;
                else if (mat.equals(Material.SPONGE)) slot = 1;
                else if (mat.equals(Material.DIAMOND_BLOCK)) slot = 2;
                else if (mat.equals(Material.EMERALD_BLOCK)) slot = 3;
                else if (mat.equals(Material.LAPIS_BLOCK)) slot = 4;
                else if (mat.equals(Material.REDSTONE_BLOCK)) slot = 5;
                else if (mat.equals(Material.IRON_BLOCK)) slot = 6;
                else if (mat.equals(Material.GOLD_BLOCK)) slot = 7;
                else if (mat.equals(Material.SMOOTH_BRICK)) slot = 8;
                ip.add(new IPItem(mat, lore, slot));
            }
        }
        if (ip.size() > 0) {
            // Make sure size is a multiple of 9
            int size = 18;
            String title = "Ada Blok Bilgisi";
            newPanel = Bukkit.createInventory(null, size, title);
            for (IPItem i : ip) {
                newPanel.setItem(i.getSlot(), i.getItem());
            }
            ItemStack infoItem = new ItemStack(Material.BOOK);
            ItemMeta meta = infoItem.getItemMeta();

            meta.setDisplayName(ChatColor.RED + "BİLGİ");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GREEN + "Blok sayılarını güncellemek");
            lore.add(ChatColor.GREEN + "için /is level yazın.");
            meta.setLore(lore);
            infoItem.setItemMeta(meta);
            newPanel.setItem(13, infoItem);
        }
        return newPanel;
    }

    /**
     * Handle clicks to the Settings panel
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory(); // The inventory that was clicked in
        if (inventory.getName() == null) {
            return;
        }
        // The player that clicked the item
        final Player player = (Player) event.getWhoClicked();
        if (!inventory.getTitle().equalsIgnoreCase("Ada Blok Bilgisi")) {
            return;
        }
        event.setCancelled(true);
        if (event.getSlotType().equals(SlotType.OUTSIDE)) {
            player.closeInventory();
            return;
        }
        if (event.getClick().equals(ClickType.SHIFT_RIGHT)) {
            player.closeInventory();
            player.updateInventory();
            return;
        }
    }
}
