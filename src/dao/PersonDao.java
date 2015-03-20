package dao;

import domain.Person;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PersonDao {

    private static final String FILE_PATH = "data.txt";

    private PersonTransformer personTransformer = new PersonTransformer();

    public List<Person> getAllPersons(){
        List<String> lines = readLines();
         List<Person> persons = new ArrayList<Person>();
        for (String line : lines) {
            Person person = personTransformer.reverse(line);
            persons.add(person);
        }
        return persons;
    }

    private boolean isSamePerson(String firstName, String middleInitial, String lastName, Person person) {
        return person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)
                && (person.getMiddleInitial().isEmpty() || person.getMiddleInitial().equals(middleInitial));
    }

    public String addPerson(Person personToAdd){
        try{
            String personRecord = personTransformer.transform(personToAdd);
            File file = new File (FILE_PATH);
            BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
            List<String> lines = readLines();
            for (String line : lines) {
                Person person = personTransformer.reverse(line);
                if(isSamePerson(personToAdd.getFirstName(), personToAdd.getMiddleInitial(), personToAdd.getLastName(), person)){
                    out.close();
                    return "Person already exists";
                }
            }
            out.write(personRecord);
            out.close();
            return "Person Added SuccessFully";
        } catch (Exception exp){
            return "Failed to process : " + exp.getMessage();
        }
    }

    public String deletePerson(long id) {
        List<String> lines = readLines();
        Iterator<String> linesIterator = lines.iterator();
        while(linesIterator.hasNext()) {
            String line = linesIterator.next();
            Person person = personTransformer.reverse(line);
           if(person.getId() == id){
               linesIterator.remove();
           }
        }
        try {
            writeLinesToFile(lines);
            return "Person deleted successfully";
        } catch (IOException e) {
            return "Failed to process : " + e.getMessage();
        }

    }

    private void writeLinesToFile(List<String> output) throws IOException {
        FileWriter writer = new FileWriter(FILE_PATH);
        for(String lineToWrite: output) {
            writer.write(lineToWrite);
        }
        writer.close();
    }

    private List<String> readLines(){
        List<String> lines = new ArrayList<String>();
        try {
            FileInputStream file = new FileInputStream(FILE_PATH);
            BufferedReader reader = new BufferedReader(new InputStreamReader(file));
            String line;
            try {
                while ((line = reader.readLine()) != null){
                    lines.add(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public String modifyPerson(Person personToUpdate){
        List<String> lines = readLines();
        List<String> updatedLines = new ArrayList<String>();
        for (String line : lines) {
            Person person = personTransformer.reverse(line);
            if(person.getId() == personToUpdate.getId()){
                updatedLines.add(personTransformer.transform(personToUpdate));
            } else {
                updatedLines.add(line);
            }
        }
        try {
            writeLinesToFile(updatedLines);
            return "Person Updated Successfully";
        } catch (IOException e) {
            return "Failed to process : " + e.getMessage();
        }
    }
}
