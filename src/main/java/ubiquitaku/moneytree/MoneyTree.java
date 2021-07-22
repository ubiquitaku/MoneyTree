package ubiquitaku.moneytree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class MoneyTree extends JavaPlugin {
    //for spigot1.12.2
    private FileConfiguration config;
    private static Random random = new Random();
    private String prefix;
    private List<String> list;
    private ItemStack item;
    private int taskId = -1;
    private List<Area> areas;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            for(Area area : areas)
            	if (area.getChance()>random.nextDouble())
            		area.getWorld().dropItem(area.randomLocation(), item);
        }
    };

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        loadConfig();
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
            if (args[0].equalsIgnoreCase("reload")) {
                reloadConfig();
                loadConfig();
                sender.sendMessage(prefix+"リロード完了");

            } else  if (args[0].equalsIgnoreCase("on")) {
            	if (enable())
            		sender.sendMessage(prefix+"onにしました");
            	else
            		 sender.sendMessage(prefix+"既にonです");

            } else if (args[0].equalsIgnoreCase("off")) {
                if (disable())
                	sender.sendMessage(prefix+"offにしました");
                else
                	sender.sendMessage(prefix+"既にoffです");
            }
        }
        return true;
    }

    public void loadConfig() {
    	if (taskId != -1)
    		Bukkit.getScheduler().cancelTask(taskId);
        config = getConfig();
        prefix = config.getString("prefix");
        list = config.getStringList("Area");
        areas = new ArrayList<>();
        for(String tmp : list)
        	areas.add(new Area(tmp));
        if (config.getBoolean("use"))
        	taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, runnable, 0, 1);
        else
        	taskId = -1;
        //アイテムの情報を作る
        item = ItemMake.make(config.getString("Item.Material"),config.getString("Item.Name"),config.getStringList("Item.Lore"),1);
    }


    public boolean disable() {
    	if (taskId == -1) return false;
    	Bukkit.getScheduler().cancelTask(taskId);
        taskId = -1;
        config.set("use",false);
        saveConfig();
        return true;
    }


    public boolean enable() {
    	if (taskId != -1) return false;
    	taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, runnable, 0, 1);
        config.set("use",true);
        saveConfig();
        return true;
    }

}
