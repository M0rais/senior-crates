package pt.m0rais.crates.repository;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import pt.m0rais.crates.SeniorCrates;
import pt.m0rais.crates.model.crate.Crate;
import pt.m0rais.crates.model.crate.Prize;
import pt.m0rais.crates.model.util.ConfigModel;
import pt.m0rais.crates.util.NBTUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CrateRepository {

    private final FileConfiguration configuration;
    private final ConfigModel config = new ConfigModel("crates.yml");
    private final NBTUtil nbtUtil;
    private final Map<String, Crate> crates = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public CrateRepository(SeniorCrates plugin) {
        this.configuration = plugin.getConfig();
        this.nbtUtil = plugin.getNbtUtil();
        load();
    }

    public void add(String id, ItemStack item) {
        Crate crate = new Crate(id, nbtUtil.setString(item, "crate", id), new ArrayList<>(), 0);
        crates.put(id, crate);
        save(crate);
    }

    public Crate get(String id) {
        return crates.get(id);
    }

    public boolean isNotKey(String id) {
        return !crates.containsKey(id);
    }

    public void removePrize(Crate crate, int prizeID) {
        FileConfiguration configuration = config.getConfig();
        configuration.set(crate.getId() + ".prizes.id-" + prizeID, null);
        config.save(configuration);
    }

    public void update(Crate crate) {
        FileConfiguration configuration = config.getConfig();
        String id = crate.getId();
        for (Prize prize : crate.getPrizes()) {
            String path = id + ".prizes.id-" + prize.getId();
            configuration.set(path + ".item", prize.getItem());
            configuration.set(path + ".chance", prize.getChance());
        }
        config.save(configuration);
    }

    private void save(Crate crate) {
        FileConfiguration configuration = config.getConfig();
        String id = crate.getId();
        configuration.set(id + ".item", crate.getItem());
        for (Prize prize : crate.getPrizes()) {
            String path = id + ".prizes.id-" + prize.getId();
            configuration.set(path + ".item", prize.getItem());
            configuration.set(path + ".chance", prize.getChance());
        }
        config.save(configuration);
    }

    public void remove(String id) {
        crates.remove(id);
        delete(id);
    }

    private void delete(String id) {
        config.getConfig().set(id, null);
        config.save();
    }

    private void load() {
        FileConfiguration configuration = config.getConfig();
        int max = this.configuration.getInt("items");
        for (String key : configuration.getKeys(false)) {

            List<Prize> prizes = new ArrayList<>();

            int index = 0;

            if (configuration.contains(key + ".prizes")) {
                for (String s : configuration.getConfigurationSection(key + ".prizes").getKeys(false)) {
                    String path = key + ".prizes." + s;
                    if (prizes.size() == max) {
                        configuration.set(path, null);
                        continue;
                    }
                    index = Integer.parseInt(s.split("id-")[1]);
                    prizes.add(new Prize(
                            index,
                            configuration.getItemStack(path + ".item"),
                            configuration.getDouble(path + ".chance")
                    ));
                }
                index += 1;
            }

            crates.put(key, new Crate(key, nbtUtil.setString(configuration.getItemStack(key + ".item"), "crate", key), prizes,
                    index));
        }
        config.save(configuration);
    }

    public Crate getCrateByPrize(Prize prize) {
        return crates.values().stream().filter(crate -> crate.getPrizes().contains(prize)).findFirst().orElse(null);
    }

    public Map<String, Crate> getCrates() {
        return crates;
    }

}