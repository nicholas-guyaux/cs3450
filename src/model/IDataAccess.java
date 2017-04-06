package model;

public interface IDataAccess {
	public Product getProductById(int id);
	public Product[] getInventoryList(String search);
	public void removeProductById(int id);
	public void modifyProductById(int id, Product p);
	public void addProduct(Product p);
	
	public boolean login(String user,String pass);
	public Employee getCurrentUser();
	public void logOut();
	
	public Employee[] getEmployeeList();
}