package test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import check.ConnectToDB;
import entity.Person;
import service.PersonService;
import serviceImpl.PersonServiceImpl;

public class Demo_old {

	public static void main(String[] args) {

		// getData();
		getAllPersons();
	}

	public static List<Person> getAllPersons() {
		PersonService personService = new PersonServiceImpl();
		System.out.println(personService.getAllPersons().size());
		return personService.getAllPersons();

	}

	private static void getData() {

		Connection c = ConnectToDB.ConnectToDBC();
		try {
			PreparedStatement stmt = c.prepareStatement("select * from Person");
			ResultSet r = stmt.getResultSet();
			while (r.next()) {
				System.out.println(r.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
