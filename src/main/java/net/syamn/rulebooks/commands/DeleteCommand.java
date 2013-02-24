/**
 * RuleBooks - Package: net.syamn.rulebooks.commands Created: 2013/02/23
 * 19:51:38
 */
package net.syamn.rulebooks.commands;

import net.syamn.rulebooks.I18n;
import net.syamn.rulebooks.Perms;
import net.syamn.rulebooks.manager.RuleBookManager;
import net.syamn.utils.StrUtil;
import net.syamn.utils.Util;
import net.syamn.utils.exception.CommandException;
import static net.syamn.rulebooks.I18n._;

/**
 * DeleteCommand (DeleteCommand.java)
 * 
 * @author syam(syamn)
 */
public class DeleteCommand extends BaseCommand {
    public DeleteCommand() {
        bePlayer = false;
        name = "delete";
        argLength = 1;
        usage = "<bookName> <- delete rule book";
        perm = Perms.DELETE;
    }

    @Override
    public void execute() throws CommandException {
        final String name = args.get(0).trim();

        if (!RuleBookManager.isExist(name)) { throw new CommandException(_("BookNotFound", I18n.BOOK_NAME, name)); }

        RuleBookManager.getBook(name).delete();
        Util.message(sender, _("DeleteBook", I18n.BOOK_NAME, name));
    }
}
