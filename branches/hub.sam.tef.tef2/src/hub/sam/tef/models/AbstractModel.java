package hub.sam.tef.models;

import hub.sam.tef.models.extensions.DelegateCommandFactory;

public abstract class AbstractModel implements IModel {

	private ICommandFactory fFactory = null;	
	private final Object editedResource;
	
	public AbstractModel(final Object editedResource) {
		super();
		this.editedResource = editedResource;
	}

	protected abstract ICommandFactory createCommandFactory();

	public final ICommandFactory getCommandFactory() {
		if (fFactory == null) {
			fFactory = new DelegateCommandFactory(createCommandFactory());
		}
		return fFactory;
	}

	public ICollection getOutermostCompositesOfEditedResource() {
		return getOutermostComposites(editedResource);
	}		
	
	public void replaceOutermostComposite(Object resource, IModelElement oldElement, IModelElement newElement) {		
		ICollection outermostComposites = getOutermostComposites(resource);
		if (!outermostComposites.contains(oldElement)) {
			System.out.println("ERROR: old composite is not part of the model");
		}
		int size = outermostComposites.size();
		getCommandFactory().remove(outermostComposites, oldElement).execute();		
		getCommandFactory().add(getOutermostComposites(resource), newElement).execute();
		if (getOutermostComposites(resource).size() > size) {
			System.out.println("ERROR: composite was edit and not replaced");
		}
	}

	public Iterable<IModelElement> getElementExceptEditedResource(IMetaModelElement metaElement) {
		return getElements(metaElement, editedResource);
	}
		
}
