package com.github.bingoohuang.utils.redis;

public class RedisConfig {
    private String host = "127.0.0.1";
    private int port = 6379;
    private String password;
    private int database = 0;
    private int timeout = 2000;
    private int maxClients = 50;

    public RedisConfig(String host, int port, String password, int database, int timeout, int maxClients) {
        this.host = host;
        this.port = port;
        this.password = password;
        this.database = database;
        this.timeout = timeout;
        this.maxClients = maxClients;
    }

    public RedisConfig() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getMaxClients() {
        return maxClients;
    }

    public void setMaxClients(int maxClients) {
        this.maxClients = maxClients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RedisConfig that = (RedisConfig) o;

        if (database != that.database) return false;
        if (maxClients != that.maxClients) return false;
        if (port != that.port) return false;
        if (timeout != that.timeout) return false;
        if (host != null ? !host.equals(that.host) : that.host != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = host != null ? host.hashCode() : 0;
        result = 31 * result + port;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + database;
        result = 31 * result + timeout;
        result = 31 * result + maxClients;
        return result;
    }

    @Override
    public String toString() {
        return "RedisConfig{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", password='" + password + '\'' +
                ", database=" + database +
                ", timeout=" + timeout +
                ", maxClients=" + maxClients +
                '}';
    }
}
