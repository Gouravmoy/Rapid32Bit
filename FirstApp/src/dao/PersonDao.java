package dao;

import java.util.List;

import entity.Person;

public interface PersonDao {

	void addPerson(Person person);

	List<Person> getAllPersons();
	
	Person getPerson(int Id);

}
