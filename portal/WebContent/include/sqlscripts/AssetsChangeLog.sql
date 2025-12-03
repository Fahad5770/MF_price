SELECT main_asset_number, asset_subnumber, count(*) ct FROM pep.common_assets_tot_log group by main_asset_number, asset_subnumber having ct > 1;

select count(*) from (
SELECT main_asset_number, asset_subnumber, count(*) ct FROM pep.common_assets_tot_log group by main_asset_number, asset_subnumber having ct > 1
) tab1;
