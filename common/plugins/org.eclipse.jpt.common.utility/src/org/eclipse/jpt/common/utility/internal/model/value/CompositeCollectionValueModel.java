/*******************************************************************************
 * Copyright (c) 2007, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.utility.internal.model.value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import org.eclipse.jpt.common.utility.internal.StringBuilderTools;
import org.eclipse.jpt.common.utility.internal.collection.CollectionTools;
import org.eclipse.jpt.common.utility.internal.collection.NullList;
import org.eclipse.jpt.common.utility.internal.iterable.IterableTools;
import org.eclipse.jpt.common.utility.model.event.CollectionAddEvent;
import org.eclipse.jpt.common.utility.model.event.CollectionChangeEvent;
import org.eclipse.jpt.common.utility.model.event.CollectionClearEvent;
import org.eclipse.jpt.common.utility.model.event.CollectionEvent;
import org.eclipse.jpt.common.utility.model.event.CollectionRemoveEvent;
import org.eclipse.jpt.common.utility.model.listener.CollectionChangeAdapter;
import org.eclipse.jpt.common.utility.model.listener.CollectionChangeListener;
import org.eclipse.jpt.common.utility.model.value.CollectionValueModel;
import org.eclipse.jpt.common.utility.model.value.ListValueModel;
import org.eclipse.jpt.common.utility.transformer.Transformer;

/**
 * A <code>CompositeCollectionValueModel</code> wraps another
 * {@link CollectionValueModel} and uses a {@link Transformer}
 * to convert each item in the wrapped collection to yet another
 * {@link CollectionValueModel}. This composite collection contains
 * the combined items from all these component collections.
 * <p>
 * <strong>NB:</strong> The wrapped collection must be an "identity set" that does not
 * contain the same item twice or this class will throw an exception.
 * <p>
 * Terminology:<ul>
 * <li><em>sources</em> - the items in the wrapped collection value model; these
 *    are converted into component CVMs by the transformer
 * <li><em>component CVMs</em> - the component collection value models that are combined
 *    by this composite collection value model
 * <li><em>items</em> - the items held by the component CVMs
 * </ul>
 * 
 * @param <E1> the type of items held by the wrapped collection model;
 *     each of these is transformed (by the {@link #transformer}) into a
 *     collection model of <code>E2</code>s
 * @param <E2> the type of items held by the composite collection model
 */
