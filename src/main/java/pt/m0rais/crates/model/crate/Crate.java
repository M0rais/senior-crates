package pt.m0rais.crates.model.crate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@AllArgsConstructor
@Getter
public class Crate {

    private final String id;
    private final ItemStack item;
    private final List<Prize> prizes;
    private int prizeID;

    public void addPrizeID() {
        prizeID++;
    }

}