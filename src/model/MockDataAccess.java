package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MockDataAccess implements IDataAccess {

	private Map<Integer, Product> mProducts;
	private Map<Integer, Employee> mEmployees;
	Employee currentUser = null;

	public MockDataAccess() {
		mProducts = new HashMap<>();
		mProducts.put(1, new Product(1, "Apple", 3.99));
		mProducts.put(2, new Product(2, "Apple Pie", 6.99));
		mProducts.put(3, new Product(3, "Apple Juice", 2.99));
		mProducts.put(4, new Product(4, "Apple Watch", 399.99));
		

		mEmployees = new HashMap<>();
		mEmployees.put(1, new Employee(1,"Jordan Knudsen", "jordan", "password", false));
		mEmployees.put(2, new Employee(2,"Nicholas Guyaux", "nick", "password", false));
		mEmployees.put(3, new Employee(3,"Mr. Smith", "smith", "password", true));
	}

	public Product getProductById(int id) {
		return mProducts.get(id);
	}

	public Product[] getInventoryList(String search) {
		Product[] list;
		if(search != ""){
			if(mProducts.get(Integer.parseInt(search)) == null)
				return null;
			list = new Product[1];
			list[0] = mProducts.get(Integer.parseInt(search));
			return list;			
		}
		if(mProducts.size() < 10)
			list = new Product[mProducts.size()];
		else
			list = new Product[10];
		for(int i = 0;i<list.length;i++){
			list[i] = (Product) mProducts.values().toArray()[i];
		}
		return list;
	}

	@Override
	public void removeProductById(int id) {
		mProducts.remove(id);
	}

	@Override
	public void modifyProductById(int id, Product p) {
		mProducts.put(id, p);
	}

	@Override
	public void addProduct(Product p) {
		mProducts.put(p.getId(),p);
	}

	@Override
	public boolean login(String user, String pass) {
		Iterator<Employee> itr = mEmployees.values().iterator();
		while(itr.hasNext()){
			Employee next = itr.next();
			if(next.getUsername().equals(user) && next.getPassword().equals(pass)){
				currentUser = next;
			}
		}
		if(currentUser != null)
			return true;
		else
			return false;
	}

	@Override
	public Employee getCurrentUser() {
		return currentUser;
	}

	@Override
	public void logOut() {
		currentUser = null;		
	}

	@Override
	public Employee[] getEmployeeList() {
		return  mEmployees.values().toArray(new Employee[0]);
	}
}