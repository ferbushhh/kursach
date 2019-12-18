import org.junit.*;
import java.util.*;
import java.util.function.IntFunction;

public class TreeTest {
    SplayTree<Integer> tree = new SplayTree<>();

    @Before
    public void addToTree() {
        tree.add(5);
        tree.add(9);
        tree.add(1);
        tree.add(0);
        tree.add(4);
        tree.add(6);
        tree.add(3);
        tree.add(8);
        tree.add(7);
    }

    @After
    public void clearTree() {
        tree.clear();
    }

    @Test
    public void add() {
        SplayTree<Integer> treeTest = new SplayTree<>();

        Assert.assertEquals(0, treeTest.size()); //длина пустого  = 0

        treeTest.add(5);

        Assert.assertEquals(1, treeTest.size()); //длина с одним элементом = 1

        Integer i = treeTest.getRoot().getValue();

        Assert.assertEquals((Object) 5, i); //проверка, что добавили корень со значением 5

        Assert.assertEquals(9, tree.size()); //длина = 9

        Assert.assertTrue(tree.contains(7)); //дерево содержит 7

        Assert.assertFalse(tree.contains(202)); //не содержит 202

        tree.add(11);

        Assert.assertEquals(10, tree.size());//длина уже 10
    }

    @Test
    public void remove() {
        Assert.assertEquals(9, tree.size()); //изначально длина 9

        Assert.assertTrue(tree.contains(4));
        tree.remove(4); //удаляем 4
        Assert.assertFalse(tree.contains(4)); //наличие элемента после удаления = false
        Assert.assertEquals(8, tree.size()); //уже длина 8

        Assert.assertFalse(tree.remove(11)); //не можем удалить элемент, которого нет

        //удаляем все элементы
        tree.remove(7);
        tree.remove(8);
        tree.remove(3);
        tree.remove(6);
        tree.remove(0);
        tree.remove(1);
        tree.remove(9);
        tree.remove(5);

        Assert.assertEquals(0, tree.size()); //длина 0
    }

    @Test
    public void containsAll() {
        List<Integer> list = new LinkedList<>();

        list.add(5);
        list.add(0);
        list.add(4);

        Assert.assertTrue(tree.containsAll(list));

        list.add(202);

        Assert.assertFalse(tree.containsAll(list));
    }

    @Test
    public void retainAll() {
        List<Integer> list = new LinkedList<>();

        list.add(5);
        list.add(0);
        list.add(4);

        Assert.assertTrue(tree.retainAll(list));
        Assert.assertEquals(list.size(), tree.size());

        list.add(202);

        Assert.assertFalse(tree.retainAll(list));
    }

    @Test
    public void addAll() {
        List<Integer> list = new LinkedList<>();
        list.add(17);
        list.add(12);
        list.add(100);

        Assert.assertTrue(tree.addAll(list)); //добавила весь список
        Assert.assertTrue(tree.containsAll(list)); //проверили, что весь список есть
        Assert.assertEquals(12, tree.size());

        SplayTree<Integer> newTree = new SplayTree<>();

        Assert.assertTrue(newTree.addAll(list));
        Assert.assertTrue(newTree.containsAll(list));
        Assert.assertEquals(list.size(), newTree.size());
    }

    @Test
    public void removeAll() {
        List<Integer> list = new LinkedList<>();
        list.add(5);
        list.add(3);
        list.add(9);

        Assert.assertTrue(tree.removeAll(list));
        Assert.assertEquals(6, tree.size());
        Assert.assertFalse(tree.contains(9));
        Assert.assertFalse(tree.contains(5));
        Assert.assertFalse(tree.contains(3));

        SplayTree<Integer> newTree = new SplayTree<>();

        Assert.assertTrue(newTree.addAll(list));
        Assert.assertTrue(newTree.removeAll(list));
        Assert.assertEquals(0, newTree.size());
    }

    @Test
    public void clear() {
        tree.clear();
        Assert.assertEquals(0, tree.size());
    }

    @Test
    public void iterator() {
        Set<Integer> val = new HashSet<>();
        val.add(5);
        val.add(9);
        val.add(1);
        val.add(0);
        val.add(4);
        val.add(6);
        val.add(3);
        val.add(8);
        val.add(7);


        tree.forEach(value -> {
            Assert.assertTrue(tree.contains(value));
            val.remove(value);
        });
    }

    @Test
    public void first() {

        try {
            SplayTree treeEmpty = new SplayTree();
            treeEmpty.first();
        } catch (NoSuchElementException ignored) {
        }

        Assert.assertEquals((Object) 0, tree.first());
        tree.add(-1);
        Assert.assertEquals(-1, (Object) tree.first());
    }

