/**
 * RuleBooks - Package: net.syamn.rulebooks.events
 * Created: 2013/02/25 13:07:19
 */
package net.syamn.rulebooks.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * TransactionEvent (TransactionEvent.java)
 * @author syam(syamn)
 */
public class TransactionEvent extends Event{
    private static final HandlerList handlers = new HandlerList();
    
    private Player player;
    private String bookName;
    private double price;
    private ItemStack item;
    private boolean paid;
    
    
    public TransactionEvent(PreTransactionEvent event, boolean paid){
        this.player = event.getPlayer();
        this.bookName = event.getBookname();
        this.price = event.getPrice();
        this.item = event.getItem();
        
        this.paid = paid;
    }
    
    public Player getPlayer(){
        return this.player;
    }
    
    public String getBookname(){
        return this.bookName;
    }
    
    public double getPrice(){
        return this.price;
    }
    
    public ItemStack getItem(){
        return this.item;
    }
    
    public boolean isPaid(){
        return this.paid;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
