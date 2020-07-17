package cn.whitetown.dogbase.db.factory;

import cn.whitetown.dogbase.common.memdata.WhiteExpireMap;
import cn.whitetown.dogbase.db.annotation.QueryField;
import cn.whitetown.dogbase.db.annotation.QueryTable;
import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.dogbase.db.entity.DataBaseConstant;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * database查询条件构造
 * @author GrainRain
 * @date 2020/06/20 16:39
 **/
public class DefaultQueryConditionFactory implements QueryConditionFactory{

    @Autowired
    private WhiteExpireMap whiteExpireMap;

    @Override
    public <T> LambdaQueryWrapper<T> getQueryCondition(Class<T> claz){
        return new LambdaQueryWrapper<>();
    }

    @Override
    public <T> LambdaUpdateWrapper<T> getUpdateCondition(Class<T> claz) {
        return new LambdaUpdateWrapper<>();
    }

    /**
     * 根据传入对象构建LambdaQueryWrapper
     * 传入对象可添加相应注解控制检索方式
     * 传入对象父类信息中，如果添加了@QueryTable，那么也会被加入到条件中
     * 本方法会自动忽略值为null的属性，如果是String类型，空值也会被忽略
     * @param obj
     * @param claz
     * @param <T>
     * @return
     */
    @Override
    public <T> LambdaQueryWrapper<T> allEqWithNull2IsNull(Object obj,Class<T> claz){
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();

        //get all field
        String fieldMemName = DataBaseConstant.MEM_FIELD+obj.getClass().getName();
        List<Field> fieldList = (List<Field>) whiteExpireMap.get(fieldMemName);
        if(fieldList == null) {
            fieldList = this.getObjFields(obj);

            //store fields to memory
            whiteExpireMap.putS(fieldMemName,
                    fieldList, DataBaseConstant.CLASS_SAVE_TIME);
        }
        whiteExpireMap.sExpire(fieldMemName,
                DataBaseConstant.CLASS_SAVE_TIME);

        //get all getMethod
        String methodMemName = DataBaseConstant.MEM_METHOD + obj.getClass().getName();
        Map<String,Method> methods = (Map<String,Method>) whiteExpireMap.get(methodMemName);
        if(methods == null){
            methods = this.getObjGetMethods(obj);
            //store method to memory
            whiteExpireMap.putS(methodMemName,
                    methods,DataBaseConstant.CLASS_SAVE_TIME);
        }
        whiteExpireMap.sExpire(methodMemName,
                DataBaseConstant.CLASS_SAVE_TIME);

        //create condition
        for(Field field : fieldList){
            String colName = null;
            QueryField annotation = field.getAnnotation(QueryField.class);
            if(annotation != null) {
                //忽略的信息
                if(annotation.ignore()){
                    continue;
                }
                //colName
                if (!DataCheckUtil.checkTextNullBool(annotation.value())) {
                    colName = annotation.value();
                }else {
                    colName = this.fieldToColName(field.getName());
                }
            }else {
                colName = this.fieldToColName(field.getName());
            }

            //获取数据
            try {
                Method method = methods.get("get" + field.getName().toLowerCase());
                Object param = method.invoke(obj);
                if(param == null){
                    continue;
                }
                if(param instanceof String){
                    if(DataCheckUtil.checkTextNullBool(String.valueOf(param))){
                        continue;
                    }
                }
                if(annotation != null) {
                    String operation = annotation.operation();
                    if ("eq".equalsIgnoreCase(operation)) {
                        queryWrapper.eq(colName, param);
                    } else if ("le".equalsIgnoreCase(operation)) {
                        queryWrapper.le(colName, param);
                    } else if ("lt".equalsIgnoreCase(operation)) {
                        queryWrapper.lt(colName, param);
                    } else if ("ge".equalsIgnoreCase(operation)) {
                        queryWrapper.ge(colName, param);
                    } else if ("gt".equalsIgnoreCase(operation)) {
                        queryWrapper.gt(colName, param);
                    } else if ("like".equalsIgnoreCase(operation)){
                        queryWrapper.like(colName,param);
                    }else{
                        continue;
                    }
                }else {
                    queryWrapper.eq(colName,param);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return queryWrapper.lambda();
    }

    /**
     * 获取用于分页的Page对象
     * @param page
     * @param size
     * @param claz
     * @param <T>
     * @return
     */
    @Override
    public <T> Page<T> createPage(Integer page,Integer size,Class<T> claz) {
        Page<T> mPage = new Page<>();
        mPage.setCurrent(page);
        mPage.setSize(size);
        return mPage;
    }

    /**
     * 获取Object的field字段
     * @param obj
     * @return
     */
    protected List<Field> getObjFields(Object obj){
        List<Field> fieldList = new LinkedList<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        Arrays.stream(fields).forEach(field -> fieldList.add(field));
        Class<?> superclass = obj.getClass().getSuperclass();
        while (superclass != null) {
            QueryTable annotation = superclass.getAnnotation(QueryTable.class);
            if (annotation == null) {
                break;
            }
            Field[] fields1 = superclass.getDeclaredFields();
            Arrays.stream(fields1).forEach(field -> fieldList.add(field));
            superclass = superclass.getSuperclass();
        }
        return fieldList;
    }

    /**
     * 获取obj的所有get方法
     * @param obj
     * @return
     */
    protected Map<String,Method> getObjGetMethods(Object obj){
        Map<String,Method> methods = new HashMap<>(16);
        Method[] ms = obj.getClass().getMethods();
        Arrays.stream(ms).filter(method -> method.getName().startsWith("get"))
                .forEach(method -> methods.put(method.getName().toLowerCase(),method));
        return methods;
    }

    /**
     * 将属性名称转换为列名称,符合驼峰命名法可用
     * @param fieldName
     * @return
     */
    protected String fieldToColName(String fieldName){
        StringBuilder builder = new StringBuilder();
        char[] fieldChars = fieldName.toCharArray();
        for (int i = 0; i < fieldChars.length; i++) {
            if(fieldChars[i] >= 'A' && fieldChars[i] <= 'Z'){
                builder.append("_").append(String.valueOf(fieldChars[i]).toLowerCase());
            }else {
                builder.append(fieldChars[i]);
            }
        }
        return builder.toString();
    }
}
