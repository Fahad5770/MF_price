
select * from inventory_sales_promotions_request where id in (
SELECT product_promotion_id FROM pep.inventory_sales_promotions_request_regions where region_id = 1
);

select * from inventory_sales_promotions_request_products isprp join inventory_packages ip on isprp.package_id = ip.id join inventory_sales_promotions_request_products_brands isprpb on isprp.id= isprpb.id and isprp.package_id = isprpb.package_id and isprp.type_id = isprpb.type_id join inventory_brands ib on isprpb.brand_id = ib.id where isprp.id in (
SELECT product_promotion_id FROM pep.inventory_sales_promotions_request_regions where region_id in (1) union SELECT product_promotion_id FROM pep.inventory_sales_promotions_request_distributors where distributor_id = 100054
) order by isprp.id, isprp.type_id;
