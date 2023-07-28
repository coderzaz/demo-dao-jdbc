package model.dao;

import db.DB;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
//Esta classe vai ter operções estática para instânciar os Daos
	//retorna um tipo da interface
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());//internamente instãncia uma implemetação,obrigado apassar uma conexão como argumento
	}
}
