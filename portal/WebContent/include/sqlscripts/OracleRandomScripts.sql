select vbak.auart, vbap.matnr, vbap.vrkme, sum(vbap.kwmeng) from sapsr3.vbak vbak join sapsr3.vbuk vbuk on vbak.vbeln = vbuk.vbeln join sapsr3.vbap vbap on vbak.vbeln = vbap.vbeln
where vbak.audat between '20140101' and '20140630' and vbak.auart in ('ZSAM','ZIFQ','ZCLA','ZCAN') and vbuk.lfstk = 'C' group by vbak.auart, vbap.matnr, vbap.vrkme order by vbak.auart, vbap.matnr;


select vbak.kunnr, vbap.vrkme, sum(vbap.kwmeng) from sapsr3.vbak vbak join sapsr3.vbuk vbuk on vbak.vbeln = vbuk.vbeln join sapsr3.vbap vbap on vbak.vbeln = vbap.vbeln
where vbak.audat between '20150101' and '20150630' and vbak.auart in ('ZCLA') and vbuk.lfstk = 'C' group by vbak.kunnr, vbap.vrkme;


select vbap.matnr, vbap.vrkme, sum(vbap.kwmeng) from sapsr3.vbak vbak join sapsr3.vbuk vbuk on vbak.vbeln = vbuk.vbeln join sapsr3.vbap vbap on vbak.vbeln = vbap.vbeln
where vbak.audat between '20150101' and '20150630' and vbak.auart in ('ZCLA') and vbuk.lfstk = 'C' and vbak.kunnr = '0000100034' group by vbap.matnr, vbap.vrkme;

select vbak.vbeln, vbap.matnr, vbap.vrkme, vbap.kwmeng from sapsr3.vbak vbak join sapsr3.vbuk vbuk on vbak.vbeln = vbuk.vbeln join sapsr3.vbap vbap on vbak.vbeln = vbap.vbeln
where vbak.audat between '20140101' and '2014 c0630' and vbak.auart in ('ZCLA') and vbuk.lfstk = 'C' and vbak.kunnr = '0000100034' order by vbap.vrkme, vbap.kwmeng;


select * from sapsr3.vbak vbak join sapsr3.vbuk vbuk on vbak.vbeln = vbuk.vbeln join sapsr3.vbap vbap on vbak.vbeln = vbap.vbeln
where vbak.audat between '20140101' and '20140630' and vbak.auart in ('ZSAM','ZIFQ','ZCLA','ZCAN') and vbuk.lfstk = 'C';

select * from sapsr3.vbak where auart in ('ZREN','YEMP','ZEMP');
select * from sapsr3.vbak where vbeln = 2000374518;

select sum((vbap.kzwi1+ /*vbrp.kzwi2 + vbrp.kzwi3 +*/ vbap.kzwi6)) gross_value from sapsr3.vbak vbak join sapsr3.vbuk vbuk on vbak.vbeln = vbuk.vbeln join sapsr3.vbap vbap on vbak.vbeln = vbap.vbeln
where vbak.audat between '20150401' and '20150430' and vbak.auart in ('ZDIS','ZMRS','ZDFR') and vbuk.lfstk = 'C' and vbap.pstyv = 'TANN';

select vbrk.kurrf_dat /*Transaction Date*/, vbrk.fkdat /*Billing Date*/, vbrk.erdat, vbrk.aedat /*Changed On*/, vbrk.vbeln, vbrk.netwr+vbrk.mwsbk invoice_amount, vbrk.fkart, vbrk.kunag /*Customer*/, kna1.name1, vbrk.ZZCDS_STATUS
from sapsr3.vbrk vbrk join sapsr3.vbuk vbuk on vbrk.vbeln = vbuk.vbeln join sapsr3.kna1 kna1 on vbrk.kunag = kna1.kunnr 
where vbrk.kurrf_dat between '20140101' and '20140301' and vbrk.fksto != 'X' /* Invoice not Cancelled */ and vbrk.fkart in ('ZDIS','ZMRS','ZDFR') and vbuk.buchk != 'C';


select * from sapsr3.vbak where auart in ('ZREN','YEMP','ZEMP');

select mkpf.mblnr DocumentID, mkpf.budat PostingDate, mseg.matnr ProductSAPCode, mseg.erfme UoM, mseg.erfmg Quantity, mseg.lgort from sapsr3.mkpf mkpf join sapsr3.mseg mseg on mkpf.mblnr = mseg.mblnr where mkpf.budat between '20150101' and '20150630' and mseg.lgort in ('363','387') and mkpf.usnam = 'STINCH' and mseg.bwart in ('311','312');

