package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		/*
		Department obj = new Department(1,"Books");
		
		Seller seller = new Seller(21,"Bob","bob@gmail.com",new Date(), 3000.0, obj);
		*/
		
		//instância o Objeto sellerDao
		SellerDao sellerDao = DaoFactory.createSellerDao();
		//objeto do tipo seller recebe de sellerDao.findById o código 3 que faz refêrencia ao vendedor Alex Gray
		
		System.out.println("=== Test 1: seller findById ===");
		
		Seller seller = sellerDao.findById(3);
		
		System.out.println(seller);
		
		System.out.println("\n=== Test 2: seller findByDepartment ===");
		Department department = new Department(2,null);
		List<Seller> list = sellerDao.findByDepartment(department);
		for(Seller obj: list) {
			System.out.println(obj);
		}
	}

}
