use db_app
go

exec as user = '_app'

-- test đăng nhập
declare @_rid varchar(10);
exec dbo.usp_app_log_in 
	@username = 'phatnm.partner1', 
	@password = 'pn12345', 
	@RID = @_rid output
select @_rid as 'RID'
go

revert
go

EXECUTE AS USER = '_partner';
-- test đối tác đăng kí thông tin
exec dbo.usp_partner_registation
	@username = 'phatnm.test',
	@password = 'pn12345',
	@name = N'Phát Partner1',
	@representative_name = N'Ngô Minh Phát1',
	@province = N'Đồng Tháp',
	@district = N'Lai Vung',
	@brach_number = 2,
	@order_number = 100,
	@product_type_id = 'PTYPE001',
	@address_line = N'319A, ấp Tân Lộc A',
	@phone = '07aa',
	@mail = 'nmphat.partner3@mail.com';
go

-- test đối tác lập hợp đồng
exec dbo.usp_partner_register_contract
	@CID = 'CID001',
	@extension = 1,
	@username = 'phatnm.partner1',
	@TIN = '01234567890123456789',
	@representative_name = N'Phát học sinh giỏi',
	@brach_number = 2,
	@contract_time = 6,
	@commission = 0.6
go

-- test đối tác thêm chi nhánh
exec dbo.usp_partner_add_brach
	@PBID = 'PBID001',
	@username = 'phatnm.partner1',
	@name = 'Chi nhanh 1',
	@province = N'Đồng Tháp',
	@district = N'Sá đéc',
	@address_line = N'123 trần phú',
	@CID = 'CID001',
	@extension = 1
go

exec dbo.usp_partner_add_brach
	@PBID = 'PBID002',
	@username = 'phatnm.partner1',
	@name = 'Chi nhanh 1',
	@province = N'Đồng Tháp',
	@district = N'Sá đéc',
	@address_line = N'123 trần phú',
	@CID = 'CID001',
	@extension = 1
go

exec dbo.usp_partner_add_brach
	@PBID = 'PBID003',
	@username = 'phatnm.partner1',
	@name = 'Chi nhanh 1',
	@province = N'Đồng Tháp',
	@district = N'Sá đéc',
	@address_line = N'123 trần phú',
	@CID = 'CID001',
	@extension = 1
go

-- test đối tác thêm sản phẩm
exec dbo.usp_partner_add_product 'PID001', 'PTYPE001', 'phatnm.partner1', 'img_src', N'Bánh oishi', N'Bánh oishi cay nồng', 5
exec dbo.usp_partner_add_product 'PID003', 'PTYPE001', 'phatnm.partner1', 'img_src', N'Bánh bông lan', N'Bánh bông lan nhân trứng múi', 5
GO

-- test đối tác thêm sản phẩm vào chi nhánh phân phối
exec dbo.usp_partner_add_product_to_branch
	@PID = 'PID001',
	@PBID = 'PBID001'
GO

revert
go

exec as user = '_customer'
-- test khách hàng đăng kí
exec dbo.usp_customer_registration
	@username = 'phatnm.customer2',
	@password = 'cus12345',
	@name = N'Phát Partner1',
	@province = N'Đồng Tháp',
	@district = N'Lai Vung',
	@address_line = N'319A, ấp Tân Lộc A',
	@phone = '0704921215',
	@mail = 'nmphat.partner3@mail.com';
go

-- test khách hàng tạo đơn hàng
exec dbo.usp_customer_create_order
	@order_id = 'OID001',
	@customer_username = 'phatnm.customer2',
	@partner_username = 'phatnm.partner1',
	@payment_method = 'CASH', -- CASH, MOMO, ZALOPAY, CREDIT, QRPAY,
	@delivery_province = N'TP. HCM',
	@delivery_district = N'Quận 7',
	@delivery_adsress_line = N'42 đường số 1'
go

revert
go

exec as user = '_driver'
-- Test tài xế đăng kí tài khoản
exec dbo.usp_driver_registration
	@username = 'phatnm.driver1',
	@password = 'dri12345',
	@name = N'Phát driver1',
	@NIN = '0000000000001',
	@province = N'Đồng Tháp',
	@district = N'Lai Vung',
	@address_line = N'319A, ấp Tân Lộc A',
	@area = N'Quận 7',
	@mail = 'nmphat.partner3@mail.com',
	@BID = 'BID001',
	@bank_name = N'Ngân hàng agribank',
	@bank_branch = N'Quận 7'
go

exec dbo.usp_driver_registration
	@username = 'phatnm.driver2',
	@password = 'dri12345',
	@name = N'Phát driver1',
	@NIN = '0000000000002',
	@province = N'Đồng Tháp',
	@district = N'Lai Vung',
	@address_line = N'319A, ấp Tân Lộc A',
	@area = N'Quận 7',
	@mail = 'nmphat.partner3@mail.com',
	@BID = 'BID002',
	@bank_name = N'Ngân hàng agribank',
	@bank_branch = N'Quận 7'
go

-- Test Tài xế ~ đơn hàng trong khu vực đăng kí
exec dbo.usp_driver_get_orders 'phatnm.driver2'

-- Test Tài xế tiếp nhận đơn hàng
exec dbo.usp_driver_receive_order 'phatnm.driver2', 'OID001'

-- Test Tài xế theo dõi thu nhập
exec dbo.usp_driver_history_incomes 'phatnm.driver2'

revert
go

exec as user = '_employee'
-- Test Nhân viên duyệt hợp đồng
exec dbo.usp_employee_accept_contract 'CID001'

revert
go