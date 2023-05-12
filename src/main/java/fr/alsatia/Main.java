package fr.alsatia;

import fr.alsatia.utils.RegenMap;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    public static Main INSTANCE;


    private STATE state;

    @Override
    public void onEnable() {
        getLogger().info("[ALSATIA] Arena PVP enabled");
        INSTANCE = this;
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(INSTANCE), INSTANCE);

        RegenMap rm = new RegenMap();
        World delete = Bukkit.getWorld("world");
        assert delete != null;
        File deleteFolder = delete.getWorldFolder();
        rm.unloadWorld(delete);
        System.out.println("unload");
        rm.deleteWorld(deleteFolder);
        System.out.println("delete");
        // The world to copy
        //World source = Bukkit.getWorld("fata1");
        //File sourceFolder = source.getWorldFolder();
        File sourceFolder = new File("worlds/arena4");

        // The world to overwrite when copying
        World target = Bukkit.getWorld("world");
        assert target != null;
        File targetFolder = target.getWorldFolder();

        rm.copyWorld(sourceFolder, targetFolder);
        System.out.println("copy");




    }

    @Override
    public void onDisable() {
        getLogger().info("[ALSATIA] Arena PVP disabled");
    }

    public boolean isState(STATE state) {
        return this.state == state;
    }

    public void setState(STATE state) {
        this.state = state;
    }
}