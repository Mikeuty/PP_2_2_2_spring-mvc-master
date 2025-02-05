package web.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    private EntityManagerFactory entityManagerFactory;

    @Autowired
    public UserDaoImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void createUsersTable() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createNativeQuery(
                    "CREATE TABLE IF NOT EXISTS users (\n" +
                            "  id BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
                            "  name VARCHAR(255) NOT NULL,\n" +
                            "  last_name VARCHAR(255) NOT NULL,\n" +
                            "  age TINYINT UNSIGNED NOT NULL\n" +
                            ");"
            );
            query.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Ошибка при создании таблицы!", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void dropUsersTable() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createNativeQuery("DROP TABLE IF EXISTS users;");
            query.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Ошибка при удалении таблицы", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void saveUser(User user) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            if (user.getId() == null) {
                em.persist(user);
            } else {
                em.merge(user);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Ошибка при сохранении юзера", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void removeUserById(long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, id);
            if (user != null) {
                em.remove(user);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Ошибка при удалении юзера", e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            List<User> users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
            em.getTransaction().commit();
            return users;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Ошибка при предоставлении всех юзеров", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void cleanUsersTable() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createNativeQuery("TRUNCATE TABLE users;");
            query.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Ошибка при очистке таблицы", e);
        } finally {
            em.close();
        }
    }

    @Override
    public User getUserById(long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            User user = em.find(User.class, id);
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении юзера по ID: " + id, e);
        } finally {
            em.close();
        }
    }
}
