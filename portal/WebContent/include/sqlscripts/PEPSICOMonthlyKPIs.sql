
select sum(isap.total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-07-01' and '2016-08-01';
select sum(isap.total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2015-07-01' and '2015-08-01';

select distinct isa.distributor_id from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-05-01' and '2016-06-01';
;
select sum(isap.total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2015-01-01' and '2015-08-01';
select sum(isap.total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-01-01' and '2016-08-01';
;

select sum(isap.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.created_on between '2015-07-01' and '2015-08-01' and outlet_id in (
	select distinct outlet_id from inventory_sales_adjusted where created_on between '2015-07-01' and '2015-08-01' and outlet_id in (select distinct outlet_id from inventory_sales_adjusted where created_on between '2016-07-01' and '2016-08-01')
);
select sum(isap.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.created_on between '2016-07-01' and '2016-08-01' and outlet_id in (
	select distinct outlet_id from inventory_sales_adjusted where created_on between '2015-07-01' and '2015-08-01' and outlet_id in (select distinct outlet_id from inventory_sales_adjusted where created_on between '2016-07-01' and '2016-08-01')
);


select sum(isap.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.created_on between '2016-01-01' and '2016-08-01' and outlet_id in (
	select distinct outlet_id from inventory_sales_adjusted where created_on between '2015-01-01' and '2015-08-01' and outlet_id in (select distinct outlet_id from inventory_sales_adjusted where created_on between '2016-01-01' and '2016-08-01')
);
select sum(isap.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.created_on between '2015-01-01' and '2015-08-01' and outlet_id in (
	select distinct outlet_id from inventory_sales_adjusted where created_on between '2015-01-01' and '2015-08-01' and outlet_id in (select distinct outlet_id from inventory_sales_adjusted where created_on between '2016-01-01' and '2016-08-01')
);

select count(distinct outlet_id) from inventory_sales_adjusted where created_on between '2016-07-01' and '2016-08-01';
select count(distinct outlet_id) from inventory_sales_adjusted where created_on between '2016-01-01' and '2016-08-01';

select count(distinct outlet_id) from distributor_beat_plan_log where log_date between '2016-07-01' and '2016-07-31';
select count(distinct outlet_id) from distributor_beat_plan_log where log_date between '2016-01-01' and '2016-07-31';


select sum(isap.raw_cases) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-07-01' and '2016-07-18';
select count(isa.id) from inventory_sales_adjusted isa where isa.created_on between '2016-06-01' and '2016-07-01';


select sum(isap.raw_cases) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-07-01' and '2016-08-01';
select count(isa.id) from inventory_sales_adjusted isa where isa.created_on between '2016-07-01' and '2016-08-01';

1940659
182998
;
select sum(isap.raw_cases) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-01-01' and '2016-08-01';
select count(isa.id) from inventory_sales_adjusted isa where isa.created_on between '2016-01-01' and '2016-08-01';

10279107
1173109
;
select count(isap.product_id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-07-01' and '2016-08-01';
select count(isa.id) from inventory_sales_adjusted isa where isa.created_on between '2016-07-01' and '2016-08-01';
;


;
select count(isap.product_id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-01-01' and '2016-07-01';
select count(isa.id) from inventory_sales_adjusted isa where isa.created_on between '2016-01-01' and '2016-07-01';

3642748
990362


select sum(d) from (
select created_by, time_to_sec(time(max(created_on))) - time_to_sec(time(min(created_on))) d from mobile_order where created_on between '2016-05-01'  and '2016-06-01' group by created_by having d > 0
) tab1;

select created_by, time_to_sec(time(max(created_on))) - time_to_sec(time(min(created_on))) d from mobile_order where created_on between '2016-05-01'  and '2016-06-01' group by created_by having d > 0;

select count(id) from mobile_order where created_on between '2016-05-01'  and '2016-06-01';
select count(distinct created_by) from mobile_order where created_on between '2016-05-01'  and '2016-06-01';


