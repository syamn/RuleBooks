/**
 * RuleBooks - Package: net.syamn.rulebooks.commands Created: 2013/02/23
 * 19:54:59
 */
package net.syamn.rulebooks.commands;

import static net.syamn.rulebooks.I18n._;
import net.syamn.rulebooks.I18n;
import net.syamn.rulebooks.Perms;
import net.syamn.rulebooks.manager.RuleBookManager;
import net.syamn.utils.StrUtil;
import net.syamn.utils.Util;
import net.syamn.utils.economy.EconomyUtil;
import net.syamn.utils.exception.CommandException;

/**
 * CostCommand (CostCommand.java)
 * 
 * @author syam(syamn)
 */
public class CostCommand extends BaseCommand {
    public CostCommand() {
        bePlayer = true;
        name = "cost";
        argLength = 2;
        usage = "<bookName> <price> <- set book price";
        perm = Perms.COST;
    }

    @Override
    public void execute() throws CommandException {
        if (!plugin.getConfigs().isEnabledEcon()) {
            throw new CommandException(_("VaultDisabled"));
        }

        // check book
        final String name = args.get(0).trim();
        if (!RuleBookManager.isExist(name)) {
            throw new CommandException(_("BookNotFound", name));
        }

        // check price
        double cost = 0;
        if ("free".equalsIgnoreCase(args.get(1))) {
            cost = 0;
        } else {
            if (StrUtil.isDouble(args.get(1))) {
                cost = Double.parseDouble(args.get(1));
            } else {
                throw new CommandException(_("InvalidPrice", I18n.COST, args.get(1)));
            }
        }

        if (cost < 0) {
            throw new CommandException(_("InvalidPrice", I18n.COST, cost));
        }

        RuleBookManager.getBook(name).setCost(cost);
        Util.message(sender, _("SetPrice", I18n.BOOK_NAME, name, I18n.COST, (cost > 0) ? EconomyUtil.getCurrencyString(cost) : "FREE"));
    }
}
