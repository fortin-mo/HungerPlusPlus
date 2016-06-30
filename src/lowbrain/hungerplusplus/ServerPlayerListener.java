package lowbrain.hungerplusplus;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class ServerPlayerListener
implements Listener {
    public static Main plugin;

    public ServerPlayerListener(Main instance) {
        plugin = instance;
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
        plugin.getServer().getScheduler().runTaskTimerAsynchronously((Plugin)plugin, new Runnable(){

            @Override
            public void run() {
                if (debug_mode == 1) {
                    float debug_exhaustion = p.getExhaustion();
                    System.out.println("new_exhaust_start_level: " + final_new_exhaust_start_level + " current exhaust: " + debug_exhaustion);
                }
            }
        }, 60, 20);
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)plugin, new Runnable(){

            @Override
            public void run() {
                float current_exhaustion = p.getExhaustion();
                if (final_hunger_config_setting < 0.0f) {
                    if (current_exhaustion > -1.0f & current_exhaustion < 0.0f) {
                        p.setExhaustion(4.0f);
                    }
                    if (current_exhaustion > 0.0f & current_exhaustion < 4.0f) {
                        p.setExhaustion(final_new_exhaust_start_level);
                    }
                }
                if (final_hunger_config_setting > 0.0f && current_exhaustion <= final_new_exhaust_start_level) {
                    p.setExhaustion(final_new_exhaust_start_level);
                }
                current_exhaustion = p.getExhaustion();
                if (final_realistic_metabolism_setting != 0.0f) {
                    if (final_hunger_config_setting < 0.0f) {
                        p.setExhaustion(current_exhaustion - final_realistic_metabolism_setting);
                    }
                    if (final_hunger_config_setting > 0.0f) {
                        p.setExhaustion(current_exhaustion + final_realistic_metabolism_setting);
                    }
                }
            }
        }, 0, 1);
    }

}

