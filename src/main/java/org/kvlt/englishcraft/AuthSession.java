package org.kvlt.englishcraft;

public class AuthSession {

    private String ip;
    private long expirationTime;

    public AuthSession(String ip, long duration) {
        this.ip = ip;
        this.expirationTime = duration;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

}
