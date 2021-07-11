package pt.m0rais.crates;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import pt.m0rais.crates.command.CrateCommand;
import pt.m0rais.crates.inventory.ConfirmationInventory;
import pt.m0rais.crates.inventory.EditCrateInventory;
import pt.m0rais.crates.listener.CrateListener;
import pt.m0rais.crates.listener.EditCrateListener;
import pt.m0rais.crates.listener.UserListener;
import pt.m0rais.crates.manager.CrateManager;
import pt.m0rais.crates.manager.InventoryManager;
import pt.m0rais.crates.manager.UserManager;
import pt.m0rais.crates.util.NBTUtil;

public class SeniorCrates extends JavaPlugin {

    private static SeniorCrates instance;
    private CrateManager crateManager;
    private InventoryManager inventoryManager;
    private UserManager userManager;
    private final NBTUtil nbtUtil = new NBTUtil();
    private ConfirmationInventory confirmationInventory;
    private EditCrateInventory editCrateInventory;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        loadManager();
        loadInventory();
        loadListener();
        loadCommand();
    }

    private void loadManager() {
        inventoryManager = new InventoryManager(this);
        userManager = new UserManager(getConfig());
        crateManager = new CrateManager(this);
    }

    private void loadInventory() {
        confirmationInventory = new ConfirmationInventory(crateManager);
        editCrateInventory = new EditCrateInventory(this);
    }

    private void loadListener() {
        loadListeners(new EditCrateListener(this), new UserListener(userManager),
                new CrateListener(this));
    }

    private void loadCommand() {
        getCommand("crate").setExecutor(new CrateCommand(this));
    }

    private void loadListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    public NBTUtil getNbtUtil() {
        return nbtUtil;
    }

    public static SeniorCrates getInstance() {
        return instance;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public EditCrateInventory getEditCrateInventory() {
        return editCrateInventory;
    }

    public CrateManager getCrateManager() {
        return crateManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public ConfirmationInventory getConfirmationInventory() {
        return confirmationInventory;
    }
}