package pt.m0rais.crates.model.crate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
@Setter
public class Prize {

    private final int id;
    private final ItemStack item;
    private double chance;

    public String getStringID() {
        return "Item #" + id;
    }

}