/**
 * RuleBooks - Package: net.syamn.rulebooks.listeners Created: 2012/09/09
 * 17:41:20
 */
package net.syamn.rulebooks.listeners;

import static net.syamn.rulebooks.I18n._;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.syamn.rulebooks.I18n;
import net.syamn.rulebooks.RuleBooks;
import net.syamn.rulebooks.manager.RuleBook;
import net.syamn.rulebooks.manager.RuleBookManager;
import net.syamn.utils.LogUtil;
import net.syamn.utils.Util;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;

/**
 * RuleBooksListener (RuleBooksListener.java)
 * 
 * @author syam
 */
public class RuleBooksListener implements Listener {
    private final RuleBooks plugin;

    public RuleBooksListener(final RuleBooks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        if (!plugin.getConfigs().giveBooksOnFirstJoin()) {
            return;
        }

        final Player player = event.getPlayer();
        final String pname = player.getName();
        // First join
        if (!player.hasPlayedBefore()) {
            final List<String> bookNames = plugin.getConfigs().getBookNames();
            if (bookNames == null || bookNames.isEmpty()) {
                return;
            }

            final List<RuleBook> books = new ArrayList<RuleBook>();
            for (String name : bookNames) {
                name = name.trim();
                if (name.length() <= 0 || !RuleBookManager.isExist(name)) {
                    continue;
                }
                books.add(RuleBookManager.getBook(name));
            }
            if (books.isEmpty()) {
                return;
            }

            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    if (player == null || !player.isOnline()) {
                        LogUtil.warning("Failed to give rulebooks to player " + pname + ": player offline");
                        return;
                    }

                    // check empty slot
                    Iterator<ItemStack> iter = player.getInventory().iterator();
                    int emptySlot = 0;
                    while (iter.hasNext()) {
                        if (iter.next() == null) {
                            emptySlot++;
                        }
                    }
                    if (books.size() > emptySlot) {
                        LogUtil.warning("Failed to give rulebooks to player " + pname + ": not enough empty slot");
                        return;
                    }

                    // add books
                    for (final RuleBook book : books) {
                        player.getInventory().addItem(book.getItem());
                    }

                    Util.message(player, _("FirstJoin", I18n.COUNT, books.size()));
                }
            }, 4L);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPluginDisable(final PluginDisableEvent event) {
        if (!plugin.getConfigs().isEnabledEcon()) {
            return;
        }

        if (event.getPlugin().getName().equals("Vault")) {
            LogUtil.warning("Detected unloading Vault plugin. Disabled Vault integration.");
            plugin.getConfigs().disableEcon();
        }
    }
}
