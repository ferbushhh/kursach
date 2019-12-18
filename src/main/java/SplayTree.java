import java.util.*;

public class SplayTree<T extends Comparable<T>> implements SortedSet<T> {

    private int size;
    private SplayNode<T> root;

    public SplayTree() {
        this.root = null;
        this.size = 0;
    }

    public SplayNode<T> getRoot() {
        return this.root;
    }

    private SplayNode<T> rotateRight(SplayNode<T> node) {
        SplayNode<T> value = node.getLeft();
        node.setLeft(value.getRight());
        value.setRight(node);
        return value;
    }

    private SplayNode<T> rotateLeft(SplayNode<T> node) {
        SplayNode<T> value = node.getRight();
        node.setRight(value.getLeft());
        value.setLeft(node);
        return value;
    }

    private SplayNode<T> splay(SplayNode<T> node, T value) {
        if (node == null) return null;

        if (value.compareTo(node.getValue()) < 0) {
            if (node.getLeft() == null) return node;

            if (value.compareTo(node.getLeft().getValue()) < 0) {
                node.getLeft().setLeft(splay(node.getLeft().getLeft(), value));
                node = rotateRight(node);
            } else if (value.compareTo(node.getLeft().getValue()) > 0) {
                node.getLeft().setRight(splay(node.getLeft().getRight(), value));
                if (node.getLeft().getRight() != null)
                    node.setLeft(rotateLeft(node.getLeft()));
            }
            return node.getLeft() == null ? node : rotateRight(node);
        } else if (value.compareTo(node.getValue()) > 0) {
            if (node.getRight() == null) return node;

            if (value.compareTo(node.getRight().getValue()) < 0) {
                node.getRight().setLeft(splay(node.getRight().getLeft(), value));
                if (node.getRight().getLeft() != null)
                    node.setRight(rotateRight(node.getRight()));
            } else if (value.compareTo(node.getRight().getValue()) > 0) {
                node.getRight().setRight(splay(node.getRight().getRight(), value));
                node = rotateLeft(node);
            }
            return node.getRight() == null ? node : rotateLeft(node);
        } else return node;
    }

    public Comparator<? super T> comparator() { //+
        return new SecondaryComparator<T>();
    }

    public static class SecondaryComparator<T extends Comparable<T>> implements Comparator<T> {
        public int compare(T o1, T o2) {
            return o1.compareTo(o2);
        }
    }

    public SortedSet<T> subSet(T fromElement, T toElement) { //+
        if (fromElement.compareTo(toElement) > 0) throw new IllegalArgumentException();
        return new SplaySubSet<>(this, fromElement, toElement);
    }

    public SortedSet<T> headSet(T toElement) { //+
        return new SplaySubSet<>(this, null, toElement);
    }

    public SortedSet<T> tailSet(T fromElement) { //+
        return new SplaySubSet<>(this, fromElement, null);
    }

    public T first() { //+
        if (this.size() == 0) throw new NoSuchElementException();
        SplayNode<T> cur = root;
        while (cur.getLeft() != null) {
            cur = cur.getLeft();
        }
        return cur.getValue();
    }

    public T last() { //+
        if (this.size() == 0) throw new NoSuchElementException();
        SplayNode<T> cur = root;
        while (cur.getRight() != null) {
            cur = cur.getRight();
        }
        return cur.getValue();
    }

    public int size() { //+
        return this.size;
    }

    public boolean isEmpty() { //+
        return (this.root == null);
    }

    public boolean contains(Object o) { //+
        if (this.root == null) return false;

        T value = (T) o;

        root = splay(root, value);

        return root.getValue().equals(value);
    }

    public Iterator<T> iterator() { //+
        return new SplayTreeIterator<>(this);
    }

    public Object[] toArray() { //+
        Object[] arr = new Object[size];
        int i = 0;
        for (T value : this) {
            arr[i] = value;
            i++;
        }
        return arr;
    }

    @SuppressWarnings("unchecked")
    public <T1> T1[] toArray(T1[] a) {
        T1[] mas;

        if (a.length >= size())
            mas = a;
        else
            mas = (T1[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), this.size());
        Iterator<T> iter = iterator();
        for (int i = 0; i < mas.length; i++) {
            if (!iter.hasNext()) {
                mas[i] = null;
                return mas;
            }
            mas[i] = (T1) iter.next();
        }
        return mas;
    }

