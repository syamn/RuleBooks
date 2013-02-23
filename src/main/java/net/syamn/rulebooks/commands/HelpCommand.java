/**
 * RuleBooks - Package: net.syamn.rulebooks.commands Created: 2012/09/08
 * 12:53:08
 */
package net.syamn.rulebooks.commands;

import net.syamn.rulebooks.RuleBooks;
import net.syamn.utils.Util;

/**
 * HelpCommand (HelpCommand.java)
 * 
 * @author syam
 */
public class HelpCommand extends BaseCommand {
    public HelpCommand() {
        bePlayer = false;
        name = "help";
        argLength = 0;
        usage = "<- show command help";
        perm = null;
    }

    @Override
    public void execute() {
        Util.message(sender, "&c===================================");
        Util.message(sender, "&bRuleBooks Plugin version &3%version &bby syamn");
        Util.message(sender, " &b<>&f = required, &b[]&f = optional");
        // 全コマンドをループで表示
        for (BaseCommand cmd : RuleBooks.commands.toArray(new BaseCommand[0])) {
            cmd.sender = this.sender;
            if (cmd.canExecute(sender)) {
                Util.message(sender, "&8-&7 /" + command + " &c" + cmd.name + " &7" + cmd.usage);
            }
        }
        Util.message(sender, "&c===================================");

        return;
    }
}
