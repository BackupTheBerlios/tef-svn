package editortest.template;

import editortest.controller.Proposal;
import editortest.model.IMetaModelElement;
import editortest.model.IModelElement;
import editortest.view.CompoundText;
import editortest.view.DocumentText;
import editortest.view.Text;

/**
 * A special ValueTemplate used for elements, whereby elements are container for
 * Properties. An ElementTemplate provides the representations for whole model
 * elements using property and terminal templates as children. It defines a node in 
 * a hierarchy of templates.
 */
public abstract class ElementTemplate extends ValueTemplate<IModelElement> {
	
	private final IMetaModelElement fMetaModel;
	private Template[] fTemplates;
	
	public ElementTemplate(Template template, IMetaModelElement metaModel) {
		super(template);		
		fMetaModel = metaModel;
	}	

	public ElementTemplate(DocumentText document, IMetaModelElement metaModel) {
		super(document);
		fMetaModel = metaModel;
	}
	
	/**
	 * Creates all child templates. This can be Terminal or ValueTemplates.
	 */
	public abstract Template[] createTemplates();
	
	private final Template[] getTemplates() {
		if (fTemplates == null) {
			fTemplates = createTemplates();
		}
		return fTemplates;
	}

	/**
	 * @return The meta element that describes the elements that this template
	 *         provides representations for.
	 */
	protected IMetaModelElement getMetaElement() {
		return this.fMetaModel;
	}

	/**
	 * Can be used when no value change listener is needed. Compare with {@link #createView(IModelElement, IValueChangeListener)}.
	 */
	public Text createView(IModelElement model) {
		return createView(model, new IValueChangeListener<IModelElement>() {
			public void valueChanges(IModelElement newValue) {
				// empty				
			}			
		});
	}

	@Override
	public Text createView(IModelElement model, IValueChangeListener<IModelElement> changeListener) {
		CompoundText result = new CompoundText();
		for (Template template: getTemplates()) {
			if (template instanceof TerminalTemplate) {
				result.addText(((TerminalTemplate)template).createView());
			} else if (template instanceof PropertyTemplate) {
				PropertyTemplate propertyTemplate = (PropertyTemplate)template;
				Text propertyText = ((PropertyTemplate)template).createView(model);
				if (isIdentifierProperty(propertyTemplate.getProperty())) {
					model.registerOccurence(propertyText);
				}											
				result.addText(propertyText);
			} else if (template instanceof ElementTemplate) {
				result.addText(((ElementTemplate)template).createView(model));
			} else {
				throw new RuntimeException("assert");
			}			
		}
		return result;
	}	

	public IModelElement createModelFromProposal(Proposal proposal) {
		IModelElement result = getModel().createElement(getMetaElement());		
		return result;
	}	
	
	/**
	 * Returns true for those meta model elements that this element template
	 * provides representations for.
	 */
	public boolean isTemplateFor(IModelElement model) {		
		return getMetaElement().isMetaModelFor(model);
	}

	@Override
	public void deleteModel(IModelElement model) {
		model.delete();
	}
	
	/**
	 * When a view for this element is created those property subviews that
	 * represent properties that this method yields will be registered as
	 * occurences of the element represented by this template..
	 * 
	 * @param property
	 *            The property.
	 * @return True when the representation of this property should be marked as
	 *         a occurence of the element represented by the containing element
	 *         template.
	 */
	protected boolean isIdentifierProperty(String property) {
		return false;
	}
}
