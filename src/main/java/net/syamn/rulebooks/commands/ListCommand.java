/**
 * RuleBooks - Package: net.syamn.rulebooks.commands Created: 2013/02/23
 * 18:04:53
 */
package net.syamn.rulebooks.commands;

import java.util.Map;

import net.syamn.rulebooks.Perms;
import net.syamn.rulebooks.manager.RuleBook;
import net.syamn.rulebooks.manager.RuleBookManager;
import net.syamn.utils.Util;
import net.syamn.utils.exception.CommandException;
import static net.syamn.rulebooks.I18n._;

/**
 * ListCommand (ListCommand.java)
 * 
 * @author syam(syamn)
 */
public class ListCommand extends BaseCommand {
    public ListCommand() {
        bePlayer = false;
        name = "list";
        argLength = 0;
        usage = "<- see all rule books";
        perm = Perms.LIST;
    }

    @Override
    public void execute() throws CommandException {
        Map<String, RuleBook> books = RuleBookManager.getBooks();
        if (books.size() <= 0) { throw new CommandException(_("NoBooks")); }

        Util.message(sender, "&a ========== &bRuleBooks(" + books.size() + ") &a==========");
        for (final RuleBook book : books.values()) {
            Util.message(sender, " &6" + book.getName() + "&7 (Cost: " + book.getCost() + " )");
        }
    }
}
