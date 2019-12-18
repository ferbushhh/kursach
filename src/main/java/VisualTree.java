import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class VisualTree extends JFrame {

    int height = 700;
    int width = 1010;
    private Board board;
    private JPanel optionPanel;
    private String elements;

    public static void main(String[] args) {
        VisualTree visualTree = new VisualTree("Splay Tree");
    }

    public VisualTree(String name) {
        super(name);
        this.setLayout(new BorderLayout());
        setSize(width, height);
        initTreePanel();
        initOptionPanel();
        setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initTreePanel() {
        board = new Board(380, 15);
        this.add(board, BorderLayout.CENTER);
    }

    private void initAddAndRemovePanel() {
        JPanel addPanel = new JPanel();
        addPanel.setBackground(new Color(255, 182, 193));
        addPanel.setPreferredSize(new Dimension(160, 85));
        addPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel head = new JLabel("ДОБАВИТЬ В ДЕРЕВО");
        addPanel.add(head);
        JTextField textInsert = new JTextField(10);
        addPanel.add(textInsert);
        ActionListener actionListener = e -> {
            if (!textInsert.getText().matches("-?[0-9]+")) {
                elements = "Можно добавить только целое число :(";
                this.showWarring();
                return;
            }
            board.addNode(Integer.parseInt(textInsert.getText()));
            repaint();
        };
        JButton buttonInsert = new JButton("ОК");
        buttonInsert.addActionListener(actionListener);
        addPanel.add(buttonInsert);
        optionPanel.add(addPanel);

        JPanel removePanel = new JPanel();
        removePanel.setPreferredSize(new Dimension(160, 85));
        removePanel.setBackground(new Color(255, 182, 193));
        removePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel remove = new JLabel("УДАЛИТЬ ИЗ ДЕРЕВА");
        removePanel.add(remove);
        JTextField textRemove = new JTextField(10);
        removePanel.add(textRemove);
        ActionListener listener = e -> {
            if (!textRemove.getText().matches("-?[0-9]+")) {
                elements = "Можно удалить только целое число :(";
                this.showWarring();
                return;
            }
            board.removeNode(Integer.parseInt(textRemove.getText()));
            repaint();
        };
        JButton buttonRemove = new JButton("ОК");
        buttonRemove.addActionListener(listener);
        removePanel.add(buttonRemove);
        optionPanel.add(removePanel);
    }

    private void initOptionPanel() {
        optionPanel = new JPanel();
        optionPanel.setPreferredSize(new Dimension(190, 300));
        optionPanel.setBackground(new Color(122, 100, 100));
        initAddAndRemovePanel();
        JButton exitButton = new JButton("Выйти");
        exitButton.addActionListener(e -> System.exit(0));
        optionPanel.add(exitButton);
        ActionListener actionListener = e -> {
            board.clearBoard();
            repaint();
        };
        JButton clear = new JButton("Очистить");
        clear.addActionListener(actionListener);
        optionPanel.add(clear);
        this.add(optionPanel, BorderLayout.WEST);
    }

    private void showWarring() {
       JOptionPane.showMessageDialog(this,elements,"Ошибка!",JOptionPane.WARNING_MESSAGE);
    }
}
