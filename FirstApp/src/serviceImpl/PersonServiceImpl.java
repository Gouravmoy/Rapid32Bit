package serviceImpl;

import java.util.List;

import dao.PersonDao;
import daoImpl.PersonDaoImpl;
import entity.Person;
import service.PersonService;

public class PersonServiceImpl implements PersonService {
	PersonDao personDao;

	@Override
	public void addPerson(String firstName, String lastName) {
		personDao = new PersonDaoImpl();
		Person p = new Person();
		p.setFirstName(firstName);
		p.setLastName(lastName);
		p.setAge(50);
		p.setMarried(true);
		personDao.addPerson(p);

	}

	@Override
	public List<Person> getAllPersons() {
		personDao = new PersonDaoImpl();
		List<Person> persons = personDao.getAllPersons();
		return personDao.getAllPersons();
	}

	@Override
	public Person getPerson(int Id) {
		personDao = new PersonDaoImpl();
		return personDao.getPerson(Id);
	}

}
