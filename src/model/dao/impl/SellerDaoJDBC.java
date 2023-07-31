package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
	
	private Connection conn;
	//força a injeçãode dependencia
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Seller obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);//pega o valor do id gerado na posição 1,primeira coluna das chaves
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ? "); // Id do vendedor
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());
			
			st.executeUpdate();
					
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");
			st.setInt(1, id);
			st.executeUpdate();
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}
/*
	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
				    + "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");
			
			st.setInt(1,id); //recebe o id como parâmetro da função
			rs = st.executeQuery(); //rs recebe a consulta sql
			
			//implemetação para retornar um vendedor por id
			if(rs.next()) { //se for true
				Department dep = new  Department(); //instânciamos um departamento
				dep.setId(rs.getInt("DepartmentId")); //setamos os valores dele
				dep.setName(rs.getString("DepName"));
				
				Seller obj = new Seller();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
				obj.setEmail(rs.getString("Email"));
				obj.setBaseSalary(rs.getDouble("BaseSalary"));
				obj.setBirthDate(rs.getDate("BirthDate"));
				obj.setDepartment(dep); //dep é o objeto montado inteiro com toda a tabela
				//ciramos o Objeto seller com todos os atributos definidos 
				return obj;
			}
			return null; //caso a consulta seja nula
		}
		catch (SQLException e) { //cria uma exceção 
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}
*/	
	
	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
				    + "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");
			
			st.setInt(1,id); //recebe o id como parâmetro da função
			rs = st.executeQuery(); //rs recebe a consulta sql
			
			//implemetação para retornar um vendedor por id
			if(rs.next()) { //se for true
				Department dep = instanciateDepartment(rs); //instâncio Department
				
				Seller obj = instanciateDepartment(rs,dep); //instâncio Seller
				
				return obj; //retorno seller
			}
			return null; //caso a consulta seja nula
		}
		catch (SQLException e) { //cria uma exceção 
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}
	

	private Seller instanciateDepartment(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep); 
		
		return obj;
		
}

	private Department instanciateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId")); 
		dep.setName(rs.getString("DepName"));
		return dep;
}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
				    + "ON seller.DepartmentId = department.Id "
					+ "ORDER BY Name");
			
		
			rs = st.executeQuery(); //rs recebe a consulta sql
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			//neste map vazio vou quardar qualquer departamento que eu instânciar
			
			//implemetação para retornar um vendedor por id
			while(rs.next()) { //se for true,toda vez que passar numa linha do resultset faz o teste
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				//buscando dentro do map um id, se não existir retorna nulo ai sim vai instânciar um departamento
				if(dep == null) { //instânciação esta sendo feita dentro do if
					dep = instanciateDepartment(rs);//instância um departamneto apartir do resultset
					//salva  o departamento no map para que no próximo vez possa verificar se já existe
					map.put(rs.getInt("DepartmentId"), dep);
					//o valor da chave rs.getInt e que é departamento que vou salvar o que estiver na varável dep
				}
						
				Seller obj = instanciateDepartment(rs,dep); //instâncio Seller
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) { //cria uma exceção 
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
				    + "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name");
			
			st.setInt(1,department.getId()); //recebe o id como parâmetro da função
			rs = st.executeQuery(); //rs recebe a consulta sql
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			//neste map vazio vou quardar qualquer departamento que eu instânciar
			
			//implemetação para retornar um vendedor por id
			while(rs.next()) { //se for true
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				//buscando dentro do map um id, se não existir retorna nulo ai sim vai instânciar um departamento
				if(dep == null) { //instânciação esta sendo feita dentro do if
					dep = instanciateDepartment(rs);//instância um departamneto apartir do resultset
					//salva  o departamento no map para que no próximo vez possa verificar se já existe
					map.put(rs.getInt("DepartmentId"), dep);
					//o valor da chave rs.getInt e que é departamento que vou salvar o que estiver na varável dep
				}
						
				Seller obj = instanciateDepartment(rs,dep); //instâncio Seller
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) { //cria uma exceção 
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}

}
