/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.utility.tests.internal.model.value;

import java.util.Date;

import org.eclipse.jpt.utility.internal.model.AbstractModel;
import org.eclipse.jpt.utility.internal.model.event.PropertyChangeEvent;
import org.eclipse.jpt.utility.internal.model.listener.PropertyChangeListener;
import org.eclipse.jpt.utility.internal.model.value.BufferedPropertyValueModel;
import org.eclipse.jpt.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.utility.internal.model.value.PropertyValueModel;
import org.eclipse.jpt.utility.internal.model.value.SimplePropertyValueModel;
import org.eclipse.jpt.utility.internal.model.value.ValueModel;
import org.eclipse.jpt.utility.tests.internal.TestTools;

import junit.framework.TestCase;

public class BufferedPropertyValueModelTests extends TestCase {
	private Employee employee;
	private PropertyValueModel employeeHolder;
	PropertyChangeEvent employeeEvent;

	private PropertyValueModel idAdapter;
	private PropertyValueModel nameAdapter;
	private PropertyValueModel hireDateAdapter;
	PropertyChangeEvent adapterEvent;

	private BufferedPropertyValueModel.Trigger trigger;
	private PropertyValueModel bufferedIDHolder;
	private PropertyValueModel bufferedNameHolder;
	private PropertyValueModel bufferedHireDateHolder;
	PropertyChangeEvent bufferedEvent;

	public BufferedPropertyValueModelTests(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		this.employee = new Employee(17, "Freddy", new Date());
		this.employeeHolder = new SimplePropertyValueModel(this.employee);

		this.trigger = new BufferedPropertyValueModel.Trigger();

		this.idAdapter = this.buildIDAdapter(this.employeeHolder);
		this.bufferedIDHolder = new BufferedPropertyValueModel(this.idAdapter, this.trigger);

		this.nameAdapter = this.buildNameAdapter(this.employeeHolder);
		this.bufferedNameHolder = new BufferedPropertyValueModel(this.nameAdapter, this.trigger);

		this.hireDateAdapter = this.buildHireDateAdapter(this.employeeHolder);
		this.bufferedHireDateHolder = new BufferedPropertyValueModel(this.hireDateAdapter, this.trigger);
	}

	private PropertyValueModel buildIDAdapter(ValueModel eHolder) {
		return new PropertyAspectAdapter(eHolder, Employee.ID_PROPERTY) {
			@Override
			protected Object getValueFromSubject() {
				return new Integer(((Employee) this.subject).getID());
			}
			@Override
			protected void setValueOnSubject(Object value) {
				((Employee) this.subject).setID(((Integer) value).intValue());
			}
		};
	}

	private PropertyValueModel buildNameAdapter(ValueModel eHolder) {
		return new PropertyAspectAdapter(eHolder, Employee.NAME_PROPERTY) {
			@Override
			protected Object getValueFromSubject() {
				return ((Employee) this.subject).getName();
			}
			@Override
			protected void setValueOnSubject(Object value) {
				((Employee) this.subject).setName((String) value);
			}
		};
	}

	private PropertyValueModel buildHireDateAdapter(ValueModel eHolder) {
		return new PropertyAspectAdapter(eHolder, Employee.HIRE_DATE_PROPERTY) {
			@Override
			protected Object getValueFromSubject() {
				return ((Employee) this.subject).getHireDate();
			}
			@Override
			protected void setValueOnSubject(Object value) {
				((Employee) this.subject).setHireDate((Date) value);
			}
		};
	}

	@Override
	protected void tearDown() throws Exception {
		TestTools.clear(this);
		super.tearDown();
	}

	public void testValue() {
		PropertyChangeListener bufferedListener = this.buildBufferedListener();
		this.bufferedIDHolder.addPropertyChangeListener(ValueModel.VALUE, bufferedListener);
		this.bufferedNameHolder.addPropertyChangeListener(ValueModel.VALUE, bufferedListener);
		this.bufferedHireDateHolder.addPropertyChangeListener(ValueModel.VALUE, bufferedListener);

		assertEquals(new Integer(17), this.idAdapter.value());
		assertEquals(new Integer(17), this.bufferedIDHolder.value());

		assertEquals("Freddy", this.employee.getName());
		assertEquals("Freddy", this.nameAdapter.value());
		assertEquals("Freddy", this.bufferedNameHolder.value());

		Date temp = this.employee.getHireDate();
		assertEquals(temp, this.employee.getHireDate());
		assertEquals(temp, this.hireDateAdapter.value());
		assertEquals(temp, this.bufferedHireDateHolder.value());

		this.bufferedIDHolder.setValue(new Integer(323));
		assertEquals(17, this.employee.getID());
		assertEquals(new Integer(17), this.idAdapter.value());
		assertEquals(new Integer(323), this.bufferedIDHolder.value());

		this.bufferedNameHolder.setValue("Ripley");
		assertEquals("Freddy", this.employee.getName());
		assertEquals("Freddy", this.nameAdapter.value());
		assertEquals("Ripley", this.bufferedNameHolder.value());

		this.bufferedHireDateHolder.setValue(null);
		assertEquals(temp, this.employee.getHireDate());
		assertEquals(temp, this.hireDateAdapter.value());
		assertEquals(null, this.bufferedHireDateHolder.value());
	}

