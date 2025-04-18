package ec.edu.uees.Lista;

public interface List<E> {
    boolean addLast(E e);
    E removeFirst();
    E removeLast();
    E get(int index);
    boolean isEmpty();
    int size();
    E remove(int index);
    E getFirst();
}