    public boolean add(T value) { //+

        if (this.isEmpty()) {
            this.root = new SplayNode<>(value);
            size = 1;
            return true;
        }

        root = splay(root, value);

        int comp = value.compareTo(root.getValue());

        SplayNode<T> node = new SplayNode<T>(value);

        if (comp < 0) { //левая ветка
            node.setLeft(root.getLeft());
            node.setRight(root);
            root.setLeft(null);
            root = node;
            this.size++;
            return true;
        } else if (comp > 0) { //правая ветка
            node.setRight(root.getRight());
            node.setLeft(root);
            root.setRight(null);
            root = node;
            this.size++;
            return true;
        } else return false;
    }

    public boolean remove(Object o) { //+
        if (root == null) return false;

        T value = (T) o;

        root = splay(root, value);

        if (value.equals(root.getValue())) {
            if (root.getLeft() != null) {
                SplayNode<T> node = root.getRight();
                root = root.getLeft();
                splay(root, value);
                root.setRight(node);
            } else {
                root = root.getRight();
            }
            size--;
            return true;
        } else {
            return false;
        }
    }

    public boolean containsAll(Collection<?> c) { //+
        for (Object o : c)
            if (!contains(o)) return false;
        return true;
    }

    public boolean addAll(Collection<? extends T> c) { //+
        int n = this.size;
        for (Object o : c)
            add((T) o);
        return this.size > n;
    }

    public boolean retainAll(Collection<?> c) { //+
        int n = this.size;
        for (Object o : this)
            if (!c.contains(o)) this.remove(o);
        return this.size < n;
    }

    public boolean removeAll(Collection<?> c) { //+
        int n = this.size;
        for (Object o : c)
            if (contains(o)) this.remove(o);
        return this.size < n;
    }

    public void clear() { //+
        for (Object o : this)
            remove((T) o);
        this.size = 0;
    }

    public class SplayTreeIterator<T extends Comparable<T>> implements Iterator<T> { //такое же как и в BinaryTree (в котоеде)

        private SplayTree<T> splayTree;
        private Stack<SplayNode<T>> stack;

        public SplayTreeIterator(SplayTree splayTree) {
            this.splayTree = splayTree;

            stack = new Stack<>();
            SplayNode<T> current = this.splayTree.getRoot();
            while (current != null) {
                stack.push(current);
                current = current.getLeft();
            }

        }

        public boolean hasNext() {
            return !stack.isEmpty();
        }

        public T next() {
            if (!hasNext()) throw new NoSuchElementException();

            SplayNode<T> small = stack.pop();

            if (small.getRight() != null) {
                SplayNode<T> splayNode = small.getRight();
                while (splayNode != null) {
                    stack.push(splayNode);
                    splayNode = splayNode.getLeft();
                }
            }

            return small.getValue();
        }

        public void remove() {
            splayTree.remove(next());
        }
    }

    public class SplaySubSet<T extends Comparable<T>> implements SortedSet<T> {

        T toElement; //topBorder
        T fromElement; //bottomBorder

        SplayTree<T> subTree;

        SplaySubSet(SplayTree tree, T fromElement, T toElement) {
            this.fromElement = fromElement;
            this.toElement = toElement;

            this.subTree = tree;
        }

        boolean isInside(Object o) {
            T val = (T) o;

            if (fromElement != null && toElement != null) {
                return toElement.compareTo(val) >= 0 && fromElement.compareTo(val) < 0;
            } else if (fromElement == null) {
                return toElement.compareTo(val) > 0;
            } else
                return fromElement.compareTo(val) <= 0;
        }

        @Override
        public boolean add(T val) {
            if (isInside(val)) {
                subTree.add(val);
                return true;
            } else
                return false;
        }

