-- creat 6 role
CREATE LOGIN _app WITH PASSWORD = '1', CHECK_POLICY = OFF;
CREATE LOGIN _admin WITH PASSWORD = '1', CHECK_POLICY = OFF;
CREATE LOGIN _partner WITH PASSWORD = '1', CHECK_POLICY = OFF;
CREATE LOGIN _customer WITH PASSWORD = '1', CHECK_POLICY = OFF;
CREATE LOGIN _employee WITH PASSWORD = '1', CHECK_POLICY = OFF;
CREATE LOGIN _driver WITH PASSWORD = '1', CHECK_POLICY = OFF;
GO

CREATE user _app from login _app;
CREATE user _admin from login _admin;
CREATE user _partner from login _partner;
CREATE user _customer from login _customer;
CREATE user _employee from login _employee;
CREATE user _driver from login _driver;
go

-- GRANT ACCESS TO _app
grant exec on dbo.usp_app_log_in to _app


-- GRANT ACCESS TO _partner
grant exec on dbo.usp_partner_registation to _partner
grant exec on dbo.usp_partner_register_contract to _partner
grant exec on dbo.usp_partner_add_product to _partner
grant exec on dbo.usp_partner_add_brach to _partner
grant exec on dbo.usp_partner_update_product to _partner
grant exec on dbo.usp_partner_delete_product to _partner
grant exec on dbo.usp_partner_add_product_to_branch to _partner
grant exec on dbo.usp_partner_delete_product_from_branch to _partner
grant exec on dbo.usp_partner_get_orders to _partner

-- GRANT ACCESS TO _customer
grant exec on dbo.usp_customer_registration to _customer
grant exec on dbo.usp_customer_create_order to _customer
grant exec on dbo.usp_customer_add_product_to_order to _customer
grant exec on dbo.usp_customer_pay_order to _customer

-- GRANT ACCESS TO _driver
grant exec on dbo.usp_driver_registration to _driver
grant exec on dbo.usp_driver_get_orders to _driver
grant exec on dbo.usp_driver_receive_order to _driver
grant exec on dbo.usp_driver_history_incomes to _driver

-- GRANT ACCESS TO _employee
grant exec on dbo.usp_employee_get_contracts to _employee
grant exec on dbo.usp_employee_accept_contract to _employee

-- GRANT ACCESS TO _admin
grant exec on dbo.usp_admin_update_login_info to _admin
grant exec on dbo.usp_admin_add_admin_account to _admin
grant exec on dbo.usp_admin_add_employee_account to _admin
