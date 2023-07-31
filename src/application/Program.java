package application;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
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
		
		System.out.println("\n=== Test 3: seller findByAll ===");
		
		List<Seller> list2 = sellerDao.findAll();
		for(Seller obj: list2) {
			System.out.println(obj);
		}
		
		System.out.println("\n=== Test 4: seller insert ===");
		Seller newseller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
		sellerDao.insert(newseller);
		System.out.println("Inserted! New id = "+newseller.getId() );
		
		System.out.println("\n=== Test 5: seller update ===");
		seller = sellerDao.findById(1);
		System.out.println(seller);
		seller.setName("Martha Waine");
		sellerDao.update(seller);
		System.out.println("Update complete");
		
		
		System.out.println("\n=== Test 6: seller delete ===");
		System.out.println("Enter id for delete test: ");
		int id = sc.nextInt();
		sellerDao.deleteById(id);
		System.out.println("Delete completed");
		
		sc.close();
			
	}

}
