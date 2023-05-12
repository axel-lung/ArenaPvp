package fr.alsatia.services;

import fr.alsatia.database.DatabaseManager;
import fr.alsatia.models.GameModel;
import fr.alsatia.models.ServerModel;
import fr.alsatia.models.WaitingModel;
import fr.alsatia.models.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerService {
    private Connection connection;

    public PlayerService() {
        this.connection = new DatabaseManager().connection();
    }

    public boolean isPlayer(Player p){
        this.connection = new DatabaseManager().connection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement("SELECT COUNT(*) FROM alsatia_players WHERE uuid=?");
            stmt.setString(1, p.getUniqueId().toString());
            rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return false;
    }

    public void addPlayer(Player p){
        this.connection = new DatabaseManager().connection();
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement("INSERT INTO alsatia_players (uuid, name, lastlogin) VALUES (?, ?, ?)");
            stmt.setString(1, p.getUniqueId().toString());
            stmt.setString(2, p.getDisplayName());
            stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public PlayerModel getPlayer(Player p){
        this.connection = new DatabaseManager().connection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement("SELECT * FROM alsatia_players WHERE uuid=?");
            stmt.setString(1, p.getUniqueId().toString());
            rs = stmt.executeQuery();
            if (rs.next()) {
                PlayerModel pm = new PlayerModel();
                pm.setUuid(p.getUniqueId());
                pm.setName(rs.getString(2));
                pm.setFirstLogin(rs.getTimestamp(3));
                pm.setLastLogin(rs.getTimestamp(4));
                pm.setVip(rs.getBoolean(5));
                pm.setMvp(rs.getBoolean(6));
                pm.setLegend(rs.getBoolean(7));
                return pm;
            }else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return null;
    }

    public List<GameModel> getPlayerGames(Player p){
        this.connection = new DatabaseManager().connection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement("SELECT * FROM alsatia_games WHERE player_uuid = ?");
            stmt.setString(1, p.getUniqueId().toString());
            rs = stmt.executeQuery();
            List<GameModel> gameModelList = new ArrayList<>();

            while (rs.next()) {
                GameModel gm = new GameModel();
                gm.setId(rs.getInt(1));
                gm.setMode(rs.getString(2));
                gm.setRanked(rs.getBoolean(3));
                gm.setElo(rs.getInt(4));
                gm.setPlayed(rs.getInt(5));
                gm.setLoss(rs.getInt(6));
                gm.setWon(rs.getInt(7));
                gameModelList.add(gm);
            }
            if(gameModelList.isEmpty()){
                return null;
            }else {
                return gameModelList;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return null;
    }

    public GameModel getPlayerGame(Player p, String mode, boolean ranked){
        this.connection = new DatabaseManager().connection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement("SELECT * FROM alsatia_games WHERE player_uuid = ? AND mode = ? AND ranked = ?");
            stmt.setString(1, p.getUniqueId().toString());
            stmt.setString(2, mode);
            stmt.setBoolean(3, ranked);
            rs = stmt.executeQuery();

            if (rs.next()) {
                GameModel gm = new GameModel();
                gm.setId(rs.getInt(1));
                gm.setMode(rs.getString(2));
                gm.setRanked(rs.getBoolean(3));
                gm.setElo(rs.getInt(4));
                gm.setPlayed(rs.getInt(5));
                gm.setLoss(rs.getInt(6));
                gm.setWon(rs.getInt(7));
                gm.setPlayerUuid(p.getUniqueId().toString());
                return gm;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return null;
    }

    public void addPlayerGame(GameModel gm){
        this.connection = new DatabaseManager().connection();
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement("INSERT INTO alsatia_games (mode, ranked, player_uuid) VALUES (?, ?, ?)");
            stmt.setString(1, gm.getMode());
            stmt.setBoolean(2, gm.isRanked());
            stmt.setString(3, gm.getPlayerUuid());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void updatePlayerGame(GameModel gm){
        this.connection = new DatabaseManager().connection();
        PreparedStatement stmt = null;
        System.out.println("update player game " + gm.toString());
        try {
            stmt = connection.prepareStatement("UPDATE alsatia_games SET mode = ?, ranked = ?, elo = ?, played = ?, loss = ?, won = ? WHERE player_uuid = ? AND mode = ? AND ranked = ?");
            stmt.setString(1, gm.getMode());
            stmt.setBoolean(2, gm.isRanked());
            stmt.setInt(3, gm.getElo());
            stmt.setInt(4, gm.getPlayed());
            stmt.setInt(5, gm.getLoss());
            stmt.setInt(6, gm.getWon());
            stmt.setString(7, gm.getPlayerUuid());
            stmt.setString(8, gm.getMode());
            stmt.setBoolean(9, gm.isRanked());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void updateLastLogin(PlayerModel pm){
        this.connection = new DatabaseManager().connection();
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement("UPDATE alsatia_players SET lastlogin = ? WHERE uuid = ?");
            stmt.setTimestamp(1, pm.getLastLogin());
            stmt.setString(2, pm.getUuid().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void addWaitingPlayer(Player p, GameModel gm){
        this.connection = new DatabaseManager().connection();
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement("INSERT INTO alsatia_waiting (mode, ranked, player_uuid) VALUES (?, ?, ?)");
            stmt.setString(1, gm.getMode());
            stmt.setBoolean(2, gm.isRanked());
            stmt.setString(3, p.getUniqueId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void removeWaitingPlayer(Player p){
        this.connection = new DatabaseManager().connection();
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement("DELETE FROM alsatia_waiting WHERE player_uuid = ?");
            stmt.setString(1, p.getUniqueId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void removeAllWaitingPlayer(){
        this.connection = new DatabaseManager().connection();
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement("TRUNCATE TABLE alsatia_waiting");
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public boolean isWaitingPlayer(Player p) {
        this.connection = new DatabaseManager().connection();
        PreparedStatement stmt = null;
        ResultSet rs;
        try {
            stmt = connection.prepareStatement("SELECT COUNT(*) FROM alsatia_waiting WHERE player_uuid = ?");
            stmt.setString(1, p.getUniqueId().toString());
            rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public List<WaitingModel> getWaitingPlayers(){
        this.connection = new DatabaseManager().connection();
        PreparedStatement stmt = null;
        ResultSet rs;
        List<WaitingModel> waitingPlayers = new ArrayList<>();
        try {
            stmt = connection.prepareStatement("SELECT * FROM alsatia_waiting");
            rs = stmt.executeQuery();
            while (rs.next()) {
                WaitingModel waitingModel = new WaitingModel();
                waitingModel.setId(rs.getInt(1));
                waitingModel.setMode(rs.getString(2));
                waitingModel.setRanked(rs.getBoolean(3));
                waitingModel.setPlayer_uuid(UUID.fromString(rs.getString(4)));
                waitingPlayers.add(waitingModel);
            }
            return waitingPlayers;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return waitingPlayers;
    }

    public void addServer(ServerModel serverModel){
        this.connection = new DatabaseManager().connection();
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement("INSERT INTO alsatia_servers (servername, mode, ranked, player_uuid1, player_uuid2) VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, serverModel.getServername());
            stmt.setString(2, serverModel.getMode());
            stmt.setBoolean(3, serverModel.getRanked());
            stmt.setString(4, serverModel.getPlayer_uuid1().toString());
            stmt.setString(5, serverModel.getPlayer_uuid2().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public List<ServerModel> getServersWithoutServername(){
        this.connection = new DatabaseManager().connection();
        PreparedStatement stmt = null;
        ResultSet rs;
        List<ServerModel> servers = new ArrayList<>();
        try {
            stmt = connection.prepareStatement("SELECT * FROM alsatia_servers WHERE servername = 'NULL'");
            rs = stmt.executeQuery();
            while (rs.next()) {
                ServerModel server = new ServerModel();
                server.setId(rs.getInt(1));
                server.setServername(rs.getString(2));
                server.setMode(rs.getString(3));
                server.setRanked(rs.getBoolean(4));
                server.setPlayer_uuid1(UUID.fromString(rs.getString(5)));
                server.setPlayer_uuid2(UUID.fromString(rs.getString(6)));
                servers.add(server);
            }
            return servers;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    public ServerModel getServerModel(Player p){
        this.connection = new DatabaseManager().connection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement("SELECT * FROM alsatia_servers WHERE player_uuid1 = ? OR player_uuid2 = ?");
            stmt.setString(1, p.getUniqueId().toString());
            stmt.setString(2, p.getUniqueId().toString());
            rs = stmt.executeQuery();

            if (rs.next()) {
                ServerModel sm = new ServerModel();
                sm.setId(rs.getInt(1));
                sm.setServername(rs.getString(2));
                sm.setMode(rs.getString(3));
                sm.setRanked(rs.getBoolean(4));
                sm.setPlayer_uuid1(UUID.fromString(rs.getString(5)));
                sm.setPlayer_uuid2(UUID.fromString(rs.getString(6)));
                return sm;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return null;
    }

    public void deleteServer(int id){
        this.connection = new DatabaseManager().connection();
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement("DELETE FROM alsatia_servers WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public String getEloString(int elo) {

        int colorIndex = elo / 100; // Calculate the index of the color based on the Elo

        String[] colors = {ChatColor.BLACK.toString(), ChatColor.RED.toString(), ChatColor.GRAY.toString(),
                ChatColor.GOLD.toString(), ChatColor.AQUA.toString(), ChatColor.GREEN.toString()};
        String[] colorNames = {"CHARBON", "REDSTONE", "FER", "OR", "DIAMANT", "EMERAUDE"};

        return colors[colorIndex] + colorNames[colorIndex];
    }
}