select * from sapsr3.mseg where mblnr = 4902101185 and mseg.lgort = 363;

select * from sapsr3.vbrk vbrk join sapsr3.vbrp vbrp on vbrk.vbeln = vbrp.vbeln where vbrk.vbeln = 9000099959;

select * from sapsr3.zsd_credit_bal where regio = 'RM5' and botcp in ('R01','R02','R12') and reason = '18' and audat between '20150101' and '20150630';

select benid, botcp, reason, vbeln, audat, vehno, movmt, status, quantity from sapsr3.zsd_credit_bal where regio = 'RM5' and botcp in ('R01','R02','R12') and audat between '20150101' and '20150630';

select benid, botcp, reason, vbeln, audat, vehno, movmt, status, quantity, botyp from sapsr3.zsd_credit_bal where regio = 'RM5' and botcp in ('R01','R02','R12') and audat between '20150101' and '20150630';


  SELECT mkpf.mblnr, mkpf.vgart, mvke.mvgr4, mseg.grund, mkpf.budat, mseg.bwart, mseg.erfme, mseg.erfmg erfmg, mvke.mvgr1 FROM sapsr3.mkpf mkpf JOIN sapsr3.mseg mseg ON (mkpf.mblnr = mseg.mblnr AND mkpf.mjahr = mseg.mjahr)
    JOIN sapsr3.mara ON ( mseg.matnr = mara.matnr )
    JOIN sapsr3.mvke mvke ON ( mara.matnr = mvke.matnr AND mvke.vkorg = '1000' AND mvke.vtweg = '20' )
    WHERE mkpf.budat between '20150101' and '20150520'  AND  ( ( mvke.mvgr4 = 'R01' AND mseg.grund IN ('0003','0007','0005') ) OR ( mvke.mvgr1 in ('P01','P12') AND mseg.grund IN ('0013', '0003','0004','0006') ) )
    AND   mara.matkl IN ('001','002','003','004','005','006','007','008','009','010','012','013','054','011','014')
    AND   mseg.bwart IN ('551','552')
    /*AND   mseg.grund IN ('0013','0006','0003','0004','0007','0005')*/
    AND   mseg.lgort IN (309,310,313,314,311,312,306,316,307,356,303,357,304,375,366,376,367,363,315,364,370,372,371,373,380,381,377,378,383,384) and mseg.grund = 13;
    

;

SELECT mseg.mblnr, mseg~shkzg, mseg.erfme, mseg.erfmg, mseg~lgort FROM mseg JOIN mara ON ( mseg.matnr = mara.matnr )
       WHERE  mseg.mblnr = all_docs-mblnr
       AND    mseg~mjahr = all_docs-mjahr
       AND    mara~matnr IN rmatnr
       AND    mseg~bwart = '262'
       AND    mseg~lgort = '369';
       
       
       
/*during production shipping breakage*/
/*250*/
select mkpf.mblnr DocumentID, mkpf.budat PostingDate, mseg.matnr ProductSAPCode, mseg.erfme UoM, mseg.erfmg Quantity, mseg.lgort from sapsr3.mkpf mkpf join sapsr3.mseg mseg on mkpf.mblnr = mseg.mblnr where mkpf.budat between '20150101' and '20150520' and mseg.lgort in ('369') and mseg.bwart in ('262') and mseg.matnr in ('000000000000005001','000000000000005002','000000000000005003','000000000000005004','000000000000005005','000000000000005006','000000000000005007','000000000000005031','000000000000005041');
/*1000*/
select mkpf.mblnr DocumentID, mkpf.budat PostingDate, mseg.matnr ProductSAPCode, mseg.erfme UoM, mseg.erfmg Quantity, mseg.lgort from sapsr3.mkpf mkpf join sapsr3.mseg mseg on mkpf.mblnr = mseg.mblnr where mkpf.budat between '20150101' and '20150520' and mseg.lgort in ('369') and mseg.bwart in ('262') and mseg.matnr in ('000000000000005008','000000000000005009','000000000000005010','000000000000005011');


;
  SELECT afru.budat, afru.vornr, afru.grund, SUM(afru.xmnga) xmnga  FROM sapsr3.afru afru JOIN sapsr3.afpo afpo ON ( afru.aufnr = afpo.aufnr )
  JOIN sapsr3.mara ON ( afpo.matnr = mara.matnr )
  JOIN sapsr3.mvke ON ( mara.matnr = mvke.matnr AND mvke.vkorg = '1000' AND mvke.vtweg = '20' AND mvke.mvgr1 = 'P02' )
      WHERE afru.budat between '20150101' and '20150520'
        AND afru.werks = '3000'
        AND afru.xmnga <> '000.0'
        /*AND afru.grund IN ('0003', '0009')*/
        AND afru.stokz = ''
        AND afru.stzhl = '00000000'
        AND afpo.dauat = 'ZPBF'
    GROUP BY afru.budat,afru.vornr,afru.grund


