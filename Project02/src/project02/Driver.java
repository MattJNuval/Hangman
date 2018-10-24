package project02;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JComboBox;

public class Driver {

    private JFrame frame;
    private JTextField textField;
    private JTextField textField_2;
    private JTextField textField_1;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Driver window = new Driver();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public Driver() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(300, 300, 650, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton btnFind = new JButton("Find");

        textField = new JTextField();
        textField.setColumns(10);

        JLabel lblFindWhat = new JLabel("Find what:");

        JCheckBox chckbxMatchCase = new JCheckBox("Match Case");

        JCheckBox chckbxWholeWord = new JCheckBox("Match Word");

        textField_2 = new JTextField();
        textField_2.setColumns(10);

        JButton btnReplace = new JButton("Browse");

        JScrollPane scrollPane = new JScrollPane();

        textField_1 = new JTextField();
        textField_1.setColumns(10);

        JLabel lblReplaceWhat = new JLabel("Replace what:");

        JComboBox comboBox = new JComboBox();
        GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                                        .addGroup(groupLayout.createSequentialGroup()
                                                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                                                        .addComponent(lblFindWhat)
                                                                        .addComponent(lblReplaceWhat))
                                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                                                        .addComponent(textField_2, GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
                                                                        .addGroup(groupLayout.createSequentialGroup()
                                                                                .addComponent(chckbxMatchCase)
                                                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                                                .addComponent(chckbxWholeWord))
                                                                        .addComponent(textField_1, GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)))
                                                        .addGroup(groupLayout.createSequentialGroup()
                                                                .addComponent(comboBox, 0, 189, Short.MAX_VALUE)
                                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                                .addComponent(textField, GroupLayout.PREFERRED_SIZE, 283, GroupLayout.PREFERRED_SIZE)))
                                                .addGap(18)
                                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
                                                        .addComponent(btnFind, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnReplace, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(43))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
                                                .addContainerGap())))
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnReplace)
                                        .addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnFind)
                                        .addComponent(lblFindWhat))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblReplaceWhat))
                                .addPreferredGap(ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(chckbxMatchCase)
                                        .addComponent(chckbxWholeWord))
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 291, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        JTextArea textArea = new JTextArea();
        scrollPane.setViewportView(textArea);
        frame.getContentPane().setLayout(groupLayout);

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu mnApp = new JMenu("App");
        menuBar.add(mnApp);

        JMenuItem mntmExit = new JMenuItem("Exit");
        mnApp.add(mntmExit);
    }
}
