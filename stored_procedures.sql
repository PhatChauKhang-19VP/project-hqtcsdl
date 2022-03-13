use master
use db_app
go

/* ___________________ APP USP ___________________ */
create proc dbo.usp_app_log_in
	@username varchar(50),
	@password varchar(512),
	@RID varchar(10) output
as
begin
	if exists (select RID as _output from LOGIN_INFOS where username=@username and password=@password)
		select @RID = (select RID as _output from LOGIN_INFOS where username=@username and password=@password)
	else
		select @RID = 'LOGIN_FAILED'
end
go

/* ___________________ ĐỐI TÁC USP ___________________ */

-- đối tác đăng kí thông tin
create proc dbo.usp_partner_registation
	@username varchar(50),
	@password varchar(512),
	@name nvarchar(50),
	@representative_name nvarchar(50),
	@province nvarchar(20),
	@district nvarchar(20),
	@brach_number int,
	@order_number int,
	@product_type_id varchar(10),
	@address_line nvarchar(100),
	@phone char(10),
	@mail varchar(50)
as begin
	-- add to PARTNERS table
	insert into dbo.LOGIN_INFOS
		values (@username, @password, 'PARTNER', 'PENDING')

	insert into dbo.PARTNERS
		values 
			(	@username,
				@name,
				@representative_name,
				@province,
				@district,
				@brach_number,
				@order_number,
				@product_type_id,
				@address_line,
				@phone,
				@mail
			)
	-- INSERT TO PARTNER_REGISTRATIONS
	insert into dbo.PARTNER_REGISTRATIONS(username, register_time, status)
		values 
			(@username, GETDATE(), 'PENDING');
end
go

-- đối tác lập hợp đồng
create proc dbo.usp_partner_register_contract
	@CID varchar(10),
	@extension int,
	@username varchar(50),
	@TIN varchar(20),
	@representative_name nvarchar(50),
	@brach_number int,
	@contract_time int,
	@commission float
as
begin
	insert into dbo.CONTRACTS(CID, extension, username, TIN, representative_name, brach_number, created_at, expired_at, commission, status)
	values
		(@CID, @extension, @username ,@TIN, @representative_name, @brach_number, GETDATE(), DATEADD(MONTH, @contract_time, GETDATE()), @commission, 'PENDING')
end
go

-- đối tác thêm chi nhánh
create proc dbo.usp_partner_add_brach
	@PBID varchar(10),
	@username varchar(50),
	@name nvarchar(50),
	@province nvarchar(20),
	@district nvarchar(20),
	@address_line nvarchar(100),
	@CID varchar(10),
	@extension int
as
begin
	if (select count(*) from dbo.PARTNER_BRANCHES where CID=@CID and extension=@extension ) < (select dbo.CONTRACTS.brach_number from dbo.CONTRACTS where CID=@CID and extension=@extension)
	begin
	insert into dbo.PARTNER_BRANCHES
		values
			(@PBID, @username, @name, @province, @district, @address_line, @CID, @extension)
	end

	else
	THROW 52000, --Error number must be between 50000 and  2147483647.
        'Cannot add more branch. Out of room', --Message
        1; --State
end
go

-- đối tác thêm sản phẩm
create proc dbo.usp_partner_add_product
	@PID varchar(10),
	@product_type_id varchar(10),
	@username varchar(50),
	@img_src nvarchar(200),
	@name nvarchar(50),
	@description nvarchar(200),
	@price float
as
begin
	insert into dbo.PRODUCTS(PID, product_type_id, username, img_src, name, description, price)
		values
			(@PID, @product_type_id, @username, @img_src, @name, @description, @price)
end
go

-- TODO: đối tác sửa thông tin sản phẩm
create proc dbo.usp_partner_update_product

as begin
print 'sua thong tin sp'
end
go

-- TODO: đối tác xóa sản phẩm
create proc dbo.usp_partner_delete_product

