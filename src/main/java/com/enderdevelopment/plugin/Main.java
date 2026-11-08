package com.enderdevelopment.plugin;

import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public final class Main extends JavaPlugin implements Listener {
    private NamespacedKey bladeKey;
    private NamespacedKey recipeKey;

    @Override
    public void onEnable() {
        bladeKey = new NamespacedKey(this, "void_blade");
        recipeKey = new NamespacedKey(this, "void_blade_recipe");
        Bukkit.getPluginManager().registerEvents(this, this);
        registerBladeRecipe();
        getLogger().info("LoveeeCraft End Update enabled.");
    }

    @Override
    public void onDisable() {
        Bukkit.removeRecipe(recipeKey);
    }

    private ItemStack createVoidBlade() {
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        meta.displayName(Component.text("Void Blade", NamedTextColor.LIGHT_PURPLE));
        meta.lore(java.util.List.of(
                Component.text("Forged for the outer darkness.", NamedTextColor.GRAY),
                Component.text("Deals extra damage to End creatures.", NamedTextColor.DARK_PURPLE)));
        meta.getPersistentDataContainer().set(bladeKey, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    private void registerBladeRecipe() {
        Bukkit.removeRecipe(recipeKey);
        ShapedRecipe recipe = new ShapedRecipe(recipeKey, createVoidBlade());
        recipe.shape(" E ", "ENE", " D ");
        recipe.setIngredient('E', Material.ENDER_EYE);
        recipe.setIngredient('N', Material.NETHERITE_SWORD);
        recipe.setIngredient('D', Material.DRAGON_BREATH);
        Bukkit.addRecipe(recipe);
    }

    private boolean isVoidBlade(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        Byte marker = item.getItemMeta().getPersistentDataContainer()
                .get(bladeKey, PersistentDataType.BYTE);
        return marker != null && marker == (byte) 1;
    }

    @EventHandler
    public void onDragonSpawn(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof EnderDragon dragon)) {
            return;
        }
        Bukkit.getScheduler().runTask(this, () -> {
            if (!dragon.isValid() || dragon.isDead()) {
                return;
            }
            var maxHealth = dragon.getAttribute(Attribute.MAX_HEALTH);
            if (maxHealth != null) {
                maxHealth.setBaseValue(400.0);
                dragon.setHealth(400.0);
            }
            dragon.customName(Component.text("The Ender Dragon", NamedTextColor.DARK_PURPLE));
            dragon.setCustomNameVisible(true);
        });
    }

    @EventHandler
    public void onEndermanDeath(EntityDeathEvent event) {
        if (event.getEntityType() != EntityType.ENDERMAN
                || event.getEntity().getWorld().getEnvironment() != org.bukkit.World.Environment.THE_END) {
            return;
        }
        if (Math.random() < 0.20) {
            event.getDrops().add(new ItemStack(Material.ECHO_SHARD));
        }
    }

    @EventHandler
    public void onVoidBladeHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player) || !isVoidBlade(player.getInventory().getItemInMainHand())) {
            return;
        }
        Entity target = event.getEntity();
        if (target.getWorld().getEnvironment() == org.bukkit.World.Environment.THE_END
                || target instanceof EnderDragon) {
            event.setDamage(event.getDamage() + 5.0);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("loveeeend")) {
            return false;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("giveblade")) {
            if (!sender.hasPermission("loveeeend.giveblade")) {
                sender.sendMessage(Component.text("You do not have permission to use this command.", NamedTextColor.RED));
                return true;
            }
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Component.text("This command can only be used by a player.", NamedTextColor.RED));
                return true;
            }
            player.getInventory().addItem(createVoidBlade()).values()
                    .forEach(item -> player.getWorld().dropItemNaturally(player.getLocation(), item));
            player.sendMessage(Component.text("The Void Blade is yours.", NamedTextColor.LIGHT_PURPLE));
            return true;
        }
        sender.sendMessage(Component.text("LoveeeCraft End Update: craft the Void Blade or use /loveeeend giveblade.", NamedTextColor.LIGHT_PURPLE));
        return true;
    }
}
