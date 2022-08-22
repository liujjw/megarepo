package distributedView;

import java.util.Set;

// NOTE: The following code was taken from the github repository for lab 10 and
// modified for our use.

/** Represents a QueryInfo object for converting to and from querystrings */
public class QueryInfo {
	public String query;
	public Set<String> paramNames;
	public String fooParam;

	/**
	 * Constructs a QueryInfo object from given query string and paramNames and
	 * fooParam
	 */
	public QueryInfo(String query, Set<String> paramNames, String fooParam) {
		this.query = query;
		this.paramNames = paramNames;
		this.fooParam = fooParam;
	}
}