as begin
print 'xoa sp'
end
go
-- đối tác thêm sản phẩm vào chi nhánh phân phối
create proc dbo.usp_partner_add_product_to_branch
	@PBID varchar(10),
	@PID varchar(10)
as begin
	insert into dbo.PRODUCT_IN_BRANCHES (PBID,PID)
		values
			(@PBID, @PID)
end
go

-- TODO: đối tác xóa sản phẩm khỏi chi nhánh phân phối
create proc dbo.usp_partner_delete_product_from_branch

as begin
print 'sua thong tin sp'
end
go

-- đối tác xem đơn hàng
create proc dbo.usp_partner_get_orders
	@partner_username varchar(10)
as begin
	select * from dbo.ORDERS where partner_username=@partner_username
end
go

-- TODO: đối tác cập nhật tình trạng đơn hàng

/* ___________________ KHÁCH HÀNG USP ___________________ */

-- Đăng kí thành viên
create proc dbo.usp_customer_registration
	@username varchar(50),
	@password varchar(512),
	@name nvarchar(50),
	@province nvarchar(20),
	@district nvarchar(20),
	@address_line nvarchar(100),
	@phone char(10),
	@mail varchar(50)
as begin
	insert into dbo.LOGIN_INFOS
		values (@username, @password, 'CUSTOMER', 'ACTIVE')
	insert into dbo.CUSTOMERS(username, name, province, district, address_line, phone,mail)
	values 
		(	@username,
			@name,
			@province,
			@district,
			@address_line,
			@phone,
			@mail
		)
end
go

-- KHÁCH HÀNG ĐẶT HÀNG

-- Khách hàng tạo đơn hàng
create proc dbo.usp_customer_create_order
	@order_id varchar(10),
	@customer_username varchar(50),
	@partner_username varchar(50),
	@payment_method varchar(10), -- CASH, MOMO, ZALOPAY, CREDIT, QRPAY,
	@delivery_province nvarchar(20),
	@delivery_district nvarchar(20),
	@delivery_adsress_line nvarchar(50)
as 
begin
	insert into dbo.ORDERS(order_id, customer_username, partner_username, payment_method, delivery_province,delivery_district, delivery_adsress_line, delivery_status, paid_status)
		values
			(@order_id, @customer_username, @partner_username,@payment_method,@delivery_province,@delivery_district,@delivery_adsress_line, 'PENDING', 'NOTPAID')
end
go

-- Thêm sản phẩm vào đơn hàng
create proc dbo.usp_customer_add_product_to_order
	@order_id varchar(10),
	@PID varchar(10),
	@quantity int
as 
begin
	insert into dbo.ORDERS_DETAILS(order_id, PID, quantity)
		values
			(@order_id, @PID, @quantity)
end
go

-- khách hàng thanh toán đơn hàng
create proc dbo.usp_customer_pay_order
	@order_id varchar(10)
as begin
	if (select paid_status from ORDERS where order_id=@order_id) = 'NOTPAID'
		begin
			update ORDERS
				set paid_status='PAID'
		end
	else
		THROW 52000, --Error number must be between 50000 and  2147483647.
        'You have already paid this order', --Message
        1; --State
end
go

-- Đối tác/Tài xế cập nhật tình trạng đơn hàng
create proc usp_update_order_status
	@order_id varchar(10),
	@new_delivery_status varchar(20),
	@new_paid_status varchar(20)
as 
begin
	update ORDERS
		set delivery_status = @new_delivery_status,
			paid_status = @new_paid_status
		where order_id = @order_id
end
go
	

/* ___________________ TÀI XẾ USP ___________________ */

-- Tài xế đăng kí tài khoảng
create proc dbo.usp_driver_registration
	@username varchar(50),
	@password varchar(512),
	@name nvarchar(50),
	@NIN char(12),
	@province nvarchar(20),
	@district nvarchar(20),
	@address_line nvarchar(100),
	@area nvarchar(50),
	@mail varchar(50),
	@BID varchar(20),
	@bank_name nvarchar(50),
	@bank_branch nvarchar(50)
