package model;

import com.jfinal.plugin.activerecord.Model;

public class Order extends Model<Order> {
	/**
	 * Order
	 */
	private static final long serialVersionUID = 1L;
	public static final Order dao = new Order();	
}