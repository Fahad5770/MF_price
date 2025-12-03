package com.pbc.reports;

import java.util.ArrayList;

public class R372DataList {
public ArrayList<R372Data> R372s = new ArrayList<R372Data>();
	
	public R372Data[] getR372(){
		return R372s.toArray(new R372Data[R372s.size()]);
	}
}
