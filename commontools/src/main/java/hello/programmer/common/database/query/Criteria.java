package hello.programmer.common.database.query;

import hello.programmer.common.basic.Combo2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author liwei
 * @Description sql查询生成器,暂时只支持and操作
 * @date 2017/6/29 15:58
 */
public  class Criteria {

    private List<Criterion> criterions;

    private List<String> appendCondtions;

    private String orderByClause;

    private Map<String,Boolean> preCheckMap = new HashMap<>();

    public static  Criteria create(){
        return new Criteria();
    }
    private Criteria() {
        super();
        criterions = new ArrayList<>();
        appendCondtions = new ArrayList<>();
    }

    public boolean isValid() {
        return criterions.size() > 0;
    }

    public void clear(){
        preCheckMap.clear();
        criterions.clear();
        appendCondtions.clear();
    }

    public Criteria append(String condition){
        if(condition == null || condition.length() == 0){
            throw new RuntimeException("参数不能为空");
        }
        appendCondtions.add(condition);
        return this;
    }

    public Criteria preNull(String column){
        preCheckMap.put(column +":null",true);
        return this;
    }

    public Criteria preEmpty(String column){
        preCheckMap.put(column +":empty",true);
        return this;
    }



    public Criteria preNull(String column,String value){
        if(value == null){
            preCheckMap.put(column,true);
        }
        return this;
    }

    public Criteria preEmpty(String column,Object value){
        if(value == null || value.toString().length() == 0){
            preCheckMap.put(column,true);
        }
        return this;
    }

    /**
     * 排序条件设置
     * @param orderByClause 前面不要以order by开头，直接写如 id desc,time asc
     * @return
     */
    public Criteria orderBy(String orderByClause){
        if(orderByClause == null || orderByClause.trim().length() == 0 || orderByClause.trim().toLowerCase().startsWith("order by")){
            throw new RuntimeException("orderByClause 错误");
        }
        this.orderByClause = orderByClause;
        return this;
    }

    public Criteria andIsNull(String column) {
        addCriterion(column +" is null");
        return  this;
    }

    public Criteria andEqualTo(String column,Object value) {
        addCriterion(column +" =", value, column);
        return  this;
    }


    /**
     * 此方法操作两个数据列相等 如 a.id = b.id
     * @param column1
     * @param column2
     * @return
     */
    public Criteria andEqualToColumn(String column1,String column2) {
        if (column1 == null || column2 == null) {
            throw new RuntimeException("Value cannot be null");
        }
        criterions.add(new Criterion(column1 +" = ", column2,true));

        return  this;
    }


    public Criteria andIsNotNull(String column) {
        addCriterion(column + " is not null");
        return  this;
    }

    public Criteria andNotEqualTo(String column,Object value) {
        addCriterion(column + " <>", value, column);
        return  this;
    }



    public Criteria andGreaterThan(String column,Object value) {
        addCriterion(column + " >", value, column);
        return  this;
    }



    public Criteria andGreaterThanOrEqualTo(String column,Object value) {
        addCriterion(column + " >=", value, column);
        return  this;
    }


    public Criteria andLessThan(String column,Object value) {
        addCriterion(column + " <", value, column);
        return  this;
    }



    public Criteria andLessThanOrEqualTo(String column,Object value) {
        addCriterion(column + " <=", value, column);
        return  this;
    }


    public Criteria andIn(String column,List<Object> values) {
        addCriterion(column +" in", values, column);
        return  this;
    }

    public Criteria andIn(String column,  Object... values) {
        addCriterion(column +" in", Arrays.asList(values), column);
        return  this;
    }



    public Criteria andNotIn(String column,List<Object> values) {
        addCriterion(column +" in", values, column);
        return  this;
    }



    public Criteria andBetween(String column,Object value1, Object value2) {
        addCriterion(column +" between", value1, value2, column);
        return  this;
    }

    public Criteria andNotBetween(String column,Object value1, Object value2) {
        addCriterion(column +" not between", value1, value2, column);
        return  this;
    }


    public Criteria andLeftLike(String column,String value) {
        if(checkNull(column + " like",value) || checkEmpty(column + " like",value)){
            return this;
        }
        addCriterion(column + " like", value + "%", column);
        return  this;
    }

    public Criteria andRightLike(String column,String value) {
        if(checkNull(column + " like",value) || checkEmpty(column + " like",value)){
            return this;
        }
        addCriterion(column + " like", "%" + value , column);
        return  this;
    }

    public Criteria andFullLike(String column,String value) {
        if(checkNull(column + " like",value) || checkEmpty(column + " like",value)){
            return this;
        }
        addCriterion(column + " like", "%" + value + "%", column);
        return  this;
    }



