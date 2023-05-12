package fr.alsatia;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Stuff {
    private ItemStack helmet, chestplate, leggings, boots, sword, gapple, blocks, steaks;
    private ItemMeta helmetMeta, chestplateMeta, leggingsMeta, bootsMeta, swordMeta;

    public Stuff(Player p) {
        initializeIS();
        defineItemMeta();
        addStuff(p);
    }

    public void initializeIS(){
        helmet = new ItemStack(Material.DIAMOND_HELMET);
        chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        boots = new ItemStack(Material.DIAMOND_BOOTS);
        sword = new ItemStack(Material.IRON_SWORD);
        gapple = new ItemStack(Material.GOLDEN_APPLE);
        gapple.setAmount(1);
        blocks = new ItemStack(Material.WHITE_WOOL);
        blocks.setAmount(4);
        steaks = new ItemStack(Material.COOKED_BEEF);
        steaks.setAmount(8);

        helmetMeta = helmet.getItemMeta();
        chestplateMeta = chestplate.getItemMeta();
        leggingsMeta = leggings.getItemMeta();
        bootsMeta = boots.getItemMeta();
        swordMeta = sword.getItemMeta();
    }

    public void defineItemMeta(){
        helmetMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
        chestplateMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
        leggingsMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
        bootsMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
        swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 4, true);

        helmet.setItemMeta(helmetMeta);
        chestplate.setItemMeta(chestplateMeta);
        leggings.setItemMeta(leggingsMeta);
        boots.setItemMeta(bootsMeta);
        sword.setItemMeta(swordMeta);
    }

    public void addStuff(Player p){
        Inventory i = p.getInventory();
        i.clear();
        p.getInventory().setItem(EquipmentSlot.HEAD, helmet);
        p.getInventory().setItem(EquipmentSlot.CHEST, chestplate);
        p.getInventory().setItem(EquipmentSlot.LEGS, leggings);
        p.getInventory().setItem(EquipmentSlot.FEET, boots);
        i.setItem(0, sword);
        i.setItem(2, steaks);
        i.setItem(1, blocks);
        i.setItem(8, gapple);

    }

}
