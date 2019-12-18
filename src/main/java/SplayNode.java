public class SplayNode<T> {

    private SplayNode<T> left, right;

    private T value;

    public SplayNode(T value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public void setLeft (SplayNode<T> node) {
        this.left = node;
    }

    public void setRight (SplayNode<T> node) {
        this.right = node;
    }

    public T getValue() {
        return value;
    }

    public SplayNode<T> getLeft() {
        return left;
    }

    public SplayNode<T> getRight() {
        return right;
    }
}