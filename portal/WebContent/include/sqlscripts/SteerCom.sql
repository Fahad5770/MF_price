/* Orders Converted Cases*/
select sum(total_units*ipv.liquid_in_ml)/6000 from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where mo.created_on between '2015-08-01' and '2015-09-01';
select sum(total_units*ipv.liquid_in_ml)/6000 from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where mo.created_on between '2015-09-01' and '2015-10-01';

select sum(total_units*ipv.liquid_in_ml)/6000 from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where mo.created_on between '2014-10-01' and '2014-11-01' and mo.distributor_id in (select distributor_id from common_distributors where region_id = 5);
select sum(total_units*ipv.liquid_in_ml)/6000 from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where mo.created_on between '2014-11-01' and '2014-12-01' and mo.distributor_id in (select distributor_id from common_distributors where region_id = 5);
select sum(total_units*ipv.liquid_in_ml)/6000 from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where mo.created_on between '2014-12-01' and '2015-01-01' and mo.distributor_id in (select distributor_id from common_distributors where region_id = 5);

/* count of order ids*/
select count(id) from mobile_order where created_on between '2015-08-01' and '2015-09-01';
select count(id) from mobile_order where created_on between '2015-09-01' and '2015-10-01';

select count(id) from mobile_order where created_on between '2014-10-01' and '2014-11-01' and distributor_id in (select distributor_id from common_distributors where region_id = 5);
select count(id) from mobile_order where created_on between '2014-11-01' and '2014-12-01' and distributor_id in (select distributor_id from common_distributors where region_id = 5);
select count(id) from mobile_order where created_on between '2014-12-01' and '2015-01-01' and distributor_id in (select distributor_id from common_distributors where region_id = 5);


