package ubiquitaku.moneytree;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemMake {
    public ItemStack make(String mate, String name, List<String> lore,int ammo) {
        ItemStack item = new ItemStack(Material.getMaterial(mate));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        item.setAmount(ammo);
        return item;
    }
}
