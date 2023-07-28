package model.dao;

import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
//Esta classe vai ter operções estática para instânciar os Daos
	//retorna um tipo da interface
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC();//internamente instãncia uma implemetação
	}
}