;
/* return to store */
select mkpf.mblnr DocumentID, mkpf.budat PostingDate, mseg.matnr ProductSAPCode, mseg.erfme UoM, mseg.erfmg Quantity, mseg.lgort, mseg.bwart from sapsr3.mkpf mkpf join sapsr3.mseg mseg on mkpf.mblnr = mseg.mblnr where mkpf.budat between '20150101' and '20150520' and mseg.lgort in ('302','386') and mseg.bwart in ('313','314') and mseg.matnr in ('000000000000005017','000000000000005018','000000000000005019','000000000000005020','000000000000005022','000000000000005023');



SELECT * FROM sapsr3.vbap vbap where vbap.ABGRU = ' ';

/* for targets */f
SELECT vbak.vbeln order_number, vbak.kunnr distributor_id, vbak.erdat entry_date, vbak.audat order_date, vbuk.fksak, vbap.ABGRU, vbap.posnr, vbap.matnr, vbap.arktx, vbap.vrkme, vbap.KWMENG, vbap.pstyv FROM sapsr3.vbak vbak join sapsr3.vbuk vbuk on vbak.vbeln = vbuk.vbeln join sapsr3.vbap vbap on vbak.vbeln = vbap.vbeln where vbak.auart in ('ZDIS','ZMRS','ZDFR') and vbak.audat between '20150701' and '20150731' and vbuk.vbtyp = 'C'
;
select * from sapsr3.vbap where matnr in ('000000000000001241');



select vbrk.kurrf_dat /*Transaction Date*/, vbrk.fkdat /*Billing Date*/, vbrk.erdat, vbrk.aedat /*Changed On*/, vbrk.vbeln, vbrk.netwr+vbrk.mwsbk invoice_amount, vbrk.fkart, vbrk.kunag /*Customer*/, kna1.name1, vbrk.ZZCDS_STATUS
from sapsr3.vbrk vbrk join sapsr3.vbuk vbuk on vbrk.vbeln = vbuk.vbeln join sapsr3.kna1 kna1 on vbrk.kunag = kna1.kunnr 
where vbrk.kurrf_dat between '20140101' and '20140301' and vbrk.fksto != 'X' /* Invoice not Cancelled */ and vbrk.fkart in ('ZDIS','ZMRS','ZDFR') and vbuk.buchk != 'C';


select vbrk.vbeln, vbrk.fkart, vbrk.knumv, vbrp.posnr, vbrk.kurrf_dat, vbrp.pstyv, vbrp.matnr, vbrp.fkimg quantity, (vbrp.kzwi1+ /*vbrp.kzwi2 + vbrp.kzwi3 +*/ vbrp.kzwi6) gross_value, vbrp.kzwi4 unloading, vbrp.kzwi5 freight, vbrp.vrkme, vbrk.kunrg from sapsr3.vbrp vbrp join sapsr3.vbrk on vbrk.vbeln = vbrp.vbeln where vbrk.fkart in ('ZDIS', 'ZMRS', 'ZDFR') and vbrk.fksto != 'X' and vbrk.kurrf_dat between '20150801' and '20151231' and vbrk.vbeln = 8000219926;
;
select vbrk.fksto, kurrf_dat from sapsr3.vbrk vbrk where vbeln = 9000390189;
select * from sapsr3.vbak vbak where vbeln = 2000392682;

select vbrk.kurrf_dat /*Transaction Date*/, vbrk.fkdat /*Billing Date*/, vbrk.erdat, vbrk.aedat /*Changed On*/, vbrk.vbeln, vbrk.netwr+vbrk.mwsbk invoice_amount, vbrk.fkart, vbrk.kunag /*Customer*/, kna1.name1, vbrk.ZZCDS_STATUS
from sapsr3.vbrk vbrk join sapsr3.vbuk vbuk on vbrk.vbeln = vbuk.vbeln join sapsr3.kna1 kna1 on vbrk.kunag = kna1.kunnr 
where vbrk.kurrf_dat between '20140101' and '20140301' and vbrk.fksto != 'X' /* Invoice not Cancelled */ and vbrk.fkart in ('ZDIS','ZMRS','ZDFR') and vbuk.buchk != 'C' and vbrk.vbeln = '900041499';