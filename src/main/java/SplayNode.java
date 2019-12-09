public class SplayNode<T> {

    private SplayNode<T> left, right;

    private T value;

    public SplayNode(T value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public void setLeft (SplayNode node) {
        this.left = node;
    }

    public void setRight (SplayNode node) {
        this.right = node;
    }

    public T getValue() {
        return value;
    }

    public SplayNode getLeft() {
        return left;
    }

    public SplayNode getRight() {
        return right;
    }
}