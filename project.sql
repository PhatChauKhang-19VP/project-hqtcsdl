create database db_app
go

use db_app
go

-- CREATE TABLE - NO FKEY
create table  dbo.LOGIN_INFOS (
    username varchar(50) primary key not null,
    password varchar(512) not null,
    RID varchar(10) not null,
	status varchar(20) not null -- pending, active, inactive
);
go 

create table dbo.PARTNERS (
	username varchar(50) primary key not null,
	name nvarchar(50) not null,
	representative_name nvarchar(50) not null,
	province nvarchar(20) not null,
	district nvarchar(20) not null,
	branch_number int not null,
	order_number int not null,
	product_type_id varchar(10) not null,
	address_line nvarchar(50) not null,
	phone varchar(10) not null,
	mail varchar(50) not null
);
go


create table dbo.PARTNER_REGISTRATIONS (
	PRID int primary key IDENTITY(1,1),
	username varchar(50) not null,
	register_time datetime not null,
	status varchar(20) not null
);
go

create table dbo.PARTNER_BRANCHES (
	PBID varchar(10) primary key,
	username varchar(50) not null,
	name nvarchar(50) not null,
	province nvarchar(20) not null,
	district nvarchar(20) not null,
	address_line nvarchar(50) not null,
	CID varchar(10) not null,
	extension int
)
go

create table dbo.CONTRACTS (
	CID varchar(10),
	username varchar(50) not null,
	extension int not null,
	TIN varchar(20) not null,
	representative_name nvarchar(50) not null,
	brach_number int not null,
	created_at datetime not null,
	expired_at datetime not null,
	commission float not null,
	status varchar(20) not null -- accepted/not-accepted
	primary key(CID, extension)
)
go


create table dbo.PRODUCT_TYPES (
	product_type_id varchar(10) primary key,
	name nvarchar(50) not null
)
go

create table dbo.PRODUCTS(
	PID varchar(10) primary key,
	product_type_id varchar(10) not null,
	username varchar(50) not null,
	img_src nvarchar(200) not null,
	name nvarchar(50) not null,
	description nvarchar(200) not null,
	price float not null,
)
go

create table dbo.PRODUCT_IN_BRANCHES (
	PBID varchar(10) not null,
	PID varchar(10) not null,
	primary key(PBID, PID)
)
go

create table dbo.ORDERS (
	order_id varchar(10) primary key,
	partner_username varchar(50) not null,
	customer_username varchar(50) not null,
	payment_method varchar(10) not null, -- CASH, MOMO, ZALOPAY, CREDIT, QRPAY,
	delivery_province nvarchar(20) not null,
	delivery_district nvarchar(20) not null,
	delivery_adsress_line nvarchar(50) not null,
	delivery_status varchar(20) not null,
	paid_status varchar(20) not null,
	price float not null
)
go

create table dbo.ORDERS_DETAILS (
	order_id varchar(10) not null,
	PID varchar(10) not null,
	quantity int not null,
	primary key (order_id, PID)
)
go

create table dbo.CUSTOMERS (
	username varchar(50) primary key,
	name nvarchar(50) not null,
	phone char(10) not null,
	province nvarchar(20) not null,
	district nvarchar(20) not null,
	address_line nvarchar(50) not null,
	mail varchar(50) not null,
)
go

create table dbo.DRIVERS (
	username varchar(50) primary key,
	name nvarchar(50) not null,
	NIN char(12) not null,
	province nvarchar(20) not null,
	district nvarchar(20) not null,
	address_line nvarchar(50) not null,
	area nvarchar(50) not null, -- district
	mail varchar(50) not null,
	BID varchar(20) not null
)
go

create table dbo.EMPLOYEES (
	username varchar(50) primary key,
	name nvarchar(50) not null,
	mail nvarchar(50) not null
)
go

create table dbo.ADMINS (
	username varchar(50) primary key,
	name nvarchar(50) not null,	
)
go

create table dbo.BANKS (
	BID varchar(20) primary key,
	name nvarchar(50) not null,
	branch nvarchar(50) not null
)
go

create table dbo.DRIVER_REGISTRATIONS (
	VIN char(17) primary key,
	driver_username varchar(50) not null,
	fee float not null,
	registration_status varchar(20) not null,
	paid_fee_status varchar(20) not null
)
go

create table dbo.DRIVER_HISTORIES (
	order_id varchar(10) primary key,
	driver_username varchar(50) not null,
	income float not null
)
go

-- SCRIPTS TO GENERATE FOREIGNE KEYS 


-- LOGIN_INFOS

-- PARTNERS
alter table dbo.PARTNERS
	add foreign key (username) references dbo.LOGIN_INFOS(username);
go

alter table dbo.PARTNERS
	add foreign key (product_type_id) references dbo.PRODUCT_TYPES(product_type_id)
go

