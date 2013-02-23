/**
 * RuleBooks - Package: net.syamn.rulebooks.commands
 * Created: 2013/02/23 19:54:59
 */
package net.syamn.rulebooks.commands;

import net.syamn.rulebooks.Perms;
import net.syamn.rulebooks.manager.RuleBookManager;
import net.syamn.utils.StrUtil;
import net.syamn.utils.Util;
import net.syamn.utils.exception.CommandException;

/**
 * CostCommand (CostCommand.java)
 * @author syam(syamn)
 */
public class CostCommand extends BaseCommand{
    public CostCommand() {
        bePlayer = true;
        name = "cost";
        argLength = 2;
        usage = "<bookName> <price> <- set book price";
        perm = Perms.COST;
    }

    @Override
    public void execute() throws CommandException {
        if (!plugin.getConfigs().isEnabledEcon()){
            throw new CommandException("&cEconomy links disabled on configuration!");
        }
        
        // check book
        final String name = args.get(0).trim();
        if (!RuleBookManager.isExist(name)){
            throw new CommandException("&cRuleBook '" + name + "' is not exist!");
        }
        
        // check price
        if (!StrUtil.isDouble(args.get(1))){
            throw new CommandException("&cPrice must be numeric: " + args.get(1));
        }
        final double cost = Double.parseDouble(args.get(1));
        if (cost < 0){
            throw new CommandException("&cPrice must be positive!");
        }
        
        RuleBookManager.getBook(name).setCost(cost);
        Util.message(sender, "&aSet price for rulebook '" + name + "': " + cost + "Gold");
    }
}
