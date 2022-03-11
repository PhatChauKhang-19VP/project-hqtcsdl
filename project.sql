create database db_a82d76_dogafow789
go
use db_a82d76_dogafow789
go


--use master
--drop database db_a82d76_dogafow789
--go



-- CREATE TABLE - NO FKEY
create table  LOGIN_INFOS (
    username varchar(50) primary key not null,
    password varchar(512) not null,
    RID varchar(10) not null,
	status_ int not null -- status_ in range [0,2]. 0: pending, 1: active, 2: inactive
);
go 

create table ROLES (
	RID varchar(10) primary key not null,
	name_ varchar(20) not null
);
go 

create table PARTNERS (
	username varchar(50) primary key not null,
	name_ nvarchar(50) not null,
	representative_name nvarchar(50) not null,
	province nvarchar(20) not null,
	district nvarchar(20) not null,
	branch_number int not null,
	order_number int not null,
	product_type_id varchar(10) not null,
	address_line nvarchar(100) not null,
	phone char(10) not null,
	mail varchar(50) not null
);
go


create table PARTNER_REGISTRATIONS (
	PRID int primary key IDENTITY(1,1),
	username varchar(50) not null,
	register_time datetime not null,
	status_ bit not null
);
go

create table PARTNER_BRANCHES (
	PBID varchar(10) primary key,
	username varchar(50) not null,
	name_ nvarchar(50) not null,
	province nvarchar(20) not null,
	district nvarchar(20) not null,
	address_line nvarchar(100) not null,
	CID varchar(10) not null,
	extension int
)
go

create table CONTRACTS (
	CID varchar(10),
	username varchar(50) not null,
	extension int not null,
	TIN varchar(20) not null,
	representative_name varchar(50) not null,
	created_at datetime not null,
	expired_at datetime not null,
	commission float not null,
	primary key(CID, extension)
)
go


create table PRODUCT_TYPES (
	product_type_id varchar(10) primary key,
	name_ nvarchar(50)
)
go

create table PRODUCTS(
	PID varchar(10) primary key,
	product_type_id varchar(10),
	username varchar(50),
	name_ nvarchar(50),
	description varchar(200),
	price float,
)
go

create table PRODUCT_IN_BRANCHES (
	PBID varchar(10) not null,
	PID varchar(10) not null,
	primary key(PBID, PID)
)
go

create table ORDERS (
	order_id varchar(10) primary key,
	customer_username varchar(50) not null,
)
go

create table ORDERS_DETAILS (
	order_id varchar(10) not null,
	username varchar(50) not null,
	PID varchar(10) not null,
	primary key (order_id, username, PID)
)
go

create table CUSTOMERS (
	username varchar(50) primary key,
	name_ nvarchar(50) not null,
	phone char(10) not null,
	province nvarchar(20) not null,
	district nvarchar(20) not null,
	address_line nvarchar(50) not null,
	mail varchar(50) not null,
)
go

create table DRIVERS (
	username varchar(50) primary key,
	name_ nvarchar(50) not null,
	NIN char(12) not null,
	province nvarchar(20) not null,
	district nvarchar(20) not null,
	address_line nvarchar(100) not null,
	area nvarchar(50) not null,
	mail varchar(50) not null,
	BID varchar(10) not null
)
go

create table EMPLOYEES (
	username varchar(50) primary key,
	name_ nvarchar(50) not null,
	mail nvarchar(50) not null
)
go

create table ADMINS (
	username varchar(50) primary key,
	name_ nvarchar(50) not null,
	
)
go

create table BANKS (
	BID varchar(10) primary key,
	name_ nvarchar(50) not null,
	branch nvarchar(50) not null
)
go

create table DRIVER_REGISTRATIONS (
	VIN char(17) primary key,
	driver_username varchar(50) not null,
	fee float not null,
	status_ bit not null
)
go

create table DRIVER_HISTORIES (
	order_id varchar(10) primary key,
	driver_username varchar(50) not null,
	income float not null
)
go

-- SCRIPTS TO GENERATE FOREIGNE KEYS 


-- LOGIN_INFOS
alter table LOGIN_INFOS 
	add foreign key (RID) references ROLES(RID);
go

-- PARTNERS
alter table PARTNERS
	add foreign key (username) references LOGIN_INFOS(username);
go

alter table PARTNERS
	add foreign key (product_type_id) references PRODUCT_TYPES(product_type_id)
go

-- PARTNER_REGISTRATIONS

alter table PARTNER_REGISTRATIONS
	add foreign key (username) references PARTNERS(username);
go

alter table PARTNER_REGISTRATIONS
	add foreign key (username) references PARTNERS(username);
go

-- PARTNER_BRANCHES
alter table PARTNER_BRANCHES
	add foreign key (username) references PARTNERS(username);
go

alter table PARTNER_BRANCHES
	add foreign key (CID, extension) references CONTRACTS(CID, extension)
go

-- CONTRACTS
alter table CONTRACTS
	add foreign key (username) references LOGIN_INFOS(username)
go

-- PRODUCTS
alter table PRODUCTS
	add foreign key (username) references LOGIN_INFOS(username)
go

alter table PRODUCTS
	add foreign key (product_type_id) references PRODUCT_TYPES(product_type_id)
go

-- PRODUCT_IN_BRANCHES
alter table PRODUCT_IN_BRANCHES
	add foreign key (PID) references PRODUCTS(PID)
go
alter table PRODUCT_IN_BRANCHES
	add foreign key (PBID) references PARTNER_BRANCHES(PBID)
go

-- ORDERS
alter table ORDERS
	add foreign key (customer_username) references LOGIN_INFOS(username)
go

-- ORDER_DETAILS
alter table ORDERS_DETAILS
	add foreign key (order_id) references ORDERS(order_id)
go

alter table ORDERS_DETAILS
	add foreign key (PID) references PRODUCTS(PID)
go


-- CUSTOMERS
alter table CUSTOMERS
	add foreign key (username) references LOGIN_INFOS(username)
go

-- DRIVERS
alter table DRIVERS
	add foreign key (username) references LOGIN_INFOS(username)
go

alter table DRIVERS
	add foreign key (BID) references BANKS(BID);
go

-- EMPLOYEES
alter table EMPLOYEES
	add foreign key (username) references LOGIN_INFOS(username)
go

-- ADMINS
alter table ADMINS
	add foreign key (username) references LOGIN_INFOS(username)
go

-- DRIVER_REGISTRATIONS
alter table DRIVER_REGISTRATIONS
	add foreign key (driver_username) references LOGIN_INFOS(username);
go

-- DRIVER_HISTORIES
alter table DRIVER_HISTORIES
	add foreign key (driver_username) references LOGIN_INFOS(username);
go

alter table DRIVER_HISTORIES
	add foreign key (order_id) references ORDERS(order_id);
go

-- FIRST INIT WITH 5 ROLES
insert into ROLES (RID, name_)
values
	('ADMIN', 'ADMIN'),
	('EMPLOYEE', 'EMPLOYEE'),
	('PARTNER', 'PARTNER'),
	('CUSTOMER', 'CUSTOMER'),
	('DRIVER', 'DRIVER');

	-- FIRST INIT WITH 2 PRODUCT_TYPES
insert into PRODUCT_TYPES
values
	('PTYPE001', 'FOOD'),
	('PTYPE002', 'DRINK');