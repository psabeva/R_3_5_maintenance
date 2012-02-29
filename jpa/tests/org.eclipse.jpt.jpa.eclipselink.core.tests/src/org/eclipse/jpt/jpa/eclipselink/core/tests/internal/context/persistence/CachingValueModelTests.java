/*******************************************************************************
 * Copyright (c) 2008, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.core.tests.internal.context.persistence;

import org.eclipse.jpt.common.utility.internal.model.AbstractModel;
import org.eclipse.jpt.common.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.common.utility.internal.model.value.SimplePropertyValueModel;
import org.eclipse.jpt.common.utility.model.event.PropertyChangeEvent;
import org.eclipse.jpt.common.utility.model.listener.PropertyChangeListener;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.common.utility.model.value.ModifiablePropertyValueModel;
import org.eclipse.jpt.jpa.core.context.persistence.PersistenceUnitProperties;
import org.eclipse.jpt.jpa.eclipselink.core.context.persistence.CacheType;
import org.eclipse.jpt.jpa.eclipselink.core.context.persistence.Caching;
import org.eclipse.jpt.jpa.eclipselink.core.context.persistence.CachingEntity;

/**
 * CachingValueModelTests
 */
@SuppressWarnings("nls")
public class CachingValueModelTests extends EclipseLinkPersistenceUnitTestCase
{
	private Caching caching;
	private PropertyValueModel<Caching> cachingHolder;
	
	private ModifiablePropertyValueModel<CacheType> cacheTypeHolder;
	private PropertyChangeListener cacheTypeListener;
	private PropertyChangeEvent cacheTypeEvent;

	private ModifiablePropertyValueModel<Boolean> sharedCacheHolder;
	private PropertyChangeListener sharedCacheListener;
	private PropertyChangeEvent sharedCacheEvent;

	private ModifiablePropertyValueModel<CacheType> cacheTypeDefaultHolder;
	private PropertyChangeListener cacheTypeDefaultListener;
	private PropertyChangeEvent cacheTypeDefaultEvent;

	private ModifiablePropertyValueModel<Boolean> sharedCacheDefaultHolder;
	private PropertyChangeListener sharedCacheDefaultListener;
	private PropertyChangeEvent sharedCacheDefaultEvent;

	public static final String ENTITY_NAME_TEST_VALUE = "Employee";
	public static final CacheType CACHE_TYPE_TEST_VALUE = CacheType.hard_weak;
	public static final Boolean SHARED_CACHE_TEST_VALUE = Boolean.FALSE;
	public static final CacheType CACHE_TYPE_DEFAULT_TEST_VALUE = CacheType.weak;
	public static final Boolean SHARED_CACHE_DEFAULT_TEST_VALUE = Boolean.FALSE;

	public CachingValueModelTests(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.caching = this.subject.getCaching(); // Subject
		this.cachingHolder = new SimplePropertyValueModel<Caching>(this.caching);
		
		this.cacheTypeHolder = this.buildCacheTypeAA(this.cachingHolder);
		this.cacheTypeListener = this.buildCacheTypeChangeListener();
		this.cacheTypeHolder.addPropertyChangeListener(PropertyValueModel.VALUE, this.cacheTypeListener);
		this.cacheTypeEvent = null;
		
		this.sharedCacheHolder = this.buildSharedCacheAA(this.cachingHolder);
		this.sharedCacheListener = this.buildSharedCacheChangeListener();
		this.sharedCacheHolder.addPropertyChangeListener(PropertyValueModel.VALUE, this.sharedCacheListener);
		this.sharedCacheEvent = null;
		
		this.cacheTypeDefaultHolder = this.buildCacheTypeDefaultAA(this.cachingHolder);
		this.cacheTypeDefaultListener = this.buildCacheTypeDefaultChangeListener();
		this.cacheTypeDefaultHolder.addPropertyChangeListener(PropertyValueModel.VALUE, this.cacheTypeDefaultListener);
		this.cacheTypeDefaultEvent = null;
		
		this.sharedCacheDefaultHolder = this.buildSharedCacheDefaultAA(this.cachingHolder);
		this.sharedCacheDefaultListener = this.buildSharedCacheDefaultChangeListener();
		this.sharedCacheDefaultHolder.addPropertyChangeListener(PropertyValueModel.VALUE, this.sharedCacheDefaultListener);
		this.sharedCacheDefaultEvent = null;
	}

