package hub.sam.tef.emf;

import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.edit.domain.EditingDomain;

import hub.sam.tef.documents.TEFDocumentProvider;

public abstract class AbstractEMFDocumentProvider extends TEFDocumentProvider {
	protected abstract Iterable<EPackage> getPackage();
	protected abstract Iterable<EFactory> getFactory();
	public abstract EditingDomain getEditingDomain();
}
