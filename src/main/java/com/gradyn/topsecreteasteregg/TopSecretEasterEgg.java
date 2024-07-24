package com.gradyn.topsecreteasteregg;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class TopSecretEasterEgg extends JavaPlugin {
    public static FileConfiguration config;
    public static TopSecretEasterEgg inst;

    @Override
    public void onEnable() {
        inst = this;
        this.saveDefaultConfig();
        config = this.getConfig();
        getServer().getPluginManager().registerEvents(new PlayerDeathEventHandler(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
