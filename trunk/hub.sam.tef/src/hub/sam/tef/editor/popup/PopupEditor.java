package hub.sam.tef.editor.popup;

import java.util.ArrayList;
import java.util.List;

import hub.sam.tef.TEFPlugin;
import hub.sam.tef.editor.model.ModelEditor;
import hub.sam.tef.editor.popup.OpenPopupEditor.Closer;
import hub.sam.tef.modelcreating.IModelCreatingContext;
import hub.sam.tef.modelcreating.ModelCreatingContext;
import hub.sam.tef.tsl.TslException;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.ReplaceCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * This TEF editor type is used for small pop-up windows with text editors for
 * partial models. This editors pop-up within other editors, like the standard
 * EMF generated tree-based model editors.
 */
public abstract class PopupEditor extends ModelEditor {
	
	public static final String CLOSE_POPUP_EDITOR_ACTION_ID = "hub.sam.tef.closePopupEditor";

	private Closer fPopupCloser = null;
	private IEditingDomainProvider fEditingDomainProvider = null;
	private EObject editedObject = null;
	private EObject fOriginalObject = null;
		
	/**
	 * Pop-up editors do not only edit whole models, but single model elements.
	 * For this, we need specific syntaxes for all the elements that can be
	 * edited.
	 */			
	public void reduceSyntax(EClass classifier) throws TslException {
		getSyntax().reduceSyntax(classifier);
	}

	@Override
	protected void initialiseDocumentProvider() {
		setDocumentProvider(new PopupDocumentProvider(this));
	}
	
	protected void editorContextMenuAboutToShow(IMenuManager menu) {		
		super.editorContextMenuAboutToShow(menu);
		addAction(menu, TEF_CONTEXT_MENU_GROUP, 
				ClosePopupEditorAction.ACTION_DEFINITION_ID);
		
	}

	@Override
	protected void createActions() {
		super.createActions();		
		setAction(ClosePopupEditorAction.ACTION_DEFINITION_ID, 
				new ClosePopupEditorAction(this));
	}
	
	/**
	 * Is used when the user accepts the editor changes and wants to close the
	 * editor. This will either replace the edited object with the original one
	 * (non store, error cases), or will create a command that replaces the
	 * original with the edited object.
	 */
	@SuppressWarnings("unchecked")
	public void close(boolean store) {
		if (!store || hasError()) {		
			replaceEditedObject(fOriginalObject);
		} else {
			EObject newObject = editedObject;
			// replace the edited object with the original
			replaceEditedObject(fOriginalObject);
			
			// now use a command to replace the original object with the edited
			EditingDomain editingDomain = 
					fEditingDomainProvider.getEditingDomain();			
//			
//			EObject container = fOriginalObject.eContainer();
//			EList containerList = null;
//			Command setReplaceCommand = null;
//			if (container == null) {
//				containerList = fOriginalObject.eResource().getContents();				
//			} else {	
//				EReference containmentFeature = fOriginalObject.eContainmentFeature();	
//				if (containmentFeature.isMany()) {
//					containerList = (EList)container.eGet(containmentFeature);
//				} else { 									
//					setReplaceCommand = SetCommand.create(editingDomain, container, 
//							containmentFeature, newObject);					
//				}
//			}
//			if (containerList != null) {
//				setReplaceCommand = new ReplaceCommand(editingDomain, 
//						containerList, fOriginalObject, newObject);
//			}
//			List<Command> commands = new ArrayList<Command>();
//			commands.add(setReplaceCommand);
//			commands.add(DeleteCommand.create(editingDomain, fOriginalObject));
			Command command = PopupEditingReplaceCommand.create(editingDomain, fOriginalObject, newObject);
			if (!command.canExecute()) {
				TEFPlugin.getDefault().getLog().log(
						new Status(Status.ERROR, TEFPlugin.PLUGIN_ID,
								"Cannot save pop-up editor result! Cannot execute: " + 
								command.getDescription()));
				MessageDialog.openError(getSite().getShell(), "Cannot save pop-up editor result!", 
				"Cannot save pop-up editor result! Cannot execute: " + command.getDescription());
			}
			editingDomain.getCommandStack().execute(command);						
		}
		fPopupCloser.close();
	}
	
	public void configure(IEditingDomainProvider editingDomainProvider, Closer popupCloser) {
		this.fEditingDomainProvider = editingDomainProvider;
		this.fPopupCloser = popupCloser;		
	}

	/**
	 * The super-class method simply replaces the edited model with the model in
	 * this given resource. A pop-up editor only replaces the edited element
	 * with the content of the given resource. This is not done here, but
	 * directly in the used model creating context
	 * {@link this#createModelCreatingContext()}.
	 */	
	@Override
	public void updateCurrentModel(IModelCreatingContext context) {
		// emtpy		
	}

	/**
	 * Replaces the edited object in the edited resource. 
	 */
	@SuppressWarnings("unchecked")
	private void replaceEditedObject(EObject newObject) {
		EObject container = editedObject.eContainer();
		EList containerList = null;
		if (container == null) {
			containerList = fResource.getContents();
		} else { 
			EReference containmentFeature = editedObject.eContainmentFeature();
			if (containmentFeature.isMany()) {
				containerList = (EList)container.eGet(containmentFeature);						
			} else {
				container.eSet(containmentFeature, newObject);
			}				
		}
		
		if (containerList != null) {
			int index = containerList.indexOf(editedObject);
			containerList.set(index, newObject);
		} 
		
		editedObject = newObject;
	}
	
	/**
	 * Configures this editor with the editedObject. Can only be called once in
	 * the life-cycle of an pop-up editor.
	 */
	protected void setEditedObject(EObject editedObject) {
		Assert.isTrue(this.editedObject == null);
		this.editedObject = editedObject;
		this.fOriginalObject = editedObject;
	}

	/**
	 * Returns a model creating context that uses the edited resource directly.
	 * When the reconciliation process adds an element to the resource, it is
	 * assumed that this is the newly edited object and this implementation will
	 * replace the old edited object with this object.
	 */
	@Override
	public IModelCreatingContext createModelCreatingContext() {
		return new ModelCreatingContext(getMetaModelPackages(),
				getSemanticsProvider(), fResource, getCurrentText()) {
			@Override
			public void addCreatedObject(EObject object) {
				replaceEditedObject(object);
			}

		};
	}
	
	
}