package com.atguigu.admin;

import com.atguigu.admin.utils.BaiJiaXing;
import com.atguigu.admin.utils.FirstName;
import com.atguigu.admin.utils.Gender;
import com.atguigu.admin.view.MainView;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@SpringBootTest
class Boot05WebAdminApplicationTests {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;
    @Test//用@org.junit.Test会报空指针异常，可能跟JUnit新版本有关
    void contextLoads() {
//        jdbcTemplate.queryForObject("select * from account_tbl")
//        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from address_book");

        Long aLong = jdbcTemplate.queryForObject("select count(*) from student", Long.class);
        log.info("记录总数：{}",aLong);
        log.info("数据源类型",dataSource.getClass());
    }
    @Test
    void insert(){
//        String sql = "INSERT INTO student(s_id, s_name, s_sex, s_age, s_class) VALUES(?, ?, ?, ?, ?)";
//        jdbcTemplate.update(sql, "20210001", "张三", "男", 20, "计科2001");
//        log.info("name"+BaiJiaXing.values()[1].toString());
        jdbcTemplate.update("INSERT INTO student(s_id, s_name,s_sex,s_age,s_class) values (?, ?, ?, ?, ?)",
                "" + 2020 + 1
                ,BaiJiaXing.values()[1].toString() + FirstName.values()[1].toString()
                ,Gender.values()[1].toString()
                ,20+1
                ,2);
    }
    @Test
    void insertCourse(){
        int MIN_HOURS = 8; // 最小学时
        int MAX_HOURS = 192; // 最大学时
        int HOURS_STEP = 8; // 学时步长
        int HOURS_PER_CREDIT = 16; // 每个学分需要的学时数

        String[] SUBJECTS = { "计算机科学", "电子工程", "机械工程", "化学", "物理", "数学" };
        String[] COURSE_NAMES = { "数据结构", "数字电路", "热力学", "有机化学", "电磁学", "高等数学" };

        Random random = new Random();
            int count = 1000;
            for (int i = 0; i < count; i++) {
                String subject = SUBJECTS[random.nextInt(SUBJECTS.length)];
                String courseId = subject.substring(0, 2) + String.format("%05d",(i + 1)); // 课程号 = 学科缩写 + 序号
                String courseName = subject + COURSE_NAMES[i % COURSE_NAMES.length]; // 课程名称 = 学科名称 + 课程名
                int hours = random.nextInt(MAX_HOURS / HOURS_STEP) * HOURS_STEP + MIN_HOURS; // 随机生成学时
                float credit = hours / (float) HOURS_PER_CREDIT; // 计算学分
                String teacherId = String.format("%04d", random.nextInt(1000)); // 生成三位随机教师编号
                jdbcTemplate.update("INSERT INTO course(c_id, c_name, credit, c_hours, t_id) VALUES (?, ?, ?, ?, ?)",
                        courseId, courseName, credit, hours, teacherId);
                log.info(courseId);
            }
    }
    @Test
    void insertStudent(){
        Random random = new Random();
        int YEAR_FROM = 2020;
        int YEAR_TO = 2023;
        int CLASS_AMOUNT = 30;
        int YEAR_TOTAL = 2500;
        for (int year = YEAR_FROM; year <= YEAR_TO; year++) {
            //班级号
            for (int class_number = 1; class_number < YEAR_TOTAL/CLASS_AMOUNT + 1; class_number++) {
                for (int classId = 1; classId < CLASS_AMOUNT; classId++) {
                    jdbcTemplate.update("INSERT INTO student(s_id, s_name,s_sex,s_age,s_class) values (?, ?, ?, ?, ?)",
                            "" + year + String.format("%04d", class_number) + String.format("%02d", classId)
                            ,BaiJiaXing.values()[random.nextInt(BaiJiaXing.values().length)].toString() + FirstName.values()[random.nextInt(FirstName.values().length)].toString()
                            ,Gender.values()[random.nextInt(Gender.values().length)].toString()
                            ,20+random.nextInt(15)
                            ,classId);
//                    log.info("" + year + class_number + classId);
                }
            }
        }
    }
    @Test
    void insertSC(){
        int SC_TOTAL = 50*10000;
        int insertCount = 0;
        int YEAR_BASE = 16;
        String sql = "SELECT DISTINCT c_id FROM course ORDER BY RAND() LIMIT ?";
        List<String> sIds = jdbcTemplate.queryForList("SELECT s_id FROM student", String.class);
        Random random = new Random();
        float min = 1.0f; // 随机数的最小值
        float max = 100.0f; // 随机数的最大值
        float randomScore;
        for (String sId :
                sIds) {
            int year = Integer.parseInt("" + sId.charAt(3)) + 1;
            int yearAmount = year * YEAR_BASE;
            List<String> cIds = jdbcTemplate.queryForList(sql, new Object[]{yearAmount}, String.class);
            for (String cId :
                    cIds) {
                randomScore = random.nextFloat() * (max - min) + min; // 生成随机分数
                jdbcTemplate.update("INSERT INTO sc(s_id, c_id, score) VALUES (?, ?, ?)", sId, cId, randomScore);
            }
            insertCount += cIds.size();
            if (insertCount > SC_TOTAL){
                break;
            }
        }
    }
    @Test
    void delete(){
        String tableName = "sc";
        String sql = "DELETE FROM " + tableName;
        jdbcTemplate.update(sql);
    }
    @Test
    void loadColumn(){

    }
    @Test
    void getColumnCount() {
        String tableName  = "student";
        String sql = "SELECT * FROM " + tableName + " WHERE 1 = 0";
        Integer column = jdbcTemplate.queryForObject(sql, Integer.class);
        log.info(column.toString());
    }
    @Test
    void getColumnCount1() {
//        执行一个查询，但是限制结果集为空，以避免返回任何数据。然后我们从结果集的ResultSetMetaData对象中获取列数
        String tableName = "sc";
        String sql = "SELECT * FROM " + tableName + " WHERE 1 = 0";
        Integer c = jdbcTemplate.query(sql, (ResultSetExtractor<Integer>) rs -> rs.getMetaData().getColumnCount());
        log.info(c.toString());
    }
    // 获取表的总页数
    @Test
    void getTotalPage() {
        String tableName = "student";
        int pageSize = 5;
        String sql = "SELECT COUNT(*) FROM " + tableName;
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class);
        log.info((rowCount + pageSize - 1) / pageSize+"");
    }
    @Test
    void getColumnNames() throws SQLException {
        List<String> columnNames = new ArrayList<>();
        String tableName = "student"; // 表名
        try {
            // 获取表格的元数据信息
            DatabaseMetaData metaData = jdbcTemplate.getDataSource().getConnection().getMetaData();
            ResultSet rs = metaData.getColumns(null, null, tableName, null);

            // 解析出表格的列名
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                columnNames.add(columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        log.info(columnNames.toString());
    }


}
