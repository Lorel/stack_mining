/**
 * Copyright (c) 2010 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Johannes Lerch - initial API and implementation.
 */
package de.tud.stacktraces.evaluation.datastruct;

import java.util.List;

public class StackTrace extends BugDescription {

	private String exceptionType;
	private String message;

	private List<StackTraceElement> elements;

	private StackTrace causedBy;

	private int number;
	private int commentIndex;

	protected StackTrace() {
		// Only for deserialization via gson
	}

	public StackTrace(final String exceptionType, final String message,
			final List<StackTraceElement> elements) {
		this(exceptionType, message, elements, null);
	}

	public StackTrace(final String exceptionType, final String message,
			final List<StackTraceElement> elements, final StackTrace causedBy) {

		this.exceptionType = exceptionType;
		this.message = message;
		this.elements = elements;
		this.causedBy = causedBy;
	}

	@Override
	public String getShortIdentifier() {
		return String.format("[Bug: %s; Duplicate: %s; Number: %d]", bugId,
				duplicateId, number);
	}

	public String getExceptionType() {
		return exceptionType;
	}

	public String getMessage() {
		return message;
	}

	public List<StackTraceElement> getElements() {
		return elements;
	}

	public StackTrace getCausedBy() {
		return causedBy;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(getExceptionType());
		if (getMessage() != null) {
			builder.append(": ");
			builder.append(getMessage());
		}

		for (final StackTraceElement element : getElements()) {
			builder.append("\n\t ");
			builder.append(element.toString());
		}

		if (causedBy != null) {
			builder.append("\nCaused by: ");
			builder.append(causedBy.toString());
		}

		return builder.toString();
	}

	public boolean isValid() {
		if (!(exceptionType != null && elements != null)) {
			return false;
		}
		for (final StackTraceElement element : elements) {
			if (element.getMethod() == null) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((causedBy == null) ? 0 : causedBy.hashCode());
		result = prime * result
				+ ((elements == null) ? 0 : elements.hashCode());
		result = prime * result
				+ ((exceptionType == null) ? 0 : exceptionType.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StackTrace other = (StackTrace) obj;
		if (causedBy == null) {
			if (other.causedBy != null)
				return false;
		} else if (!causedBy.equals(other.causedBy))
			return false;
		if (elements == null) {
			if (other.elements != null)
				return false;
		} else if (!elements.equals(other.elements))
			return false;
		if (exceptionType == null) {
			if (other.exceptionType != null)
				return false;
		} else if (!exceptionType.equals(other.exceptionType))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		return true;
	}

	public void setNumber(final int i) {
		this.number = i;
	}

	public void setCommentIndex(int commentIndex) {
		this.commentIndex = commentIndex;
	}

	public int getCommentIndex() {
		return commentIndex;
	}

	public int getNumber() {
		return number;
	}
}