	public void testAccept() {
		PropertyChangeListener bufferedListener = this.buildBufferedListener();
		this.bufferedIDHolder.addPropertyChangeListener(ValueModel.VALUE, bufferedListener);
		this.bufferedNameHolder.addPropertyChangeListener(ValueModel.VALUE, bufferedListener);
		this.bufferedHireDateHolder.addPropertyChangeListener(ValueModel.VALUE, bufferedListener);

		this.bufferedIDHolder.setValue(new Integer(323));
		assertEquals(17, this.employee.getID());
		assertEquals(new Integer(17), this.idAdapter.value());
		assertEquals(new Integer(323), this.bufferedIDHolder.value());

		this.bufferedNameHolder.setValue("Ripley");
		assertEquals("Freddy", this.employee.getName());
		assertEquals("Freddy", this.nameAdapter.value());
		assertEquals("Ripley", this.bufferedNameHolder.value());

		Date temp = this.employee.getHireDate();
		this.bufferedHireDateHolder.setValue(null);
		assertEquals(temp, this.employee.getHireDate());
		assertEquals(temp, this.hireDateAdapter.value());
		assertEquals(null, this.bufferedHireDateHolder.value());

		this.trigger.accept();

		assertEquals(323, this.employee.getID());
		assertEquals(new Integer(323), this.idAdapter.value());
		assertEquals(new Integer(323), this.bufferedIDHolder.value());

		assertEquals("Ripley", this.employee.getName());
		assertEquals("Ripley", this.nameAdapter.value());
		assertEquals("Ripley", this.bufferedNameHolder.value());

		assertEquals(null, this.employee.getHireDate());
		assertEquals(null, this.hireDateAdapter.value());
		assertEquals(null, this.bufferedHireDateHolder.value());
	}

	public void testReset() {
		PropertyChangeListener bufferedListener = this.buildBufferedListener();
		this.bufferedIDHolder.addPropertyChangeListener(ValueModel.VALUE, bufferedListener);
		this.bufferedNameHolder.addPropertyChangeListener(ValueModel.VALUE, bufferedListener);
		this.bufferedHireDateHolder.addPropertyChangeListener(ValueModel.VALUE, bufferedListener);

		this.bufferedIDHolder.setValue(new Integer(323));
		assertEquals(17, this.employee.getID());
		assertEquals(new Integer(17), this.idAdapter.value());
		assertEquals(new Integer(323), this.bufferedIDHolder.value());

		this.bufferedNameHolder.setValue("Ripley");
		assertEquals("Freddy", this.employee.getName());
		assertEquals("Freddy", this.nameAdapter.value());
		assertEquals("Ripley", this.bufferedNameHolder.value());

		Date temp = this.employee.getHireDate();
		this.bufferedHireDateHolder.setValue(null);
		assertEquals(temp, this.employee.getHireDate());
		assertEquals(temp, this.hireDateAdapter.value());
		assertEquals(null, this.bufferedHireDateHolder.value());

		this.trigger.reset();

		assertEquals(17, this.employee.getID());
		assertEquals(new Integer(17), this.idAdapter.value());
		assertEquals(new Integer(17), this.bufferedIDHolder.value());

		assertEquals("Freddy", this.employee.getName());
		assertEquals("Freddy", this.nameAdapter.value());
		assertEquals("Freddy", this.bufferedNameHolder.value());

		assertEquals(temp, this.employee.getHireDate());
		assertEquals(temp, this.hireDateAdapter.value());
		assertEquals(temp, this.bufferedHireDateHolder.value());
	}

