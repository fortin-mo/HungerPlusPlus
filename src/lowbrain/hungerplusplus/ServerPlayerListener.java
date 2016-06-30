package lowbrain.hungerplusplus;

import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ServerPlayerListener
  implements Listener
{
  public final Logger log = Logger.getLogger("Minecraft");
  public static Main plugin;
  
  public ServerPlayerListener(Main instance)
  {
    plugin = instance;
  }
  
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e)
  {
    float hunger_config_setting = 0.0F;
    float realistic_metabolism_setting = 0.0F;
    
    final Player p = e.getPlayer();
    float new_exhaust_start_level = 0.0F;
    
    hunger_config_setting = (float)plugin.config.getDouble("Hunger.hunger_rate");
    realistic_metabolism_setting = (float)plugin.config.getDouble("Hunger.basal_metabolic_rate");
    final int debug_mode = plugin.config.getInt("Hunger.debug_mode");
    
    final float final_hunger_config_setting = hunger_config_setting;
    if (hunger_config_setting == 0.0F) {
      hunger_config_setting = 1.0F;
    }
    if (hunger_config_setting > 0.0F) {
      new_exhaust_start_level = 4.0F / hunger_config_setting * (hunger_config_setting - 1.0F);
    }
    if (hunger_config_setting < 0.0F) {
      new_exhaust_start_level = hunger_config_setting * 4.0F - 1.0F;
    }
    float final_realistic_metabolism_setting;
    if (realistic_metabolism_setting != 0.0F) {
      final_realistic_metabolism_setting = 0.0041666F / realistic_metabolism_setting / hunger_config_setting;
    } else {
      final_realistic_metabolism_setting = 0.0F;
    }
    final float final_new_exhaust_start_level = new_exhaust_start_level;
    
    p.setExhaustion(new_exhaust_start_level);
    if (debug_mode == 1) {
      this.log.info("new_exhaust_start_level is now " + final_new_exhaust_start_level);
    }
    plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new Runnable()
    {
      public void run()
      {
        if (debug_mode == 1)
        {
          float debug_exhaustion = p.getExhaustion();
          System.out.println("new_exhaust_start_level: " + final_new_exhaust_start_level + " current exhaust: " + debug_exhaustion);
        }
      }
    }, 60L, 20L);
    
    plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
    {
      public void run()
      {
        float current_exhaustion = p.getExhaustion();
        if (final_hunger_config_setting < 0.0F)
        {
          if (((current_exhaustion > -1.0F ? 1 : 0) & (current_exhaustion < 0.0F ? 1 : 0)) != 0) {
            p.setExhaustion(4.0F);
          }
          if (((current_exhaustion > 0.0F ? 1 : 0) & (current_exhaustion < 4.0F ? 1 : 0)) != 0) {
            p.setExhaustion(final_new_exhaust_start_level);
          }
        }
        if (final_hunger_config_setting > 0.0F) {
          if (current_exhaustion <= final_new_exhaust_start_level) {
            p.setExhaustion(final_new_exhaust_start_level);
          }
        }
        current_exhaustion = p.getExhaustion();
        if (final_realistic_metabolism_setting != 0.0F)
        {
          if (final_hunger_config_setting < 0.0F) {
            p.setExhaustion(current_exhaustion - final_realistic_metabolism_setting);
          }
          if (final_hunger_config_setting > 0.0F) {
            p.setExhaustion(current_exhaustion + final_realistic_metabolism_setting);
          }
        }
      }
    }, 0L, 1L);
  }
}