/* Sales Converted Cases */
select sum(total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2014-11-01' and '2014-12-01';
select sum(total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2015-11-01' and '2015-12-01';

/* Count of Outlets and Distributors */
select count(distinct outlet_id) from distributor_beat_plan_log where log_date between '2015-12-01' and '2015-12-31' and distributor_id in (select distributor_id from inventory_sales_adjusted where created_on between '2015-12-01' and '2016-01-01');
select count(distinct distributor_id) from distributor_beat_plan_log where log_date between '2015-12-01' and '2015-12-31' and distributor_id in (select distributor_id from inventory_sales_adjusted where created_on between '2015-12-01' and '2016-01-01');
select count(distinct outlet_id) from distributor_beat_plan_log where log_date between '2015-11-01' and '2015-11-30';

/* Total Lines Sold */
select count(isap.id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2015-08-01' and '2015-09-01';
select count(isap.id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2015-09-01' and '2015-10-01';
select count(isap.id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2015-10-01' and '2015-11-01';
select count(isap.id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2015-11-01' and '2015-12-01';
select count(isap.id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2015-12-01' and '2016-01-01';

/* No of Invoices */
select count(isa.id) from inventory_sales_adjusted isa where created_on between '2015-08-01' and '2015-09-01' and net_amount != 0;
select count(isa.id) from inventory_sales_adjusted isa where created_on between '2015-09-01' and '2015-10-01' and net_amount != 0;
select count(isa.id) from inventory_sales_adjusted isa where created_on between '2015-10-01' and '2015-11-01' and net_amount != 0;
select count(isa.id) from inventory_sales_adjusted isa where created_on between '2015-11-01' and '2015-12-01' and net_amount != 0;
select count(isa.id) from inventory_sales_adjusted isa where created_on between '2015-12-01' and '2016-01-01' and net_amount != 0;

/* Count of Orders */
select count(distinct outlet_id) from mobile_order where created_on between '2015-08-01' and '2015-09-01';
select count(distinct outlet_id) from mobile_order where created_on between '2015-09-01' and '2015-10-01';
select count(distinct outlet_id) from mobile_order where created_on between '2015-10-01' and '2015-11-01';
select count(distinct outlet_id) from mobile_order where created_on between '2015-11-01' and '2015-12-01';
select count(distinct outlet_id) from mobile_order where created_on between '2015-12-01' and '2016-01-01';

/* Raw Cases */
select sum(isap.raw_cases) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2014-11-01' and '2014-12-01';
select sum(isap.raw_cases) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2015-11-01' and '2015-12-01';
















/* Count of Outlets */
select count(distinct outlet_id) from distributor_beat_plan_log where log_date between '2014-10-01' and '2014-12-31' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769);
select count(distinct outlet_id) from distributor_beat_plan_log where log_date between '2015-01-01' and '2015-12-31' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769);

select count(distinct outlet_id) from mobile_order where created_on between '2015-10-01' and '2016-02-01' and outlet_id in (
	select distinct outlet_id from distributor_beat_plan_log where log_date = '2016-01-31' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769)
);

select count(distinct created_by) from mobile_order where created_on between '2016-01-25' and '2016-02-01' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769);

/* Count of Orders */
select count(distinct outlet_id) from mobile_order where created_on between '2014-10-01' and '2015-01-01' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769);
select count(distinct outlet_id) from mobile_order where created_on between '2015-01-01' and '2016-01-01' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769);

/* Total Lines Sold */
select count(isap.id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2014-10-01' and '2015-01-01' and isa.distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769);
select count(isap.id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2015-01-01' and '2016-01-01' and isa.distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769);
select count(isap.id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2015-10-01' and '2016-01-01' and isa.distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769);
select count(isap.id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2016-01-01' and '2016-02-01' and isa.distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769);
select count(isap.id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2015-10-01' and '2016-01-01' and isa.distributor_id in (100914);
select count(isap.id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2016-01-01' and '2016-02-01' and isa.distributor_id in (100914);

/* No of Invoices */
select count(isa.id) from inventory_sales_adjusted isa where created_on between '2014-10-01' and '2015-01-01' and net_amount != 0 and isa.distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769);
select count(isa.id) from inventory_sales_adjusted isa where created_on between '2015-01-01' and '2016-01-01' and net_amount != 0 and isa.distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769);
select count(isa.id) from inventory_sales_adjusted isa where created_on between '2015-10-01' and '2016-01-01' and net_amount != 0 and isa.distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769);
select count(isa.id) from inventory_sales_adjusted isa where created_on between '2016-01-01' and '2016-02-01' and net_amount != 0 and isa.distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769);
select count(isa.id) from inventory_sales_adjusted isa where created_on between '2015-10-01' and '2016-01-01' and net_amount != 0 and isa.distributor_id in (100914);
select count(isa.id) from inventory_sales_adjusted isa where created_on between '2016-01-01' and '2016-02-01' and net_amount != 0 and isa.distributor_id in (100914);


/* Raw Cases */
select sum(isap.raw_cases) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2014-10-01' and '2015-01-01' and isa.distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769);
select sum(isap.raw_cases) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2015-01-01' and '2016-01-01' and isa.distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769);
select sum(isap.raw_cases) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2015-10-01' and '2016-01-01' and isa.distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769);
select sum(isap.raw_cases) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2016-01-01' and '2016-02-01' and isa.distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769);

select sum(isap.raw_cases) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2015-10-01' and '2016-01-01' and isa.distributor_id in (100914);
select sum(isap.raw_cases) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2016-01-01' and '2016-02-01' and isa.distributor_id in (100914);


/* Time In */
SELECT sec_to_time(avg(time_to_sec(time_format(first_order_time,'%H:%i')))) avg_time_in from (
	SELECT min(mobile_timestamp) first_order_time FROM mobile_order where mobile_timestamp between '2014-10-01' and '2015-01-01' and created_on between '2014-10-01' and '2015-01-01' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769) group by date(mobile_timestamp)
) tbl1;
SELECT sec_to_time(avg(time_to_sec(time_format(first_order_time,'%H:%i')))) avg_time_in from (
	SELECT min(mobile_timestamp) first_order_time FROM mobile_order where mobile_timestamp between '2015-01-01' and '2016-01-01' and created_on between '2015-01-01' and '2016-01-01' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769) group by date(mobile_timestamp)
) tbl1;
SELECT sec_to_time(avg(time_to_sec(time_format(first_order_time,'%H:%i')))) avg_time_in from (
	SELECT min(mobile_timestamp) first_order_time FROM mobile_order where mobile_timestamp between '2015-10-01' and '2016-01-01' and created_on between '2015-10-01' and '2016-01-01' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769) group by date(mobile_timestamp)
) tbl1;
SELECT sec_to_time(avg(time_to_sec(time_format(first_order_time,'%H:%i')))) avg_time_in from (
	SELECT min(mobile_timestamp) first_order_time FROM mobile_order where mobile_timestamp between '2016-01-01' and '2016-02-01' and created_on between '2016-01-01' and '2016-02-01' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769) group by date(mobile_timestamp)
) tbl1;

