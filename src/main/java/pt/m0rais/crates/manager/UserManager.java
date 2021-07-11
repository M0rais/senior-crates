package pt.m0rais.crates.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import pt.m0rais.crates.dao.UserDao;
import pt.m0rais.crates.model.User;
import pt.m0rais.crates.util.MessageUtil;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class UserManager {

    private final UserDao userDao;
    private final FileConfiguration configuration;
    private final long delay;

    public UserManager(FileConfiguration configuration) {
        this.configuration = configuration;
        this.userDao = new UserDao(configuration);
        this.delay = TimeUnit.SECONDS.toMillis(configuration.getInt("delay"));
    }

    public void update(UUID uuid, boolean remove) {
        userDao.update(userDao.get(uuid), remove);
    }

    public void load(UUID uuid) {
        userDao.load(uuid);
    }

    public boolean open(Player player) {
        User user = userDao.get(player);

        if (!player.hasPermission("crates.bypass")) {
            if (user.isDelay()) {
                MessageUtil.sendMessage(player, configuration.getStringList("messages.open.delay"));
                return false;
            }

            if (!user.canOpen(configuration.getInt("max-crates"))) {
                MessageUtil.sendMessage(player, configuration.getStringList("messages.open.day-delay"));
                return false;
            }
        }

        user.updateDelay(delay);
        update(player.getUniqueId(), false);

        return true;
    }

}
