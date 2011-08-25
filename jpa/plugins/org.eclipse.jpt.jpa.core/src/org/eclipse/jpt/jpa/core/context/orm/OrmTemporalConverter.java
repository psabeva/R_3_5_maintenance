/*******************************************************************************
 * Copyright (c) 2010, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.context.orm;

import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.jpa.core.context.Converter;
import org.eclipse.jpt.jpa.core.context.TemporalConverter;
import org.eclipse.jpt.jpa.core.internal.context.JptValidator;
import org.eclipse.jpt.jpa.core.internal.jpa1.context.ConverterTextRangeResolver;
import org.eclipse.jpt.jpa.core.internal.jpa2.context.orm.OrmElementCollectionTemporalConverterValidator;
import org.eclipse.jpt.jpa.core.internal.jpa2.context.orm.OrmTemporalConverterValidator;
import org.eclipse.jpt.jpa.core.resource.orm.TemporalType;
import org.eclipse.jpt.jpa.core.resource.orm.XmlAttributeMapping;
import org.eclipse.jpt.jpa.core.resource.orm.XmlConvertibleMapping;

/**
 * <code>orm.xml</code> temporal converter
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public interface OrmTemporalConverter
	extends TemporalConverter, OrmConverter
{
	// ********** owner **********

	/**
	 */
	public interface Owner 
		extends OrmConverter.Owner
	{
		TemporalType getXmlTemporalType();

		void setXmlTemporalType(TemporalType temporalType);

		TextRange getTemporalTextRange();
	}

	// ********** adapter **********
	public static class BasicAdapter extends AbstractAdapter
	{
		private static final Adapter INSTANCE = new BasicAdapter();
		public static Adapter instance() {
			return INSTANCE;
		}
	
		private BasicAdapter() {
			super();
		}

		@Override
		protected Owner buildOwner(final XmlConvertibleMapping mapping) {
			return new OrmTemporalConverter.Owner() {
				public void setXmlTemporalType(TemporalType temporalType) {
					mapping.setTemporal(temporalType);
				}
				public TemporalType getXmlTemporalType() {
					return mapping.getTemporal();
				}
				public TextRange getTemporalTextRange() {
					return mapping.getTemporalTextRange();
				}
				public JptValidator buildValidator(Converter converter, ConverterTextRangeResolver textRangeResolver) {
					return new OrmTemporalConverterValidator((TemporalConverter) converter, textRangeResolver);
				}
			};
		}
	}

	public static class ElementCollectionAdapter extends AbstractAdapter
	{
		private static final Adapter INSTANCE = new ElementCollectionAdapter();
		public static Adapter instance() {
			return INSTANCE;
		}
	
		private ElementCollectionAdapter() {
			super();
		}

		@Override
		protected Owner buildOwner(final XmlConvertibleMapping mapping) {
			return new OrmTemporalConverter.Owner() {
				public void setXmlTemporalType(TemporalType temporalType) {
					mapping.setTemporal(temporalType);
				}
				public TemporalType getXmlTemporalType() {
					return mapping.getTemporal();
				}
				public TextRange getTemporalTextRange() {
					return mapping.getTemporalTextRange();
				}
				public JptValidator buildValidator(Converter converter, ConverterTextRangeResolver textRangeResolver) {
					return new OrmElementCollectionTemporalConverterValidator((TemporalConverter) converter, textRangeResolver);
				}
			};
		}
	}

	abstract static class AbstractAdapter
		implements OrmConverter.Adapter
	{
		AbstractAdapter() {
			super();
		}

		public Class<? extends Converter> getConverterType() {
			return TemporalConverter.class;
		}

		public OrmConverter buildConverter(OrmAttributeMapping parent, OrmXmlContextNodeFactory factory) {
			XmlConvertibleMapping xmlMapping = (XmlConvertibleMapping) parent.getXmlAttributeMapping();
			return (xmlMapping.getTemporal() == null) ? null : factory.buildOrmTemporalConverter(parent, this.buildOwner(xmlMapping));
		}

		protected abstract OrmTemporalConverter.Owner buildOwner(final XmlConvertibleMapping mapping); 


		public boolean isActive(XmlAttributeMapping xmlMapping) {
			return ((XmlConvertibleMapping) xmlMapping).getTemporal() != null;
		}

		public OrmConverter buildNewConverter(OrmAttributeMapping parent, OrmXmlContextNodeFactory factory) {
			return factory.buildOrmTemporalConverter(parent, this.buildOwner((XmlConvertibleMapping) parent.getXmlAttributeMapping()));
		}

		public void clearXmlValue(XmlAttributeMapping xmlMapping) {
			((XmlConvertibleMapping) xmlMapping).setTemporal(null);
		}
	}
}