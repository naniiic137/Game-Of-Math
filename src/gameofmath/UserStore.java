package gameofmath;

import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

/**
 * In-memory user store shared by the whole web application (kept in the ServletContext).
 * Accounts live as long as the application is running: restarting or redeploying Tomcat
 * clears them. Passwords are never stored, only a per-user salt and PBKDF2 hash.
 */
public final class UserStore {

    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_PASSWORD_LENGTH = 100;
    private static final Pattern USERNAME = Pattern.compile("[A-Za-z0-9_]{3,20}");
    private static final String CONTEXT_KEY = UserStore.class.getName();

    private static final class Account {
        final String displayName;
        final byte[] salt;
        final byte[] hash;

        Account(String displayName, byte[] salt, byte[] hash) {
            this.displayName = displayName;
            this.salt = salt;
            this.hash = hash;
        }
    }

    /** Result of a registration attempt: null error means success. */
    public static final class Result {
        public final String error;
        public final String username;

        private Result(String error, String username) {
            this.error = error;
            this.username = username;
        }

        public boolean ok() {
            return error == null;
        }
    }

    // Keys are lower-cased so "Hamza" and "hamza" cannot both be registered.
    private final ConcurrentMap<String, Account> accounts = new ConcurrentHashMap<>();

    /** Returns the application's single store, creating it on first use. */
    public static UserStore get(ServletContext context) {
        synchronized (context) {
            UserStore store = (UserStore) context.getAttribute(CONTEXT_KEY);
            if (store == null) {
                store = new UserStore();
                context.setAttribute(CONTEXT_KEY, store);
            }
            return store;
        }
    }

    public static boolean isValidUsername(String username) {
        return username != null && USERNAME.matcher(username).matches();
    }

    public Result register(String username, String password, String confirm) {
        username = username == null ? "" : username.trim();
        if (!isValidUsername(username)) {
            return new Result("Username must be 3-20 characters: letters, digits or _.", null);
        }
        if (password == null || password.length() < MIN_PASSWORD_LENGTH
                || password.length() > MAX_PASSWORD_LENGTH) {
            return new Result("Password must be " + MIN_PASSWORD_LENGTH + "-" + MAX_PASSWORD_LENGTH
                    + " characters.", null);
        }
        if (!password.equals(confirm)) {
            return new Result("The two passwords do not match.", null);
        }
        char[] chars = password.toCharArray();
        byte[] salt = PasswordHasher.newSalt();
        Account account = new Account(username, salt, PasswordHasher.hash(chars, salt));
        Arrays.fill(chars, '\0');
        if (accounts.putIfAbsent(key(username), account) != null) {
            return new Result("That username is already taken.", null);
        }
        return new Result(null, username);
    }

    /** Returns the stored display name if the credentials are correct, otherwise null. */
    public String authenticate(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        Account account = accounts.get(key(username.trim()));
        char[] chars = password.toCharArray();
        try {
            if (account == null) {
                // Hash anyway so unknown usernames take about as long as wrong passwords.
                PasswordHasher.hash(chars, PasswordHasher.newSalt());
                return null;
            }
            return PasswordHasher.matches(chars, account.salt, account.hash) ? account.displayName : null;
        } finally {
            Arrays.fill(chars, '\0');
        }
    }

    private static String key(String username) {
        return username.toLowerCase(Locale.ROOT);
    }
}
