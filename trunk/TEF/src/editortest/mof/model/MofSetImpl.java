package editortest.mof.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import cmof.common.ReflectiveCollection;
import editortest.model.ModelEventListener;
import editortest.model.ICollection;

public class MofSetImpl<E> extends Mof implements ICollection<E> {

	private final ReflectiveCollection<E> fCollection;
	protected final Collection<ModelEventListener> fListener = new Vector<ModelEventListener>();	
	
	public MofSetImpl(final ReflectiveCollection<? extends E> collection) {
		super();
		fCollection = (ReflectiveCollection<E>)collection;
	}

	protected ReflectiveCollection<E> getCollection() {
		return fCollection;
	}
	
	public void add(E element) {
		fCollection.add(mofObjectFromObject(element));
		for(ModelEventListener listener: fListener) {
			listener.elementAdded(element, fCollection.size() - 1);
		}
	}
	
	class MyIterator implements Iterator {
		private final Iterator fBase;

		public MyIterator(final Iterator base) {
			super();
			fBase = base;
		}

		public boolean hasNext() {
			return fBase.hasNext();
		}

		public Object next() {
			Object result = fBase.next();
			return objectFromMofObject(result);
		}
		
		public void remove() {			
		}				
	}

	public Iterator<E> iterator() {
		return new MyIterator(fCollection.iterator());
	}

	public void addChangeListener(ModelEventListener listener) {
		fListener.add(listener);
	}

	public int size() {
		return fCollection.size();
	}	
	
}
