package lowbrain.hungerplusplus;

import org.bukkit.entity.Player;

public class Task implements Runnable{

	private float final_hunger_config_setting;
	private float final_new_exhaust_start_level;
	private float final_realistic_metabolism_setting;
	private Player player;

	
	public Task(float final_hunger_config_setting, float final_new_exhaust_start_level,
			float final_realistic_metabolism_setting, Player player) {
		this.final_hunger_config_setting = final_hunger_config_setting;
		this.final_new_exhaust_start_level = final_new_exhaust_start_level;
		this.final_realistic_metabolism_setting = final_realistic_metabolism_setting;
		this.player = player;
	}


	@Override
    public void run() {
        float current_exhaustion = player.getExhaustion();
        if (final_hunger_config_setting < 0.0f) {
            if (current_exhaustion > -1.0f & current_exhaustion < 0.0f) {
                player.setExhaustion(4.0f);
            }
            if (current_exhaustion > 0.0f & current_exhaustion < 4.0f) {
                player.setExhaustion(final_new_exhaust_start_level);
            }
        }
        if (final_hunger_config_setting > 0.0f && current_exhaustion <= final_new_exhaust_start_level) {
            player.setExhaustion(final_new_exhaust_start_level);
        }
        current_exhaustion = player.getExhaustion();
        if (final_realistic_metabolism_setting != 0.0f) {
            if (final_hunger_config_setting < 0.0f) {
                player.setExhaustion(current_exhaustion - final_realistic_metabolism_setting);
            }
            if (final_hunger_config_setting > 0.0f) {
                player.setExhaustion(current_exhaustion + final_realistic_metabolism_setting);
            }
        }
    }
	
}
