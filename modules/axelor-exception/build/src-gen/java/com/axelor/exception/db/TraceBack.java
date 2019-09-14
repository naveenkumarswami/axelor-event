package com.axelor.exception.db;

import java.time.ZonedDateTime;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axelor.auth.db.AuditableModel;
import com.axelor.auth.db.User;
import com.axelor.db.annotations.NameColumn;
import com.axelor.db.annotations.VirtualColumn;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "EXCEPTION_TRACE_BACK", indexes = { @Index(columnList = "internal_user"), @Index(columnList = "name") })
public class TraceBack extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EXCEPTION_TRACE_BACK_SEQ")
	@SequenceGenerator(name = "EXCEPTION_TRACE_BACK_SEQ", sequenceName = "EXCEPTION_TRACE_BACK_SEQ", allocationSize = 1)
	private Long id;

	@Widget(title = "Anomaly")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	@NotNull
	private String exception;

	@Widget(title = "Type", selection = "trace.back.type.select")
	private Integer typeSelect = 0;

	@Widget(title = "Category", selection = "trace.back.category.select")
	private Integer categorySelect = 0;

	@Widget(title = "Origin", selection = "trace.back.origin.select")
	private String origin;

	@Widget(title = "Date")
	@NotNull
	@Column(name = "date_val")
	private ZonedDateTime date;

	@Widget(title = "User")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private User internalUser;

	@Widget(title = "Error")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String error;

	@Widget(title = "Cause")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String cause;

	@Widget(title = "Message")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String message;

	@Widget(title = "Trace")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String trace;

	@Widget(title = "Batch")
	private Long batchId = 0L;

	@Widget(title = "Reference")
	private String ref;

	@Widget(title = "Reference ID")
	@Column(nullable = true)
	private Long refId;

	@Widget(search = { "id", "date" })
	@NameColumn
	@VirtualColumn
	@Access(AccessType.PROPERTY)
	private String name;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public TraceBack() {
	}

	public TraceBack(String name) {
		this.name = name;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public Integer getTypeSelect() {
		return typeSelect == null ? 0 : typeSelect;
	}

	public void setTypeSelect(Integer typeSelect) {
		this.typeSelect = typeSelect;
	}

	public Integer getCategorySelect() {
		return categorySelect == null ? 0 : categorySelect;
	}

	public void setCategorySelect(Integer categorySelect) {
		this.categorySelect = categorySelect;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public ZonedDateTime getDate() {
		return date;
	}

	public void setDate(ZonedDateTime date) {
		this.date = date;
	}

	public User getInternalUser() {
		return internalUser;
	}

	public void setInternalUser(User internalUser) {
		this.internalUser = internalUser;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTrace() {
		return trace;
	}

	public void setTrace(String trace) {
		this.trace = trace;
	}

	public Long getBatchId() {
		return batchId == null ? 0L : batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
	}

	public String getName() {
		try {
			name = computeName();
		} catch (NullPointerException e) {
			Logger logger = LoggerFactory.getLogger(getClass());
			logger.error("NPE in function field: getName()", e);
		}
		return name;
	}

	protected String computeName() {
		return this.id + " : " + this.date;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAttrs() {
		return attrs;
	}

	public void setAttrs(String attrs) {
		this.attrs = attrs;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (!(obj instanceof TraceBack)) return false;

		final TraceBack other = (TraceBack) obj;
		if (this.getId() != null || other.getId() != null) {
			return Objects.equals(this.getId(), other.getId());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("id", getId())
			.add("typeSelect", getTypeSelect())
			.add("categorySelect", getCategorySelect())
			.add("origin", getOrigin())
			.add("date", getDate())
			.add("batchId", getBatchId())
			.add("ref", getRef())
			.add("refId", getRefId())
			.omitNullValues()
			.toString();
	}
}
