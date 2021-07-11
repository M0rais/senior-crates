package pt.m0rais.crates.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pt.m0rais.crates.manager.UserManager;

public class UserListener implements Listener {

    private final UserManager userManager;

    public UserListener(UserManager userManager) {
        this.userManager = userManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        userManager.load(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        userManager.update(event.getPlayer().getUniqueId(), true);
    }

}
