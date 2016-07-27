package daoImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.URL;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;

import org.apache.commons.io.FileUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.rolling.RollingFileAppender;
import dao.GenericDAO;
import exceptions.EntityNotPresent;

public abstract class GenericDAOImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {
	@PersistenceContext
	protected EntityManager entityManager;
	protected EntityTransaction entityTransaction;
	static SessionFactory factory;
	Session session;
	static Configuration cfg = null;

	LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
	@SuppressWarnings("rawtypes")
	RollingFileAppender rfAppender = new RollingFileAppender();
	Logger logbackLogger;

	public GenericDAOImpl() {
		super();
		rfAppender.setContext(loggerContext);
		rfAppender.setFile(System.getProperty("log_file_loc") + "/log/" + "rapid" + ".log");
		logbackLogger = loggerContext.getLogger("MainController");
		logbackLogger.addAppender(rfAppender);
		try {
			if (cfg == null) {
				URL url;
				try {
					InetAddress currentIp = InetAddress.getLocalHost();
					// String ip = currentIp.getHostAddress();
					String ip = System.getProperty("database_url");
					ip = ip.trim();
					url = new URL("platform:/plugin/FirstApp/src/META-INF/persistence.xml");
					InputStream inputStream = url.openConnection().getInputStream();
					BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
					String inputLine;
					logbackLogger.error("Hello from GenericDAOImpl");
					File file = new File("persistenceTemp.xml");
					FileUtils.copyURLToFile(url, file);
					logbackLogger.error("Reading persistence xml from path " + file.getAbsolutePath());
					while ((inputLine = in.readLine()) != null) {
						logbackLogger.info(inputLine);
					}
					cfg = new Configuration();
					cfg.configure(file);
					cfg.setProperty("hibernate.connection.url", "jdbc:derby://" + ip + ":1527/falcon1;create=true");
					cfg.setProperty("hibernate.jdbc.batch_size property", "20");
					factory = cfg.buildSessionFactory();
					if (file.delete()) {
						System.out.println(file.getName() + " is deleted!");
						logbackLogger.debug(file.getName() + " is deleted!");
					} else {
						System.out.println("Delete operation is failed.");
						logbackLogger.error(file.getName() + "Delete operation is failed.");
					}
				} catch (IOException e) {
					logbackLogger.error(e.getMessage(), e);
					e.printStackTrace();
				}
			}
		} catch (Exception err) {
			logbackLogger.error(err.getMessage(), err);
		}
		// session = factory.openSession();

	}

	@Override
	public T save(T t) {
		Session session = null;
		try {
			session = factory.openSession();
			session.beginTransaction().begin();
			session.persist(t);
			session.flush();
			session.beginTransaction().commit();
			// session.close();

		} catch (Exception err) {
			logbackLogger.error(err.getMessage(), err);
		}
		session.beginTransaction().commit();
		session.close();

		return t;
	}

	@Override
	public void batchSaveDAO(List<T> t) {
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		int i = 0;
		for (T tSingle : t) {
			i++;
			try {
				session.save(tSingle);
			} catch (Exception err) {
				logbackLogger.error(err.getMessage(), err);
			}
			if (i % 20 == 0) { // 20, same as the JDBC batch size
				// flush a batch of inserts and release memory
				session.flush();
				session.clear();
			}
		}
		tx.commit();
		session.close();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<T> readAll(String namedQueryName, Class clazz) {
		Session session = factory.openSession();
		List<T> listT = null;
		try {
			session = factory.openSession();
			session.beginTransaction().begin();
			Query query = session.createQuery("from " + clazz.getName());
			listT = query.list();
			session.beginTransaction().commit();
			// session.close();
		} catch (Exception err) {
			logbackLogger.error(err.getMessage(), err);
		} finally {
			// session.beginTransaction().commit();
			// session.close();
		}
		session.close();
		return listT;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T readById(@SuppressWarnings("rawtypes") Class clazz, ID id) {
		T t = null;
		try {
			Session session = factory.openSession();
			session.beginTransaction().begin();
			t = (T) session.get(clazz, id);
			session.beginTransaction().commit();
			session.close();
		} catch (Exception err) {
			logbackLogger.error(err.getMessage(), err);
		} finally {
			// session.beginTransaction().commit();
			// session.close();
		}
		return t;
	}

	@Override
	public void delete(@SuppressWarnings("rawtypes") Class clazz, ID removeId) {
		if (isEntityExists(clazz, removeId)) {
			try {
				T old = readById(clazz, removeId);
				entityTransaction.begin();
				entityManager.remove(old);
				entityTransaction.commit();
			} catch (Exception err) {
				logbackLogger.error(err.getMessage(), err);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isEntityExists(@SuppressWarnings("rawtypes") Class clazz, ID id) {
		return entityManager.find(clazz, id) != null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getFirstRecord(Class<?> clazz) {
		T t = null;
		try {
			session = factory.openSession();
			session.beginTransaction().begin();
			Criteria queryCriteria = session.createCriteria(clazz);
			queryCriteria.setFirstResult(0);
			queryCriteria.setMaxResults(1);
			t = (T) queryCriteria.list().get(0);
			session.close();
		} catch (Exception err) {
			logbackLogger.error(err.getMessage(), err);
		}
		return t;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getByQuery(String queryExecute, Object[] pars, @SuppressWarnings("rawtypes") Class clazz) {
		List<T> results = null;
		Session session = null;
		try {
			session = factory.openSession();
			Query query = session.createQuery(queryExecute);
			for (int i = 0; i < pars.length; i++) {
				query.setParameter("arg" + i, pars[i]);
			}
			results = query.list();
		} catch (Exception err) {
			logbackLogger.error(err.getMessage(), err);
		}
		session.close();
		return results;

	}

	@SuppressWarnings("unchecked")
	@Override
	public T update(@SuppressWarnings("rawtypes") Class clazz, ID id, T t) throws EntityNotPresent {
		Session session = null;
		try {
			session = factory.openSession();
			T newEntityRef =    (T) session.merge(t);
			session.beginTransaction().begin();
			session.update(newEntityRef);
			session.beginTransaction().commit();
		} catch (Exception err) {
			logbackLogger.error(err.getMessage(), err);
		}
		session.close();
		return t;
	}

	public T saveOrUpdate(@SuppressWarnings("rawtypes") Class clazz, T t) {
		Session session = null;
		try {
			session = factory.openSession();
			session.beginTransaction().begin();
			session.saveOrUpdate(t);
			session.beginTransaction().commit();
		} catch (Exception err) {
			logbackLogger.error(err.getMessage(), err);
		}
		session.close();
		return t;
	}

	@SuppressWarnings("unchecked")
	public void saveOrUpdateBatch(List<T> t) {

		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		int i = 0;
		for (T tSingle : t) {
			T newEntityRef =    (T) session.merge(tSingle);
			i++;
			try {
				session.saveOrUpdate(newEntityRef);
			} catch (Exception err) {
				logbackLogger.error(err.getMessage(), err);
			}
			if (i % 20 == 0) { // 20, same as the JDBC batch size
				// flush a batch of inserts and release memory
				session.flush();
			}
		}
		tx.commit();
		session.close();

	}

}