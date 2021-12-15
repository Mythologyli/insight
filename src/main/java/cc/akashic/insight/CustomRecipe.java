package cc.akashic.insight;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.Collection;

public final class CustomRecipe {
    private static final Collection<NamespacedKey> recipes = new ArrayList<>();

    public static void addRecipe() {
        ItemStack item = new ItemStack(Material.BUNDLE);
        NamespacedKey key = new NamespacedKey(Insight.instance, "bundle");
        recipes.add(key);

        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("SRS", "R R", "RRR");
        recipe.setIngredient('S', Material.STRING);
        recipe.setIngredient('R', Material.RABBIT_HIDE);

        Bukkit.addRecipe(recipe);
    }

    public static final class EventListener implements Listener {
        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
            event.getPlayer().discoverRecipes(recipes);
        }
    }
}
