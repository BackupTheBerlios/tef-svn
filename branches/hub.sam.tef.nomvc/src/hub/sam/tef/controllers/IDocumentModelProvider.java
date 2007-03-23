package hub.sam.tef.controllers;

import hub.sam.tef.models.IModel;

/**
 * An interface that allows implementing clients to provide all components of a TEF document model.
 * A TEF document model consist of the document text as a string, a tree representation of this text,
 * the acutal EMF model of the document, and the annotation model.
 */
public interface IDocumentModelProvider {
	public IModel getModel();
}
