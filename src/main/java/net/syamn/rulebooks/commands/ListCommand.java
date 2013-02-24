/**
 * RuleBooks - Package: net.syamn.rulebooks.commands Created: 2013/02/23
 * 18:04:53
 */
package net.syamn.rulebooks.commands;

import static net.syamn.rulebooks.I18n._;

import java.util.Map;

import net.syamn.rulebooks.I18n;
import net.syamn.rulebooks.Perms;
import net.syamn.rulebooks.manager.RuleBook;
import net.syamn.rulebooks.manager.RuleBookManager;
import net.syamn.utils.Util;
import net.syamn.utils.economy.EconomyUtil;
import net.syamn.utils.exception.CommandException;

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
        if (books.size() <= 0) {
            throw new CommandException(_("NoBooks"));
        }

        Util.message(sender, _("AllListHeader", I18n.COUNT, books.size()));

        final boolean econEnabled = plugin.getConfigs().isEnabledEcon();
        String line;
        for (final RuleBook book : books.values()) {
            line = "&6 " + book.getName();
            if (econEnabled) {
                line += "&7 (" + _("Price") + ": " + ((book.getCost() > 0) ? EconomyUtil.getCurrencyString(book.getCost()) : "FREE") + ")";
            }
            Util.message(sender, line);
        }
    }
}
