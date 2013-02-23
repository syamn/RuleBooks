/**
 * RuleBooks - Package: net.syamn.rulebooks Created: 2012/09/08 12:08:02
 */
package net.syamn.rulebooks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.syamn.rulebooks.commands.BaseCommand;
import net.syamn.rulebooks.commands.BuyCommand;
import net.syamn.rulebooks.commands.HelpCommand;
import net.syamn.rulebooks.commands.ReloadCommand;
import net.syamn.rulebooks.listeners.RuleBooksListener;
import net.syamn.rulebooks.manager.RuleBookManager;
import net.syamn.utils.LogUtil;
import net.syamn.utils.Metrics;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * RuleBooks (RuleBooks.java)
 * 
 * @author syam
 */
public class RuleBooks extends JavaPlugin {

    // ** Listener **
    RuleBooksListener serverListener = new RuleBooksListener(this);

    // ** Commands **
    public static List<BaseCommand> commands = new ArrayList<BaseCommand>();

    // ** Private Classes **
    private ConfigurationManager config;

    // ** Instance **
    private static RuleBooks instance;

    

    /**
     * プラグイン起動処理
     */
    @Override
    public void onEnable() {
        instance = this;
        LogUtil.init(this);

        PluginManager pm = getServer().getPluginManager();
        config = new ConfigurationManager(this);

        // loadconfig
        try {
            config.loadConfig(true);
        } catch (Exception ex) {
            LogUtil.warning("an error occured while trying to load the config file.");
            ex.printStackTrace();
        }

        // check really enabled?
        if (!pm.isPluginEnabled(this)) {
            return;
        }

        // Regist Listeners
        pm.registerEvents(serverListener, this);

        registerCommands();
        
        // load Books
        RuleBookManager.loadBooks();

        // メッセージ表示
        PluginDescriptionFile pdfFile = this.getDescription();
        LogUtil.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");

        setupMetrics(); // mcstats
    }

    /**
     * プラグイン停止処理
     */
    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        
        // save
        RuleBookManager.dispose();
        
        // messages
        PluginDescriptionFile pdfFile = this.getDescription();
        LogUtil.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is disabled!");
    }

    /**
     * コマンドを登録
     */
    private void registerCommands() {
        // Intro Commands
        commands.add(new HelpCommand());

        // General Commands

        // Admin Commands
        commands.add(new ReloadCommand());
    }

    /**
     * Metricsセットアップ
     */
    private void setupMetrics() {
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException ex) {
            LogUtil.warning("Cant send metrics data!");
            ex.printStackTrace();
        }
    }

    /**
     * コマンドが呼ばれた
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]) {
        if (cmd.getName().equalsIgnoreCase("rulebooks")) {
            //if (args.length == 0) {
            //    args = new String[] { "help" };
            //}

            outer: for (BaseCommand command : commands.toArray(new BaseCommand[0])) {
                String[] cmds = command.name.split(" ");
                for (int i = 0; i < cmds.length; i++) {
                    if (i >= args.length || !cmds[i].equalsIgnoreCase(args[i])) {
                        continue outer;
                    }
                    // run actually
                    return command.run(this, sender, args, commandLabel);
                }
            }
            
            if (args.length == 0){
                new HelpCommand().run(this, sender, args, commandLabel);
            }
            else{
                new BuyCommand().run(this, sender, args, commandLabel);
            }
            return true;
        }
        return false;
    }

    /* getter */
    /**
     * 設定マネージャを返す
     * 
     * @return ConfigurationManager
     */
    public ConfigurationManager getConfigs() {
        return config;
    }

    /**
     * インスタンスを返す
     * 
     * @return RuleBooksインスタンス
     */
    public static RuleBooks getInstance() {
        return instance;
    }
}
