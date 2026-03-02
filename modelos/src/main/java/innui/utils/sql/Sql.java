package innui.utils.sql;

import innui.utils.bundles.Bundles;
import java.util.regex.Pattern;

public class Sql {
    /**
     * Guards against SQL injection by rejecting values that contain dangerous
     * SQL keywords, comment sequences, statement terminators, or quote characters.
     *
     * <p>
     * This is a defence-in-depth safeguard. The primary protection should always
     * be parameterised queries / JPA criteria; use this when raw string fragments
     * must be concatenated into a WHERE clause.
     * </p>
     *
     * @param value the user-supplied string to validate
     * @return the original value, trimmed and scaped, if it is considered safe
     * @throws IllegalArgumentException if the value looks like a SQL injection
     *                                  attempt
     */
    public static String sanitizeSqlValue(String value) {
        Bundles bundle = new Bundles();
        String trimmed = value.trim();
        // Single quotes are scaped, always
        trimmed = trimmed.replace("'", "''");
        // Reject empty strings – they have no semantic value in a WHERE clause
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(bundle.getMessage("error.sql.empty.value"));
        }

        // Reject double quotes – they can escape string delimiters
        if (trimmed.contains("\"")) {
            throw new IllegalArgumentException(
                    bundle.getMessage("error.sql.injection", value));
        }

        // Reject SQL comment sequences
        if (trimmed.contains("--") || trimmed.contains("/*") || trimmed.contains("*/")) {
            throw new IllegalArgumentException(
                    bundle.getMessage("error.sql.injection", value));
        }

        // Reject semicolons – they allow stacking multiple statements
        if (trimmed.contains(";")) {
            throw new IllegalArgumentException(
                    bundle.getMessage("error.sql.injection", value));
        }

        // Reject dangerous SQL keywords (case-insensitive, word-boundary aware)
        Pattern sqlKeywords = Pattern.compile(
                "(?i)\\b(SELECT|INSERT|UPDATE|DELETE|DROP|CREATE|ALTER|TRUNCATE"
                        + "|EXEC|EXECUTE|UNION|MERGE|CALL|GRANT|REVOKE|LOAD|OUTFILE"
                        + "|INTO|FROM|WHERE|OR|AND|HAVING|GROUP|ORDER|LIMIT)\\b");
        if (sqlKeywords.matcher(trimmed).find()) {
            throw new IllegalArgumentException(
                    bundle.getMessage("error.sql.injection", value));
        }

        return trimmed;
    }

    /**
     * Creates an AND WHERE clause for a given field and data.
     * 
     * @param where    The current WHERE clause.
     * @param field    The field to apply the condition to.
     * @param data     The data to apply the condition to.
     * @param isString Whether the data is a string.
     * @return The updated WHERE clause.
     */
    public static String createAndWhere(String where, String field, String data, boolean isString) {
        int pos = 0;
        boolean is_there_condition_one = false;
        String prefix = "", sufix = "";
        String operator;
        String data_two = "";
        data = data.trim();
        data = sanitizeSqlValue(data);
        if (isString) {
            prefix = "'";
            sufix = "'";
        }
        pos = data.indexOf("\\,");
        if (pos > 0) {
            is_there_condition_one = true;
            data_two = data.substring(pos + 2).trim();
            data = data.substring(0, pos).trim();
        }
        if (where.isBlank() == false) {
            where = where + " AND ";
        }
        if (data.startsWith("<=") || data.startsWith(">=")) {
            operator = " " + data.substring(0, 2) + " ";
            where = where + field + operator + prefix + data.substring(2).trim() + sufix;
        } else if (data.startsWith("<") || data.startsWith(">") || data.startsWith("=")) {
            operator = " " + data.substring(0, 1) + " ";
            where = where + field + operator + prefix + data.substring(1).trim() + sufix;
        }
        if (is_there_condition_one) {
            pos = data_two.indexOf("<=");
            if (pos >= 0) {
                operator = " " + data_two.substring(pos, pos + 2) + " ";
                where = where + " AND " + field + operator + prefix + data_two.substring(pos + 2).trim() + sufix;
            } else {
                pos = data_two.indexOf(">=");
                if (pos >= 0) {
                    operator = " " + data_two.substring(pos, pos + 2) + " ";
                    where = where + " AND " + field + operator + prefix + data_two.substring(pos + 2).trim() + sufix;
                } else {
                    pos = data_two.indexOf("<");
                    if (pos >= 0) {
                        operator = " " + data_two.substring(pos, pos + 1) + " ";
                        where = where + " AND " + field + operator + prefix + data_two.substring(pos + 1).trim()
                                + sufix;
                    } else {
                        pos = data_two.indexOf(">");
                        if (pos >= 0) {
                            operator = " " + data_two.substring(pos, pos + 1) + " ";
                            where = where + " AND " + field + operator + prefix + data_two.substring(pos + 1).trim()
                                    + sufix;
                        } else {
                            pos = data_two.indexOf("=");
                            if (pos >= 0) {
                                operator = " " + data_two.substring(pos, pos + 1) + " ";
                                where = where + " AND " + field + operator + prefix + data_two.substring(pos + 1).trim()
                                        + sufix;
                            }
                        }
                    }
                }
            }
        } else {
            where = where + field + " = " + prefix + data + sufix;
        }
        where = where.replace("\\_", " ");
        return where;
    }

}
