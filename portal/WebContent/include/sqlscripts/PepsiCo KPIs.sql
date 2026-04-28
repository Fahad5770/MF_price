/* Sales Converted Cases */
select sum(total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2014-11-01' and '2014-12-01';
select sum(total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2015-11-01' and '2015-12-01';

/* Count of Outlets */
select count(distinct outlet_id) from distributor_beat_plan_log where log_date between '2014-11-01' and '2014-11-30';
select count(distinct outlet_id) from distributor_beat_plan_log where log_date between '2015-11-01' and '2015-11-30';

/* Total Lines Sold */
select count(isap.id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2014-11-01' and '2014-12-01';
select count(isap.id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2015-11-01' and '2015-12-01';

/* No of Invoices */
select count(isa.id) from inventory_sales_adjusted isa where created_on between '2014-11-01' and '2014-12-01' and net_amount != 0;
select count(isa.id) from inventory_sales_adjusted isa where created_on between '2015-11-01' and '2015-12-01' and net_amount != 0;

/* Count of Orders */
select count(distinct outlet_id) from mobile_order where created_on between '2014-11-01' and '2014-12-01';
select count(distinct outlet_id) from mobile_order where created_on between '2015-11-01' and '2015-12-01';

/* Raw Cases */
select sum(isap.raw_cases) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2014-11-01' and '2014-12-01';
select sum(isap.raw_cases) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2015-11-01' and '2015-12-01';

