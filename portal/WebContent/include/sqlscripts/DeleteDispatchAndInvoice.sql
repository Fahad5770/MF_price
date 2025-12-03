
select * from inventory_sales_invoices where id in (4778314,4778277);
select * from inventory_sales_invoices_products where id in (4778314,4778277);
select * from inventory_sales_adjusted where id in (4778314,4778277);
select * from inventory_sales_adjusted_products where id in (4778314,4778277);
select * from inventory_sales_dispatch where id = 138548;
select * from inventory_sales_dispatch_invoices where id = 138548;
select * from inventory_distributor_stock where document_id = 138548 and document_type_id = 13;
select * from inventory_distributor_stock_products where id = 531800;


select * from common_documents;
select * from users where id = 3479;