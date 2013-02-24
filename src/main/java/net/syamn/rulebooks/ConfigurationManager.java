/**
 * RuleBooks - Package: net.syamn.rulebooks Created: 2012/09/08 12:08:18
 */
package net.syamn.rulebooks;

import java.io.File;
import java.util.List;

import net.milkbowl.vault.economy.Economy;
import net.syamn.utils.LogUtil;
import net.syamn.utils.SakuraLib;
import net.syamn.utils.economy.EconomyUtil;
import net.syamn.utils.file.FileStructure;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * ConfigrationManager (ConfigrationManager.java)
 * 
 * @author syam
 */
public class ConfigurationManager {
    /* Current config.yml file version */
    private final int latestVersion = 1;

    private RuleBooks plugin;

    private FileConfiguration conf;
    private File pluginDir;

    // ** Hookup Plugins **
    private boolean isEnabledEcon = false;

    public boolean isEnabledEcon() {
        return this.isEnabledEcon;
    }

    public void disableEcon() {
        this.isEnabledEcon = false;
    }

    /**
     * コンストラクタ
     * 
     * @param plugin
     */
    public ConfigurationManager(final RuleBooks plugin) {
        this.plugin = plugin;
        this.pluginDir = this.plugin.getDataFolder();
    }

    /**
     * 設定をファイルから読み込む
     * 
     * @param initialLoad
     *            初回ロードかどうか
     */
    public void loadConfig(boolean initialLoad) throws Exception {
        FileStructure.createDir(pluginDir);

        File file = new File(pluginDir, "config.yml");
        if (!file.exists()) {
            FileStructure.extractResource("/config.yml", pluginDir, false, false, plugin);
            LogUtil.info("config.yml is not found! Created default config.yml!");
        }

        plugin.reloadConfig();
        conf = plugin.getConfig();

        checkver(conf.getInt("ConfigVersion", 1));

        // setup Vault economy
        if (getUseVault()) {
            RegisteredServiceProvider<Economy> econProv = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
            if (econProv != null) {
                SakuraLib.setEconomy(econProv.getProvider());
                this.isEnabledEcon = true;
                LogUtil.info("Enabled economy hookup! Using Vault (" + EconomyUtil.getEconomyName() + ") for economy plugin!");
            } else {
                this.isEnabledEcon = false;
                LogUtil.warning("Could not hook to economy plugin!");
            }
        } else {
            this.isEnabledEcon = false;
        }
    }

    /**
     * Check configuration file version
     * 
     * @param ver
     */
    private void checkver(final int ver) {
        // compare configuration file version
        if (ver < latestVersion) {
            // first, rename old configuration
            final String destName = "oldconfig-v" + ver + ".yml";
            String srcPath = new File(pluginDir, "config.yml").getPath();
            String destPath = new File(pluginDir, destName).getPath();
            try {
                FileStructure.copyTransfer(srcPath, destPath);
                LogUtil.info("Copied old config.yml to " + destName + "!");
            } catch (Exception ex) {
                LogUtil.warning("Failed to copy old config.yml!");
            }

            // force copy config.yml and languages
            FileStructure.extractResource("/config.yml", pluginDir, true, false, plugin);
            // Language.extractLanguageFile(true);

            plugin.reloadConfig();
            conf = plugin.getConfig();

            LogUtil.info("Deleted existing configuration file and generate a new one!");
        }
    }

    /* ***** Begin Configuration Getters ******************** */
    // General
    public String getLanguage() {
        return conf.getString("Language", "default");
    }

    private boolean getUseVault() {
        return conf.getBoolean("UseVault", false);
    }

    public boolean giveBooksOnFirstJoin() {
        return conf.getBoolean("FirstJoin.GiveRulebooks", false);
    }

    public List<String> getBookNames() {
        return conf.getStringList("FirstJoin.BookNames");
    }
}
