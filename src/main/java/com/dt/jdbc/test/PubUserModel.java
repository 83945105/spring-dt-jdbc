package com.dt.jdbc.test;

import com.dt.core.bean.*;
import com.dt.core.norm.Model;

public final class PubUserModel implements Model<PubUserModel, PubUserModel.Column, PubUserModel.On, PubUserModel.Where, PubUserModel.Sort, PubUserModel.Group> {

    public static final String tableName = "pub_user";
    public static final String tableAlias = "PubUser";
    public static final String primaryKeyName = "id";
    public static final String primaryKeyAlias = "id";
    public static final String id = "id";
    public static final String id_alias = "id";
    public static final String loginName = "login_name";
    public static final String loginName_alias = "loginName";

    public PubUserModel() {
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public String getTableAlias() {
        return tableAlias;
    }

    @Override
    public String getPrimaryKeyName() {
        return primaryKeyName;
    }

    @Override
    public String getPrimaryKeyAlias() {
        return primaryKeyAlias;
    }

    @Override
    public ColumnModel<PubUserModel, Column, On, Where, Sort, Group> getColumn() {
        return new Column();
    }

    public static final class Column extends ColumnModel<PubUserModel, Column, On, Where, Sort, Group> {

        private Column() {
        }

        public Column id() {
            this.columns.put(PubUserModel.id, PubUserModel.id_alias);
            return this;
        }

        public Column loginName(String alias) {
            this.columns.put(PubUserModel.loginName, loginName_alias);
            return this;
        }
    }

    @Override
    public OnModel<PubUserModel, Column, On, Where, Sort, Group> getOn() {
        return new On();
    }

    public static final class On extends OnModel<PubUserModel, Column, On, Where, Sort, Group> {

        private On() {
        }

        public OnBuilder<PubUserModel, Column, On, Where, Sort, Group> id() {
            return this.onBuilder.handler(PubUserModel.tableName, PubUserModel.tableAlias, PubUserModel.id);
        }

        public OnBuilder<PubUserModel, Column, On, Where, Sort, Group> loginName() {
            return this.onBuilder.handler(PubUserModel.tableName, PubUserModel.tableAlias, PubUserModel.loginName);
        }
    }

    @Override
    public Where getWhere() {
        return new Where();
    }

    public static final class Where extends WhereModel<PubUserModel, Column, On, Where, Sort, Group> {

        private Where() {
        }

        public WhereBuilder<PubUserModel, Column, On, Where, Sort, Group> id() {
            return this.whereBuilder.handler(PubUserModel.tableName, PubUserModel.tableAlias, PubUserModel.id);
        }

        public WhereBuilder<PubUserModel, Column, On, Where, Sort, Group> loginName() {
            return this.whereBuilder.handler(PubUserModel.tableName, PubUserModel.tableAlias, PubUserModel.loginName);
        }

    }

    @Override
    public GroupModel getGroup() {
        return new Group();
    }

    public static final class Group extends GroupModel<PubUserModel, Column, On, Where, Sort, Group> {

        private Group() {
        }

        public Group id() {
            this.columns.add(PubUserModel.id);
            return this;
        }

        public Group loginName() {
            this.columns.add(PubUserModel.loginName);
            return this;
        }

    }

    @Override
    public SortModel getSort() {
        return new Sort();
    }

    public static final class Sort extends SortModel<PubUserModel, Column, On, Where, Sort, Group> {

        public SortBuilder<PubUserModel, Column, On, Where, Sort, Group> id() {
            return this.sortBuilder.handler(PubUserModel.id);
        }

        public SortBuilder<PubUserModel, Column, On, Where, Sort, Group> loginName() {
            return this.sortBuilder.handler(PubUserModel.loginName);
        }

    }

}