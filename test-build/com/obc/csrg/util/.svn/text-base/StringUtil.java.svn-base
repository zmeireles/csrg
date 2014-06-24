package com.obc.csrg.util;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

import com.obc.csrg.constants.Constants;

public final class StringUtil implements Serializable {

	private static final long serialVersionUID = 200810141857L;

	private StringUtil() { }

    /**
     * Convenience constant instead of calling the system property everywhere.
     */
    public static final String EOL = System.getProperty("line.separator");

    /**
     * Returns the line executed that caused the given throwable.
     *
     * <p>
     * This is a pass-through to the overload using the given throwable, and its class.
     *
     * <p>
     * Calling with a throwable and PreparedStatement.class would yield:
     * <br/> &nbsp; <em>com.mysql.jdbc.MysqlIO.checkErrorPacket(MysqlIO.java:1997)</em> for:
     *
     * <pre>
     *  java.sql.SQLException: Some SQL Exception Message Goes Here:
     *      <strong>at com.mysql.jdbc.MysqlIO.checkErrorPacket(MysqlIO.java:1997)</strong>
     *      at com.mysql.jdbc.MysqlIO.sendCommand(MysqlIO.java:1167)
     *      at com.mysql.jdbc.MysqlIO.sqlQueryDirect(MysqlIO.java:1278)
     *      at com.mysql.jdbc.Connection.execSQL(Connection.java:2247)
     *      at com.mysql.jdbc.PreparedStatement.executeUpdate(PreparedStatement.java:1772)
     *      at com.mysql.jdbc.PreparedStatement.executeUpdate(PreparedStatement.java:1619)
     *      at com.abc.database.jdbc.PreparedStatement.executeUpdate(PreparedStatement.java:80)
     *      at com.abc.database.jdbc.PreparedStatement.executeUpdateIgnoreDuplicates(PreparedStatement.java:103)
     *      at SomeMain.statementLogging(SomeMain.java:99)
     *      at SomeMain.main(SomeMain.java:21)
     * </pre>
     *
     * @param t The root cause of the problem.
     *
     * @return The next line in the given stack trace after the "marker" class.
     *
     * @see #getSource(Throwable, Class)
     */
    public static String getSource(final Throwable t)
    {
        return(getSource(t, t.getClass()));
    }

    /**
     * Returns the line executed that caused the given throwable, but it will skip down the stack
     * trace and ignore everything until after the last occurrence of the given class.
     *
     * <p>
     * This can be useful if you know there is going to be a lot of "clutter" between the start of the stack trace and
     * the actual line of code that you care about.  An example of this is when a SQL exception happens there will be
     * many lines of JDBC driver code between the start of the trace and the first line of company code.  In this case,
     * you could pass in the (prepared/callable) statement class to get the line that called them (most likely a "facade" call).
     *
     * <p>
     * Calling with a throwable and PreparedStatement.class would yeild:
     * <br/> &nbsp; <em>SomeMain.statementLogging(SomeMain.java:99)</em> for:
     *
     * <pre>
     *  java.sql.SQLException: Some SQL Exception Message Goes Here:
     *      at com.mysql.jdbc.MysqlIO.checkErrorPacket(MysqlIO.java:1997)
     *      at com.mysql.jdbc.MysqlIO.sendCommand(MysqlIO.java:1167)
     *      at com.mysql.jdbc.MysqlIO.sqlQueryDirect(MysqlIO.java:1278)
     *      at com.mysql.jdbc.Connection.execSQL(Connection.java:2247)
     *      at com.mysql.jdbc.PreparedStatement.executeUpdate(PreparedStatement.java:1772)
     *      at com.mysql.jdbc.PreparedStatement.executeUpdate(PreparedStatement.java:1619)
     *      at com.abc.database.jdbc.PreparedStatement.executeUpdate(PreparedStatement.java:80)
     *      at com.abc.database.jdbc.PreparedStatement.executeUpdateIgnoreDuplicates(PreparedStatement.java:103)
     *      <strong>at SomeMain.statementLogging(SomeMain.java:99)</strong>
     *      at SomeMain.main(SomeMain.java:21)
     * </pre>
     *
     * @param t         The root cause of the problem.
     * @param lastClass Ignore all lines in the stack trace until after you reach this class name.
     *
     * @return The next line in the given stack trace after the "marker" class.
     */
    public static String getSource(final Throwable t, final Class<?> lastClass)
    {

        String className = lastClass.getName();
        className = className.substring(1 + className.lastIndexOf((int) '.'));

        final String stackTrace = getStackTrace(t);

        String source = "";

        try
        {
            final String openDelimiter  = "at ";
            final String closeDelimiter = ")";

            final int indexOfName  = stackTrace.lastIndexOf(className);
            final int indexOfOpen  = stackTrace.indexOf(openDelimiter, indexOfName);
            final int indexOfClose = stackTrace.indexOf(closeDelimiter, indexOfOpen);

            source = stackTrace.substring(indexOfOpen  + openDelimiter.length(),
                                          indexOfClose + closeDelimiter.length());
        }
        catch(IndexOutOfBoundsException ioobe) { }

        return(source);
    }

