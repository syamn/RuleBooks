/**
 * RuleBooks - Package: net.syamn.rulebooks.events Created: 2013/02/25 12:49:47
 */
package net.syamn.rulebooks.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * PreTransactionEvent (PreTransactionEvent.java)
 * 
 * @author syam(syamn)
 */
public class PreTransactionEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled = false;

    private Player player;
    private String bookName;
    private ItemStack item;
    private double price;

    public PreTransactionEvent(Player player, String bookName, ItemStack item, double price) {
        this.player = player;
        this.bookName = bookName;
        this.item = item;
        this.price = price;
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getBookname() {
        return this.bookName;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
