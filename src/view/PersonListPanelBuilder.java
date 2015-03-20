package view;

import dao.PersonDao;
import domain.Person;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;

public class PersonListPanelBuilder {
    private PersonDao personDao;
    private PersonFormPanelBuilder personFormPanelBuilder;

    private JList<Person> personJList;

    public PersonListPanelBuilder(PersonDao personDao) {
        this.personDao = personDao;
        initComponent();
    }

    public void setPersonFormPanelBuilder(PersonFormPanelBuilder personFormPanelBuilder) {
        this.personFormPanelBuilder = personFormPanelBuilder;
    }

    private void initComponent() {
        personJList = new JList<Person>();
        personJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        personJList.setCellRenderer(new PersonListRenderer());
    }

    /**
     * This method creates the JList component to display records that
     * are stored in file.
     *
     * @return JComponent.
     */
    public JComponent create(){
        List<Person> persons = personDao.getAllPersons();
        final Person[] personArray = persons.toArray(new Person[persons.size()]);
        personJList.setModel(createModel(personArray));
        personJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Person person = (Person)((JList) e.getSource()).getSelectedValue();
                personFormPanelBuilder.clearForm();
                if (person != null) {
                    personFormPanelBuilder.updateDataFields(person);
                    personFormPanelBuilder.updateButton.setEnabled(true);
                    personFormPanelBuilder.deleteButton.setEnabled(true);
                    personFormPanelBuilder.clearButton.setEnabled(true);
                }
            }
        });

        personJList.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 5));
        JScrollPane leftPanel = new JScrollPane(personJList);
        leftPanel.setMinimumSize(new Dimension(200, leftPanel.getHeight()));
        return leftPanel;
    }

    /**
     * This method fetches the data from file and update the list.
     */
    public void refreshList(){
        List<Person> persons = personDao.getAllPersons();
        final Person[] personArray = persons.toArray(new Person[persons.size()]);
        personJList.setModel(createModel(personArray));
    }

    /**
     * This method clears the selection from the list.
     *
     */
    public void clearListSelection(){
        personJList.clearSelection();
    }

    /**
     * This method creates the model for list.
     *
     * @return Person model.
     */
    private ListModel<Person> createModel(final Person[] personArray) {
        return new AbstractListModel<Person>() {
            public int getSize() { return personArray.length; }
            public Person getElementAt(int i) { return personArray[i]; }
        };
    }

    /**
     * This method gives the selected person id.
     *
     * @return Long id.
     */
    public Long getSelectedPersonId() {
        Person selectedValue = personJList.getSelectedValue();
        if(selectedValue != null) {
            return selectedValue.getId();
        }
        return null;
    }
}
