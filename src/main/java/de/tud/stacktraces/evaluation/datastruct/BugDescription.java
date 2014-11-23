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

import java.util.Date;

public class BugDescription {

	public String groupId;
	public String bugId;
	public String duplicateId;

	/**
	 * Creation date of bug report; In case of a stack trace this is the date
	 * the comment was written.
	 **/
	public Date date;
	public String product;
	public String component;
	public String severity;

	public BugDescription() {
	}

	public String getShortIdentifier() {
		return String.format("[Bug: %s; Duplicate: %s]", bugId, duplicateId);
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(final String groupId) {
		this.groupId = groupId;
	}

	public String getBugId() {
		return bugId;
	}

	public void setBugId(final String bugId) {
		this.bugId = bugId;
	}

	public String getDuplicateId() {
		return duplicateId;
	}

	public void setDuplicateId(final String duplicateId) {
		this.duplicateId = duplicateId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(final Date date) {
		this.date = date;
	}
}
