package com.atguigu.admin.controller;

import com.atguigu.admin.bean.Student;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@FXMLController
public class MainController implements Initializable {


    @Autowired
    JdbcTemplate jdbcTemplate;

    @FXML
    private AnchorPane sqlRadioPane;


    private Map<RadioButton, List<TextField>> radioButtonTextFieldMap;
    @FXML
    private TextField addressTextField;

    @FXML
    private TextField sNameTextField;

    @FXML
    private TextField ageFromTextField;

    @FXML
    private RadioButton classRadioButton;

    @FXML
    private TextField sexTextField;

    @FXML
    private Button sqlRadioButton;

    @FXML
    private TextField departmentTextField;

    @FXML
    private RadioButton ageRadioButton;

    @FXML
    private RadioButton sIdRadioButton;

    @FXML
    private TextField sIdTextField;

    @FXML
    private RadioButton sexRadioButton;

    @FXML
    private RadioButton addressRadioButton;

    @FXML
    private RadioButton departmentRadioButton;

    @FXML
    private TextField ageToTextField;

    @FXML
    private RadioButton sNameRadioButton;

    @FXML
    private Group age;

    @FXML
    private TextField classTextField;

    @FXML
    public RadioButton classNameRadioButton;
    @FXML
    public TextField classNameTextField;
    @FXML
    public RadioButton classIdRadioButton;
    @FXML
    public TextField classIdTextField;
    @FXML

    public RadioButton scoreRadioButton;
    @FXML
    public TextField scoreTextField;
    @FXML
    private VBox exeVBox;
    @FXML
    private Text sqlHeadLine;
    @FXML
    private TextArea sqlTextArea;
    @FXML
    private Text sqlExecuteHeadLine;
    @FXML
    private TextArea executionInfo;
    @FXML
    private Button sqlButton;
    @FXML
    private Pagination paginationSql;
    @FXML
    private TableView<ObservableList<String>> sqlTableView;

    // 分页参数
    private static final int PAGE_SIZE = 10;

    // TableColumn 对象列表，用于保存 tableview 的列信息
    private List<TableColumn<Student, ?>> tableColumns = new ArrayList<>();


    private ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

    // Pagination 对象，用于分页显示数据
    @FXML
    private Pagination paginationOne;
    @FXML
    private Pagination paginationTwo;
    @FXML
    private Pagination paginationThree;
    @FXML
    private VBox tableVBoxOne;
    @FXML
    private VBox tableVBoxTwo;
    @FXML
    private VBox tableVBoxThree;

    @FXML
    private Button ButtonTableOne;


    @FXML
    private Button ButtonTableThree;

    @FXML
    private TableView<ObservableList<String>> tableViewThree;

    @FXML
    private TextField textTableThree;

    @FXML
    private TextField textTableOne;

    @FXML
    private TableView<ObservableList<String>> tableViewOne;

    @FXML
    private TextField textTableTwo;

    @FXML
    private Button ButtonTableTwo;

    @FXML
    private TableView<ObservableList<String>> tableViewTwo;



    @FXML
    void onSql(ActionEvent event) {
        String sql = sqlTextArea.getText();
        log.info(sql);
        data = FXCollections.observableArrayList();
        if (isSelect(sql)){
            String tableName = getTableNameBySql(sql);
//            log.info("table name:" +tableName);

            if (isTableExist(tableName)) {
                initColumn(sqlTableView, tableName);
                try {
                    queryData(tableName, paginationSql, 10000);
    //                executionInfo.setText();
                } catch (DataAccessException e) {
                    String errorMessage = Objects.requireNonNull(e.getRootCause()).getMessage();
                    executionInfo.setText(errorMessage);
                }
            }else {
                executionInfo.setText("table not exist");
            }

        }else {
            executionInfo.setText("not a select");
        }
    }

