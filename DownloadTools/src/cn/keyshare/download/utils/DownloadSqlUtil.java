package cn.keyshare.download.utils;

import cn.keyshare.download.core.Downloads;

public class DownloadSqlUtil {

	/**
	 * Get a parameterized SQL WHERE clause to select a bunch of IDs.
	 */
	public static String getWhereClauseForIds(long[] ids) {
		StringBuilder whereClause = new StringBuilder();
		whereClause.append("(");
		for (int i = 0; i < ids.length; i++) {
			if (i > 0) {
				whereClause.append("OR ");
			}
			whereClause.append(Downloads._ID);
			whereClause.append(" = ? ");
		}
		whereClause.append(")");
		return whereClause.toString();
	}

	/**
	 * Get the selection args for a clause returned by
	 * {@link #getWhereClauseForIds(long[])}.
	 */
	public static String[] getWhereArgsForIds(long[] ids) {
		String[] whereArgs = new String[ids.length];
		for (int i = 0; i < ids.length; i++) {
			whereArgs[i] = Long.toString(ids[i]);
		}
		return whereArgs;
	}

}
