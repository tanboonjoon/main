package seedu.address.commons.events.storage;

import seedu.address.commons.core.Config;
import seedu.address.commons.events.BaseEvent;

public class TaskForceStorageChangedEvent extends BaseEvent {
	
	public Config newConfigFile;

	public TaskForceStorageChangedEvent(Config newConfigFile) {
		this.newConfigFile = newConfigFile;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return newConfigFile.toString();
	}

}
