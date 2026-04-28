select cache_distributor_id, (select name from common_distributors where distributor_id = tab1.cache_distributor_id) ct, ct from (
SELECT co.cache_distributor_id, count(*) ct FROM pep.common_assets ca join common_outlets co on ca.outlet_id_parsed = co.id where ca.tot_status = 'INJECTED' and ca.movement_date_parsed between '2001-01-01' and '2015-12-31' and co.id not in (select distinct outlet_id from sap_sales where created_on_erdat between '2015-01-01' and '2015-12-31') group by cache_distributor_id
) tab1;




