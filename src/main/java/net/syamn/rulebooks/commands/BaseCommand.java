/**
 * RuleBooks - Package: net.syamn.rulebooks.commands Created: 2012/09/08
 * 12:10:18
 */
package net.syamn.rulebooks.commands;

import java.util.ArrayList;
import java.util.List;

import net.syamn.rulebooks.Perms;
import net.syamn.rulebooks.RuleBooks;
import net.syamn.utils.Util;
import net.syamn.utils.exception.CommandException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import static net.syamn.rulebooks.I18n._;

/**
 * BaseCommand (BaseCommand.java)
 * 
 * @author syam
 */
public abstract class BaseCommand {
    protected RuleBooks plugin;

    /* コマンド関係 */
    protected String command;
    protected CommandSender sender;
    protected Player player;
    protected boolean isPlayer;
    protected List<String> args = new ArrayList<String>();
    protected int argLength = 0;

    public String name;
    protected boolean bePlayer = true;
    protected String usage;
    protected Perms perm = null;

    public boolean run(RuleBooks plugin, CommandSender sender, String[] preArgs, String cmd) {
        this.plugin = plugin;
        this.sender = sender;
        this.command = cmd;

        // 引数をソート
        args.clear();
        for (String arg : preArgs) {
            args.add(arg);
        }

        if (name != null) {
            for (int i = 0; i < name.split(" ").length && i < args.size(); i++) {
                args.remove(0);
            }
        }

        // 引数の長さチェック
        if (argLength > args.size()) {
            sendUsage();
            return true;
        }

        if (sender instanceof Player) {
            player = (Player) sender;
            isPlayer = true;
        } else {
            player = null;
            isPlayer = false;
        }

        // 実行にプレイヤーであることが必要かチェックする
        if (bePlayer && !isPlayer) {
            Util.message(sender, "&cThis command cannot run from Console!");
            return true;
        }

        // 権限チェック
        if (!canExecute(sender)) {
            Util.message(sender, _("PermissionDenied"));
            return true;
        }

        // 実行
        try {
            execute();
        } catch (CommandException ex) {
            Throwable error = ex;
            while (error instanceof Exception) {
                Util.message(sender, error.getMessage());
                error = error.getCause();
            }
        }

        return true;
    }

    /**
     * コマンドを実際に実行する
     * 
     * @return 成功すればtrue それ以外はfalse
     */
    public abstract void execute() throws CommandException;

    /**
     * コマンド実行に必要な権限を持っているか検証する
     * 
     * @return trueなら権限あり、falseなら権限なし
     */
    public boolean permission(CommandSender sender) {
        return true;
    }

    final public boolean canExecute(CommandSender sender) {
        if ((perm != null && !perm.has(sender)) || !permission(sender)) { return false; }
        return true;
    }

    /**
     * コマンドの使い方を送信する
     */
    public void sendUsage() {
        Util.message(sender, "&c/" + this.command + " " + usage);
    }
}
