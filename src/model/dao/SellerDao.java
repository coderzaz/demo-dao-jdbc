package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;

public interface SellerDao {
	void insert(Seller obj);
	void update(Seller obj);
	void deleteById(Seller  id);
	
	Seller findById(Integer id);//consulta no Bd um Objeto com este id se existir retorna true ou null
	List<Seller> findAll();//Retorna todos os vendedores
	List<Seller> findByDepartment(Department department);//assinatura do método,bscar por departamento
}
