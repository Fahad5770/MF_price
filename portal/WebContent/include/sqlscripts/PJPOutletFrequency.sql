select tab2.distributor_id, (select name from common_distributors where distributor_id = tab2.distributor_id) distributor_name, pjp_id, pjp_name, assigned_to, (select display_name from users where id = assigned_to) assgined_to_name, outlet_frequency, no_of_outlets from (
select distributor_id, pjp_id, pjp_name, assigned_to, outlet_frequency, count(outlet_id) no_of_outlets from (
SELECT dbpv.distributor_id, dbpv.id pjp_id, dbpv.label pjp_name, assigned_to, outlet_id, count(*) outlet_frequency FROM pep.distributor_beat_plan_view dbpv group by dbpv.distributor_id, dbpv.id, dbpv.label, assigned_to, outlet_id
) tab1 group by distributor_id, pjp_id, pjp_name, assigned_to, outlet_frequency order by distributor_id, pjp_id
) tab2;



SELECT dbpv.distributor_id, dbpv.id pjp_id, dbpv.label pjp_name, assigned_to FROM pep.distributor_beat_plan_view dbpv group by dbpv.distributor_id, dbpv.id, dbpv.label, assigned_to;

select co.cache_beat_plan_id, sum((isap.total_units*ipv.liquid_in_ml))/6000 converted from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id join common_outlets co on isa.outlet_id = co.id where isa.created_on between '2015-01-01' and '2015-10-27' and isap.is_promotion=0 and cache_beat_plan_id is not null group by co.cache_beat_plan_id;
