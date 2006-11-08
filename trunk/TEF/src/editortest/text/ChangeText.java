package editortest.text;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;

public class ChangeText extends Text {
	
	private boolean isNull = false;
	
	public void setText(String text) {
		if (text == null) {
			changeContent(0, getLength(), "<null>");
			isNull = true;
		} else {
			changeContent(0, getLength(), text);
			isNull = false;
		}
	}

	public boolean isNull() {
		return isNull;
	}
}