	public void testHasListeners() {
		AbstractModel subjectCaching = (AbstractModel) this.caching; // Subject
		PropertyAspectAdapter<Caching, CacheType> cacheTypeAA = 
			(PropertyAspectAdapter<Caching, CacheType>) this.cacheTypeHolder;
		assertTrue(cacheTypeAA.hasAnyPropertyChangeListeners(PropertyValueModel.VALUE));
		assertTrue(subjectCaching.hasAnyPropertyChangeListeners(Caching.CACHE_TYPE_PROPERTY));
		
		cacheTypeAA.removePropertyChangeListener(PropertyValueModel.VALUE, this.cacheTypeListener);
		assertFalse(subjectCaching.hasAnyPropertyChangeListeners(Caching.CACHE_TYPE_PROPERTY));
		assertFalse(cacheTypeAA.hasAnyPropertyChangeListeners(PropertyValueModel.VALUE));
		
		PropertyAspectAdapter<Caching, Boolean> sharedCacheAA = 
			(PropertyAspectAdapter<Caching, Boolean>) this.sharedCacheHolder;
		assertTrue(sharedCacheAA.hasAnyPropertyChangeListeners(PropertyValueModel.VALUE));
		assertTrue(subjectCaching.hasAnyPropertyChangeListeners(Caching.SHARED_CACHE_PROPERTY));
		
		sharedCacheAA.removePropertyChangeListener(PropertyValueModel.VALUE, this.sharedCacheListener);
		assertFalse(subjectCaching.hasAnyPropertyChangeListeners(Caching.SHARED_CACHE_PROPERTY));
		assertFalse(sharedCacheAA.hasAnyPropertyChangeListeners(PropertyValueModel.VALUE));
		
		PropertyAspectAdapter<Caching, CacheType> cacheTypeDefaultAA = 
			(PropertyAspectAdapter<Caching, CacheType>) this.cacheTypeDefaultHolder;
		assertTrue(cacheTypeDefaultAA.hasAnyPropertyChangeListeners(PropertyValueModel.VALUE));
		assertTrue(subjectCaching.hasAnyPropertyChangeListeners(Caching.CACHE_TYPE_DEFAULT_PROPERTY));
		
		cacheTypeDefaultAA.removePropertyChangeListener(PropertyValueModel.VALUE, this.cacheTypeDefaultListener);
		assertFalse(subjectCaching.hasAnyPropertyChangeListeners(Caching.CACHE_TYPE_DEFAULT_PROPERTY));
		assertFalse(cacheTypeDefaultAA.hasAnyPropertyChangeListeners(PropertyValueModel.VALUE));
		
		PropertyAspectAdapter<Caching, Boolean> sharedCacheDefaultAA = 
			(PropertyAspectAdapter<Caching, Boolean>) this.sharedCacheDefaultHolder;
		assertTrue(sharedCacheDefaultAA.hasAnyPropertyChangeListeners(PropertyValueModel.VALUE));
		assertTrue(subjectCaching.hasAnyPropertyChangeListeners(Caching.SHARED_CACHE_DEFAULT_PROPERTY));
		
		sharedCacheDefaultAA.removePropertyChangeListener(PropertyValueModel.VALUE, this.sharedCacheDefaultListener);
		assertFalse(subjectCaching.hasAnyPropertyChangeListeners(Caching.SHARED_CACHE_DEFAULT_PROPERTY));
		assertFalse(sharedCacheDefaultAA.hasAnyPropertyChangeListeners(PropertyValueModel.VALUE));
	}

	/**
	 * Initializes directly the PU properties before testing.
	 */
	@Override
	protected void populatePu() {
		this.persistenceUnitSetProperty(
			Caching.ECLIPSELINK_CACHE_TYPE + ENTITY_NAME_TEST_VALUE, 
			CACHE_TYPE_TEST_VALUE);
		this.persistenceUnitSetProperty(
			Caching.ECLIPSELINK_SHARED_CACHE + ENTITY_NAME_TEST_VALUE, 
			SHARED_CACHE_TEST_VALUE);
		this.persistenceUnitSetProperty(
			Caching.ECLIPSELINK_CACHE_TYPE_DEFAULT, 
			CACHE_TYPE_DEFAULT_TEST_VALUE);
		this.persistenceUnitSetProperty(
			Caching.ECLIPSELINK_CACHE_SHARED_DEFAULT, 
			SHARED_CACHE_DEFAULT_TEST_VALUE);
		return;
	}

