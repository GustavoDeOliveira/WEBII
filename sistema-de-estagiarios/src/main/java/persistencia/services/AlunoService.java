package persistencia.services;

import persistencia.dao.AlunoDAO;

public class AlunoService {
    
    private AlunoDAO alunoDAO;

    public AlunoDAO getAlunoDAO() {
        return alunoDAO;
    }

    public void setAlunoDAO(AlunoDAO alunoDAO) {
        this.alunoDAO = alunoDAO;
    }

    public AlunoService() {
        this.alunoDAO = new AlunoDAO();
    }
    
    public List<Aluno> listar() {
        return alunoDAO.listar();
    }
    
}