	public void testLazyListening() {
		assertTrue(((AbstractModel) this.bufferedIDHolder).hasNoPropertyChangeListeners(ValueModel.VALUE));
		assertTrue(((AbstractModel) this.bufferedNameHolder).hasNoPropertyChangeListeners(ValueModel.VALUE));
		assertTrue(((AbstractModel) this.bufferedHireDateHolder).hasNoPropertyChangeListeners(ValueModel.VALUE));

		assertTrue(((AbstractModel) this.idAdapter).hasNoPropertyChangeListeners(ValueModel.VALUE));
		assertTrue(((AbstractModel) this.nameAdapter).hasNoPropertyChangeListeners(ValueModel.VALUE));
		assertTrue(((AbstractModel) this.hireDateAdapter).hasNoPropertyChangeListeners(ValueModel.VALUE));

		assertTrue(this.employee.hasNoPropertyChangeListeners(Employee.ID_PROPERTY));
		assertTrue(this.employee.hasNoPropertyChangeListeners(Employee.NAME_PROPERTY));
		assertTrue(this.employee.hasNoPropertyChangeListeners(Employee.HIRE_DATE_PROPERTY));

		PropertyChangeListener bufferedListener = this.buildBufferedListener();
		this.bufferedIDHolder.addPropertyChangeListener(ValueModel.VALUE, bufferedListener);
		this.bufferedNameHolder.addPropertyChangeListener(ValueModel.VALUE, bufferedListener);
		this.bufferedHireDateHolder.addPropertyChangeListener(ValueModel.VALUE, bufferedListener);

		assertTrue(((AbstractModel) this.bufferedIDHolder).hasAnyPropertyChangeListeners(ValueModel.VALUE));
		assertTrue(((AbstractModel) this.bufferedNameHolder).hasAnyPropertyChangeListeners(ValueModel.VALUE));
		assertTrue(((AbstractModel) this.bufferedHireDateHolder).hasAnyPropertyChangeListeners(ValueModel.VALUE));

		assertTrue(((AbstractModel) this.idAdapter).hasAnyPropertyChangeListeners(ValueModel.VALUE));
		assertTrue(((AbstractModel) this.nameAdapter).hasAnyPropertyChangeListeners(ValueModel.VALUE));
		assertTrue(((AbstractModel) this.hireDateAdapter).hasAnyPropertyChangeListeners(ValueModel.VALUE));

		assertTrue(this.employee.hasAnyPropertyChangeListeners(Employee.ID_PROPERTY));
		assertTrue(this.employee.hasAnyPropertyChangeListeners(Employee.NAME_PROPERTY));
		assertTrue(this.employee.hasAnyPropertyChangeListeners(Employee.HIRE_DATE_PROPERTY));

		this.bufferedIDHolder.removePropertyChangeListener(ValueModel.VALUE, bufferedListener);
		this.bufferedNameHolder.removePropertyChangeListener(ValueModel.VALUE, bufferedListener);
		this.bufferedHireDateHolder.removePropertyChangeListener(ValueModel.VALUE, bufferedListener);

		assertTrue(((AbstractModel) this.bufferedIDHolder).hasNoPropertyChangeListeners(ValueModel.VALUE));
		assertTrue(((AbstractModel) this.bufferedNameHolder).hasNoPropertyChangeListeners(ValueModel.VALUE));
		assertTrue(((AbstractModel) this.bufferedHireDateHolder).hasNoPropertyChangeListeners(ValueModel.VALUE));

		assertTrue(((AbstractModel) this.idAdapter).hasNoPropertyChangeListeners(ValueModel.VALUE));
		assertTrue(((AbstractModel) this.nameAdapter).hasNoPropertyChangeListeners(ValueModel.VALUE));
		assertTrue(((AbstractModel) this.hireDateAdapter).hasNoPropertyChangeListeners(ValueModel.VALUE));

		assertTrue(this.employee.hasNoPropertyChangeListeners(Employee.ID_PROPERTY));
		assertTrue(this.employee.hasNoPropertyChangeListeners(Employee.NAME_PROPERTY));
		assertTrue(this.employee.hasNoPropertyChangeListeners(Employee.HIRE_DATE_PROPERTY));
	}

	public void testPropertyChange1() {
		PropertyChangeListener bufferedListener = this.buildBufferedListener();
		this.bufferedNameHolder.addPropertyChangeListener(ValueModel.VALUE, bufferedListener);

		PropertyChangeListener adapterListener = this.buildAdapterListener();
		this.nameAdapter.addPropertyChangeListener(ValueModel.VALUE, adapterListener);

		PropertyChangeListener employeeListener = this.buildEmployeeListener();
		this.employee.addPropertyChangeListener(Employee.NAME_PROPERTY, employeeListener);

		this.verifyPropertyChanges();
	}