    @FXML
    public void onSqlRadio(ActionEvent actionEvent) {
        String sql = "select student.s_id as stu_sid,\n" +
                "       student.s_name,\n" +
                "       student.s_sex,\n" +
                "       student.s_age,\n" +
                "       student.s_class,\n" +
                "       sc.s_id as sc_sid,\n" +
                "       sc.c_id,\n" +
                "       sc.score,\n" +
                "       course.c_id as course_cid,\n" +
                "       course.c_name,\n" +
                "       course.credit,\n" +
                "       course.c_hours,\n" +
                "       course.t_id\n" +
                "from student\n" +
                "join sc on student.s_id = sc.s_id\n" +
                "join course on course.c_id = sc.c_id\n";
        if(sIdRadioButton.isSelected()){
            sql += "where student.s_id = " + radioButtonTextFieldMap.get(sIdRadioButton).get(0).getText() + "\n";

        }else {
            sql += "where 1 = 1 \n";
        }
        if(classRadioButton.isSelected()){
            sql +=  "and student.s_class = " + radioButtonTextFieldMap.get(classRadioButton).get(0).getText() + "\n" ;
        }
        if(sNameRadioButton.isSelected()){
            sql += "and student.s_name = " + radioButtonTextFieldMap.get(sNameRadioButton).get(0).getText() + "\n";
        }
        if (sexRadioButton.isSelected()){
            sql += "and student.s_sex = " + "'" + radioButtonTextFieldMap.get(sexRadioButton).get(0).getText().toUpperCase()  + "'"+ "\n";
        }
        if (ageRadioButton.isSelected()){
            sql += "and student.s_age > " + radioButtonTextFieldMap.get(ageRadioButton).get(0).getText() + "\n";
            sql += "and student.s_age < " + radioButtonTextFieldMap.get(ageRadioButton).get(1).getText() + "\n";
        }
        if (classIdRadioButton.isSelected()) {
            sql += " and course.c_id = " + addSingleQuotation(radioButtonTextFieldMap.get(classIdRadioButton).get(0).getText()) + "\n";
        }
        if (scoreRadioButton.isSelected()) {
            sql += " and course.credit = " + addSingleQuotation(radioButtonTextFieldMap.get(scoreRadioButton).get(0).getText() )+ "\n";
        }
        if (classNameRadioButton.isSelected()){
            sql += " and course.c_name = " + addSingleQuotation(radioButtonTextFieldMap.get(classNameRadioButton).get(0).getText()) + "\n";
        }
        sql += " limit 2000";
        log.info(sql);
//                "where student.s_id = " +  + "\n" +
//                "and student.s_class = 1\n" +
//                "and student.s_name = '施平伦'\n" +
//                "and student.s_sex = 'F'\n" +
//                "and student.s_age > 1\n" +
//                "and student.s_age < 100";
        if (isSelect(sql)) {
            String tableName = getTableNameBySql(sql);
//            log.info("table name:" +tableName);

//            if (isTableExist(tableName)) {
                initColumn(sqlTableView, "student");
                addColumn(sqlTableView,"sc");
                addColumn(sqlTableView,"course");
//            }
        }
        queryDataBysql(sql, paginationSql);
    }

    public void initializeOnSqlRadio() {
        radioButtonTextFieldMap = new HashMap<>();
        for (Node node : sqlRadioPane.getChildren()) {
            if (node instanceof HBox) {
                HBox HBox = (HBox) node;
                RadioButton radioButton = null;
                List<TextField> textFieldList = new ArrayList<>();

                for (Node childNode : HBox.getChildren()) {
                    if (childNode instanceof RadioButton) {
                        radioButton = (RadioButton) childNode;
                    } else if (childNode instanceof HBox) {
                        HBox hbox = (HBox) childNode;

                        for (Node paneChildNode : hbox.getChildren()) {
                            if (paneChildNode instanceof TextField) {
                                textFieldList.add((TextField) paneChildNode);
                            }
                        }
                    }
                }

                if (radioButton != null && !textFieldList.isEmpty()) {
                    radioButtonTextFieldMap.put(radioButton, textFieldList);
                }
            }
        }
//        log.info(String.valueOf(radioButtonTextFieldMap.containsKey(addressRadioButton)));
    }

