package controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.hibernate.Session;

public class JpaTest {

	//static final Logger logger = Logger.getLogger(JpaTest.class);

	private EntityManager manager;

	public JpaTest(EntityManager manager) {
		this.manager = manager;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("persistenceUnit");
		EntityManager manager = factory.createEntityManager();

		JpaTest test = new JpaTest(manager);
		Session session = manager.unwrap(Session.class);

		EntityTransaction tx = manager.getTransaction();
		tx.begin();
		try {
			// test.createEmployees();
		} catch (Exception e) {
			e.printStackTrace();
			//logger.error(e);
		}
		tx.commit();

		// test.listEmployees();

		System.out.println(".. done");
	}

	/*
	 * private void createEmployees() { int numOfEmployees = manager
	 * .createQuery("Select a From Employee a", Employee.class)
	 * .getResultList().size(); if (numOfEmployees == 0) { Department department
	 * = new Department("java"); manager.persist(department);
	 * manager.persist(new Employee("Jakab Gipsz", department));
	 * manager.persist(new Employee("Captain Nemo", department));
	 * 
	 * } }
	 * 
	 * private void listEmployees() { List<Employee> resultList =
	 * manager.createQuery( "Select a From Employee a",
	 * Employee.class).getResultList(); System.out.println("num of employess:" +
	 * resultList.size()); logger.debug("num of employess:" +
	 * resultList.size()); for (Employee next : resultList) {
	 * System.out.println("next employee: " + next); } }
	 */

}