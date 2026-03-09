package com.pbc.myscripts;

import java.sql.SQLException;
import com.mf.controller.order.OrderFunctions;

public class TestFunctions {
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		final OrderFunctions OF = new OrderFunctions();
		OF.splitOrder(10073284);
	}
}