SELECT sec_to_time(avg(time_to_sec(time_format(first_order_time,'%H:%i')))) avg_time_in from (
	SELECT min(mobile_timestamp) first_order_time FROM mobile_order where mobile_timestamp between '2015-10-01' and '2016-01-01' and created_on between '2015-10-01' and '2016-01-01' and distributor_id in (100914) group by date(mobile_timestamp)
) tbl1;
SELECT sec_to_time(avg(time_to_sec(time_format(first_order_time,'%H:%i')))) avg_time_in from (
	SELECT min(mobile_timestamp) first_order_time FROM mobile_order where mobile_timestamp between '2016-01-01' and '2016-02-01' and created_on between '2016-01-01' and '2016-02-01' and distributor_id in (100914) group by date(mobile_timestamp)
) tbl1;


/* Time Out */
SELECT sec_to_time(avg(time_to_sec(time_format(last_order_time,'%H:%i')))) avg_time_out from (
	SELECT max(mobile_timestamp) last_order_time FROM mobile_order where mobile_timestamp between '2014-10-01' and '2015-01-01' and created_on between '2014-10-01' and '2015-01-01' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769) group by date(mobile_timestamp)
) tbl2;

SELECT sec_to_time(avg(time_to_sec(time_format(last_order_time,'%H:%i')))) avg_time_out from (
	SELECT max(mobile_timestamp) last_order_time FROM mobile_order where mobile_timestamp between '2015-01-01' and '2016-01-01' and created_on between '2015-01-01' and '2016-01-01' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769) group by date(mobile_timestamp)
) tbl2;

SELECT sec_to_time(avg(time_to_sec(time_format(last_order_time,'%H:%i')))) avg_time_out from (
	SELECT max(mobile_timestamp) last_order_time FROM mobile_order where mobile_timestamp between '2015-10-01' and '2016-01-01' and created_on between '2015-10-01' and '2016-01-01' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769) group by date(mobile_timestamp)
) tbl2;

SELECT sec_to_time(avg(time_to_sec(time_format(last_order_time,'%H:%i')))) avg_time_out from (
	SELECT max(mobile_timestamp) last_order_time FROM mobile_order where mobile_timestamp between '2016-01-01' and '2016-02-01' and created_on between '2016-01-01' and '2016-02-01' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769) group by date(mobile_timestamp)
) tbl2;

SELECT sec_to_time(avg(time_to_sec(time_format(last_order_time,'%H:%i')))) avg_time_out from (
	SELECT max(mobile_timestamp) last_order_time FROM mobile_order where mobile_timestamp between '2015-10-01' and '2016-01-01' and created_on between '2015-10-01' and '2016-01-01' and distributor_id in (100914) group by date(mobile_timestamp)
) tbl2;

SELECT sec_to_time(avg(time_to_sec(time_format(last_order_time,'%H:%i')))) avg_time_out from (
	SELECT max(mobile_timestamp) last_order_time FROM mobile_order where mobile_timestamp between '2016-01-01' and '2016-02-01' and created_on between '2016-01-01' and '2016-02-01' and distributor_id in (100914) group by date(mobile_timestamp)
) tbl2;


/* Adjusted / Cancelled Orders */
SELECT count(distinct invoice_id) FROM inventory_sales_dispatch_adjusted_products where invoice_id in (
	select id from inventory_sales_adjusted where order_id in (select id from mobile_order where created_on between '2014-10-01' and '2015-01-01' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769))
);
select count(id) from mobile_order where created_on between '2014-10-01' and '2015-01-01' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769);

SELECT count(distinct invoice_id) FROM inventory_sales_dispatch_adjusted_products where invoice_id in (
	select id from inventory_sales_adjusted where order_id in (select id from mobile_order where created_on between '2015-01-01' and '2016-01-01' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769))
);
select count(id) from mobile_order where created_on between '2015-01-01' and '2016-01-01' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769);

SELECT count(distinct invoice_id) FROM inventory_sales_dispatch_adjusted_products where invoice_id in (
	select id from inventory_sales_adjusted where order_id in (select id from mobile_order where created_on between '2015-10-01' and '2016-01-01' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769))
);
select count(id) from mobile_order where created_on between '2015-10-01' and '2016-01-01' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769);

