package com.axelor.exception.db.repo;

import com.axelor.db.JpaRepository;
import com.axelor.db.Query;
import com.axelor.exception.db.TraceBack;

public class TraceBackRepository extends JpaRepository<TraceBack> {

	public TraceBackRepository() {
		super(TraceBack.class);
	}

	public TraceBack findByName(String name) {
		return Query.of(TraceBack.class)
				.filter("self.name = :name")
				.bind("name", name)
				.fetchOne();
	}

	// TYPE SELECT
	/**
	 * @deprecated With current implementation of TraceBackService, all exceptions of type
	 *	 AxelorException are flagged as functional exceptions. However there are many cases of
	 *	 misuses of AxelorException due to confusion between TYPE and CATEGORY.
	 */
	@Deprecated
	public static final int TYPE_TECHNICAL = 0;
	/**
	 * @deprecated With current implementation of TraceBackService, all exceptions of type
	 *	 AxelorException are flagged as functional exceptions. However there are many cases of
	 *	 misuses of AxelorException due to confusion between TYPE and CATEGORY.
	 */
	@Deprecated
	public static final int TYPE_FUNCTIONNAL = 1;

	// CATEGORY SELECT
	public static final int CATEGORY_MISSING_FIELD = 1;
	public static final int CATEGORY_NO_UNIQUE_KEY = 2;
	public static final int CATEGORY_NO_VALUE = 3;
	public static final int CATEGORY_CONFIGURATION_ERROR = 4;
	public static final int CATEGORY_INCONSISTENCY = 5;
}

