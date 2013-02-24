/**
 * RuleBooks - Package: net.syamn.rulebooks.commands Created: 2012/09/08
 * 17:14:30
 */
package net.syamn.rulebooks.commands;

import net.syamn.rulebooks.I18n;
import net.syamn.rulebooks.Perms;
import net.syamn.rulebooks.manager.RuleBookManager;
import net.syamn.utils.LogUtil;
import net.syamn.utils.Util;
import static net.syamn.rulebooks.I18n._;

/**
 * ReloadCommand (ReloadCommand.java)
 * 
 * @author syam
 */
public class ReloadCommand extends BaseCommand {
    public ReloadCommand() {
        bePlayer = false;
        name = "reload";
        argLength = 0;
        usage = "<- reload config.yml";
        perm = Perms.RELOAD;
    }

    @Override
    public void execute() {
        try {
            plugin.getConfigs().loadConfig(false);

            I18n.extractLanguageFiles(false);
            I18n.setCurrentLanguage(plugin.getConfigs().getLanguage());

            RuleBookManager.dispose();
            RuleBookManager.loadBooks();

            Util.message(sender, _("Reloaded"));
        } catch (Exception ex) {
            LogUtil.warning("an error occured while trying to load the config file.");
            ex.printStackTrace();
        }
    }
}
