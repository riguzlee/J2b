package com.riguz.j2b.model;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Strings;

public class Linq {
    List<String>  columns        = new ArrayList<String>();
    List<String>  tables         = new ArrayList<String>();
    List<Object>  params         = new ArrayList<Object>();
    Condition     condition;
    String        orderByFields  = "";
    Order         order          = null;

    StringBuilder builder        = new StringBuilder();

    final String  SPACE          = " ";
    final String  MYSQL_BACKTICK = "`";

    public abstract class Condition {
        String       sql;
        List<Object> params = new ArrayList<Object>();

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        public List<Object> getParams() {
            return params;
        }

        public void setParams(List<Object> params) {
            this.params = params;
        }

        Condition and(Condition right) {
            this.sql += " AND (" + right.getSql() + ") ";
            this.params.addAll(right.getParams());
            return this;
        }

        Condition or(Condition right) {
            this.sql += " OR (" + right.getSql() + ") ";
            this.params.addAll(right.getParams());
            return this;
        }
    }

    public class Order {
        String fields;
        ORDER_TYPE  order;

        public Order(String fields, ORDER_TYPE order) {
            this.fields = fields;
            this.order = order;
        }

        public String getFields() {
            return fields;
        }

        public void setFields(String fields) {
            this.fields = fields;
        }

        public ORDER_TYPE getOrder() {
            return order;
        }

        public void setOrder(ORDER_TYPE order) {
            this.order = order;
        }
    }

    enum ORDER_TYPE {
        DESC, ASC
    };

    public Linq select(String... columns) {
        this.columns.clear();
        this.tables.clear();
        this.condition = null;
        this.order = null;
        for (String c : columns)
            this.columns.add(c);
        return this;
    }

    public Linq from(String... tables) {
        for (String t : tables)
            this.columns.add(t);
        return this;
    }

    public Linq where(Condition condition) {
        this.condition = condition;
        return this;
    }

    public Linq orderBy(String field, ORDER_TYPE order) {
        this.order = new Order(field, order);
        return this;
    }

    void buildSelectSql() {
        this.builder.append("SELECT ");
        if (this.columns.isEmpty())
            throw new IllegalArgumentException("No field is selected.");
        for (String c : this.columns) {
            String field = c;
            String as = "";
            if (c.contains("AS")) {
                String[] tmp = c.split(".");
                if (tmp.length != 2)
                    throw new IllegalArgumentException("Invalid field expr.");
                field = tmp[0].trim();
                as = tmp[1].trim();
            }
            if (field.contains(".")) {
                String[] tmp = c.split(".");
                if (tmp.length != 2)
                    throw new IllegalArgumentException("Invalid field expr.");
                this.builder.append(MYSQL_BACKTICK + tmp[0] + MYSQL_BACKTICK + ".");
                if (!"*".equals(tmp[1]))
                    this.builder.append(MYSQL_BACKTICK + tmp[1] + MYSQL_BACKTICK + " ");
                else
                    this.builder.append("* ");
            }
            else {
                this.builder.append(MYSQL_BACKTICK + field + MYSQL_BACKTICK + " ");
                if (!Strings.isNullOrEmpty(as))
                    this.builder.append("AS " + MYSQL_BACKTICK + as + MYSQL_BACKTICK + " ");
            }
        }
    }

    public String buildSql() {

        return "";
    }

    public Object[] getParams() {
        return this.params.toArray();
    }
}
