package fr.alsatia;

import fr.alsatia.models.GameModel;
import fr.alsatia.models.PlayerModel;
import fr.alsatia.models.ServerModel;
import fr.alsatia.services.PlayerService;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getWorld;

public class Game {
    private Player p1;
    private Player p2;
    private final PlayerService playerService;
    private BukkitTask task;
    private ServerModel sm;
    private GameModel gm1, gm2;
    private final Main main;

    public Game(Main main){
        this.main = main;
        playerService = new PlayerService();
        waiting();
    }

    public void waiting(){
        main.setState(STATE.WAITING);
    }

    public void start(){
        World world = getWorld("world");
        assert world != null;
        world.setClearWeatherDuration(99999);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, true);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
        world.setTime(6000);
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        if(main.isState(STATE.WAITING)){
            Objects.requireNonNull(getWorld("world")).setGameRule(GameRule.KEEP_INVENTORY, true);
            task = new BukkitRunnable() {
                int counter = 5;
                @Override
                public void run() {
                    Bukkit.broadcastMessage(ChatColor.BLUE + "PRACTICE " + ChatColor.GRAY + ">> " + ChatColor.WHITE + counter);
                    counter --;
                    if (counter == 0){
                        main.setState(STATE.RUNNING);
                        Bukkit.broadcastMessage(ChatColor.BLUE + "PRACTICE " + ChatColor.GRAY + ">> " + ChatColor.WHITE + "C'est parti !");
                        Bukkit.getOnlinePlayers().forEach(Stuff::new);
                        cancel();
                    }
                }
            }.runTaskTimer(main, 100, 20);
        }
    }

    public void endMessage(Player p1, GameModel gm1, Player p2, GameModel gm2){
        Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "==================================");
        Bukkit.getServer().broadcastMessage(ChatColor.WHITE + "          " + ChatColor.MAGIC + "#" + ChatColor.AQUA + ChatColor.BOLD +" Fin de la partie " + ChatColor.RESET + ChatColor.MAGIC + "#");
        Bukkit.getServer().broadcastMessage("");
        Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "  ⚑ " + ChatColor.WHITE + ChatColor.BOLD + p1.getDisplayName() + ChatColor.RESET + " remporte le combat !");
        Bukkit.getServer().broadcastMessage("");
        if(sm.getRanked()){
            Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "  ▶ " + ChatColor.GRAY + "[" + (gm1.getElo() - 10) + " + 10] " + ChatColor.BOLD
                    + playerService.getEloString(gm1.getElo()) + ChatColor.WHITE + " " + p1.getDisplayName());
            Bukkit.getServer().broadcastMessage( ChatColor.DARK_RED + "  ▶ " + ChatColor.GRAY + "[" + (gm2.getElo() + 5) + " - 5] " + ChatColor.BOLD
                    + playerService.getEloString(gm2.getElo()) + ChatColor.WHITE + " " + p2.getDisplayName());
        }
        Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "==================================");
    }

    public void endDrawMessage(Player p1, GameModel gm1, Player p2, GameModel gm2){
        Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "==================================");
        Bukkit.getServer().broadcastMessage(ChatColor.WHITE + "          " + ChatColor.MAGIC + "#" + ChatColor.AQUA + ChatColor.BOLD +" Fin de la partie " + ChatColor.RESET + ChatColor.MAGIC + "#");
        Bukkit.getServer().broadcastMessage("");
        Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "  ⚑ " + ChatColor.WHITE + "Personne ne remporte le combat !");
        Bukkit.getServer().broadcastMessage("");
        if(sm.getRanked()){
            Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "  ▶ " + ChatColor.GRAY + "[" + gm1.getElo() + " + 0] " + ChatColor.BOLD
                    + playerService.getEloString(gm1.getElo()) + ChatColor.WHITE + " " + p1.getDisplayName());
            Bukkit.getServer().broadcastMessage( ChatColor.DARK_RED + "  ▶ " + ChatColor.GRAY + "[" + (gm2.getElo() + 2) + " - 2] " + ChatColor.BOLD
                    + playerService.getEloString(gm2.getElo()) + ChatColor.WHITE + " " + p2.getDisplayName());
        }

        Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "==================================");
    }

    public void endRagequitMessage(Player p1, GameModel gm1, Player p2, GameModel gm2){
        Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "==================================");
        Bukkit.getServer().broadcastMessage(ChatColor.WHITE + "          " + ChatColor.MAGIC + "#" + ChatColor.AQUA + ChatColor.BOLD +" Fin de la partie " + ChatColor.RESET + ChatColor.MAGIC + "#");
        Bukkit.getServer().broadcastMessage("");
        Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "  ⚑ " + ChatColor.WHITE + ChatColor.BOLD + p1.getDisplayName() + ChatColor.RESET + " remporte le combat par forfait !");
        Bukkit.getServer().broadcastMessage("");
        if(sm.getRanked()){
            Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "  ▶ " + ChatColor.GRAY + "[" + gm1.getElo() + " + 0] " + ChatColor.BOLD
                    + playerService.getEloString(gm1.getElo()) + ChatColor.WHITE + " " + p1.getDisplayName());
            Bukkit.getServer().broadcastMessage( ChatColor.DARK_RED + "  ▶ " + ChatColor.GRAY + "[" + gm2.getElo() + " + 0] " + ChatColor.BOLD
                    + playerService.getEloString(gm2.getElo()) + ChatColor.WHITE + " " + p2.getDisplayName());
        }

        Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "==================================");
    }

    private void changeServer(String serverName, Player p) {
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(main, "BungeeCord");
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeUTF("Connect");
            dataOutputStream.writeUTF(serverName);
        } catch (Exception e) {
            getLogger().severe("[ALSATIA] Error : " + e.getMessage());
        }

        p.sendPluginMessage(main, "BungeeCord", byteArrayOutputStream.toByteArray());


        try {
            dataOutputStream.close();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            getLogger().severe("[HUB] Error : " + e.getMessage());
        }

    }

    public void stop(Player winner, Player loser, boolean ragequit, boolean draw){
        sm = playerService.getServerModel(loser);
        gm1 = playerService.getPlayerGame(winner, sm.getMode(), sm.getRanked());
        gm2 = playerService.getPlayerGame(loser, sm.getMode(), sm.getRanked());

        if(sm != null && sm.getRanked()){
            if(main.isState(STATE.RUNNING)){
                main.setState(STATE.STOP);
                if(ragequit){
                    if(UUID.fromString(gm1.getPlayerUuid()) == loser.getUniqueId()){
                        removeElo(gm1, 2);
                        endRagequitMessage(loser, gm2, winner, gm1);
                    }else{
                        removeElo(gm2, 2);
                        endRagequitMessage(winner, gm2, loser, gm1);
                    }
                }else if(draw){
                    if(UUID.fromString(gm1.getPlayerUuid()) == winner.getUniqueId()){
                        endDrawMessage(winner, gm1, loser, gm2);
                    }else{
                        endDrawMessage(winner, gm2, loser, gm1);
                    }
                }else{
                    addElo(gm1, 10);
                    removeElo(gm2, 5);
                    endMessage(winner, gm1, loser, gm2);
                }
            }
        } else if (sm != null && !sm.getRanked()) { // Unranked
            if(main.isState(STATE.RUNNING)){
                main.setState(STATE.STOP);
                if(ragequit){
                    if(loser.getUniqueId() == UUID.fromString(gm1.getPlayerUuid())){
                        endRagequitMessage(loser, gm2, winner, gm1);
                    }else{
                        endRagequitMessage(winner, gm2, loser, gm1);
                    }
                }else if(draw){
                    if(UUID.fromString(gm1.getPlayerUuid()) == winner.getUniqueId()){
                        endDrawMessage(winner, gm1, loser, gm2);
                    }else{
                        endDrawMessage(winner, gm2, loser, gm1);
                    }
                }else{
                    if(UUID.fromString(gm1.getPlayerUuid()) == winner.getUniqueId()){
                        endMessage(winner, gm1, loser, gm2);
                    }else{
                        endMessage(winner, gm2, loser, gm1);
                    }

                }

            }
        }

        loser.spigot().respawn();
        loser.setGameMode(GameMode.SPECTATOR);
        playerService.deleteServer(sm.getId());

        new BukkitRunnable() {
            int counter = 10;
            @Override
            public void run() {
                if(counter == 5) {
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        changeServer("lobby", player);
                    });
                }
                if(counter == 0){
                    Bukkit.getServer().shutdown();
                }
                counter--;
            }
        }.runTaskTimer(main, 200L, 20);

    }

    public void addElo(GameModel gm, int elo){
        gm.setElo(gm.getElo() + elo);
        gm.setWon(gm.getWon() + 1);
        gm.setPlayed(gm.getPlayed() + 1);
        playerService.updatePlayerGame(gm);
    }

    public void removeElo(GameModel gm, int elo){
        gm.setElo(gm.getElo() - elo);
        gm.setLoss(gm.getLoss() + 1);
        gm.setPlayed(gm.getPlayed() + 1);
        playerService.updatePlayerGame(gm);
    }

    public Player getP1() {
        return p1;
    }

    public void setP1(Player p1) {
        this.p1 = p1;
        PlayerModel pm1 = playerService.getPlayer(p1);
    }

    public Player getP2() {
        return p2;
    }

    public void setP2(Player p2) {
        this.p2 = p2;
        PlayerModel pm2 = playerService.getPlayer(p2);
    }

    public BukkitTask getTask(){
        return task;
    }

}
