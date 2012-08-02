package org.eclipse.jpt.common.core.tests.internal.resource.java;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jpt.common.core.internal.resource.java.source.SourceAnnotation;
import org.eclipse.jpt.common.core.internal.utility.jdt.SimpleDeclarationAnnotationAdapter;
import org.eclipse.jpt.common.core.resource.java.JavaResourceAnnotatedElement;
import org.eclipse.jpt.common.core.utility.jdt.AnnotatedElement;
import org.eclipse.jpt.common.core.utility.jdt.DeclarationAnnotationAdapter;


public class SourceDeprecatedAnnotation
		extends SourceAnnotation {
	
	private static final DeclarationAnnotationAdapter DECLARATION_ANNOTATION_ADAPTER 
			= new SimpleDeclarationAnnotationAdapter(Deprecated.class.getName());
	
	
	public SourceDeprecatedAnnotation(JavaResourceAnnotatedElement parent, AnnotatedElement element) {
		super(parent, element, DECLARATION_ANNOTATION_ADAPTER);
	}

	public String getAnnotationName() {
		return Deprecated.class.getName();
	}
	
	@Override
	public void initialize(Annotation astAnnotation) {
		// nothing to initialize
	}
	
	@Override
	public void synchronizeWith(Annotation astAnnotation) {
		// nothing to sync
	}
}
