package lowbrain.hungerplusplus;

import java.util.UUID;

public class TaskContainer {
	private UUID playerUUID;
	private int taskID;
	
	public TaskContainer(UUID uuid, int taskID) {
		this.playerUUID = uuid;
		this.taskID =taskID;
	}

	public UUID getPlayerUUID() {
		return playerUUID;
	}

	public int getTaskID() {
		return taskID;
	}

}
