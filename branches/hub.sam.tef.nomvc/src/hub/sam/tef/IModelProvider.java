package hub.sam.tef;

import hub.sam.tef.models.IModel;
import hub.sam.tef.models.IModelElement;

public interface IModelProvider {
	public IModel getModel();
	
	public IModelElement getTopLevelElement();
}