SELECT count(distinct invoice_id) FROM inventory_sales_dispatch_adjusted_products where invoice_id in (
	select id from inventory_sales_adjusted where order_id in (select id from mobile_order where created_on between '2016-01-01' and '2016-02-01' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769))
);
select count(id) from mobile_order where created_on between '2016-01-01' and '2016-02-01' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769);

SELECT count(distinct invoice_id) FROM inventory_sales_dispatch_adjusted_products where invoice_id in (
	select id from inventory_sales_adjusted where order_id in (select id from mobile_order where created_on between '2015-10-01' and '2016-01-01' and distributor_id in (100914))
);
select count(id) from mobile_order where created_on between '2015-10-01' and '2016-01-01' and distributor_id in (100914);

SELECT count(distinct invoice_id) FROM inventory_sales_dispatch_adjusted_products where invoice_id in (
	select id from inventory_sales_adjusted where order_id in (select id from mobile_order where created_on between '2016-01-01' and '2016-02-01' and distributor_id in (100914))
);
select count(id) from mobile_order where created_on between '2016-01-01' and '2016-02-01' and distributor_id in (100914);


/* Adjusted / Cancelled Orders Volume */
SELECT sum(liquid_in_ml)/6000 FROM inventory_sales_dispatch_adjusted_products where invoice_id in (
	select id from inventory_sales_adjusted where order_id in (select id from mobile_order where created_on between '2014-10-01' and '2015-01-01' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769))
);

SELECT sum(liquid_in_ml)/6000 FROM inventory_sales_dispatch_adjusted_products where invoice_id in (
	select id from inventory_sales_adjusted where order_id in (select id from mobile_order where created_on between '2015-01-01' and '2016-01-01' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769))
);

SELECT sum(liquid_in_ml)/6000 FROM inventory_sales_dispatch_adjusted_products where invoice_id in (
	select id from inventory_sales_adjusted where order_id in (select id from mobile_order where created_on between '2015-10-01' and '2016-01-01' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769))
);

SELECT sum(liquid_in_ml)/6000 FROM inventory_sales_dispatch_adjusted_products where invoice_id in (
	select id from inventory_sales_adjusted where order_id in (select id from mobile_order where created_on between '2016-01-01' and '2016-02-01' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769))
);

SELECT sum(liquid_in_ml)/6000 FROM inventory_sales_dispatch_adjusted_products where invoice_id in (
	select id from inventory_sales_adjusted where order_id in (select id from mobile_order where created_on between '2015-10-01' and '2016-01-01' and distributor_id in (100914))
);

SELECT sum(liquid_in_ml)/6000 FROM inventory_sales_dispatch_adjusted_products where invoice_id in (
	select id from inventory_sales_adjusted where order_id in (select id from mobile_order where created_on between '2016-01-01' and '2016-02-01' and distributor_id in (100914))
);


/* Unplanned Calls */
select mo.distributor_id, date(mo.created_on) cdate, count(mo.outlet_id) ct from mobile_order mo where created_on between '2014-10-01' and '2015-01-01' and mo.distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769)
and outlet_id not in (
select dbpl.outlet_id from distributor_beat_plan_log dbpl where dbpl.log_date = date(mo.created_on) and dbpl.distributor_id = mo.distributor_id and day_number = dayofweek(date(mo.created_on))
) group by mo.distributor_id, date(mo.created_on) having ct < 30;

select mo.distributor_id, date(mo.created_on) cdate, count(mo.outlet_id) ct from mobile_order mo where created_on between '2015-01-01' and '2016-01-01' and mo.distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769)
and outlet_id not in (
select dbpl.outlet_id from distributor_beat_plan_log dbpl where dbpl.log_date = date(mo.created_on) and dbpl.distributor_id = mo.distributor_id and day_number = dayofweek(date(mo.created_on))
) group by mo.distributor_id, date(mo.created_on) having ct < 30;

select mo.distributor_id, date(mo.created_on) cdate, count(mo.outlet_id) ct from mobile_order mo where created_on between '2015-10-01' and '2016-01-01' and mo.distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769)
and outlet_id not in (
select dbpl.outlet_id from distributor_beat_plan_log dbpl where dbpl.log_date = date(mo.created_on) and dbpl.distributor_id = mo.distributor_id and day_number = dayofweek(date(mo.created_on))
) group by mo.distributor_id, date(mo.created_on) having ct < 30;

