package dao;

import domain.Address;
import domain.Person;

public class PersonTransformer {
    public static final String PERSON_DELIMITER = "\t";
    public static final String ADD_DELIMITER = "|";
    public static final String BLANK = " ";

    /**
     * This method converts the Person object to String.
     *
     * @param person
     *
     * @return String
     */
    public String transform(Person person) {
        return person.getId() + PERSON_DELIMITER + person.getFirstName() + PERSON_DELIMITER + nullSafe(person.getMiddleInitial()) + PERSON_DELIMITER + person.getLastName() + PERSON_DELIMITER
                + transformAddress(person.getAddress()) + PERSON_DELIMITER + person.getGender() + PERSON_DELIMITER + person.getPhoneNumber() + "\n";
    }

    /**
     * This method converts the String to Person object.
     *
     * @param line
     *        person detail in String form.
     *
     * @return Person
     */
    public Person reverse(String line) {
        String[] personRecord = line.split(PERSON_DELIMITER);
        String[] addressDetail = personRecord[4].split("\\" +ADD_DELIMITER);
        Long id = Long.valueOf(personRecord[0]);
        Person person = new Person(id);
        Address address = new Address();
        address.setAddress1(addressDetail[0]);
        address.setAddress2(addressDetail[1].trim());
        address.setCity(addressDetail[2]);
        address.setState(addressDetail[3]);
        address.setZipCode(addressDetail[4]);
        person.setFirstName(personRecord[1]);
        person.setMiddleInitial(personRecord[2].trim());
        person.setLastName(personRecord[3]);
        person.setAddress(address);
        person.setGender(personRecord[5].charAt(0));
        person.setPhoneNumber(personRecord[6].substring(0,personRecord[6].length()-1));
        return person;
    }

    /**
     * This method converts the Address object to String.
     *
     * @param address
     *        contains the address of the person.
     *
     * @return String
     */
    private String transformAddress(Address address) {
        return address.getAddress1() + ADD_DELIMITER + nullSafe(address.getAddress2()) + ADD_DELIMITER
                + address.getCity() + ADD_DELIMITER + address.getState() + ADD_DELIMITER + address.getZipCode();
    }

    /**
     * This method checks for any null value and replaces
     * it with empty String(BLANK).
     *
     * @param value
     *
     * @return String.
     */
    private String nullSafe(String value) {
        if (value == null) {
            return BLANK;
        }
        return value;
    }

}
