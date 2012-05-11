/*******************************************************************************
 * Copyright (c) 2009, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.java;

import java.util.Iterator;
import java.util.ListIterator;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.MappingKeys;
import org.eclipse.jpt.core.context.BaseColumn;
import org.eclipse.jpt.core.context.BaseJoinColumn;
import org.eclipse.jpt.core.context.Entity;
import org.eclipse.jpt.core.context.NamedColumn;
import org.eclipse.jpt.core.context.PersistentAttribute;
import org.eclipse.jpt.core.context.RelationshipMapping;
import org.eclipse.jpt.core.context.TypeMapping;
import org.eclipse.jpt.core.context.java.JavaAssociationOverride;
import org.eclipse.jpt.core.context.java.JavaAssociationOverrideRelationshipReference;
import org.eclipse.jpt.core.context.java.JavaJoinColumn;
import org.eclipse.jpt.core.context.java.JavaJoinColumnInAssociationOverrideJoiningStrategy;
import org.eclipse.jpt.core.context.java.JavaJoinColumn.Owner;
import org.eclipse.jpt.core.internal.resource.java.NullJoinColumnAnnotation;
import org.eclipse.jpt.core.resource.java.AssociationOverrideAnnotation;
import org.eclipse.jpt.core.resource.java.JoinColumnAnnotation;
import org.eclipse.jpt.core.utility.TextRange;
import org.eclipse.jpt.db.Table;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;

public class GenericJavaJoinColumnInAssociationOverrideJoiningStrategy 
	extends AbstractJavaJoinColumnJoiningStrategy
	implements JavaJoinColumnInAssociationOverrideJoiningStrategy
{
	protected transient AssociationOverrideAnnotation associationOverrideAnnotation;
	
	public GenericJavaJoinColumnInAssociationOverrideJoiningStrategy(JavaAssociationOverrideRelationshipReference parent) {
		super(parent);
	}
	
	@Override
	protected Owner buildJoinColumnOwner() {
		return new JoinColumnOwner();
	}
	
	public boolean isTargetForeignKeyRelationship() {
		RelationshipMapping relationshipMapping = getRelationshipMapping();
		if (relationshipMapping != null) {
			return relationshipMapping.getRelationshipReference().isTargetForeignKeyRelationship();
		}
		return false;
	}

	public TypeMapping getRelationshipSource() {
		if (isTargetForeignKeyRelationship()) {
			return getRelationshipMapping().getResolvedTargetEntity();
		}
		return getAssociationOverrideOwner().getTypeMapping();
	}

	public TypeMapping getRelationshipTarget() {
		if (isTargetForeignKeyRelationship()) {
			return getAssociationOverrideOwner().getTypeMapping();
		}
		RelationshipMapping relationshipMapping = getRelationshipMapping();
		return relationshipMapping == null ? null : relationshipMapping.getResolvedTargetEntity();
	}

	protected Entity getRelationshipTargetEntity() {
		TypeMapping relationshipTarget = getRelationshipTarget();
		return (relationshipTarget != null) && (relationshipTarget.getKey() == MappingKeys.ENTITY_TYPE_MAPPING_KEY) ? (Entity) relationshipTarget : null;
	}

	@Override
	public RelationshipMapping getRelationshipMapping() {
		return getAssociationOverrideOwner().getRelationshipMapping(getAttributeName());
	}
	
	protected String getAttributeName() {
		return this.getAssociationOverride().getName();
	}

	@Override
	public String getTableName() {
		if (isTargetForeignKeyRelationship()) {
			return super.getTableName();
		}
		return getAssociationOverrideOwner().getDefaultTableName();
	}

	@Override
	public boolean tableNameIsInvalid(String tableName) {
		if (isTargetForeignKeyRelationship()) {
			return super.tableNameIsInvalid(tableName);
		}
		return getAssociationOverrideOwner().tableNameIsInvalid(tableName);
	}

	@Override
	public Iterator<String> candidateTableNames() {
		if (isTargetForeignKeyRelationship()) {
			return super.candidateTableNames();
		}
		return getAssociationOverrideOwner().candidateTableNames();
	}

	@Override
	public Table getDbTable(String tableName) {
		if (isTargetForeignKeyRelationship()) {
			return super.getDbTable(tableName);
		}
		return getAssociationOverrideOwner().getDbTable(tableName);
	}

	public String getColumnTableNotValidDescription() {
		return null;
	}

	public boolean isOverridableAssociation() {
		return false;
	}
	
	protected JavaAssociationOverride getAssociationOverride() {
		return this.getRelationshipReference().getAssociationOverride();
	}
	
	protected JavaAssociationOverride.Owner getAssociationOverrideOwner() {
		return getAssociationOverride().getOwner();
	}

	@Override
	public JavaAssociationOverrideRelationshipReference getRelationshipReference() {
		return (JavaAssociationOverrideRelationshipReference) super.getRelationshipReference();
	}
	
	@Override
	protected ListIterator<JoinColumnAnnotation> joinColumnAnnotations() {
		return this.associationOverrideAnnotation.joinColumns();
	}
	
	@Override
	protected JoinColumnAnnotation buildNullJoinColumnAnnotation() {
		return new NullJoinColumnAnnotation(this.associationOverrideAnnotation);
	}
		
	@Override
	protected JoinColumnAnnotation addAnnotation(int index) {
		return this.associationOverrideAnnotation.addJoinColumn(index);
	}
	
	@Override
	protected void removeAnnotation(int index) {
		this.associationOverrideAnnotation.removeJoinColumn(index);
	}
	
	@Override
	protected void moveAnnotation(int targetIndex, int sourceIndex) {
		this.associationOverrideAnnotation.moveJoinColumn(targetIndex, sourceIndex);
	}
	
	public void initialize(AssociationOverrideAnnotation associationOverride) {
		this.associationOverrideAnnotation = associationOverride;
		super.initialize();
	}
	
	public void update(AssociationOverrideAnnotation associationOverride) {
		this.associationOverrideAnnotation = associationOverride;
		super.update();
	}
	
	public TextRange getValidationTextRange(CompilationUnit astRoot) {
		return this.getRelationshipReference().getValidationTextRange(astRoot);
	}
	
//	 ********** join column owner adapter **********
	
	protected class JoinColumnOwner
		implements JavaJoinColumn.Owner
	{

		protected JoinColumnOwner() {
			super();
		}

		public String getDefaultTableName() {
			return GenericJavaJoinColumnInAssociationOverrideJoiningStrategy.this.getTableName();
		}
		
		public String getDefaultColumnName() {
			//built in MappingTools.buildJoinColumnDefaultName()
			return null;
		}

		public String getAttributeName() {
			return GenericJavaJoinColumnInAssociationOverrideJoiningStrategy.this.getAttributeName();
		}
		
		public PersistentAttribute getPersistentAttribute() {
			RelationshipMapping relationshipMapping = getRelationshipMapping();
			return relationshipMapping == null ? null : relationshipMapping.getPersistentAttribute();
		}

		public TypeMapping getTypeMapping() {
			return GenericJavaJoinColumnInAssociationOverrideJoiningStrategy.this.getRelationshipSource();
		}
		
		public Entity getRelationshipTarget() {
			return GenericJavaJoinColumnInAssociationOverrideJoiningStrategy.this.getRelationshipTargetEntity();
		}

		public boolean tableNameIsInvalid(String tableName) {
			return GenericJavaJoinColumnInAssociationOverrideJoiningStrategy.this.tableNameIsInvalid(tableName);
		}

		public Iterator<String> candidateTableNames() {
			return GenericJavaJoinColumnInAssociationOverrideJoiningStrategy.this.candidateTableNames();
		}

		public Table getDbTable(String tableName) {
			return GenericJavaJoinColumnInAssociationOverrideJoiningStrategy.this.getDbTable(tableName);
		}

		public Table getReferencedColumnDbTable() {
			return GenericJavaJoinColumnInAssociationOverrideJoiningStrategy.this.getReferencedColumnDbTable();
		}
		
		public boolean isVirtual(BaseJoinColumn joinColumn) {
			return false;
		}

		public int joinColumnsSize() {
			return GenericJavaJoinColumnInAssociationOverrideJoiningStrategy.this.joinColumnsSize();
		}

		public TextRange getValidationTextRange(CompilationUnit astRoot) {
			return GenericJavaJoinColumnInAssociationOverrideJoiningStrategy.this.getValidationTextRange(astRoot);
		}

		public IMessage buildUnresolvedNameMessage(NamedColumn column, TextRange textRange) {
			return getAssociationOverrideOwner().buildColumnUnresolvedNameMessage(getAssociationOverride(), column, textRange);
		}

		public IMessage buildTableNotValidMessage(BaseColumn column, TextRange textRange) {
			return getAssociationOverrideOwner().buildColumnTableNotValidMessage(getAssociationOverride(), column, textRange);
		}

		public IMessage buildUnresolvedReferencedColumnNameMessage(BaseJoinColumn column, TextRange textRange) {
			return getAssociationOverrideOwner().buildColumnUnresolvedReferencedColumnNameMessage(getAssociationOverride(), column, textRange);
		}

		public IMessage buildUnspecifiedNameMultipleJoinColumnsMessage(BaseJoinColumn column, TextRange textRange) {
			return getAssociationOverrideOwner().buildUnspecifiedNameMultipleJoinColumnsMessage(getAssociationOverride(), column, textRange);
		}

		public IMessage buildUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage(BaseJoinColumn column, TextRange textRange) {
			return getAssociationOverrideOwner().buildUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage(getAssociationOverride(), column, textRange);
		}
	}

}
