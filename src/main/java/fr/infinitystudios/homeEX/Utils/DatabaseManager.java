package fr.infinitystudios.homeEX.Utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseManager {

    private final JavaPlugin plugin;
    private Connection connection;

    public DatabaseManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void connect() throws SQLException {
        File dbFile = new File(plugin.getDataFolder(), "data.db");
        String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();

        connection = DriverManager.getConnection(url);
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public void initDatabase() throws  SQLException {
        connect();
        CreateTable();
    }

    public void CreateTable() throws SQLException {
        String sql = """
        CREATE TABLE IF NOT EXISTS homes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                uuid TEXT NOT NULL,
                homename TEXT NOT NULL,
                itemstack BLOB NOT NULL,
                UNIQUE(uuid, homename)
        );
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void addHome(UUID uuid, String homeName, ItemStack item) throws SQLException {
        String sql = """
        INSERT INTO homes (uuid, homename, itemstack)
        VALUES (?, ?, ?);
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, uuid.toString());
            ps.setString(2, homeName);
            ps.setBytes(3, SerialisationUtils.itemToBytes(item));
            ps.executeUpdate();
        }
    }

    public void removeHome(UUID uuid, String homeName) throws SQLException {
        String sql = "DELETE FROM homes WHERE id = ?;";
        int id = getHomeId(uuid, homeName);
        if(id == 0){
            return;
        }

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public void editHomeItem(int id, ItemStack item) throws SQLException {
        String sql = "UPDATE homes SET itemstack = ? WHERE id = ?;";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setBytes(1, SerialisationUtils.itemToBytes(item));
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public int getHomeId(UUID uuid, String homeName) throws SQLException {
        String sql = "SELECT id FROM homes WHERE uuid = ? AND homename = ?;";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, uuid.toString());
            ps.setString(2, homeName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }

        return 0;
    }

    public List<String> getHomes(UUID uuid) throws SQLException {
        String sql = "SELECT homename FROM homes WHERE uuid = ?;";

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, uuid.toString());
            try(ResultSet rs = ps.executeQuery()){
                List<String> homes = new ArrayList<>();
                while(rs.next()){
                    homes.add(rs.getString("homename"));
                }
                return homes;
            }
        }
    }

    public ArrayList<HomeInfo> getHomesInfo(UUID uuid) throws  SQLException {
        String sql = "SELECT * FROM homes WHERE uuid = ?;";

        ArrayList<HomeInfo> homes = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    homes.add(maprow(rs));
                }
            }
        }
        return homes;
    }

    public boolean containsHome(UUID uuid, String homeName) throws  SQLException {
        String sql = "SELECT id FROM homes WHERE uuid = ? AND homename = ?;";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, uuid.toString());
            ps.setString(2, homeName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        }

        return false;
    }

    public String getHomeName(int id) throws  SQLException {
        String sql = "SELECT homename FROM homes WHERE id = ?;";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return rs.getString("homename");
                }
            }
        }

        return null;
    }

    private HomeInfo maprow(ResultSet rs) throws SQLException {
        return new HomeInfo(
                rs.getInt("id"),
                UUID.fromString(rs.getString("uuid")),
                rs.getString("homename"),
                SerialisationUtils.bytesToItem(rs.getBytes("itemstack"))
        );
    }

}
