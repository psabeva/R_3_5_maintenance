package org.eclipse.jpt.core.internal.resource.orm.translators;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.wst.common.internal.emf.resource.IDTranslator;
import org.eclipse.wst.common.internal.emf.resource.Translator;

public class TheEmbeddableAttributesTranslator extends Translator
	implements OrmXmlMapper
{
	private Translator[] children;	
	
	
	public TheEmbeddableAttributesTranslator(String domNameAndPath, EStructuralFeature aFeature) {
		super(domNameAndPath, aFeature);
	}
	
	
	public Translator[] getChildren(Object target, int versionID) {
		if (children == null) {
			children = createChildren();
		}
		return children;
	}
		
	protected Translator[] createChildren() {
		return new Translator[] {
			IDTranslator.INSTANCE,
			createBasicTranslator(),
			createTransientTranslator()
		};
	}
	
	private Translator createBasicTranslator() {
		return new BasicTranslator(BASIC, ORM_PKG.getEmbeddableAttributes_Basics());
	}
	
	private Translator createTransientTranslator() {
		return new TransientTranslator(TRANSIENT, ORM_PKG.getEmbeddableAttributes_Transients());
	}
}
