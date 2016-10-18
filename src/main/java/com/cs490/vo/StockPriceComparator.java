package com.cs490.vo;

import java.util.Comparator;

public class StockPriceComparator implements Comparator<StockSnapshotVO> {
	@Override
	public int compare(StockSnapshotVO o1, StockSnapshotVO o2) {
		return o1.getPrice().compareTo(o2.getPrice());
	}

}
