/**
 * RuleBooks - Package: net.syamn.rulebooks.commands Created: 2013/02/23
 * 20:10:00
 */
package net.syamn.rulebooks.commands;

import static net.syamn.rulebooks.I18n._;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import net.syamn.rulebooks.I18n;
import net.syamn.rulebooks.Perms;
import net.syamn.rulebooks.RuleBooks;
import net.syamn.rulebooks.manager.RuleBook;
import net.syamn.rulebooks.manager.RuleBookManager;
import net.syamn.utils.Util;
import net.syamn.utils.economy.EconomyUtil;
import net.syamn.utils.exception.CommandException;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.Permissible;

/**
 * BuyCommand (BuyCommand.java)
 * 
 * @author syam(syamn)
 */
public class BuyCommand extends BaseCommand {
    public BuyCommand() {
        bePlayer = true;
        name = null;
        argLength = 1;
        usage = "<- buy book";
        perm = null;
    }

    @Override
    public void execute() throws CommandException {
        final String bookName = args.get(0).trim();
        final Map<String, RuleBook> books = getBuyableBooks(sender);

        if (!books.containsKey(bookName.toLowerCase(Locale.ENGLISH))) {
            throw new CommandException(_("BookNotFound", I18n.BOOK_NAME, bookName));
        }
        final RuleBook book = books.get(bookName.toLowerCase(Locale.ENGLISH));

        // check inventory
        PlayerInventory inv = player.getInventory();
        Iterator<ItemStack> iter = inv.iterator();
        boolean hasEmptySlot = false;
        while (iter.hasNext()) {
            if (iter.next() == null) {
                hasEmptySlot = true;
                break;
            }
        }
        if (!hasEmptySlot) {
            throw new CommandException(_("NotEnoughSlot"));
        }

        // pay cost
        double cost = book.getCost();
        boolean paid = false;
        if (cost > 0 && plugin.getConfigs().isEnabledEcon()) {
            paid = EconomyUtil.takeMoney(player, cost);
            if (!paid) {
                throw new CommandException(_("NotEnoughMoney", I18n.COST, cost));
            }
        }

        inv.addItem(book.getItem());
        if (paid) {
            Util.message(sender, _("BoughtBook", I18n.BOOK_NAME, book.getName(), I18n.COST, EconomyUtil.getCurrencyString(cost)));
        } else {
            Util.message(sender, _("GotBook", I18n.BOOK_NAME, book.getName()));
        }
    }

    public static void sendBuyables(final Player p) {
        Map<String, RuleBook> books = getBuyableBooks(p);
        if (books.isEmpty()) {
            Util.message(p, _("NoAvailableBooks"));
            return;
        }

        final boolean econEnabled = RuleBooks.getInstance().getConfigs().isEnabledEcon();
        Util.message(p, _("AvailableListHeader", I18n.COUNT, books.size()));

        String line;
        for (final RuleBook book : books.values()) {
            line = "&6 " + book.getName();
            if (econEnabled) {
                line += "&7 (" + _("Price") + ": " + ((book.getCost() > 0) ? EconomyUtil.getCurrencyString(book.getCost()) : "FREE") + ")";
            }
            Util.message(p, line);
        }
    }

    private static boolean isBuyableBook(final RuleBook book, final Permissible perm) {
        if (book == null || perm == null) return false;
        return (Perms.BUY_HEADER.has(perm, book.getName().toLowerCase(Locale.ENGLISH)));
    }

    private static Map<String, RuleBook> getBuyableBooks(final Permissible perm) {
        Map<String, RuleBook> ret = new HashMap<String, RuleBook>();
        ret.clear();

        for (final Map.Entry<String, RuleBook> entry : RuleBookManager.getBooks().entrySet()) {
            if (isBuyableBook(entry.getValue(), perm)) {
                ret.put(entry.getKey(), entry.getValue());
            }
        }

        return ret;
    }
}