    /**
     * 比较日期 yyyy-MM-dd 相等
     * @param column
     * @param value
     * @return
     */
    public Criteria andDateEqualTo(String column,Date value) {
        addCriterionForJDBCDate(column +" =", value, column);
        return  this;
    }

    /**
     * 比较 DateTime yyyy-MM-dd HH:mm:ss 相等
     * @param column
     * @param value
     * @return
     */
    public Criteria andDateTimeEqualTo(String column,Date value) {
        addCriterionForJDBCDateTime(column +" =", value, column);
        return  this;
    }

    public Criteria andDateNotEqualTo(String column,Date value) {
        addCriterionForJDBCDate(column +" <>", value, column);
        return  this;
    }

    public Criteria andDateTimeNotEqualTo(String column,Date value) {
        addCriterionForJDBCDateTime(column +" <>", value, column);
        return  this;
    }

    public Criteria andDateGreaterThan(String column,Date value) {
        addCriterionForJDBCDate(column +" >", value, column);
        return  this;
    }

    public Criteria andDateTimeGreaterThan(String column,Date value) {
        addCriterionForJDBCDateTime(column +" >", value, column);
        return  this;
    }

    public Criteria andDateGreaterThanOrEqualTo(String column,Date value) {
        addCriterionForJDBCDate(column +" >=", value, column);
        return  this;
    }

    public Criteria andDateTimeGreaterThanOrEqualTo(String column,Date value) {
        addCriterionForJDBCDateTime(column +" >=", value, column);
        return  this;
    }

    public Criteria andDateLessThan(String column,Date value) {
        addCriterionForJDBCDate(column +" <", value, column);
        return  this;
    }

    public Criteria andDateTimeLessThan(String column,Date value) {
        addCriterionForJDBCDateTime(column +" <", value, column);
        return  this;
    }

    public Criteria andDateLessThanOrEqualTo(String column,Date value) {
        addCriterionForJDBCDate(column +" <=", value, column);
        return  this;
    }

    public Criteria andDateTimeLessThanOrEqualTo(String column,Date value) {
        addCriterionForJDBCDateTime(column +" <=", value, column);
        return  this;
    }

    public Criteria andDateIn(String column,List<Date> values) {
        addCriterionForJDBCDate(column +" in", values, column);
        return  this;
    }

    public Criteria andDateTimeIn(String column,List<Date> values) {
        addCriterionForJDBCDateTime(column +" in", values, column);
        return  this;
    }

    public Criteria andDateNotIn(String column,List<Date> values) {
        addCriterionForJDBCDate(column +" not in", values, column);
        return  this;
    }

    public Criteria andDateTimeNotIn(String column,List<Date> values) {
        addCriterionForJDBCDateTime(column +" not in", values, column);
        return  this;
    }

    public Criteria andDateBetween(String column,Date value1, Date value2) {
        addCriterionForJDBCDate(column +" between", value1, value2, column);
        return  this;
    }

    public Criteria andDateTimeBetween(String column,Date value1, Date value2) {
        addCriterionForJDBCDateTime(column +" between", value1, value2, column);
        return  this;
    }

    public Criteria andDateNotBetween(String column,Date value1, Date value2) {
        addCriterionForJDBCDate(column +" not between", value1, value2, column);
        return  this;
    }

    public Criteria andDateTimeNotBetween(String column,Date value1, Date value2) {
        addCriterionForJDBCDateTime(column +" not between", value1, value2, column);
        return  this;
    }


    /**
     *
     * @return
     */
    public String build(){
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < criterions.size(); i++){
            Criterion criterion = criterions.get(i);

            if(i != 0){
                buffer.append(" AND ");
            }

            if(criterion.isNoValue()){
                buffer.append(criterion.getCondition());
            }

            if(criterion.isSingleValue()){
                buffer.append(criterion.getCondition()).append(" ");
                Object v = criterion.getValue();

                boolean isString = !criterion.isColumnEq() && (v instanceof String || v instanceof Date);

                buffer.append(isString ? "'" + v +"'" : v);
            }

            if(criterion.isBetweenValue()){
                buffer.append(criterion.getCondition()).append(" ");
                Object v = criterion.getValue();
                buffer.append(v instanceof String || v instanceof Date ? "'" + v +"'" : v);

                Object sv = criterion.getSecondValue();
                buffer.append(" AND ").append(sv instanceof String || sv instanceof Date ? "'" + sv +"'" : sv);
            }

            if(criterion.isListValue()){
                buffer.append(criterion.getCondition()).append("(");
                List<Object> objects = (List<Object>)criterion.getValue();
                for(int j=0; j<objects.size(); j++){
                    Object obj = objects.get(j);
                    if(obj instanceof String || obj instanceof Date){
                        buffer.append("'" + obj +"'");
                    }
                    else{
                        buffer.append(obj);
                    }
                    if(j != objects.size() - 1){
                        buffer.append(",");
                    }
                }
                buffer.append(")");
            }
        }


