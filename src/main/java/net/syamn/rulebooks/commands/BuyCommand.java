/**
 * RuleBooks - Package: net.syamn.rulebooks.commands
 * Created: 2013/02/23 20:10:00
 */
package net.syamn.rulebooks.commands;

import java.util.Iterator;

import net.syamn.rulebooks.manager.RuleBook;
import net.syamn.rulebooks.manager.RuleBookManager;
import net.syamn.utils.StrUtil;
import net.syamn.utils.Util;
import net.syamn.utils.economy.EconomyUtil;
import net.syamn.utils.exception.CommandException;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * BuyCommand (BuyCommand.java)
 * @author syam(syamn)
 */
public class BuyCommand extends BaseCommand{
    public BuyCommand() {
        bePlayer = true;
        name = null;
        argLength = 1;
        usage = "<- buy book";
        perm = null;
    }

    @Override
    public void execute() throws CommandException {
        final String bookName = StrUtil.join(args, " ");
        if (!RuleBookManager.isExist(bookName)){
            throw new CommandException("&cRulebook '" + bookName + "' is not exist!");
        }
        final RuleBook book = RuleBookManager.getBook(bookName);
        
        // check inventory
        PlayerInventory inv = player.getInventory();
        Iterator<ItemStack> iter = inv.iterator();
        boolean hasEmptySlot = false;
        while (iter.hasNext()){
            if (iter.next() == null){
                hasEmptySlot = true;
                break;
            }
        }
        if (!hasEmptySlot){
            throw new CommandException("&cThere is no empty slot in your inventory!");
        }
        
        // pay cost
        double cost = book.getCost();
        boolean paid = false;
        if (cost > 0 && plugin.getConfigs().isEnabledEcon()){
            paid = EconomyUtil.takeMoney(player, cost);
            if (!paid){
                throw new CommandException("&cYou don't have enough money! (" + cost + ")");
            }
        }
        
        inv.addItem(book.getItem());
        if (paid){
            Util.message(sender, "&aYou bought rulebook '" + book.getName() + "' for " + cost + "!");
        }else{
            Util.message(sender, "&aYou got rulebook '" + book.getName() + "'!");
        }
    }
}