public class CompositeCollectionValueModel<E1, E2>
	extends CollectionValueModelWrapper<E1>
	implements CollectionValueModel<E2>
{
	/**
	 * This is the (optional) user-supplied object that transforms
	 * the items in the wrapped collection to collection value models.
	 */
	private final Transformer<E1, ? extends CollectionValueModel<? extends E2>> transformer;

	/**
	 * Cache of the component collection value models that
	 * were generated by the transformer; keyed by the item
	 * in the wrapped collection that was passed to the transformer.
	 */
	private final IdentityHashMap<E1, CollectionValueModel<? extends E2>> componentCVMs =
			new IdentityHashMap<E1, CollectionValueModel<? extends E2>>();

	/**
	 * Cache of the collections corresponding to the component
	 * collection value models above; keyed by the component
	 * collection value models.
	 * Use {@link ArrayList}s so we can use {@link ArrayList}-specific methods
	 * (e.g. {@link ArrayList#clone()} and {@link ArrayList#ensureCapacity(int)}).
	 */
	private final IdentityHashMap<CollectionValueModel<? extends E2>, ArrayList<E2>> collections =
			new IdentityHashMap<CollectionValueModel<? extends E2>, ArrayList<E2>>();

	/** Listener that listens to all the component collection value models. */
	private final CollectionChangeListener componentCVMListener;

	/** Cache the size of the composite collection. */
	private int size;


	// ********** constructors **********

	/**
	 * Construct a collection value model with the specified wrapped
	 * list value model. The specified list model already contains other
	 * collection value models.
	 */
	public static <E1 extends CollectionValueModel<? extends E2>, E2> CompositeCollectionValueModel<E1, E2> forModels(ListValueModel<E1> listModel) {
		return forModels(new ListCollectionValueModelAdapter<E1>(listModel));
	}

	/**
	 * Construct a collection value model with the specified wrapped collection.
	 */
	public static <E1 extends CollectionValueModel<? extends E2>, E2> CompositeCollectionValueModel<E1, E2> forModels(Collection<E1> collection) {
		return forModels(new StaticCollectionValueModel<E1>(collection));
	}

	/**
	 * Construct a collection value model with the specified wrapped collection.
	 */
	public static <E1 extends CollectionValueModel<? extends E2>, E2> CompositeCollectionValueModel<E1, E2> forModels(E1... collection) {
		return forModels(new StaticCollectionValueModel<E1>(collection));
	}

	/**
	 * Construct a collection value model with the specified wrapped
	 * collection value model. The specified collection
	 * model already contains other collection value models.
	 */
	public static <E1 extends CollectionValueModel<? extends E2>, E2> CompositeCollectionValueModel<E1, E2> forModels(CollectionValueModel<E1> collectionModel) {
		return new CompositeCollectionValueModel<E1, E2>(collectionModel, Transformer.Non.<E1>instance());
	}

	/**
	 * Construct a collection value model with the specified wrapped
	 * list value model and transformer.
	 */
	public CompositeCollectionValueModel(ListValueModel<? extends E1> listModel, Transformer<E1, ? extends CollectionValueModel<? extends E2>> transformer) {
		this(new ListCollectionValueModelAdapter<E1>(listModel), transformer);
	}

	/**
	 * Construct a collection value model with the specified, unchanging, wrapped
	 * collection and transformer.
	 */
	public CompositeCollectionValueModel(Collection<? extends E1> collection, Transformer<E1, ? extends CollectionValueModel<? extends E2>> transformer) {
		this(new StaticCollectionValueModel<E1>(collection), transformer);
	}

	/**
	 * Construct a collection value model with the specified, unchanging, wrapped
	 * collection and transformer.
	 */
	public CompositeCollectionValueModel(E1[] collection, Transformer<E1, ? extends CollectionValueModel<? extends E2>> transformer) {
		this(new StaticCollectionValueModel<E1>(collection), transformer);
	}

	/**
	 * Construct a collection value model with the specified wrapped
	 * collection value model and transformer.
	 */
	public CompositeCollectionValueModel(CollectionValueModel<? extends E1> collectionModel, Transformer<E1, ? extends CollectionValueModel<? extends E2>> transformer) {
		super(collectionModel);
		if (transformer == null) {
			throw new NullPointerException();
		}
		this.transformer = transformer;
		this.componentCVMListener = this.buildComponentListener();
		this.size = 0;
	}


	// ********** initialization **********

	protected CollectionChangeListener buildComponentListener() {
		return new ComponentListener();
	}

	protected class ComponentListener
		extends CollectionChangeAdapter
	{
		@Override
		public void itemsAdded(CollectionAddEvent event) {
			CompositeCollectionValueModel.this.componentItemsAdded(event);
		}		
		@Override
		public void itemsRemoved(CollectionRemoveEvent event) {
			CompositeCollectionValueModel.this.componentItemsRemoved(event);
		}
		@Override
		public void collectionCleared(CollectionClearEvent event) {
			CompositeCollectionValueModel.this.componentCollectionCleared(event);
		}
		@Override
		public void collectionChanged(CollectionChangeEvent event) {
			CompositeCollectionValueModel.this.componentCollectionChanged(event);
		}
	}


	// ********** CollectionValueModel implementation **********

	public Iterator<E2> iterator() {
		return this.buildCompositeIterable().iterator();
	}

	protected Iterable<E2> buildCompositeIterable() {
		return IterableTools.concatenate(this.collections.values());
	}

	public int size() {
		return this.size;
	}


	// ********** CollectionValueModelWrapper overrides/implementation **********

	@Override
	protected void engageModel() {
		super.engageModel();
		// sync our cache *after* we start listening to the wrapped collection,
		// since its value might change when a listener is added
		this.addAllComponentSources();
	}

	/**
	 * Transform all the sources to collection value models
	 * and add their items to our cache, with no event notification.
	 */
	protected void addAllComponentSources() {
		for (E1 source : this.collectionModel) {
			this.addComponentSource(source, NullList.<E2>instance());
		}
	}

	@Override
	protected void disengageModel() {
		super.disengageModel();
		// stop listening to the components...
		for (CollectionValueModel<? extends E2> componentCVM : this.componentCVMs.values()) {
			componentCVM.removeCollectionChangeListener(VALUES, this.componentCVMListener);
		}
		// ...and clear the cache
		this.componentCVMs.clear();
		this.collections.clear();
		this.size = 0;
	}

	/**
	 * Some component sources were added;
	 * add their corresponding items to our cache.
	 */
	@Override
	protected void itemsAdded(CollectionAddEvent event) {
		ArrayList<E2> addedItems = new ArrayList<E2>();
		for (E1 item : this.getItems(event)) {
			this.addComponentSource(item, addedItems);
		}
		this.fireItemsAdded(VALUES, addedItems);
	}

	/**
	 * Transform the specified source to a collection value model
	 * and add its items to our cache and the "collecting parameter".
	 */
	protected void addComponentSource(E1 source, List<E2> addedItems) {
		CollectionValueModel<? extends E2> componentCVM = this.transformer.transform(source);
		if (this.componentCVMs.put(source, componentCVM) != null) {
			throw new IllegalStateException("duplicate component: " + source); //$NON-NLS-1$
		}
		componentCVM.addCollectionChangeListener(VALUES, this.componentCVMListener);
		ArrayList<E2> componentCollection = new ArrayList<E2>(componentCVM.size());
		if (this.collections.put(componentCVM, componentCollection) != null) {
			throw new IllegalStateException("duplicate collection: " + source); //$NON-NLS-1$
		}
		this.addComponentItems(componentCVM, componentCollection);
		addedItems.addAll(componentCollection);
	}

	/**
	 * Add the items in the specified component CVM to the specified component
	 * collection.
	 */
	protected void addComponentItems(CollectionValueModel<? extends E2> componentCVM, ArrayList<E2> componentCollection) {
		int itemsSize = componentCVM.size();
		this.size += itemsSize;
		componentCollection.ensureCapacity(componentCollection.size() + itemsSize);
		CollectionTools.addAll(componentCollection, componentCVM);
	}

	/**
	 * Some component sources were removed;
	 * remove their corresponding items from our cache.
	 */
	@Override
	protected void itemsRemoved(CollectionRemoveEvent event) {
		ArrayList<E2> removedItems = new ArrayList<E2>();
		for (E1 item : this.getItems(event)) {
			this.removeComponentSource(item, removedItems);
		}
		this.fireItemsRemoved(VALUES, removedItems);
	}

	/**
	 * Remove the items corresponding to the specified source
	 * from our cache.
	 */
	protected void removeComponentSource(E1 source, List<E2> removedItems) {
		CollectionValueModel<? extends E2> componentCVM = this.componentCVMs.remove(source);
		if (componentCVM == null) {
			throw new IllegalStateException("missing component: " + source); //$NON-NLS-1$
		}
		componentCVM.removeCollectionChangeListener(VALUES, this.componentCVMListener);
		ArrayList<E2> componentCollection = this.collections.remove(componentCVM);
		if (componentCollection == null) {
			throw new IllegalStateException("missing collection: " + source); //$NON-NLS-1$
		}
		removedItems.addAll(componentCollection);
		this.removeComponentItems(componentCollection);
	}

	/**
	 * Update our size and collection cache.
	 */
	protected void removeComponentItems(ArrayList<E2> componentCollection) {
		this.size -= componentCollection.size();
		componentCollection.clear();
	}

	/**
	 * The component sources cleared;
	 * clear our cache.
	 */
	@Override
	protected void collectionCleared(CollectionClearEvent event) {
		this.removeAllComponentSources();
		this.fireCollectionCleared(VALUES);
	}

	protected void removeAllComponentSources() {
		// copy the keys so we don't eat our own tail
		ArrayList<E1> copy = new ArrayList<E1>(this.componentCVMs.keySet());
		for (E1 source : copy) {
			this.removeComponentSource(source, NullList.<E2>instance());
		}
	}

	/**
	 * The component sources changed;
	 * rebuild our cache.
	 */
	@Override
	protected void collectionChanged(CollectionChangeEvent event) {
		this.removeAllComponentSources();
		this.addAllComponentSources();
		this.fireCollectionChanged(VALUES, CollectionTools.collection(this.iterator()));
	}


	// ********** internal methods **********

	/**
	 * One of the component collections had items added;
	 * synchronize our caches.
	 */
	protected void componentItemsAdded(CollectionAddEvent event) {
		int itemsSize = event.getItemsSize();
		this.size += itemsSize;

		ArrayList<E2> componentCollection = this.collections.get(this.componentCVM(event));
		componentCollection.ensureCapacity(componentCollection.size() + itemsSize);

		this.addItemsToCollection(this.getComponentItems(event), componentCollection, VALUES);
	}

	/**
	 * One of the component collections had items removed;
	 * synchronize our caches.
	 */
	protected void componentItemsRemoved(CollectionRemoveEvent event) {
		this.size -= event.getItemsSize();
		ArrayList<E2> componentCollection = this.collections.get(this.componentCVM(event));
		this.removeItemsFromCollection(this.getComponentItems(event), componentCollection, VALUES);
	}

	/**
	 * One of the component collections was cleared;
	 * synchronize our caches by clearing out the appropriate
	 * collection.
	 */
	protected void componentCollectionCleared(CollectionClearEvent event) {
		ArrayList<E2> componentCollection = this.collections.get(this.componentCVM(event));
		ArrayList<E2> removedItems = new ArrayList<E2>(componentCollection);
		this.removeComponentItems(componentCollection);
		this.fireItemsRemoved(VALUES, removedItems);
	}

	/**
	 * One of the component collections changed;
	 * synchronize our caches by clearing out the appropriate
	 * collection and then rebuilding it.
	 */
	protected void componentCollectionChanged(CollectionChangeEvent event) {
		CollectionValueModel<E2> componentCVM = this.componentCVM(event);
		ArrayList<E2> componentCollection = this.collections.get(componentCVM);
		this.removeComponentItems(componentCollection);
		this.addComponentItems(componentCVM, componentCollection);
		this.fireCollectionChanged(VALUES, CollectionTools.collection(this.iterator()));
	}

	// minimize scope of suppressed warnings
	@SuppressWarnings("unchecked")
	protected Iterable<E2> getComponentItems(CollectionAddEvent event) {
		return (Iterable<E2>) event.getItems();
	}

	// minimize scope of suppressed warnings
	@SuppressWarnings("unchecked")
	protected Iterable<E2> getComponentItems(CollectionRemoveEvent event) {
		return (Iterable<E2>) event.getItems();
	}

	// minimize scope of suppressed warnings
	@SuppressWarnings("unchecked")
	protected CollectionValueModel<E2> componentCVM(CollectionEvent event) {
		return (CollectionValueModel<E2>) event.getSource();
	}

	@Override
	public void toString(StringBuilder sb) {
		StringBuilderTools.append(sb, this);
	}
}
