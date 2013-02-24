/**
 * RuleBooks - Package: net.syamn.rulebooks.manager Created: 2013/02/23 11:52:33
 */
package net.syamn.rulebooks.manager;

import java.io.File;
import java.io.IOException;

import net.syamn.utils.LogUtil;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

/**
 * RuleBook (RuleBook.java)
 * 
 * @author syam(syamn)
 */
public class RuleBook {
    private String bookName;
    private ItemStack item;
    private double cost = 0D;

    private RuleBook(final String name, final ItemStack item) {
        this.bookName = name.trim();
        this.item = item.clone();
    }

    RuleBook(final File file) {
        YamlConfiguration conf = new YamlConfiguration();
        try {
            conf.load(file);

            this.bookName = conf.getString("Name", null).trim();
            this.cost = conf.getDouble("Cost", 0D);
            this.item = (ItemStack) conf.get("Item");
        } catch (Exception ex) {
            LogUtil.warning("Could not load book data (" + file.getName() + "): " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static RuleBook newBook(String name, final ItemStack item) {
        name = name.trim();
        RuleBook book = new RuleBook(name, item);
        book.save();
        RuleBookManager.putBook(book);
        return book;
    }

    public String getName() {
        return this.bookName;
    }

    public void setCost(final double cost) {
        this.cost = cost;
        save();
        RuleBookManager.putBook(this);
    }

    public double getCost() {
        return this.cost;
    }

    public void setItem(final ItemStack item) {
        this.item = item;
        RuleBookManager.putBook(this);
    }

    public ItemStack getItem() {
        return this.item.clone();
    }

    public boolean save() {
        File file = new File(RuleBookManager.getDataDirectory(), bookName + ".yml");
        YamlConfiguration conf = new YamlConfiguration();

        conf.set("Name", bookName);
        conf.set("Cost", cost);
        conf.set("Item", item);

        try {
            conf.save(file);
            return true;
        } catch (IOException ex) {
            LogUtil.warning("Could not save " + bookName + " data: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    public boolean delete() {
        File file = new File(RuleBookManager.getDataDirectory(), bookName + ".yml");

        if (!file.exists() || file.delete()) {
            this.item = null;
            RuleBookManager.removeBook(this);
            return true;
        } else {
            LogUtil.warning("Could not delete rulebook file: " + file.getPath());
            return false;
        }
    }
}