select mo.distributor_id, date(mo.created_on) cdate, count(mo.outlet_id) ct from mobile_order mo where created_on between '2016-01-01' and '2016-02-01' and mo.distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769)
and outlet_id not in (
select dbpl.outlet_id from distributor_beat_plan_log dbpl where dbpl.log_date = date(mo.created_on) and dbpl.distributor_id = mo.distributor_id and day_number = dayofweek(date(mo.created_on))
) group by mo.distributor_id, date(mo.created_on) having ct < 30;

select mo.distributor_id, date(mo.created_on) cdate, count(mo.outlet_id) ct from mobile_order mo where created_on between '2015-10-01' and '2016-01-01' and mo.distributor_id in (100914)
and outlet_id not in (
select dbpl.outlet_id from distributor_beat_plan_log dbpl where dbpl.log_date = date(mo.created_on) and dbpl.distributor_id = mo.distributor_id and day_number = dayofweek(date(mo.created_on))
) group by mo.distributor_id, date(mo.created_on) having ct < 30;

select mo.distributor_id, date(mo.created_on) cdate, count(mo.outlet_id) ct from mobile_order mo where created_on between '2016-01-01' and '2016-02-01' and mo.distributor_id in (100914)
and outlet_id not in (
select dbpl.outlet_id from distributor_beat_plan_log dbpl where dbpl.log_date = date(mo.created_on) and dbpl.distributor_id = mo.distributor_id and day_number = dayofweek(date(mo.created_on))
) group by mo.distributor_id, date(mo.created_on) having ct < 30;



select count(id) from mobile_order where created_on between '2014-10-03' and '2014-10-04' and distributor_id = 100835 and outlet_id not in (
	select outlet_id from distributor_beat_plan_log where log_date = '2014-10-03' and distributor_id = 100835 and day_number = 6
);


select outlet_id, count(distinct day_number) ct from distributor_beat_plan_log where log_date = '2016-01-31' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769) group by outlet_id having ct not in (5,2,3);


/* Outlet List with frequency */

select outlet_id from (
select outlet_id, count(distinct day_number) ct from distributor_beat_plan_log where log_date = '2016-01-31' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769) group by outlet_id having ct in (2)
) tab1;


/* Unplanned Calls for specific outlets */
select mo.id, dayofweek(mo.created_on), (select outlet_id from distributor_beat_plan_log where log_date = '2016-01-31' and outlet_id = mo.outlet_id and day_number = dayofweek(mo.created_on) limit 1) planned from mobile_order mo where mo.outlet_id in (
select outlet_id from (
select outlet_id, count(distinct day_number) ct from distributor_beat_plan_log where log_date = '2016-01-31' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769) group by outlet_id having ct in (2)
) tab1
) and mo.created_on between '2016-01-01' and '2016-02-01' having planned is null;

select mo.id, dayofweek(mo.created_on), (select outlet_id from distributor_beat_plan_log where log_date = '2016-01-31' and outlet_id = mo.outlet_id and day_number = dayofweek(mo.created_on) limit 1) planned from mobile_order mo where mo.outlet_id in (
select outlet_id from (
select outlet_id, count(distinct day_number) ct from distributor_beat_plan_log where log_date = '2016-01-31' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769) group by outlet_id having ct in (3)
) tab1
) and mo.created_on between '2016-01-01' and '2016-02-01' having planned is null;

select mo.id, dayofweek(mo.created_on), (select outlet_id from distributor_beat_plan_log where log_date = '2016-01-31' and outlet_id = mo.outlet_id and day_number = dayofweek(mo.created_on) limit 1) planned from mobile_order mo where mo.outlet_id in (
select outlet_id from (
select outlet_id, count(distinct day_number) ct from distributor_beat_plan_log where log_date = '2016-01-31' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769) group by outlet_id having ct in (6)
) tab1
) and mo.created_on between '2016-01-01' and '2016-02-01' having planned is null;


select mo.id, dayofweek(mo.created_on), (select outlet_id from distributor_beat_plan_log where log_date = '2016-01-31' and outlet_id = mo.outlet_id and day_number = dayofweek(mo.created_on) limit 1) planned from mobile_order mo where mo.outlet_id in (
select outlet_id from (
select outlet_id, count(distinct day_number) ct from distributor_beat_plan_log where log_date = '2016-01-31' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769) group by outlet_id having ct not in (2,3,6)
) tab1
) and mo.created_on between '2016-01-01' and '2016-02-01' having planned is null;


