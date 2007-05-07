package hub.sam.tef.templates.primitives;

import java.util.Collection;
import java.util.Vector;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import hub.sam.tef.models.ICommand;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.reconciliation.syntax.ISyntaxHighlightProvider;
import hub.sam.tef.templates.Template;

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
		return value;
	}

	@Override
	protected String getNonTerminal() {
		return "`stringdef`";
	}

	@Override
	protected IRule getHightlightRule(IToken token) {
		return new SingleLineRule("\"", "\"", token, '\\' );
	}		
	
}