    @Test
    public void last() {
        try {
            SplayTree treeEmpty = new SplayTree();
            treeEmpty.last();
        } catch (NoSuchElementException ignored) {
        }

        Assert.assertEquals((Object) 9, tree.last());
        tree.add(100);
        Assert.assertEquals((Object) 100, tree.last());
    }

    @Test
    public void isEmpty() {
        Assert.assertFalse(tree.isEmpty());

        SplayTree<Integer> treeEmpty = new SplayTree<>();

        Assert.assertTrue(treeEmpty.isEmpty());

        treeEmpty.add(1);

        Assert.assertFalse(treeEmpty.isEmpty());
    }

    @Test
    public void contains() {
        Assert.assertFalse(tree.contains(202));
        Assert.assertTrue(tree.contains(5));
    }

    @Test
    public void toArray() {
        //toArray()
        Object[] list = tree.toArray();
        for (Object node : list) {
            Assert.assertTrue(tree.contains(node));
        }

        //toArray(бла бла бла)
        Object[] myArray = new Integer[tree.size()];
        tree.toArray(myArray);

        for (Object node : myArray) {
            Assert.assertTrue(tree.contains(node));
        }
    }

    @Test
    public void subSet() {
        //правильно ли построили subSet по длине
        Assert.assertEquals(4, tree.subSet(1, 6).size());

        SplayTree<Integer> emptyTree = new SplayTree<>();
        Assert.assertEquals(0, emptyTree.subSet(0, 0).size());

        //правильно ли построили subSet по наполнению
        SortedSet<Integer> set = tree.subSet(0, 4);
        Assert.assertTrue(set.contains(0));
        Assert.assertTrue(set.contains(3));
        Assert.assertFalse(set.contains(4));

        //проверяем итератор по тому, сколько раз он прошелся по элементам
        final int[] i = {0};
        set.forEach(value -> {
            Assert.assertTrue(tree.contains(value));
            i[0]++;
        });
        Assert.assertEquals(3, i[0]);

        //еще проверяем итератор
        Set<Integer> setSecond = tree.subSet(3, 8);
        i[0] = 0;

        setSecond.forEach(value -> {
            Assert.assertTrue(tree.contains(value));
            i[0]++;
        });
        Assert.assertEquals(5, i[0]);

        //проверяем toArray()
        Object[] arr = setSecond.toArray();
        for (Object node : arr) {
            Assert.assertTrue(setSecond.contains(node));
        }

        //проверяем sub, tail, head set
        SortedSet<Integer> subSet = tree.subSet(1, 9);
        SortedSet subSetSecond = subSet.subSet(1, 5);
        Assert.assertEquals(3, subSetSecond.size());
        Assert.assertEquals(5, subSet.headSet(7).size());
        Assert.assertEquals(3, subSet.tailSet(6).size());

        //добавили в дерево элемент, размер subSeta поменялся
        tree.add(2);
        Assert.assertEquals(4, subSetSecond.size());

        //проверяем addAll, removeAll
        SplayTree<Integer> newTree = new SplayTree<>();
        newTree.add(1);
        newTree.add(8);
        newTree.add(9);

        Set<Integer> setNewTree = newTree.subSet(1, 8);
        Assert.assertEquals(1, setNewTree.size());

        //список содержит элементы, находящиеся в границах subSet
        List<Integer> list = new LinkedList<>();
        list.add(3);
        list.add(6);
        list.add(4);
        list.add(5);

        Assert.assertTrue(setNewTree.addAll(list)); //смогли добавить список
        Assert.assertEquals(5, setNewTree.size());

        setNewTree.removeAll(list);
        Assert.assertEquals(1, setNewTree.size());

        list.add(17);
        Assert.assertFalse(setNewTree.addAll(list)); //уже не можем добавить, т.к. содержится число, выходящее за пределы

        try {
            Set set1 = tree.subSet(4, 0);
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void headSet() {
        SplayTree<Integer> emptyTree = new SplayTree<>();
        Assert.assertEquals(0, emptyTree.headSet(0).size());

        Assert.assertEquals(5, tree.headSet(6).size());

        tree.add(-1);

        Assert.assertEquals(6, tree.headSet(6).size());
    }

    @Test
    public void tailSet() {
        SplayTree<Integer> emptyTree = new SplayTree<>();
        Assert.assertEquals(0, emptyTree.tailSet(0).size());

        Assert.assertEquals(4, tree.tailSet(6).size());

        tree.add(11);

        Assert.assertEquals(5, tree.tailSet(6).size());
    }
}