        @Override
        public boolean remove(Object o) {
            if (isInside(o) && this.contains(o)) {
                subTree.remove(o);
                return true;
            } else
                return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            for (Object o : c)
                if (!contains(o)) return false;
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends T> c) {
            int n = this.size();
            for (Object o : c)
                if (!isInside(o)) return false;
            for (Object o : c)
                this.add((T) o);
            return this.size() > n;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return this.retainAll(c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            int n = this.size();
            for (Object o : c)
                if (!isInside(o)) return false;
            for (Object o : c)
                this.remove((T) o);
            return this.size() < n;
        }

        @Override
        public void clear() {
            this.clear();
        }

        @Override
        public boolean contains(Object o) {
            T val = (T) o;
            for (T t: this)
                if (val.compareTo(t) == 0) return true;
            return false;
        }

        @Override
        public Iterator<T> iterator() {
            return new SplayTreeSetIterator();
        }

        private boolean elementInInterval(T element) {
            if (fromElement == null && toElement == null) return false;
            return (fromElement == null || element.compareTo(fromElement) >= 0) && (toElement == null || element.compareTo(toElement) < 0);
        }

        public class SplayTreeSetIterator implements Iterator<T> {

            private final Iterator<T> treeIt = SplaySubSet.this.subTree.iterator();

            private T next = null;

            SplayTreeSetIterator() {
                while (treeIt.hasNext()) {
                    final T next = treeIt.next();
                    if (elementInInterval(next)) {
                        this.next = next;
                        break;
                    }
                }
            }

            @Override
            // Проверка на наличие следующего элемента
            public boolean hasNext() {
                return next != null;
            }

            @Override
            // Переход к следующему элементу
            public T next() {
                if (next == null) throw new NoSuchElementException();
                final T result = next;
                next = treeIt.hasNext() ? treeIt.next() : null;
                if (!elementInInterval(next)) next = null;
                return result;
            }

            @Override
            // Удаление текущего элемента
            public void remove() {
                treeIt.remove();
            }
        }

        @Override
        public Object[] toArray() {
            Object[] ob = new Object[size()];
            Iterator<T> iter = this.iterator();
            for (int i = 0; i < size(); i++) {
                if (iter.hasNext()) {
                    ob[i] = iter.next();
                }
            }
            return ob;
        }

        @Override
        public <T1> T1[] toArray(T1[] a) {
            T1[] mas;
            if (a.length >= size)
                mas = a;
            else
                mas = (T1[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), this.size());

            Iterator<T> iter = this.iterator();
            for (int i = 0; i < mas.length; i++) {
                if (!iter.hasNext()) {
                    mas[i] = null;
                    return mas;
                }
                mas[i] = (T1) iter.next();
            }
            return mas;
        }

        @Override
        public int size() {
            int size = 0;
            for (T aTree : subTree) {
                if (isInside(aTree))
                    size++;
            }
            return size;
        }

        @Override
        public boolean isEmpty() {
            return size() == 0;
        }

        @Override
        public Comparator<? super T> comparator() {
            return subTree.comparator();
        }

        @Override
        public SortedSet<T> subSet(T fromElement, T toElement) {
            if (fromElement.compareTo(toElement) > 0) throw new IllegalArgumentException();
            return new SplaySubSet<>(subTree, fromElement, toElement);
        }

        @Override
        public SortedSet<T> headSet(T toElement) {
            return new SplaySubSet<>(subTree, fromElement, toElement);
        }

        @Override
        public SortedSet<T> tailSet(T fromElement) {
            return new SplaySubSet<>(subTree, fromElement, toElement);
        }

        @Override
        public T first() {
            if (fromElement == null)
                return subTree.first();
            else {
                Iterator<T> iter = subTree.iterator();
                T temp = null;

                while (iter.hasNext()) {
                    temp = iter.next();
                    if (temp.compareTo(fromElement) == 0) {
                        temp = iter.next();
                        break;
                    }
                }

                return temp;
            }
        }

        @Override
        public T last() {
            if (toElement == null)
                return subTree.last();
            else {
                Iterator<T> iter = subTree.iterator();
                T temp;
                T res = null;
                while (iter.hasNext()) {
                    temp = iter.next();
                    if (temp.compareTo(toElement) == 0)
                        break;
                    res = temp;
                }
                return res;
            }
        }
    }
}