/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/10/22 19:52:34                          */
/*==============================================================*/


drop table if exists CS_MESSAGE;

drop table if exists CS_NODE;

drop table if exists CS_ORDER;

drop table if exists CS_USER;

drop table if exists CS_USER_LOG;

drop table if exists CS_USER_REF_NODE;

drop table if exists CS_WALLET;

drop table if exists CS_WALLET_LOG;

drop table if exists CS_WATER;

drop table if exists CS_WATER_LOG;

/*==============================================================*/
/* Table: CS_MESSAGE                                            */
/*==============================================================*/
create table CS_MESSAGE
(
   ID                   numeric(8,0) not null,
   USER_ID              numeric(8,0),
   CONTENT              varchar(255),
   UPATETIME            datetime,
   CREATETIME           datetime,
   primary key (ID)
);

/*==============================================================*/
/* Table: CS_NODE                                               */
/*==============================================================*/
create table CS_NODE
(
   ID                   bigint not null auto_increment,
   NAME                 varchar(255) not null,
   P_ID                 numeric not null,
   CREATETIME           datetime not null,
   UPDATETIME           datetime not null,
   REMARK               varchar(255),
   STATUS               numeric not null,
   TYPE                 numeric not null,
   primary key (ID)
);

/*==============================================================*/
/* Table: CS_ORDER                                              */
/*==============================================================*/
create table CS_ORDER
(
   ID                   char(24) not null,
   WALLET_ID            bigint not null,
   TYPE                 numeric not null,
   AMOUNT               numeric not null,
   STATUS               numeric not null,
   CREATETIME           datetime not null,
   UPDATETIME           datetime not null,
   CREDIT               numeric not null,
   BALANCE              numeric not null,
   OPERATOR_ID          numeric not null,
   GREENPAY_ID          varchar(255),
   primary key (ID)
);

/*==============================================================*/
/* Table: CS_USER                                               */
/*==============================================================*/
create table CS_USER
(
   ID                   bigint not null auto_increment,
   PHONE                char(11) not null,
   NAME                 varchar(255),
   PASSWORD             varchar(255) not null,
   SEX                  numeric,
   AGE                  numeric,
   ADDRESS              varchar(255),
   TYPE                 numeric not null,
   ROLE                 numeric not null,
   STATUS               numeric not null,
   UPDATETIME           datetime not null,
   CREATETIME           datetime not null,
   IP                   varchar(255),
   SESSION_ID           varchar(255),
   primary key (ID)
);

/*==============================================================*/
/* Table: CS_USER_LOG                                           */
/*==============================================================*/
create table CS_USER_LOG
(
   ID                   numeric(8,0) not null,
   USER_ID              numeric(8,0),
   TYPE                 numeric(8,0),
   OPERATOR_ID          numeric(8,0),
   CREATETIME           datetime,
   REMARK               varchar(255),
   primary key (ID)
);

/*==============================================================*/
/* Table: CS_USER_REF_NODE                                      */
/*==============================================================*/
create table CS_USER_REF_NODE
(
   ID                   bigint not null auto_increment,
   USER_ID              numeric not null,
   NODE_ID              numeric not null,
   CREATETIME           datetime not null,
   primary key (ID)
);

/*==============================================================*/
/* Table: CS_WALLET                                             */
/*==============================================================*/
create table CS_WALLET
(
   ID                   bigint not null auto_increment,
   NUM                  char(17) not null,
   USER_ID              numeric not null,
   CREDIT               numeric not null,
   BALANCE              numeric not null,
   UPDATETIME           datetime not null,
   CREATETIME           datetime not null,
   primary key (ID)
);

/*==============================================================*/
/* Index: Index_1                                               */
/*==============================================================*/
create unique index Index_1 on CS_WALLET
(
   NUM
);

/*==============================================================*/
/* Table: CS_WALLET_LOG                                         */
/*==============================================================*/
create table CS_WALLET_LOG
(
   ID                   bigint not null auto_increment,
   WALLET_ID            bigint not null,
   OPERATOR_ID          numeric not null,
   CREATETIME           datetime not null,
   REMARK               varchar(255) not null,
   primary key (ID)
);

/*==============================================================*/
/* Table: CS_WATER                                              */
/*==============================================================*/
create table CS_WATER
(
   ID                   bigint not null auto_increment,
   EQM_NUM              char(11) not null,
   USER_ID              numeric not null,
   OLD_VALUE            numeric not null,
   NEW_VALUE            numeric not null,
   UPDATETIME           datetime not null,
   CREATETIME           datetime not null,
   primary key (ID)
);

/*==============================================================*/
/* Index: Index_1                                               */
/*==============================================================*/
create unique index Index_1 on CS_WATER
(
   EQM_NUM
);

/*==============================================================*/
/* Table: CS_WATER_LOG                                          */
/*==============================================================*/
create table CS_WATER_LOG
(
   ID                   bigint not null auto_increment,
   WATER_ID             bigint not null,
   OPERATOR_ID          numeric not null,
   CREATETIME           datetime not null,
   REMARK               varchar(255),
   primary key (ID)
);

