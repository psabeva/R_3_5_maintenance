package org.eclipse.jpt.core.internal.jpa1.context;

import org.eclipse.jpt.core.internal.jpa1.context.BaseColumnTableValidator.TableDescriptionProvider;
import org.eclipse.jpt.core.internal.validation.JpaValidationDescriptionMessages;

public class CollectionTableTableDescriptionProvider implements TableDescriptionProvider
{
	public String getColumnTableDescriptionMessage() {
		return JpaValidationDescriptionMessages.DOES_NOT_MATCH_COLLECTION_TABLE;
	}

}