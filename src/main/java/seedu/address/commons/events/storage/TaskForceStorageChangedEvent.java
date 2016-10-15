package seedu.address.commons.events.storage;

import seedu.address.commons.core.Config;
import seedu.address.commons.events.BaseEvent;
import seedu.address.storage.Storage;

public class TaskForceStorageChangedEvent extends BaseEvent {
	
	public Storage storageManager;
	public Config newConfigFile;

	public TaskForceStorageChangedEvent(Storage storageManager, Config newConfigFile) {
		this.storageManager = storageManager;
		this.newConfigFile = newConfigFile;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return newConfigFile.toString();
	}

}