	public void testPropertyChange2() {
		PropertyChangeListener bufferedListener = this.buildBufferedListener();
		this.bufferedNameHolder.addPropertyChangeListener(bufferedListener);

		PropertyChangeListener adapterListener = this.buildAdapterListener();
		this.nameAdapter.addPropertyChangeListener(adapterListener);

		PropertyChangeListener employeeListener = this.buildEmployeeListener();
		this.employee.addPropertyChangeListener(employeeListener);

		this.verifyPropertyChanges();
	}

	private void verifyPropertyChanges() {
		this.bufferedEvent = null;
		this.adapterEvent = null;
		this.employeeEvent = null;
		this.bufferedNameHolder.setValue("Ripley");
		this.verifyEvent(this.bufferedEvent, this.bufferedNameHolder, ValueModel.VALUE, "Freddy", "Ripley");
		assertNull(this.adapterEvent);
		assertNull(this.employeeEvent);

		this.bufferedEvent = null;
		this.adapterEvent = null;
		this.employeeEvent = null;
		this.bufferedNameHolder.setValue("Charlie");
		this.verifyEvent(this.bufferedEvent, this.bufferedNameHolder, ValueModel.VALUE, "Ripley", "Charlie");
		assertNull(this.adapterEvent);
		assertNull(this.employeeEvent);

		this.bufferedEvent = null;
		this.adapterEvent = null;
		this.employeeEvent = null;
		this.trigger.accept();
		assertNull(this.bufferedEvent);
		this.verifyEvent(this.adapterEvent, this.nameAdapter, ValueModel.VALUE, "Freddy", "Charlie");
		this.verifyEvent(this.employeeEvent, this.employee, Employee.NAME_PROPERTY, "Freddy", "Charlie");

		this.bufferedEvent = null;
		this.adapterEvent = null;
		this.employeeEvent = null;
		this.bufferedNameHolder.setValue("Jason");
		this.verifyEvent(this.bufferedEvent, this.bufferedNameHolder, ValueModel.VALUE, "Charlie", "Jason");
		assertNull(this.adapterEvent);
		assertNull(this.employeeEvent);

		this.bufferedEvent = null;
		this.adapterEvent = null;
		this.employeeEvent = null;
		this.trigger.reset();
		this.verifyEvent(this.bufferedEvent, this.bufferedNameHolder, ValueModel.VALUE, "Jason", "Charlie");
		assertNull(this.adapterEvent);
		assertNull(this.employeeEvent);
	}

	private PropertyChangeListener buildBufferedListener() {
		return new PropertyChangeListener() {
			public void propertyChanged(PropertyChangeEvent e) {
				BufferedPropertyValueModelTests.this.bufferedEvent = e;
			}
		};
	}

	private PropertyChangeListener buildAdapterListener() {
		return new PropertyChangeListener() {
			public void propertyChanged(PropertyChangeEvent e) {
				BufferedPropertyValueModelTests.this.adapterEvent = e;
			}
		};
	}

	private PropertyChangeListener buildEmployeeListener() {
		return new PropertyChangeListener() {
			public void propertyChanged(PropertyChangeEvent e) {
				BufferedPropertyValueModelTests.this.employeeEvent = e;
			}
		};
	}

	private void verifyEvent(PropertyChangeEvent event, Object source, String propertyName, Object oldValue, Object newValue) {
		assertEquals(source, event.getSource());
		assertEquals(propertyName, event.propertyName());
		assertEquals(oldValue, event.oldValue());
		assertEquals(newValue, event.newValue());
	}


	// ********** inner class **********

	private class Employee extends AbstractModel {
		private int id;
			public static final String ID_PROPERTY = "id";
		private String name;
			public static final String NAME_PROPERTY = "name";
		private Date hireDate;
			public static final String HIRE_DATE_PROPERTY = "hireDate";

		Employee(int id, String name, Date hireDate) {
			super();
			this.id = id;
			this.name = name;
			this.hireDate = hireDate;
		}
		int getID() {
			return this.id;
		}
		void setID(int id) {
			int old = this.id;
			this.id = id;
			this.firePropertyChanged(ID_PROPERTY, old, id);
		}
		String getName() {
			return this.name;
		}
		void setName(String name) {
			Object old = this.name;
			this.name = name;
			this.firePropertyChanged(NAME_PROPERTY, old, name);
		}
		Date getHireDate() {
			return this.hireDate;
		}
		void setHireDate(Date hireDate) {
			Object old = this.hireDate;
			this.hireDate = hireDate;
			this.firePropertyChanged(HIRE_DATE_PROPERTY, old, hireDate);
		}
		@Override
		public void toString(StringBuilder sb) {
			sb.append(this.name);
		}
	}
}
