package view;

import dao.PersonDao;
import domain.Address;
import domain.Person;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class UserForm {
    private PersonDao personDao = new PersonDao();

    public void show() {
        JFrame frame = new JFrame("Person Form");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        PersonFormPanelBuilder rightPanelBuilder = new PersonFormPanelBuilder(personDao);
        PersonListPanelBuilder leftPanelBuilder = new PersonListPanelBuilder(personDao);
        rightPanelBuilder.setPersonListPanelBuilder(leftPanelBuilder);
        leftPanelBuilder.setPersonFormPanelBuilder(rightPanelBuilder);

        JComponent leftPanel = leftPanelBuilder.create();
        JPanel rightPanel = rightPanelBuilder.create();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);

        frame.add(splitPane);
        frame.setVisible(true);
        frame.pack();
    }

}
