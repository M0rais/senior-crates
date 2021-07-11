package pt.m0rais.crates.manager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import pt.m0rais.crates.SeniorCrates;
import pt.m0rais.crates.model.crate.Crate;
import pt.m0rais.crates.model.crate.Prize;
import pt.m0rais.crates.model.enums.ConfirmationType;
import pt.m0rais.crates.model.inventory.AnimationInventory;
import pt.m0rais.crates.model.util.ItemBuilder;
import pt.m0rais.crates.model.util.Placeholder;
import pt.m0rais.crates.model.util.RandomCollection;
import pt.m0rais.crates.repository.CrateRepository;
import pt.m0rais.crates.util.ColorUtil;
import pt.m0rais.crates.util.MessageUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class CrateManager {

    private final FileConfiguration configuration;
    private final UserManager userManager;
    private final CrateRepository crateRepository;
    private final SeniorCrates plugin;

    public CrateManager(SeniorCrates plugin) {
        this.plugin = plugin;
        this.userManager = plugin.getUserManager();
        this.configuration = plugin.getConfig();
        this.crateRepository = new CrateRepository(plugin);
    }

    public List<Prize> getPrizesByID(String id) {
        return crateRepository.get(id).getPrizes();
    }

    public void removePrize(Player player, String id, Prize prize) {
        if (isNotCrate(player, id)) return;
        Crate crate = crateRepository.get(id);
        MessageUtil.sendMessage(player, configuration.getStringList("messages.edit.remove-prize"));
        crate.getPrizes().removeIf(prize1 -> prize1.getId() == prize.getId());
        crateRepository.removePrize(crate, prize.getId());
    }

    public void confirmation(Player player, String id, boolean cancel, ConfirmationType type) {
        player.closeInventory();
        switch (type) {
            case CREATE:
                this.createCrate(player, id, cancel);
                break;
            case OPEN:
                this.open(player, id, cancel);
                break;
            default:
                this.delete(player, id, cancel);
        }
    }

    public Crate getCrate(String id) {
        return crateRepository.get(id);
    }

    public boolean isNotKey(String id) {
        return crateRepository.isNotKey(id);
    }

    public boolean isNotCrate(CommandSender sender, String id) {
        if (isNotKey(id)) {
            MessageUtil.sendMessage(sender, configuration.getStringList("messages.crate-not-found"));
            return true;
        }
        return false;
    }

    public void giveCrate(CommandSender sender, Player target, String id, int amount) {
        Crate crate = crateRepository.get(id);

        ItemStack crateItem = crate.getItem().clone();
        crateItem.setAmount(amount);
        if (target.getInventory().firstEmpty() == -1) {
            target.getWorld().dropItem(target.getLocation(), crateItem);
        } else {
            target.getInventory().addItem(crateItem);
        }

        MessageUtil.sendMessage(sender, configuration.getStringList("messages.give.sender"),
                new Placeholder("%amount%", Integer.toString(amount)),
                new Placeholder("%player%", target.getName()),
                new Placeholder("%crate%", crate.getId()));
        MessageUtil.sendMessage(target, configuration.getStringList("messages.give.target"),
                new Placeholder("%amount%", Integer.toString(amount)),
                new Placeholder("%crate%", crate.getId()));
    }

    public void createCrate(Player player, String id, boolean cancel) {
        if (cancel) {
            MessageUtil.sendMessage(player, configuration.getStringList("messages.create.cancel"));
            return;
        }

        ItemStack item = player.getItemInHand().clone();
        if (item == null || item.getType() == Material.AIR) {
            MessageUtil.sendMessage(player, configuration.getStringList("messages.create.invalid-item"));
            return;
        }

        item.setAmount(1);

        crateRepository.add(id, item);
        MessageUtil.sendMessage(player, configuration.getStringList("messages.create.success"));
    }

    public void delete(Player player, String id, boolean cancel) {
        if (cancel) {
            MessageUtil.sendMessage(player, configuration.getStringList("messages.delete.cancel"));
            return;
        }

        MessageUtil.sendMessage(player, configuration.getStringList("messages.delete.success"));
        crateRepository.remove(id);
    }

    public void open(Player player, String id, boolean cancel) {
        if (cancel) {
            MessageUtil.sendMessage(player, configuration.getStringList("messages.open.cancel"));
            return;
        }

        if (!userManager.open(player)) return;

        ItemStack item = player.getItemInHand();
        int amount = item.getAmount();
        if (amount == 1)
            player.setItemInHand(null);
        else
            item.setAmount(amount - 1);

        Crate crate = crateRepository.get(id);

        if (crate.getPrizes().size() == 0) {
            player.closeInventory();
            MessageUtil.sendMessage(player, configuration.getStringList("messages.open.empty-crate"));
            return;
        }

        RandomCollection<Prize> prizes = new RandomCollection<>();
        for (Prize prize : crate.getPrizes()) {
            if (prize.getChance() == 0) continue;
            prizes.add(prize.getChance(), prize);
        }

        if (prizes.isEmpty()) {
            player.closeInventory();
            MessageUtil.sendMessage(player, configuration.getStringList("messages.open.empty-crate"));
            return;
        }

        Prize prize = prizes.next();
        player.closeInventory();

        Inventory inventory = Bukkit.createInventory(new AnimationInventory(event -> event.setCancelled(true)), 9 * 5, "Drawing an item...");

        Random random = new Random();

        player.openInventory(inventory);

        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (!inventory.getViewers().contains(player)) {
                    cancel();
                    MessageUtil.sendMessage(player, configuration.getStringList("messages.open.quit"));
                    return;
                }
                if (i < 22) {
                    inventory.setItem(i, buildGlass(getRandomColor(random)));
                    inventory.setItem(44 - i, buildGlass(getRandomColor(random)));
                    i++;
                } else {
                    cancel();
                    ItemStack prizeItem = prize.getItem().clone();
                    inventory.setItem(i, prizeItem);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.closeInventory();

                            String itemName;
                            if (!prizeItem.hasItemMeta()) itemName = prizeItem.getType().name();
                            else itemName = prizeItem.getItemMeta().getDisplayName();

                            MessageUtil.sendMessage(player, configuration.getStringList("messages.open.success"),
                                    new Placeholder("%item_name%", itemName),
                                    new Placeholder("%item_chance%", Double.toString(prize.getChance())));

                            if (player.getInventory().firstEmpty() == -1) {
                                player.getWorld().dropItem(player.getLocation(), prizeItem);
                                return;
                            }
                            player.getInventory().addItem(prizeItem);
                        }
                    }.runTaskLaterAsynchronously(plugin, 20);
                }
            }
        }.runTaskTimerAsynchronously(plugin, 5, 5);

    }

    public int getRandomColor(Random random) {
        return random.nextInt(16);
    }

    private ItemStack buildGlass(int data) {
        return new ItemBuilder(Material.STAINED_GLASS_PANE, data)
                .name(ColorUtil.getChatColorByDyeColor(data) + "...").getItem();
    }

    private final HashMap<Player, Prize> playerPrizeEditMap = new HashMap<>();

    public void addPrize(String id, ItemStack prize) {
        Crate crate = crateRepository.get(id);
        crate.getPrizes().add(new Prize(crate.getPrizeID(), prize.clone(), 0));
        crate.addPrizeID();
        crateRepository.update(crate);
    }

    public void addPrizeEdit(Player player, Prize prize) {
        MessageUtil.sendMessage(player, configuration.getStringList("messages.edit.digit-chance"),
                new Placeholder("%id%", prize.getStringID()));
        playerPrizeEditMap.put(player, prize);
    }

    public void editChance(Player player, String message, Consumer<String> callback) {
        if (!playerPrizeEditMap.containsKey(player)) {
            callback.accept("");
            return;
        }

        Prize prize = playerPrizeEditMap.get(player);

        try {
            double chance = Double.parseDouble(message);
            if (chance > 0 || chance < 100) {
                prize.setChance(chance);
                playerPrizeEditMap.remove(player);
                MessageUtil.sendMessage(player, configuration.getStringList("messages.edit.chance"),
                        new Placeholder("%chance%", Double.toString(chance)),
                        new Placeholder("%id%", prize.getStringID()));
                crateRepository.update(crateRepository.getCrateByPrize(prize));
            } else {
                MessageUtil.sendMessage(player, configuration.getStringList("messages.edit.wrong-chance"));
            }
        } catch (NumberFormatException e) {
            playerPrizeEditMap.remove(player);
            MessageUtil.sendMessage(player, configuration.getStringList("messages.edit.invalid-chance"));
        }

        callback.accept(crateRepository.getCrateByPrize(prize).getId());
    }

}