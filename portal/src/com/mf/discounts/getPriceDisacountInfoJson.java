package com.mf.discounts;

import com.mf.modals.PriceHandDiscount;
import com.pbc.util.Datasource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class getPriceDisacountInfoJson {
   public static List<PriceHandDiscount> get_price_hand_discount(Datasource ds, String AllOutlets) {
      ArrayList PriceHandDiscounts = new ArrayList();

      try {
         Statement s = ds.createStatement();
         if (AllOutlets.length() > 0) {
            ResultSet rsPHD = s.executeQuery(" SELECT * FROM inventory_price_list_hand_discount_mview where outlet_id in(" + AllOutlets + ") ");

            while(rsPHD.next()) {
               PriceHandDiscount priceHandDiscount = new PriceHandDiscount(rsPHD.getInt("sampling_id"), rsPHD.getLong("outlet_id"), rsPHD.getInt("product_id"), rsPHD.getDouble("discount"), rsPHD.getString("created_on"));
               PriceHandDiscounts.add(priceHandDiscount);
            }
         }

         s.close();
      } catch (SQLException var6) {
         System.out.println("User Details Error :- " + var6);
      }

      return PriceHandDiscounts;
   }
}
