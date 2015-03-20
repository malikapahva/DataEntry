package view;

import dao.PersonDao;
import domain.Address;
import domain.Person;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PersonFormPanelBuilder {
    private PersonListPanelBuilder personListPanelBuilder;
    private PersonDao personDao;

    private JTextField firstNameTextField;
    private JTextField middleInitialTextField;
    private JTextField lastNameTextField;
    private JTextField address1TextField;
    private JTextField address2TextField;
    private JTextField cityTextField;
    private JTextField stateTextField;
    private JTextField zipCodeTextField;
    private JTextField phoneNumberTextField;
    private JComboBox<String> genderCombo;
    private JLabel messageLabel;
    JButton addButton;
    JButton updateButton;
    JButton deleteButton;
    JButton clearButton;

    private static final String[] GENDERS = {"M", "F"};


    public PersonFormPanelBuilder(PersonDao personDao) {
        this.personDao = personDao;
        initComponents();
        addListeners();
    }

    public void setPersonListPanelBuilder(PersonListPanelBuilder personListPanelBuilder) {
        this.personListPanelBuilder = personListPanelBuilder;
    }

    /**
     * This method creates components required for person form.
     *Also creates buttons to add, update, delete and clear.
     */
    private void initComponents() {
        firstNameTextField = new JTextField(20);
        middleInitialTextField = new JTextField(20);
        lastNameTextField = new JTextField(20);
        address1TextField = new JTextField(20);
        address2TextField = new JTextField(20);
        cityTextField = new JTextField(20);
        stateTextField = new JTextField(20);
        zipCodeTextField = new JTextField(20);
        phoneNumberTextField = new JTextField(20);
        genderCombo = new JComboBox<String>(GENDERS);
        genderCombo.setSelectedItem(null);
        messageLabel = new JLabel(" ");
        addButton = createAddButton();
        updateButton = createUpdateButton();
        deleteButton = createDeleteButton();
        clearButton = createClearButton();
       disableAllButtons();
    }

    /**
     * This method add listeners to components to validate the data
     * and update the button status.
     *
     */
    private void addListeners() {
        firstNameTextField.addKeyListener(new ValueChangeListener());
        middleInitialTextField.addKeyListener(new ValueChangeListener());
        lastNameTextField.addKeyListener(new ValueChangeListener());
        address1TextField.addKeyListener(new ValueChangeListener());
        address2TextField.addKeyListener(new ValueChangeListener());
        cityTextField.addKeyListener(new ValueChangeListener());
        stateTextField.addKeyListener(new ValueChangeListener());
        zipCodeTextField.addKeyListener(new ValueChangeListener());
        genderCombo.addKeyListener(new ValueChangeListener());
        phoneNumberTextField.addKeyListener(new ValueChangeListener());
        genderCombo.addItemListener(new ValueChangeListener());
    }

    /**
     * This method creates the form which includes data fields,
     * buttons and message label.
     *
     * @return JPanel which contains the whole form.
     */
    public JPanel create() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        JPanel dataFieldsPanel = createDataFieldsPanel();
        JPanel buttonsPanel = createButtonsPanel();
        rightPanel.add(dataFieldsPanel);
        rightPanel.add(buttonsPanel);
        JPanel messagePanel = new JPanel();
        messagePanel.add(messageLabel);
        messagePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        rightPanel.add(messagePanel);
        return rightPanel;
    }

    /**
     * This method creates a JPanel and add all the buttons
     * to it.
     *
     * @return JPanel which includes all the buttons.
     */
    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        buttonsPanel.add(addButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(clearButton);
        return buttonsPanel;
    }

    /**
     * This method creates the clear button and associates the actions
     * to clear data from text fields and set visibility of all
     * buttons with it.
     *
     * @return created Add button.
     */
    private JButton createClearButton() {
        return new JButton(new AbstractAction("Clear") {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
                personListPanelBuilder.clearListSelection();
               disableAllButtons();
            }
        });
    }

    /**
     * This method clears all the text fields' values.
     */
    public void clearForm() {
        firstNameTextField.setText("");
        middleInitialTextField.setText("");
        lastNameTextField.setText("");
        address1TextField.setText("");
        address2TextField.setText("");
        cityTextField.setText("");
        stateTextField.setText("");
        zipCodeTextField.setText("");
        genderCombo.setSelectedItem(null);
        phoneNumberTextField.setText("");
        messageLabel.setText(" ");
    }

    /**
     * This method creates the delete button and associates the actions
     * to delete data, display appropriate message and set visibility
     * of all buttons with it.
     *
     * @return created Add button.
     */
    private JButton createDeleteButton() {
        return new JButton(new AbstractAction("Delete") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Long selectedPersonId = personListPanelBuilder.getSelectedPersonId();
                if(selectedPersonId != null){
                    String result = personDao.deletePerson(selectedPersonId);
                    clearForm();
                    personListPanelBuilder.refreshList();
                    messageLabel.setText(result);
                    disableAllButtons();
                }

            }
        });
    }

    /**
     * This method creates the update button and associates the actions
     * to update data, display appropriate message and set visibility
     * of all buttons with it.
     *
     * @return created Update button.
     */
    private JButton createUpdateButton() {
        return new JButton(new AbstractAction("Update") {
            @Override
            public void actionPerformed(ActionEvent e) {
                long id = personListPanelBuilder.getSelectedPersonId();
                Person person = retrieveDataAndCreatePerson(id);
                String result = personDao.modifyPerson(person);
                clearForm();
                personListPanelBuilder.refreshList();
                messageLabel.setText(result);
               disableAllButtons();
            }
        });
    }

    /**
     * This method creates the add button and associates the action
     * to save data, display appropriate message and set visibility
     * of all buttons with it.
     *
     * @return created Add button.
     */
    private JButton createAddButton() {
        return new JButton(new AbstractAction("Add") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Person person = retrieveDataAndCreatePerson(System.currentTimeMillis());
                String result = personDao.addPerson(person);
                clearForm();
                personListPanelBuilder.refreshList();
                messageLabel.setText(result);
               disableAllButtons();
            }
        });
    }

    /**
     * This method retrieve the data from text fields and combo box
     * and creates a Person object.
     *
     * @return Person object created using retrieved values of data fields.
     */
    private Person retrieveDataAndCreatePerson(long id) {
        Person person = new Person(id);
        person.setFirstName(firstNameTextField.getText());
        person.setMiddleInitial(middleInitialTextField.getText());
        person.setLastName(lastNameTextField.getText());
        Address address = new Address();
        address.setAddress1(address1TextField.getText());
        address.setAddress2(address2TextField.getText());
        address.setCity(cityTextField.getText());
        address.setState(stateTextField.getText());
        address.setZipCode(zipCodeTextField.getText());
        person.setAddress(address);
        person.setGender(genderCombo.getSelectedItem().toString().charAt(0));
        person.setPhoneNumber(phoneNumberTextField.getText());
        return person;
    }

    /**
     * This method creates the row in form with labels,
     * text fields and combo box.
     *
     * @return JPanel which includes the labels, text fields and combo box.
     */
    private JPanel createDataFieldsPanel() {
        final JPanel dataFieldsPanel = new JPanel();
        dataFieldsPanel.setLayout(new GridLayout(10, 2));

//        dataFieldsPanel.setLayout(new BoxLayout(dataFieldsPanel, BoxLayout.Y_AXIS));
        dataFieldsPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        dataFieldsPanel.add(new JLabel("First Name"));
        dataFieldsPanel.add(firstNameTextField);
        dataFieldsPanel.add(new JLabel("Middle Initial"));
        dataFieldsPanel.add(middleInitialTextField);
        dataFieldsPanel.add(new JLabel("Last Name"));
        dataFieldsPanel.add(lastNameTextField);
        dataFieldsPanel.add(new JLabel("Address1"));
        dataFieldsPanel.add(address1TextField);
        dataFieldsPanel.add(new JLabel("Address2"));
        dataFieldsPanel.add(address2TextField);
        dataFieldsPanel.add(new JLabel("City"));
        dataFieldsPanel.add(cityTextField);
        dataFieldsPanel.add(new JLabel("State"));
        dataFieldsPanel.add(stateTextField);
        dataFieldsPanel.add(new JLabel("Zip Code"));
        dataFieldsPanel.add(zipCodeTextField);
        dataFieldsPanel.add(new JLabel("Gender"));
        dataFieldsPanel.add(genderCombo);
        dataFieldsPanel.add(new JLabel("Phone Number"));
        dataFieldsPanel.add(phoneNumberTextField);

        return dataFieldsPanel;
    }

    /**
     * This method takes the label name and text field name and
     * creates a row in the form.
     *
     * @param label
     * @param textField
     * @return JPanel which includes the created row
     */
    private JPanel createRow(String label, JTextField textField) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JLabel jLabel = new JLabel(label);
        jLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        row.add(jLabel);
        row.add(textField);
        return row;
    }

    /**
     * This method updates or sets the values in the TextFields and
     * ComboBox when any record in the list is selected.
     *
     * @param person
     */
    public void updateDataFields(Person person) {
        firstNameTextField.setText(person.getFirstName());
        middleInitialTextField.setText(person.getMiddleInitial());
        lastNameTextField.setText(person.getLastName());
        address1TextField.setText(person.getAddress().getAddress1());
        address2TextField.setText(person.getAddress().getAddress2());
        cityTextField.setText(person.getAddress().getCity());
        stateTextField.setText(person.getAddress().getState());
        zipCodeTextField.setText(person.getAddress().getZipCode());
        genderCombo.setSelectedItem(String.valueOf(person.getGender()));
        phoneNumberTextField.setText(person.getPhoneNumber());
    }

    private class ValueChangeListener extends KeyAdapter implements ItemListener {
        @Override
        public void keyReleased(KeyEvent e) {
            updateButtonStatus();
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            updateButtonStatus();
        }

        /**
         * This method checks and validates button status.
         */
        private void updateButtonStatus() {
            boolean enableUpdate = areMandatoryFieldsFilled() && personListPanelBuilder.getSelectedPersonId() != null;
            boolean enableAdd = areMandatoryFieldsFilled() && personListPanelBuilder.getSelectedPersonId() == null;
            addButton.setEnabled(enableAdd);
            updateButton.setEnabled(enableUpdate);
            clearButton.setEnabled(isAnyFieldFilled());
        }

        /**
         * This method checks for valid data entry.
         *
         * @return boolean.
         */
        private boolean areMandatoryFieldsFilled() {
            return isValidRange(firstNameTextField.getText().trim(), 1, 20)
                    && isValidRange(lastNameTextField.getText().trim(), 1, 20)
                    && isValidRange(middleInitialTextField.getText().trim(), 0, 1)
                    && isValidRange(address1TextField.getText().trim(), 1, 35)
                    && isValidRange(address2TextField.getText().trim(), 0, 35)
                    && isValidRange(cityTextField.getText().trim(), 1, 25)
                    && isValidRange(stateTextField.getText().trim(), 1, 2)
                    && isValidRange(zipCodeTextField.getText().trim(), 1, 9) && isNumber(zipCodeTextField.getText().trim())
                    && isValidRange(phoneNumberTextField.getText().trim(), 1, 21)  && isNumber(phoneNumberTextField.getText().trim())
                    && genderCombo.getSelectedItem() != null;
        }

        /**
         * This method checks the length of given data field.
         *
         * @return boolean.
         */
        private boolean isValidRange(String value, int min, int max) {
            return value.length() >= min && value.length() <= max;
        }

        /**
         * This method checks whether entered data is number or not.
         *
         * @return boolean.
         */
        private boolean isNumber(String value) {
            try {
                Long.parseLong(value);
                return true;
            } catch (NumberFormatException ex) {
                return false;
            }
        }

        /**
         * This method checks whether there is data in any field or not.
         *
         * @return boolean.
         */
        private boolean isAnyFieldFilled() {
            return !firstNameTextField.getText().isEmpty() || !lastNameTextField.getText().isEmpty() || (!address1TextField.getText().isEmpty())
                    || (!cityTextField.getText().isEmpty()) || (!stateTextField.getText().isEmpty()) || (!zipCodeTextField.getText().isEmpty())
                    || (!phoneNumberTextField.getText().isEmpty()) || (genderCombo.getSelectedItem() != null)
                    || !middleInitialTextField.getText().isEmpty() || !address2TextField.getText().isEmpty();
        }
    }

    /**
     * This method disable all the Buttons.
     */
    private void disableAllButtons(){
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        addButton.setEnabled(false);
        clearButton.setEnabled(false);
    }
}