as begin
	insert into dbo.BANKS(BID, name, branch)
		values
			(@BID, @bank_name, @bank_branch)

	insert into dbo.LOGIN_INFOS
		values (@username, @password, 'DRIVER', 'INACTIVE')

	insert into dbo.DRIVERS(username, name, NIN, province, district, address_line, area, mail, BID)
	values 
		(	@username,
			@name,
			@NIN,
			@province,
			@district,
			@address_line,
			@area,
			@mail,
			@BID
		)
end
go

-- Tài xế ~ đơn hàng trong khu vực đăng kí
create proc dbo.usp_driver_get_orders
	@username varchar(50)
as
begin
	select o.* from dbo.ORDERS as o
		join dbo.DRIVERS as d on o.delivery_district = d.area
			where o.delivery_status != 'DELIVERIED'
end
go

-- Tài xế tiếp nhận đơn hàng
create proc dbo.usp_driver_receive_order
	@username varchar(50),
	@order_id varchar(10)
as 
begin
	-- update order delivery_status to DELIVERING
	begin
		update dbo.ORDERS
			set delivery_status = 'DELIVERING'
			where order_id=@order_id
	end

	-- insert new record to driver history
	begin
		insert into dbo.DRIVER_HISTORIES
			values
				(@order_id, @username, 20000)
	end
end
go

-- Tài xế theo dõi thu nhập
create proc dbo.usp_driver_history_incomes
	@username varchar(50)
as 
begin
	select dh.order_id, dh.income, o.delivery_province, o.delivery_district,o.delivery_adsress_line
		from dbo.DRIVER_HISTORIES as dh 
			join dbo.ORDERS as o on dh.order_id = o.order_id 
		where dh.driver_username = @username
end
go

/* ___________________ NHÂN VIÊN USP ___________________ */

-- Nhân viên xem danh sách hợp đồng của đối tác
create proc dbo.usp_employee_get_contracts
	@partner_username varchar(50),
	@contract_status varchar(20)
as
begin
	if @contract_status!='*'
		begin
			select * from dbo.CONTRACTS as c
				where c.username = @partner_username and c.status=@contract_status
		end
	else
		begin
			select * from dbo.CONTRACTS as c
				where c.username = @partner_username
				order by c.status desc
		end
end
go

-- Nhân viên duyệt hợp đồng
create proc dbo.usp_employee_accept_contract
	@CID varchar(10)
as
begin
	update dbo.CONTRACTS
		set dbo.CONTRACTS.status = 'ACCEPTED'
		where CID=@CID
end
go

/* ___________________ QUẢN TRỊ USP ___________________ */

/* ----- quản trị người dùng ----- */

-- Admin cập nhật thông tin tài khoản <- kích hoạt/ khóa tài khoảng
create proc dbo.usp_admin_update_login_info
--	@username varchar(50)
--	@new_status
as
begin
	print 'cap nhat thong tin nguoi dung'
end
go

-- Admin thêm tài khoản Admin
create proc dbo.usp_admin_add_admin_account
	@username varchar(50),
	@password varchar(512),
	@name nvarchar(50)
as
begin
	insert into dbo.LOGIN_INFOS 
		values (@username, @password, 'ADMIN', 'ACTIVE')
	insert into dbo.ADMINS
		values (@username, @name)
end
go

-- Admin thêm tài khoản nhân viên
create proc dbo.usp_admin_add_employee_account
	@username varchar(50),
	@password varchar(512),
	@name nvarchar(50),
	@mail varchar(50)
as
begin
	insert into dbo.LOGIN_INFOS 
		values (@username, @password, 'EMPLOYEE', 'ACTIVE')
	insert into dbo.EMPLOYEES
		values (@username, @name, @mail)
end

/* ----- quản trị quyền người dùng ----- */

-- TODO: admin cấp quyền thao tác trên dữ liệu