/* Raw Cases of specific frequency outlets */

select sum(isap.raw_cases) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.outlet_id in (
select outlet_id from (
select outlet_id, count(distinct day_number) ct from distributor_beat_plan_log where log_date = '2016-01-31' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769) group by outlet_id having ct not in (2,3,6)
) tab1
) and isa.created_on between '2016-01-01' and '2016-02-01';

select sum(isap.raw_cases) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.outlet_id in (
select outlet_id from (
select outlet_id, count(distinct day_number) ct from distributor_beat_plan_log where log_date = '2016-01-31' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769) group by outlet_id having ct in (2)
) tab1
) and isa.created_on between '2016-01-01' and '2016-02-01';

select sum(isap.raw_cases) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.outlet_id in (
select outlet_id from (
select outlet_id, count(distinct day_number) ct from distributor_beat_plan_log where log_date = '2016-01-31' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769) group by outlet_id having ct in (3)
) tab1
) and isa.created_on between '2016-01-01' and '2016-02-01';

select sum(isap.raw_cases) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.outlet_id in (
select outlet_id from (
select outlet_id, count(distinct day_number) ct from distributor_beat_plan_log where log_date = '2016-01-31' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769) group by outlet_id having ct in (6)
) tab1
) and isa.created_on between '2016-01-01' and '2016-02-01';


/* No of invoices of specific frequency outlets */

select count(isa.id) from inventory_sales_adjusted isa where created_on between '2016-01-01' and '2016-02-01' and net_amount != 0 and isa.outlet_id in (
select outlet_id from (
select outlet_id, count(distinct day_number) ct from distributor_beat_plan_log where log_date = '2016-01-31' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769) group by outlet_id having ct not in (2,3,6)
) tab1
);


/* No of lines of frequency outlets */

select count(isap.id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where created_on between '2016-01-01' and '2016-02-01' and isa.outlet_id in (
select outlet_id from (
select outlet_id, count(distinct day_number) ct from distributor_beat_plan_log where log_date = '2016-01-31' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769) group by outlet_id having ct not in (2,3,6)
) tab1
);

/* No of orders of specific frequency outlets */
select count(id) from mobile_order mo where created_on between '2016-01-01' and '2016-02-01' and mo.outlet_id in (
select outlet_id from (
select outlet_id, count(distinct day_number) ct from distributor_beat_plan_log where log_date = '2016-01-31' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769) group by outlet_id having ct not in (2,3,6)
) tab1
);


/* No of calls of specific frequency outlets */
select count(id) from mobile_order mo where created_on between '2016-01-01' and '2016-02-01' and mo.outlet_id in (
select outlet_id from (
select outlet_id, count(distinct day_number) ct from distributor_beat_plan_log where log_date = '2016-01-31' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769) group by outlet_id having ct in (5)
) tab1
);

/* Planeed calls for specific frequency outlets */
select count(*)*4 from distributor_beat_plan_log where log_date = '2016-01-31' and outlet_id in (
select outlet_id from (
select outlet_id, count(distinct day_number) ct from distributor_beat_plan_log where log_date = '2016-01-31' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769) group by outlet_id having ct not in (2,3,6)
) tab1
) order by outlet_id;


/* adjusted volume for specific frequency outlets */
SELECT sum(liquid_in_ml)/6000 FROM inventory_sales_dispatch_adjusted_products where invoice_id in (
	select id from inventory_sales_adjusted where order_id in (select id from mobile_order where created_on between '2016-01-01' and '2016-02-01' and outlet_id in (
		select outlet_id from (
		select outlet_id, count(distinct day_number) ct from distributor_beat_plan_log where log_date = '2016-01-31' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769) group by outlet_id having ct not in (2,3,6)
		) tab1
    ))
);

/* adjusted orders for specific frequency outlets */
SELECT count(distinct invoice_id) FROM inventory_sales_dispatch_adjusted_products where invoice_id in (
	select id from inventory_sales_adjusted where order_id in (select id from mobile_order where created_on between '2016-01-01' and '2016-02-01' and outlet_id in (
		select outlet_id from (
		select outlet_id, count(distinct day_number) ct from distributor_beat_plan_log where log_date = '2016-01-31' and distributor_id in (100095,100287,100356,100401,100439,100657,100679,100706,100746,100756,100761,100795,100833,100835,200769) group by outlet_id having ct not in (2,3,6)
		) tab1
    ))
);




