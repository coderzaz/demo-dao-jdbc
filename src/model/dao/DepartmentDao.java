package model.dao;

import java.util.List;

import model.entities.Department;

public interface DepartmentDao {
	void insert(Department obj);
	void update(Department obj);
	void deleteById(Integer id);
	
	Department findById(Integer id);//consulta no Bd um Objeto com este id se existir retorna true ou null
	List<Department> findAll(); //Retorna todos os departamentos
	
}
