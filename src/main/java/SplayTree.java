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

    private SplayNode splay(SplayNode node, T value) {
        if (node == null) return null;

        if (value.compareTo((T) node.getValue()) < 0) {
            if (node.getLeft() == null) return node;

            if (value.compareTo((T) node.getLeft().getValue()) < 0) {
                node.getLeft().setLeft(splay(node.getLeft().getLeft(), value));
                node = rotateRight(node);
            } else if (value.compareTo((T) node.getLeft().getValue()) > 0) {
                node.getLeft().setRight(splay(node.getLeft().getRight(), value));
                if (node.getLeft().getRight() != null)
                    node.setLeft(rotateLeft(node.getLeft()));
            }
            return node.getLeft() == null ? node : rotateRight(node);
        } else if (value.compareTo((T) node.getValue()) > 0) {
            if (node.getRight() == null) return node;

            if (value.compareTo((T) node.getRight().getValue()) < 0) {
                node.getRight().setLeft(splay(node.getRight().getLeft(), value));
                if (node.getRight().getLeft() != null)
                    node.setRight(rotateRight(node.getRight()));
            } else if (value.compareTo((T) node.getRight().getValue()) > 0) {
                node.getRight().setRight(splay(node.getRight().getRight(), value));
                node = rotateLeft(node);
            }
            return node.getRight() == null ? node : rotateLeft(node);
        } else return node;
    }

    public Comparator<? super T> comparator() { //+
        return new SecondaryComparator<T>();
    }

    public class SecondaryComparator<T extends Comparable<T>> implements Comparator<T> {
        public int compare(T o1, T o2) {
            return o1.compareTo(o2);
        }
    }

    public SortedSet<T> subSet(T fromElement, T toElement) { //+
        if (fromElement.compareTo(toElement) > 0) throw new NoSuchElementException();
        return new SplaySubSet<>(this, toElement, fromElement, false, false);
    }

    public SortedSet<T> headSet(T toElement) { //+
        return new SplaySubSet<>(this, toElement, null, true, false);
    }

    public SortedSet<T> tailSet(T fromElement) { //+
        return new SplaySubSet<>(this, null, fromElement, false, true);
    }

    public T first() { //+
        if (root == null) return null;
        SplayNode<T> cur = root;
        while (cur.getLeft() != null) {
            cur = cur.getLeft();
        }
        return cur.getValue();
    }

    public T last() { //+
        if (root == null) return null;
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

        boolean res = root.getValue().equals(value);

        return res;
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

    public <T> T[] toArray(T[] a) { //+
        return (T[]) this.toArray();
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

        private SplayTree splayTree;
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

        T topBorder;
        T bottomBorder;

        boolean toLast;
        boolean fromFirst;

        SplayTree<T> subTree;

        SplaySubSet(SplayTree tree, T topBorder, T bottomBorder, boolean fromFirst, boolean toLast) {
            this.topBorder = topBorder;
            this.bottomBorder = bottomBorder;

            this.fromFirst = fromFirst;
            this.toLast = toLast;

            this.subTree = tree;
        }

        boolean isInside(Object o) {
            T val = (T) o;

            if (bottomBorder != null && topBorder != null) {
                return val.compareTo(bottomBorder) >= 0 && val.compareTo(topBorder) < 0;
            } else if (bottomBorder == null) {
                return val.compareTo(topBorder) < 0;
            } else
                return val.compareTo(bottomBorder) >= 0;
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
            if (isInside(o)) {
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
            return subTree.addAll(c);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return subTree.retainAll(c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return subTree.removeAll(c);
        }

        @Override
        public void clear() {
            subTree.clear();
        }

        @Override
        public boolean contains(Object o) {
            if (isInside(o)) {
                return subTree.contains(o);
            }
            return false;
        }

        @Override
        public Iterator<T> iterator() {
            return subTree.iterator();
        }

        @Override
        public Object[] toArray() {
            return subTree.toArray();
        }

        @Override
        public <T1> T1[] toArray(T1[] a) {
            return subTree.toArray(a);
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
            if (fromElement.compareTo(toElement) > 0) throw new NoSuchElementException();
            return new SplaySubSet<>(subTree, fromElement, toElement, false, false);
        }

        @Override
        public SortedSet<T> headSet(T toElement) {
            return new SplaySubSet<>(subTree, bottomBorder, toElement, fromFirst, false);
        }

        @Override
        public SortedSet<T> tailSet(T fromElement) {
            return new SplaySubSet<>(subTree, fromElement, topBorder, false, toLast);
        }

        @Override
        public T first() {
            if (bottomBorder == null)
                return subTree.first();
            else if (toLast)
                return bottomBorder;
            else {
                Iterator<T> iter = subTree.iterator();
                T temp = null;

                while (iter.hasNext()) {
                    temp = iter.next();
                    if (temp.compareTo(bottomBorder) == 0) {
                        temp = iter.next();
                        break;
                    }
                }

                return temp;
            }
        }

        @Override
        public T last() {
            if (topBorder == null)
                return subTree.last();
            else {
                Iterator<T> iter = subTree.iterator();
                T temp;
                T res = null;
                while (iter.hasNext()) {
                    temp = iter.next();
                    if (temp.compareTo(topBorder) == 0)
                        break;
                    res = temp;
                }
                return res;
            }
        }
    }
}