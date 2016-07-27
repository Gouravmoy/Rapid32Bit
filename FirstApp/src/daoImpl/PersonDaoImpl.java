package daoImpl;

import java.util.List;

import dao.PersonDao;
import entity.Person;

public class PersonDaoImpl extends GenericDAOImpl<Person, Integer> implements PersonDao {

	@Override
	public void addPerson(Person person) {
		save(person);
	}

	@Override
	public List<Person> getAllPersons() {
		return readAll("Person.finadAll", Person.class);
	}

	@Override
	public Person getPerson(int Id) {
		return readById(Person.class, Id);
	}

}