	@Override
	protected PersistenceUnitProperties getModel() {
		return this.caching;
	}

	/** ****** CacheType ******* */
	private ModifiablePropertyValueModel<CacheType> buildCacheTypeAA(PropertyValueModel<Caching> subjectHolder) {
		return new PropertyAspectAdapter<Caching, CacheType>(subjectHolder, Caching.CACHE_TYPE_PROPERTY) {
			@Override
			protected CacheType buildValue_() {
				return this.subject.getCacheTypeOf(ENTITY_NAME_TEST_VALUE);
			}

			@Override
			protected void setValue_(CacheType enumValue) {
				this.subject.setCacheTypeOf(ENTITY_NAME_TEST_VALUE, enumValue);
			}
		};
	}

	private PropertyChangeListener buildCacheTypeChangeListener() {
		return new PropertyChangeListener() {
			public void propertyChanged(PropertyChangeEvent e) {
				CachingValueModelTests.this.cacheTypeEvent = e;
			}
		};
	}

	/** ****** SharedCache ******* */
	private ModifiablePropertyValueModel<Boolean> buildSharedCacheAA(PropertyValueModel<Caching> subjectHolder) {
		return new PropertyAspectAdapter<Caching, Boolean>(subjectHolder, Caching.SHARED_CACHE_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				return this.subject.getSharedCacheOf(ENTITY_NAME_TEST_VALUE);
			}

			@Override
			protected void setValue_(Boolean enumValue) {
				this.subject.setSharedCacheOf(ENTITY_NAME_TEST_VALUE, enumValue);
			}
		};
	}

	private PropertyChangeListener buildSharedCacheChangeListener() {
		return new PropertyChangeListener() {
			public void propertyChanged(PropertyChangeEvent e) {
				CachingValueModelTests.this.sharedCacheEvent = e;
			}
		};
	}

	/** ****** CacheTypeDefault ******* */
	private ModifiablePropertyValueModel<CacheType> buildCacheTypeDefaultAA(PropertyValueModel<Caching> subjectHolder) {
		return new PropertyAspectAdapter<Caching, CacheType>(subjectHolder, Caching.CACHE_TYPE_DEFAULT_PROPERTY) {
			@Override
			protected CacheType buildValue_() {
				return this.subject.getCacheTypeDefault();
			}

			@Override
			protected void setValue_(CacheType enumValue) {
				this.subject.setCacheTypeDefault(enumValue);
			}
		};
	}

	private PropertyChangeListener buildCacheTypeDefaultChangeListener() {
		return new PropertyChangeListener() {
			public void propertyChanged(PropertyChangeEvent e) {
				CachingValueModelTests.this.cacheTypeDefaultEvent = e;
			}
		};
	}

	/** ****** SharedCacheDefault ******* */
	private ModifiablePropertyValueModel<Boolean> buildSharedCacheDefaultAA(PropertyValueModel<Caching> subjectHolder) {
		return new PropertyAspectAdapter<Caching, Boolean>(subjectHolder, Caching.SHARED_CACHE_DEFAULT_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				return this.subject.getSharedCacheDefault();
			}

			@Override
			protected void setValue_(Boolean enumValue) {
				this.subject.setSharedCacheDefault(enumValue);
			}
		};
	}

	private PropertyChangeListener buildSharedCacheDefaultChangeListener() {
		return new PropertyChangeListener() {
			public void propertyChanged(PropertyChangeEvent e) {
				CachingValueModelTests.this.sharedCacheDefaultEvent = e;
			}
		};
	}

	/** ****** Basic Entity's Properties Tests ******* */

	public void testClone() {
		CachingEntity entity = this.buildEntity("TestEntity", CacheType.full, 100, true);

		this.verifyClone(entity, entity.clone());
	}
	
	public void testEquals() {
		CachingEntity entity1 = this.buildEntity("TestEntityA", CacheType.full, 100, true);
		CachingEntity entity2 = this.buildEntity("TestEntityB", CacheType.full, 100, true);
		assertEquals(entity1, entity2);
		CachingEntity entity3 = this.buildEntity("TestEntityC", CacheType.full, 100, true);
		assertEquals(entity1, entity3);
		assertEquals(entity2, entity3);
	}
	
	public void testIsEmpty() {
		CachingEntity entity = this.buildEntity("TestEntity");
		assertTrue(entity.isEmpty());
		this.caching.setCacheSizeOf(entity.getName(), 100);
		assertFalse(entity.isEmpty());
	}

	private void verifyClone(CachingEntity original, CachingEntity clone) {
		assertNotSame(original, clone);
		assertEquals(original, original);
		assertEquals(original, clone);
	}

	private CachingEntity buildEntity(String name) {
		return this.caching.addEntity(name);
	}

	private CachingEntity buildEntity(String name, CacheType cacheType, Integer size, Boolean isShared) {
		CachingEntity entity = this.caching.addEntity(name);
		this.caching.setCacheTypeOf(entity.getName(), cacheType);
		this.caching.setCacheSizeOf(entity.getName(), size);
		this.caching.setSharedCacheOf(entity.getName(), isShared);
		return entity;
	}
	
	/** ****** Caching Tests ******* */
	public void testValue() {
		/** ****** CacheType - defaults for entity level caching are equal to the persistence unit settings ******* */
		this.verifyCacheTypeAAValue(CACHE_TYPE_TEST_VALUE);
		assertEquals(this.caching.getCacheTypeDefault(), this.caching.getDefaultCacheType());
		/** ****** SharedCache - defaults for entity level caching are equal to the persistence unit settings ******* */
		this.verifySharedCacheAAValue(SHARED_CACHE_TEST_VALUE);
		assertEquals(this.caching.getSharedCacheDefault(), this.caching.getDefaultSharedCache());
		/** ****** CacheTypeDefault ******* */
		this.verifyCacheTypeDefaultAAValue(CACHE_TYPE_DEFAULT_TEST_VALUE);
		assertEquals(Caching.DEFAULT_CACHE_TYPE_DEFAULT, this.caching.getDefaultCacheTypeDefault());
		/** ****** SharedCacheDefault ******* */
		this.verifySharedCacheDefaultAAValue(SHARED_CACHE_DEFAULT_TEST_VALUE);
		assertEquals(Caching.DEFAULT_SHARED_CACHE_DEFAULT, this.caching.getDefaultSharedCacheDefault());
	}

	public void testSetValue() throws Exception {
		/** ****** CacheType ******* */
		this.cacheTypeEvent = null;
		this.verifyHasListeners(this.cacheTypeHolder, PropertyValueModel.VALUE);
		CacheType newCacheType = CacheType.full;
		// Modify the property holder
		this.cacheTypeHolder.setValue(newCacheType);
		this.verifyCacheTypeAAValue(newCacheType);
		assertNotNull(this.cacheTypeEvent);
		/** ****** SharedCache ******* */
		this.sharedCacheEvent = null;
		this.verifyHasListeners(this.sharedCacheHolder, PropertyValueModel.VALUE);
		Boolean newSharedCache = !SHARED_CACHE_TEST_VALUE;
		// Modify the property holder
		this.sharedCacheHolder.setValue(newSharedCache);
		this.verifySharedCacheAAValue(newSharedCache);
		assertNotNull(this.sharedCacheEvent);
		/** ****** CacheTypeDefault ******* */
		this.cacheTypeDefaultEvent = null;
		this.verifyHasListeners(this.cacheTypeDefaultHolder, PropertyValueModel.VALUE);
		CacheType newCacheTypeDefault = CacheType.none;
		// Modify the property holder
		this.cacheTypeDefaultHolder.setValue(newCacheTypeDefault);
		this.verifyCacheTypeDefaultAAValue(newCacheTypeDefault);
		assertNotNull(this.cacheTypeDefaultEvent);
		/** ****** SharedCacheDefault ******* */
		this.sharedCacheDefaultEvent = null;
		this.verifyHasListeners(this.sharedCacheDefaultHolder, PropertyValueModel.VALUE);
		Boolean newSharedCacheDefault = !SHARED_CACHE_DEFAULT_TEST_VALUE;
		// Modify the property holder
		this.sharedCacheDefaultHolder.setValue(newSharedCacheDefault);
		this.verifySharedCacheDefaultAAValue(newSharedCacheDefault);
		assertNotNull(this.sharedCacheDefaultEvent);
	}

	public void testSetNullValue() {
		String notDeleted = "Property not deleted";
		/** ****** CacheType ******* */
		this.cacheTypeEvent = null;
		// Setting the property holder
		this.cacheTypeHolder.setValue(null);
		// testing Holder
		this.verifyCacheTypeAAValue(null);
		assertNotNull(this.cacheTypeEvent);
		// testing PU properties
		this.verifyPuHasNotProperty(Caching.ECLIPSELINK_CACHE_TYPE + ENTITY_NAME_TEST_VALUE, notDeleted);
		/** ****** SharedCache ******* */
		this.sharedCacheEvent = null;
		// Setting the property holder
		this.sharedCacheHolder.setValue(null);
		// testing Holder
		this.verifySharedCacheAAValue(null);
		assertNotNull(this.sharedCacheEvent);
		// testing PU properties
		this.verifyPuHasNotProperty(Caching.ECLIPSELINK_SHARED_CACHE + ENTITY_NAME_TEST_VALUE, notDeleted);
		/** ****** CacheTypeDefault ******* */
		this.cacheTypeDefaultEvent = null;
		// Setting the property holder
		this.cacheTypeDefaultHolder.setValue(null);
		// testing Holder
		this.verifyCacheTypeDefaultAAValue(null);
		assertNotNull(this.cacheTypeDefaultEvent);
		// testing PU properties
		this.verifyPuHasNotProperty(Caching.ECLIPSELINK_CACHE_TYPE_DEFAULT, notDeleted);
		/** ****** SharedCacheDefault ******* */
		this.sharedCacheDefaultEvent = null;
		// Setting the property holder
		this.sharedCacheDefaultHolder.setValue(null);
		// testing Holder
		this.verifySharedCacheDefaultAAValue(null);
		assertNotNull(this.sharedCacheDefaultEvent);
		// testing PU properties
		this.verifyPuHasNotProperty(Caching.ECLIPSELINK_CACHE_SHARED_DEFAULT, notDeleted);
	}

	/** ****** convenience methods ******* */
	/**
	 * Performs three value tests:<br>
	 * 1. subject value<br>
	 * 2. aspect adapter value<br>
	 * 3. persistenceUnit property value<br>
	 */
	protected void verifyCacheTypeAAValue(CacheType testValue) {
		this.verifyAAValue(
			testValue, 
			this.caching.getCacheTypeOf(ENTITY_NAME_TEST_VALUE), 
			this.cacheTypeHolder, 
			Caching.ECLIPSELINK_CACHE_TYPE + ENTITY_NAME_TEST_VALUE);
	}

	/**
	 * Performs three value tests:<br>
	 * 1. subject value<br>
	 * 2. aspect adapter value<br>
	 * 3. persistenceUnit property value<br>
	 */
	protected void verifySharedCacheAAValue(Boolean testValue) {
		this.verifyAAValue(
			testValue, 
			this.caching.getSharedCacheOf(ENTITY_NAME_TEST_VALUE), 
			this.sharedCacheHolder, 
			Caching.ECLIPSELINK_SHARED_CACHE + ENTITY_NAME_TEST_VALUE);
	}

	/**
	 * Performs three value tests:<br>
	 * 1. subject value<br>
	 * 2. aspect adapter value<br>
	 * 3. persistenceUnit property value<br>
	 */
	protected void verifyCacheTypeDefaultAAValue(CacheType testValue) {
		this.verifyAAValue(
			testValue, 
			this.caching.getCacheTypeDefault(), 
			this.cacheTypeDefaultHolder, 
			Caching.ECLIPSELINK_CACHE_TYPE_DEFAULT);
	}

	/**
	 * Performs three value tests:<br>
	 * 1. subject value<br>
	 * 2. aspect adapter value<br>
	 * 3. persistenceUnit property value<br>
	 */
	protected void verifySharedCacheDefaultAAValue(Boolean testValue) {
		this.verifyAAValue(
			testValue, 
			this.caching.getSharedCacheDefault(), 
			this.sharedCacheDefaultHolder, 
			Caching.ECLIPSELINK_CACHE_SHARED_DEFAULT);
	}


	// ********** get/set property **********
	@Override
	protected void setProperty(String propertyName, Object newValue) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Object getProperty(String propertyName) throws NoSuchFieldException {
		throw new UnsupportedOperationException();
	}
}