    /**
     * Returns the string representation for the given throwable's stack trace.
     *
     * @param t The root cause of the problem
     *
     * @return The string representation of the stack trace for the given cause.
     */
    public static String getStackTrace(final Throwable t)
    {
        final StringWriter writer = new StringWriter();
        t.printStackTrace(new PrintWriter(writer));

        return(writer.toString());
    }

    /**
     * Returns only the actual name for the class with the package information removed.
     *
     * <p>
     * If the given parameter is an inner class, it will return "Outer$Inner".
     *
     * @param someClass The class name to parse.
     *
     * @return The name of the class, or empty string if null was supplied.
     */
    public static String getClassName(final Class<?> someClass)
    {
        String returnValue = "";

        if(someClass != null)
        {
            final String fullClassName = someClass.getName();
            final int    lastDot       = fullClassName.lastIndexOf((int) '.');
            returnValue = fullClassName.substring(lastDot + 1);
        }

        return(returnValue);
    }

    /**
     * Used to guarantee that the returned string is never <code>null</code>.  If
     * <code>null</code> was given, the empty string is returned.
     *
     * @param value The string to <code>null</code>-check.
     *
     * @return A non-<code>null</code> string.
     */
    public static String nullToEmpty(final String value)
    {
        return(value == null ? "" : value);
    }

    /**
     * Returns <code>true</code> if the passed in value is non-null and is equal
     * to "1", "y", "yes", "t", or "true" after trimming and ignoring case.
     *
     * @param value The string to test.
     *
     * @return true if the String is a recognized representation of truth
     *
     * @see #formatTrueFalse(boolean)
     * @see #formatYesNo(boolean)
     */
    public static boolean isTrue(final String value)
    {
        boolean result = false;

        if(value != null)
        {
            final String trimmedValue  = value.trim().toLowerCase();

            result = "1".equals(trimmedValue)    ||
                     "true".equals(trimmedValue) || "t".equals(trimmedValue) ||
                     "yes".equals(trimmedValue)  || "y".equals(trimmedValue);
        }

        return(result);
    }

    /**
     * Will return either the value <code>"TRUE"</code> or <code>"FALSE"</code> depending on the given boolean.
     *
     * @param value The boolean value to format.
     *
     * @return The appropriate string representation of the given boolean.
     *
     * @see #isTrue(String)
     * @see #formatYesNo(boolean)
     */
    public static String formatTrueFalse(final boolean value)
    {
        return(value ? "TRUE" : "FALSE");
    }

    /**
     * Will return either the value <code>"YES"</code> or <code>"NO"</code> depending on the given boolean.
     *
     * @param value The boolean value to format.
     *
     * @return The appropriate string representation of the given boolean.
     *
     * @see #isTrue(String)
     * @see #formatTrueFalse(boolean)
     */
    public static String formatYesNo(final boolean value)
    {
        return(value ? "YES" : "NO");
    }

    /**
     * Creates a single-word string for the currently host operating system.
     *
     * @return Which operating system is running?
     */
    public static String getOperatingSystem()
    {
        final String osType    = Constants.OS_NAME;                    // Example: "Linux" or "Windows XP"
        final String osPartial = osType.toLowerCase().split("[ ]")[0]; // Example: "linux" or "windows"

        return(osPartial);
    }
}
