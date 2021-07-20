package ubiquitaku.moneytree;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public final class MoneyTree extends JavaPlugin {
    //for spigot1.12.2
    FileConfiguration config;
    ItemMake im = new ItemMake();
    Random random = new Random();
    String prefix;
    boolean use;
    List<String> list;
    ItemStack item;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        configLoad();
        run();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("mtree")) {
            if (!sender.hasPermission("mtree.op")) {
                sender.sendMessage("§c§lあなたはこのコマンドを実行する権限を持っていません");
                return true;
            }
            if (args.length == 0) {
                //説明を省略
                return true;
            }
            if (args[0].equals("reload")) {
                configLoad();
                sender.sendMessage("リロード完了");
                return true;
            }
            if (args[0].equals("on")) {
                if (use) {
                    sender.sendMessage("既にonです");
                    return true;
                }
                use = true;
                config.set("use",true);
                saveConfig();
                sender.sendMessage("onにしました");
                return true;
            }
            if (args[0].equals("off")) {
                if (!use) {
                    sender.sendMessage("既にoffです");
                    return true;
                }
                use = false;
                config.set("use",false);
                saveConfig();
                sender.sendMessage("offにしました");
                return true;
            }
        }
        return true;
    }

    public void configLoad() {
        config = getConfig();
        prefix = config.getString("prefix");
        use = config.getBoolean("use");
        list = config.getStringList("Area");
        //アイテムの情報を作る
        item = im.make(config.getString("Item.Material"),config.getString("Item.Name"),config.getStringList("Item.Lore"),1);
    }

    public void run() {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                //pluginを停止していない場合のみ動く
                if (use) {
                    //configLoadで作成したエリアのリストを順に取り出す
                    for (String s : list) {
                        Bukkit.broadcastMessage(s);
                        String[] str = s.split("/");
                        //確率部分の計算
                        if (Integer.parseInt(str[7]) <= random.nextInt(100)) {
                            Bukkit.broadcastMessage("continue");
                            continue;
                        }
                        //範囲内のランダムな位置を設定(ブロック単位)
                        int x,y,z;
                        int x1 = Integer.parseInt(str[1]),x2 = Integer.parseInt(str[4]),y1 = Integer.parseInt(str[2]),y2 = Integer.parseInt(str[5]),z1 = Integer.parseInt(str[3]),z2 = Integer.parseInt(str[6]);
                        if (x1 > x2) {
                            x = x2+random.nextInt(x1 - x2);
                        } else if (x1 < x2) {
                            x = x1+random.nextInt(x2 - x1);
                        } else {
                            x = x1;
                        }
                        if (y1 > y2) {
                            y = y2+random.nextInt(y1 - y2);
                        } else if (y1 < y2) {
                            y = y1+random.nextInt(y2 - y1);
                        } else {
                            y = y1;
                        }
                        if (z1 > z2) {
                            z = z2+random.nextInt(z1 - z2);
                        } else if (z1 < z2) {
                            z = y1+random.nextInt(z2 - z1);
                        } else {
                            z = z1;
                        }
                        Location location = new Location(Bukkit.getWorld(str[0]),x,y,z,1,1);
                        //itemをlocationに落とす
                        Bukkit.getWorld(str[0]).dropItem(location,item);
                        Bukkit.broadcastMessage(x+" "+y+" "+z);
                        Bukkit.broadcastMessage(item.getItemMeta().getDisplayName());
                        //プレイヤーから取ったlocationにはちゃんと反応するくせに同じような書き方してる上のやつは動かない｡ﾟ(ﾟ´ω`ﾟ)ﾟ｡
                        for (Player p:Bukkit.getOnlinePlayers()) {
                            Bukkit.getWorld("world").dropItem(p.getLocation(),item);
                            Bukkit.broadcastMessage(String.valueOf(p.getLocation().getDirection()));
                        }
                    }
                }
            }
        };
        runnable.runTaskTimer(this,40,40);
    }
}
