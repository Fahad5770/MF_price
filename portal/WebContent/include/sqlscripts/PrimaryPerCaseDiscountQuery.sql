select sum(if(lifting_today>lifting_total,lifting_total,lifting_today) * percase_discount_rate) discount from (
select ipprd.distributor_id,ipprpl.package_id, ipprpl.lrb_type_id, ipprp.percase_discount_rate, ippr.valid_from, ippr.valid_to, ifnull(ipprp.quantity - (
	select sum(idnp.total_units)/ipv.unit_per_sku from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id = ipprd.distributor_id and created_on between ippr.valid_from and ippr.valid_to and package_id=ipprpl.package_id and lrb_type_id=ipprpl.lrb_type_id
),0) lifting_total, ifnull((
	select sum(idnp.total_units)/ipv.unit_per_sku from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id = ipprd.distributor_id and created_on between curdate() and from_days(to_days(curdate())+1) and package_id=ipprpl.package_id and lrb_type_id=ipprpl.lrb_type_id
),0) lifting_today from inventory_primary_percase_request ippr join inventory_primary_percase_request_distributors ipprd on ippr.id = ipprd.product_promotion_id join inventory_primary_percase_request_products ipprp on ippr.id=ipprp.id join inventory_primary_percase_request_products_lrb_types ipprpl on ippr.id=ipprpl.id and ipprp.package_id = ipprpl.package_id where curdate() between ippr.valid_from and ippr.valid_to and ippr.is_active = 1 and ipprd.distributor_id in (select distributor_id from common_distributors where snd_id = 2368) and ipprpl.package_id = 24 and ipprpl.lrb_type_id = 1 having lifting_total > 0
) tab1;




