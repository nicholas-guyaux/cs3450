package model;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import controller.Program;

public class Order {
	//private Map<Product, Integer> mItemsList;
	//private Map<Product, Integer> mReturnList;
	private ArrayList<Item> mItemList;
	private ArrayList<Item> mReturnList;

	private int mOrderID;
	private int mCustomerID;
	private float mTotal;
	private int mEmployeeID;
	
	
	private double mReturnTotal;
	public Object getItemList;

	private int productID;

	public Order(int id, int custID, float total) {
		mOrderID = id;
		mCustomerID = custID;
		mTotal = total;
		mEmployeeID = Program.getInstance().getDataAccess().getCurrentUser().getId();
		
		//mItemsList = new HashMap<Product, Integer>();
		//mReturnList = new HashMap<Product, Integer>();
		setmItemList(new ArrayList<Item>());
		
		
		mReturnTotal = 0;
		
		
	}
	
	public Order(Order o) {
		this.mOrderID = o.getId();
		this.mCustomerID = o.getmCustomerID();
		this.mTotal = o.getTotal();
		this.mEmployeeID = o.getmEmployeeID();
		mReturnTotal = 0;
		//mItemsList = Program.getInstance().getDataAccess().getOrderAndProducts(mOrderID);
		setmItemList(Program.getInstance().getDataAccess().getItemsByOrderID(mOrderID));
	}


	public int getmEmployeeID() {
		return mEmployeeID;
	}
	
	/*
	public Map<Product, Integer> getMItemsList() {
		return mItemsList;
	}
*/

	public void setmEmployeeID(int mEmployeeID) {
		this.mEmployeeID = mEmployeeID;
	}



	public int getmCustomerID() {
		return mCustomerID;
	}



	public void setmCustomerID(int mCustomerID) {
		this.mCustomerID = mCustomerID;
	}

	public ArrayList<Item> getItemList() {
		return mItemList;
	}
	
	public ArrayList<Item> getReturnList() {
		return mReturnList;
	}


	/** makes an copy of copy */
//	public Order(Order copy){
//		this(copy.getId());
//		for (Item i : copy.getReturnList()){
//			if(i.getQuantity() > 0)
//				addItem(i.getProduct(), i.getQuantity());
//			else
//				returnProduct(i.getProduct(),i.getQuantity());
//		}
//		
//	}

	/** adds a product to the order */
	public void addItem(int productID, int quantity, float price) {
		int pos = getProdPosition(mItemList, productID);
		int q;
		if(pos>=0) {
			q = quantity + mItemList.get(pos).getQuantity();
			mItemList.get(pos).setQuantity(q);
			
		} else {
			q = quantity;
			mItemList.add(new Item(mOrderID, productID, quantity, price));
		}
		
	
		mTotal = mTotal + price * quantity;
		
	}
	
	public int getProdPosition(ArrayList<Item> list, int prodID) {
		for(int i = 0; i < list.size(); ++i) {
			if(this.mItemList.get(i).getProductID() == prodID) return i;
		}
		return -1;
	}

	public void editItem(int productID, int quantity, float price) {
		int pos = getProdPosition(mItemList, productID);
		int q;
		if(pos>=0) {
			q = mItemList.get(pos).getQuantity();
			mItemList.get(pos).setQuantity(quantity);
			mTotal = mTotal + price * (quantity - q);
		}
	}

	public void removeItem(int productID) {
		int pos = getProdPosition(mItemList, productID);
		if(pos>=0) {
			mTotal = mTotal - mItemList.get(pos).getmPrice() * mItemList.get(pos).getQuantity();
			mItemList.remove(pos);
		}
	}

	/*
	public ArrayList<Item> getOrderList() {
		ArrayList<Item> itemList = new LinkedList<>();
		for (Entry<Product, Integer> entry : mItemsList.entrySet()) {
			Product p = entry.getKey();
			Integer q = entry.getValue();
			itemList.add(new Item(p, q));
		}
		return itemList;
	}
	*/

	public float getTotal() {
		return mTotal;
	}
	
	public int getCustomerID() {
		return mCustomerID;
	}
	
	public int getId(){
		return mOrderID;
	}
	
	/*
	public int getQuantityOfProduct(Product p){
		if(!mItemsList.containsKey(p))
			return 0;
		if(!mReturnList.containsKey(p))
			return mItemsList.get(p);
		return mItemsList.get(p)-mReturnList.get(p);
	}
	
	
	public List<Item> getReturnList(){
		List<Item> itemList = new LinkedList<>();
		for (Entry<Product, Integer> entry : mItemsList.entrySet()) {
			Product p = entry.getKey();
			Integer q = entry.getValue();
			itemList.add(new Item(p, q));
		}
		for (Entry<Product, Integer> entry : mReturnList.entrySet()) {
			Product p = entry.getKey();
			Integer q = entry.getValue();
			itemList.add(new Item(p, -q));
		}
		return itemList;
	}
	*/
	
	public double getReturnPrice(){
		return (mTotal*1.06) - ((mTotal - mReturnTotal) * 1.06);
	}
	
	public void returnProduct(int product, int quantity){
		int pos = getProdPosition(mItemList, productID);
		int q;
		if(pos<0) {
			q = quantity;
		} else {
			q = mItemList.get(pos).getQuantity() + quantity;
		}
		mItemList.get(pos).setQuantity(q);
	
		mReturnTotal = mReturnTotal + mItemList.get(pos).getmPrice() * q;
	
	}
	
	public void editReturn(Product product, int quantity) {
		Integer q = mReturnList.get(product);
		if (q != null) {
			mReturnList.put(product, quantity);
			mReturnTotal = mReturnTotal - product.getUnitPrice() * (q - quantity);
		}
	}

	public void removeReturn(Product product) {
		Integer q = mReturnList.get(product);
		if (q != null) {
			mReturnList.remove(product);
			mReturnTotal = mReturnTotal - product.getUnitPrice() * q;
		}
	}
	
	public void saveReturn(){
		for (Entry<Product, Integer> entry : mReturnList.entrySet()) {
			Product p = entry.getKey();
			Integer q = entry.getValue();
			if(q == mItemsList.get(p))
				removeItem(p);
			else
				editItem(p, mItemsList.get(p) - q);
		}
		mReturnList = new HashMap<Product, Integer>();
		mReturnTotal = 0;
	}

	public ArrayList<Item> getmItemList() {
		return mItemList;
	}

	public void setmItemList(ArrayList<Item> mItemList) {
		this.mItemList = mItemList;
	}
	
	public void setmReturnItemList(ArrayList<Item> mReturnList) {
		this.mReturnList = mReturnList;
	}
	
	
}
