package hub.sam.tef;

import hub.sam.tef.controllers.IAnnotationModelProvider;
import hub.sam.tef.views.Text;

import org.eclipse.jface.text.source.Annotation;

import editortest.emf.model.IOccurence;

public class ErrorAnnotation extends TEFAnnotation {

	public static final String TEF_ERROR = "hub.sam.tef.error";
	
	public ErrorAnnotation(final IOccurence position) {
		super(new Annotation(TEF_ERROR, false, "An error..."), position);
	}

	public void addToAnnotationModel(IAnnotationModelProvider model) {
		model.addAnnotation(this);		
	}
	
	public void removeFromAnnotationModel(IAnnotationModelProvider model) {
		model.removeAnnotation(this);
	}
	
}
