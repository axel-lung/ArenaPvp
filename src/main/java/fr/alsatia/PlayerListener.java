package fr.alsatia;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private final Main main;
    private boolean allPlayer = false;
    private final Game game;
    private final World world;

    private static Location LOCATION_P1;
    private static Location LOCATION_P2;

    public PlayerListener(Main main) {
        this.main = main;
        this.game = new Game(main);
        this.world = Bukkit.getWorld("world");
        LOCATION_P1 = new Location(world, -14.5, 79, 18.5, -90, 0);
        LOCATION_P2 = new Location(world, 41.5, 79, 18.5, 90, 0);
    }
    @EventHandler
    public void onJoinEvent(PlayerJoinEvent event){
        Player p = event.getPlayer();
        p.setGameMode(GameMode.SURVIVAL);
        if(!allPlayer){
            p.teleport(LOCATION_P1);
            game.setP1(p);
            allPlayer = true;
        }else {
            p.teleport(LOCATION_P2);
            game.setP2(p);
            game.start();
        }
    }
    @EventHandler
    public void onQuitEvent(PlayerQuitEvent event){
        Player p1 = event.getPlayer();
        if(main.isState(STATE.WAITING) || main.isState(STATE.RUNNING)){
            main.setState(STATE.RUNNING);
            if(p1.getUniqueId() == game.getP1().getUniqueId()){
                game.stop(game.getP2(), p1, true, false);
            }else {
                game.stop(p1, game.getP2(), true, false);
            }
            game.getTask().cancel();
        }
        if(!main.isState(STATE.STOP)) Bukkit.getServer().broadcastMessage(ChatColor.BLUE + "PRACTICE " + ChatColor.GRAY + ">> " + ChatColor.WHITE + "L'adversaire s'est déconnecté ou a abandonné !");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // Vérifiez si vous devez empêcher le joueur de marcher
        if (main.isState(STATE.WAITING)) {
            event.setCancelled(true);
        } else {
            Player player = event.getPlayer();
            if (player.getFallDistance() > 5.9) {
                player.setFallDistance(0);
                player.teleport(new Location(player.getWorld(), 0.5, 66.8, 0.5, 0, 0));
            }
        }
    }

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent event) {
        if(!event.getBlock().getType().equals(Material.WHITE_WOOL)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.getHealth() - event.getFinalDamage() <= 0) {
                // Le joueur est en train de mourir
                if (player == game.getP1()) {
                    game.stop(game.getP2(), game.getP1(), false, false);
                } else if (player == game.getP2()) {
                    game.stop(game.getP1(), game.getP2(), false, false);
                } else {
                    game.stop(game.getP2(), game.getP1(), false, true);
                }
                player.teleport(LOCATION_P1);
                player.setHealth(20);
                player.setGameMode(GameMode.SPECTATOR);
            }

        }
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }
}
