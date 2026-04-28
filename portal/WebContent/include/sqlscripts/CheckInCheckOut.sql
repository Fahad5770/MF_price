select outlet_id, check_in, check_out, round((time_to_sec(check_out)-time_to_sec(check_in))/60,2) difference_in_minutes from (
SELECT moci.outlet_id, (select min(check_timestamp) from mobile_outlet_check_in where type_id = 1 and outlet_id = moci.outlet_id and check_timestamp between '2015-10-25' and '2015-10-26') check_in, (select min(check_timestamp) from mobile_outlet_check_in where type_id = 2 and outlet_id = moci.outlet_id and check_timestamp between '2015-10-25' and '2015-10-26') check_out FROM pep.mobile_outlet_check_in moci where moci.check_timestamp between '2015-10-25' and '2015-10-26' group by moci.outlet_id having check_in is not null and check_out is not null
) tab2 having difference_in_minutes > 0;



select * from 