    private boolean isSelect(String sql) {
        return sql.trim().toUpperCase().startsWith("SELECT");
    }

    String getTableNameBySql(String sql){
        String tableName = null;

        Pattern pattern = Pattern.compile("FROM\\s+([\\w\\d_]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);

        if (matcher.find()) {
            tableName = matcher.group(1);
        }
        return tableName;
    }

    private boolean isTableExist(String tableName) {
        if (tableName != null) {
            try {
                Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
                DatabaseMetaData metaData = conn.getMetaData();
                ResultSet tables = metaData.getTables(null, null, tableName, null);
                boolean tableExists = tables.next();
                tables.close();
                conn.close();

                return tableExists;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//
        initPagination();
//                 将表格和分页控件添加到VBox中
        initVBox();
        initColumn(tableViewOne, "student");
        initColumn(tableViewTwo, "course");
        initColumn(tableViewThree, "sc");
        initDataSet();

        initializeOnSqlRadio();


    }

    private void initPagination() {
        // 初始化分页控件
        paginationOne.setPageFactory(pageIndex -> createPage(pageIndex, PAGE_SIZE, tableViewOne));
        paginationTwo.setPageFactory(pageIndex -> createPage(pageIndex, PAGE_SIZE, tableViewTwo));
        paginationThree.setPageFactory(pageIndex -> createPage(pageIndex, PAGE_SIZE, tableViewThree));
        paginationSql.setPageFactory(pageIndex -> createPage(pageIndex, PAGE_SIZE, sqlTableView));
    }

    private void initVBox() {
        // 将表格和分页控件添加到 VBox 中
        // 检查是否已经存在
        if (!tableVBoxOne.getChildren().contains(tableViewOne)) {
            // 不存在则添加
            tableVBoxOne.getChildren().add(tableViewOne);
        }
        if (!tableVBoxOne.getChildren().contains(paginationOne)) {
            tableVBoxOne.getChildren().add(paginationOne);
        }
    }

    private void initDataSet() {
        queryData("student",paginationOne);
        queryData("course",paginationTwo);
        queryData("sc",paginationThree, 10000);
//        for (ObservableList<String> row : data) {
//            for (String cell : row) {
//                System.out.print(cell + "\t");
//            }
//            System.out.println();
//        }
    }

    private void initColumn(TableView<ObservableList<String>> tableView, String tableName) {
        if (!tableView.getColumns().isEmpty()) {
            tableView.getColumns().clear();
        }
        addColumn(tableView, tableName);
    }
    private void addColumn(TableView<ObservableList<String>> tableView, String tableName) {
        // 初始化表格列
        List<String> columnNames = null; // 获取表格列名
        try {
            columnNames = getColumnNames(tableName);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        int base = tableView.getColumns().size();
        //i:最新要添加到tableView的数据库表的行在最新要添加到tableView的数据库表中的序号
//        index:最新要添加到tableView的数据库表的行在整个tableView中的序号
        for (int i = 0; i < Objects.requireNonNull(columnNames).size(); i++) {
            final int colIndex = i + base;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnNames.get(i));
            column.setCellValueFactory(cellData -> {
                ObservableList<String> rowValues = cellData.getValue();
                if (rowValues.size() > colIndex) {
                    return new ReadOnlyObjectWrapper<>(rowValues.get(colIndex));
                } else {
                    return new ReadOnlyObjectWrapper<>("");
                }
            });
            tableView.getColumns().add(column);
        }
    }
    private Node createPage(int pageIndex, int pageSize, TableView<ObservableList<String>> tableView) {
        int fromIndex = pageIndex * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, data.size());
        tableView.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));
        return new VBox(tableView);
    }
    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, data.size());
        tableViewOne.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));
        return tableVBoxOne;
    }
    public void queryData(String tableName, Pagination pagination) {
        queryData(tableName, pagination, Integer.MAX_VALUE);
    }
    public void queryData(String tableName, Pagination pagination, int limit) {
        data = FXCollections.observableArrayList();
        String sql = "SELECT * FROM " + tableName + " LIMIT " + limit;
        jdbcTemplate.query(sql, rs -> {
            ObservableList<String> row = FXCollections.observableArrayList();
            int columnCount = rs.getMetaData().getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                row.add(rs.getString(i));
            }
            data.add(row);
        });
        pagination.setPageCount(getTotalPage(tableName, 5));
        pagination.setCurrentPageIndex(1); // 切换到第一页
    }
    public void queryDataBysql(String sql, Pagination pagination) {
        data = FXCollections.observableArrayList();
        List<Map<String, Object>> queryResult = null;
        try {
            queryResult = jdbcTemplate.queryForList(sql);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        try {
            for (Map<String, Object> row : Objects.requireNonNull(queryResult)) {
                ObservableList<String> observableRow = FXCollections.observableArrayList();
                for (Object value : row.values()) {
                    if (value != null) {
                        observableRow.add(value.toString());
                    } else {
                        observableRow.add("");
                    }
                }
                data.add(observableRow);
            }
        } catch (Exception e) {
            initColumn(sqlTableView, "");
            e.printStackTrace();
        }
        if (null == queryResult || queryResult.isEmpty()){
            initColumn(sqlTableView, "");
        }
        pagination.setPageCount(getTotalPage(data.size(), 5));
        pagination.setCurrentPageIndex(1); // 切换到第一页
    }

    // 获取表的列数
    private int getColumnCount(String tableName) {
        //        执行一个查询，但是限制结果集为空，以避免返回任何数据。然后我们从结果集的ResultSetMetaData对象中获取列数
        String sql = "SELECT * FROM " + tableName + " WHERE 1 = 0";
        return jdbcTemplate.query(sql, (ResultSetExtractor<Integer>) rs -> rs.getMetaData().getColumnCount());
    }
    // 获取表的总页数
    private int getTotalPage(String tableName, int pageSize) {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class);
        return (rowCount + pageSize - 1) / pageSize;
    }
    // 获取表的总页数
    private int getTotalPage(int rowCount, int pageSize) {
        return (rowCount + pageSize - 1) / pageSize;
    }
    private List<String> getColumnNames(String tableName) throws SQLException {
        List<String> columnNames = new ArrayList<>();
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
        return columnNames;
    }
    @FXML
    public void onLoadTableOne(ActionEvent actionEvent) {
        onLoad(1);
    }
    @FXML
    public void onLoadTableTwo(ActionEvent actionEvent) {
        onLoad(2);
    }
    @FXML
    public void onLoadTableThree(ActionEvent actionEvent) {
        onLoad(3);
    }
    void onLoad(int tableNumber) {
        String tableName = "";
        Pagination pagination;
        TableView<ObservableList<String>> tableView;
        switch (tableNumber) {
            case 1 :
                tableName = textTableOne.getText();
                pagination = paginationOne;
                tableView = tableViewOne;
                break;
            case 2 :
                tableName = textTableTwo.getText();
                pagination = paginationTwo;
                tableView = tableViewTwo;
                break;
            case 3 :
                tableName = textTableThree.getText();
                pagination = paginationThree;
                tableView = tableViewThree;
                break;
            default:
                tableName = "student";
                pagination = paginationOne;
                tableView = tableViewOne;
        }
        if (isTableExist(tableName)){
            initColumn(tableView, tableName);
            queryData(tableName, pagination, 10000);
        }
    }
    private String addSingleQuotation(String name){
        return "'" + name + "'";
    }
}
