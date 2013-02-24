/**
 * RuleBooks - Package: net.syamn.rulebooks Created: 2013/02/23 11:12:27
 */
package net.syamn.rulebooks;

import net.syamn.utils.Util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

/**
 * Perms (Perms.java)
 * 
 * @author syam(syamn)
 */
public enum Perms {
    /* 権限ノード */
    NEW("admin.new"), DELETE("admin.delete"), COST("admin.cost"), LIST("admin.list"), RELOAD("admin.reload"), ;

    // ノードヘッダー
    final String HEADER = "rulebooks.";
    private String node;

    /**
     * コンストラクタ
     * 
     * @param node
     *            権限ノード
     */
    Perms(final String node) {
        this.node = HEADER + node;
    }

    /**
     * 指定したプレイヤーが権限を持っているか
     * 
     * @param perm
     *            Permissible. Player, CommandSender etc
     * @return boolean
     */
    public boolean has(final Permissible perm) {
        if (perm == null) { return false; }
        return perm.hasPermission(getNode());
    }

    /**
     * 指定したプレイヤーが権限を持っているか
     * 
     * @param perm
     *            Permissible. Player, CommandSender etc
     * @param subPerm
     *            subPermission without head period
     * @return boolean
     */
    public boolean has(final Permissible perm, final String subPerm) {
        if (perm == null) { return false; }
        return perm.hasPermission(getNode().concat("." + subPerm));
    }

    /**
     * Send message to players has this permission.
     * 
     * @param message
     *            send message.
     */
    public void message(final String message) {
        for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (this.has(player)) {
                Util.message(player, message);
            }
        }
    }

    public String getNode() {
        return this.node;
    }
}