        for(int i = 0; i < appendCondtions.size(); i++){
            if(i == 0 && criterions.size() == 0){
                buffer.append(appendCondtions.get(i));
            }
            else{
                buffer.append(" AND " + appendCondtions.get(i));
            }

        }

        String whereString = buffer.toString();
        if(whereString == null || whereString.length() == 0){
            return "";
        }

        return whereString;
    }


    /**
     *
     * @param sql
     * @param withWhere
     * @return
     */
    public String build(String sql,boolean withWhere){
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < criterions.size(); i++){
            Criterion criterion = criterions.get(i);

            if(i != 0){
                buffer.append(" AND ");
            }

            if(criterion.isNoValue()){
                buffer.append(criterion.getCondition());
            }

            if(criterion.isSingleValue()){
                buffer.append(criterion.getCondition()).append(" ");
                Object v = criterion.getValue();

                boolean isString = !criterion.isColumnEq() && (v instanceof String || v instanceof Date);

                buffer.append(isString ? "'" + v +"'" : v);
            }

            if(criterion.isBetweenValue()){
                buffer.append(criterion.getCondition()).append(" ");
                Object v = criterion.getValue();
                buffer.append(v instanceof String || v instanceof Date ? "'" + v +"'" : v);

                Object sv = criterion.getSecondValue();
                buffer.append(" AND ").append(sv instanceof String || sv instanceof Date ? "'" + sv +"'" : sv);
            }

            if(criterion.isListValue()){
                buffer.append(criterion.getCondition()).append("(");
                List<Object> objects = (List<Object>)criterion.getValue();
                for(int j=0; j<objects.size(); j++){
                    Object obj = objects.get(j);
                    if(obj instanceof String || obj instanceof Date){
                        buffer.append("'" + obj +"'");
                    }
                    else{
                        buffer.append(obj);
                    }
                    if(j != objects.size() - 1){
                        buffer.append(",");
                    }
                }
                buffer.append(")");
            }
        }


        for(int i = 0; i < appendCondtions.size(); i++){
            if(i == 0 && criterions.size() == 0){
                buffer.append(appendCondtions.get(i));
            }
            else{
                buffer.append(" AND " + appendCondtions.get(i));
            }

        }

        String whereString = buffer.toString();
        if(whereString == null || whereString.length() == 0){
            return "";
        }

        String _sql = sql + (withWhere ? " AND " + whereString : " WHERE " + whereString);

        return _sql;
    }

    public Combo2<String,Object[]> build2(){
        StringBuffer buffer = new StringBuffer();
        List<Object> params = new ArrayList<>();

        for(int i = 0; i < criterions.size(); i++){
            Criterion criterion = criterions.get(i);
            if(i != 0){
                buffer.append(" AND ");
            }

            if(criterion.isNoValue()){
                buffer.append(criterion.getCondition());
            }

            if(criterion.isSingleValue()){
                buffer.append(criterion.getCondition()).append(" ");
                Object v = criterion.getValue();

                boolean isString = !criterion.isColumnEq() && (v instanceof String || v instanceof Date);
                //v = isString ? "'" + v +"'" : v;
                if(criterion.isColumnEq()){
                    buffer.append(v);
                }
                else{
                    params.add(v);
                    buffer.append("?");
                }

            }

            if(criterion.isBetweenValue()){
                buffer.append(criterion.getCondition()).append(" ");
                Object v = criterion.getValue();
                //buffer.append(v instanceof String || v instanceof Date ? "'" + v +"'" : v);

                params.add(v);
                buffer.append("?");

                Object sv = criterion.getSecondValue();
                //buffer.append(" AND ").append(sv instanceof String || sv instanceof Date ? "'" + sv +"'" : sv);

                params.add(sv);
                buffer.append(" AND ?");
            }

            if(criterion.isListValue()){
                buffer.append(criterion.getCondition()).append("(");
                List<Object> objects = (List<Object>)criterion.getValue();
                for(int j=0; j<objects.size(); j++){

                    buffer.append("?");

                    Object obj = objects.get(j);
                    if(obj instanceof String || obj instanceof Date){
                       // buffer.append("'" + obj +"'");
                        params.add( obj );
                    }
                    else{
                        //buffer.append(obj);
                        params.add(obj);
                    }
                    if(j != objects.size() - 1){
                        buffer.append(",");
                    }
                }
                buffer.append(")");
            }
        }

        //String _sql = sql + (withWhere ? " AND " + buffer.toString() : " WHERE " + buffer.toString());

//        if(orderByClause != null && orderByClause.trim().length() > 0){
//            _sql += " ORDER BY " + orderByClause;
//        }

        System.out.println(params);

        Combo2<String,Object[]> combo2 = new Combo2(buffer.toString(),params.toArray());
        return combo2;
    }

    private void addCriterion(String condition) {
        if (condition == null) {
            throw new RuntimeException("Value for condition cannot be null");
        }
        criterions.add(new Criterion(condition));
    }

    private boolean checkNull(String condition,Object val) {
        String column = condition.substring(0,condition.indexOf(" "));
        if (preCheckMap.containsKey(column + ":null") && val == null) {
            return true;
        }
        return false;
    }

    private boolean checkEmpty(String condition,Object val){
        String column = condition.substring(0,condition.indexOf(" "));
        if(preCheckMap.containsKey(column+":empty") && (val == null || val.toString().length() == 0)){
            return true;
        }

        return false;
    }



    private void addCriterion(String condition, Object value, String property) {
        if(checkNull(condition,value) || checkEmpty(condition,value)){
            return;
        }
        if (value == null) {
            throw new RuntimeException("Value for " + property + " cannot be null");
        }
        criterions.add(new Criterion(condition, value));
    }

    private void addCriterion(String condition, Object value1, Object value2, String property) {
        if(checkNull(condition,value1) || checkEmpty(condition,value1)){
            return;
        }
        if(checkNull(condition,value2) || checkEmpty(condition,value2)){
            return;
        }
        if (value1 == null || value2 == null) {
            throw new RuntimeException("Between values for " + property + " cannot be null");
        }
        criterions.add(new Criterion(condition, value1, value2));
    }


    private void addCriterionForJDBCDate(String condition, Date value, String property) {
        if(checkNull(condition,value) || checkEmpty(condition,value)){
            return;
        }
        if (value == null) {
            throw new RuntimeException("Value for " + property + " cannot be null");
        }
        addCriterion(condition, new java.sql.Date(value.getTime()), property);
    }

    private void addCriterionForJDBCDateTime(String condition, Date value, String property) {
        if(checkNull(condition,value) || checkEmpty(condition,value)){
            return;
        }
        if (value == null) {
            throw new RuntimeException("Value for " + property + " cannot be null");
        }
        DateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        addCriterion(condition, parser.format(value), property);
    }



    private void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
        if(checkNull(condition,values) || checkEmpty(condition,values)){
            return;
        }
        if (values == null || values.size() == 0) {
            throw new RuntimeException("Value list for " + property + " cannot be null or empty");
        }
        List<java.sql.Date> dateList = new ArrayList<>();
        Iterator<Date> iter = values.iterator();
        while (iter.hasNext()) {
            dateList.add(new java.sql.Date(iter.next().getTime()));
        }
        addCriterion(condition, dateList, property);
    }

    private void addCriterionForJDBCDateTime(String condition, List<Date> values, String property) {
        if(checkNull(condition,values) || checkEmpty(condition,values)){
            return;
        }
        if (values == null || values.size() == 0) {
            throw new RuntimeException("Value list for " + property + " cannot be null or empty");
        }
        DateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Object> dateList = new ArrayList<>();
        Iterator<Date> iter = values.iterator();
        while (iter.hasNext()) {
            dateList.add(parser.format(iter.next()));
        }
        addCriterion(condition, dateList, property);
    }

    private void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
        if(checkNull(condition,value1) || checkEmpty(condition,value1)){
            return;
        }
        if(checkNull(condition,value2) || checkEmpty(condition,value2)){
            return;
        }
        if (value1 == null || value2 == null) {
            throw new RuntimeException("Between values for " + property + " cannot be null");
        }
        addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
    }
    private void addCriterionForJDBCDateTime(String condition, Date value1, Date value2, String property) {
        if(checkNull(condition,value1) || checkEmpty(condition,value1)){
            return;
        }
        if(checkNull(condition,value2) || checkEmpty(condition,value2)){
            return;
        }
        if (value1 == null || value2 == null) {
            throw new RuntimeException("Between values for " + property + " cannot be null");
        }
        DateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        addCriterion(condition, parser.format(value1), parser.format(value2), property);
    }


    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean columnEq;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isColumnEq(){
            return columnEq;
        }

        public void setColumnEq(boolean columnEq){
            this.columnEq = columnEq;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        private Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        private Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        private Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        private Criterion(String condition, Object value,boolean columnEq) {
            this(condition, value, null);
            this.columnEq = columnEq;
        }

        private Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        private Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }

}
