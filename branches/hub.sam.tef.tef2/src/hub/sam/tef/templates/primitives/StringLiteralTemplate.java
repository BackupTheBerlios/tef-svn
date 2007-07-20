package hub.sam.tef.templates.primitives;

import hub.sam.tef.models.ICommand;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.templates.Template;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.SingleLineRule;

public class StringLiteralTemplate extends PrimitiveValueLiteralTemplate<String> {	
	
	public StringLiteralTemplate(Template template) {
		super(template, template.getModelProvider().getModel().getType(Integer.class));
	}

	@Override
	public ICommand getCommandToCreateADefaultValue(IModelElement owner, String property, boolean composite) {
		return getModel().getCommandFactory().set(owner, property, "");
	}			
	
	@Override
	protected Object getObjectValueFromStringValue(String value) {
		return value.substring(1, value.length()-1);
	}

	@Override
	protected String getNonTerminal() {
		return "`stringdef`";
	}

	@Override
	protected IRule getHightlightRule(IToken token) {
		return new SingleLineRule("\"", "\"", token, '\\' );
	}		
	
	@Override
	protected String getStringValueFromObjectValue(Object value) {
		return "\"" + value.toString() + "\"";
	}
}