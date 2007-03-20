package editortest.mof.editor;

import hub.sam.tef.TEFDocument;
import hub.sam.tef.controllers.IAnnotationModelProvider;
import hub.sam.tef.controllers.ICursorPostionProvider;
import hub.sam.tef.templates.Template;
import editortest.mof.template.MofTemplate;

public class MofDocument extends TEFDocument {

	@Override
	public Template createTopLevelTemplate(IAnnotationModelProvider annotationModelProvider, ICursorPostionProvider cursorPositionProvider) {
		return new MofTemplate(annotationModelProvider, cursorPositionProvider, getModelRepresentationProvider());
	}

	
}
