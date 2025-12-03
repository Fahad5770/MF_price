select sum(smap.sampling_amount)/sum(sales) from sampling_monthly_approval sma join sampling_monthly_approval_percase smap on sma.approval_id = smap.approval_id where sma.month = '2016-10-31' and sma.distributor_id = 100914 and smap.package_id = 12;

select sum(smap.sampling_amount)/sum(sales) from sampling_monthly_approval sma join sampling_monthly_approval_percase smap on sma.approval_id = smap.approval_id where sma.month = '2016-10-31' and sma.distributor_id = 100914 and smap.package_id = 11;

select * from inventory_packages;
