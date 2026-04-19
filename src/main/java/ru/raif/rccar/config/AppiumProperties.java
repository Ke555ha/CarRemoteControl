package ru.raif.rccar.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "appium")
public class AppiumProperties {

    private String url;
    private String udid;
    private boolean noReset = true;
    private int newCommandTimeoutSeconds = 120;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public boolean isNoReset() {
        return noReset;
    }

    public void setNoReset(boolean noReset) {
        this.noReset = noReset;
    }

    public int getNewCommandTimeoutSeconds() {
        return newCommandTimeoutSeconds;
    }

    public void setNewCommandTimeoutSeconds(int newCommandTimeoutSeconds) {
        this.newCommandTimeoutSeconds = newCommandTimeoutSeconds;
    }
}
