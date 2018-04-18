/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.util.List;
import javax.persistence.Persistence;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Professor;
import modelo.Atendimento;
import persistencia.*;
import persistencia.exceptions.NonexistentEntityException;
import util.Util;

/**
 *
 * @author gustavo
 */
@WebServlet(name = "ProfessorServlet", urlPatterns = {"/ProfessorServlet"})
public class ProfessorServlet extends HttpServlet {

    private static ProfessorDAO professorDao = new ProfessorDAO(Persistence.createEntityManagerFactory("SistemaDeAtendimentoPU"));
    private static AtendimentoDAO atendimentoDao = new AtendimentoDAO(Persistence.createEntityManagerFactory("SistemaDeAtendimentoPU"));
    private HttpServletRequest request;
    private HttpServletResponse response;
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.request = request;
        this.response = response;
        String acao = request.getParameter("acao");
        switch(acao) {
            case "editar":
                editar();
                break;
            case "listar":
                listar();
                break;
            case "salvar":
                salvar();
                break;
            case "excluir":
                excluir();
                break;
            default:
                throw new ServletException("Ação '" + acao + "' inválida.");
        }
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void listar() throws ServletException, IOException {
        request.setAttribute("professores", professorDao.listar());
        RequestDispatcher rd = request.getRequestDispatcher("/Professores/Index.jsp");
        rd.forward(request, response);
    }

    private void editar() throws ServletException, IOException {
        Integer id = Util.stringToInteger(request.getParameter("id"));
        Professor professor = null;
        List<Atendimento> atendimentos = null;
        
        if (id != null) {
            professor = professorDao.buscar(id);
            atendimentos = atendimentoDao.listarPorProfessor(id);
        }
        
        request.setAttribute("professor", professor);
        request.setAttribute("atendimentos", atendimentos);
        RequestDispatcher rd = request.getRequestDispatcher("/Professores/Professor.jsp");
        rd.forward(request, response);
    }
    
    private void salvar() throws ServletException, IOException {
        Integer id = Util.stringToInteger(request.getParameter("id"));
        String nome = request.getParameter("nome");
        String siape = request.getParameter("siape");
        
        Integer ids[] = Util.stringToInteger(request.getParameterValues("atendimentos"));
        List<Atendimento> atendimentos = atendimentoDao.listar(ids);
        
        Professor p = new Professor();
        p.setId(id);
        p.setNome(nome);
        p.setSiape(siape);
        p.setAtendimentos(atendimentos);
        
        try {
            if (p.getId() != null)
                professorDao.editar(p);
            else
                professorDao.criar(p);
        } catch (Exception ex) {
            log("Erro editando professor.", ex);
        }
        
        RequestDispatcher rd = request.getRequestDispatcher("./ProfessorServlet?acao=listar");
        rd.forward(request, response);
    }
    
    private void excluir() throws ServletException, IOException {
        try {
            professorDao.excluir(Util.stringToInteger(request.getParameter("id")));
        } catch (NonexistentEntityException e) {
            throw new ServletException("Erro excluindo professor.", e);
        }
        RequestDispatcher rd = request.getRequestDispatcher("./ProfessorServlet?acao=listar");
        rd.forward(request, response);
    }
    
}
