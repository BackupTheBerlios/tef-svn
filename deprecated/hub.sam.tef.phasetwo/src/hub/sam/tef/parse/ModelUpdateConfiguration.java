package hub.sam.tef.parse;

import hub.sam.tef.models.IModelElement;
import hub.sam.tef.treerepresentation.TreeRepresentation;

public class ModelUpdateConfiguration {

	private TreeRepresentation ast;
	private String value = null;
	private IModelElement owner;
	private String property;
	private boolean isComposite;
	private boolean isCollection;
	private boolean hasOldParent;
	private int postion = -1;
	
	public ModelUpdateConfiguration(TreeRepresentation ast, IModelElement owner, String property, boolean hasOldParent) {
		super();
		this.ast = ast;
		this.owner = owner;
		this.property = property;
		this.hasOldParent = hasOldParent;
		
		this.isComposite = true;
		this.isCollection = false;
	}
	
	private ModelUpdateConfiguration(TreeRepresentation ast, String value, IModelElement owner, String property, boolean isComposite, boolean isCollection, boolean hasOldParent, int position) {
		super();
		this.ast = ast;
		this.value = value;
		this.owner = owner;
		this.property = property;
		this.isComposite = isComposite;
		this.isCollection = isCollection;
		this.hasOldParent = hasOldParent;
		this.postion = position;
	}

	public TreeRepresentation getAst() {
		return ast;
	}
	public boolean isHasOldParent() {
		return hasOldParent;
	}
	public boolean isCollection() {
		return isCollection;
	}
	public boolean isComposite() {
		return isComposite;
	}
	public IModelElement getOwner() {
		return owner;
	}
	public String getProperty() {
		return property;
	}
	
	public String getPrimitiveValue() {
		return value;
	}
	
	public ModelUpdateConfiguration createReferenceConfiguration(TreeRepresentation referenceAST) {		
		return new ModelUpdateConfiguration(referenceAST, null, owner, property, false, isCollection, hasOldParent, -1);
	}
	
	public ModelUpdateConfiguration createCollectionConfiguration(TreeRepresentation valueAST) {		
		return new ModelUpdateConfiguration(valueAST, null, owner, property, isComposite, true, hasOldParent, -1);
	}
	
	public ModelUpdateConfiguration createCollectionConfiguration(TreeRepresentation valueAST, int position) {
		return new ModelUpdateConfiguration(valueAST, null, owner, property, isComposite, true, hasOldParent, position);
	}
	
	public ModelUpdateConfiguration createDelegateConfiguration(TreeRepresentation ast) {
		return new ModelUpdateConfiguration(ast, null, owner, property, isComposite, isCollection, hasOldParent, -1);
	}
	
	public ModelUpdateConfiguration createPrimitiveConfiguration(String value) {
		return new ModelUpdateConfiguration(ast, value, owner, property, isComposite, isCollection, hasOldParent, -1);
	}
	
	public boolean isOldNode() {
		return (this.ast != null) ? ast.referencesOldTreeNode() : false;
	}
	
	public boolean hasPosition() {
		return postion != -1;
	}
	
	public int getPosition() {
		if (!hasPosition()) {
			throw new RuntimeException("assert");
		} else {
			return postion;
		}
	}
}
