package com.github.ScipioAM.scipio_utils_javafx.persistence;

import com.github.ScipioAM.scipio_utils_common.time.DateTimeUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

/**
 * Entity Database Access Object
 *
 * @author Alan Scipio
 * @since 1.0.10 _ 2022/2/21
 */
public class EntityDao {

    /** 是否约束传入entity的类型必须为{@link BaseEntity}，true代表是 */
    private boolean checkBaseEntity;

    private final EntityManagerFactory entityManagerFactory;
    private final EntityManager entityManager;

    private EntityDao(boolean checkBaseEntity) {
        this.checkBaseEntity = checkBaseEntity;
        entityManagerFactory = Persistence.createEntityManagerFactory("jpa");
        entityManager = entityManagerFactory.createEntityManager();
    }

    private EntityDao() {
        this(true);
    }

    /**
     * 数据库连接初始化
     */
    public static EntityDao init() {
        return new EntityDao();
    }

    /**
     * 关闭连接
     */
    public void destroy() {
        entityManager.close();
        entityManagerFactory.close();
    }

    /**
     * 开启一个事务
     */
    public EntityTransaction beginTransaction() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        return transaction;
    }

    public EntityManager getManager() {
        return entityManager;
    }

    //==================================== 增删改 ====================================

    /**
     * 新增数据
     *
     * @param entity 要新增的数据
     */
    public void add(Object entity) {
        checkEntityType(entity.getClass());
        EntityTransaction transaction = beginTransaction();
        try {
            entityManager.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    /**
     * 修改数据(非动态，所以字段都会被更新，哪怕字段是null)
     *
     * @param entity 要修改的数据
     */
    public void update(Object entity) {
        checkEntityType(entity.getClass());
        EntityTransaction transaction = beginTransaction();
        try {
            BaseEntity daoEntity = (BaseEntity) entity;
            daoEntity.setUpdateTime(DateTimeUtil.getNowStr());
            entityManager.merge(daoEntity);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    /**
     * 删除数据
     *
     * @param entity 要删除的数据
     */
    public void delete(Object entity) {
        checkEntityType(entity.getClass());
        EntityTransaction transaction = beginTransaction();
        try {
            entityManager.remove(entity);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param clazz      要删除数据的类型
     * @param primaryKey 要删除的数据的主键
     */
    public void delete(Class<?> clazz, Object primaryKey) {
        checkEntityType(clazz);
        BaseEntity entity = findById(clazz, primaryKey);
        delete(entity);
    }

    //==================================== select ====================================

    /**
     * 根据主键查询
     *
     * @param clazz      查询的实体类
     * @param primaryKey 主键
     */
    @SuppressWarnings("unchecked")
    public <T> T findById(Class<?> clazz, Object primaryKey) {
        checkEntityType(clazz);
        return (T) entityManager.find(clazz, primaryKey);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> findAll(Class<?> clazz) {
        checkEntityType(clazz);
        return (List<T>) entityManager.createQuery("from " + clazz.getName()).getResultList();
    }

    //==================================== 私有方法 ====================================

    /**
     * 检查entity的类型
     * @param clazz entity的类型，必须是{@link BaseEntity}或其子类
     */
    private void checkEntityType(Class<?> clazz) throws IllegalArgumentException {
        if (checkBaseEntity) {
            assert clazz != null;
            if (clazz != BaseEntity.class) {
                throw new IllegalArgumentException("Invalid entity type! expect [" + BaseEntity.class.getName() + "], actual [" + clazz.getName() + "]");
            }
        }
    }

    //==================================== getter/setter ====================================

    public boolean isCheckBaseEntity() {
        return checkBaseEntity;
    }

    public void setCheckBaseEntity(boolean checkBaseEntity) {
        this.checkBaseEntity = checkBaseEntity;
    }

}
