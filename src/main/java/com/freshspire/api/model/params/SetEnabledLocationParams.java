package com.freshspire.api.model.params;

public class SetEnabledLocationParams {
    private boolean enabledLocation;

    public SetEnabledLocationParams() {}

    public SetEnabledLocationParams(boolean enabledLocation) {
        this.enabledLocation = enabledLocation;
    }

    public boolean getEnabledLocation() {
        return enabledLocation;
    }

    public void setEnabledLocation(boolean enabledLocation) {
        this.enabledLocation = enabledLocation;
    }
}
