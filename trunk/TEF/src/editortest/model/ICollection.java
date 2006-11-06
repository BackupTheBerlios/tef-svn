package editortest.model;

public interface ICollection<T> extends Iterable<T> {
	public void add(T element);
	
	public boolean remove(T element);
	
	public int size();
}
