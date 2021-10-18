package com.github.ScipioAM.scipio_utils_common.data.mount;

import com.github.ScipioAM.scipio_utils_common.AssertUtil;
import com.github.ScipioAM.scipio_utils_common.reflect.FieldUtil;
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

    /** 默认数据赋值\取值策略 */
    private final DataAssignmentPolicy DEFAULT_ASSIGN_POLICY = DataAssignmentPolicy.USE_METHOD;

    /** 被装饰的实例 */
    private MountedCache mountedCache;

    private Map<Integer,Field> cacheFields;

    private DataAssignmentPolicy assignmentPolicy;

    public MountedCacheWrapper(@NotNull MountedCache mountedCache, @NotNull DataAssignmentPolicy assignmentPolicy) {
        init(mountedCache,assignmentPolicy);
    }

    public MountedCacheWrapper(@NotNull MountedCache mountedCache) {
        init(mountedCache,DEFAULT_ASSIGN_POLICY);
    }

    public void reset(@NotNull MountedCache mountedCache, @NotNull DataAssignmentPolicy assignmentPolicy) {
        init(mountedCache,assignmentPolicy);
    }

    public void reset(@NotNull MountedCache mountedCache) {
        init(mountedCache,DEFAULT_ASSIGN_POLICY);
    }

    /**
     * 初始化
     * @param mountedCache 被装饰的实例对象
     * @param assignmentPolicy 数据赋值\取值策略(调用set\get方法还是直接赋值\取值)
     */
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
            if(assignmentPolicy == DataAssignmentPolicy.USE_METHOD) {
                Class<?> parameterizedType = FieldUtil.getParameterizedType(field);//list的泛型类型
                Method setMethod = findMethod("set",fieldName,parameterizedType);
                setMethod.invoke(mountedCache, data);
            }
            else {
                field.setAccessible(true);
                Object fieldInstance = field.get(mountedCache);
                List list = (List) fieldInstance;
                list.add(data);
            }
        }
        else if(fieldType == Set.class) {
            if(assignmentPolicy == DataAssignmentPolicy.USE_METHOD) {
                Class<?> parameterizedType = FieldUtil.getParameterizedType(field);//set的泛型类型
                Method setMethod = findMethod("set",fieldName,parameterizedType);
                setMethod.invoke(mountedCache, data);
            }
            else {
                field.setAccessible(true);
                Object fieldInstance = field.get(mountedCache);
                Set set = (Set) fieldInstance;
                set.add(data);
            }
        }
        else if(fieldType == Map.class) {
            if(assignmentPolicy == DataAssignmentPolicy.USE_METHOD) {
                Class<?> parameterizedType_key = FieldUtil.getParameterizedType(field);//map的key的泛型类型
                Class<?> parameterizedType_value = FieldUtil.getParameterizedType(field);//map的key的泛型类型
                Method setMethod = findMethod("set",fieldName,parameterizedType_key, parameterizedType_value);
                setMethod.invoke(mountedCache, key, data);
            }
            else {
                field.setAccessible(true);
                Object fieldInstance = field.get(mountedCache);
                Map map = (Map) fieldInstance;
                map.put(key,data);
            }
        }
        else if (fieldType == Vector.class) {
            if(assignmentPolicy == DataAssignmentPolicy.USE_METHOD) {
                Class<?> parameterizedType = FieldUtil.getParameterizedType(field);//vector的泛型类型
                Method setMethod = findMethod("set",fieldName,parameterizedType);
                setMethod.invoke(mountedCache, data);
            }
            else {
                field.setAccessible(true);
                Object fieldInstance = field.get(mountedCache);
                Vector vector = (Vector) fieldInstance;
                vector.add(data);
            }
        }
        else { //非集合类型
            if(assignmentPolicy == DataAssignmentPolicy.DIRECT) {
                field.setAccessible(true);
                field.set(mountedCache,data);//直接赋值
            }
            else {
                Method setMethod = findMethod("set",fieldName, (Class<?>[]) null);
                setMethod.invoke(data);//调用set方法
            }
        }
        //被装饰者自己的实现
        mountedCache.putData(id, key, data);
    }//end of putData()

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getData(int id, Object key) throws Exception {
        //被装饰者自己实现的getData，拥有最高优先级
        T data = mountedCache.getData(id, key);
        if(data != null) {
            return data;
        }
        //未实现(或返回为null)，则执行默认操作
        checkId(id);
        Field field = cacheFields.get(id);
        Class<?> fieldType = field.getType();
        String fieldName = field.getName();
        if(fieldType == List.class) {
            AssertUtil.checkType(key, Integer.class);
            if(assignmentPolicy == DataAssignmentPolicy.USE_METHOD) {
                Method getMethod = findMethod("get",fieldName);
                List<T> list = (List<T>) getMethod.invoke(mountedCache,key);
                data = list.get((Integer) key);
            }
            else {
                field.setAccessible(true);
                Object fieldInstance = field.get(mountedCache);
                List<T> list = (List<T>) fieldInstance;
                data = list.get((Integer) key);
            }
        }
        else if(fieldType == Set.class) {
            AssertUtil.checkType(key, Integer.class);
            if(assignmentPolicy == DataAssignmentPolicy.USE_METHOD) {
                Method getMethod = findMethod("get",fieldName);
                Set<T> set = (Set<T>) getMethod.invoke(mountedCache,key);
                List<T> list = new ArrayList<>(set);
                data = list.get((Integer) key);
            }
            else {
                field.setAccessible(true);
                Object fieldInstance = field.get(mountedCache);
                Set<T> set = (Set<T>) fieldInstance;
                List<T> list = new ArrayList<>(set);
                data = list.get((Integer) key);
            }
        }
        else if(fieldType == Map.class) {
            if(assignmentPolicy == DataAssignmentPolicy.USE_METHOD) {
                Class<?> parameterizedType_key = FieldUtil.getParameterizedType(field);//map的key的泛型类型
                AssertUtil.checkType(key, parameterizedType_key);//检查map的key类型与传入的是否匹配
                Method getMethod = findMethod("get",fieldName);
                Map<?,T> map = (Map<?,T>) getMethod.invoke(mountedCache,key);
                data = map.get(key);
            }
            else {
                field.setAccessible(true);
                Object fieldInstance = field.get(mountedCache);
                Map<?,T> map = (Map<?,T>) fieldInstance;
                data = map.get(key);
            }
        }
        else if (fieldType == Vector.class) {
            AssertUtil.checkType(key, Integer.class);
            if(assignmentPolicy == DataAssignmentPolicy.USE_METHOD) {
                Method getMethod = findMethod("get",fieldName);
                Vector<T> vector = (Vector<T>) getMethod.invoke(mountedCache,key);
                data = vector.get((Integer) key);
            }
            else {
                field.setAccessible(true);
                Object fieldInstance = field.get(mountedCache);
                Vector<T> vector = (Vector<T>) fieldInstance;
                data = vector.get((Integer) key);
            }
        }
        else {
            if(assignmentPolicy == DataAssignmentPolicy.DIRECT) {
                field.setAccessible(true);
                data = (T) field.get(mountedCache);
            }
            else {
                Method getMethod = findMethod("get",fieldName, (Class<?>[]) null);
                data = (T) getMethod.invoke(mountedCache);
            }
        }
        return data;
    }

    //==================================================================================================================

    public DataAssignmentPolicy getDataAssignmentPolicy() {
        return assignmentPolicy;
    }

    public void setDataAssignmentPolicy(DataAssignmentPolicy assignmentPolicy) {
        this.assignmentPolicy = assignmentPolicy;
    }

    //==================================================================================================================

    /**
     * 检查指定id的{@link MountedCache}字段是否存在
     * @param id 指定的id，默认值：{@link MountedCache#DEFAULT_ID}
     */
    private void checkId(int id) {
        if(!cacheFields.containsKey(id)) {
            throw new IllegalArgumentException("unknown id: " + id);
        }
    }

    /**
     * 查找set方法
     * @param prefix 方法前缀(set还是get)
     * @param fieldName 字段名称（根据字段名称拼出set方法的名称）
     * @param parameterizedTypes 方法参数的类型
     * @return 找到的set方法对象
     * @throws NoSuchMethodException 没有该方法
     */
    private Method findMethod(String prefix, String fieldName, Class<?>... parameterizedTypes) throws NoSuchMethodException {
        String methodName = prefix + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Method setMethod;
        try {
            if(parameterizedTypes == null) {
                setMethod = mountedCache.getClass().getDeclaredMethod(methodName);
            }
            else {
                setMethod = mountedCache.getClass().getDeclaredMethod(methodName, parameterizedTypes);
            }
        } catch (NoSuchMethodException e) {
            System.err.print("No such method found, methodName[" + methodName + "]");
            if(parameterizedTypes != null) {
                for(int i = 0; i < parameterizedTypes.length; i++) {
                    System.err.print(", parameterType" + i + "[" + parameterizedTypes[i] + "]");
                }
                System.err.println();
            }
            throw e;
        }
        return setMethod;
    }

}
