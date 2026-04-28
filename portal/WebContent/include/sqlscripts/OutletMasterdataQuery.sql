SELECT co.id CO_OUTLET_ID, co.id SD_OUTLET_ID, co.id dma_outlet_id, co.id dmap_outlet_id, co.name CO_OUTLET_NAME, co.region_id CO_REGION_ID, 
(select region_short_name from common_regions where region_id = co.region_id) CO_REGION_LABEL, 
(select region_id from common_distributors where distributor_id = co.cache_distributor_id) CO_REGION_ID_THEIA, 
(select (select region_short_name from common_regions where region_id = cd.region_id) from common_distributors cd where cd.distributor_id = co.cache_distributor_id) CO_REGION_LABEL_THEIA,
(select city from common_distributors where distributor_id = co.cache_distributor_id) CO_DISTRIBUTOR_CITY,
co.cache_snd_id CO_SND_ID,
(select display_name from users where id = co.cache_snd_id) CO_SND_NAME,
co.cache_rsm_id CO_RSM_ID,
(select display_name from users where id = co.cache_rsm_id) CO_RSM_NAME,
co.cache_tdm_id CO_TDM_ID,
(select display_name from users where id = co.cache_tdm_id) CO_TDM_NAME,
co.cache_distributor_id CO_DISTRIBUTOR_ID,
co.cache_distributor_name CO_DISTRIBUTOR_NAME,
if((select s.request_id from sampling s where s.outlet_id = co.id and curdate() between s.activated_on and s.deactivated_on) is null,'No','Yes') CO_IS_DISCOUNT_ACTIVE,
if((select s.advance_company_share from sampling s where s.outlet_id = co.id and curdate() between s.activated_on and s.deactivated_on) > 0,'Yes','No') CO_IS_ADVANCE_ACTIVE,
(select s.advance_company_share from sampling s where s.outlet_id = co.id and curdate() between s.activated_on and s.deactivated_on) CO_ADVANCE_ISSUED,
(select date(max(s.activated_on)) from sampling s where s.outlet_id = co.id) CO_LAST_DISCOUNT_AGREEMENT_ON,
(select sp.company_share from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id where s.outlet_id = co.id and curdate() between s.activated_on and s.deactivated_on and sp.package = 11 and curdate() between sp.valid_from and sp.valid_to limit 1) CO_PER_CASE_DISCOUNT_250ML,
(select count(id) from common_assets where outlet_id_parsed = co.id and tot_staus = 'INJECTED') CO_TOT_INJECTED
FROM common_outlets co;


;