-- PARTNER_REGISTRATIONS

alter table dbo.PARTNER_REGISTRATIONS
	add foreign key (username) references dbo.PARTNERS(username);
go

alter table dbo.PARTNER_REGISTRATIONS
	add foreign key (username) references dbo.PARTNERS(username);
go

-- PARTNER_BRANCHES
alter table dbo.PARTNER_BRANCHES
	add foreign key (username) references dbo.PARTNERS(username);
go

alter table dbo.PARTNER_BRANCHES
	add foreign key (CID, extension) references dbo.CONTRACTS(CID, extension)
go

-- CONTRACTS
alter table dbo.CONTRACTS
	add foreign key (username) references dbo.PARTNERS(username)
go

-- PRODUCTS
alter table dbo.PRODUCTS
	add foreign key (username) references dbo.PARTNERS(username)
go

alter table dbo.PRODUCTS
	add foreign key (product_type_id) references dbo.PRODUCT_TYPES(product_type_id)
go

-- PRODUCT_IN_BRANCHES
alter table dbo.PRODUCT_IN_BRANCHES
	add foreign key (PID) references dbo.PRODUCTS(PID)
go
alter table dbo.PRODUCT_IN_BRANCHES
	add foreign key (PBID) references dbo.PARTNER_BRANCHES(PBID)
go

-- ORDERS
alter table dbo.ORDERS
	add foreign key (customer_username) references dbo.CUSTOMERS(username)
go
alter table dbo.ORDERS
	add foreign key (partner_username) references dbo.PARTNERS(username)
go

-- ORDER_DETAILS
alter table dbo.ORDERS_DETAILS
	add foreign key (order_id) references dbo.ORDERS(order_id)
go

alter table dbo.ORDERS_DETAILS
	add foreign key (PID) references dbo.PRODUCTS(PID)
go


-- CUSTOMERS
alter table dbo.CUSTOMERS
	add foreign key (username) references dbo.LOGIN_INFOS(username)
go

-- DRIVERS
alter table dbo.DRIVERS
	add foreign key (username) references dbo.LOGIN_INFOS(username)
go

alter table dbo.DRIVERS
	add foreign key (BID) references dbo.BANKS(BID);
go

-- EMPLOYEES
alter table dbo.EMPLOYEES
	add foreign key (username) references dbo.LOGIN_INFOS(username)
go

-- ADMINS
alter table dbo.ADMINS
	add foreign key (username) references dbo.LOGIN_INFOS(username)
go

-- DRIVER_REGISTRATIONS
alter table dbo.DRIVER_REGISTRATIONS
	add foreign key (driver_username) references dbo.DRIVERS(username);
go

-- DRIVER_HISTORIES
alter table dbo.DRIVER_HISTORIES
	add foreign key (driver_username) references dbo.DRIVERS(username);
go

alter table dbo.DRIVER_HISTORIES
	add foreign key (order_id) references dbo.ORDERS(order_id);
go

/* ___________ ADD TABLE CHECK ___________ */
alter table dbo.LOGIN_INFOS
	add check (RID = 'PARTNER' or RID = 'ADMIN' OR RID = 'EMPLOYEE' OR RID = 'CUSTOMER' OR RID = 'DRIVER')
go

alter table dbo.LOGIN_INFOS
	add check (status = 'PENDING' or status = 'ACTIVE' or status = 'INACTIVE')
go

alter table dbo.PARTNER_REGISTRATIONS
	add check (status = 'PENDING' or status = 'ACCEPTED' or status = 'NOTACCEPTED')
go

alter table dbo.DRIVER_REGISTRATIONS
	add check (registration_status = 'PENDING' or registration_status = 'ACCEPTED' or registration_status = 'NOTACCEPTED')
go

alter table dbo.DRIVER_REGISTRATIONS
	add check (paid_fee_status = 'PENDING' or paid_fee_status = 'ACCEPTED' or paid_fee_status = 'NOTACCEPTED')
go

alter table dbo.ORDERS
	add check (delivery_status = 'PENDING' or delivery_status = 'DELIVERING' or delivery_status = 'DELIVERIED')
go

alter table dbo.ORDERS
	add constraint CHK_table_orders_check_paid_status check (paid_status = 'PAID' or paid_status = 'NOTPAID')
go

alter table dbo.ORDERS
	add check (price > 0)
go

alter table dbo.PRODUCTS
	add check (price > 0)


alter table dbo.CONTRACTS
	add check (commission >= 0 and commission <= 1)
go

---- phone validation
--alter table dbo.PARTNERS
--	add constraint CHK_phone_validation_check check (phone like '0(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}')
--go

-- FIRST INIT WITH 2 PRODUCT_TYPES
insert into dbo.PRODUCT_TYPES
values
	('PTYPE001', 'FOOD'),
	('PTYPE002', 'DRINK');