SELECT * FROM sampling where outlet_id in (
SELECT id FROM common_outlets where name like '%merit%'
) order by outlet_id;

SELECT * FROM sampling where outlet_id = 16376;

select * from sampling_percase where sampling_id = 21985;

select distinct sampling_id from sampling_percase where sampling_id in (
	select sampling_id from sampling where active = 1 and advance_company_share != 0 and curdate() between activated_on and deactivated_on
) and company_share != deduction_term and curdate() between valid_from and valid_to;


select * from sampling where active = 1 and advance_company_share != 0;


select * from sampling where active = 1 and advance_company_share != 0 and curdate() between activated_on and deactivated_on;

select distinct sampling_id from sampling_percase where sampling_id in (
select sampling_id from (
	select sampling_id, outlet_id, (select ifnull(sum(debit),0)-ifnull(sum(credit),0) from sampling_posting_accounts where outlet_id = s.outlet_id) ledger_balance from sampling s where s.active = 1 having ledger_balance > 0
) tab1
) and curdate() between valid_from and valid_to and company_share != deduction_term;

