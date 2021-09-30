package com.github.ScipioAM.scipio_utils_common.data.mount;

import jakarta.validation.constraints.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Alan Scipio
 * @since 1.0.2
 * @date 2021/9/30
 */
public class MountedCacheWrapper implements MountedCache{

    /** 默认数据写入策略 */
    private final DataAssignmentPolicy DEFAULT_POLICY = DataAssignmentPolicy.USE_SET_METHOD;

    /** 被装饰的实例 */
    private MountedCache mountedCache;

    private Map<Integer,Field> cacheFields;

    private DataAssignmentPolicy assignmentPolicy;

    public MountedCacheWrapper(@NotNull MountedCache mountedCache, @NotNull DataAssignmentPolicy assignmentPolicy) {
        init(mountedCache,assignmentPolicy);
    }

    public MountedCacheWrapper(@NotNull MountedCache mountedCache) {
        init(mountedCache,DEFAULT_POLICY);
    }

    public void reset(@NotNull MountedCache mountedCache, @NotNull DataAssignmentPolicy assignmentPolicy) {
        init(mountedCache,assignmentPolicy);
    }

    public void reset(@NotNull MountedCache mountedCache) {
        init(mountedCache,DEFAULT_POLICY);
    }

    private void init(MountedCache mountedCache, DataAssignmentPolicy assignmentPolicy) {
        if(mountedCache == null) {
            throw new IllegalArgumentException("argument \"mountedCache\" can not be null");
        }
        //获取目标类所有类字段
        Class<?> cacheClass = mountedCache.getClass();
        Field[] allFields = cacheClass.getDeclaredFields();
        if(allFields.length <= 0) {
            throw new IllegalStateException("there has no fields found in [" + cacheClass + "]");
        }
        cacheFields = new HashMap<>();
        //记录所有被打上@CacheField注解的字段
        for(Field field : allFields) {
            if(field.isAnnotationPresent(CacheField.class)) {
                CacheField annotation = field.getDeclaredAnnotation(CacheField.class);
                int id = annotation.id();
                if(cacheFields.containsKey(id)) {
                    throw new IllegalStateException("Multi @CacheField must declare different id");
                }
                cacheFields.put(id, field);
            }
        }
        if(cacheFields.size() <= 0) {
            throw new IllegalStateException("there has no annotated fields(@CacheField) found in [" + cacheClass + "]");
        }
        this.mountedCache = mountedCache;
        this.assignmentPolicy = assignmentPolicy;
    }

    //==================================================================================================================

    @SuppressWarnings({"rawtypes","unchecked"})
    @Override
    public void putData(int id, Object key, Object data) throws Exception {
        checkId(id);
        Field field = cacheFields.get(id);
        Class<?> fieldType = field.getType();
        String fieldName = field.getName();
        if(fieldType == List.class) {
            field.setAccessible(true);
            Object fieldInstance = field.get(mountedCache);
            List list = (List) fieldInstance;
            list.add(data);
        }
        else if(fieldType == Set.class) {
            field.setAccessible(true);
            Object fieldInstance = field.get(mountedCache);
            Set set = (Set) fieldInstance;
            set.add(data);
        }
        else if(fieldType == Map.class) {
            field.setAccessible(true);
            Object fieldInstance = field.get(mountedCache);
            Map map = (Map) fieldInstance;
            map.put(key,data);
        }
        else if (fieldType == Vector.class) {
            field.setAccessible(true);
            Object fieldInstance = field.get(mountedCache);
            Vector vector = (Vector) fieldInstance;
            vector.add(data);
        }
        else {
            //TODO 上面的写法等于直接赋值，考虑if结构的大改：先判写入策略，再赋值，但对于集合类型怎么调set方法？无脑调？
            if(assignmentPolicy == DataAssignmentPolicy.DIRECT_ASSIGN) {
                field.setAccessible(true);
                field.set(mountedCache,data);
            }
            else {
                String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method setMethod = mountedCache.getClass().getDeclaredMethod(setMethodName);
                setMethod.invoke(data);
            }
        }
        mountedCache.putData(id, key, data);
    }//end of putData()

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getData(int id, Object key) throws Exception {
        checkId(id);
        Field field = cacheFields.get(id);
        Class<?> fieldType = field.getType();
        String fieldName = field.getName();
        T data = null;
        if(fieldType == List.class) {
            if(!(key instanceof Integer)) {
                throw new IllegalArgumentException("key type must be [java.lang.Integer] when MountedField`s type is [java.util.List]");
            }
            field.setAccessible(true);
            Object fieldInstance = field.get(mountedCache);
            List<T> list = (List<T>) fieldInstance;
            data = list.get((Integer) key);
        }
        //TODO 待完成
//        else if(fieldType == Set.class) {
//            if(!(key instanceof Integer)) {
//                throw new IllegalArgumentException("key type must be [java.lang.Integer] when MountedField`s type is [java.util.List]");
//            }
//            Object fieldInstance = field.get(mountedCache);
//            Set<T> set = (Set<T>) fieldInstance;
//
//        }
//        else if(fieldType == Map.class) {
//            Object fieldInstance = field.get(mountedCache);
//            Map map = (Map) fieldInstance;
////            map.put(key,data);
//        }
//        else if (fieldType == Vector.class) {
//            Object fieldInstance = field.get(mountedCache);
//            Vector vector = (Vector) fieldInstance;
////            vector.add(data);
//        }
//        else {
//            if(assignmentPolicy == DataAssignmentPolicy.DIRECT_ASSIGN) {
//                field.setAccessible(true);
////                field.set(mountedCache,data);
//            }
//            else {
//                String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
//                Method setMethod = mountedCache.getClass().getDeclaredMethod(setMethodName);
////                setMethod.invoke(data);
//            }
//        }
//        mountedCache.putData(id, key, data);
        return data;
    }

    //==================================================================================================================

    private void checkId(int id) {
        if(!cacheFields.containsKey(id)) {
            throw new IllegalArgumentException("unknown id: " + id);
        }
    }

    private void setDataIntoField(Object key, Object data) {

    }

}
