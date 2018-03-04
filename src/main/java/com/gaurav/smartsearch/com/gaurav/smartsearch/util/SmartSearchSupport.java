package com.gaurav.smartsearch.com.gaurav.smartsearch.util;

import java.util.ArrayList;

/**
 * Created by gaurav on 04/03/18.
 */

public class SmartSearchSupport {

    /**
     * Levenshtein Distance Algo
     *
     * @param s
     * @param t
     * @return
     */
    public static int getLevenshteinDistance(CharSequence s, CharSequence t) {
        if (s != null && t != null) {
            int n = s.length();
            int m = t.length();
            if (n == 0) {
                return m;
            } else if (m == 0) {
                return n;
            } else {
                if (n > m) {
                    CharSequence p = s;
                    s = t;
                    t = p;
                    n = m;
                    m = p.length();
                }

                int[] var11 = new int[n + 1];
                int[] d = new int[n + 1];

                int i;
                for (i = 0; i <= n; var11[i] = i++) {
                    ;
                }

                for (int j = 1; j <= m; ++j) {
                    char t_j = t.charAt(j - 1);
                    d[0] = j;

                    for (i = 1; i <= n; ++i) {
                        int cost = s.charAt(i - 1) == t_j ? 0 : 1;
                        d[i] = Math.min(Math.min(d[i - 1] + 1, var11[i] + 1), var11[i - 1] + cost);
                    }

                    int[] _d = var11;
                    var11 = d;
                    d = _d;
                }

                return var11[n];
            }
        } else {
            throw new IllegalArgumentException("Strings must not be null");
        }
    }

    public static String getDbStringForColumn(ArrayList<String> colStrings) {
        String returnStr = "";
        for (String col : colStrings) {
            if (colStrings.indexOf(col) == colStrings.size() - 1) {
                returnStr += col;

            } else {
                returnStr += col + ",";

            }

        }
        return returnStr;
    }
}
