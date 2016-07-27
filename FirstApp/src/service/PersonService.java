package service;

import java.util.List;

import entity.Person;

public interface PersonService {
	void addPerson(String firstName, String lastName);

	List<Person> getAllPersons();
	
	Person getPerson(int Id);
}
