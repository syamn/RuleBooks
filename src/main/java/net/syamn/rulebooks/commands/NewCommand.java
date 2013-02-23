/**
 * RuleBooks - Package: net.syamn.rulebooks.commands
 * Created: 2013/02/23 19:23:49
 */
package net.syamn.rulebooks.commands;

import net.syamn.rulebooks.Perms;
import net.syamn.rulebooks.manager.RuleBook;
import net.syamn.rulebooks.manager.RuleBookManager;
import net.syamn.utils.Util;
import net.syamn.utils.exception.CommandException;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * NewCommand (NewCommand.java)
 * @author syam(syamn)
 */
public class NewCommand extends BaseCommand{
    public NewCommand() {
        bePlayer = true;
        name = "new";
        argLength = 1;
        usage = "<bookName> <- add new rule book";
        perm = Perms.NEW;
    }

    @Override
    public void execute() throws CommandException {
        final String name = args.get(0).trim();
        
        if (RuleBookManager.isExist(name)){
            throw new CommandException("&cSame name book already exists!");
        }
        
        final ItemStack is = player.getItemInHand();
        if (is == null || is.getType() != Material.WRITTEN_BOOK){
            throw new CommandException("&cYou must hold a written book!");
        }
        
        RuleBook.newBook(name, is);
        Util.message(sender, "&aRulebook '" + name + "' saved!");
    }
}