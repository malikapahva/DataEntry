package view;

import domain.Person;

import javax.swing.*;
import java.awt.*;

public class PersonListRenderer extends DefaultListCellRenderer {
    /**
     * This method render the person object in a list to person name and contact.
     */
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Person person = (Person) value;
        return super.getListCellRendererComponent(list, person.getName() + " - " + person.getPhoneNumber(), index, isSelected, cellHasFocus);
    }

}
