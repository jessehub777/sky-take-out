### 使用MD5加密

```java
password =DigestUtils.

md5DigestAsHex(password.getBytes());
```

### 设置和获取当前用户id

```java
threadLocal.set(id);

id =threadLocal.

get();
```

### 使用对象属性拷贝，来简化2个不同实体的转换

```java
// 1.使用BeanUtils.copyProperties
BeanUtils.copyProperties(source, target);
BeanUtils.

copyProperties(employeeDTO, employee);

// 2.使用.build()
Category category = Category.builder()
        .id(id)
        .status(status)
        .build();
```

### 分页查询  PageHelper.startPage

```java
public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
    // select * from employee limit 0,10
    // 通过PageHelper.startPage()来实现分页查询--需要在pom.xml中添加pagehelper-spring-boot-starter的依赖
    PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
    
    // 这里的PageHelper.startPage()方法会在查询之前调用，设置当前页和每页显示的条数
    // 这里的PageHelper会自动拦截查询语句，进行分页处理 通过ThreadLocal来实现,自动关联上下2条语句107,115
    
    
    // 这里传DTO的目的是参数中还有一个name属性
    // 左边的Page是pagehelper的Page类，里面封装了分页查询的结果
    Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
    
    long total = page.getTotal();  // 通过page.getTotal()获取总记录数
    List<Employee> records = page.getResult(); // 通过page.getResult()获取当前页的记录
    records.forEach(record -> {
        record.setPassword("");
    });
    return new PageResult(total, records);
}
```

```xml

<select id="pageQuery" resultType="com.sky.entity.Employee">
    select * from employee
    <where>
        <if test="name != null and name != ''">
            and name like concat('%', #{name}, '%')
        </if>
    </where>
    order by create_time desc
</select>
```

以上xml会返回Page<Employee>的集合，里面包含了`List<Employee>`和`总记录数`

```java
    Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);

long total = page.getTotal();
List<Employee> records = page.getResult(); 
```

另外

```
 * 分页查询
 * 此处不使用注解方式，因为不方便（动态sql）动态标签
 * 应该写到映射文件中，resources/mapper/EmployeeMapper.xml中
 * 是否能扫描到xml？看resources/application.yml中mybatis.mapper-locations配置
```

### @Autowired 和 @RequiredArgsConstructor

```java
// 1.直接在属性名上 使用@Autowired注入
@Autowired
private EmployeeMapper employeeMapper;

```

```java
// (！！更推荐！！)
// 2.在类名上 使用构造器注入 属性要设置为final
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeMapper employeeMapper;
```

# 1.@Transactional  2.useGeneratedKeys  3.批量插入
在xml上使用  `useGeneratedKeys="true" keyProperty="id"`

告诉 MyBatis 使用数据库自动生成的主键 填充到对应的 Java 对象中。
```xml
<insert id="insert" parameterType="Dish" useGeneratedKeys="true" keyProperty="id">
    insert into dish ()
    values ()
</insert>
```
对应的 Java 对象：
```java
public class Dish {
    private Long id; // 主键
}
```

在ServiceImpl中使用
```java
@Transactional
public void saveWithFlavor(DishDTO dishDTO) {
    
    // 通过DishMapper.xml中 useGeneratedKeys="true" keyProperty="id"
    Long dishId = dish.getId();
    
    
    // 新增口味 多个 批量插入
    List<DishFlavor> dishFlavors = dishDTO.getFlavors();
    if (dishFlavors != null && !dishFlavors.isEmpty()) {
        for (DishFlavor dishFlavor : dishFlavors) {
            dishFlavor.setDishId(dishId);
        }
        dishFlavorMapper.insertBatch(dishFlavors);
    }
}
```
### 批量插入
```xml
<insert id="insertBatch">
    insert into dish_flavor (dish_id, name, value) values
    <foreach collection="dishFlavors" item="item" separator=",">
        (#{item.dishId}, #{item.name}, #{item.value})
    </foreach>
</insert>
在<insert> 中的 values 使用<foreach collection="dishFlavors" item="item" separator=",">
其中collection对应传进来的参数名；  separator=","是 SQL 拼接语义（后续还会用到更深层次的用法）

```