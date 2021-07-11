package pt.m0rais.crates.dao;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import pt.m0rais.crates.database.MySQL;
import pt.m0rais.crates.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class UserDao {

    private final HashMap<UUID, User> users = new HashMap<>();
    private final MySQL mySQL;

    public UserDao(FileConfiguration config) {
        mySQL = new MySQL(config.getString("mysql.host"), config.getString("mysql.database"), "3306",
                config.getString("mysql.user"), config.getString("mysql.password"));
        mySQL.createTable("senior_crates", "uuid VARCHAR(36), crates INT, dayDelay LONG, delay LONG");
    }

    private void save(User user) {
        users.put(user.getUuid(), user);
        try (Connection connection = mySQL.getConnection()) {
            try (PreparedStatement stm = connection.prepareStatement("INSERT INTO senior_crates(uuid, crates, dayDelay, delay) VALUES(?,?,?,?)")) {
                stm.setString(1, user.getUuid().toString());
                stm.setInt(2, user.getCrates());
                stm.setLong(3, user.getDay());
                stm.setLong(4, user.getDelay());
                stm.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(User user, boolean remove) {
        if (remove) users.remove(user.getUuid());
        try (Connection connection = mySQL.getConnection()) {
            try (PreparedStatement stm = connection.prepareStatement("UPDATE senior_crates SET crates = ?, dayDelay = ?, delay = ? WHERE uuid = ?")) {
                stm.setInt(1, user.getCrates());
                stm.setLong(2, user.getDay());
                stm.setLong(3, user.getDelay());
                stm.setString(4, user.getUuid().toString());
                stm.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void load(UUID uuid) {
        try (Connection connection = mySQL.getConnection()) {
            try (PreparedStatement stm = connection.prepareStatement("SELECT crates, dayDelay, delay FROM senior_crates WHERE uuid = ?")) {
                stm.setString(1, uuid.toString());
                try (ResultSet rs = stm.executeQuery()) {
                    if (rs.next()) {
                        users.put(uuid, new User(uuid, rs.getInt("crates"), rs.getLong("dayDelay"), rs.getLong("delay")));
                        return;
                    }
                    save(new User(uuid));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User get(UUID uuid) {
        return users.get(uuid);
    }

    public User get(Player player) {
        return users.get(player.getUniqueId());
    }

    public HashMap<UUID, User> getUsers() {
        return users;
    }

}