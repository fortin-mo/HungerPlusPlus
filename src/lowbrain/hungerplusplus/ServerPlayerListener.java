package lowbrain.hungerplusplus;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;


public class ServerPlayerListener
implements Listener {
    public static Main plugin;
    public static List<TaskContainer> lstTask = new ArrayList<TaskContainer>();

    public ServerPlayerListener(Main instance) {
        plugin = instance;
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
    	final int debug_mode = ServerPlayerListener.plugin.config.getInt("Hunger.debug_mode");
    	
    	for (int i = 0; i < lstTask.size(); i++) {
			if(lstTask.get(i).getPlayerUUID().compareTo(e.getPlayer().getUniqueId()) == 0){
				plugin.getServer().getScheduler().cancelTask(lstTask.get(i).getTaskID());
				if(debug_mode == 1){
					plugin.getLogger().info(e.getPlayer().getName() + "disconnected, task now cancelled !");
				}
		        break;
			}
		}
    	
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        float hunger_config_setting = 0.0f;
        float realistic_metabolism_setting = 0.0f;
        final Player p = e.getPlayer();
        float new_exhaust_start_level = 0.0f;
        hunger_config_setting = (float)ServerPlayerListener.plugin.config.getDouble("Hunger.hunger_rate");
        realistic_metabolism_setting = (float)ServerPlayerListener.plugin.config.getDouble("Hunger.basal_metabolic_rate");
        final int debug_mode = ServerPlayerListener.plugin.config.getInt("Hunger.debug_mode");
        final float final_hunger_config_setting = hunger_config_setting;
        if (hunger_config_setting == 0.0f) {
            hunger_config_setting = 1.0f;
        }
        if (hunger_config_setting > 0.0f) {
            new_exhaust_start_level = 4.0f / hunger_config_setting * (hunger_config_setting - 1.0f);
        }
        if (hunger_config_setting < 0.0f) {
            new_exhaust_start_level = hunger_config_setting * 4.0f - 1.0f;
        }
        final float final_realistic_metabolism_setting = realistic_metabolism_setting != 0.0f ? 0.0041666f / realistic_metabolism_setting / hunger_config_setting : 0.0f;
        final float final_new_exhaust_start_level = new_exhaust_start_level;
        p.setExhaustion(new_exhaust_start_level);
        if (debug_mode == 1) {
            plugin.getLogger().info("new_exhaust_start_level is now " + final_new_exhaust_start_level);
        }
        
        Task task = new Task(final_hunger_config_setting, final_new_exhaust_start_level, final_realistic_metabolism_setting, p);
        
        int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)plugin,task , 0, 1);
        lstTask.add(new TaskContainer(e.getPlayer().getUniqueId(), taskID));
    }

}

