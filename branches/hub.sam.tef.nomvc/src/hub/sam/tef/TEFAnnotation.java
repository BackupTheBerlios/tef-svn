package hub.sam.tef;

import org.eclipse.jface.text.source.Annotation;

public class TEFAnnotation extends Annotation {

	/*
	private final Annotation annotation;
	private final IOccurence fOccurence;
	private Position position = null;
	
	public TEFAnnotation(final Annotation annotation, final IOccurence occurence) {
		super();
		this.annotation = annotation;
		this.fOccurence = occurence;
		this.position = new Position(fOccurence.getAbsolutOffset(0), fOccurence.getLength());
	}
	
	public Annotation getAnnotation() {
		return annotation;
	}
	
	public Position getPosition() {
		if (fOccurence.isActive()) {
			position = new Position(fOccurence.getAbsolutOffset(0), fOccurence.getLength());
		}
		return position;
	}
	
	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof ErrorAnnotation) {
			return fOccurence.equals(((TEFAnnotation)arg0).fOccurence) && annotation.getType().equals(((TEFAnnotation)arg0).annotation.getType());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return fOccurence.hashCode();
	}
	*/
}
