/**
 * RuleBooks - Package: net.syamn.rulebooks.manager Created: 2013/02/23 11:52:54
 */
package net.syamn.rulebooks.manager;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.syamn.rulebooks.RuleBooks;
import net.syamn.rulebooks.commands.BaseCommand;

/**
 * RuleBookManager (RuleBookManager.java)
 * 
 * @author syam(syamn)
 */
public class RuleBookManager {
    final private static String dirName = "books";
    private static Map<String, RuleBook> books = new HashMap<String, RuleBook>();

    public static void dispose() {
        saveBooks();
        books.clear();
    }

    public static boolean isExist(final String name) {
        return books.containsKey(name.toLowerCase(Locale.ENGLISH).trim());
    }

    public static RuleBook getBook(final String name) {
        return books.get(name.toLowerCase(Locale.ENGLISH).trim());
    }

    public static void putBook(final RuleBook book) {
        books.put(book.getName().toLowerCase(Locale.ENGLISH).trim(), book);
    }

    public static void removeBook(final RuleBook book) {
        books.remove(book.getName().toLowerCase(Locale.ENGLISH).trim());
    }

    public static Map<String, RuleBook> getBooks() {
        return Collections.unmodifiableMap(books);
    }

    public static void saveBooks() {
        for (final RuleBook book : books.values()) {
            book.save();
        }
    }

    public static void loadBooks() {
        File dir = getDataDirectory();
        File[] files = dir.listFiles();

        books.clear();

        // ファイルなし
        if (files == null || files.length == 0) return;

        for (final File file : files) {
            try {
                putBook(new RuleBook(file));
            } catch (Exception ex) {
                continue;
            }
        }
    }

    public static File getDataDirectory() {
        final File dir = new File(RuleBooks.getInstance().getDataFolder(), dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static boolean isValidName(final String bookName) {
        if (bookName == null || bookName.contains(" ") || bookName.contains("*") || bookName.contains(File.separator) || bookName.contains(File.pathSeparator) || bookName.contains("<") || bookName.contains(">")) {
            return false;
        }

        RuleBooks.getInstance();
        for (final BaseCommand cmd : RuleBooks.commands) {
            if (bookName.equalsIgnoreCase(cmd.name)) {
                return false;
            }
        }

        return true;
    }
}
