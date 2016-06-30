package lowbrain.hungerplusplus;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main
  extends JavaPlugin
{
  ServerPlayerListener PlayerListener = new ServerPlayerListener(this);
  FileConfiguration config;
  
  public void onEnable()
  {
    this.config = this.getConfig();
    this.saveDefaultConfig();
    getServer().getPluginManager().registerEvents(this.PlayerListener, this);
    this.getLogger().info("[HungerPlusPlus] " + getDescription().getVersion() + " enabled!");
  }
  
  public void onDisable()
  {
    this.config = null;
  }
}
