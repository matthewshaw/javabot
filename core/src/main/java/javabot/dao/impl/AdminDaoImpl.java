package javabot.dao.impl;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

import javabot.dao.AbstractDaoImpl;
import javabot.dao.AdminDao;
import javabot.model.Admin;
import org.springframework.stereotype.Component;

@Component
public class AdminDaoImpl extends AbstractDaoImpl<Admin> implements AdminDao {
    public AdminDaoImpl() {
        super(Admin.class);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<Admin> findAll() {
        return getEntityManager().createNamedQuery(AdminDao.FIND_ALL)
            .getResultList();
    }

    @Override
    public boolean isAdmin(final String user, final String hostName) {
        return findAll().isEmpty() || getAdmin(user, hostName) != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Admin getAdmin(final String ircName, final String hostName) {
        final List<Admin> list = getEntityManager().createNamedQuery(AdminDao.FIND_WITH_HOST)
            .setParameter("ircName", ircName)
            .setParameter("hostName", hostName)
            .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    @Override
    @SuppressWarnings("unchecked")
    public Admin getAdmin(final String userName) {
        final List<Admin> list = getEntityManager().createNamedQuery(AdminDao.FIND)
            .setParameter("userName", userName)
            .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public void setEntityManager(final EntityManager manager) {
        super.setEntityManager(manager);
    }

    @Override
    public void create(final String newAdmin, final String newHostName) {
        final Admin admin = new Admin();
        admin.setUserName(newAdmin);
        admin.setUpdated(new Date());

        save(admin);
    }
}