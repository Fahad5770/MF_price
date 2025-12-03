/* total volume converted  -  Presell */
select sum(isap.total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2017-01-01' and '2017-07-01';
select sum(isap.total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2015-01-01' and '2016-04-01';

select sum(isap.total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-10-01' and '2016-11-01' and isa.region_id in (5,11);
select sum(isap.total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2015-10-01' and '2015-11-01' and isa.region_id in (5,11);


/* total volume converted pepsi */
select sum(isap.total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-08-01' and '2016-09-01' and ipv.brand_id = 1;
select sum(isap.total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2015-08-01' and '2015-09-01' and ipv.brand_id = 1;

/* total volume dew */
select sum(isap.total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-08-01' and '2016-09-01' and ipv.brand_id = 5;
select sum(isap.total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2015-08-01' and '2015-09-01' and ipv.brand_id = 5;

/* total volume 7up */
select sum(isap.total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-08-01' and '2016-09-01' and ipv.brand_id = 4;
select sum(isap.total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2015-08-01' and '2015-09-01' and ipv.brand_id = 4;

/* == YTD == */

/* total volume converted  - Presell*/
select sum(isap.total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-03-01' and '2016-04-01';
select sum(isap.total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2017-01-01' and '2017-05-01';

select sum(isap.total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-01-01' and '2016-11-01' and isa.region_id in (5,11);
select sum(isap.total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2015-01-01' and '2015-11-01' and isa.region_id in (5,11);


/* total volume converted pepsi */
select sum(isap.total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-01-01' and '2016-09-01' and ipv.brand_id = 1;
select sum(isap.total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2015-01-01' and '2015-09-01' and ipv.brand_id = 1;

/* total volume dew */
select sum(isap.total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-01-01' and '2016-09-01' and ipv.brand_id = 5;
select sum(isap.total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2015-01-01' and '2015-09-01' and ipv.brand_id = 5;

/* total volume 7up */
select sum(isap.total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-01-01' and '2016-09-01' and ipv.brand_id = 4;
select sum(isap.total_units*ipv.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2015-01-01' and '2015-09-01' and ipv.brand_id = 4;


select distinct isa.distributor_id from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-05-01' and '2016-06-01';
;

/* Same outlet volume Month - Same store growth*/
select sum(isap.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.created_on between '2015-11-01' and '2015-12-01' and outlet_id in (
	select distinct outlet_id from inventory_sales_adjusted where created_on between '2015-11-01' and '2015-12-01' and outlet_id in (select distinct outlet_id from inventory_sales_adjusted where created_on between '2016-11-01' and '2016-12-01')
);

/* for 2016 - Same store growth  */
/* Formula 

difference = 2016 - 2015
growth  = (difference / 2015) * 100

*/


/* 2017 */
select sum(isap.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.created_on between '2017-07-01' and '2017-08-01' and outlet_id in (
	select distinct outlet_id from inventory_sales_adjusted where created_on between '2016-07-01' and '2016-08-01' and outlet_id in (select distinct outlet_id from inventory_sales_adjusted where created_on between '2016-07-01' and '2016-08-01')
);

/* 2016 */

select sum(isap.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.created_on between '2016-07-01' and '2016-08-01' and outlet_id in (
	select distinct outlet_id from inventory_sales_adjusted where created_on between '2016-07-01' and '2016-08-01' and outlet_id in (select distinct outlet_id from inventory_sales_adjusted where created_on between '2016-07-01' and '2016-08-01')
);


/* ////////////////////////////////////// */

/* Same outlet volume Month - FSD */
select sum(isap.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.created_on between '2015-10-01' and '2015-11-01' and outlet_id in (
	select distinct outlet_id from inventory_sales_adjusted where created_on between '2015-10-01' and '2015-11-01' and outlet_id in (select distinct outlet_id from inventory_sales_adjusted where created_on between '2016-10-01' and '2016-11-01')
) and isa.region_id in (5,11);
select sum(isap.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.created_on between '2016-10-01' and '2016-11-01' and outlet_id in (
	select distinct outlet_id from inventory_sales_adjusted where created_on between '2015-10-01' and '2015-11-01' and outlet_id in (select distinct outlet_id from inventory_sales_adjusted where created_on between '2016-10-01' and '2016-11-01')
) and isa.region_id in (5,11);


/* Same outlet volume Month PEPSI */
select sum(isap.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.created_on between '2015-08-01' and '2015-09-01' and outlet_id in (
	select distinct outlet_id from inventory_sales_adjusted where created_on between '2015-08-01' and '2015-09-01' and outlet_id in (select distinct outlet_id from inventory_sales_adjusted where created_on between '2016-08-01' and '2016-09-01')
) and cache_brand_id = 1;
select sum(isap.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.created_on between '2016-08-01' and '2016-09-01' and outlet_id in (
	select distinct outlet_id from inventory_sales_adjusted where created_on between '2015-08-01' and '2015-09-01' and outlet_id in (select distinct outlet_id from inventory_sales_adjusted where created_on between '2016-08-01' and '2016-09-01')
) and cache_brand_id = 1;

/* Same outlet volume YEAR */
select sum(isap.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.created_on between '2015-01-01' and '2015-09-01' and outlet_id in (
	select distinct outlet_id from inventory_sales_adjusted where created_on between '2015-01-01' and '2015-09-01' and outlet_id in (select distinct outlet_id from inventory_sales_adjusted where created_on between '2016-01-01' and '2016-09-01')
);
select sum(isap.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.created_on between '2016-01-01' and '2016-09-01' and outlet_id in (
	select distinct outlet_id from inventory_sales_adjusted where created_on between '2015-01-01' and '2015-09-01' and outlet_id in (select distinct outlet_id from inventory_sales_adjusted where created_on between '2016-01-01' and '2016-09-01')
);

/* Same outlet volume YEAR Pepsi */
select sum(isap.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.created_on between '2015-01-01' and '2015-09-01' and outlet_id in (
	select distinct outlet_id from inventory_sales_adjusted where created_on between '2015-01-01' and '2015-09-01' and outlet_id in (select distinct outlet_id from inventory_sales_adjusted where created_on between '2016-01-01' and '2016-09-01')
) and cache_brand_id = 1;
select sum(isap.liquid_in_ml)/6000 from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.created_on between '2016-01-01' and '2016-09-01' and outlet_id in (
	select distinct outlet_id from inventory_sales_adjusted where created_on between '2015-01-01' and '2015-09-01' and outlet_id in (select distinct outlet_id from inventory_sales_adjusted where created_on between '2016-01-01' and '2016-09-01')
) and cache_brand_id = 1;


/* Outlets covered Month */
select count(distinct outlet_id) from inventory_sales_adjusted where created_on between '2016-08-01' and '2016-09-01';
select count(distinct isa.outlet_id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-08-01' and '2016-09-01' and isap.cache_brand_id = 1;
select count(distinct isa.outlet_id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-08-01' and '2016-09-01' and isap.cache_brand_id = 5;
select count(distinct isa.outlet_id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-08-01' and '2016-09-01' and isap.cache_brand_id = 4;

/* Outlets covered Year */
select count(distinct outlet_id) from inventory_sales_adjusted where created_on between '2016-01-01' and '2016-09-01';
select count(distinct isa.outlet_id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-01-01' and '2016-09-01' and isap.cache_brand_id = 1;
select count(distinct isa.outlet_id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-01-01' and '2016-09-01' and isap.cache_brand_id = 5;
select count(distinct isa.outlet_id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-01-01' and '2016-09-01' and isap.cache_brand_id = 4;


/* PJP analysis */
/*select count(distinct outlet_id) from distributor_beat_plan_log where log_date between '2016-11-01' and '2016-11-30';*/

/* PJP - Moth & year - Put month date for month, put year date for year*/
select count(distinct outlet_id) from distributor_beat_plan_log where log_date between '2017-01-01' and '2017-06-30' and distributor_id not in (select distributor_id from common_distributors where is_shifted_to_other_plant = 1);
/* Zero sale for month and year*/
select count(distinct outlet_id) from distributor_beat_plan_log where log_date between '2017-01-01' and '2017-06-31' and distributor_id not in (select distributor_id from common_distributors where is_shifted_to_other_plant = 1) and outlet_id not in (select distinct outlet_id from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.created_on between '2017-01-01' and '2017-07-01');


/* No of invoices Month */
select count(distinct isa.id) from inventory_sales_adjusted isa where isa.created_on between '2016-10-01' and '2016-11-01' and net_amount != 0;

select count(distinct isa.id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-08-01' and '2016-09-01' and isap.cache_brand_id = 1;
select count(distinct isa.id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-08-01' and '2016-09-01' and isap.cache_brand_id = 5;
select count(distinct isa.id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-08-01' and '2016-09-01' and isap.cache_brand_id = 4;

/* No of invoices Year */
select count(distinct isa.id) from inventory_sales_adjusted isa where isa.created_on between '2016-01-01' and '2016-11-01' and net_amount != 0;

select count(distinct isa.id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-01-01' and '2016-09-01' and isap.cache_brand_id = 1;
select count(distinct isa.id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-01-01' and '2016-09-01' and isap.cache_brand_id = 5;
select count(distinct isa.id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-01-01' and '2016-09-01' and isap.cache_brand_id = 4;

/* Lines Sold */
select count(isap.product_id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-10-01' and '2016-11-01';

select count(isap.product_id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-08-01' and '2016-09-01' and isap.cache_brand_id = 1;
select count(isap.product_id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-08-01' and '2016-09-01' and isap.cache_brand_id = 5;
select count(isap.product_id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-08-01' and '2016-09-01' and isap.cache_brand_id = 4;

/* Lines Sold Year */
select count(isap.product_id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-01-01' and '2016-11-01';

select count(isap.product_id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-01-01' and '2016-09-01' and isap.cache_brand_id = 1;
select count(isap.product_id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-01-01' and '2016-09-01' and isap.cache_brand_id = 5;
select count(isap.product_id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-01-01' and '2016-09-01' and isap.cache_brand_id = 4;

/* Raw Cases Month and Year - For Dropsize*/
select sum(isap.raw_cases) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2017-07-01' and '2017-08-01'; -- MTD - Rawcases = Total 8 oz. cases sold
select sum(isap.raw_cases) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2017-01-01' and '2017-07-01'; -- YTD



select sum(isap.raw_cases) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-07-01' and '2016-07-18';

/* dropsize = rawcases/total invoice ---- total invoices*/ 
select count(isa.id) from inventory_sales_adjusted isa where isa.created_on between '2017-04-01' and '2017-05-01'; -- MTD   Total Invoices = Total Productive Calls


select count(isa.id) from inventory_sales_adjusted isa where isa.created_on between '2017-01-01' and '2017-05-01'; -- YTD

1940659
182998
;
select sum(isap.raw_cases) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2016-01-01' and '2016-08-01';
select count(isa.id) from inventory_sales_adjusted isa where isa.created_on between '2016-07-01' and '2016-08-01';

10279107
1173109
;

/*  SKU Per Bill  - Sku bill query / no of invoices */
select count(isap.product_id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2017-07-01' and '2017-08-01'; -- MTD -- Sum of SKUs sold per individual order in the period
select count(isa.id) from inventory_sales_adjusted isa where isa.created_on between '2017-01-01' and '2017-07-01'; -- Total Productive Calls
;


;
select count(isap.product_id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.created_on between '2017-01-01' and '2017-05-01'; -- YTD
select count(isa.id) from inventory_sales_adjusted isa where isa.created_on between '2017-01-01' and '2017-03-01';

3642748
990362


select sum(d) from (
select created_by, time_to_sec(time(max(created_on))) - time_to_sec(time(min(created_on))) d from mobile_order where created_on between '2016-05-01'  and '2016-06-01' group by created_by having d > 0
) tab1;

select created_by, time_to_sec(time(max(created_on))) - time_to_sec(time(min(created_on))) d from mobile_order where created_on between '2016-05-01'  and '2016-06-01' group by created_by having d > 0;

select count(id) from mobile_order where created_on between '2016-05-01'  and '2016-06-01';
select count(distinct created_by) from mobile_order where created_on between '2016-05-01'  and '2016-06-01';


