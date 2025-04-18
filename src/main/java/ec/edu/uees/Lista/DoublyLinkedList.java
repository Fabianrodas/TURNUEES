package ec.edu.uees.Lista;

import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;

public class DoublyLinkedList<E> implements List<E>, Iterable<E> {

    Node<E> last;
    int current;

    @Override
    public E getFirst() {
        return last.next.data;
    }
    
    class Node<E>{
        E data;
        Node<E> next;
        Node<E> previous;
        
        Node(E data){
            this.data = data;
            this.next = this;
            this.previous = this;
        }
    }
    @Override
    public boolean addLast(E e) {
        if(e==null)
            return false;
        Node<E> p = new Node<>(e);
        if(isEmpty()) 
            last = p; 
        else {
            p.next = last.next;
            last.next = p;
            p.previous = last;
            p.next.previous = p;
            last = p;
        }
        current++;
        return true;
    }

    @Override
    public E removeFirst() {
        if(this.isEmpty()){ 
            throw new UnsupportedOperationException("lista vacía");
        }
        E tmp = last.next.data; 
        if(last.next == last) {
            last = null;
        }
        else {
           Node<E> q = last.next;            
           last.next = last.next.next;
           last.next.previous = last;
           q.next = null; q.previous = null;
        }
        current--; 
        return tmp;
    }

    @Override
    public E removeLast() {
        if(this.isEmpty()){
            throw new UnsupportedOperationException("lista vacía");
        }
        E tmp = last.data;
        if(last.next == last) {
            last = null;
        }
        else {
            Node<E> q = last;
            last.previous.next = last.next;
            last.next.previous = last.previous;
            last = last.previous;
            q.next = null; q.previous = null;
        }
        current--; 
        return tmp;
    }

    @Override
    public E get(int index) {
        if(this.isEmpty())
            throw new UnsupportedOperationException("lista vacía");
        if(index >= 0 && index < current) {
            Node<E> p = last.next;
            for(int i = 0; i<index; i++){
                p = p.next; 
            }
            return p.data;
        } else {
            throw new IndexOutOfBoundsException("Indice fuera de rango");
        }
    }

    @Override
    public boolean isEmpty() {
        return last == null;
    }

    @Override
    public int size() {
        return current;
    }

    @Override
    public E remove(int index) {
        if(this.isEmpty())
            throw new UnsupportedOperationException("lista vacía");
        if(index < 0 || index >= current) 
            throw new IndexOutOfBoundsException("Indice fuera de rango");
        if(index==0)
            return this.removeFirst();
        if(index==current-1)
            return this.removeLast();
        E tmp;
        Node<E> p = last.next;
        for(int i = 0; i<index-1; i++){
            p = p.next;  
        }
        tmp = p.data;
        p.next.next.previous = p;
        p.next.previous = null;
        p.next = p.next.next;
        current--;
        return tmp;
    }
    
    @Override
    public String toString() {
        if(this.isEmpty())
            return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(Node<E> p = last.next; p!=last; p = p.next) {
            sb.append(p.data);
            if(p!= last)
                sb.append(",");
        }
        sb.append(last.data);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Iterator<E> iterator() {
        if(this.isEmpty()){
            throw new UnsupportedOperationException("lista vacía");
        }
        Iterator<E> it = new Iterator<E>(){
            Node<E> p = last.next;
            @Override
            public boolean hasNext() {
                return p != null;
            }
            @Override
            public E next() {
                E tmp = p.data;
                p = p.next;
                return tmp;
            }
        };
        return it;
    }

    public ListIterator<E> listIterator() {
        if(this.isEmpty()){
            throw new UnsupportedOperationException("lista vacía");
        }
        ListIterator<E> lit = new ListIterator<E>() {
            Node<E> p = last.next;
            @Override
            public boolean hasNext() {
                return p != null;
            }

            @Override
            public E next() {
                p = p.next;
                return p.data;
            }

            @Override
            public boolean hasPrevious() {
                return p != null;
            }

            @Override
            public E previous() {
                p = p.previous;
                return p.data;
            }

            @Override
            public int nextIndex() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'nextIndex'");
            }

            @Override
            public int previousIndex() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'previousIndex'");
            }

            @Override
            public void remove() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'remove'");
            }

            @Override
            public void set(E e) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'set'");
            }

            @Override
            public void add(E e) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'add'");
            }
            
        };
        return lit;
    }
}
