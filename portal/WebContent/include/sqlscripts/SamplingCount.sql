select distinct outlet_id from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id where s.sampling_id in (
select distinct sampling_id from sampling_percase where sampling_id in (
SELECT sampling_id FROM sampling s where s.active = 1 and s.fixed_company_share != 0 and curdate() between s.fixed_valid_from and s.fixed_valid_to
) and curdate() between valid_from and valid_to
);
select count(*) from (
SELECT sampling_id, outlet_id, fixed_valid_from, fixed_valid_to, (select sampling_id from sampling_percase sp where sp.sampling_id = s.sampling_id and curdate() between sp.valid_from and sp.valid_to limit 1) per_case FROM sampling s where s.active = 1 having curdate() between s.fixed_valid_from and s.fixed_valid_to or per_case is not null
) tab1;